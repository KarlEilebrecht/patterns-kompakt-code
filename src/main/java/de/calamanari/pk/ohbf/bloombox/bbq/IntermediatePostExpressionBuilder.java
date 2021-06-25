//@formatter:off
/*
 * IntermediatePostExpressionBuilder
 * Code-Beispiel zum Buch Patterns Kompakt, Verlag Springer Vieweg
 * Copyright 2014 Karl Eilebrecht
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"):
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
//@formatter:on

package de.calamanari.pk.ohbf.bloombox.bbq;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import de.calamanari.pk.ohbf.bloombox.BbxMessage;
import de.calamanari.pk.ohbf.bloombox.bbq.BoundedOr.InvalidBoundsException;
import de.calamanari.pk.ohbf.bloombox.bbq.PostBbqParser.BracedExpressionContext;
import de.calamanari.pk.ohbf.bloombox.bbq.PostBbqParser.ExpressionContext;
import de.calamanari.pk.ohbf.bloombox.bbq.PostBbqParser.IntersectExpressionContext;
import de.calamanari.pk.ohbf.bloombox.bbq.PostBbqParser.LowerBoundContext;
import de.calamanari.pk.ohbf.bloombox.bbq.PostBbqParser.MinMaxExpressionContext;
import de.calamanari.pk.ohbf.bloombox.bbq.PostBbqParser.MinusExpressionContext;
import de.calamanari.pk.ohbf.bloombox.bbq.PostBbqParser.QueryContext;
import de.calamanari.pk.ohbf.bloombox.bbq.PostBbqParser.SourceContext;
import de.calamanari.pk.ohbf.bloombox.bbq.PostBbqParser.UnionExpressionContext;
import de.calamanari.pk.ohbf.bloombox.bbq.PostBbqParser.UpperBoundContext;

/**
 * The {@link IntermediateExpressionBuilder} uses ANTLR to transform a given textual BBQ-query (post query) into a first tree of
 * {@link IntermediateExpression}s.
 * <p>
 * <b>Note:</b> I decided to introduce an independent grammar for post queries to keep each grammar (and related parsing) simple. This way I could clearly
 * separate semantics, which also helps to control the optimization and execution leading to better result consistency between related queries.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class IntermediatePostExpressionBuilder extends PostBbqBaseListener {

    /**
     * Entry point, root collector
     */
    private DataCollector root = null;

    /**
     * Stack of data collectors to reflect the recursive structure incoming from the parser
     */
    private final Deque<DataCollector> stack = new ArrayDeque<>();

    /**
     * Any error that was detected during parsing to be able to throw an exception at the end
     */
    private ExpressionBuilderException error = null;

    /**
     * OBSERVER of the ANTLER processing
     */
    private final ANTLRErrorListener errorListener = new ErrorListener();

    /**
     * reference map, only known expressions can be referenced by name
     */
    private Map<String, Long> namedExpressions = null;

    /**
     * @param namedExpressions reference map, only known expressions can be referenced by name
     */
    public IntermediatePostExpressionBuilder(Map<String, Long> namedExpressions) {
        this.namedExpressions = namedExpressions;
    }

    @Override
    public void enterSource(SourceContext ctx) {
        String refName = ctx.getText();
        Long refExpressionId = namedExpressions.get(refName);
        Long parentExpressionId = lookupParentExpressionIdForSubQuery(refName);
        if (refExpressionId == null || parentExpressionId == null) {
            int line = ctx.getStart().getLine();
            int charPos = ctx.getStart().getCharPositionInLine();
            this.errorListener.syntaxError(null, refName, line, charPos,
                    BbxMessage.ERR_INVALID_QUERY_REFERENCE.format(String.format("Unable to resolve named query '%s'.", refName)), null);
        }
        else {
            DataCollector wrapper = new DataCollector(ElementType.SOURCE);
            wrapper.expression = new IntermediateReferenceExpression(refName, refExpressionId, parentExpressionId);
            stack.peek().childCollectors.add(wrapper);
        }
    }

    /**
     * @param refName total ref name
     * @return the parent expressions id or 0 to indicate no parent (valid) or null to indicate an error (invalid)
     */
    private Long lookupParentExpressionIdForSubQuery(String refName) {
        Long res = 0L;
        int endOfParentRefNamePos = refName.indexOf('.');
        if (endOfParentRefNamePos > 0) {
            String parentRefName = refName.substring(0, endOfParentRefNamePos);
            res = namedExpressions.get(parentRefName);
        }
        return res;
    }

    @Override
    public void enterUnionExpression(UnionExpressionContext ctx) {
        DataCollector nested = new DataCollector(ElementType.UNION);
        stack.peek().childCollectors.add(nested);
        stack.push(nested);
    }

    @Override
    public void exitUnionExpression(UnionExpressionContext ctx) {
        stack.pop();
    }

    @Override
    public void enterIntersectExpression(IntersectExpressionContext ctx) {
        DataCollector nested = new DataCollector(ElementType.INTERSECT);
        stack.peek().childCollectors.add(nested);
        stack.push(nested);
    }

    @Override
    public void exitIntersectExpression(IntersectExpressionContext ctx) {
        stack.pop();
    }

    @Override
    public void enterMinusExpression(MinusExpressionContext ctx) {
        DataCollector nested = new DataCollector(ElementType.MINUS);
        stack.peek().childCollectors.add(nested);
        stack.push(nested);
    }

    @Override
    public void exitMinusExpression(MinusExpressionContext ctx) {
        stack.pop();
    }

    @Override
    public void enterBracedExpression(BracedExpressionContext ctx) {
        DataCollector nested = new DataCollector(ElementType.BRACE);
        stack.peek().childCollectors.add(nested);
        stack.push(nested);
    }

    @Override
    public void exitBracedExpression(BracedExpressionContext ctx) {
        stack.pop();
    }

    @Override
    public void enterLowerBound(LowerBoundContext ctx) {
        stack.peek().lowerBound = BoundedOr.parseBoundValue(ctx.getText(), ctx.getParent().getText());
    }

    @Override
    public void enterUpperBound(UpperBoundContext ctx) {
        stack.peek().upperBound = BoundedOr.parseBoundValue(ctx.getText(), ctx.getParent().getText());
    }

    @Override
    public void enterMinMaxExpression(MinMaxExpressionContext ctx) {
        DataCollector nested = new DataCollector(ElementType.BRACE);
        nested.requiresOpenExtraBraces = true;
        stack.peek().childCollectors.add(nested);
        stack.push(nested);
    }

    @Override
    public void exitMinMaxExpression(MinMaxExpressionContext ctx) {
        DataCollector minMaxCollector = stack.pop();
        List<IntermediateExpression> expressions = minMaxCollector.childCollectors.stream().map(DataCollector::createExpression).collect(Collectors.toList());
        try {
            // @formatter:off
            minMaxCollector.expression = BoundedOr.of(expressions)
                     .min(minMaxCollector.lowerBound)
                     .max(minMaxCollector.upperBound)
                     .build();
            // @formatter:on
        }
        catch (InvalidBoundsException ex) {
            String msg = ex.getMessage() + String.format(" Problematic expression: '%s'.", ctx.getText());
            throw new InvalidBoundsException(BbxMessage.ERR_QUERY_SYNTAX_BOUNDS.format(msg));
        }
    }

    @Override
    public void enterExpression(ExpressionContext ctx) {
        if (stack.peek().requiresOpenExtraBraces) {
            DataCollector nested = new DataCollector(ElementType.BRACE);
            nested.requiresCloseExtraBraces = true;
            stack.peek().childCollectors.add(nested);
            stack.push(nested);
        }
    }

    @Override
    public void exitExpression(ExpressionContext ctx) {
        if (stack.peek().requiresCloseExtraBraces) {
            stack.pop();
        }
    }

    @Override
    public void enterQuery(QueryContext ctx) {
        this.root = new DataCollector(ElementType.ROOT);
        stack.clear();
        stack.push(root);
    }

    @Override
    public void exitQuery(QueryContext ctx) {
        if (stack.peek() != root) {
            throw new IllegalStateException("Unexpected element when end of query (root) was expected: " + root.elementType);
        }
        stack.pop();
    }

    /**
     * Method to compose and fetch the BUILDER's result
     * 
     * @return root intermediate expression
     * @throws ExpressionBuilderException on any error
     */
    public IntermediateExpression getResult() {
        IntermediateExpression res = null;
        if (error != null) {
            ExpressionBuilderException toThrow = error;
            error = null;
            root = null;
            throw toThrow;
        }
        else {
            if (root != null) {
                res = root.createExpression();
            }
        }
        error = null;
        root = null;
        return res;
    }

    /**
     * @return ANTLR observer
     */
    public ANTLRErrorListener getErrorListener() {
        return this.errorListener;
    }

    /**
     * OBSERVER of the ANTLER processing, tracks issues and collects messages
     */
    private class ErrorListener extends BaseErrorListener {

        @Override
        public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {

            if (error == null) {
                error = new ExpressionBuilderException(line, charPositionInLine, String.format("%nline %d:%d ", line, charPositionInLine) + msg, e);
            }
            else {
                error = new ExpressionBuilderException(line, charPositionInLine,
                        error.getMessage() + String.format("%nline %d:%d ", line, charPositionInLine) + msg, error.getCause() == null ? e : error.getCause());
            }
        }
    }

    /**
     * Types of detected elements (we are currently inside) during the parsing phase
     */
    private enum ElementType {

        ROOT, BRACE, UNION, INTERSECT, MINUS, SOURCE;

    }

    /**
     * Container to keep some elements while parsing the same level, the collector associates information
     */
    private static class DataCollector {

        /**
         * @param elementType type of element we are currently inside during parsing
         */
        DataCollector(ElementType elementType) {
            this.elementType = elementType;
        }

        /**
         * type of element we are currently inside during parsing
         */
        ElementType elementType = null;

        /**
         * expression found on this level
         */
        IntermediateExpression expression = null;

        /**
         * child collectors (build tree while walking through the stack)
         */
        List<DataCollector> childCollectors = new ArrayList<>();

        /**
         * minmax constraint
         */
        int lowerBound = 0;

        /**
         * minmax constraint
         */
        int upperBound = 0;

        /**
         * in case of MINMAX we need extra braces to keep comma-separated expressions intact
         */
        boolean requiresOpenExtraBraces = false;

        /**
         * MINMAX: close the extra braces
         */
        boolean requiresCloseExtraBraces = false;

        /**
         * @return intermediate expression representing the collected data or combining the expressions of all child collectors
         */
        IntermediateExpression createExpression() {
            IntermediateExpression res = null;
            if (expression != null) {
                res = expression;
            }
            else if (!childCollectors.isEmpty()) {
                res = createExpressionFromChildCollectors();
            }
            if (res == null) {
                res = new IntermediateBraces();
            }
            return res;
        }

        /**
         * @return collected simple expression or expression created from one or more child collectors
         */
        private IntermediateExpression createExpressionFromChildCollectors() {
            IntermediateExpression res;
            if (childCollectors.size() > 1) {
                ElementType type = childCollectors.get(0).elementType;
                if (type == ElementType.SOURCE || type == ElementType.BRACE) {
                    type = childCollectors.get(1).elementType;
                }
                res = createCombinedExpressionFromMultipleChildCollectors(type);
            }
            else {
                // single child does not need combined expression wrapper
                res = childCollectors.get(0).createExpression();
            }
            return res;
        }

        /**
         * @param type the type of the combined expression, must not be called with empty child collectors or only a single element
         * @return expression representing multiple child collectors
         */
        private IntermediateExpression createCombinedExpressionFromMultipleChildCollectors(ElementType type) {
            IntermediateExpression res = null;
            if (type == ElementType.SOURCE || type == ElementType.BRACE) {
                res = new IntermediateBraces();
                for (DataCollector child : childCollectors) {
                    ((IntermediateCombinedExpression) res).getSubExpressionList().add(child.createExpression());
                }
            }
            else if (type == ElementType.UNION) {
                res = new IntermediateUnionExpression();
                for (DataCollector child : childCollectors) {
                    ((IntermediateCombinedExpression) res).getSubExpressionList().add(child.createExpression());
                }
            }
            else if (type == ElementType.INTERSECT) {
                res = new IntermediateIntersectExpression();
                for (DataCollector child : childCollectors) {
                    ((IntermediateCombinedExpression) res).getSubExpressionList().add(child.createExpression());
                }
            }
            else if (type == ElementType.MINUS) {
                res = new IntermediateMinusExpression();
                for (DataCollector child : childCollectors) {
                    ((IntermediateCombinedExpression) res).getSubExpressionList().add(child.createExpression());
                }
            }
            return res;
        }
    }
}
