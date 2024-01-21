//@formatter:off
/*
 * IntermediateExpressionBuilder
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

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import de.calamanari.pk.ohbf.bloombox.BbxMessage;
import de.calamanari.pk.ohbf.bloombox.bbq.BbqParser.AndBBQContext;
import de.calamanari.pk.ohbf.bloombox.bbq.BbqParser.AndExpressionContext;
import de.calamanari.pk.ohbf.bloombox.bbq.BbqParser.ArgNameContext;
import de.calamanari.pk.ohbf.bloombox.bbq.BbqParser.ArgValueContext;
import de.calamanari.pk.ohbf.bloombox.bbq.BbqParser.BbqDetailsContext;
import de.calamanari.pk.ohbf.bloombox.bbq.BbqParser.BracedExpressionContext;
import de.calamanari.pk.ohbf.bloombox.bbq.BbqParser.CmpEqualsContext;
import de.calamanari.pk.ohbf.bloombox.bbq.BbqParser.CmpInContext;
import de.calamanari.pk.ohbf.bloombox.bbq.BbqParser.CmpNotEqualsContext;
import de.calamanari.pk.ohbf.bloombox.bbq.BbqParser.CmpNotInContext;
import de.calamanari.pk.ohbf.bloombox.bbq.BbqParser.LowerBoundContext;
import de.calamanari.pk.ohbf.bloombox.bbq.BbqParser.MinMaxExpressionContext;
import de.calamanari.pk.ohbf.bloombox.bbq.BbqParser.NotExpressionContext;
import de.calamanari.pk.ohbf.bloombox.bbq.BbqParser.OrBBQContext;
import de.calamanari.pk.ohbf.bloombox.bbq.BbqParser.OrExpressionContext;
import de.calamanari.pk.ohbf.bloombox.bbq.BbqParser.QueryContext;
import de.calamanari.pk.ohbf.bloombox.bbq.BbqParser.UpperBoundContext;
import de.calamanari.pk.ohbf.bloombox.bbq.BoundedOr.InvalidBoundsException;

/**
 * The {@link IntermediateExpressionBuilder} uses ANTLR to transform a given textual BBQ-query (basic query) into a first tree of
 * {@link IntermediateExpression}s.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
// Suppressed Sonar-warning about "monster class", language-related methods kept together by intention
@SuppressWarnings("java:S6539")
public class IntermediateExpressionBuilder extends BbqBaseListener {

    // This builder creates a raw intermediate expression
    // In this step I avoid any optimization to keep the implementation simple
    // It operates on a stack, where every ENTER of a relevant (complex) parser rule opens a new level
    // and the combining logic all happens in the corresponding EXIT.

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

    @Override
    public void enterArgName(ArgNameContext ctx) {
        stack.peek().argName = ctx.getText();
    }

    @Override
    public void enterArgValue(ArgValueContext ctx) {
        stack.peek().values.add(ctx.getText());
    }

    @Override
    public void enterCmpEquals(CmpEqualsContext ctx) {
        stack.push(new DataCollector(ElementType.EQUALS));
    }

    @Override
    public void exitCmpEquals(CmpEqualsContext ctx) {
        DataCollector currentCollector = stack.pop();
        IntermediateEquals expression = new IntermediateEquals(currentCollector.argName, currentCollector.values.get(0));
        stack.peek().childExpressions.add(expression);
    }

    @Override
    public void enterCmpNotEquals(CmpNotEqualsContext ctx) {
        stack.push(new DataCollector(ElementType.NOT_EQUALS));
    }

    @Override
    public void exitCmpNotEquals(CmpNotEqualsContext ctx) {
        DataCollector currentCollector = stack.pop();
        IntermediateNotEquals expression = new IntermediateNotEquals(currentCollector.argName, currentCollector.values.get(0));
        stack.peek().childExpressions.add(expression);
    }

    @Override
    public void enterCmpIn(CmpInContext ctx) {
        stack.push(new DataCollector(ElementType.IN));
    }

    @Override
    public void exitCmpIn(CmpInContext ctx) {
        DataCollector currentCollector = stack.pop();

        if (currentCollector.lowerBound <= 0 && currentCollector.upperBound <= 0) {
            IntermediateOrExpression expression = new IntermediateOrExpression();
            for (String value : currentCollector.values) {
                expression.getSubExpressionList().add(new IntermediateEquals(currentCollector.argName, value));
            }
            stack.peek().childExpressions.add(expression);
        }
        else {
            List<IntermediateExpression> conditions = currentCollector.values.stream()
                    .map(value -> (IntermediateExpression) new IntermediateEquals(currentCollector.argName, value)).toList();
            stack.peek().childExpressions.add(createBoundedOr(conditions, currentCollector.lowerBound, currentCollector.upperBound, ctx.getText()));
        }
    }

    @Override
    public void enterCmpNotIn(CmpNotInContext ctx) {
        stack.push(new DataCollector(ElementType.NOT_IN));
    }

    @Override
    public void exitCmpNotIn(CmpNotInContext ctx) {
        DataCollector currentCollector = stack.pop();

        if (currentCollector.lowerBound <= 0 && currentCollector.upperBound <= 0) {

            IntermediateAndExpression expression = new IntermediateAndExpression();
            for (String value : currentCollector.values) {
                expression.getSubExpressionList().add(new IntermediateNotEquals(currentCollector.argName, value));
            }
            stack.peek().childExpressions.add(expression);
        }
        else {
            List<IntermediateExpression> conditions = currentCollector.values.stream()
                    .map(value -> (IntermediateExpression) new IntermediateNotEquals(currentCollector.argName, value)).toList();
            stack.peek().childExpressions.add(createBoundedOr(conditions, currentCollector.lowerBound, currentCollector.upperBound, ctx.getText()));
        }
    }

    @Override
    public void enterOrExpression(OrExpressionContext ctx) {
        stack.push(new DataCollector(ElementType.OR));
    }

    @Override
    public void exitOrExpression(OrExpressionContext ctx) {
        DataCollector currentCollector = stack.pop();

        IntermediateOrExpression expression = new IntermediateOrExpression();
        expression.getSubExpressionList().addAll(currentCollector.childExpressions);
        stack.peek().childExpressions.add(expression);

    }

    @Override
    public void enterOrBBQ(OrBBQContext ctx) {
        stack.push(new DataCollector(ElementType.OR));
    }

    @Override
    public void exitOrBBQ(OrBBQContext ctx) {
        DataCollector currentCollector = stack.pop();

        IntermediateOrExpression expression = new IntermediateOrExpression();
        expression.getSubExpressionList().addAll(currentCollector.childExpressions);
        stack.peek().childExpressions.add(expression);
    }

    @Override
    public void enterAndExpression(AndExpressionContext ctx) {
        stack.push(new DataCollector(ElementType.AND));
    }

    @Override
    public void exitAndExpression(AndExpressionContext ctx) {
        DataCollector currentCollector = stack.pop();

        IntermediateAndExpression expression = new IntermediateAndExpression();
        expression.getSubExpressionList().addAll(currentCollector.childExpressions);
        stack.peek().childExpressions.add(expression);
    }

    @Override
    public void enterAndBBQ(AndBBQContext ctx) {
        stack.push(new DataCollector(ElementType.AND));
    }

    @Override
    public void exitAndBBQ(AndBBQContext ctx) {
        DataCollector currentCollector = stack.pop();

        IntermediateAndExpression expression = new IntermediateAndExpression();
        expression.getSubExpressionList().addAll(currentCollector.childExpressions);
        stack.peek().childExpressions.add(expression);
    }

    @Override
    public void enterNotExpression(NotExpressionContext ctx) {
        stack.push(new DataCollector(ElementType.BRACE));
    }

    @Override
    public void exitNotExpression(NotExpressionContext ctx) {
        DataCollector currentCollector = stack.pop();

        IntermediateNotExpression expression = new IntermediateNotExpression();
        expression.getSubExpressionList().addAll(currentCollector.childExpressions);
        stack.peek().childExpressions.add(expression);
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
        stack.push(new DataCollector(ElementType.BRACE));
        stack.peek().requiresOpenExtraBraces = true;
    }

    @Override
    public void exitMinMaxExpression(MinMaxExpressionContext ctx) {
        DataCollector currentCollector = stack.pop();
        stack.peek().childExpressions
                .add(createBoundedOr(currentCollector.childExpressions, currentCollector.lowerBound, currentCollector.upperBound, ctx.getText()));
    }

    @Override
    public void enterBbqDetails(BbqDetailsContext ctx) {
        if (stack.peek().requiresOpenExtraBraces) {
            stack.push(new DataCollector(ElementType.BRACE));
            stack.peek().requiresCloseExtraBraces = true;
        }
    }

    @Override
    public void exitBbqDetails(BbqDetailsContext ctx) {
        if (stack.peek().requiresCloseExtraBraces) {
            DataCollector currentCollector = stack.pop();
            IntermediateBraces expression = new IntermediateBraces();
            expression.getSubExpressionList().addAll(currentCollector.childExpressions);
            stack.peek().childExpressions.add(expression);
        }
    }

    @Override
    public void enterBracedExpression(BracedExpressionContext ctx) {
        stack.push(new DataCollector(ElementType.BRACE));
    }

    @Override
    public void exitBracedExpression(BracedExpressionContext ctx) {
        DataCollector currentCollector = stack.pop();

        IntermediateBraces expression = new IntermediateBraces();
        expression.getSubExpressionList().addAll(currentCollector.childExpressions);
        stack.peek().childExpressions.add(expression);
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
     * @return root intermediate expression of the not yet optimized expression
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
            if (root != null && !root.childExpressions.isEmpty()) {
                if (root.childExpressions.size() > 1) {
                    IntermediateBraces expression = new IntermediateBraces();
                    expression.getSubExpressionList().addAll(root.childExpressions);
                    res = expression;
                }
                else {
                    res = root.childExpressions.get(0);
                }
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
     * Creates for minmax and (not) in clauses a bounded expression
     * 
     * @param conditions elements
     * @param lowerBound constraint
     * @param upperBound constraint
     * @param debugInfo to narrow a problem in error message
     * @return bounded expression
     */
    private IntermediateExpression createBoundedOr(List<IntermediateExpression> conditions, int lowerBound, int upperBound, String debugInfo) {
        IntermediateExpression res = null;
        try {
            // @formatter:off
            res = BoundedOr.of(conditions)
                             .min(lowerBound)
                             .max(upperBound)
                             .build();
            // @formatter:on
        }
        catch (InvalidBoundsException ex) {
            String msg = ex.getMessage() + String.format(" Problematic expression: '%s'.", debugInfo);
            throw new InvalidBoundsException(BbxMessage.ERR_QUERY_SYNTAX_BOUNDS.format(msg));
        }
        return res;
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

        ROOT, BRACE, EQUALS, NOT_EQUALS, IN, NOT_IN, AND, OR

    }

    /**
     * Container to keep some elements while parsing the same level, e.g. we receive multiple calls when parsing <code>color=blue</code>, the collector
     * associates them
     */
    private static class DataCollector {

        /**
         * @param elementType type of element we are inside
         */
        DataCollector(ElementType elementType) {
            this.elementType = elementType;
        }

        /**
         * argument name
         */
        String argName = null;

        /**
         * list of collected values when processing an IN-condition
         */
        List<String> values = new ArrayList<>();

        /**
         * type of element we are currently parsing
         */
        ElementType elementType = null;

        /**
         * member expressions we found inside this expression
         */
        List<IntermediateExpression> childExpressions = new ArrayList<>();

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

    }

}
