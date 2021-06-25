//@formatter:off
/*
 * BbqParser
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
public class BbqParser extends Parser {
    static {
        RuntimeMetaData.checkVersion("4.9.2", RuntimeMetaData.VERSION);
    }

    protected static final DFA[] _decisionToDFA;
    protected static final PredictionContextCache _sharedContextCache = new PredictionContextCache();
    public static final int T__0 = 1, T__1 = 2, T__2 = 3, T__3 = 4, T__4 = 5, T__5 = 6, T__6 = 7, T__7 = 8, T__8 = 9, T__9 = 10, ESCESC = 11, ESCSQ = 12,
            ESCDQ = 13, AND = 14, OR = 15, NOT = 16, IN = 17, MINMAX = 18, SIMPLE_WORD = 19, SQWORD = 20, DQWORD = 21;
    public static final int RULE_argName = 0, RULE_argValue = 1, RULE_lowerBound = 2, RULE_upperBound = 3, RULE_inValue = 4, RULE_nextValue = 5,
            RULE_cmpEquals = 6, RULE_cmpNotEquals = 7, RULE_cmpIn = 8, RULE_cmpNotIn = 9, RULE_minMaxExpression = 10, RULE_expressionDetails = 11,
            RULE_orExpression = 12, RULE_andExpression = 13, RULE_expression = 14, RULE_bracedExpression = 15, RULE_notExpression = 16, RULE_bbqDetails = 17,
            RULE_andBBQ = 18, RULE_orBBQ = 19, RULE_fullBBQ = 20, RULE_query = 21;

    private static String[] makeRuleNames() {
        return new String[] { "argName", "argValue", "lowerBound", "upperBound", "inValue", "nextValue", "cmpEquals", "cmpNotEquals", "cmpIn", "cmpNotIn",
                "minMaxExpression", "expressionDetails", "orExpression", "andExpression", "expression", "bracedExpression", "notExpression", "bbqDetails",
                "andBBQ", "orBBQ", "fullBBQ", "query" };
    }

    public static final String[] ruleNames = makeRuleNames();

    private static String[] makeLiteralNames() {
        return new String[] { null, "' '", "'\t'", "'\r'", "'\n'", "','", "'='", "'!'", "'('", "';'", "')'" };
    }

    private static final String[] _LITERAL_NAMES = makeLiteralNames();

    private static String[] makeSymbolicNames() {
        return new String[] { null, null, null, null, null, null, null, null, null, null, null, "ESCESC", "ESCSQ", "ESCDQ", "AND", "OR", "NOT", "IN", "MINMAX",
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
        return "Bbq.g4";
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

    public BbqParser(TokenStream input) {
        super(input);
        _interp = new ParserATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
    }

    public static class ArgNameContext extends ParserRuleContext {
        public TerminalNode SIMPLE_WORD() {
            return getToken(BbqParser.SIMPLE_WORD, 0);
        }

        public TerminalNode SQWORD() {
            return getToken(BbqParser.SQWORD, 0);
        }

        public TerminalNode DQWORD() {
            return getToken(BbqParser.DQWORD, 0);
        }

        public ArgNameContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_argName;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof BbqListener)
                ((BbqListener) listener).enterArgName(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof BbqListener)
                ((BbqListener) listener).exitArgName(this);
        }
    }

    public final ArgNameContext argName() throws RecognitionException {
        ArgNameContext _localctx = new ArgNameContext(_ctx, getState());
        enterRule(_localctx, 0, RULE_argName);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(44);
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

    public static class ArgValueContext extends ParserRuleContext {
        public TerminalNode SIMPLE_WORD() {
            return getToken(BbqParser.SIMPLE_WORD, 0);
        }

        public TerminalNode SQWORD() {
            return getToken(BbqParser.SQWORD, 0);
        }

        public TerminalNode DQWORD() {
            return getToken(BbqParser.DQWORD, 0);
        }

        public ArgValueContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_argValue;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof BbqListener)
                ((BbqListener) listener).enterArgValue(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof BbqListener)
                ((BbqListener) listener).exitArgValue(this);
        }
    }

    public final ArgValueContext argValue() throws RecognitionException {
        ArgValueContext _localctx = new ArgValueContext(_ctx, getState());
        enterRule(_localctx, 2, RULE_argValue);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(46);
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

    public static class LowerBoundContext extends ParserRuleContext {
        public TerminalNode SIMPLE_WORD() {
            return getToken(BbqParser.SIMPLE_WORD, 0);
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
            if (listener instanceof BbqListener)
                ((BbqListener) listener).enterLowerBound(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof BbqListener)
                ((BbqListener) listener).exitLowerBound(this);
        }
    }

    public final LowerBoundContext lowerBound() throws RecognitionException {
        LowerBoundContext _localctx = new LowerBoundContext(_ctx, getState());
        enterRule(_localctx, 4, RULE_lowerBound);
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
            return getToken(BbqParser.SIMPLE_WORD, 0);
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
            if (listener instanceof BbqListener)
                ((BbqListener) listener).enterUpperBound(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof BbqListener)
                ((BbqListener) listener).exitUpperBound(this);
        }
    }

    public final UpperBoundContext upperBound() throws RecognitionException {
        UpperBoundContext _localctx = new UpperBoundContext(_ctx, getState());
        enterRule(_localctx, 6, RULE_upperBound);
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

    public static class InValueContext extends ParserRuleContext {
        public ArgValueContext argValue() {
            return getRuleContext(ArgValueContext.class, 0);
        }

        public InValueContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_inValue;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof BbqListener)
                ((BbqListener) listener).enterInValue(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof BbqListener)
                ((BbqListener) listener).exitInValue(this);
        }
    }

    public final InValueContext inValue() throws RecognitionException {
        InValueContext _localctx = new InValueContext(_ctx, getState());
        enterRule(_localctx, 8, RULE_inValue);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(55);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0)) {
                    {
                        {
                            setState(52);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0))) {
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
                argValue();
                setState(62);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0)) {
                    {
                        {
                            setState(59);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0))) {
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

    public static class NextValueContext extends ParserRuleContext {
        public InValueContext inValue() {
            return getRuleContext(InValueContext.class, 0);
        }

        public NextValueContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_nextValue;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof BbqListener)
                ((BbqListener) listener).enterNextValue(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof BbqListener)
                ((BbqListener) listener).exitNextValue(this);
        }
    }

    public final NextValueContext nextValue() throws RecognitionException {
        NextValueContext _localctx = new NextValueContext(_ctx, getState());
        enterRule(_localctx, 10, RULE_nextValue);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(65);
                match(T__4);
                setState(66);
                inValue();
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

    public static class CmpEqualsContext extends ParserRuleContext {
        public ArgNameContext argName() {
            return getRuleContext(ArgNameContext.class, 0);
        }

        public ArgValueContext argValue() {
            return getRuleContext(ArgValueContext.class, 0);
        }

        public CmpEqualsContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_cmpEquals;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof BbqListener)
                ((BbqListener) listener).enterCmpEquals(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof BbqListener)
                ((BbqListener) listener).exitCmpEquals(this);
        }
    }

    public final CmpEqualsContext cmpEquals() throws RecognitionException {
        CmpEqualsContext _localctx = new CmpEqualsContext(_ctx, getState());
        enterRule(_localctx, 12, RULE_cmpEquals);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(68);
                argName();
                setState(72);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0)) {
                    {
                        {
                            setState(69);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0))) {
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
                    setState(74);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(75);
                match(T__5);
                setState(79);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0)) {
                    {
                        {
                            setState(76);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0))) {
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
                argValue();
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

    public static class CmpNotEqualsContext extends ParserRuleContext {
        public ArgNameContext argName() {
            return getRuleContext(ArgNameContext.class, 0);
        }

        public ArgValueContext argValue() {
            return getRuleContext(ArgValueContext.class, 0);
        }

        public CmpNotEqualsContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_cmpNotEquals;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof BbqListener)
                ((BbqListener) listener).enterCmpNotEquals(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof BbqListener)
                ((BbqListener) listener).exitCmpNotEquals(this);
        }
    }

    public final CmpNotEqualsContext cmpNotEquals() throws RecognitionException {
        CmpNotEqualsContext _localctx = new CmpNotEqualsContext(_ctx, getState());
        enterRule(_localctx, 14, RULE_cmpNotEquals);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(84);
                argName();
                setState(88);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0)) {
                    {
                        {
                            setState(85);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0))) {
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
                    setState(90);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(91);
                match(T__6);
                setState(92);
                match(T__5);
                setState(96);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0)) {
                    {
                        {
                            setState(93);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0))) {
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
                argValue();
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

    public static class CmpInContext extends ParserRuleContext {
        public ArgNameContext argName() {
            return getRuleContext(ArgNameContext.class, 0);
        }

        public TerminalNode IN() {
            return getToken(BbqParser.IN, 0);
        }

        public InValueContext inValue() {
            return getRuleContext(InValueContext.class, 0);
        }

        public List<NextValueContext> nextValue() {
            return getRuleContexts(NextValueContext.class);
        }

        public NextValueContext nextValue(int i) {
            return getRuleContext(NextValueContext.class, i);
        }

        public LowerBoundContext lowerBound() {
            return getRuleContext(LowerBoundContext.class, 0);
        }

        public UpperBoundContext upperBound() {
            return getRuleContext(UpperBoundContext.class, 0);
        }

        public CmpInContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_cmpIn;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof BbqListener)
                ((BbqListener) listener).enterCmpIn(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof BbqListener)
                ((BbqListener) listener).exitCmpIn(this);
        }
    }

    public final CmpInContext cmpIn() throws RecognitionException {
        CmpInContext _localctx = new CmpInContext(_ctx, getState());
        enterRule(_localctx, 16, RULE_cmpIn);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(101);
                argName();
                setState(103);
                _errHandler.sync(this);
                _la = _input.LA(1);
                do {
                    {
                        {
                            setState(102);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0))) {
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
                    setState(105);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                } while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0));
                setState(107);
                match(IN);
                setState(111);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0)) {
                    {
                        {
                            setState(108);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0))) {
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
                setState(114);
                match(T__7);
                setState(115);
                inValue();
                setState(119);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == T__4) {
                    {
                        {
                            setState(116);
                            nextValue();
                        }
                    }
                    setState(121);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(136);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 11, _ctx)) {
                case 1: {
                    setState(122);
                    match(T__8);
                    setState(126);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0)) {
                        {
                            {
                                setState(123);
                                _la = _input.LA(1);
                                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0))) {
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
                        setState(128);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                    }
                    setState(129);
                    lowerBound();
                    setState(133);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0)) {
                        {
                            {
                                setState(130);
                                _la = _input.LA(1);
                                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0))) {
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
                        setState(135);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                    }
                }
                    break;
                }
                setState(152);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == T__8) {
                    {
                        setState(138);
                        match(T__8);
                        setState(142);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                        while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0)) {
                            {
                                {
                                    setState(139);
                                    _la = _input.LA(1);
                                    if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0))) {
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
                            setState(144);
                            _errHandler.sync(this);
                            _la = _input.LA(1);
                        }
                        setState(145);
                        upperBound();
                        setState(149);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                        while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0)) {
                            {
                                {
                                    setState(146);
                                    _la = _input.LA(1);
                                    if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0))) {
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
                            setState(151);
                            _errHandler.sync(this);
                            _la = _input.LA(1);
                        }
                    }
                }

                setState(154);
                match(T__9);
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

    public static class CmpNotInContext extends ParserRuleContext {
        public ArgNameContext argName() {
            return getRuleContext(ArgNameContext.class, 0);
        }

        public TerminalNode NOT() {
            return getToken(BbqParser.NOT, 0);
        }

        public TerminalNode IN() {
            return getToken(BbqParser.IN, 0);
        }

        public InValueContext inValue() {
            return getRuleContext(InValueContext.class, 0);
        }

        public List<NextValueContext> nextValue() {
            return getRuleContexts(NextValueContext.class);
        }

        public NextValueContext nextValue(int i) {
            return getRuleContext(NextValueContext.class, i);
        }

        public LowerBoundContext lowerBound() {
            return getRuleContext(LowerBoundContext.class, 0);
        }

        public UpperBoundContext upperBound() {
            return getRuleContext(UpperBoundContext.class, 0);
        }

        public CmpNotInContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_cmpNotIn;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof BbqListener)
                ((BbqListener) listener).enterCmpNotIn(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof BbqListener)
                ((BbqListener) listener).exitCmpNotIn(this);
        }
    }

    public final CmpNotInContext cmpNotIn() throws RecognitionException {
        CmpNotInContext _localctx = new CmpNotInContext(_ctx, getState());
        enterRule(_localctx, 18, RULE_cmpNotIn);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(156);
                argName();
                setState(158);
                _errHandler.sync(this);
                _la = _input.LA(1);
                do {
                    {
                        {
                            setState(157);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0))) {
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
                    setState(160);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                } while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0));
                setState(162);
                match(NOT);
                setState(164);
                _errHandler.sync(this);
                _la = _input.LA(1);
                do {
                    {
                        {
                            setState(163);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0))) {
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
                    setState(166);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                } while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0));
                setState(168);
                match(IN);
                setState(172);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0)) {
                    {
                        {
                            setState(169);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0))) {
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
                    setState(174);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(175);
                match(T__7);
                setState(176);
                inValue();
                setState(180);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == T__4) {
                    {
                        {
                            setState(177);
                            nextValue();
                        }
                    }
                    setState(182);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(197);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 21, _ctx)) {
                case 1: {
                    setState(183);
                    match(T__8);
                    setState(187);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0)) {
                        {
                            {
                                setState(184);
                                _la = _input.LA(1);
                                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0))) {
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
                        setState(189);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                    }
                    setState(190);
                    lowerBound();
                    setState(194);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0)) {
                        {
                            {
                                setState(191);
                                _la = _input.LA(1);
                                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0))) {
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
                        setState(196);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                    }
                }
                    break;
                }
                setState(213);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == T__8) {
                    {
                        setState(199);
                        match(T__8);
                        setState(203);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                        while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0)) {
                            {
                                {
                                    setState(200);
                                    _la = _input.LA(1);
                                    if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0))) {
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
                            setState(205);
                            _errHandler.sync(this);
                            _la = _input.LA(1);
                        }
                        setState(206);
                        upperBound();
                        setState(210);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                        while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0)) {
                            {
                                {
                                    setState(207);
                                    _la = _input.LA(1);
                                    if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0))) {
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
                            setState(212);
                            _errHandler.sync(this);
                            _la = _input.LA(1);
                        }
                    }
                }

                setState(215);
                match(T__9);
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
            return getToken(BbqParser.MINMAX, 0);
        }

        public List<BbqDetailsContext> bbqDetails() {
            return getRuleContexts(BbqDetailsContext.class);
        }

        public BbqDetailsContext bbqDetails(int i) {
            return getRuleContext(BbqDetailsContext.class, i);
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
            if (listener instanceof BbqListener)
                ((BbqListener) listener).enterMinMaxExpression(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof BbqListener)
                ((BbqListener) listener).exitMinMaxExpression(this);
        }
    }

    public final MinMaxExpressionContext minMaxExpression() throws RecognitionException {
        MinMaxExpressionContext _localctx = new MinMaxExpressionContext(_ctx, getState());
        enterRule(_localctx, 20, RULE_minMaxExpression);
        int _la;
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(220);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0)) {
                    {
                        {
                            setState(217);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0))) {
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
                    setState(222);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(223);
                match(MINMAX);
                setState(227);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0)) {
                    {
                        {
                            setState(224);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0))) {
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
                    setState(229);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(230);
                match(T__7);
                setState(231);
                bbqDetails();
                setState(242);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == T__4) {
                    {
                        {
                            setState(232);
                            match(T__4);
                            setState(236);
                            _errHandler.sync(this);
                            _alt = getInterpreter().adaptivePredict(_input, 27, _ctx);
                            while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                                if (_alt == 1) {
                                    {
                                        {
                                            setState(233);
                                            _la = _input.LA(1);
                                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0))) {
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
                                setState(238);
                                _errHandler.sync(this);
                                _alt = getInterpreter().adaptivePredict(_input, 27, _ctx);
                            }
                            setState(239);
                            bbqDetails();
                        }
                    }
                    setState(244);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(248);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0)) {
                    {
                        {
                            setState(245);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0))) {
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
                    setState(250);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(251);
                match(T__8);
                setState(255);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0)) {
                    {
                        {
                            setState(252);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0))) {
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
                    setState(257);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(258);
                lowerBound();
                setState(262);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0)) {
                    {
                        {
                            setState(259);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0))) {
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
                    setState(264);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(279);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == T__8) {
                    {
                        setState(265);
                        match(T__8);
                        setState(269);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                        while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0)) {
                            {
                                {
                                    setState(266);
                                    _la = _input.LA(1);
                                    if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0))) {
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
                            setState(271);
                            _errHandler.sync(this);
                            _la = _input.LA(1);
                        }
                        setState(272);
                        upperBound();
                        setState(276);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                        while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0)) {
                            {
                                {
                                    setState(273);
                                    _la = _input.LA(1);
                                    if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0))) {
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
                            setState(278);
                            _errHandler.sync(this);
                            _la = _input.LA(1);
                        }
                    }
                }

                setState(281);
                match(T__9);
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

    public static class ExpressionDetailsContext extends ParserRuleContext {
        public CmpEqualsContext cmpEquals() {
            return getRuleContext(CmpEqualsContext.class, 0);
        }

        public CmpNotEqualsContext cmpNotEquals() {
            return getRuleContext(CmpNotEqualsContext.class, 0);
        }

        public CmpInContext cmpIn() {
            return getRuleContext(CmpInContext.class, 0);
        }

        public CmpNotInContext cmpNotIn() {
            return getRuleContext(CmpNotInContext.class, 0);
        }

        public ExpressionDetailsContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_expressionDetails;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof BbqListener)
                ((BbqListener) listener).enterExpressionDetails(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof BbqListener)
                ((BbqListener) listener).exitExpressionDetails(this);
        }
    }

    public final ExpressionDetailsContext expressionDetails() throws RecognitionException {
        ExpressionDetailsContext _localctx = new ExpressionDetailsContext(_ctx, getState());
        enterRule(_localctx, 22, RULE_expressionDetails);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(286);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0)) {
                    {
                        {
                            setState(283);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0))) {
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
                    setState(288);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(293);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 36, _ctx)) {
                case 1: {
                    setState(289);
                    cmpEquals();
                }
                    break;
                case 2: {
                    setState(290);
                    cmpNotEquals();
                }
                    break;
                case 3: {
                    setState(291);
                    cmpIn();
                }
                    break;
                case 4: {
                    setState(292);
                    cmpNotIn();
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

    public static class OrExpressionContext extends ParserRuleContext {
        public TerminalNode OR() {
            return getToken(BbqParser.OR, 0);
        }

        public FullBBQContext fullBBQ() {
            return getRuleContext(FullBBQContext.class, 0);
        }

        public ExpressionDetailsContext expressionDetails() {
            return getRuleContext(ExpressionDetailsContext.class, 0);
        }

        public OrExpressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_orExpression;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof BbqListener)
                ((BbqListener) listener).enterOrExpression(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof BbqListener)
                ((BbqListener) listener).exitOrExpression(this);
        }
    }

    public final OrExpressionContext orExpression() throws RecognitionException {
        OrExpressionContext _localctx = new OrExpressionContext(_ctx, getState());
        enterRule(_localctx, 24, RULE_orExpression);
        int _la;
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(298);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0)) {
                    {
                        {
                            setState(295);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0))) {
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
                    setState(300);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(301);
                match(OR);
                setState(305);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 38, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        {
                            {
                                setState(302);
                                _la = _input.LA(1);
                                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0))) {
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
                    setState(307);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 38, _ctx);
                }
                setState(310);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 39, _ctx)) {
                case 1: {
                    setState(308);
                    fullBBQ();
                }
                    break;
                case 2: {
                    setState(309);
                    expressionDetails();
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

    public static class AndExpressionContext extends ParserRuleContext {
        public TerminalNode AND() {
            return getToken(BbqParser.AND, 0);
        }

        public FullBBQContext fullBBQ() {
            return getRuleContext(FullBBQContext.class, 0);
        }

        public ExpressionDetailsContext expressionDetails() {
            return getRuleContext(ExpressionDetailsContext.class, 0);
        }

        public AndExpressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_andExpression;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof BbqListener)
                ((BbqListener) listener).enterAndExpression(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof BbqListener)
                ((BbqListener) listener).exitAndExpression(this);
        }
    }

    public final AndExpressionContext andExpression() throws RecognitionException {
        AndExpressionContext _localctx = new AndExpressionContext(_ctx, getState());
        enterRule(_localctx, 26, RULE_andExpression);
        int _la;
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(315);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0)) {
                    {
                        {
                            setState(312);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0))) {
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
                    setState(317);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(318);
                match(AND);
                setState(322);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 41, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        {
                            {
                                setState(319);
                                _la = _input.LA(1);
                                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0))) {
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
                    setState(324);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 41, _ctx);
                }
                setState(327);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 42, _ctx)) {
                case 1: {
                    setState(325);
                    fullBBQ();
                }
                    break;
                case 2: {
                    setState(326);
                    expressionDetails();
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

    public static class ExpressionContext extends ParserRuleContext {
        public ExpressionDetailsContext expressionDetails() {
            return getRuleContext(ExpressionDetailsContext.class, 0);
        }

        public List<AndExpressionContext> andExpression() {
            return getRuleContexts(AndExpressionContext.class);
        }

        public AndExpressionContext andExpression(int i) {
            return getRuleContext(AndExpressionContext.class, i);
        }

        public List<OrExpressionContext> orExpression() {
            return getRuleContexts(OrExpressionContext.class);
        }

        public OrExpressionContext orExpression(int i) {
            return getRuleContext(OrExpressionContext.class, i);
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
            if (listener instanceof BbqListener)
                ((BbqListener) listener).enterExpression(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof BbqListener)
                ((BbqListener) listener).exitExpression(this);
        }
    }

    public final ExpressionContext expression() throws RecognitionException {
        ExpressionContext _localctx = new ExpressionContext(_ctx, getState());
        enterRule(_localctx, 28, RULE_expression);
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(329);
                expressionDetails();
                setState(334);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 44, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        {
                            setState(332);
                            _errHandler.sync(this);
                            switch (getInterpreter().adaptivePredict(_input, 43, _ctx)) {
                            case 1: {
                                setState(330);
                                andExpression();
                            }
                                break;
                            case 2: {
                                setState(331);
                                orExpression();
                            }
                                break;
                            }
                        }
                    }
                    setState(336);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 44, _ctx);
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
        public FullBBQContext fullBBQ() {
            return getRuleContext(FullBBQContext.class, 0);
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
            if (listener instanceof BbqListener)
                ((BbqListener) listener).enterBracedExpression(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof BbqListener)
                ((BbqListener) listener).exitBracedExpression(this);
        }
    }

    public final BracedExpressionContext bracedExpression() throws RecognitionException {
        BracedExpressionContext _localctx = new BracedExpressionContext(_ctx, getState());
        enterRule(_localctx, 30, RULE_bracedExpression);
        int _la;
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(340);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0)) {
                    {
                        {
                            setState(337);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0))) {
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
                    setState(342);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(343);
                match(T__7);
                setState(344);
                fullBBQ();
                setState(348);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0)) {
                    {
                        {
                            setState(345);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0))) {
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
                    setState(350);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(351);
                match(T__9);
                setState(355);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 47, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        {
                            {
                                setState(352);
                                _la = _input.LA(1);
                                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0))) {
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
                    setState(357);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 47, _ctx);
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

    public static class NotExpressionContext extends ParserRuleContext {
        public TerminalNode NOT() {
            return getToken(BbqParser.NOT, 0);
        }

        public FullBBQContext fullBBQ() {
            return getRuleContext(FullBBQContext.class, 0);
        }

        public NotExpressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_notExpression;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof BbqListener)
                ((BbqListener) listener).enterNotExpression(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof BbqListener)
                ((BbqListener) listener).exitNotExpression(this);
        }
    }

    public final NotExpressionContext notExpression() throws RecognitionException {
        NotExpressionContext _localctx = new NotExpressionContext(_ctx, getState());
        enterRule(_localctx, 32, RULE_notExpression);
        int _la;
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(361);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0)) {
                    {
                        {
                            setState(358);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0))) {
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
                    setState(363);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(364);
                match(NOT);
                setState(368);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0)) {
                    {
                        {
                            setState(365);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0))) {
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
                    setState(370);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(371);
                match(T__7);
                setState(372);
                fullBBQ();
                setState(376);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0)) {
                    {
                        {
                            setState(373);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0))) {
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
                    setState(378);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(379);
                match(T__9);
                setState(383);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 51, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        {
                            {
                                setState(380);
                                _la = _input.LA(1);
                                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0))) {
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
                    setState(385);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 51, _ctx);
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

    public static class BbqDetailsContext extends ParserRuleContext {
        public ExpressionContext expression() {
            return getRuleContext(ExpressionContext.class, 0);
        }

        public BracedExpressionContext bracedExpression() {
            return getRuleContext(BracedExpressionContext.class, 0);
        }

        public NotExpressionContext notExpression() {
            return getRuleContext(NotExpressionContext.class, 0);
        }

        public MinMaxExpressionContext minMaxExpression() {
            return getRuleContext(MinMaxExpressionContext.class, 0);
        }

        public BbqDetailsContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_bbqDetails;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof BbqListener)
                ((BbqListener) listener).enterBbqDetails(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof BbqListener)
                ((BbqListener) listener).exitBbqDetails(this);
        }
    }

    public final BbqDetailsContext bbqDetails() throws RecognitionException {
        BbqDetailsContext _localctx = new BbqDetailsContext(_ctx, getState());
        enterRule(_localctx, 34, RULE_bbqDetails);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(390);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 52, _ctx)) {
                case 1: {
                    setState(386);
                    expression();
                }
                    break;
                case 2: {
                    setState(387);
                    bracedExpression();
                }
                    break;
                case 3: {
                    setState(388);
                    notExpression();
                }
                    break;
                case 4: {
                    setState(389);
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

    public static class AndBBQContext extends ParserRuleContext {
        public TerminalNode AND() {
            return getToken(BbqParser.AND, 0);
        }

        public BbqDetailsContext bbqDetails() {
            return getRuleContext(BbqDetailsContext.class, 0);
        }

        public AndBBQContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_andBBQ;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof BbqListener)
                ((BbqListener) listener).enterAndBBQ(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof BbqListener)
                ((BbqListener) listener).exitAndBBQ(this);
        }
    }

    public final AndBBQContext andBBQ() throws RecognitionException {
        AndBBQContext _localctx = new AndBBQContext(_ctx, getState());
        enterRule(_localctx, 36, RULE_andBBQ);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(395);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0)) {
                    {
                        {
                            setState(392);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0))) {
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
                    setState(397);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(398);
                match(AND);
                setState(399);
                bbqDetails();
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

    public static class OrBBQContext extends ParserRuleContext {
        public TerminalNode OR() {
            return getToken(BbqParser.OR, 0);
        }

        public BbqDetailsContext bbqDetails() {
            return getRuleContext(BbqDetailsContext.class, 0);
        }

        public OrBBQContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_orBBQ;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof BbqListener)
                ((BbqListener) listener).enterOrBBQ(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof BbqListener)
                ((BbqListener) listener).exitOrBBQ(this);
        }
    }

    public final OrBBQContext orBBQ() throws RecognitionException {
        OrBBQContext _localctx = new OrBBQContext(_ctx, getState());
        enterRule(_localctx, 38, RULE_orBBQ);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(404);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0)) {
                    {
                        {
                            setState(401);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0))) {
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
                    setState(406);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(407);
                match(OR);
                setState(408);
                bbqDetails();
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

    public static class FullBBQContext extends ParserRuleContext {
        public BbqDetailsContext bbqDetails() {
            return getRuleContext(BbqDetailsContext.class, 0);
        }

        public List<AndBBQContext> andBBQ() {
            return getRuleContexts(AndBBQContext.class);
        }

        public AndBBQContext andBBQ(int i) {
            return getRuleContext(AndBBQContext.class, i);
        }

        public List<OrBBQContext> orBBQ() {
            return getRuleContexts(OrBBQContext.class);
        }

        public OrBBQContext orBBQ(int i) {
            return getRuleContext(OrBBQContext.class, i);
        }

        public FullBBQContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_fullBBQ;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof BbqListener)
                ((BbqListener) listener).enterFullBBQ(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof BbqListener)
                ((BbqListener) listener).exitFullBBQ(this);
        }
    }

    public final FullBBQContext fullBBQ() throws RecognitionException {
        FullBBQContext _localctx = new FullBBQContext(_ctx, getState());
        enterRule(_localctx, 40, RULE_fullBBQ);
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(410);
                bbqDetails();
                setState(415);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 56, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        {
                            setState(413);
                            _errHandler.sync(this);
                            switch (getInterpreter().adaptivePredict(_input, 55, _ctx)) {
                            case 1: {
                                setState(411);
                                andBBQ();
                            }
                                break;
                            case 2: {
                                setState(412);
                                orBBQ();
                            }
                                break;
                            }
                        }
                    }
                    setState(417);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 56, _ctx);
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

    public static class QueryContext extends ParserRuleContext {
        public FullBBQContext fullBBQ() {
            return getRuleContext(FullBBQContext.class, 0);
        }

        public TerminalNode EOF() {
            return getToken(BbqParser.EOF, 0);
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
            if (listener instanceof BbqListener)
                ((BbqListener) listener).enterQuery(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof BbqListener)
                ((BbqListener) listener).exitQuery(this);
        }
    }

    public final QueryContext query() throws RecognitionException {
        QueryContext _localctx = new QueryContext(_ctx, getState());
        enterRule(_localctx, 42, RULE_query);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(418);
                fullBBQ();
                setState(420);
                _errHandler.sync(this);
                _la = _input.LA(1);
                do {
                    {
                        {
                            setState(419);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0))) {
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
                    setState(422);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                } while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0));
                setState(424);
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

    public static final String _serializedATN = "\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\27\u01ad\4\2\t\2"
            + "\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"
            + "\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"
            + "\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\3\2\3\2\3\3\3\3\3\4"
            + "\3\4\3\5\3\5\3\6\7\68\n\6\f\6\16\6;\13\6\3\6\3\6\7\6?\n\6\f\6\16\6B\13"
            + "\6\3\7\3\7\3\7\3\b\3\b\7\bI\n\b\f\b\16\bL\13\b\3\b\3\b\7\bP\n\b\f\b\16"
            + "\bS\13\b\3\b\3\b\3\t\3\t\7\tY\n\t\f\t\16\t\\\13\t\3\t\3\t\3\t\7\ta\n\t" + "\f\t\16\td\13\t\3\t\3\t\3\n\3\n\6\nj\n\n\r\n\16\nk\3\n\3\n\7\np\n\n\f"
            + "\n\16\ns\13\n\3\n\3\n\3\n\7\nx\n\n\f\n\16\n{\13\n\3\n\3\n\7\n\177\n\n"
            + "\f\n\16\n\u0082\13\n\3\n\3\n\7\n\u0086\n\n\f\n\16\n\u0089\13\n\5\n\u008b"
            + "\n\n\3\n\3\n\7\n\u008f\n\n\f\n\16\n\u0092\13\n\3\n\3\n\7\n\u0096\n\n\f"
            + "\n\16\n\u0099\13\n\5\n\u009b\n\n\3\n\3\n\3\13\3\13\6\13\u00a1\n\13\r\13"
            + "\16\13\u00a2\3\13\3\13\6\13\u00a7\n\13\r\13\16\13\u00a8\3\13\3\13\7\13"
            + "\u00ad\n\13\f\13\16\13\u00b0\13\13\3\13\3\13\3\13\7\13\u00b5\n\13\f\13" + "\16\13\u00b8\13\13\3\13\3\13\7\13\u00bc\n\13\f\13\16\13\u00bf\13\13\3"
            + "\13\3\13\7\13\u00c3\n\13\f\13\16\13\u00c6\13\13\5\13\u00c8\n\13\3\13\3"
            + "\13\7\13\u00cc\n\13\f\13\16\13\u00cf\13\13\3\13\3\13\7\13\u00d3\n\13\f" + "\13\16\13\u00d6\13\13\5\13\u00d8\n\13\3\13\3\13\3\f\7\f\u00dd\n\f\f\f"
            + "\16\f\u00e0\13\f\3\f\3\f\7\f\u00e4\n\f\f\f\16\f\u00e7\13\f\3\f\3\f\3\f"
            + "\3\f\7\f\u00ed\n\f\f\f\16\f\u00f0\13\f\3\f\7\f\u00f3\n\f\f\f\16\f\u00f6"
            + "\13\f\3\f\7\f\u00f9\n\f\f\f\16\f\u00fc\13\f\3\f\3\f\7\f\u0100\n\f\f\f" + "\16\f\u0103\13\f\3\f\3\f\7\f\u0107\n\f\f\f\16\f\u010a\13\f\3\f\3\f\7\f"
            + "\u010e\n\f\f\f\16\f\u0111\13\f\3\f\3\f\7\f\u0115\n\f\f\f\16\f\u0118\13"
            + "\f\5\f\u011a\n\f\3\f\3\f\3\r\7\r\u011f\n\r\f\r\16\r\u0122\13\r\3\r\3\r"
            + "\3\r\3\r\5\r\u0128\n\r\3\16\7\16\u012b\n\16\f\16\16\16\u012e\13\16\3\16"
            + "\3\16\7\16\u0132\n\16\f\16\16\16\u0135\13\16\3\16\3\16\5\16\u0139\n\16"
            + "\3\17\7\17\u013c\n\17\f\17\16\17\u013f\13\17\3\17\3\17\7\17\u0143\n\17" + "\f\17\16\17\u0146\13\17\3\17\3\17\5\17\u014a\n\17\3\20\3\20\3\20\7\20"
            + "\u014f\n\20\f\20\16\20\u0152\13\20\3\21\7\21\u0155\n\21\f\21\16\21\u0158"
            + "\13\21\3\21\3\21\3\21\7\21\u015d\n\21\f\21\16\21\u0160\13\21\3\21\3\21"
            + "\7\21\u0164\n\21\f\21\16\21\u0167\13\21\3\22\7\22\u016a\n\22\f\22\16\22"
            + "\u016d\13\22\3\22\3\22\7\22\u0171\n\22\f\22\16\22\u0174\13\22\3\22\3\22"
            + "\3\22\7\22\u0179\n\22\f\22\16\22\u017c\13\22\3\22\3\22\7\22\u0180\n\22" + "\f\22\16\22\u0183\13\22\3\23\3\23\3\23\3\23\5\23\u0189\n\23\3\24\7\24"
            + "\u018c\n\24\f\24\16\24\u018f\13\24\3\24\3\24\3\24\3\25\7\25\u0195\n\25" + "\f\25\16\25\u0198\13\25\3\25\3\25\3\25\3\26\3\26\3\26\7\26\u01a0\n\26"
            + "\f\26\16\26\u01a3\13\26\3\27\3\27\6\27\u01a7\n\27\r\27\16\27\u01a8\3\27"
            + "\3\27\3\27\2\2\30\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,\2\4\3"
            + "\2\25\27\3\2\3\6\2\u01d4\2.\3\2\2\2\4\60\3\2\2\2\6\62\3\2\2\2\b\64\3\2"
            + "\2\2\n9\3\2\2\2\fC\3\2\2\2\16F\3\2\2\2\20V\3\2\2\2\22g\3\2\2\2\24\u009e"
            + "\3\2\2\2\26\u00de\3\2\2\2\30\u0120\3\2\2\2\32\u012c\3\2\2\2\34\u013d\3" + "\2\2\2\36\u014b\3\2\2\2 \u0156\3\2\2\2\"\u016b\3\2\2\2$\u0188\3\2\2\2"
            + "&\u018d\3\2\2\2(\u0196\3\2\2\2*\u019c\3\2\2\2,\u01a4\3\2\2\2./\t\2\2\2" + "/\3\3\2\2\2\60\61\t\2\2\2\61\5\3\2\2\2\62\63\7\25\2\2\63\7\3\2\2\2\64"
            + "\65\7\25\2\2\65\t\3\2\2\2\668\t\3\2\2\67\66\3\2\2\28;\3\2\2\29\67\3\2" + "\2\29:\3\2\2\2:<\3\2\2\2;9\3\2\2\2<@\5\4\3\2=?\t\3\2\2>=\3\2\2\2?B\3\2"
            + "\2\2@>\3\2\2\2@A\3\2\2\2A\13\3\2\2\2B@\3\2\2\2CD\7\7\2\2DE\5\n\6\2E\r" + "\3\2\2\2FJ\5\2\2\2GI\t\3\2\2HG\3\2\2\2IL\3\2\2\2JH\3\2\2\2JK\3\2\2\2K"
            + "M\3\2\2\2LJ\3\2\2\2MQ\7\b\2\2NP\t\3\2\2ON\3\2\2\2PS\3\2\2\2QO\3\2\2\2" + "QR\3\2\2\2RT\3\2\2\2SQ\3\2\2\2TU\5\4\3\2U\17\3\2\2\2VZ\5\2\2\2WY\t\3\2"
            + "\2XW\3\2\2\2Y\\\3\2\2\2ZX\3\2\2\2Z[\3\2\2\2[]\3\2\2\2\\Z\3\2\2\2]^\7\t"
            + "\2\2^b\7\b\2\2_a\t\3\2\2`_\3\2\2\2ad\3\2\2\2b`\3\2\2\2bc\3\2\2\2ce\3\2"
            + "\2\2db\3\2\2\2ef\5\4\3\2f\21\3\2\2\2gi\5\2\2\2hj\t\3\2\2ih\3\2\2\2jk\3" + "\2\2\2ki\3\2\2\2kl\3\2\2\2lm\3\2\2\2mq\7\23\2\2np\t\3\2\2on\3\2\2\2ps"
            + "\3\2\2\2qo\3\2\2\2qr\3\2\2\2rt\3\2\2\2sq\3\2\2\2tu\7\n\2\2uy\5\n\6\2v" + "x\5\f\7\2wv\3\2\2\2x{\3\2\2\2yw\3\2\2\2yz\3\2\2\2z\u008a\3\2\2\2{y\3\2"
            + "\2\2|\u0080\7\13\2\2}\177\t\3\2\2~}\3\2\2\2\177\u0082\3\2\2\2\u0080~\3"
            + "\2\2\2\u0080\u0081\3\2\2\2\u0081\u0083\3\2\2\2\u0082\u0080\3\2\2\2\u0083"
            + "\u0087\5\6\4\2\u0084\u0086\t\3\2\2\u0085\u0084\3\2\2\2\u0086\u0089\3\2"
            + "\2\2\u0087\u0085\3\2\2\2\u0087\u0088\3\2\2\2\u0088\u008b\3\2\2\2\u0089" + "\u0087\3\2\2\2\u008a|\3\2\2\2\u008a\u008b\3\2\2\2\u008b\u009a\3\2\2\2"
            + "\u008c\u0090\7\13\2\2\u008d\u008f\t\3\2\2\u008e\u008d\3\2\2\2\u008f\u0092"
            + "\3\2\2\2\u0090\u008e\3\2\2\2\u0090\u0091\3\2\2\2\u0091\u0093\3\2\2\2\u0092"
            + "\u0090\3\2\2\2\u0093\u0097\5\b\5\2\u0094\u0096\t\3\2\2\u0095\u0094\3\2"
            + "\2\2\u0096\u0099\3\2\2\2\u0097\u0095\3\2\2\2\u0097\u0098\3\2\2\2\u0098"
            + "\u009b\3\2\2\2\u0099\u0097\3\2\2\2\u009a\u008c\3\2\2\2\u009a\u009b\3\2"
            + "\2\2\u009b\u009c\3\2\2\2\u009c\u009d\7\f\2\2\u009d\23\3\2\2\2\u009e\u00a0"
            + "\5\2\2\2\u009f\u00a1\t\3\2\2\u00a0\u009f\3\2\2\2\u00a1\u00a2\3\2\2\2\u00a2"
            + "\u00a0\3\2\2\2\u00a2\u00a3\3\2\2\2\u00a3\u00a4\3\2\2\2\u00a4\u00a6\7\22"
            + "\2\2\u00a5\u00a7\t\3\2\2\u00a6\u00a5\3\2\2\2\u00a7\u00a8\3\2\2\2\u00a8"
            + "\u00a6\3\2\2\2\u00a8\u00a9\3\2\2\2\u00a9\u00aa\3\2\2\2\u00aa\u00ae\7\23"
            + "\2\2\u00ab\u00ad\t\3\2\2\u00ac\u00ab\3\2\2\2\u00ad\u00b0\3\2\2\2\u00ae"
            + "\u00ac\3\2\2\2\u00ae\u00af\3\2\2\2\u00af\u00b1\3\2\2\2\u00b0\u00ae\3\2"
            + "\2\2\u00b1\u00b2\7\n\2\2\u00b2\u00b6\5\n\6\2\u00b3\u00b5\5\f\7\2\u00b4"
            + "\u00b3\3\2\2\2\u00b5\u00b8\3\2\2\2\u00b6\u00b4\3\2\2\2\u00b6\u00b7\3\2"
            + "\2\2\u00b7\u00c7\3\2\2\2\u00b8\u00b6\3\2\2\2\u00b9\u00bd\7\13\2\2\u00ba"
            + "\u00bc\t\3\2\2\u00bb\u00ba\3\2\2\2\u00bc\u00bf\3\2\2\2\u00bd\u00bb\3\2"
            + "\2\2\u00bd\u00be\3\2\2\2\u00be\u00c0\3\2\2\2\u00bf\u00bd\3\2\2\2\u00c0"
            + "\u00c4\5\6\4\2\u00c1\u00c3\t\3\2\2\u00c2\u00c1\3\2\2\2\u00c3\u00c6\3\2"
            + "\2\2\u00c4\u00c2\3\2\2\2\u00c4\u00c5\3\2\2\2\u00c5\u00c8\3\2\2\2\u00c6"
            + "\u00c4\3\2\2\2\u00c7\u00b9\3\2\2\2\u00c7\u00c8\3\2\2\2\u00c8\u00d7\3\2"
            + "\2\2\u00c9\u00cd\7\13\2\2\u00ca\u00cc\t\3\2\2\u00cb\u00ca\3\2\2\2\u00cc"
            + "\u00cf\3\2\2\2\u00cd\u00cb\3\2\2\2\u00cd\u00ce\3\2\2\2\u00ce\u00d0\3\2"
            + "\2\2\u00cf\u00cd\3\2\2\2\u00d0\u00d4\5\b\5\2\u00d1\u00d3\t\3\2\2\u00d2"
            + "\u00d1\3\2\2\2\u00d3\u00d6\3\2\2\2\u00d4\u00d2\3\2\2\2\u00d4\u00d5\3\2"
            + "\2\2\u00d5\u00d8\3\2\2\2\u00d6\u00d4\3\2\2\2\u00d7\u00c9\3\2\2\2\u00d7" + "\u00d8\3\2\2\2\u00d8\u00d9\3\2\2\2\u00d9\u00da\7\f\2\2\u00da\25\3\2\2"
            + "\2\u00db\u00dd\t\3\2\2\u00dc\u00db\3\2\2\2\u00dd\u00e0\3\2\2\2\u00de\u00dc"
            + "\3\2\2\2\u00de\u00df\3\2\2\2\u00df\u00e1\3\2\2\2\u00e0\u00de\3\2\2\2\u00e1"
            + "\u00e5\7\24\2\2\u00e2\u00e4\t\3\2\2\u00e3\u00e2\3\2\2\2\u00e4\u00e7\3"
            + "\2\2\2\u00e5\u00e3\3\2\2\2\u00e5\u00e6\3\2\2\2\u00e6\u00e8\3\2\2\2\u00e7"
            + "\u00e5\3\2\2\2\u00e8\u00e9\7\n\2\2\u00e9\u00f4\5$\23\2\u00ea\u00ee\7\7"
            + "\2\2\u00eb\u00ed\t\3\2\2\u00ec\u00eb\3\2\2\2\u00ed\u00f0\3\2\2\2\u00ee"
            + "\u00ec\3\2\2\2\u00ee\u00ef\3\2\2\2\u00ef\u00f1\3\2\2\2\u00f0\u00ee\3\2"
            + "\2\2\u00f1\u00f3\5$\23\2\u00f2\u00ea\3\2\2\2\u00f3\u00f6\3\2\2\2\u00f4"
            + "\u00f2\3\2\2\2\u00f4\u00f5\3\2\2\2\u00f5\u00fa\3\2\2\2\u00f6\u00f4\3\2"
            + "\2\2\u00f7\u00f9\t\3\2\2\u00f8\u00f7\3\2\2\2\u00f9\u00fc\3\2\2\2\u00fa"
            + "\u00f8\3\2\2\2\u00fa\u00fb\3\2\2\2\u00fb\u00fd\3\2\2\2\u00fc\u00fa\3\2"
            + "\2\2\u00fd\u0101\7\13\2\2\u00fe\u0100\t\3\2\2\u00ff\u00fe\3\2\2\2\u0100"
            + "\u0103\3\2\2\2\u0101\u00ff\3\2\2\2\u0101\u0102\3\2\2\2\u0102\u0104\3\2"
            + "\2\2\u0103\u0101\3\2\2\2\u0104\u0108\5\6\4\2\u0105\u0107\t\3\2\2\u0106"
            + "\u0105\3\2\2\2\u0107\u010a\3\2\2\2\u0108\u0106\3\2\2\2\u0108\u0109\3\2"
            + "\2\2\u0109\u0119\3\2\2\2\u010a\u0108\3\2\2\2\u010b\u010f\7\13\2\2\u010c"
            + "\u010e\t\3\2\2\u010d\u010c\3\2\2\2\u010e\u0111\3\2\2\2\u010f\u010d\3\2"
            + "\2\2\u010f\u0110\3\2\2\2\u0110\u0112\3\2\2\2\u0111\u010f\3\2\2\2\u0112"
            + "\u0116\5\b\5\2\u0113\u0115\t\3\2\2\u0114\u0113\3\2\2\2\u0115\u0118\3\2"
            + "\2\2\u0116\u0114\3\2\2\2\u0116\u0117\3\2\2\2\u0117\u011a\3\2\2\2\u0118"
            + "\u0116\3\2\2\2\u0119\u010b\3\2\2\2\u0119\u011a\3\2\2\2\u011a\u011b\3\2"
            + "\2\2\u011b\u011c\7\f\2\2\u011c\27\3\2\2\2\u011d\u011f\t\3\2\2\u011e\u011d"
            + "\3\2\2\2\u011f\u0122\3\2\2\2\u0120\u011e\3\2\2\2\u0120\u0121\3\2\2\2\u0121"
            + "\u0127\3\2\2\2\u0122\u0120\3\2\2\2\u0123\u0128\5\16\b\2\u0124\u0128\5" + "\20\t\2\u0125\u0128\5\22\n\2\u0126\u0128\5\24\13\2\u0127\u0123\3\2\2\2"
            + "\u0127\u0124\3\2\2\2\u0127\u0125\3\2\2\2\u0127\u0126\3\2\2\2\u0128\31"
            + "\3\2\2\2\u0129\u012b\t\3\2\2\u012a\u0129\3\2\2\2\u012b\u012e\3\2\2\2\u012c"
            + "\u012a\3\2\2\2\u012c\u012d\3\2\2\2\u012d\u012f\3\2\2\2\u012e\u012c\3\2"
            + "\2\2\u012f\u0133\7\21\2\2\u0130\u0132\t\3\2\2\u0131\u0130\3\2\2\2\u0132"
            + "\u0135\3\2\2\2\u0133\u0131\3\2\2\2\u0133\u0134\3\2\2\2\u0134\u0138\3\2"
            + "\2\2\u0135\u0133\3\2\2\2\u0136\u0139\5*\26\2\u0137\u0139\5\30\r\2\u0138"
            + "\u0136\3\2\2\2\u0138\u0137\3\2\2\2\u0139\33\3\2\2\2\u013a\u013c\t\3\2"
            + "\2\u013b\u013a\3\2\2\2\u013c\u013f\3\2\2\2\u013d\u013b\3\2\2\2\u013d\u013e"
            + "\3\2\2\2\u013e\u0140\3\2\2\2\u013f\u013d\3\2\2\2\u0140\u0144\7\20\2\2"
            + "\u0141\u0143\t\3\2\2\u0142\u0141\3\2\2\2\u0143\u0146\3\2\2\2\u0144\u0142"
            + "\3\2\2\2\u0144\u0145\3\2\2\2\u0145\u0149\3\2\2\2\u0146\u0144\3\2\2\2\u0147"
            + "\u014a\5*\26\2\u0148\u014a\5\30\r\2\u0149\u0147\3\2\2\2\u0149\u0148\3"
            + "\2\2\2\u014a\35\3\2\2\2\u014b\u0150\5\30\r\2\u014c\u014f\5\34\17\2\u014d"
            + "\u014f\5\32\16\2\u014e\u014c\3\2\2\2\u014e\u014d\3\2\2\2\u014f\u0152\3" + "\2\2\2\u0150\u014e\3\2\2\2\u0150\u0151\3\2\2\2\u0151\37\3\2\2\2\u0152"
            + "\u0150\3\2\2\2\u0153\u0155\t\3\2\2\u0154\u0153\3\2\2\2\u0155\u0158\3\2"
            + "\2\2\u0156\u0154\3\2\2\2\u0156\u0157\3\2\2\2\u0157\u0159\3\2\2\2\u0158"
            + "\u0156\3\2\2\2\u0159\u015a\7\n\2\2\u015a\u015e\5*\26\2\u015b\u015d\t\3"
            + "\2\2\u015c\u015b\3\2\2\2\u015d\u0160\3\2\2\2\u015e\u015c\3\2\2\2\u015e"
            + "\u015f\3\2\2\2\u015f\u0161\3\2\2\2\u0160\u015e\3\2\2\2\u0161\u0165\7\f"
            + "\2\2\u0162\u0164\t\3\2\2\u0163\u0162\3\2\2\2\u0164\u0167\3\2\2\2\u0165" + "\u0163\3\2\2\2\u0165\u0166\3\2\2\2\u0166!\3\2\2\2\u0167\u0165\3\2\2\2"
            + "\u0168\u016a\t\3\2\2\u0169\u0168\3\2\2\2\u016a\u016d\3\2\2\2\u016b\u0169"
            + "\3\2\2\2\u016b\u016c\3\2\2\2\u016c\u016e\3\2\2\2\u016d\u016b\3\2\2\2\u016e"
            + "\u0172\7\22\2\2\u016f\u0171\t\3\2\2\u0170\u016f\3\2\2\2\u0171\u0174\3"
            + "\2\2\2\u0172\u0170\3\2\2\2\u0172\u0173\3\2\2\2\u0173\u0175\3\2\2\2\u0174"
            + "\u0172\3\2\2\2\u0175\u0176\7\n\2\2\u0176\u017a\5*\26\2\u0177\u0179\t\3"
            + "\2\2\u0178\u0177\3\2\2\2\u0179\u017c\3\2\2\2\u017a\u0178\3\2\2\2\u017a"
            + "\u017b\3\2\2\2\u017b\u017d\3\2\2\2\u017c\u017a\3\2\2\2\u017d\u0181\7\f"
            + "\2\2\u017e\u0180\t\3\2\2\u017f\u017e\3\2\2\2\u0180\u0183\3\2\2\2\u0181" + "\u017f\3\2\2\2\u0181\u0182\3\2\2\2\u0182#\3\2\2\2\u0183\u0181\3\2\2\2"
            + "\u0184\u0189\5\36\20\2\u0185\u0189\5 \21\2\u0186\u0189\5\"\22\2\u0187" + "\u0189\5\26\f\2\u0188\u0184\3\2\2\2\u0188\u0185\3\2\2\2\u0188\u0186\3"
            + "\2\2\2\u0188\u0187\3\2\2\2\u0189%\3\2\2\2\u018a\u018c\t\3\2\2\u018b\u018a"
            + "\3\2\2\2\u018c\u018f\3\2\2\2\u018d\u018b\3\2\2\2\u018d\u018e\3\2\2\2\u018e"
            + "\u0190\3\2\2\2\u018f\u018d\3\2\2\2\u0190\u0191\7\20\2\2\u0191\u0192\5"
            + "$\23\2\u0192\'\3\2\2\2\u0193\u0195\t\3\2\2\u0194\u0193\3\2\2\2\u0195\u0198"
            + "\3\2\2\2\u0196\u0194\3\2\2\2\u0196\u0197\3\2\2\2\u0197\u0199\3\2\2\2\u0198"
            + "\u0196\3\2\2\2\u0199\u019a\7\21\2\2\u019a\u019b\5$\23\2\u019b)\3\2\2\2"
            + "\u019c\u01a1\5$\23\2\u019d\u01a0\5&\24\2\u019e\u01a0\5(\25\2\u019f\u019d"
            + "\3\2\2\2\u019f\u019e\3\2\2\2\u01a0\u01a3\3\2\2\2\u01a1\u019f\3\2\2\2\u01a1"
            + "\u01a2\3\2\2\2\u01a2+\3\2\2\2\u01a3\u01a1\3\2\2\2\u01a4\u01a6\5*\26\2"
            + "\u01a5\u01a7\t\3\2\2\u01a6\u01a5\3\2\2\2\u01a7\u01a8\3\2\2\2\u01a8\u01a6"
            + "\3\2\2\2\u01a8\u01a9\3\2\2\2\u01a9\u01aa\3\2\2\2\u01aa\u01ab\7\2\2\3\u01ab"
            + "-\3\2\2\2<9@JQZbkqy\u0080\u0087\u008a\u0090\u0097\u009a\u00a2\u00a8\u00ae"
            + "\u00b6\u00bd\u00c4\u00c7\u00cd\u00d4\u00d7\u00de\u00e5\u00ee\u00f4\u00fa"
            + "\u0101\u0108\u010f\u0116\u0119\u0120\u0127\u012c\u0133\u0138\u013d\u0144"
            + "\u0149\u014e\u0150\u0156\u015e\u0165\u016b\u0172\u017a\u0181\u0188\u018d" + "\u0196\u019f\u01a1\u01a8";
    public static final ATN _ATN = new ATNDeserializer().deserialize(_serializedATN.toCharArray());
    static {
        _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
        for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
            _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
        }
    }
}