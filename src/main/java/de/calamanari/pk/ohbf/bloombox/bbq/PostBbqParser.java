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

// Generated from Bbq.g4 by ANTLR 4.9.2

@SuppressWarnings({ "all", "warnings", "unchecked", "unused", "cast" })
public class PostBbqParser extends Parser {
    static {
        RuntimeMetaData.checkVersion("4.9.2", RuntimeMetaData.VERSION);
    }

    protected static final DFA[] _decisionToDFA;
    protected static final PredictionContextCache _sharedContextCache = new PredictionContextCache();
    public static final int T__0 = 1, T__1 = 2, T__2 = 3, T__3 = 4, T__4 = 5, T__5 = 6, T__6 = 7, T__7 = 8, T__8 = 9, T__9 = 10, T__10 = 11, ESCESC = 12,
            ESCSQ = 13, ESCDQ = 14, UNION = 15, INTERSECT = 16, MINUS = 17, MINMAX = 18, SIMPLE_WORD = 19, SQWORD = 20, DQWORD = 21;
    public static final int RULE_refStart = 0, RULE_refEnd = 1, RULE_source = 2, RULE_reference = 3, RULE_lowerBound = 4, RULE_upperBound = 5,
            RULE_minMaxExpression = 6, RULE_unionExpression = 7, RULE_intersectExpression = 8, RULE_minusExpression = 9, RULE_bracedExpression = 10,
            RULE_expression = 11, RULE_query = 12;

    private static String[] makeRuleNames() {
        return new String[] { "refStart", "refEnd", "source", "reference", "lowerBound", "upperBound", "minMaxExpression", "unionExpression",
                "intersectExpression", "minusExpression", "bracedExpression", "expression", "query" };
    }

    public static final String[] ruleNames = makeRuleNames();

    private static String[] makeLiteralNames() {
        return new String[] { null, "'$'", "'{'", "' '", "'\t'", "'\r'", "'\n'", "'}'", "'('", "','", "';'", "')'" };
    }

    private static final String[] _LITERAL_NAMES = makeLiteralNames();

    private static String[] makeSymbolicNames() {
        return new String[] { null, null, null, null, null, null, null, null, null, null, null, null, "ESCESC", "ESCSQ", "ESCDQ", "UNION", "INTERSECT", "MINUS",
                "MINMAX", "SIMPLE_WORD", "SQWORD", "DQWORD" };
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
                setState(26);
                match(T__0);
                setState(27);
                match(T__1);
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
                setState(37);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__5))) != 0)) {
                    {
                        {
                            setState(34);
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
                    setState(39);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(40);
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
                setState(42);
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
                setState(44);
                refStart();
                setState(45);
                source();
                setState(46);
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

    public static class LowerBoundContext extends ParserRuleContext {
        public TerminalNode SIMPLE_WORD() {
            return getToken(PostBbqParser.SIMPLE_WORD, 0);
        }

        public LowerBoundContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_lowerBound;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof PostBbqListener)
                ((PostBbqListener) listener).enterLowerBound(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof PostBbqListener)
                ((PostBbqListener) listener).exitLowerBound(this);
        }
    }

    public final LowerBoundContext lowerBound() throws RecognitionException {
        LowerBoundContext _localctx = new LowerBoundContext(_ctx, getState());
        enterRule(_localctx, 8, RULE_lowerBound);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(48);
                match(SIMPLE_WORD);
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

    public static class UpperBoundContext extends ParserRuleContext {
        public TerminalNode SIMPLE_WORD() {
            return getToken(PostBbqParser.SIMPLE_WORD, 0);
        }

        public UpperBoundContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_upperBound;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof PostBbqListener)
                ((PostBbqListener) listener).enterUpperBound(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof PostBbqListener)
                ((PostBbqListener) listener).exitUpperBound(this);
        }
    }

    public final UpperBoundContext upperBound() throws RecognitionException {
        UpperBoundContext _localctx = new UpperBoundContext(_ctx, getState());
        enterRule(_localctx, 10, RULE_upperBound);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(50);
                match(SIMPLE_WORD);
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

    public static class MinMaxExpressionContext extends ParserRuleContext {
        public TerminalNode MINMAX() {
            return getToken(PostBbqParser.MINMAX, 0);
        }

        public List<ExpressionContext> expression() {
            return getRuleContexts(ExpressionContext.class);
        }

        public ExpressionContext expression(int i) {
            return getRuleContext(ExpressionContext.class, i);
        }

        public LowerBoundContext lowerBound() {
            return getRuleContext(LowerBoundContext.class, 0);
        }

        public UpperBoundContext upperBound() {
            return getRuleContext(UpperBoundContext.class, 0);
        }

        public MinMaxExpressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_minMaxExpression;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof PostBbqListener)
                ((PostBbqListener) listener).enterMinMaxExpression(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof PostBbqListener)
                ((PostBbqListener) listener).exitMinMaxExpression(this);
        }
    }

    public final MinMaxExpressionContext minMaxExpression() throws RecognitionException {
        MinMaxExpressionContext _localctx = new MinMaxExpressionContext(_ctx, getState());
        enterRule(_localctx, 12, RULE_minMaxExpression);
        int _la;
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(55);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__5))) != 0)) {
                    {
                        {
                            setState(52);
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
                    setState(57);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(58);
                match(MINMAX);
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
                match(T__7);
                setState(66);
                expression();
                setState(77);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == T__8) {
                    {
                        {
                            setState(67);
                            match(T__8);
                            setState(71);
                            _errHandler.sync(this);
                            _alt = getInterpreter().adaptivePredict(_input, 4, _ctx);
                            while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                                if (_alt == 1) {
                                    {
                                        {
                                            setState(68);
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
                                setState(73);
                                _errHandler.sync(this);
                                _alt = getInterpreter().adaptivePredict(_input, 4, _ctx);
                            }
                            setState(74);
                            expression();
                        }
                    }
                    setState(79);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(83);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__5))) != 0)) {
                    {
                        {
                            setState(80);
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
                    setState(85);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(86);
                match(T__9);
                setState(90);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__5))) != 0)) {
                    {
                        {
                            setState(87);
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
                    setState(92);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(93);
                lowerBound();
                setState(97);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__5))) != 0)) {
                    {
                        {
                            setState(94);
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
                    setState(99);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(114);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == T__9) {
                    {
                        setState(100);
                        match(T__9);
                        setState(104);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                        while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__5))) != 0)) {
                            {
                                {
                                    setState(101);
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
                            setState(106);
                            _errHandler.sync(this);
                            _la = _input.LA(1);
                        }
                        setState(107);
                        upperBound();
                        setState(111);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                        while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__5))) != 0)) {
                            {
                                {
                                    setState(108);
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
                            setState(113);
                            _errHandler.sync(this);
                            _la = _input.LA(1);
                        }
                    }
                }

                setState(116);
                match(T__10);
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

        public MinMaxExpressionContext minMaxExpression() {
            return getRuleContext(MinMaxExpressionContext.class, 0);
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
        enterRule(_localctx, 14, RULE_unionExpression);
        int _la;
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(121);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__5))) != 0)) {
                    {
                        {
                            setState(118);
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
                    setState(123);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(124);
                match(UNION);
                setState(128);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 13, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        {
                            {
                                setState(125);
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
                    setState(130);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 13, _ctx);
                }
                setState(134);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 14, _ctx)) {
                case 1: {
                    setState(131);
                    bracedExpression();
                }
                    break;
                case 2: {
                    setState(132);
                    reference();
                }
                    break;
                case 3: {
                    setState(133);
                    minMaxExpression();
                }
                    break;
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

        public MinMaxExpressionContext minMaxExpression() {
            return getRuleContext(MinMaxExpressionContext.class, 0);
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
        enterRule(_localctx, 16, RULE_intersectExpression);
        int _la;
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(139);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__5))) != 0)) {
                    {
                        {
                            setState(136);
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
                    setState(141);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(142);
                match(INTERSECT);
                setState(146);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 16, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        {
                            {
                                setState(143);
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
                    setState(148);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 16, _ctx);
                }
                setState(152);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 17, _ctx)) {
                case 1: {
                    setState(149);
                    bracedExpression();
                }
                    break;
                case 2: {
                    setState(150);
                    reference();
                }
                    break;
                case 3: {
                    setState(151);
                    minMaxExpression();
                }
                    break;
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

        public MinMaxExpressionContext minMaxExpression() {
            return getRuleContext(MinMaxExpressionContext.class, 0);
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
        enterRule(_localctx, 18, RULE_minusExpression);
        int _la;
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(157);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__5))) != 0)) {
                    {
                        {
                            setState(154);
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
                    setState(159);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(160);
                match(MINUS);
                setState(164);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 19, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        {
                            {
                                setState(161);
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
                    setState(166);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 19, _ctx);
                }
                setState(170);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 20, _ctx)) {
                case 1: {
                    setState(167);
                    bracedExpression();
                }
                    break;
                case 2: {
                    setState(168);
                    reference();
                }
                    break;
                case 3: {
                    setState(169);
                    minMaxExpression();
                }
                    break;
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
        enterRule(_localctx, 20, RULE_bracedExpression);
        int _la;
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(175);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__5))) != 0)) {
                    {
                        {
                            setState(172);
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
                    setState(177);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(178);
                match(T__7);
                setState(179);
                expression();
                setState(180);
                match(T__10);
                setState(184);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 22, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        {
                            {
                                setState(181);
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
                    setState(186);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 22, _ctx);
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
        public MinMaxExpressionContext minMaxExpression() {
            return getRuleContext(MinMaxExpressionContext.class, 0);
        }

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
        enterRule(_localctx, 22, RULE_expression);
        try {
            int _alt;
            setState(211);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 28, _ctx)) {
            case 1:
                enterOuterAlt(_localctx, 1); {
                setState(187);
                minMaxExpression();
            }
                break;
            case 2:
                enterOuterAlt(_localctx, 2); {
                setState(188);
                reference();
            }
                break;
            case 3:
                enterOuterAlt(_localctx, 3); {
                setState(192);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 23, _ctx)) {
                case 1: {
                    setState(189);
                    minMaxExpression();
                }
                    break;
                case 2: {
                    setState(190);
                    reference();
                }
                    break;
                case 3: {
                    setState(191);
                    bracedExpression();
                }
                    break;
                }
                setState(209);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 27, _ctx)) {
                case 1: {
                    setState(195);
                    _errHandler.sync(this);
                    _alt = 1;
                    do {
                        switch (_alt) {
                        case 1: {
                            {
                                setState(194);
                                unionExpression();
                            }
                        }
                            break;
                        default:
                            throw new NoViableAltException(this);
                        }
                        setState(197);
                        _errHandler.sync(this);
                        _alt = getInterpreter().adaptivePredict(_input, 24, _ctx);
                    } while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER);
                }
                    break;
                case 2: {
                    setState(200);
                    _errHandler.sync(this);
                    _alt = 1;
                    do {
                        switch (_alt) {
                        case 1: {
                            {
                                setState(199);
                                intersectExpression();
                            }
                        }
                            break;
                        default:
                            throw new NoViableAltException(this);
                        }
                        setState(202);
                        _errHandler.sync(this);
                        _alt = getInterpreter().adaptivePredict(_input, 25, _ctx);
                    } while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER);
                }
                    break;
                case 3: {
                    setState(205);
                    _errHandler.sync(this);
                    _alt = 1;
                    do {
                        switch (_alt) {
                        case 1: {
                            {
                                setState(204);
                                minusExpression();
                            }
                        }
                            break;
                        default:
                            throw new NoViableAltException(this);
                        }
                        setState(207);
                        _errHandler.sync(this);
                        _alt = getInterpreter().adaptivePredict(_input, 26, _ctx);
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
        enterRule(_localctx, 24, RULE_query);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(213);
                expression();
                setState(217);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__5))) != 0)) {
                    {
                        {
                            setState(214);
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
                    setState(219);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(220);
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

    public static final String _serializedATN = "\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\27\u00e1\4\2\t\2"
            + "\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13" + "\t\13\4\f\t\f\4\r\t\r\4\16\t\16\3\2\3\2\3\2\7\2 \n\2\f\2\16\2#\13\2\3"
            + "\3\7\3&\n\3\f\3\16\3)\13\3\3\3\3\3\3\4\3\4\3\5\3\5\3\5\3\5\3\6\3\6\3\7"
            + "\3\7\3\b\7\b8\n\b\f\b\16\b;\13\b\3\b\3\b\7\b?\n\b\f\b\16\bB\13\b\3\b\3"
            + "\b\3\b\3\b\7\bH\n\b\f\b\16\bK\13\b\3\b\7\bN\n\b\f\b\16\bQ\13\b\3\b\7\b" + "T\n\b\f\b\16\bW\13\b\3\b\3\b\7\b[\n\b\f\b\16\b^\13\b\3\b\3\b\7\bb\n\b"
            + "\f\b\16\be\13\b\3\b\3\b\7\bi\n\b\f\b\16\bl\13\b\3\b\3\b\7\bp\n\b\f\b\16"
            + "\bs\13\b\5\bu\n\b\3\b\3\b\3\t\7\tz\n\t\f\t\16\t}\13\t\3\t\3\t\7\t\u0081"
            + "\n\t\f\t\16\t\u0084\13\t\3\t\3\t\3\t\5\t\u0089\n\t\3\n\7\n\u008c\n\n\f"
            + "\n\16\n\u008f\13\n\3\n\3\n\7\n\u0093\n\n\f\n\16\n\u0096\13\n\3\n\3\n\3"
            + "\n\5\n\u009b\n\n\3\13\7\13\u009e\n\13\f\13\16\13\u00a1\13\13\3\13\3\13"
            + "\7\13\u00a5\n\13\f\13\16\13\u00a8\13\13\3\13\3\13\3\13\5\13\u00ad\n\13"
            + "\3\f\7\f\u00b0\n\f\f\f\16\f\u00b3\13\f\3\f\3\f\3\f\3\f\7\f\u00b9\n\f\f"
            + "\f\16\f\u00bc\13\f\3\r\3\r\3\r\3\r\3\r\5\r\u00c3\n\r\3\r\6\r\u00c6\n\r"
            + "\r\r\16\r\u00c7\3\r\6\r\u00cb\n\r\r\r\16\r\u00cc\3\r\6\r\u00d0\n\r\r\r"
            + "\16\r\u00d1\5\r\u00d4\n\r\5\r\u00d6\n\r\3\16\3\16\7\16\u00da\n\16\f\16"
            + "\16\16\u00dd\13\16\3\16\3\16\3\16\2\2\17\2\4\6\b\n\f\16\20\22\24\26\30" + "\32\2\4\3\2\5\b\3\2\25\27\2\u00f7\2\34\3\2\2\2\4\'\3\2\2\2\6,\3\2\2\2"
            + "\b.\3\2\2\2\n\62\3\2\2\2\f\64\3\2\2\2\169\3\2\2\2\20{\3\2\2\2\22\u008d"
            + "\3\2\2\2\24\u009f\3\2\2\2\26\u00b1\3\2\2\2\30\u00d5\3\2\2\2\32\u00d7\3" + "\2\2\2\34\35\7\3\2\2\35!\7\4\2\2\36 \t\2\2\2\37\36\3\2\2\2 #\3\2\2\2!"
            + "\37\3\2\2\2!\"\3\2\2\2\"\3\3\2\2\2#!\3\2\2\2$&\t\2\2\2%$\3\2\2\2&)\3\2" + "\2\2\'%\3\2\2\2\'(\3\2\2\2(*\3\2\2\2)\'\3\2\2\2*+\7\t\2\2+\5\3\2\2\2,"
            + "-\t\3\2\2-\7\3\2\2\2./\5\2\2\2/\60\5\6\4\2\60\61\5\4\3\2\61\t\3\2\2\2" + "\62\63\7\25\2\2\63\13\3\2\2\2\64\65\7\25\2\2\65\r\3\2\2\2\668\t\2\2\2"
            + "\67\66\3\2\2\28;\3\2\2\29\67\3\2\2\29:\3\2\2\2:<\3\2\2\2;9\3\2\2\2<@\7" + "\24\2\2=?\t\2\2\2>=\3\2\2\2?B\3\2\2\2@>\3\2\2\2@A\3\2\2\2AC\3\2\2\2B@"
            + "\3\2\2\2CD\7\n\2\2DO\5\30\r\2EI\7\13\2\2FH\t\2\2\2GF\3\2\2\2HK\3\2\2\2" + "IG\3\2\2\2IJ\3\2\2\2JL\3\2\2\2KI\3\2\2\2LN\5\30\r\2ME\3\2\2\2NQ\3\2\2"
            + "\2OM\3\2\2\2OP\3\2\2\2PU\3\2\2\2QO\3\2\2\2RT\t\2\2\2SR\3\2\2\2TW\3\2\2" + "\2US\3\2\2\2UV\3\2\2\2VX\3\2\2\2WU\3\2\2\2X\\\7\f\2\2Y[\t\2\2\2ZY\3\2"
            + "\2\2[^\3\2\2\2\\Z\3\2\2\2\\]\3\2\2\2]_\3\2\2\2^\\\3\2\2\2_c\5\n\6\2`b" + "\t\2\2\2a`\3\2\2\2be\3\2\2\2ca\3\2\2\2cd\3\2\2\2dt\3\2\2\2ec\3\2\2\2f"
            + "j\7\f\2\2gi\t\2\2\2hg\3\2\2\2il\3\2\2\2jh\3\2\2\2jk\3\2\2\2km\3\2\2\2" + "lj\3\2\2\2mq\5\f\7\2np\t\2\2\2on\3\2\2\2ps\3\2\2\2qo\3\2\2\2qr\3\2\2\2"
            + "ru\3\2\2\2sq\3\2\2\2tf\3\2\2\2tu\3\2\2\2uv\3\2\2\2vw\7\r\2\2w\17\3\2\2"
            + "\2xz\t\2\2\2yx\3\2\2\2z}\3\2\2\2{y\3\2\2\2{|\3\2\2\2|~\3\2\2\2}{\3\2\2"
            + "\2~\u0082\7\21\2\2\177\u0081\t\2\2\2\u0080\177\3\2\2\2\u0081\u0084\3\2"
            + "\2\2\u0082\u0080\3\2\2\2\u0082\u0083\3\2\2\2\u0083\u0088\3\2\2\2\u0084" + "\u0082\3\2\2\2\u0085\u0089\5\26\f\2\u0086\u0089\5\b\5\2\u0087\u0089\5"
            + "\16\b\2\u0088\u0085\3\2\2\2\u0088\u0086\3\2\2\2\u0088\u0087\3\2\2\2\u0089"
            + "\21\3\2\2\2\u008a\u008c\t\2\2\2\u008b\u008a\3\2\2\2\u008c\u008f\3\2\2"
            + "\2\u008d\u008b\3\2\2\2\u008d\u008e\3\2\2\2\u008e\u0090\3\2\2\2\u008f\u008d"
            + "\3\2\2\2\u0090\u0094\7\22\2\2\u0091\u0093\t\2\2\2\u0092\u0091\3\2\2\2"
            + "\u0093\u0096\3\2\2\2\u0094\u0092\3\2\2\2\u0094\u0095\3\2\2\2\u0095\u009a"
            + "\3\2\2\2\u0096\u0094\3\2\2\2\u0097\u009b\5\26\f\2\u0098\u009b\5\b\5\2"
            + "\u0099\u009b\5\16\b\2\u009a\u0097\3\2\2\2\u009a\u0098\3\2\2\2\u009a\u0099"
            + "\3\2\2\2\u009b\23\3\2\2\2\u009c\u009e\t\2\2\2\u009d\u009c\3\2\2\2\u009e"
            + "\u00a1\3\2\2\2\u009f\u009d\3\2\2\2\u009f\u00a0\3\2\2\2\u00a0\u00a2\3\2"
            + "\2\2\u00a1\u009f\3\2\2\2\u00a2\u00a6\7\23\2\2\u00a3\u00a5\t\2\2\2\u00a4"
            + "\u00a3\3\2\2\2\u00a5\u00a8\3\2\2\2\u00a6\u00a4\3\2\2\2\u00a6\u00a7\3\2"
            + "\2\2\u00a7\u00ac\3\2\2\2\u00a8\u00a6\3\2\2\2\u00a9\u00ad\5\26\f\2\u00aa"
            + "\u00ad\5\b\5\2\u00ab\u00ad\5\16\b\2\u00ac\u00a9\3\2\2\2\u00ac\u00aa\3" + "\2\2\2\u00ac\u00ab\3\2\2\2\u00ad\25\3\2\2\2\u00ae\u00b0\t\2\2\2\u00af"
            + "\u00ae\3\2\2\2\u00b0\u00b3\3\2\2\2\u00b1\u00af\3\2\2\2\u00b1\u00b2\3\2"
            + "\2\2\u00b2\u00b4\3\2\2\2\u00b3\u00b1\3\2\2\2\u00b4\u00b5\7\n\2\2\u00b5" + "\u00b6\5\30\r\2\u00b6\u00ba\7\r\2\2\u00b7\u00b9\t\2\2\2\u00b8\u00b7\3"
            + "\2\2\2\u00b9\u00bc\3\2\2\2\u00ba\u00b8\3\2\2\2\u00ba\u00bb\3\2\2\2\u00bb"
            + "\27\3\2\2\2\u00bc\u00ba\3\2\2\2\u00bd\u00d6\5\16\b\2\u00be\u00d6\5\b\5"
            + "\2\u00bf\u00c3\5\16\b\2\u00c0\u00c3\5\b\5\2\u00c1\u00c3\5\26\f\2\u00c2"
            + "\u00bf\3\2\2\2\u00c2\u00c0\3\2\2\2\u00c2\u00c1\3\2\2\2\u00c3\u00d3\3\2"
            + "\2\2\u00c4\u00c6\5\20\t\2\u00c5\u00c4\3\2\2\2\u00c6\u00c7\3\2\2\2\u00c7"
            + "\u00c5\3\2\2\2\u00c7\u00c8\3\2\2\2\u00c8\u00d4\3\2\2\2\u00c9\u00cb\5\22"
            + "\n\2\u00ca\u00c9\3\2\2\2\u00cb\u00cc\3\2\2\2\u00cc\u00ca\3\2\2\2\u00cc"
            + "\u00cd\3\2\2\2\u00cd\u00d4\3\2\2\2\u00ce\u00d0\5\24\13\2\u00cf\u00ce\3"
            + "\2\2\2\u00d0\u00d1\3\2\2\2\u00d1\u00cf\3\2\2\2\u00d1\u00d2\3\2\2\2\u00d2"
            + "\u00d4\3\2\2\2\u00d3\u00c5\3\2\2\2\u00d3\u00ca\3\2\2\2\u00d3\u00cf\3\2"
            + "\2\2\u00d4\u00d6\3\2\2\2\u00d5\u00bd\3\2\2\2\u00d5\u00be\3\2\2\2\u00d5"
            + "\u00c2\3\2\2\2\u00d6\31\3\2\2\2\u00d7\u00db\5\30\r\2\u00d8\u00da\t\2\2"
            + "\2\u00d9\u00d8\3\2\2\2\u00da\u00dd\3\2\2\2\u00db\u00d9\3\2\2\2\u00db\u00dc"
            + "\3\2\2\2\u00dc\u00de\3\2\2\2\u00dd\u00db\3\2\2\2\u00de\u00df\7\2\2\3\u00df"
            + "\33\3\2\2\2 !\'9@IOU\\cjqt{\u0082\u0088\u008d\u0094\u009a\u009f\u00a6" + "\u00ac\u00b1\u00ba\u00c2\u00c7\u00cc\u00d1\u00d3\u00d5\u00db";
    public static final ATN _ATN = new ATNDeserializer().deserialize(_serializedATN.toCharArray());
    static {
        _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
        for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
            _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
        }
    }
}