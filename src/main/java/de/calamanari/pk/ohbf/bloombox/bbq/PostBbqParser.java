//@formatter:off
/*
 * PostBbqParser
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

import java.util.List;

import org.antlr.v4.runtime.NoViableAltException;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.RuntimeMetaData;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.VocabularyImpl;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.TerminalNode;

@SuppressWarnings({ "all", "warnings", "unchecked", "unused", "cast" })
public class PostBbqParser extends Parser {
    static {
        RuntimeMetaData.checkVersion("4.9.2", RuntimeMetaData.VERSION);
    }

    protected static final DFA[] _decisionToDFA;
    protected static final PredictionContextCache _sharedContextCache = new PredictionContextCache();
    public static final int T__0 = 1, T__1 = 2, T__2 = 3, T__3 = 4, T__4 = 5, T__5 = 6, T__6 = 7, T__7 = 8, T__8 = 9, ESCESC = 10, ESCSQ = 11, ESCDQ = 12,
            UNION = 13, INTERSECT = 14, MINUS = 15, SIMPLE_WORD = 16, SQWORD = 17, DQWORD = 18;
    public static final int RULE_refStart = 0, RULE_refEnd = 1, RULE_source = 2, RULE_reference = 3, RULE_unionExpression = 4, RULE_intersectExpression = 5,
            RULE_minusExpression = 6, RULE_bracedExpression = 7, RULE_expression = 8, RULE_query = 9;

    private static String[] makeRuleNames() {
        return new String[] { "refStart", "refEnd", "source", "reference", "unionExpression", "intersectExpression", "minusExpression", "bracedExpression",
                "expression", "query" };
    }

    public static final String[] ruleNames = makeRuleNames();

    private static String[] makeLiteralNames() {
        return new String[] { null, "'$'", "'{'", "' '", "'\t'", "'\r'", "'\n'", "'}'", "'('", "')'" };
    }

    private static final String[] _LITERAL_NAMES = makeLiteralNames();

    private static String[] makeSymbolicNames() {
        return new String[] { null, null, null, null, null, null, null, null, null, null, "ESCESC", "ESCSQ", "ESCDQ", "UNION", "INTERSECT", "MINUS",
                "SIMPLE_WORD", "SQWORD", "DQWORD" };
    }

    private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
    public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

    /**
     * @deprecated Use {@link #VOCABULARY} instead.
     */
    @Deprecated
    public static final String[] tokenNames;
    static {
        tokenNames = new String[_SYMBOLIC_NAMES.length];
        for (int i = 0; i < tokenNames.length; i++) {
            tokenNames[i] = VOCABULARY.getLiteralName(i);
            if (tokenNames[i] == null) {
                tokenNames[i] = VOCABULARY.getSymbolicName(i);
            }

            if (tokenNames[i] == null) {
                tokenNames[i] = "<INVALID>";
            }
        }
    }

    @Override
    @Deprecated
    public String[] getTokenNames() {
        return tokenNames;
    }

    @Override

    public Vocabulary getVocabulary() {
        return VOCABULARY;
    }

    @Override
    public String getGrammarFileName() {
        return "PostBbq.g4";
    }

    @Override
    public String[] getRuleNames() {
        return ruleNames;
    }

    @Override
    public String getSerializedATN() {
        return _serializedATN;
    }

    @Override
    public ATN getATN() {
        return _ATN;
    }

    public PostBbqParser(TokenStream input) {
        super(input);
        _interp = new ParserATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
    }

    public static class RefStartContext extends ParserRuleContext {
        public RefStartContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_refStart;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof PostBbqListener)
                ((PostBbqListener) listener).enterRefStart(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof PostBbqListener)
                ((PostBbqListener) listener).exitRefStart(this);
        }
    }

    public final RefStartContext refStart() throws RecognitionException {
        RefStartContext _localctx = new RefStartContext(_ctx, getState());
        enterRule(_localctx, 0, RULE_refStart);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(20);
                match(T__0);
                setState(21);
                match(T__1);
                setState(25);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__5))) != 0)) {
                    {
                        {
                            setState(22);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__5))) != 0))) {
                                _errHandler.recoverInline(this);
                            }
                            else {
                                if (_input.LA(1) == Token.EOF)
                                    matchedEOF = true;
                                _errHandler.reportMatch(this);
                                consume();
                            }
                        }
                    }
                    setState(27);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    public static class RefEndContext extends ParserRuleContext {
        public RefEndContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_refEnd;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof PostBbqListener)
                ((PostBbqListener) listener).enterRefEnd(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof PostBbqListener)
                ((PostBbqListener) listener).exitRefEnd(this);
        }
    }

    public final RefEndContext refEnd() throws RecognitionException {
        RefEndContext _localctx = new RefEndContext(_ctx, getState());
        enterRule(_localctx, 2, RULE_refEnd);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(31);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__5))) != 0)) {
                    {
                        {
                            setState(28);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__5))) != 0))) {
                                _errHandler.recoverInline(this);
                            }
                            else {
                                if (_input.LA(1) == Token.EOF)
                                    matchedEOF = true;
                                _errHandler.reportMatch(this);
                                consume();
                            }
                        }
                    }
                    setState(33);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(34);
                match(T__6);
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    public static class SourceContext extends ParserRuleContext {
        public TerminalNode SIMPLE_WORD() {
            return getToken(PostBbqParser.SIMPLE_WORD, 0);
        }

        public TerminalNode SQWORD() {
            return getToken(PostBbqParser.SQWORD, 0);
        }

        public TerminalNode DQWORD() {
            return getToken(PostBbqParser.DQWORD, 0);
        }

        public SourceContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_source;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof PostBbqListener)
                ((PostBbqListener) listener).enterSource(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof PostBbqListener)
                ((PostBbqListener) listener).exitSource(this);
        }
    }

    public final SourceContext source() throws RecognitionException {
        SourceContext _localctx = new SourceContext(_ctx, getState());
        enterRule(_localctx, 4, RULE_source);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(36);
                _la = _input.LA(1);
                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << SIMPLE_WORD) | (1L << SQWORD) | (1L << DQWORD))) != 0))) {
                    _errHandler.recoverInline(this);
                }
                else {
                    if (_input.LA(1) == Token.EOF)
                        matchedEOF = true;
                    _errHandler.reportMatch(this);
                    consume();
                }
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    public static class ReferenceContext extends ParserRuleContext {
        public RefStartContext refStart() {
            return getRuleContext(RefStartContext.class, 0);
        }

        public SourceContext source() {
            return getRuleContext(SourceContext.class, 0);
        }

        public RefEndContext refEnd() {
            return getRuleContext(RefEndContext.class, 0);
        }

        public ReferenceContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_reference;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof PostBbqListener)
                ((PostBbqListener) listener).enterReference(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof PostBbqListener)
                ((PostBbqListener) listener).exitReference(this);
        }
    }

    public final ReferenceContext reference() throws RecognitionException {
        ReferenceContext _localctx = new ReferenceContext(_ctx, getState());
        enterRule(_localctx, 6, RULE_reference);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(38);
                refStart();
                setState(39);
                source();
                setState(40);
                refEnd();
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    public static class UnionExpressionContext extends ParserRuleContext {
        public TerminalNode UNION() {
            return getToken(PostBbqParser.UNION, 0);
        }

        public BracedExpressionContext bracedExpression() {
            return getRuleContext(BracedExpressionContext.class, 0);
        }

        public ReferenceContext reference() {
            return getRuleContext(ReferenceContext.class, 0);
        }

        public UnionExpressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_unionExpression;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof PostBbqListener)
                ((PostBbqListener) listener).enterUnionExpression(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof PostBbqListener)
                ((PostBbqListener) listener).exitUnionExpression(this);
        }
    }

    public final UnionExpressionContext unionExpression() throws RecognitionException {
        UnionExpressionContext _localctx = new UnionExpressionContext(_ctx, getState());
        enterRule(_localctx, 8, RULE_unionExpression);
        int _la;
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(45);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__5))) != 0)) {
                    {
                        {
                            setState(42);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__5))) != 0))) {
                                _errHandler.recoverInline(this);
                            }
                            else {
                                if (_input.LA(1) == Token.EOF)
                                    matchedEOF = true;
                                _errHandler.reportMatch(this);
                                consume();
                            }
                        }
                    }
                    setState(47);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(48);
                match(UNION);
                setState(52);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 3, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        {
                            {
                                setState(49);
                                _la = _input.LA(1);
                                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__5))) != 0))) {
                                    _errHandler.recoverInline(this);
                                }
                                else {
                                    if (_input.LA(1) == Token.EOF)
                                        matchedEOF = true;
                                    _errHandler.reportMatch(this);
                                    consume();
                                }
                            }
                        }
                    }
                    setState(54);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 3, _ctx);
                }
                setState(57);
                _errHandler.sync(this);
                switch (_input.LA(1)) {
                case T__2:
                case T__3:
                case T__4:
                case T__5:
                case T__7: {
                    setState(55);
                    bracedExpression();
                }
                    break;
                case T__0: {
                    setState(56);
                    reference();
                }
                    break;
                default:
                    throw new NoViableAltException(this);
                }
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    public static class IntersectExpressionContext extends ParserRuleContext {
        public TerminalNode INTERSECT() {
            return getToken(PostBbqParser.INTERSECT, 0);
        }

        public BracedExpressionContext bracedExpression() {
            return getRuleContext(BracedExpressionContext.class, 0);
        }

        public ReferenceContext reference() {
            return getRuleContext(ReferenceContext.class, 0);
        }

        public IntersectExpressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_intersectExpression;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof PostBbqListener)
                ((PostBbqListener) listener).enterIntersectExpression(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof PostBbqListener)
                ((PostBbqListener) listener).exitIntersectExpression(this);
        }
    }

    public final IntersectExpressionContext intersectExpression() throws RecognitionException {
        IntersectExpressionContext _localctx = new IntersectExpressionContext(_ctx, getState());
        enterRule(_localctx, 10, RULE_intersectExpression);
        int _la;
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(62);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__5))) != 0)) {
                    {
                        {
                            setState(59);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__5))) != 0))) {
                                _errHandler.recoverInline(this);
                            }
                            else {
                                if (_input.LA(1) == Token.EOF)
                                    matchedEOF = true;
                                _errHandler.reportMatch(this);
                                consume();
                            }
                        }
                    }
                    setState(64);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(65);
                match(INTERSECT);
                setState(69);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 6, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        {
                            {
                                setState(66);
                                _la = _input.LA(1);
                                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__5))) != 0))) {
                                    _errHandler.recoverInline(this);
                                }
                                else {
                                    if (_input.LA(1) == Token.EOF)
                                        matchedEOF = true;
                                    _errHandler.reportMatch(this);
                                    consume();
                                }
                            }
                        }
                    }
                    setState(71);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 6, _ctx);
                }
                setState(74);
                _errHandler.sync(this);
                switch (_input.LA(1)) {
                case T__2:
                case T__3:
                case T__4:
                case T__5:
                case T__7: {
                    setState(72);
                    bracedExpression();
                }
                    break;
                case T__0: {
                    setState(73);
                    reference();
                }
                    break;
                default:
                    throw new NoViableAltException(this);
                }
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    public static class MinusExpressionContext extends ParserRuleContext {
        public TerminalNode MINUS() {
            return getToken(PostBbqParser.MINUS, 0);
        }

        public BracedExpressionContext bracedExpression() {
            return getRuleContext(BracedExpressionContext.class, 0);
        }

        public ReferenceContext reference() {
            return getRuleContext(ReferenceContext.class, 0);
        }

        public MinusExpressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_minusExpression;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof PostBbqListener)
                ((PostBbqListener) listener).enterMinusExpression(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof PostBbqListener)
                ((PostBbqListener) listener).exitMinusExpression(this);
        }
    }

    public final MinusExpressionContext minusExpression() throws RecognitionException {
        MinusExpressionContext _localctx = new MinusExpressionContext(_ctx, getState());
        enterRule(_localctx, 12, RULE_minusExpression);
        int _la;
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(79);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__5))) != 0)) {
                    {
                        {
                            setState(76);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__5))) != 0))) {
                                _errHandler.recoverInline(this);
                            }
                            else {
                                if (_input.LA(1) == Token.EOF)
                                    matchedEOF = true;
                                _errHandler.reportMatch(this);
                                consume();
                            }
                        }
                    }
                    setState(81);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(82);
                match(MINUS);
                setState(86);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 9, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        {
                            {
                                setState(83);
                                _la = _input.LA(1);
                                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__5))) != 0))) {
                                    _errHandler.recoverInline(this);
                                }
                                else {
                                    if (_input.LA(1) == Token.EOF)
                                        matchedEOF = true;
                                    _errHandler.reportMatch(this);
                                    consume();
                                }
                            }
                        }
                    }
                    setState(88);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 9, _ctx);
                }
                setState(91);
                _errHandler.sync(this);
                switch (_input.LA(1)) {
                case T__2:
                case T__3:
                case T__4:
                case T__5:
                case T__7: {
                    setState(89);
                    bracedExpression();
                }
                    break;
                case T__0: {
                    setState(90);
                    reference();
                }
                    break;
                default:
                    throw new NoViableAltException(this);
                }
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    public static class BracedExpressionContext extends ParserRuleContext {
        public ExpressionContext expression() {
            return getRuleContext(ExpressionContext.class, 0);
        }

        public BracedExpressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_bracedExpression;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof PostBbqListener)
                ((PostBbqListener) listener).enterBracedExpression(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof PostBbqListener)
                ((PostBbqListener) listener).exitBracedExpression(this);
        }
    }

    public final BracedExpressionContext bracedExpression() throws RecognitionException {
        BracedExpressionContext _localctx = new BracedExpressionContext(_ctx, getState());
        enterRule(_localctx, 14, RULE_bracedExpression);
        int _la;
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(96);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__5))) != 0)) {
                    {
                        {
                            setState(93);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__5))) != 0))) {
                                _errHandler.recoverInline(this);
                            }
                            else {
                                if (_input.LA(1) == Token.EOF)
                                    matchedEOF = true;
                                _errHandler.reportMatch(this);
                                consume();
                            }
                        }
                    }
                    setState(98);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(99);
                match(T__7);
                setState(100);
                expression();
                setState(101);
                match(T__8);
                setState(105);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 12, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        {
                            {
                                setState(102);
                                _la = _input.LA(1);
                                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__5))) != 0))) {
                                    _errHandler.recoverInline(this);
                                }
                                else {
                                    if (_input.LA(1) == Token.EOF)
                                        matchedEOF = true;
                                    _errHandler.reportMatch(this);
                                    consume();
                                }
                            }
                        }
                    }
                    setState(107);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 12, _ctx);
                }
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    public static class ExpressionContext extends ParserRuleContext {
        public ReferenceContext reference() {
            return getRuleContext(ReferenceContext.class, 0);
        }

        public BracedExpressionContext bracedExpression() {
            return getRuleContext(BracedExpressionContext.class, 0);
        }

        public List<UnionExpressionContext> unionExpression() {
            return getRuleContexts(UnionExpressionContext.class);
        }

        public UnionExpressionContext unionExpression(int i) {
            return getRuleContext(UnionExpressionContext.class, i);
        }

        public List<IntersectExpressionContext> intersectExpression() {
            return getRuleContexts(IntersectExpressionContext.class);
        }

        public IntersectExpressionContext intersectExpression(int i) {
            return getRuleContext(IntersectExpressionContext.class, i);
        }

        public List<MinusExpressionContext> minusExpression() {
            return getRuleContexts(MinusExpressionContext.class);
        }

        public MinusExpressionContext minusExpression(int i) {
            return getRuleContext(MinusExpressionContext.class, i);
        }

        public ExpressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_expression;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof PostBbqListener)
                ((PostBbqListener) listener).enterExpression(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof PostBbqListener)
                ((PostBbqListener) listener).exitExpression(this);
        }
    }

    public final ExpressionContext expression() throws RecognitionException {
        ExpressionContext _localctx = new ExpressionContext(_ctx, getState());
        enterRule(_localctx, 16, RULE_expression);
        try {
            int _alt;
            setState(130);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 18, _ctx)) {
            case 1:
                enterOuterAlt(_localctx, 1); {
                setState(108);
                reference();
            }
                break;
            case 2:
                enterOuterAlt(_localctx, 2); {
                setState(111);
                _errHandler.sync(this);
                switch (_input.LA(1)) {
                case T__0: {
                    setState(109);
                    reference();
                }
                    break;
                case T__2:
                case T__3:
                case T__4:
                case T__5:
                case T__7: {
                    setState(110);
                    bracedExpression();
                }
                    break;
                default:
                    throw new NoViableAltException(this);
                }
                setState(128);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 17, _ctx)) {
                case 1: {
                    setState(114);
                    _errHandler.sync(this);
                    _alt = 1;
                    do {
                        switch (_alt) {
                        case 1: {
                            {
                                setState(113);
                                unionExpression();
                            }
                        }
                            break;
                        default:
                            throw new NoViableAltException(this);
                        }
                        setState(116);
                        _errHandler.sync(this);
                        _alt = getInterpreter().adaptivePredict(_input, 14, _ctx);
                    } while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER);
                }
                    break;
                case 2: {
                    setState(119);
                    _errHandler.sync(this);
                    _alt = 1;
                    do {
                        switch (_alt) {
                        case 1: {
                            {
                                setState(118);
                                intersectExpression();
                            }
                        }
                            break;
                        default:
                            throw new NoViableAltException(this);
                        }
                        setState(121);
                        _errHandler.sync(this);
                        _alt = getInterpreter().adaptivePredict(_input, 15, _ctx);
                    } while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER);
                }
                    break;
                case 3: {
                    setState(124);
                    _errHandler.sync(this);
                    _alt = 1;
                    do {
                        switch (_alt) {
                        case 1: {
                            {
                                setState(123);
                                minusExpression();
                            }
                        }
                            break;
                        default:
                            throw new NoViableAltException(this);
                        }
                        setState(126);
                        _errHandler.sync(this);
                        _alt = getInterpreter().adaptivePredict(_input, 16, _ctx);
                    } while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER);
                }
                    break;
                }
            }
                break;
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    public static class QueryContext extends ParserRuleContext {
        public ExpressionContext expression() {
            return getRuleContext(ExpressionContext.class, 0);
        }

        public TerminalNode EOF() {
            return getToken(PostBbqParser.EOF, 0);
        }

        public QueryContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_query;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof PostBbqListener)
                ((PostBbqListener) listener).enterQuery(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof PostBbqListener)
                ((PostBbqListener) listener).exitQuery(this);
        }
    }

    public final QueryContext query() throws RecognitionException {
        QueryContext _localctx = new QueryContext(_ctx, getState());
        enterRule(_localctx, 18, RULE_query);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(132);
                expression();
                setState(136);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__5))) != 0)) {
                    {
                        {
                            setState(133);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__5))) != 0))) {
                                _errHandler.recoverInline(this);
                            }
                            else {
                                if (_input.LA(1) == Token.EOF)
                                    matchedEOF = true;
                                _errHandler.reportMatch(this);
                                consume();
                            }
                        }
                    }
                    setState(138);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(139);
                match(EOF);
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    public static final String _serializedATN = "\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\24\u0090\4\2\t\2"
            + "\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"
            + "\t\13\3\2\3\2\3\2\7\2\32\n\2\f\2\16\2\35\13\2\3\3\7\3 \n\3\f\3\16\3#\13"
            + "\3\3\3\3\3\3\4\3\4\3\5\3\5\3\5\3\5\3\6\7\6.\n\6\f\6\16\6\61\13\6\3\6\3"
            + "\6\7\6\65\n\6\f\6\16\68\13\6\3\6\3\6\5\6<\n\6\3\7\7\7?\n\7\f\7\16\7B\13"
            + "\7\3\7\3\7\7\7F\n\7\f\7\16\7I\13\7\3\7\3\7\5\7M\n\7\3\b\7\bP\n\b\f\b\16"
            + "\bS\13\b\3\b\3\b\7\bW\n\b\f\b\16\bZ\13\b\3\b\3\b\5\b^\n\b\3\t\7\ta\n\t" + "\f\t\16\td\13\t\3\t\3\t\3\t\3\t\7\tj\n\t\f\t\16\tm\13\t\3\n\3\n\3\n\5"
            + "\nr\n\n\3\n\6\nu\n\n\r\n\16\nv\3\n\6\nz\n\n\r\n\16\n{\3\n\6\n\177\n\n" + "\r\n\16\n\u0080\5\n\u0083\n\n\5\n\u0085\n\n\3\13\3\13\7\13\u0089\n\13"
            + "\f\13\16\13\u008c\13\13\3\13\3\13\3\13\2\2\f\2\4\6\b\n\f\16\20\22\24\2"
            + "\4\3\2\5\b\3\2\22\24\2\u009a\2\26\3\2\2\2\4!\3\2\2\2\6&\3\2\2\2\b(\3\2"
            + "\2\2\n/\3\2\2\2\f@\3\2\2\2\16Q\3\2\2\2\20b\3\2\2\2\22\u0084\3\2\2\2\24"
            + "\u0086\3\2\2\2\26\27\7\3\2\2\27\33\7\4\2\2\30\32\t\2\2\2\31\30\3\2\2\2" + "\32\35\3\2\2\2\33\31\3\2\2\2\33\34\3\2\2\2\34\3\3\2\2\2\35\33\3\2\2\2"
            + "\36 \t\2\2\2\37\36\3\2\2\2 #\3\2\2\2!\37\3\2\2\2!\"\3\2\2\2\"$\3\2\2\2"
            + "#!\3\2\2\2$%\7\t\2\2%\5\3\2\2\2&\'\t\3\2\2\'\7\3\2\2\2()\5\2\2\2)*\5\6"
            + "\4\2*+\5\4\3\2+\t\3\2\2\2,.\t\2\2\2-,\3\2\2\2.\61\3\2\2\2/-\3\2\2\2/\60"
            + "\3\2\2\2\60\62\3\2\2\2\61/\3\2\2\2\62\66\7\17\2\2\63\65\t\2\2\2\64\63" + "\3\2\2\2\658\3\2\2\2\66\64\3\2\2\2\66\67\3\2\2\2\67;\3\2\2\28\66\3\2\2"
            + "\29<\5\20\t\2:<\5\b\5\2;9\3\2\2\2;:\3\2\2\2<\13\3\2\2\2=?\t\2\2\2>=\3" + "\2\2\2?B\3\2\2\2@>\3\2\2\2@A\3\2\2\2AC\3\2\2\2B@\3\2\2\2CG\7\20\2\2DF"
            + "\t\2\2\2ED\3\2\2\2FI\3\2\2\2GE\3\2\2\2GH\3\2\2\2HL\3\2\2\2IG\3\2\2\2J" + "M\5\20\t\2KM\5\b\5\2LJ\3\2\2\2LK\3\2\2\2M\r\3\2\2\2NP\t\2\2\2ON\3\2\2"
            + "\2PS\3\2\2\2QO\3\2\2\2QR\3\2\2\2RT\3\2\2\2SQ\3\2\2\2TX\7\21\2\2UW\t\2"
            + "\2\2VU\3\2\2\2WZ\3\2\2\2XV\3\2\2\2XY\3\2\2\2Y]\3\2\2\2ZX\3\2\2\2[^\5\20"
            + "\t\2\\^\5\b\5\2][\3\2\2\2]\\\3\2\2\2^\17\3\2\2\2_a\t\2\2\2`_\3\2\2\2a" + "d\3\2\2\2b`\3\2\2\2bc\3\2\2\2ce\3\2\2\2db\3\2\2\2ef\7\n\2\2fg\5\22\n\2"
            + "gk\7\13\2\2hj\t\2\2\2ih\3\2\2\2jm\3\2\2\2ki\3\2\2\2kl\3\2\2\2l\21\3\2" + "\2\2mk\3\2\2\2n\u0085\5\b\5\2or\5\b\5\2pr\5\20\t\2qo\3\2\2\2qp\3\2\2\2"
            + "r\u0082\3\2\2\2su\5\n\6\2ts\3\2\2\2uv\3\2\2\2vt\3\2\2\2vw\3\2\2\2w\u0083"
            + "\3\2\2\2xz\5\f\7\2yx\3\2\2\2z{\3\2\2\2{y\3\2\2\2{|\3\2\2\2|\u0083\3\2"
            + "\2\2}\177\5\16\b\2~}\3\2\2\2\177\u0080\3\2\2\2\u0080~\3\2\2\2\u0080\u0081"
            + "\3\2\2\2\u0081\u0083\3\2\2\2\u0082t\3\2\2\2\u0082y\3\2\2\2\u0082~\3\2" + "\2\2\u0083\u0085\3\2\2\2\u0084n\3\2\2\2\u0084q\3\2\2\2\u0085\23\3\2\2"
            + "\2\u0086\u008a\5\22\n\2\u0087\u0089\t\2\2\2\u0088\u0087\3\2\2\2\u0089" + "\u008c\3\2\2\2\u008a\u0088\3\2\2\2\u008a\u008b\3\2\2\2\u008b\u008d\3\2"
            + "\2\2\u008c\u008a\3\2\2\2\u008d\u008e\7\2\2\3\u008e\25\3\2\2\2\26\33!/" + "\66;@GLQX]bkqv{\u0080\u0082\u0084\u008a";
    public static final ATN _ATN = new ATNDeserializer().deserialize(_serializedATN.toCharArray());
    static {
        _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
        for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
            _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
        }
    }
}