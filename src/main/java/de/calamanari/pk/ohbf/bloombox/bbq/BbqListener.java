//@formatter:off
/*
 * BbqListener
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

// Generated from Bbq.g4 by ANTLR 4.9.2
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by {@link BbqParser}.
 */
public interface BbqListener extends ParseTreeListener {
    /**
     * Enter a parse tree produced by {@link BbqParser#argName}.
     * 
     * @param ctx the parse tree
     */
    void enterArgName(BbqParser.ArgNameContext ctx);

    /**
     * Exit a parse tree produced by {@link BbqParser#argName}.
     * 
     * @param ctx the parse tree
     */
    void exitArgName(BbqParser.ArgNameContext ctx);

    /**
     * Enter a parse tree produced by {@link BbqParser#argValue}.
     * 
     * @param ctx the parse tree
     */
    void enterArgValue(BbqParser.ArgValueContext ctx);

    /**
     * Exit a parse tree produced by {@link BbqParser#argValue}.
     * 
     * @param ctx the parse tree
     */
    void exitArgValue(BbqParser.ArgValueContext ctx);

    /**
     * Enter a parse tree produced by {@link BbqParser#inValue}.
     * 
     * @param ctx the parse tree
     */
    void enterInValue(BbqParser.InValueContext ctx);

    /**
     * Exit a parse tree produced by {@link BbqParser#inValue}.
     * 
     * @param ctx the parse tree
     */
    void exitInValue(BbqParser.InValueContext ctx);

    /**
     * Enter a parse tree produced by {@link BbqParser#nextValue}.
     * 
     * @param ctx the parse tree
     */
    void enterNextValue(BbqParser.NextValueContext ctx);

    /**
     * Exit a parse tree produced by {@link BbqParser#nextValue}.
     * 
     * @param ctx the parse tree
     */
    void exitNextValue(BbqParser.NextValueContext ctx);

    /**
     * Enter a parse tree produced by {@link BbqParser#cmpEquals}.
     * 
     * @param ctx the parse tree
     */
    void enterCmpEquals(BbqParser.CmpEqualsContext ctx);

    /**
     * Exit a parse tree produced by {@link BbqParser#cmpEquals}.
     * 
     * @param ctx the parse tree
     */
    void exitCmpEquals(BbqParser.CmpEqualsContext ctx);

    /**
     * Enter a parse tree produced by {@link BbqParser#cmpNotEquals}.
     * 
     * @param ctx the parse tree
     */
    void enterCmpNotEquals(BbqParser.CmpNotEqualsContext ctx);

    /**
     * Exit a parse tree produced by {@link BbqParser#cmpNotEquals}.
     * 
     * @param ctx the parse tree
     */
    void exitCmpNotEquals(BbqParser.CmpNotEqualsContext ctx);

    /**
     * Enter a parse tree produced by {@link BbqParser#cmpIn}.
     * 
     * @param ctx the parse tree
     */
    void enterCmpIn(BbqParser.CmpInContext ctx);

    /**
     * Exit a parse tree produced by {@link BbqParser#cmpIn}.
     * 
     * @param ctx the parse tree
     */
    void exitCmpIn(BbqParser.CmpInContext ctx);

    /**
     * Enter a parse tree produced by {@link BbqParser#cmpNotIn}.
     * 
     * @param ctx the parse tree
     */
    void enterCmpNotIn(BbqParser.CmpNotInContext ctx);

    /**
     * Exit a parse tree produced by {@link BbqParser#cmpNotIn}.
     * 
     * @param ctx the parse tree
     */
    void exitCmpNotIn(BbqParser.CmpNotInContext ctx);

    /**
     * Enter a parse tree produced by {@link BbqParser#expressionDetails}.
     * 
     * @param ctx the parse tree
     */
    void enterExpressionDetails(BbqParser.ExpressionDetailsContext ctx);

    /**
     * Exit a parse tree produced by {@link BbqParser#expressionDetails}.
     * 
     * @param ctx the parse tree
     */
    void exitExpressionDetails(BbqParser.ExpressionDetailsContext ctx);

    /**
     * Enter a parse tree produced by {@link BbqParser#orExpression}.
     * 
     * @param ctx the parse tree
     */
    void enterOrExpression(BbqParser.OrExpressionContext ctx);

    /**
     * Exit a parse tree produced by {@link BbqParser#orExpression}.
     * 
     * @param ctx the parse tree
     */
    void exitOrExpression(BbqParser.OrExpressionContext ctx);

    /**
     * Enter a parse tree produced by {@link BbqParser#andExpression}.
     * 
     * @param ctx the parse tree
     */
    void enterAndExpression(BbqParser.AndExpressionContext ctx);

    /**
     * Exit a parse tree produced by {@link BbqParser#andExpression}.
     * 
     * @param ctx the parse tree
     */
    void exitAndExpression(BbqParser.AndExpressionContext ctx);

    /**
     * Enter a parse tree produced by {@link BbqParser#expression}.
     * 
     * @param ctx the parse tree
     */
    void enterExpression(BbqParser.ExpressionContext ctx);

    /**
     * Exit a parse tree produced by {@link BbqParser#expression}.
     * 
     * @param ctx the parse tree
     */
    void exitExpression(BbqParser.ExpressionContext ctx);

    /**
     * Enter a parse tree produced by {@link BbqParser#bracedExpression}.
     * 
     * @param ctx the parse tree
     */
    void enterBracedExpression(BbqParser.BracedExpressionContext ctx);

    /**
     * Exit a parse tree produced by {@link BbqParser#bracedExpression}.
     * 
     * @param ctx the parse tree
     */
    void exitBracedExpression(BbqParser.BracedExpressionContext ctx);

    /**
     * Enter a parse tree produced by {@link BbqParser#bbqDetails}.
     * 
     * @param ctx the parse tree
     */
    void enterBbqDetails(BbqParser.BbqDetailsContext ctx);

    /**
     * Exit a parse tree produced by {@link BbqParser#bbqDetails}.
     * 
     * @param ctx the parse tree
     */
    void exitBbqDetails(BbqParser.BbqDetailsContext ctx);

    /**
     * Enter a parse tree produced by {@link BbqParser#andBBQ}.
     * 
     * @param ctx the parse tree
     */
    void enterAndBBQ(BbqParser.AndBBQContext ctx);

    /**
     * Exit a parse tree produced by {@link BbqParser#andBBQ}.
     * 
     * @param ctx the parse tree
     */
    void exitAndBBQ(BbqParser.AndBBQContext ctx);

    /**
     * Enter a parse tree produced by {@link BbqParser#orBBQ}.
     * 
     * @param ctx the parse tree
     */
    void enterOrBBQ(BbqParser.OrBBQContext ctx);

    /**
     * Exit a parse tree produced by {@link BbqParser#orBBQ}.
     * 
     * @param ctx the parse tree
     */
    void exitOrBBQ(BbqParser.OrBBQContext ctx);

    /**
     * Enter a parse tree produced by {@link BbqParser#fullBBQ}.
     * 
     * @param ctx the parse tree
     */
    void enterFullBBQ(BbqParser.FullBBQContext ctx);

    /**
     * Exit a parse tree produced by {@link BbqParser#fullBBQ}.
     * 
     * @param ctx the parse tree
     */
    void exitFullBBQ(BbqParser.FullBBQContext ctx);

    /**
     * Enter a parse tree produced by {@link BbqParser#query}.
     * 
     * @param ctx the parse tree
     */
    void enterQuery(BbqParser.QueryContext ctx);

    /**
     * Exit a parse tree produced by {@link BbqParser#query}.
     * 
     * @param ctx the parse tree
     */
    void exitQuery(BbqParser.QueryContext ctx);
}