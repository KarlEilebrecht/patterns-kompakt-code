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

//Generated from Bbq.g4 by ANTLR 4.13.1 

@SuppressWarnings({ "all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue" })
public class BbqParser extends Parser {
    static {
        RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION);
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
        return new String[] { null, "' '", "'\\t'", "'\\r'", "'\\n'", "','", "'='", "'!'", "'('", "';'", "')'" };
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

    @SuppressWarnings("CheckReturnValue")
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
            if (listener instanceof BbqListener) {
                ((BbqListener) listener).enterArgName(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof BbqListener) {
                ((BbqListener) listener).exitArgName(this);
            }
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
            if (listener instanceof BbqListener) {
                ((BbqListener) listener).enterArgValue(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof BbqListener) {
                ((BbqListener) listener).exitArgValue(this);
            }
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
            if (listener instanceof BbqListener) {
                ((BbqListener) listener).enterLowerBound(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof BbqListener) {
                ((BbqListener) listener).exitLowerBound(this);
            }
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

    @SuppressWarnings("CheckReturnValue")
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
            if (listener instanceof BbqListener) {
                ((BbqListener) listener).enterUpperBound(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof BbqListener) {
                ((BbqListener) listener).exitUpperBound(this);
            }
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

    @SuppressWarnings("CheckReturnValue")
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
            if (listener instanceof BbqListener) {
                ((BbqListener) listener).enterInValue(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof BbqListener) {
                ((BbqListener) listener).exitInValue(this);
            }
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
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0)) {
                    {
                        {
                            setState(52);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0))) {
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
                argValue();
                setState(62);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0)) {
                    {
                        {
                            setState(59);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0))) {
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
            if (listener instanceof BbqListener) {
                ((BbqListener) listener).enterNextValue(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof BbqListener) {
                ((BbqListener) listener).exitNextValue(this);
            }
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

    @SuppressWarnings("CheckReturnValue")
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
            if (listener instanceof BbqListener) {
                ((BbqListener) listener).enterCmpEquals(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof BbqListener) {
                ((BbqListener) listener).exitCmpEquals(this);
            }
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
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0)) {
                    {
                        {
                            setState(69);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0))) {
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
                    setState(74);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(75);
                match(T__5);
                setState(79);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0)) {
                    {
                        {
                            setState(76);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0))) {
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

    @SuppressWarnings("CheckReturnValue")
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
            if (listener instanceof BbqListener) {
                ((BbqListener) listener).enterCmpNotEquals(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof BbqListener) {
                ((BbqListener) listener).exitCmpNotEquals(this);
            }
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
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0)) {
                    {
                        {
                            setState(85);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0))) {
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
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0)) {
                    {
                        {
                            setState(93);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0))) {
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

    @SuppressWarnings("CheckReturnValue")
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
            if (listener instanceof BbqListener) {
                ((BbqListener) listener).enterCmpIn(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof BbqListener) {
                ((BbqListener) listener).exitCmpIn(this);
            }
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
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0))) {
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
                    setState(105);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                } while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0));
                setState(107);
                match(IN);
                setState(111);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0)) {
                    {
                        {
                            setState(108);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0))) {
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
                    while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0)) {
                        {
                            {
                                setState(123);
                                _la = _input.LA(1);
                                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0))) {
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
                        setState(128);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                    }
                    setState(129);
                    lowerBound();
                    setState(133);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0)) {
                        {
                            {
                                setState(130);
                                _la = _input.LA(1);
                                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0))) {
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
                        while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0)) {
                            {
                                {
                                    setState(139);
                                    _la = _input.LA(1);
                                    if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0))) {
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
                            setState(144);
                            _errHandler.sync(this);
                            _la = _input.LA(1);
                        }
                        setState(145);
                        upperBound();
                        setState(149);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                        while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0)) {
                            {
                                {
                                    setState(146);
                                    _la = _input.LA(1);
                                    if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0))) {
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

    @SuppressWarnings("CheckReturnValue")
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
            if (listener instanceof BbqListener) {
                ((BbqListener) listener).enterCmpNotIn(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof BbqListener) {
                ((BbqListener) listener).exitCmpNotIn(this);
            }
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
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0))) {
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
                    setState(160);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                } while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0));
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
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0))) {
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
                    setState(166);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                } while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0));
                setState(168);
                match(IN);
                setState(172);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0)) {
                    {
                        {
                            setState(169);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0))) {
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
                    while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0)) {
                        {
                            {
                                setState(184);
                                _la = _input.LA(1);
                                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0))) {
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
                        setState(189);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                    }
                    setState(190);
                    lowerBound();
                    setState(194);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0)) {
                        {
                            {
                                setState(191);
                                _la = _input.LA(1);
                                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0))) {
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
                        while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0)) {
                            {
                                {
                                    setState(200);
                                    _la = _input.LA(1);
                                    if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0))) {
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
                            setState(205);
                            _errHandler.sync(this);
                            _la = _input.LA(1);
                        }
                        setState(206);
                        upperBound();
                        setState(210);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                        while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0)) {
                            {
                                {
                                    setState(207);
                                    _la = _input.LA(1);
                                    if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0))) {
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

    @SuppressWarnings("CheckReturnValue")
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
            if (listener instanceof BbqListener) {
                ((BbqListener) listener).enterMinMaxExpression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof BbqListener) {
                ((BbqListener) listener).exitMinMaxExpression(this);
            }
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
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0)) {
                    {
                        {
                            setState(217);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0))) {
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
                    setState(222);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(223);
                match(MINMAX);
                setState(227);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0)) {
                    {
                        {
                            setState(224);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0))) {
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
                                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0))) {
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
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0)) {
                    {
                        {
                            setState(245);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0))) {
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
                    setState(250);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(251);
                match(T__8);
                setState(255);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0)) {
                    {
                        {
                            setState(252);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0))) {
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
                    setState(257);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(258);
                lowerBound();
                setState(262);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0)) {
                    {
                        {
                            setState(259);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0))) {
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
                        while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0)) {
                            {
                                {
                                    setState(266);
                                    _la = _input.LA(1);
                                    if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0))) {
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
                            setState(271);
                            _errHandler.sync(this);
                            _la = _input.LA(1);
                        }
                        setState(272);
                        upperBound();
                        setState(276);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                        while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0)) {
                            {
                                {
                                    setState(273);
                                    _la = _input.LA(1);
                                    if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0))) {
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

    @SuppressWarnings("CheckReturnValue")
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
            if (listener instanceof BbqListener) {
                ((BbqListener) listener).enterExpressionDetails(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof BbqListener) {
                ((BbqListener) listener).exitExpressionDetails(this);
            }
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
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0)) {
                    {
                        {
                            setState(283);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0))) {
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

    @SuppressWarnings("CheckReturnValue")
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
            if (listener instanceof BbqListener) {
                ((BbqListener) listener).enterOrExpression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof BbqListener) {
                ((BbqListener) listener).exitOrExpression(this);
            }
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
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0)) {
                    {
                        {
                            setState(295);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0))) {
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
                                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0))) {
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

    @SuppressWarnings("CheckReturnValue")
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
            if (listener instanceof BbqListener) {
                ((BbqListener) listener).enterAndExpression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof BbqListener) {
                ((BbqListener) listener).exitAndExpression(this);
            }
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
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0)) {
                    {
                        {
                            setState(312);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0))) {
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
                                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0))) {
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

    @SuppressWarnings("CheckReturnValue")
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
            if (listener instanceof BbqListener) {
                ((BbqListener) listener).enterExpression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof BbqListener) {
                ((BbqListener) listener).exitExpression(this);
            }
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

    @SuppressWarnings("CheckReturnValue")
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
            if (listener instanceof BbqListener) {
                ((BbqListener) listener).enterBracedExpression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof BbqListener) {
                ((BbqListener) listener).exitBracedExpression(this);
            }
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
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0)) {
                    {
                        {
                            setState(337);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0))) {
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
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0)) {
                    {
                        {
                            setState(345);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0))) {
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
                                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0))) {
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

    @SuppressWarnings("CheckReturnValue")
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
            if (listener instanceof BbqListener) {
                ((BbqListener) listener).enterNotExpression(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof BbqListener) {
                ((BbqListener) listener).exitNotExpression(this);
            }
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
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0)) {
                    {
                        {
                            setState(358);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0))) {
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
                    setState(363);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(364);
                match(NOT);
                setState(368);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0)) {
                    {
                        {
                            setState(365);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0))) {
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
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0)) {
                    {
                        {
                            setState(373);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0))) {
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
                                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0))) {
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

    @SuppressWarnings("CheckReturnValue")
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
            if (listener instanceof BbqListener) {
                ((BbqListener) listener).enterBbqDetails(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof BbqListener) {
                ((BbqListener) listener).exitBbqDetails(this);
            }
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

    @SuppressWarnings("CheckReturnValue")
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
            if (listener instanceof BbqListener) {
                ((BbqListener) listener).enterAndBBQ(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof BbqListener) {
                ((BbqListener) listener).exitAndBBQ(this);
            }
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
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0)) {
                    {
                        {
                            setState(392);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0))) {
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

    @SuppressWarnings("CheckReturnValue")
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
            if (listener instanceof BbqListener) {
                ((BbqListener) listener).enterOrBBQ(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof BbqListener) {
                ((BbqListener) listener).exitOrBBQ(this);
            }
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
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0)) {
                    {
                        {
                            setState(401);
                            _la = _input.LA(1);
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0))) {
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

    @SuppressWarnings("CheckReturnValue")
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
            if (listener instanceof BbqListener) {
                ((BbqListener) listener).enterFullBBQ(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof BbqListener) {
                ((BbqListener) listener).exitFullBBQ(this);
            }
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

    @SuppressWarnings("CheckReturnValue")
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
            if (listener instanceof BbqListener) {
                ((BbqListener) listener).enterQuery(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof BbqListener) {
                ((BbqListener) listener).exitQuery(this);
            }
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
                            if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0))) {
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
                    setState(422);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                } while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0));
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

    public static final String _serializedATN = "\u0004\u0001\u0015\u01ab\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001"
            + "\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004"
            + "\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007"
            + "\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b"
            + "\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007"
            + "\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007"
            + "\u0012\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007"
            + "\u0015\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0002\u0001"
            + "\u0002\u0001\u0003\u0001\u0003\u0001\u0004\u0005\u00046\b\u0004\n\u0004"
            + "\f\u00049\t\u0004\u0001\u0004\u0001\u0004\u0005\u0004=\b\u0004\n\u0004"
            + "\f\u0004@\t\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0006\u0001"
            + "\u0006\u0005\u0006G\b\u0006\n\u0006\f\u0006J\t\u0006\u0001\u0006\u0001"
            + "\u0006\u0005\u0006N\b\u0006\n\u0006\f\u0006Q\t\u0006\u0001\u0006\u0001"
            + "\u0006\u0001\u0007\u0001\u0007\u0005\u0007W\b\u0007\n\u0007\f\u0007Z\t"
            + "\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0005\u0007_\b\u0007\n\u0007"
            + "\f\u0007b\t\u0007\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0004\bh\b\b" + "\u000b\b\f\bi\u0001\b\u0001\b\u0005\bn\b\b\n\b\f\bq\t\b\u0001\b\u0001"
            + "\b\u0001\b\u0005\bv\b\b\n\b\f\by\t\b\u0001\b\u0001\b\u0005\b}\b\b\n\b"
            + "\f\b\u0080\t\b\u0001\b\u0001\b\u0005\b\u0084\b\b\n\b\f\b\u0087\t\b\u0003"
            + "\b\u0089\b\b\u0001\b\u0001\b\u0005\b\u008d\b\b\n\b\f\b\u0090\t\b\u0001"
            + "\b\u0001\b\u0005\b\u0094\b\b\n\b\f\b\u0097\t\b\u0003\b\u0099\b\b\u0001"
            + "\b\u0001\b\u0001\t\u0001\t\u0004\t\u009f\b\t\u000b\t\f\t\u00a0\u0001\t"
            + "\u0001\t\u0004\t\u00a5\b\t\u000b\t\f\t\u00a6\u0001\t\u0001\t\u0005\t\u00ab"
            + "\b\t\n\t\f\t\u00ae\t\t\u0001\t\u0001\t\u0001\t\u0005\t\u00b3\b\t\n\t\f"
            + "\t\u00b6\t\t\u0001\t\u0001\t\u0005\t\u00ba\b\t\n\t\f\t\u00bd\t\t\u0001"
            + "\t\u0001\t\u0005\t\u00c1\b\t\n\t\f\t\u00c4\t\t\u0003\t\u00c6\b\t\u0001"
            + "\t\u0001\t\u0005\t\u00ca\b\t\n\t\f\t\u00cd\t\t\u0001\t\u0001\t\u0005\t"
            + "\u00d1\b\t\n\t\f\t\u00d4\t\t\u0003\t\u00d6\b\t\u0001\t\u0001\t\u0001\n"
            + "\u0005\n\u00db\b\n\n\n\f\n\u00de\t\n\u0001\n\u0001\n\u0005\n\u00e2\b\n"
            + "\n\n\f\n\u00e5\t\n\u0001\n\u0001\n\u0001\n\u0001\n\u0005\n\u00eb\b\n\n"
            + "\n\f\n\u00ee\t\n\u0001\n\u0005\n\u00f1\b\n\n\n\f\n\u00f4\t\n\u0001\n\u0005"
            + "\n\u00f7\b\n\n\n\f\n\u00fa\t\n\u0001\n\u0001\n\u0005\n\u00fe\b\n\n\n\f"
            + "\n\u0101\t\n\u0001\n\u0001\n\u0005\n\u0105\b\n\n\n\f\n\u0108\t\n\u0001"
            + "\n\u0001\n\u0005\n\u010c\b\n\n\n\f\n\u010f\t\n\u0001\n\u0001\n\u0005\n"
            + "\u0113\b\n\n\n\f\n\u0116\t\n\u0003\n\u0118\b\n\u0001\n\u0001\n\u0001\u000b"
            + "\u0005\u000b\u011d\b\u000b\n\u000b\f\u000b\u0120\t\u000b\u0001\u000b\u0001"
            + "\u000b\u0001\u000b\u0001\u000b\u0003\u000b\u0126\b\u000b\u0001\f\u0005"
            + "\f\u0129\b\f\n\f\f\f\u012c\t\f\u0001\f\u0001\f\u0005\f\u0130\b\f\n\f\f"
            + "\f\u0133\t\f\u0001\f\u0001\f\u0003\f\u0137\b\f\u0001\r\u0005\r\u013a\b"
            + "\r\n\r\f\r\u013d\t\r\u0001\r\u0001\r\u0005\r\u0141\b\r\n\r\f\r\u0144\t"
            + "\r\u0001\r\u0001\r\u0003\r\u0148\b\r\u0001\u000e\u0001\u000e\u0001\u000e"
            + "\u0005\u000e\u014d\b\u000e\n\u000e\f\u000e\u0150\t\u000e\u0001\u000f\u0005"
            + "\u000f\u0153\b\u000f\n\u000f\f\u000f\u0156\t\u000f\u0001\u000f\u0001\u000f"
            + "\u0001\u000f\u0005\u000f\u015b\b\u000f\n\u000f\f\u000f\u015e\t\u000f\u0001"
            + "\u000f\u0001\u000f\u0005\u000f\u0162\b\u000f\n\u000f\f\u000f\u0165\t\u000f"
            + "\u0001\u0010\u0005\u0010\u0168\b\u0010\n\u0010\f\u0010\u016b\t\u0010\u0001"
            + "\u0010\u0001\u0010\u0005\u0010\u016f\b\u0010\n\u0010\f\u0010\u0172\t\u0010"
            + "\u0001\u0010\u0001\u0010\u0001\u0010\u0005\u0010\u0177\b\u0010\n\u0010"
            + "\f\u0010\u017a\t\u0010\u0001\u0010\u0001\u0010\u0005\u0010\u017e\b\u0010"
            + "\n\u0010\f\u0010\u0181\t\u0010\u0001\u0011\u0001\u0011\u0001\u0011\u0001"
            + "\u0011\u0003\u0011\u0187\b\u0011\u0001\u0012\u0005\u0012\u018a\b\u0012"
            + "\n\u0012\f\u0012\u018d\t\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001"
            + "\u0013\u0005\u0013\u0193\b\u0013\n\u0013\f\u0013\u0196\t\u0013\u0001\u0013"
            + "\u0001\u0013\u0001\u0013\u0001\u0014\u0001\u0014\u0001\u0014\u0005\u0014"
            + "\u019e\b\u0014\n\u0014\f\u0014\u01a1\t\u0014\u0001\u0015\u0001\u0015\u0004"
            + "\u0015\u01a5\b\u0015\u000b\u0015\f\u0015\u01a6\u0001\u0015\u0001\u0015"
            + "\u0001\u0015\u0000\u0000\u0016\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010"
            + "\u0012\u0014\u0016\u0018\u001a\u001c\u001e \"$&(*\u0000\u0002\u0001\u0000"
            + "\u0013\u0015\u0001\u0000\u0001\u0004\u01d2\u0000,\u0001\u0000\u0000\u0000"
            + "\u0002.\u0001\u0000\u0000\u0000\u00040\u0001\u0000\u0000\u0000\u00062"
            + "\u0001\u0000\u0000\u0000\b7\u0001\u0000\u0000\u0000\nA\u0001\u0000\u0000"
            + "\u0000\fD\u0001\u0000\u0000\u0000\u000eT\u0001\u0000\u0000\u0000\u0010"
            + "e\u0001\u0000\u0000\u0000\u0012\u009c\u0001\u0000\u0000\u0000\u0014\u00dc"
            + "\u0001\u0000\u0000\u0000\u0016\u011e\u0001\u0000\u0000\u0000\u0018\u012a"
            + "\u0001\u0000\u0000\u0000\u001a\u013b\u0001\u0000\u0000\u0000\u001c\u0149"
            + "\u0001\u0000\u0000\u0000\u001e\u0154\u0001\u0000\u0000\u0000 \u0169\u0001"
            + "\u0000\u0000\u0000\"\u0186\u0001\u0000\u0000\u0000$\u018b\u0001\u0000"
            + "\u0000\u0000&\u0194\u0001\u0000\u0000\u0000(\u019a\u0001\u0000\u0000\u0000"
            + "*\u01a2\u0001\u0000\u0000\u0000,-\u0007\u0000\u0000\u0000-\u0001\u0001" + "\u0000\u0000\u0000./\u0007\u0000\u0000\u0000/\u0003\u0001\u0000\u0000"
            + "\u000001\u0005\u0013\u0000\u00001\u0005\u0001\u0000\u0000\u000023\u0005"
            + "\u0013\u0000\u00003\u0007\u0001\u0000\u0000\u000046\u0007\u0001\u0000"
            + "\u000054\u0001\u0000\u0000\u000069\u0001\u0000\u0000\u000075\u0001\u0000"
            + "\u0000\u000078\u0001\u0000\u0000\u00008:\u0001\u0000\u0000\u000097\u0001"
            + "\u0000\u0000\u0000:>\u0003\u0002\u0001\u0000;=\u0007\u0001\u0000\u0000"
            + "<;\u0001\u0000\u0000\u0000=@\u0001\u0000\u0000\u0000><\u0001\u0000\u0000"
            + "\u0000>?\u0001\u0000\u0000\u0000?\t\u0001\u0000\u0000\u0000@>\u0001\u0000"
            + "\u0000\u0000AB\u0005\u0005\u0000\u0000BC\u0003\b\u0004\u0000C\u000b\u0001"
            + "\u0000\u0000\u0000DH\u0003\u0000\u0000\u0000EG\u0007\u0001\u0000\u0000"
            + "FE\u0001\u0000\u0000\u0000GJ\u0001\u0000\u0000\u0000HF\u0001\u0000\u0000"
            + "\u0000HI\u0001\u0000\u0000\u0000IK\u0001\u0000\u0000\u0000JH\u0001\u0000"
            + "\u0000\u0000KO\u0005\u0006\u0000\u0000LN\u0007\u0001\u0000\u0000ML\u0001"
            + "\u0000\u0000\u0000NQ\u0001\u0000\u0000\u0000OM\u0001\u0000\u0000\u0000"
            + "OP\u0001\u0000\u0000\u0000PR\u0001\u0000\u0000\u0000QO\u0001\u0000\u0000"
            + "\u0000RS\u0003\u0002\u0001\u0000S\r\u0001\u0000\u0000\u0000TX\u0003\u0000"
            + "\u0000\u0000UW\u0007\u0001\u0000\u0000VU\u0001\u0000\u0000\u0000WZ\u0001"
            + "\u0000\u0000\u0000XV\u0001\u0000\u0000\u0000XY\u0001\u0000\u0000\u0000"
            + "Y[\u0001\u0000\u0000\u0000ZX\u0001\u0000\u0000\u0000[\\\u0005\u0007\u0000"
            + "\u0000\\`\u0005\u0006\u0000\u0000]_\u0007\u0001\u0000\u0000^]\u0001\u0000"
            + "\u0000\u0000_b\u0001\u0000\u0000\u0000`^\u0001\u0000\u0000\u0000`a\u0001"
            + "\u0000\u0000\u0000ac\u0001\u0000\u0000\u0000b`\u0001\u0000\u0000\u0000"
            + "cd\u0003\u0002\u0001\u0000d\u000f\u0001\u0000\u0000\u0000eg\u0003\u0000"
            + "\u0000\u0000fh\u0007\u0001\u0000\u0000gf\u0001\u0000\u0000\u0000hi\u0001"
            + "\u0000\u0000\u0000ig\u0001\u0000\u0000\u0000ij\u0001\u0000\u0000\u0000"
            + "jk\u0001\u0000\u0000\u0000ko\u0005\u0011\u0000\u0000ln\u0007\u0001\u0000"
            + "\u0000ml\u0001\u0000\u0000\u0000nq\u0001\u0000\u0000\u0000om\u0001\u0000"
            + "\u0000\u0000op\u0001\u0000\u0000\u0000pr\u0001\u0000\u0000\u0000qo\u0001"
            + "\u0000\u0000\u0000rs\u0005\b\u0000\u0000sw\u0003\b\u0004\u0000tv\u0003"
            + "\n\u0005\u0000ut\u0001\u0000\u0000\u0000vy\u0001\u0000\u0000\u0000wu\u0001"
            + "\u0000\u0000\u0000wx\u0001\u0000\u0000\u0000x\u0088\u0001\u0000\u0000"
            + "\u0000yw\u0001\u0000\u0000\u0000z~\u0005\t\u0000\u0000{}\u0007\u0001\u0000"
            + "\u0000|{\u0001\u0000\u0000\u0000}\u0080\u0001\u0000\u0000\u0000~|\u0001"
            + "\u0000\u0000\u0000~\u007f\u0001\u0000\u0000\u0000\u007f\u0081\u0001\u0000"
            + "\u0000\u0000\u0080~\u0001\u0000\u0000\u0000\u0081\u0085\u0003\u0004\u0002"
            + "\u0000\u0082\u0084\u0007\u0001\u0000\u0000\u0083\u0082\u0001\u0000\u0000"
            + "\u0000\u0084\u0087\u0001\u0000\u0000\u0000\u0085\u0083\u0001\u0000\u0000"
            + "\u0000\u0085\u0086\u0001\u0000\u0000\u0000\u0086\u0089\u0001\u0000\u0000"
            + "\u0000\u0087\u0085\u0001\u0000\u0000\u0000\u0088z\u0001\u0000\u0000\u0000"
            + "\u0088\u0089\u0001\u0000\u0000\u0000\u0089\u0098\u0001\u0000\u0000\u0000"
            + "\u008a\u008e\u0005\t\u0000\u0000\u008b\u008d\u0007\u0001\u0000\u0000\u008c"
            + "\u008b\u0001\u0000\u0000\u0000\u008d\u0090\u0001\u0000\u0000\u0000\u008e"
            + "\u008c\u0001\u0000\u0000\u0000\u008e\u008f\u0001\u0000\u0000\u0000\u008f"
            + "\u0091\u0001\u0000\u0000\u0000\u0090\u008e\u0001\u0000\u0000\u0000\u0091"
            + "\u0095\u0003\u0006\u0003\u0000\u0092\u0094\u0007\u0001\u0000\u0000\u0093"
            + "\u0092\u0001\u0000\u0000\u0000\u0094\u0097\u0001\u0000\u0000\u0000\u0095"
            + "\u0093\u0001\u0000\u0000\u0000\u0095\u0096\u0001\u0000\u0000\u0000\u0096"
            + "\u0099\u0001\u0000\u0000\u0000\u0097\u0095\u0001\u0000\u0000\u0000\u0098"
            + "\u008a\u0001\u0000\u0000\u0000\u0098\u0099\u0001\u0000\u0000\u0000\u0099"
            + "\u009a\u0001\u0000\u0000\u0000\u009a\u009b\u0005\n\u0000\u0000\u009b\u0011"
            + "\u0001\u0000\u0000\u0000\u009c\u009e\u0003\u0000\u0000\u0000\u009d\u009f"
            + "\u0007\u0001\u0000\u0000\u009e\u009d\u0001\u0000\u0000\u0000\u009f\u00a0"
            + "\u0001\u0000\u0000\u0000\u00a0\u009e\u0001\u0000\u0000\u0000\u00a0\u00a1"
            + "\u0001\u0000\u0000\u0000\u00a1\u00a2\u0001\u0000\u0000\u0000\u00a2\u00a4"
            + "\u0005\u0010\u0000\u0000\u00a3\u00a5\u0007\u0001\u0000\u0000\u00a4\u00a3"
            + "\u0001\u0000\u0000\u0000\u00a5\u00a6\u0001\u0000\u0000\u0000\u00a6\u00a4"
            + "\u0001\u0000\u0000\u0000\u00a6\u00a7\u0001\u0000\u0000\u0000\u00a7\u00a8"
            + "\u0001\u0000\u0000\u0000\u00a8\u00ac\u0005\u0011\u0000\u0000\u00a9\u00ab"
            + "\u0007\u0001\u0000\u0000\u00aa\u00a9\u0001\u0000\u0000\u0000\u00ab\u00ae"
            + "\u0001\u0000\u0000\u0000\u00ac\u00aa\u0001\u0000\u0000\u0000\u00ac\u00ad"
            + "\u0001\u0000\u0000\u0000\u00ad\u00af\u0001\u0000\u0000\u0000\u00ae\u00ac"
            + "\u0001\u0000\u0000\u0000\u00af\u00b0\u0005\b\u0000\u0000\u00b0\u00b4\u0003"
            + "\b\u0004\u0000\u00b1\u00b3\u0003\n\u0005\u0000\u00b2\u00b1\u0001\u0000"
            + "\u0000\u0000\u00b3\u00b6\u0001\u0000\u0000\u0000\u00b4\u00b2\u0001\u0000"
            + "\u0000\u0000\u00b4\u00b5\u0001\u0000\u0000\u0000\u00b5\u00c5\u0001\u0000"
            + "\u0000\u0000\u00b6\u00b4\u0001\u0000\u0000\u0000\u00b7\u00bb\u0005\t\u0000"
            + "\u0000\u00b8\u00ba\u0007\u0001\u0000\u0000\u00b9\u00b8\u0001\u0000\u0000"
            + "\u0000\u00ba\u00bd\u0001\u0000\u0000\u0000\u00bb\u00b9\u0001\u0000\u0000"
            + "\u0000\u00bb\u00bc\u0001\u0000\u0000\u0000\u00bc\u00be\u0001\u0000\u0000"
            + "\u0000\u00bd\u00bb\u0001\u0000\u0000\u0000\u00be\u00c2\u0003\u0004\u0002"
            + "\u0000\u00bf\u00c1\u0007\u0001\u0000\u0000\u00c0\u00bf\u0001\u0000\u0000"
            + "\u0000\u00c1\u00c4\u0001\u0000\u0000\u0000\u00c2\u00c0\u0001\u0000\u0000"
            + "\u0000\u00c2\u00c3\u0001\u0000\u0000\u0000\u00c3\u00c6\u0001\u0000\u0000"
            + "\u0000\u00c4\u00c2\u0001\u0000\u0000\u0000\u00c5\u00b7\u0001\u0000\u0000"
            + "\u0000\u00c5\u00c6\u0001\u0000\u0000\u0000\u00c6\u00d5\u0001\u0000\u0000"
            + "\u0000\u00c7\u00cb\u0005\t\u0000\u0000\u00c8\u00ca\u0007\u0001\u0000\u0000"
            + "\u00c9\u00c8\u0001\u0000\u0000\u0000\u00ca\u00cd\u0001\u0000\u0000\u0000"
            + "\u00cb\u00c9\u0001\u0000\u0000\u0000\u00cb\u00cc\u0001\u0000\u0000\u0000"
            + "\u00cc\u00ce\u0001\u0000\u0000\u0000\u00cd\u00cb\u0001\u0000\u0000\u0000"
            + "\u00ce\u00d2\u0003\u0006\u0003\u0000\u00cf\u00d1\u0007\u0001\u0000\u0000"
            + "\u00d0\u00cf\u0001\u0000\u0000\u0000\u00d1\u00d4\u0001\u0000\u0000\u0000"
            + "\u00d2\u00d0\u0001\u0000\u0000\u0000\u00d2\u00d3\u0001\u0000\u0000\u0000"
            + "\u00d3\u00d6\u0001\u0000\u0000\u0000\u00d4\u00d2\u0001\u0000\u0000\u0000"
            + "\u00d5\u00c7\u0001\u0000\u0000\u0000\u00d5\u00d6\u0001\u0000\u0000\u0000"
            + "\u00d6\u00d7\u0001\u0000\u0000\u0000\u00d7\u00d8\u0005\n\u0000\u0000\u00d8"
            + "\u0013\u0001\u0000\u0000\u0000\u00d9\u00db\u0007\u0001\u0000\u0000\u00da"
            + "\u00d9\u0001\u0000\u0000\u0000\u00db\u00de\u0001\u0000\u0000\u0000\u00dc"
            + "\u00da\u0001\u0000\u0000\u0000\u00dc\u00dd\u0001\u0000\u0000\u0000\u00dd"
            + "\u00df\u0001\u0000\u0000\u0000\u00de\u00dc\u0001\u0000\u0000\u0000\u00df"
            + "\u00e3\u0005\u0012\u0000\u0000\u00e0\u00e2\u0007\u0001\u0000\u0000\u00e1"
            + "\u00e0\u0001\u0000\u0000\u0000\u00e2\u00e5\u0001\u0000\u0000\u0000\u00e3"
            + "\u00e1\u0001\u0000\u0000\u0000\u00e3\u00e4\u0001\u0000\u0000\u0000\u00e4"
            + "\u00e6\u0001\u0000\u0000\u0000\u00e5\u00e3\u0001\u0000\u0000\u0000\u00e6"
            + "\u00e7\u0005\b\u0000\u0000\u00e7\u00f2\u0003\"\u0011\u0000\u00e8\u00ec"
            + "\u0005\u0005\u0000\u0000\u00e9\u00eb\u0007\u0001\u0000\u0000\u00ea\u00e9"
            + "\u0001\u0000\u0000\u0000\u00eb\u00ee\u0001\u0000\u0000\u0000\u00ec\u00ea"
            + "\u0001\u0000\u0000\u0000\u00ec\u00ed\u0001\u0000\u0000\u0000\u00ed\u00ef"
            + "\u0001\u0000\u0000\u0000\u00ee\u00ec\u0001\u0000\u0000\u0000\u00ef\u00f1"
            + "\u0003\"\u0011\u0000\u00f0\u00e8\u0001\u0000\u0000\u0000\u00f1\u00f4\u0001"
            + "\u0000\u0000\u0000\u00f2\u00f0\u0001\u0000\u0000\u0000\u00f2\u00f3\u0001"
            + "\u0000\u0000\u0000\u00f3\u00f8\u0001\u0000\u0000\u0000\u00f4\u00f2\u0001"
            + "\u0000\u0000\u0000\u00f5\u00f7\u0007\u0001\u0000\u0000\u00f6\u00f5\u0001"
            + "\u0000\u0000\u0000\u00f7\u00fa\u0001\u0000\u0000\u0000\u00f8\u00f6\u0001"
            + "\u0000\u0000\u0000\u00f8\u00f9\u0001\u0000\u0000\u0000\u00f9\u00fb\u0001"
            + "\u0000\u0000\u0000\u00fa\u00f8\u0001\u0000\u0000\u0000\u00fb\u00ff\u0005"
            + "\t\u0000\u0000\u00fc\u00fe\u0007\u0001\u0000\u0000\u00fd\u00fc\u0001\u0000"
            + "\u0000\u0000\u00fe\u0101\u0001\u0000\u0000\u0000\u00ff\u00fd\u0001\u0000"
            + "\u0000\u0000\u00ff\u0100\u0001\u0000\u0000\u0000\u0100\u0102\u0001\u0000"
            + "\u0000\u0000\u0101\u00ff\u0001\u0000\u0000\u0000\u0102\u0106\u0003\u0004"
            + "\u0002\u0000\u0103\u0105\u0007\u0001\u0000\u0000\u0104\u0103\u0001\u0000"
            + "\u0000\u0000\u0105\u0108\u0001\u0000\u0000\u0000\u0106\u0104\u0001\u0000"
            + "\u0000\u0000\u0106\u0107\u0001\u0000\u0000\u0000\u0107\u0117\u0001\u0000"
            + "\u0000\u0000\u0108\u0106\u0001\u0000\u0000\u0000\u0109\u010d\u0005\t\u0000"
            + "\u0000\u010a\u010c\u0007\u0001\u0000\u0000\u010b\u010a\u0001\u0000\u0000"
            + "\u0000\u010c\u010f\u0001\u0000\u0000\u0000\u010d\u010b\u0001\u0000\u0000"
            + "\u0000\u010d\u010e\u0001\u0000\u0000\u0000\u010e\u0110\u0001\u0000\u0000"
            + "\u0000\u010f\u010d\u0001\u0000\u0000\u0000\u0110\u0114\u0003\u0006\u0003"
            + "\u0000\u0111\u0113\u0007\u0001\u0000\u0000\u0112\u0111\u0001\u0000\u0000"
            + "\u0000\u0113\u0116\u0001\u0000\u0000\u0000\u0114\u0112\u0001\u0000\u0000"
            + "\u0000\u0114\u0115\u0001\u0000\u0000\u0000\u0115\u0118\u0001\u0000\u0000"
            + "\u0000\u0116\u0114\u0001\u0000\u0000\u0000\u0117\u0109\u0001\u0000\u0000"
            + "\u0000\u0117\u0118\u0001\u0000\u0000\u0000\u0118\u0119\u0001\u0000\u0000"
            + "\u0000\u0119\u011a\u0005\n\u0000\u0000\u011a\u0015\u0001\u0000\u0000\u0000"
            + "\u011b\u011d\u0007\u0001\u0000\u0000\u011c\u011b\u0001\u0000\u0000\u0000"
            + "\u011d\u0120\u0001\u0000\u0000\u0000\u011e\u011c\u0001\u0000\u0000\u0000"
            + "\u011e\u011f\u0001\u0000\u0000\u0000\u011f\u0125\u0001\u0000\u0000\u0000"
            + "\u0120\u011e\u0001\u0000\u0000\u0000\u0121\u0126\u0003\f\u0006\u0000\u0122"
            + "\u0126\u0003\u000e\u0007\u0000\u0123\u0126\u0003\u0010\b\u0000\u0124\u0126"
            + "\u0003\u0012\t\u0000\u0125\u0121\u0001\u0000\u0000\u0000\u0125\u0122\u0001"
            + "\u0000\u0000\u0000\u0125\u0123\u0001\u0000\u0000\u0000\u0125\u0124\u0001"
            + "\u0000\u0000\u0000\u0126\u0017\u0001\u0000\u0000\u0000\u0127\u0129\u0007"
            + "\u0001\u0000\u0000\u0128\u0127\u0001\u0000\u0000\u0000\u0129\u012c\u0001"
            + "\u0000\u0000\u0000\u012a\u0128\u0001\u0000\u0000\u0000\u012a\u012b\u0001"
            + "\u0000\u0000\u0000\u012b\u012d\u0001\u0000\u0000\u0000\u012c\u012a\u0001"
            + "\u0000\u0000\u0000\u012d\u0131\u0005\u000f\u0000\u0000\u012e\u0130\u0007"
            + "\u0001\u0000\u0000\u012f\u012e\u0001\u0000\u0000\u0000\u0130\u0133\u0001"
            + "\u0000\u0000\u0000\u0131\u012f\u0001\u0000\u0000\u0000\u0131\u0132\u0001"
            + "\u0000\u0000\u0000\u0132\u0136\u0001\u0000\u0000\u0000\u0133\u0131\u0001"
            + "\u0000\u0000\u0000\u0134\u0137\u0003(\u0014\u0000\u0135\u0137\u0003\u0016"
            + "\u000b\u0000\u0136\u0134\u0001\u0000\u0000\u0000\u0136\u0135\u0001\u0000"
            + "\u0000\u0000\u0137\u0019\u0001\u0000\u0000\u0000\u0138\u013a\u0007\u0001"
            + "\u0000\u0000\u0139\u0138\u0001\u0000\u0000\u0000\u013a\u013d\u0001\u0000"
            + "\u0000\u0000\u013b\u0139\u0001\u0000\u0000\u0000\u013b\u013c\u0001\u0000"
            + "\u0000\u0000\u013c\u013e\u0001\u0000\u0000\u0000\u013d\u013b\u0001\u0000"
            + "\u0000\u0000\u013e\u0142\u0005\u000e\u0000\u0000\u013f\u0141\u0007\u0001"
            + "\u0000\u0000\u0140\u013f\u0001\u0000\u0000\u0000\u0141\u0144\u0001\u0000"
            + "\u0000\u0000\u0142\u0140\u0001\u0000\u0000\u0000\u0142\u0143\u0001\u0000"
            + "\u0000\u0000\u0143\u0147\u0001\u0000\u0000\u0000\u0144\u0142\u0001\u0000"
            + "\u0000\u0000\u0145\u0148\u0003(\u0014\u0000\u0146\u0148\u0003\u0016\u000b"
            + "\u0000\u0147\u0145\u0001\u0000\u0000\u0000\u0147\u0146\u0001\u0000\u0000"
            + "\u0000\u0148\u001b\u0001\u0000\u0000\u0000\u0149\u014e\u0003\u0016\u000b"
            + "\u0000\u014a\u014d\u0003\u001a\r\u0000\u014b\u014d\u0003\u0018\f\u0000"
            + "\u014c\u014a\u0001\u0000\u0000\u0000\u014c\u014b\u0001\u0000\u0000\u0000"
            + "\u014d\u0150\u0001\u0000\u0000\u0000\u014e\u014c\u0001\u0000\u0000\u0000"
            + "\u014e\u014f\u0001\u0000\u0000\u0000\u014f\u001d\u0001\u0000\u0000\u0000"
            + "\u0150\u014e\u0001\u0000\u0000\u0000\u0151\u0153\u0007\u0001\u0000\u0000"
            + "\u0152\u0151\u0001\u0000\u0000\u0000\u0153\u0156\u0001\u0000\u0000\u0000"
            + "\u0154\u0152\u0001\u0000\u0000\u0000\u0154\u0155\u0001\u0000\u0000\u0000"
            + "\u0155\u0157\u0001\u0000\u0000\u0000\u0156\u0154\u0001\u0000\u0000\u0000"
            + "\u0157\u0158\u0005\b\u0000\u0000\u0158\u015c\u0003(\u0014\u0000\u0159"
            + "\u015b\u0007\u0001\u0000\u0000\u015a\u0159\u0001\u0000\u0000\u0000\u015b"
            + "\u015e\u0001\u0000\u0000\u0000\u015c\u015a\u0001\u0000\u0000\u0000\u015c"
            + "\u015d\u0001\u0000\u0000\u0000\u015d\u015f\u0001\u0000\u0000\u0000\u015e"
            + "\u015c\u0001\u0000\u0000\u0000\u015f\u0163\u0005\n\u0000\u0000\u0160\u0162"
            + "\u0007\u0001\u0000\u0000\u0161\u0160\u0001\u0000\u0000\u0000\u0162\u0165"
            + "\u0001\u0000\u0000\u0000\u0163\u0161\u0001\u0000\u0000\u0000\u0163\u0164"
            + "\u0001\u0000\u0000\u0000\u0164\u001f\u0001\u0000\u0000\u0000\u0165\u0163"
            + "\u0001\u0000\u0000\u0000\u0166\u0168\u0007\u0001\u0000\u0000\u0167\u0166"
            + "\u0001\u0000\u0000\u0000\u0168\u016b\u0001\u0000\u0000\u0000\u0169\u0167"
            + "\u0001\u0000\u0000\u0000\u0169\u016a\u0001\u0000\u0000\u0000\u016a\u016c"
            + "\u0001\u0000\u0000\u0000\u016b\u0169\u0001\u0000\u0000\u0000\u016c\u0170"
            + "\u0005\u0010\u0000\u0000\u016d\u016f\u0007\u0001\u0000\u0000\u016e\u016d"
            + "\u0001\u0000\u0000\u0000\u016f\u0172\u0001\u0000\u0000\u0000\u0170\u016e"
            + "\u0001\u0000\u0000\u0000\u0170\u0171\u0001\u0000\u0000\u0000\u0171\u0173"
            + "\u0001\u0000\u0000\u0000\u0172\u0170\u0001\u0000\u0000\u0000\u0173\u0174"
            + "\u0005\b\u0000\u0000\u0174\u0178\u0003(\u0014\u0000\u0175\u0177\u0007"
            + "\u0001\u0000\u0000\u0176\u0175\u0001\u0000\u0000\u0000\u0177\u017a\u0001"
            + "\u0000\u0000\u0000\u0178\u0176\u0001\u0000\u0000\u0000\u0178\u0179\u0001"
            + "\u0000\u0000\u0000\u0179\u017b\u0001\u0000\u0000\u0000\u017a\u0178\u0001"
            + "\u0000\u0000\u0000\u017b\u017f\u0005\n\u0000\u0000\u017c\u017e\u0007\u0001"
            + "\u0000\u0000\u017d\u017c\u0001\u0000\u0000\u0000\u017e\u0181\u0001\u0000"
            + "\u0000\u0000\u017f\u017d\u0001\u0000\u0000\u0000\u017f\u0180\u0001\u0000"
            + "\u0000\u0000\u0180!\u0001\u0000\u0000\u0000\u0181\u017f\u0001\u0000\u0000"
            + "\u0000\u0182\u0187\u0003\u001c\u000e\u0000\u0183\u0187\u0003\u001e\u000f"
            + "\u0000\u0184\u0187\u0003 \u0010\u0000\u0185\u0187\u0003\u0014\n\u0000"
            + "\u0186\u0182\u0001\u0000\u0000\u0000\u0186\u0183\u0001\u0000\u0000\u0000"
            + "\u0186\u0184\u0001\u0000\u0000\u0000\u0186\u0185\u0001\u0000\u0000\u0000"
            + "\u0187#\u0001\u0000\u0000\u0000\u0188\u018a\u0007\u0001\u0000\u0000\u0189"
            + "\u0188\u0001\u0000\u0000\u0000\u018a\u018d\u0001\u0000\u0000\u0000\u018b"
            + "\u0189\u0001\u0000\u0000\u0000\u018b\u018c\u0001\u0000\u0000\u0000\u018c"
            + "\u018e\u0001\u0000\u0000\u0000\u018d\u018b\u0001\u0000\u0000\u0000\u018e"
            + "\u018f\u0005\u000e\u0000\u0000\u018f\u0190\u0003\"\u0011\u0000\u0190%"
            + "\u0001\u0000\u0000\u0000\u0191\u0193\u0007\u0001\u0000\u0000\u0192\u0191"
            + "\u0001\u0000\u0000\u0000\u0193\u0196\u0001\u0000\u0000\u0000\u0194\u0192"
            + "\u0001\u0000\u0000\u0000\u0194\u0195\u0001\u0000\u0000\u0000\u0195\u0197"
            + "\u0001\u0000\u0000\u0000\u0196\u0194\u0001\u0000\u0000\u0000\u0197\u0198"
            + "\u0005\u000f\u0000\u0000\u0198\u0199\u0003\"\u0011\u0000\u0199\'\u0001" + "\u0000\u0000\u0000\u019a\u019f\u0003\"\u0011\u0000\u019b\u019e\u0003$"
            + "\u0012\u0000\u019c\u019e\u0003&\u0013\u0000\u019d\u019b\u0001\u0000\u0000"
            + "\u0000\u019d\u019c\u0001\u0000\u0000\u0000\u019e\u01a1\u0001\u0000\u0000"
            + "\u0000\u019f\u019d\u0001\u0000\u0000\u0000\u019f\u01a0\u0001\u0000\u0000"
            + "\u0000\u01a0)\u0001\u0000\u0000\u0000\u01a1\u019f\u0001\u0000\u0000\u0000"
            + "\u01a2\u01a4\u0003(\u0014\u0000\u01a3\u01a5\u0007\u0001\u0000\u0000\u01a4"
            + "\u01a3\u0001\u0000\u0000\u0000\u01a5\u01a6\u0001\u0000\u0000\u0000\u01a6"
            + "\u01a4\u0001\u0000\u0000\u0000\u01a6\u01a7\u0001\u0000\u0000\u0000\u01a7"
            + "\u01a8\u0001\u0000\u0000\u0000\u01a8\u01a9\u0005\u0000\u0000\u0001\u01a9"
            + "+\u0001\u0000\u0000\u0000:7>HOX`iow~\u0085\u0088\u008e\u0095\u0098\u00a0"
            + "\u00a6\u00ac\u00b4\u00bb\u00c2\u00c5\u00cb\u00d2\u00d5\u00dc\u00e3\u00ec"
            + "\u00f2\u00f8\u00ff\u0106\u010d\u0114\u0117\u011e\u0125\u012a\u0131\u0136"
            + "\u013b\u0142\u0147\u014c\u014e\u0154\u015c\u0163\u0169\u0170\u0178\u017f" + "\u0186\u018b\u0194\u019d\u019f\u01a6";
    public static final ATN _ATN = new ATNDeserializer().deserialize(_serializedATN.toCharArray());
    static {
        _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
        for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
            _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
        }
    }
}