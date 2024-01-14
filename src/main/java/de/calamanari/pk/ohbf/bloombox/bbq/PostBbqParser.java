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

//Generated from PostBbq.g4 by ANTLR 4.13.1

@SuppressWarnings({ "all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue" })
public class PostBbqParser extends Parser {
    static {
        RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION);
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
        return new String[] { null, "'$'", "'{'", "' '", "'\\t'", "'\\r'", "'\\n'", "'}'", "'('", "','", "';'", "')'" };
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

    @SuppressWarnings("CheckReturnValue")
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
            if (listener instanceof PostBbqListener) {
                ((PostBbqListener) listener).enterRefStart(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof PostBbqListener) {
                ((PostBbqListener) listener).exitRefStart(this);
            }
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
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 120L) != 0)) {
                    {
                        {
                            setState(28);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 120L) != 0))) {
                                _errHandler.recoverInline(this);
                            }
                            else {
                                if (_input.LA(1) == Token.EOF) {
                                    matchedEOF = true;
                                }
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

    @SuppressWarnings("CheckReturnValue")
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
            if (listener instanceof PostBbqListener) {
                ((PostBbqListener) listener).enterRefEnd(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof PostBbqListener) {
                ((PostBbqListener) listener).exitRefEnd(this);
            }
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
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 120L) != 0)) {
                    {
                        {
                            setState(34);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 120L) != 0))) {
                                _errHandler.recoverInline(this);
                            }
                            else {
                                if (_input.LA(1) == Token.EOF) {
                                    matchedEOF = true;
                                }
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

    @SuppressWarnings("CheckReturnValue")
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
            if (listener instanceof PostBbqListener) {
                ((PostBbqListener) listener).enterSource(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof PostBbqListener) {
                ((PostBbqListener) listener).exitSource(this);
            }
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
                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 3670016L) != 0))) {
                    _errHandler.recoverInline(this);
                }
                else {
                    if (_input.LA(1) == Token.EOF) {
                        matchedEOF = true;
                    }
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

    @SuppressWarnings("CheckReturnValue")
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
            if (listener instanceof PostBbqListener) {
                ((PostBbqListener) listener).enterReference(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof PostBbqListener) {
                ((PostBbqListener) listener).exitReference(this);
            }
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

    @SuppressWarnings("CheckReturnValue")
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
            if (listener instanceof PostBbqListener) {
                ((PostBbqListener) listener).enterLowerBound(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof PostBbqListener) {
                ((PostBbqListener) listener).exitLowerBound(this);
            }
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

    @SuppressWarnings("CheckReturnValue")
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
            if (listener instanceof PostBbqListener) {
                ((PostBbqListener) listener).enterUpperBound(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof PostBbqListener) {
                ((PostBbqListener) listener).exitUpperBound(this);
            }
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

    @SuppressWarnings("CheckReturnValue")
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
            if (listener instanceof PostBbqListener) {
                ((PostBbqListener) listener).enterMinMaxExpression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof PostBbqListener) {
                ((PostBbqListener) listener).exitMinMaxExpression(this);
            }
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
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 120L) != 0)) {
                    {
                        {
                            setState(52);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 120L) != 0))) {
                                _errHandler.recoverInline(this);
                            }
                            else {
                                if (_input.LA(1) == Token.EOF) {
                                    matchedEOF = true;
                                }
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
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 120L) != 0)) {
                    {
                        {
                            setState(59);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 120L) != 0))) {
                                _errHandler.recoverInline(this);
                            }
                            else {
                                if (_input.LA(1) == Token.EOF) {
                                    matchedEOF = true;
                                }
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
                                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 120L) != 0))) {
                                                _errHandler.recoverInline(this);
                                            }
                                            else {
                                                if (_input.LA(1) == Token.EOF) {
                                                    matchedEOF = true;
                                                }
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
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 120L) != 0)) {
                    {
                        {
                            setState(80);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 120L) != 0))) {
                                _errHandler.recoverInline(this);
                            }
                            else {
                                if (_input.LA(1) == Token.EOF) {
                                    matchedEOF = true;
                                }
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
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 120L) != 0)) {
                    {
                        {
                            setState(87);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 120L) != 0))) {
                                _errHandler.recoverInline(this);
                            }
                            else {
                                if (_input.LA(1) == Token.EOF) {
                                    matchedEOF = true;
                                }
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
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 120L) != 0)) {
                    {
                        {
                            setState(94);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 120L) != 0))) {
                                _errHandler.recoverInline(this);
                            }
                            else {
                                if (_input.LA(1) == Token.EOF) {
                                    matchedEOF = true;
                                }
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
                        while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 120L) != 0)) {
                            {
                                {
                                    setState(101);
                                    _la = _input.LA(1);
                                    if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 120L) != 0))) {
                                        _errHandler.recoverInline(this);
                                    }
                                    else {
                                        if (_input.LA(1) == Token.EOF) {
                                            matchedEOF = true;
                                        }
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
                        while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 120L) != 0)) {
                            {
                                {
                                    setState(108);
                                    _la = _input.LA(1);
                                    if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 120L) != 0))) {
                                        _errHandler.recoverInline(this);
                                    }
                                    else {
                                        if (_input.LA(1) == Token.EOF) {
                                            matchedEOF = true;
                                        }
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

    @SuppressWarnings("CheckReturnValue")
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
            if (listener instanceof PostBbqListener) {
                ((PostBbqListener) listener).enterUnionExpression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof PostBbqListener) {
                ((PostBbqListener) listener).exitUnionExpression(this);
            }
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
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 120L) != 0)) {
                    {
                        {
                            setState(118);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 120L) != 0))) {
                                _errHandler.recoverInline(this);
                            }
                            else {
                                if (_input.LA(1) == Token.EOF) {
                                    matchedEOF = true;
                                }
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
                                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 120L) != 0))) {
                                    _errHandler.recoverInline(this);
                                }
                                else {
                                    if (_input.LA(1) == Token.EOF) {
                                        matchedEOF = true;
                                    }
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

    @SuppressWarnings("CheckReturnValue")
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
            if (listener instanceof PostBbqListener) {
                ((PostBbqListener) listener).enterIntersectExpression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof PostBbqListener) {
                ((PostBbqListener) listener).exitIntersectExpression(this);
            }
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
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 120L) != 0)) {
                    {
                        {
                            setState(136);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 120L) != 0))) {
                                _errHandler.recoverInline(this);
                            }
                            else {
                                if (_input.LA(1) == Token.EOF) {
                                    matchedEOF = true;
                                }
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
                                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 120L) != 0))) {
                                    _errHandler.recoverInline(this);
                                }
                                else {
                                    if (_input.LA(1) == Token.EOF) {
                                        matchedEOF = true;
                                    }
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

    @SuppressWarnings("CheckReturnValue")
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
            if (listener instanceof PostBbqListener) {
                ((PostBbqListener) listener).enterMinusExpression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof PostBbqListener) {
                ((PostBbqListener) listener).exitMinusExpression(this);
            }
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
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 120L) != 0)) {
                    {
                        {
                            setState(154);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 120L) != 0))) {
                                _errHandler.recoverInline(this);
                            }
                            else {
                                if (_input.LA(1) == Token.EOF) {
                                    matchedEOF = true;
                                }
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
                                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 120L) != 0))) {
                                    _errHandler.recoverInline(this);
                                }
                                else {
                                    if (_input.LA(1) == Token.EOF) {
                                        matchedEOF = true;
                                    }
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

    @SuppressWarnings("CheckReturnValue")
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
            if (listener instanceof PostBbqListener) {
                ((PostBbqListener) listener).enterBracedExpression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof PostBbqListener) {
                ((PostBbqListener) listener).exitBracedExpression(this);
            }
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
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 120L) != 0)) {
                    {
                        {
                            setState(172);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 120L) != 0))) {
                                _errHandler.recoverInline(this);
                            }
                            else {
                                if (_input.LA(1) == Token.EOF) {
                                    matchedEOF = true;
                                }
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
                                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 120L) != 0))) {
                                    _errHandler.recoverInline(this);
                                }
                                else {
                                    if (_input.LA(1) == Token.EOF) {
                                        matchedEOF = true;
                                    }
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

    @SuppressWarnings("CheckReturnValue")
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
            if (listener instanceof PostBbqListener) {
                ((PostBbqListener) listener).enterExpression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof PostBbqListener) {
                ((PostBbqListener) listener).exitExpression(this);
            }
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

    @SuppressWarnings("CheckReturnValue")
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
            if (listener instanceof PostBbqListener) {
                ((PostBbqListener) listener).enterQuery(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof PostBbqListener) {
                ((PostBbqListener) listener).exitQuery(this);
            }
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
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 120L) != 0)) {
                    {
                        {
                            setState(214);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 120L) != 0))) {
                                _errHandler.recoverInline(this);
                            }
                            else {
                                if (_input.LA(1) == Token.EOF) {
                                    matchedEOF = true;
                                }
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

    public static final String _serializedATN = "\u0004\u0001\u0015\u00df\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001"
            + "\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004"
            + "\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007"
            + "\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b"
            + "\u0002\f\u0007\f\u0001\u0000\u0001\u0000\u0001\u0000\u0005\u0000\u001e"
            + "\b\u0000\n\u0000\f\u0000!\t\u0000\u0001\u0001\u0005\u0001$\b\u0001\n\u0001"
            + "\f\u0001\'\t\u0001\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001"
            + "\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001"
            + "\u0005\u0001\u0005\u0001\u0006\u0005\u00066\b\u0006\n\u0006\f\u00069\t"
            + "\u0006\u0001\u0006\u0001\u0006\u0005\u0006=\b\u0006\n\u0006\f\u0006@\t" + "\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0005\u0006F\b"
            + "\u0006\n\u0006\f\u0006I\t\u0006\u0001\u0006\u0005\u0006L\b\u0006\n\u0006"
            + "\f\u0006O\t\u0006\u0001\u0006\u0005\u0006R\b\u0006\n\u0006\f\u0006U\t" + "\u0006\u0001\u0006\u0001\u0006\u0005\u0006Y\b\u0006\n\u0006\f\u0006\\"
            + "\t\u0006\u0001\u0006\u0001\u0006\u0005\u0006`\b\u0006\n\u0006\f\u0006" + "c\t\u0006\u0001\u0006\u0001\u0006\u0005\u0006g\b\u0006\n\u0006\f\u0006"
            + "j\t\u0006\u0001\u0006\u0001\u0006\u0005\u0006n\b\u0006\n\u0006\f\u0006"
            + "q\t\u0006\u0003\u0006s\b\u0006\u0001\u0006\u0001\u0006\u0001\u0007\u0005"
            + "\u0007x\b\u0007\n\u0007\f\u0007{\t\u0007\u0001\u0007\u0001\u0007\u0005"
            + "\u0007\u007f\b\u0007\n\u0007\f\u0007\u0082\t\u0007\u0001\u0007\u0001\u0007"
            + "\u0001\u0007\u0003\u0007\u0087\b\u0007\u0001\b\u0005\b\u008a\b\b\n\b\f"
            + "\b\u008d\t\b\u0001\b\u0001\b\u0005\b\u0091\b\b\n\b\f\b\u0094\t\b\u0001"
            + "\b\u0001\b\u0001\b\u0003\b\u0099\b\b\u0001\t\u0005\t\u009c\b\t\n\t\f\t"
            + "\u009f\t\t\u0001\t\u0001\t\u0005\t\u00a3\b\t\n\t\f\t\u00a6\t\t\u0001\t"
            + "\u0001\t\u0001\t\u0003\t\u00ab\b\t\u0001\n\u0005\n\u00ae\b\n\n\n\f\n\u00b1"
            + "\t\n\u0001\n\u0001\n\u0001\n\u0001\n\u0005\n\u00b7\b\n\n\n\f\n\u00ba\t"
            + "\n\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0003\u000b"
            + "\u00c1\b\u000b\u0001\u000b\u0004\u000b\u00c4\b\u000b\u000b\u000b\f\u000b"
            + "\u00c5\u0001\u000b\u0004\u000b\u00c9\b\u000b\u000b\u000b\f\u000b\u00ca"
            + "\u0001\u000b\u0004\u000b\u00ce\b\u000b\u000b\u000b\f\u000b\u00cf\u0003"
            + "\u000b\u00d2\b\u000b\u0003\u000b\u00d4\b\u000b\u0001\f\u0001\f\u0005\f"
            + "\u00d8\b\f\n\f\f\f\u00db\t\f\u0001\f\u0001\f\u0001\f\u0000\u0000\r\u0000"
            + "\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u0000\u0002"
            + "\u0001\u0000\u0003\u0006\u0001\u0000\u0013\u0015\u00f5\u0000\u001a\u0001"
            + "\u0000\u0000\u0000\u0002%\u0001\u0000\u0000\u0000\u0004*\u0001\u0000\u0000"
            + "\u0000\u0006,\u0001\u0000\u0000\u0000\b0\u0001\u0000\u0000\u0000\n2\u0001"
            + "\u0000\u0000\u0000\f7\u0001\u0000\u0000\u0000\u000ey\u0001\u0000\u0000"
            + "\u0000\u0010\u008b\u0001\u0000\u0000\u0000\u0012\u009d\u0001\u0000\u0000"
            + "\u0000\u0014\u00af\u0001\u0000\u0000\u0000\u0016\u00d3\u0001\u0000\u0000"
            + "\u0000\u0018\u00d5\u0001\u0000\u0000\u0000\u001a\u001b\u0005\u0001\u0000"
            + "\u0000\u001b\u001f\u0005\u0002\u0000\u0000\u001c\u001e\u0007\u0000\u0000"
            + "\u0000\u001d\u001c\u0001\u0000\u0000\u0000\u001e!\u0001\u0000\u0000\u0000"
            + "\u001f\u001d\u0001\u0000\u0000\u0000\u001f \u0001\u0000\u0000\u0000 \u0001"
            + "\u0001\u0000\u0000\u0000!\u001f\u0001\u0000\u0000\u0000\"$\u0007\u0000"
            + "\u0000\u0000#\"\u0001\u0000\u0000\u0000$\'\u0001\u0000\u0000\u0000%#\u0001"
            + "\u0000\u0000\u0000%&\u0001\u0000\u0000\u0000&(\u0001\u0000\u0000\u0000"
            + "\'%\u0001\u0000\u0000\u0000()\u0005\u0007\u0000\u0000)\u0003\u0001\u0000"
            + "\u0000\u0000*+\u0007\u0001\u0000\u0000+\u0005\u0001\u0000\u0000\u0000"
            + ",-\u0003\u0000\u0000\u0000-.\u0003\u0004\u0002\u0000./\u0003\u0002\u0001"
            + "\u0000/\u0007\u0001\u0000\u0000\u000001\u0005\u0013\u0000\u00001\t\u0001"
            + "\u0000\u0000\u000023\u0005\u0013\u0000\u00003\u000b\u0001\u0000\u0000"
            + "\u000046\u0007\u0000\u0000\u000054\u0001\u0000\u0000\u000069\u0001\u0000"
            + "\u0000\u000075\u0001\u0000\u0000\u000078\u0001\u0000\u0000\u00008:\u0001"
            + "\u0000\u0000\u000097\u0001\u0000\u0000\u0000:>\u0005\u0012\u0000\u0000"
            + ";=\u0007\u0000\u0000\u0000<;\u0001\u0000\u0000\u0000=@\u0001\u0000\u0000"
            + "\u0000><\u0001\u0000\u0000\u0000>?\u0001\u0000\u0000\u0000?A\u0001\u0000"
            + "\u0000\u0000@>\u0001\u0000\u0000\u0000AB\u0005\b\u0000\u0000BM\u0003\u0016"
            + "\u000b\u0000CG\u0005\t\u0000\u0000DF\u0007\u0000\u0000\u0000ED\u0001\u0000"
            + "\u0000\u0000FI\u0001\u0000\u0000\u0000GE\u0001\u0000\u0000\u0000GH\u0001"
            + "\u0000\u0000\u0000HJ\u0001\u0000\u0000\u0000IG\u0001\u0000\u0000\u0000"
            + "JL\u0003\u0016\u000b\u0000KC\u0001\u0000\u0000\u0000LO\u0001\u0000\u0000"
            + "\u0000MK\u0001\u0000\u0000\u0000MN\u0001\u0000\u0000\u0000NS\u0001\u0000"
            + "\u0000\u0000OM\u0001\u0000\u0000\u0000PR\u0007\u0000\u0000\u0000QP\u0001"
            + "\u0000\u0000\u0000RU\u0001\u0000\u0000\u0000SQ\u0001\u0000\u0000\u0000"
            + "ST\u0001\u0000\u0000\u0000TV\u0001\u0000\u0000\u0000US\u0001\u0000\u0000"
            + "\u0000VZ\u0005\n\u0000\u0000WY\u0007\u0000\u0000\u0000XW\u0001\u0000\u0000"
            + "\u0000Y\\\u0001\u0000\u0000\u0000ZX\u0001\u0000\u0000\u0000Z[\u0001\u0000"
            + "\u0000\u0000[]\u0001\u0000\u0000\u0000\\Z\u0001\u0000\u0000\u0000]a\u0003"
            + "\b\u0004\u0000^`\u0007\u0000\u0000\u0000_^\u0001\u0000\u0000\u0000`c\u0001"
            + "\u0000\u0000\u0000a_\u0001\u0000\u0000\u0000ab\u0001\u0000\u0000\u0000"
            + "br\u0001\u0000\u0000\u0000ca\u0001\u0000\u0000\u0000dh\u0005\n\u0000\u0000"
            + "eg\u0007\u0000\u0000\u0000fe\u0001\u0000\u0000\u0000gj\u0001\u0000\u0000"
            + "\u0000hf\u0001\u0000\u0000\u0000hi\u0001\u0000\u0000\u0000ik\u0001\u0000"
            + "\u0000\u0000jh\u0001\u0000\u0000\u0000ko\u0003\n\u0005\u0000ln\u0007\u0000"
            + "\u0000\u0000ml\u0001\u0000\u0000\u0000nq\u0001\u0000\u0000\u0000om\u0001"
            + "\u0000\u0000\u0000op\u0001\u0000\u0000\u0000ps\u0001\u0000\u0000\u0000"
            + "qo\u0001\u0000\u0000\u0000rd\u0001\u0000\u0000\u0000rs\u0001\u0000\u0000"
            + "\u0000st\u0001\u0000\u0000\u0000tu\u0005\u000b\u0000\u0000u\r\u0001\u0000"
            + "\u0000\u0000vx\u0007\u0000\u0000\u0000wv\u0001\u0000\u0000\u0000x{\u0001"
            + "\u0000\u0000\u0000yw\u0001\u0000\u0000\u0000yz\u0001\u0000\u0000\u0000"
            + "z|\u0001\u0000\u0000\u0000{y\u0001\u0000\u0000\u0000|\u0080\u0005\u000f"
            + "\u0000\u0000}\u007f\u0007\u0000\u0000\u0000~}\u0001\u0000\u0000\u0000"
            + "\u007f\u0082\u0001\u0000\u0000\u0000\u0080~\u0001\u0000\u0000\u0000\u0080"
            + "\u0081\u0001\u0000\u0000\u0000\u0081\u0086\u0001\u0000\u0000\u0000\u0082"
            + "\u0080\u0001\u0000\u0000\u0000\u0083\u0087\u0003\u0014\n\u0000\u0084\u0087"
            + "\u0003\u0006\u0003\u0000\u0085\u0087\u0003\f\u0006\u0000\u0086\u0083\u0001"
            + "\u0000\u0000\u0000\u0086\u0084\u0001\u0000\u0000\u0000\u0086\u0085\u0001"
            + "\u0000\u0000\u0000\u0087\u000f\u0001\u0000\u0000\u0000\u0088\u008a\u0007"
            + "\u0000\u0000\u0000\u0089\u0088\u0001\u0000\u0000\u0000\u008a\u008d\u0001"
            + "\u0000\u0000\u0000\u008b\u0089\u0001\u0000\u0000\u0000\u008b\u008c\u0001"
            + "\u0000\u0000\u0000\u008c\u008e\u0001\u0000\u0000\u0000\u008d\u008b\u0001"
            + "\u0000\u0000\u0000\u008e\u0092\u0005\u0010\u0000\u0000\u008f\u0091\u0007"
            + "\u0000\u0000\u0000\u0090\u008f\u0001\u0000\u0000\u0000\u0091\u0094\u0001"
            + "\u0000\u0000\u0000\u0092\u0090\u0001\u0000\u0000\u0000\u0092\u0093\u0001"
            + "\u0000\u0000\u0000\u0093\u0098\u0001\u0000\u0000\u0000\u0094\u0092\u0001"
            + "\u0000\u0000\u0000\u0095\u0099\u0003\u0014\n\u0000\u0096\u0099\u0003\u0006"
            + "\u0003\u0000\u0097\u0099\u0003\f\u0006\u0000\u0098\u0095\u0001\u0000\u0000"
            + "\u0000\u0098\u0096\u0001\u0000\u0000\u0000\u0098\u0097\u0001\u0000\u0000"
            + "\u0000\u0099\u0011\u0001\u0000\u0000\u0000\u009a\u009c\u0007\u0000\u0000"
            + "\u0000\u009b\u009a\u0001\u0000\u0000\u0000\u009c\u009f\u0001\u0000\u0000"
            + "\u0000\u009d\u009b\u0001\u0000\u0000\u0000\u009d\u009e\u0001\u0000\u0000"
            + "\u0000\u009e\u00a0\u0001\u0000\u0000\u0000\u009f\u009d\u0001\u0000\u0000"
            + "\u0000\u00a0\u00a4\u0005\u0011\u0000\u0000\u00a1\u00a3\u0007\u0000\u0000"
            + "\u0000\u00a2\u00a1\u0001\u0000\u0000\u0000\u00a3\u00a6\u0001\u0000\u0000"
            + "\u0000\u00a4\u00a2\u0001\u0000\u0000\u0000\u00a4\u00a5\u0001\u0000\u0000"
            + "\u0000\u00a5\u00aa\u0001\u0000\u0000\u0000\u00a6\u00a4\u0001\u0000\u0000"
            + "\u0000\u00a7\u00ab\u0003\u0014\n\u0000\u00a8\u00ab\u0003\u0006\u0003\u0000"
            + "\u00a9\u00ab\u0003\f\u0006\u0000\u00aa\u00a7\u0001\u0000\u0000\u0000\u00aa"
            + "\u00a8\u0001\u0000\u0000\u0000\u00aa\u00a9\u0001\u0000\u0000\u0000\u00ab"
            + "\u0013\u0001\u0000\u0000\u0000\u00ac\u00ae\u0007\u0000\u0000\u0000\u00ad"
            + "\u00ac\u0001\u0000\u0000\u0000\u00ae\u00b1\u0001\u0000\u0000\u0000\u00af"
            + "\u00ad\u0001\u0000\u0000\u0000\u00af\u00b0\u0001\u0000\u0000\u0000\u00b0"
            + "\u00b2\u0001\u0000\u0000\u0000\u00b1\u00af\u0001\u0000\u0000\u0000\u00b2"
            + "\u00b3\u0005\b\u0000\u0000\u00b3\u00b4\u0003\u0016\u000b\u0000\u00b4\u00b8"
            + "\u0005\u000b\u0000\u0000\u00b5\u00b7\u0007\u0000\u0000\u0000\u00b6\u00b5"
            + "\u0001\u0000\u0000\u0000\u00b7\u00ba\u0001\u0000\u0000\u0000\u00b8\u00b6"
            + "\u0001\u0000\u0000\u0000\u00b8\u00b9\u0001\u0000\u0000\u0000\u00b9\u0015"
            + "\u0001\u0000\u0000\u0000\u00ba\u00b8\u0001\u0000\u0000\u0000\u00bb\u00d4"
            + "\u0003\f\u0006\u0000\u00bc\u00d4\u0003\u0006\u0003\u0000\u00bd\u00c1\u0003"
            + "\f\u0006\u0000\u00be\u00c1\u0003\u0006\u0003\u0000\u00bf\u00c1\u0003\u0014"
            + "\n\u0000\u00c0\u00bd\u0001\u0000\u0000\u0000\u00c0\u00be\u0001\u0000\u0000"
            + "\u0000\u00c0\u00bf\u0001\u0000\u0000\u0000\u00c1\u00d1\u0001\u0000\u0000"
            + "\u0000\u00c2\u00c4\u0003\u000e\u0007\u0000\u00c3\u00c2\u0001\u0000\u0000"
            + "\u0000\u00c4\u00c5\u0001\u0000\u0000\u0000\u00c5\u00c3\u0001\u0000\u0000"
            + "\u0000\u00c5\u00c6\u0001\u0000\u0000\u0000\u00c6\u00d2\u0001\u0000\u0000"
            + "\u0000\u00c7\u00c9\u0003\u0010\b\u0000\u00c8\u00c7\u0001\u0000\u0000\u0000"
            + "\u00c9\u00ca\u0001\u0000\u0000\u0000\u00ca\u00c8\u0001\u0000\u0000\u0000"
            + "\u00ca\u00cb\u0001\u0000\u0000\u0000\u00cb\u00d2\u0001\u0000\u0000\u0000"
            + "\u00cc\u00ce\u0003\u0012\t\u0000\u00cd\u00cc\u0001\u0000\u0000\u0000\u00ce"
            + "\u00cf\u0001\u0000\u0000\u0000\u00cf\u00cd\u0001\u0000\u0000\u0000\u00cf"
            + "\u00d0\u0001\u0000\u0000\u0000\u00d0\u00d2\u0001\u0000\u0000\u0000\u00d1"
            + "\u00c3\u0001\u0000\u0000\u0000\u00d1\u00c8\u0001\u0000\u0000\u0000\u00d1"
            + "\u00cd\u0001\u0000\u0000\u0000\u00d2\u00d4\u0001\u0000\u0000\u0000\u00d3"
            + "\u00bb\u0001\u0000\u0000\u0000\u00d3\u00bc\u0001\u0000\u0000\u0000\u00d3"
            + "\u00c0\u0001\u0000\u0000\u0000\u00d4\u0017\u0001\u0000\u0000\u0000\u00d5"
            + "\u00d9\u0003\u0016\u000b\u0000\u00d6\u00d8\u0007\u0000\u0000\u0000\u00d7"
            + "\u00d6\u0001\u0000\u0000\u0000\u00d8\u00db\u0001\u0000\u0000\u0000\u00d9"
            + "\u00d7\u0001\u0000\u0000\u0000\u00d9\u00da\u0001\u0000\u0000\u0000\u00da"
            + "\u00dc\u0001\u0000\u0000\u0000\u00db\u00d9\u0001\u0000\u0000\u0000\u00dc"
            + "\u00dd\u0005\u0000\u0000\u0001\u00dd\u0019\u0001\u0000\u0000\u0000\u001e"
            + "\u001f%7>GMSZahory\u0080\u0086\u008b\u0092\u0098\u009d\u00a4\u00aa\u00af" + "\u00b8\u00c0\u00c5\u00ca\u00cf\u00d1\u00d3\u00d9";
    public static final ATN _ATN = new ATNDeserializer().deserialize(_serializedATN.toCharArray());
    static {
        _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
        for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
            _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
        }
    }
}