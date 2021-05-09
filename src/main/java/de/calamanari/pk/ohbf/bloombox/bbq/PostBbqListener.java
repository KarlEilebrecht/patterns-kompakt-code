//@formatter:off
/*
 * PostBbqListener
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

// Generated from PostBbq.g4 by ANTLR 4.9.2
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by {@link PostBbqParser}.
 */
public interface PostBbqListener extends ParseTreeListener {
    /**
     * Enter a parse tree produced by {@link PostBbqParser#refStart}.
     * 
     * @param ctx the parse tree
     */
    void enterRefStart(PostBbqParser.RefStartContext ctx);

    /**
     * Exit a parse tree produced by {@link PostBbqParser#refStart}.
     * 
     * @param ctx the parse tree
     */
    void exitRefStart(PostBbqParser.RefStartContext ctx);

    /**
     * Enter a parse tree produced by {@link PostBbqParser#refEnd}.
     * 
     * @param ctx the parse tree
     */
    void enterRefEnd(PostBbqParser.RefEndContext ctx);

    /**
     * Exit a parse tree produced by {@link PostBbqParser#refEnd}.
     * 
     * @param ctx the parse tree
     */
    void exitRefEnd(PostBbqParser.RefEndContext ctx);

    /**
     * Enter a parse tree produced by {@link PostBbqParser#source}.
     * 
     * @param ctx the parse tree
     */
    void enterSource(PostBbqParser.SourceContext ctx);

    /**
     * Exit a parse tree produced by {@link PostBbqParser#source}.
     * 
     * @param ctx the parse tree
     */
    void exitSource(PostBbqParser.SourceContext ctx);

    /**
     * Enter a parse tree produced by {@link PostBbqParser#reference}.
     * 
     * @param ctx the parse tree
     */
    void enterReference(PostBbqParser.ReferenceContext ctx);

    /**
     * Exit a parse tree produced by {@link PostBbqParser#reference}.
     * 
     * @param ctx the parse tree
     */
    void exitReference(PostBbqParser.ReferenceContext ctx);

    /**
     * Enter a parse tree produced by {@link PostBbqParser#unionExpression}.
     * 
     * @param ctx the parse tree
     */
    void enterUnionExpression(PostBbqParser.UnionExpressionContext ctx);

    /**
     * Exit a parse tree produced by {@link PostBbqParser#unionExpression}.
     * 
     * @param ctx the parse tree
     */
    void exitUnionExpression(PostBbqParser.UnionExpressionContext ctx);

    /**
     * Enter a parse tree produced by {@link PostBbqParser#intersectExpression}.
     * 
     * @param ctx the parse tree
     */
    void enterIntersectExpression(PostBbqParser.IntersectExpressionContext ctx);

    /**
     * Exit a parse tree produced by {@link PostBbqParser#intersectExpression}.
     * 
     * @param ctx the parse tree
     */
    void exitIntersectExpression(PostBbqParser.IntersectExpressionContext ctx);

    /**
     * Enter a parse tree produced by {@link PostBbqParser#minusExpression}.
     * 
     * @param ctx the parse tree
     */
    void enterMinusExpression(PostBbqParser.MinusExpressionContext ctx);

    /**
     * Exit a parse tree produced by {@link PostBbqParser#minusExpression}.
     * 
     * @param ctx the parse tree
     */
    void exitMinusExpression(PostBbqParser.MinusExpressionContext ctx);

    /**
     * Enter a parse tree produced by {@link PostBbqParser#bracedExpression}.
     * 
     * @param ctx the parse tree
     */
    void enterBracedExpression(PostBbqParser.BracedExpressionContext ctx);

    /**
     * Exit a parse tree produced by {@link PostBbqParser#bracedExpression}.
     * 
     * @param ctx the parse tree
     */
    void exitBracedExpression(PostBbqParser.BracedExpressionContext ctx);

    /**
     * Enter a parse tree produced by {@link PostBbqParser#expression}.
     * 
     * @param ctx the parse tree
     */
    void enterExpression(PostBbqParser.ExpressionContext ctx);

    /**
     * Exit a parse tree produced by {@link PostBbqParser#expression}.
     * 
     * @param ctx the parse tree
     */
    void exitExpression(PostBbqParser.ExpressionContext ctx);

    /**
     * Enter a parse tree produced by {@link PostBbqParser#query}.
     * 
     * @param ctx the parse tree
     */
    void enterQuery(PostBbqParser.QueryContext ctx);

    /**
     * Exit a parse tree produced by {@link PostBbqParser#query}.
     * 
     * @param ctx the parse tree
     */
    void exitQuery(PostBbqParser.QueryContext ctx);
}