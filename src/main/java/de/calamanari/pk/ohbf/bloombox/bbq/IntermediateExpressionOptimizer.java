//@formatter:off
/*
 * IntermediateExpressionOptimizer
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

import static de.calamanari.pk.util.LambdaSupportLoggerProxy.defer;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.util.LambdaSupportLoggerProxy;

/**
 * The {@link IntermediateExpressionOptimizer} takes an raw {@link IntermediateExpression} parsed from BBQ-language and tries to optimize it in several ways.
 * <p>
 * Post queries undergo a slightly different optimization than basic queries, as we treat (previously optimized) referenced queries as <i>opaque</i> for better
 * result consistency especially in conjunction with upscaling.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class IntermediateExpressionOptimizer {

    private static final Logger LOGGER = LambdaSupportLoggerProxy.wrap(LoggerFactory.getLogger(IntermediateExpressionOptimizer.class));

    /**
     * The infix notation causes left-hand orphans. This method includes them into the subsequent combined expression (and/or)
     * 
     * @param expression to be fixed
     */
    void convertToPrefixNotation(IntermediateExpression expression) {
        LOGGER.trace("convertToPrefixNotation BEFORE: \n{}", defer(() -> formatExpressionAsTree(expression)));
        if (!expression.getType().isSimple()) {
            List<IntermediateExpression> subExpressions = ((IntermediateCombinedExpression) expression).getSubExpressionList();
            subExpressions.forEach(this::convertToPrefixNotation);
            // Only use magnet rules if the surrounding expression is still untyped
            if (expression.getType() == IntermediateExpressionType.BRACES) {
                suckInOrphanFromTheLeft(subExpressions, IntermediateExpressionType.AND);
                suckInOrphanFromTheLeft(subExpressions, IntermediateExpressionType.OR);
            }
        }
        LOGGER.trace("convertToPrefixNotation AFTER: \n{}", defer(() -> formatExpressionAsTree(expression)));
    }

    /**
     * Eliminates problems caused by the infix notation and the way we parse it by detecting and including orphans.
     * 
     * @param subExpressions list of expressions
     * @param magneticType the expression type that wants to suck-in elements
     */
    private void suckInOrphanFromTheLeft(List<IntermediateExpression> subExpressions, IntermediateExpressionType magneticType) {
        for (int i = subExpressions.size() - 1; i > 0; i--) {
            IntermediateExpression magnet = subExpressions.get(i);
            IntermediateExpression candidate = subExpressions.get(i - 1);
            if (magnet.getType() == magneticType && candidate.getType() != magneticType) {
                IntermediateCombinedExpression combi = (IntermediateCombinedExpression) magnet;
                combi.getSubExpressionList().add(0, candidate);
                subExpressions.remove(i - 1);
            }
        }
    }

    /**
     * Prefix-notation does not need any braces
     * 
     * @param expression existing expression
     */
    void removeAllBraces(IntermediateExpression expression) {
        LOGGER.trace("removeAllBraces BEFORE: \n{}", defer(() -> formatExpressionAsTree(expression)));
        if (!expression.getType().isSimple()) {
            List<IntermediateExpression> subExpressions = ((IntermediateCombinedExpression) expression).getSubExpressionList();
            subExpressions.forEach(this::removeAllBraces);
            removeBraceExpressions(subExpressions, expression.getType());
        }
        LOGGER.trace("removeAllBraces AFTER: \n{}", defer(() -> formatExpressionAsTree(expression)));
    }

    /**
     * Removes braces from the given expression list taking into account the parent type
     * 
     * @param subExpressions list of expressions
     * @param parentType type of the expression that holds these together
     */
    private void removeBraceExpressions(List<IntermediateExpression> subExpressions, IntermediateExpressionType parentType) {
        for (int i = subExpressions.size() - 1; i > -1; i--) {
            IntermediateExpression candidate = subExpressions.get(i);
            if (candidate.getType() == IntermediateExpressionType.BRACES) {

                List<IntermediateExpression> candidatesChildren = ((IntermediateCombinedExpression) candidate).getSubExpressionList();

                if (parentType == IntermediateExpressionType.BRACES || hasOnlyCombination(candidatesChildren, parentType)) {
                    // we can just drop the extra braces
                    subExpressions.remove(i);
                    subExpressions.addAll(i, candidatesChildren);
                }
                else if (parentType == IntermediateExpressionType.OR) {
                    // must replace the braces with an AND
                    IntermediateAndExpression newExpression = new IntermediateAndExpression();
                    newExpression.getSubExpressionList().addAll(candidatesChildren);
                    subExpressions.remove(i);
                    subExpressions.add(i, newExpression);
                }
                else if (parentType == IntermediateExpressionType.AND) {
                    // must replace the braces with an OR
                    IntermediateOrExpression newExpression = new IntermediateOrExpression();
                    newExpression.getSubExpressionList().addAll(candidatesChildren);
                    subExpressions.remove(i);
                    subExpressions.add(i, newExpression);
                }
            }
        }
    }

    /**
     * Determines if all the expression in the list have the same given filter type
     * 
     * @param subExpressions list of expressions
     * @param filterType type (AND, OR)
     * @return true if all have the given type
     */
    private boolean hasOnlyCombination(List<IntermediateExpression> subExpressions, IntermediateExpressionType filterType) {
        for (IntermediateExpression candidate : subExpressions) {
            if ((candidate.getType() == IntermediateExpressionType.AND || candidate.getType() == IntermediateExpressionType.OR)
                    && candidate.getType() != filterType) {
                LOGGER.trace("hasOnlyCombination : filterType={}: false", filterType);
                return false;
            }
        }
        return true;
    }

    /**
     * There may be two subsequent ANDs or two subsequent ORs, which can be combined, same for UNION, INTERSECT and MINUS
     * 
     * @param expression source expressions
     */
    void consolidateCombinedExpressions(IntermediateExpression expression) {
        LOGGER.trace("consolidateCombinedExpressions BEFORE: \n{}", defer(() -> formatExpressionAsTree(expression)));
        if (!expression.getType().isSimple()) {
            List<IntermediateExpression> subExpressions = ((IntermediateCombinedExpression) expression).getSubExpressionList();
            subExpressions.forEach(this::consolidateCombinedExpressions);
            if (expression.getType() != IntermediateExpressionType.NOT) {
                consolidateSubExpressionsOfSameType(subExpressions, expression.getType());
            }
        }
        LOGGER.trace("consolidateCombinedExpressions AFTER: \n{}", defer(() -> formatExpressionAsTree(expression)));
    }

    /**
     * combines subsequent expressions of the same type as the parent
     * 
     * @param subExpressions list of expressions
     * @param parentType type to combine
     */
    private void consolidateSubExpressionsOfSameType(List<IntermediateExpression> subExpressions, IntermediateExpressionType parentType) {

        for (int i = subExpressions.size() - 1; i > -1; i--) {

            IntermediateExpression candidate = subExpressions.get(i);

            if (candidate.getType() == parentType) {
                IntermediateCombinedExpression candidateCasted = (IntermediateCombinedExpression) candidate;
                subExpressions.remove(i);
                subExpressions.addAll(candidateCasted.getSubExpressionList());
            }

        }
    }

    /**
     * Removes nested negations recursively
     * 
     * @param expression candidate
     * @return the optimized expression
     */
    IntermediateExpression removeNestedNegations(IntermediateExpression expression) {
        final IntermediateExpression logExIn = expression;
        LOGGER.trace("removeNestedNegations BEFORE: \n{}", defer(() -> formatExpressionAsTree(logExIn)));
        if (!expression.getType().isSimple()) {
            List<IntermediateExpression> subExpressions = ((IntermediateCombinedExpression) expression).getSubExpressionList();
            for (int i = 0; i < subExpressions.size(); i++) {
                IntermediateExpression subExpression = subExpressions.get(i);
                IntermediateExpression optimizedSubExpression = removeNestedNegations(subExpression);
                if (optimizedSubExpression != subExpression) {
                    subExpressions.set(i, optimizedSubExpression);
                }
            }
            if (expression.getType() == IntermediateExpressionType.NOT) {
                expression = removeNestedNegation((IntermediateNotExpression) expression);
            }
        }

        final IntermediateExpression logExOut = expression;
        LOGGER.trace("removeNestedNegations AFTER: \n{}", defer(() -> formatExpressionAsTree(logExOut)));
        return expression;

    }

    /**
     * Removes nested negation
     * 
     * @param expression candidate
     * @return the optimized expression
     */
    IntermediateExpression removeNestedNegation(IntermediateNotExpression expression) {
        IntermediateExpression res = expression;
        List<IntermediateExpression> subExpressions = expression.getSubExpressionList();
        if (subExpressions.size() == 1) {
            if (subExpressions.get(0).getType() == IntermediateExpressionType.NOT) {
                List<IntermediateExpression> subSubExpressions = ((IntermediateCombinedExpression) subExpressions.get(0)).getSubExpressionList();
                if (subSubExpressions.size() == 1) {
                    res = subSubExpressions.get(0);
                }
            }
            else if (subExpressions.get(0).getType() == IntermediateExpressionType.NOT_EQUALS) {
                IntermediateNotEquals negated = (IntermediateNotEquals) subExpressions.get(0);
                res = new IntermediateEquals(negated.argName, negated.argValue);
            }
        }

        return res;
    }

    /**
     * Removes the remaining braces at the root (if any)
     * 
     * @param expression root expression
     * @return new root
     */
    IntermediateExpression convertRootBraces(IntermediateExpression expression) {
        final IntermediateExpression logExIn = expression;
        LOGGER.trace("convertRootBraces BEFORE: \n{}", defer(() -> formatExpressionAsTree(logExIn)));
        if (expression.getType() == IntermediateExpressionType.BRACES) {
            IntermediateCombinedExpression oldRoot = (IntermediateCombinedExpression) expression;
            if (!oldRoot.getSubExpressionList().isEmpty()) {

                if (oldRoot.getSubExpressionList().size() == 1) {
                    expression = oldRoot.getSubExpressionList().get(0);
                }
                else {
                    expression = createNewCombinedRoot(oldRoot);
                }
            }
        }
        if (expression.getType() != IntermediateExpressionType.NOT && !expression.getType().isSimple()) {
            IntermediateCombinedExpression casted = (IntermediateCombinedExpression) expression;
            if (casted.getSubExpressionList().size() == 1) {
                expression = casted.getSubExpressionList().get(0);
            }
        }
        final IntermediateExpression logExOut = expression;
        LOGGER.trace("convertRootBraces AFTER: \n{}", defer(() -> formatExpressionAsTree(logExOut)));
        return expression;
    }

    /**
     * The root must not be a brace, so we create a single root, which is AND or OR
     * 
     * @param oldRoot root expression
     * @return new root
     */
    private IntermediateCombinedExpression createNewCombinedRoot(IntermediateCombinedExpression oldRoot) {
        IntermediateExpressionType currentType = detectStartTypeAndOr(oldRoot);
        IntermediateCombinedExpression newRoot = createNewCombinedExpression(currentType);
        for (IntermediateExpression subExpression : oldRoot.getSubExpressionList()) {
            if (subExpression.getType() == currentType) {
                newRoot.getSubExpressionList().add(subExpression);
            }
            else if (subExpression.getType() == IntermediateExpressionType.AND || subExpression.getType() == IntermediateExpressionType.OR
                    || (subExpression.getType().isPost() && !subExpression.getType().isSimple())) {
                IntermediateCombinedExpression tmp = newRoot;
                currentType = subExpression.getType();
                newRoot = createNewCombinedExpression(currentType);
                newRoot.getSubExpressionList().add(tmp);
                newRoot.getSubExpressionList().add(subExpression);
            }
        }
        return newRoot;
    }

    /**
     * Checks the sub-expressions to find the first AND resp. OR (for later combining)
     * 
     * @param root element to start
     * @return the first detected AND/OR
     */
    private IntermediateExpressionType detectStartTypeAndOr(IntermediateCombinedExpression root) {
        List<IntermediateExpressionType> types = root.getSubExpressionList().stream().map(IntermediateExpression::getType).toList();

        IntermediateExpressionType currentType = IntermediateExpressionType.AND;
        for (IntermediateExpressionType type : types) {
            if (type == IntermediateExpressionType.AND || type == IntermediateExpressionType.OR) {
                currentType = type;
                break;
            }
        }
        return currentType;
    }

    /**
     * Creates a new combined expression of the requested type
     * 
     * @param type expression type, e.g. AND
     * @return new expression instance
     */
    private IntermediateCombinedExpression createNewCombinedExpression(IntermediateExpressionType type) {

        switch (type) {
        case IntermediateExpressionType.AND:
            return new IntermediateAndExpression();
        case IntermediateExpressionType.OR:
            return new IntermediateOrExpression();
        case IntermediateExpressionType.UNION:
            return new IntermediateUnionExpression();
        case IntermediateExpressionType.INTERSECT:
            return new IntermediateIntersectExpression();
        case IntermediateExpressionType.MINUS:
            return new IntermediateMinusExpression();
        // $CASES-OMITTED$
        default:
            throw new IllegalArgumentException("Not applicable to type " + type);
        }

    }

    /**
     * Working with expressions works best if they are sorted and deduplicated, this method performs actions on all levels of expression tree (bottom-up) to
     * remove duplicates and define the order inside.
     * 
     * @param expression root
     */
    void sortAndDedup(IntermediateExpression expression) {
        LOGGER.trace("sortAndDedup BEFORE: \n{}", defer(() -> formatExpressionAsTree(expression)));

        if (expression.getType() == IntermediateExpressionType.MINUS) {
            List<IntermediateExpression> subExpressions = ((IntermediateCombinedExpression) expression).getSubExpressionList();
            subExpressions.forEach(this::sortAndDedup);
            if (subExpressions.size() > 2) {
                IntermediateExpression baseExpression = subExpressions.get(0);
                List<IntermediateExpression> subtractions = new ArrayList<>(subExpressions.subList(1, subExpressions.size()));
                sortAndDedup(subtractions);
                subExpressions.clear();
                subExpressions.add(baseExpression);
                subExpressions.addAll(subtractions);
            }
        }
        else if (!expression.getType().isSimple()) {
            List<IntermediateExpression> subExpressions = ((IntermediateCombinedExpression) expression).getSubExpressionList();
            subExpressions.forEach(this::sortAndDedup);
            sortAndDedup(subExpressions);
        }
        LOGGER.trace("sortAndDedup AFTER: \n{}", defer(() -> formatExpressionAsTree(expression)));
    }

    /**
     * @param subExpressions list to be sorted and deduplicated
     */
    private void sortAndDedup(List<IntermediateExpression> subExpressions) {

        TreeMap<String, IntermediateExpression> alphaOrderedAndUnique = new TreeMap<>(
                subExpressions.stream().collect(Collectors.toMap(IntermediateExpression::toString, Function.identity(), (existing, replacement) -> existing)));

        subExpressions.clear();

        subExpressions.addAll(alphaOrderedAndUnique.values());

    }

    /**
     * It can happen that an AND resp. OR remains with a only a single member, which creates overhead and prevents further optimization. This method eliminates
     * such abnormal expressions bottom-up by replacing them with the single member.
     * 
     * @param expression the root expression
     * @return the root expression or the new root
     */
    IntermediateExpression removeAndOrSingletons(IntermediateExpression expression) {
        final IntermediateExpression logExIn = expression;
        LOGGER.trace("removeAndOrSingletons BEFORE: \n{}", defer(() -> formatExpressionAsTree(logExIn)));
        if (!expression.getType().isSimple()) {
            List<IntermediateExpression> subExpressions = ((IntermediateCombinedExpression) expression).getSubExpressionList();
            removeAndOrSingletons(subExpressions);
            if ((expression.getType() == IntermediateExpressionType.AND || expression.getType() == IntermediateExpressionType.OR)
                    && subExpressions.size() == 1) {
                expression = subExpressions.get(0);
            }
        }
        final IntermediateExpression logExOut = expression;
        LOGGER.trace("removeAndOrSingletons AFTER: \n{}", defer(() -> formatExpressionAsTree(logExOut)));
        return expression;
    }

    /**
     * Recursive method that does the details bottom-up for {@link #removeAndOrSingletons(IntermediateExpression)}
     * 
     * @param subExpressions member expressions
     */
    private void removeAndOrSingletons(List<IntermediateExpression> subExpressions) {
        List<IntermediateExpression> cleanSubExpressions = new ArrayList<>(subExpressions.size());
        subExpressions.forEach(expression -> cleanSubExpressions.add(removeAndOrSingletons(expression)));
        subExpressions.clear();
        subExpressions.addAll(cleanSubExpressions);
    }

    /**
     * Utility to format debugging output
     * 
     * @param expression
     * @return string for logging
     */
    protected String formatExpressionAsTree(IntermediateExpression expression) {
        StringBuilder sb = new StringBuilder();
        expression.appendAsTree(sb, 1, "");
        return sb.toString();
    }

    /**
     * Takes the parsed root expression, runs the optimizations and returns the new root.
     * 
     * @param expression root
     * @return new root
     */
    public IntermediateExpression process(IntermediateExpression expression) {
        final IntermediateExpression logIn = expression;
        LOGGER.trace("Optimizing expression: \n{}", defer(() -> formatExpressionAsTree(logIn)));
        convertToPrefixNotation(expression);
        removeAllBraces(expression);
        expression = convertRootBraces(expression);
        consolidateCombinedExpressions(expression);
        expression = removeNestedNegations(expression);
        expression = removeAndOrSingletons(expression);
        sortAndDedup(expression);
        expression = removeAndOrSingletons(expression);
        sortAndDedup(expression);
        final IntermediateExpression logOut = expression;
        LOGGER.trace("Optimized expression: \n{}", defer(() -> formatExpressionAsTree(logOut)));
        return expression;
    }

}
