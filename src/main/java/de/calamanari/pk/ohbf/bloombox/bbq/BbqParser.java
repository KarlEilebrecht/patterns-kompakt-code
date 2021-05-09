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

// Generated from Bbq.g4 by ANTLR 4.9.2
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

@SuppressWarnings({ "all", "warnings", "unchecked", "unused", "cast" })
public class BbqParser extends Parser {
    static {
        RuntimeMetaData.checkVersion("4.9.2", RuntimeMetaData.VERSION);
    }

    protected static final DFA[] _decisionToDFA;
    protected static final PredictionContextCache _sharedContextCache = new PredictionContextCache();
    public static final int T__0 = 1, T__1 = 2, T__2 = 3, T__3 = 4, T__4 = 5, T__5 = 6, T__6 = 7, T__7 = 8, T__8 = 9, ESCESC = 10, ESCSQ = 11, ESCDQ = 12,
            AND = 13, OR = 14, NOT = 15, IN = 16, SIMPLE_WORD = 17, SQWORD = 18, DQWORD = 19;
    public static final int RULE_argName = 0, RULE_argValue = 1, RULE_inValue = 2, RULE_nextValue = 3, RULE_cmpEquals = 4, RULE_cmpNotEquals = 5,
            RULE_cmpIn = 6, RULE_cmpNotIn = 7, RULE_expressionDetails = 8, RULE_orExpression = 9, RULE_andExpression = 10, RULE_expression = 11,
            RULE_bracedExpression = 12, RULE_bbqDetails = 13, RULE_andBBQ = 14, RULE_orBBQ = 15, RULE_fullBBQ = 16, RULE_query = 17;

    private static String[] makeRuleNames() {
        return new String[] { "argName", "argValue", "inValue", "nextValue", "cmpEquals", "cmpNotEquals", "cmpIn", "cmpNotIn", "expressionDetails",
                "orExpression", "andExpression", "expression", "bracedExpression", "bbqDetails", "andBBQ", "orBBQ", "fullBBQ", "query" };
    }

    public static final String[] ruleNames = makeRuleNames();

    private static String[] makeLiteralNames() {
        return new String[] { null, "' '", "'\t'", "'\r'", "'\n'", "','", "'='", "'!'", "'('", "')'" };
    }

    private static final String[] _LITERAL_NAMES = makeLiteralNames();

    private static String[] makeSymbolicNames() {
        return new String[] { null, null, null, null, null, null, null, null, null, null, "ESCESC", "ESCSQ", "ESCDQ", "AND", "OR", "NOT", "IN", "SIMPLE_WORD",
                "SQWORD", "DQWORD" };
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
                setState(38);
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
        enterRule(_localctx, 4, RULE_inValue);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(43);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0)) {
                    {
                        {
                            setState(40);
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
                    setState(45);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(46);
                argValue();
                setState(50);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0)) {
                    {
                        {
                            setState(47);
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
                    setState(52);
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
        enterRule(_localctx, 6, RULE_nextValue);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(53);
                match(T__4);
                setState(54);
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
        enterRule(_localctx, 8, RULE_cmpEquals);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(56);
                argName();
                setState(60);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0)) {
                    {
                        {
                            setState(57);
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
                    setState(62);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(63);
                match(T__5);
                setState(67);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0)) {
                    {
                        {
                            setState(64);
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
                    setState(69);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(70);
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
        enterRule(_localctx, 10, RULE_cmpNotEquals);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(72);
                argName();
                setState(76);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0)) {
                    {
                        {
                            setState(73);
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
                    setState(78);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(79);
                match(T__6);
                setState(80);
                match(T__5);
                setState(84);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0)) {
                    {
                        {
                            setState(81);
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
                    setState(86);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(87);
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
        enterRule(_localctx, 12, RULE_cmpIn);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(89);
                argName();
                setState(91);
                _errHandler.sync(this);
                _la = _input.LA(1);
                do {
                    {
                        {
                            setState(90);
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
                    setState(93);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                } while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0));
                setState(95);
                match(IN);
                setState(99);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0)) {
                    {
                        {
                            setState(96);
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
                    setState(101);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(102);
                match(T__7);
                setState(103);
                inValue();
                setState(107);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == T__4) {
                    {
                        {
                            setState(104);
                            nextValue();
                        }
                    }
                    setState(109);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(110);
                match(T__8);
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
        enterRule(_localctx, 14, RULE_cmpNotIn);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(112);
                argName();
                setState(114);
                _errHandler.sync(this);
                _la = _input.LA(1);
                do {
                    {
                        {
                            setState(113);
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
                    setState(116);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                } while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0));
                setState(118);
                match(NOT);
                setState(120);
                _errHandler.sync(this);
                _la = _input.LA(1);
                do {
                    {
                        {
                            setState(119);
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
                    setState(122);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                } while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0));
                setState(124);
                match(IN);
                setState(128);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0)) {
                    {
                        {
                            setState(125);
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
                    setState(130);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(131);
                match(T__7);
                setState(132);
                inValue();
                setState(136);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == T__4) {
                    {
                        {
                            setState(133);
                            nextValue();
                        }
                    }
                    setState(138);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(139);
                match(T__8);
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
        enterRule(_localctx, 16, RULE_expressionDetails);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(144);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0)) {
                    {
                        {
                            setState(141);
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
                    setState(146);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(151);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 14, _ctx)) {
                case 1: {
                    setState(147);
                    cmpEquals();
                }
                    break;
                case 2: {
                    setState(148);
                    cmpNotEquals();
                }
                    break;
                case 3: {
                    setState(149);
                    cmpIn();
                }
                    break;
                case 4: {
                    setState(150);
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
        enterRule(_localctx, 18, RULE_orExpression);
        int _la;
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(156);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0)) {
                    {
                        {
                            setState(153);
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
                    setState(158);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(159);
                match(OR);
                setState(163);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 16, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        {
                            {
                                setState(160);
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
                    setState(165);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 16, _ctx);
                }
                setState(168);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 17, _ctx)) {
                case 1: {
                    setState(166);
                    fullBBQ();
                }
                    break;
                case 2: {
                    setState(167);
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
        enterRule(_localctx, 20, RULE_andExpression);
        int _la;
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(173);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0)) {
                    {
                        {
                            setState(170);
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
                    setState(175);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(176);
                match(AND);
                setState(180);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 19, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        {
                            {
                                setState(177);
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
                    setState(182);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 19, _ctx);
                }
                setState(185);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 20, _ctx)) {
                case 1: {
                    setState(183);
                    fullBBQ();
                }
                    break;
                case 2: {
                    setState(184);
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
        enterRule(_localctx, 22, RULE_expression);
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(187);
                expressionDetails();
                setState(192);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 22, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        {
                            setState(190);
                            _errHandler.sync(this);
                            switch (getInterpreter().adaptivePredict(_input, 21, _ctx)) {
                            case 1: {
                                setState(188);
                                andExpression();
                            }
                                break;
                            case 2: {
                                setState(189);
                                orExpression();
                            }
                                break;
                            }
                        }
                    }
                    setState(194);
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
        enterRule(_localctx, 24, RULE_bracedExpression);
        int _la;
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(198);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0)) {
                    {
                        {
                            setState(195);
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
                    setState(200);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(201);
                match(T__7);
                setState(202);
                fullBBQ();
                setState(203);
                match(T__8);
                setState(207);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 24, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        {
                            {
                                setState(204);
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
                    setState(209);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 24, _ctx);
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
        enterRule(_localctx, 26, RULE_bbqDetails);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(212);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 25, _ctx)) {
                case 1: {
                    setState(210);
                    expression();
                }
                    break;
                case 2: {
                    setState(211);
                    bracedExpression();
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
        enterRule(_localctx, 28, RULE_andBBQ);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(217);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0)) {
                    {
                        {
                            setState(214);
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
                    setState(219);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(220);
                match(AND);
                setState(221);
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
        enterRule(_localctx, 30, RULE_orBBQ);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(226);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0)) {
                    {
                        {
                            setState(223);
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
                    setState(228);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(229);
                match(OR);
                setState(230);
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
        enterRule(_localctx, 32, RULE_fullBBQ);
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(232);
                bbqDetails();
                setState(237);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 29, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        {
                            setState(235);
                            _errHandler.sync(this);
                            switch (getInterpreter().adaptivePredict(_input, 28, _ctx)) {
                            case 1: {
                                setState(233);
                                andBBQ();
                            }
                                break;
                            case 2: {
                                setState(234);
                                orBBQ();
                            }
                                break;
                            }
                        }
                    }
                    setState(239);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 29, _ctx);
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
        enterRule(_localctx, 34, RULE_query);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(240);
                fullBBQ();
                setState(242);
                _errHandler.sync(this);
                _la = _input.LA(1);
                do {
                    {
                        {
                            setState(241);
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
                    setState(244);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                } while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3))) != 0));
                setState(246);
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

    public static final String _serializedATN = "\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\25\u00fb\4\2\t\2"
            + "\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"
            + "\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"
            + "\4\23\t\23\3\2\3\2\3\3\3\3\3\4\7\4,\n\4\f\4\16\4/\13\4\3\4\3\4\7\4\63" + "\n\4\f\4\16\4\66\13\4\3\5\3\5\3\5\3\6\3\6\7\6=\n\6\f\6\16\6@\13\6\3\6"
            + "\3\6\7\6D\n\6\f\6\16\6G\13\6\3\6\3\6\3\7\3\7\7\7M\n\7\f\7\16\7P\13\7\3" + "\7\3\7\3\7\7\7U\n\7\f\7\16\7X\13\7\3\7\3\7\3\b\3\b\6\b^\n\b\r\b\16\b_"
            + "\3\b\3\b\7\bd\n\b\f\b\16\bg\13\b\3\b\3\b\3\b\7\bl\n\b\f\b\16\bo\13\b\3"
            + "\b\3\b\3\t\3\t\6\tu\n\t\r\t\16\tv\3\t\3\t\6\t{\n\t\r\t\16\t|\3\t\3\t\7"
            + "\t\u0081\n\t\f\t\16\t\u0084\13\t\3\t\3\t\3\t\7\t\u0089\n\t\f\t\16\t\u008c"
            + "\13\t\3\t\3\t\3\n\7\n\u0091\n\n\f\n\16\n\u0094\13\n\3\n\3\n\3\n\3\n\5"
            + "\n\u009a\n\n\3\13\7\13\u009d\n\13\f\13\16\13\u00a0\13\13\3\13\3\13\7\13"
            + "\u00a4\n\13\f\13\16\13\u00a7\13\13\3\13\3\13\5\13\u00ab\n\13\3\f\7\f\u00ae"
            + "\n\f\f\f\16\f\u00b1\13\f\3\f\3\f\7\f\u00b5\n\f\f\f\16\f\u00b8\13\f\3\f" + "\3\f\5\f\u00bc\n\f\3\r\3\r\3\r\7\r\u00c1\n\r\f\r\16\r\u00c4\13\r\3\16"
            + "\7\16\u00c7\n\16\f\16\16\16\u00ca\13\16\3\16\3\16\3\16\3\16\7\16\u00d0"
            + "\n\16\f\16\16\16\u00d3\13\16\3\17\3\17\5\17\u00d7\n\17\3\20\7\20\u00da" + "\n\20\f\20\16\20\u00dd\13\20\3\20\3\20\3\20\3\21\7\21\u00e3\n\21\f\21"
            + "\16\21\u00e6\13\21\3\21\3\21\3\21\3\22\3\22\3\22\7\22\u00ee\n\22\f\22"
            + "\16\22\u00f1\13\22\3\23\3\23\6\23\u00f5\n\23\r\23\16\23\u00f6\3\23\3\23"
            + "\3\23\2\2\24\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$\2\4\3\2\23\25" + "\3\2\3\6\2\u0109\2&\3\2\2\2\4(\3\2\2\2\6-\3\2\2\2\b\67\3\2\2\2\n:\3\2"
            + "\2\2\fJ\3\2\2\2\16[\3\2\2\2\20r\3\2\2\2\22\u0092\3\2\2\2\24\u009e\3\2" + "\2\2\26\u00af\3\2\2\2\30\u00bd\3\2\2\2\32\u00c8\3\2\2\2\34\u00d6\3\2\2"
            + "\2\36\u00db\3\2\2\2 \u00e4\3\2\2\2\"\u00ea\3\2\2\2$\u00f2\3\2\2\2&\'\t" + "\2\2\2\'\3\3\2\2\2()\t\2\2\2)\5\3\2\2\2*,\t\3\2\2+*\3\2\2\2,/\3\2\2\2"
            + "-+\3\2\2\2-.\3\2\2\2.\60\3\2\2\2/-\3\2\2\2\60\64\5\4\3\2\61\63\t\3\2\2" + "\62\61\3\2\2\2\63\66\3\2\2\2\64\62\3\2\2\2\64\65\3\2\2\2\65\7\3\2\2\2"
            + "\66\64\3\2\2\2\678\7\7\2\289\5\6\4\29\t\3\2\2\2:>\5\2\2\2;=\t\3\2\2<;" + "\3\2\2\2=@\3\2\2\2><\3\2\2\2>?\3\2\2\2?A\3\2\2\2@>\3\2\2\2AE\7\b\2\2B"
            + "D\t\3\2\2CB\3\2\2\2DG\3\2\2\2EC\3\2\2\2EF\3\2\2\2FH\3\2\2\2GE\3\2\2\2" + "HI\5\4\3\2I\13\3\2\2\2JN\5\2\2\2KM\t\3\2\2LK\3\2\2\2MP\3\2\2\2NL\3\2\2"
            + "\2NO\3\2\2\2OQ\3\2\2\2PN\3\2\2\2QR\7\t\2\2RV\7\b\2\2SU\t\3\2\2TS\3\2\2" + "\2UX\3\2\2\2VT\3\2\2\2VW\3\2\2\2WY\3\2\2\2XV\3\2\2\2YZ\5\4\3\2Z\r\3\2"
            + "\2\2[]\5\2\2\2\\^\t\3\2\2]\\\3\2\2\2^_\3\2\2\2_]\3\2\2\2_`\3\2\2\2`a\3" + "\2\2\2ae\7\22\2\2bd\t\3\2\2cb\3\2\2\2dg\3\2\2\2ec\3\2\2\2ef\3\2\2\2fh"
            + "\3\2\2\2ge\3\2\2\2hi\7\n\2\2im\5\6\4\2jl\5\b\5\2kj\3\2\2\2lo\3\2\2\2m" + "k\3\2\2\2mn\3\2\2\2np\3\2\2\2om\3\2\2\2pq\7\13\2\2q\17\3\2\2\2rt\5\2\2"
            + "\2su\t\3\2\2ts\3\2\2\2uv\3\2\2\2vt\3\2\2\2vw\3\2\2\2wx\3\2\2\2xz\7\21"
            + "\2\2y{\t\3\2\2zy\3\2\2\2{|\3\2\2\2|z\3\2\2\2|}\3\2\2\2}~\3\2\2\2~\u0082"
            + "\7\22\2\2\177\u0081\t\3\2\2\u0080\177\3\2\2\2\u0081\u0084\3\2\2\2\u0082"
            + "\u0080\3\2\2\2\u0082\u0083\3\2\2\2\u0083\u0085\3\2\2\2\u0084\u0082\3\2"
            + "\2\2\u0085\u0086\7\n\2\2\u0086\u008a\5\6\4\2\u0087\u0089\5\b\5\2\u0088"
            + "\u0087\3\2\2\2\u0089\u008c\3\2\2\2\u008a\u0088\3\2\2\2\u008a\u008b\3\2"
            + "\2\2\u008b\u008d\3\2\2\2\u008c\u008a\3\2\2\2\u008d\u008e\7\13\2\2\u008e"
            + "\21\3\2\2\2\u008f\u0091\t\3\2\2\u0090\u008f\3\2\2\2\u0091\u0094\3\2\2"
            + "\2\u0092\u0090\3\2\2\2\u0092\u0093\3\2\2\2\u0093\u0099\3\2\2\2\u0094\u0092"
            + "\3\2\2\2\u0095\u009a\5\n\6\2\u0096\u009a\5\f\7\2\u0097\u009a\5\16\b\2"
            + "\u0098\u009a\5\20\t\2\u0099\u0095\3\2\2\2\u0099\u0096\3\2\2\2\u0099\u0097"
            + "\3\2\2\2\u0099\u0098\3\2\2\2\u009a\23\3\2\2\2\u009b\u009d\t\3\2\2\u009c"
            + "\u009b\3\2\2\2\u009d\u00a0\3\2\2\2\u009e\u009c\3\2\2\2\u009e\u009f\3\2"
            + "\2\2\u009f\u00a1\3\2\2\2\u00a0\u009e\3\2\2\2\u00a1\u00a5\7\20\2\2\u00a2"
            + "\u00a4\t\3\2\2\u00a3\u00a2\3\2\2\2\u00a4\u00a7\3\2\2\2\u00a5\u00a3\3\2"
            + "\2\2\u00a5\u00a6\3\2\2\2\u00a6\u00aa\3\2\2\2\u00a7\u00a5\3\2\2\2\u00a8"
            + "\u00ab\5\"\22\2\u00a9\u00ab\5\22\n\2\u00aa\u00a8\3\2\2\2\u00aa\u00a9\3" + "\2\2\2\u00ab\25\3\2\2\2\u00ac\u00ae\t\3\2\2\u00ad\u00ac\3\2\2\2\u00ae"
            + "\u00b1\3\2\2\2\u00af\u00ad\3\2\2\2\u00af\u00b0\3\2\2\2\u00b0\u00b2\3\2"
            + "\2\2\u00b1\u00af\3\2\2\2\u00b2\u00b6\7\17\2\2\u00b3\u00b5\t\3\2\2\u00b4"
            + "\u00b3\3\2\2\2\u00b5\u00b8\3\2\2\2\u00b6\u00b4\3\2\2\2\u00b6\u00b7\3\2"
            + "\2\2\u00b7\u00bb\3\2\2\2\u00b8\u00b6\3\2\2\2\u00b9\u00bc\5\"\22\2\u00ba"
            + "\u00bc\5\22\n\2\u00bb\u00b9\3\2\2\2\u00bb\u00ba\3\2\2\2\u00bc\27\3\2\2"
            + "\2\u00bd\u00c2\5\22\n\2\u00be\u00c1\5\26\f\2\u00bf\u00c1\5\24\13\2\u00c0"
            + "\u00be\3\2\2\2\u00c0\u00bf\3\2\2\2\u00c1\u00c4\3\2\2\2\u00c2\u00c0\3\2"
            + "\2\2\u00c2\u00c3\3\2\2\2\u00c3\31\3\2\2\2\u00c4\u00c2\3\2\2\2\u00c5\u00c7"
            + "\t\3\2\2\u00c6\u00c5\3\2\2\2\u00c7\u00ca\3\2\2\2\u00c8\u00c6\3\2\2\2\u00c8"
            + "\u00c9\3\2\2\2\u00c9\u00cb\3\2\2\2\u00ca\u00c8\3\2\2\2\u00cb\u00cc\7\n"
            + "\2\2\u00cc\u00cd\5\"\22\2\u00cd\u00d1\7\13\2\2\u00ce\u00d0\t\3\2\2\u00cf"
            + "\u00ce\3\2\2\2\u00d0\u00d3\3\2\2\2\u00d1\u00cf\3\2\2\2\u00d1\u00d2\3\2"
            + "\2\2\u00d2\33\3\2\2\2\u00d3\u00d1\3\2\2\2\u00d4\u00d7\5\30\r\2\u00d5\u00d7"
            + "\5\32\16\2\u00d6\u00d4\3\2\2\2\u00d6\u00d5\3\2\2\2\u00d7\35\3\2\2\2\u00d8"
            + "\u00da\t\3\2\2\u00d9\u00d8\3\2\2\2\u00da\u00dd\3\2\2\2\u00db\u00d9\3\2"
            + "\2\2\u00db\u00dc\3\2\2\2\u00dc\u00de\3\2\2\2\u00dd\u00db\3\2\2\2\u00de"
            + "\u00df\7\17\2\2\u00df\u00e0\5\34\17\2\u00e0\37\3\2\2\2\u00e1\u00e3\t\3"
            + "\2\2\u00e2\u00e1\3\2\2\2\u00e3\u00e6\3\2\2\2\u00e4\u00e2\3\2\2\2\u00e4"
            + "\u00e5\3\2\2\2\u00e5\u00e7\3\2\2\2\u00e6\u00e4\3\2\2\2\u00e7\u00e8\7\20"
            + "\2\2\u00e8\u00e9\5\34\17\2\u00e9!\3\2\2\2\u00ea\u00ef\5\34\17\2\u00eb" + "\u00ee\5\36\20\2\u00ec\u00ee\5 \21\2\u00ed\u00eb\3\2\2\2\u00ed\u00ec\3"
            + "\2\2\2\u00ee\u00f1\3\2\2\2\u00ef\u00ed\3\2\2\2\u00ef\u00f0\3\2\2\2\u00f0"
            + "#\3\2\2\2\u00f1\u00ef\3\2\2\2\u00f2\u00f4\5\"\22\2\u00f3\u00f5\t\3\2\2"
            + "\u00f4\u00f3\3\2\2\2\u00f5\u00f6\3\2\2\2\u00f6\u00f4\3\2\2\2\u00f6\u00f7"
            + "\3\2\2\2\u00f7\u00f8\3\2\2\2\u00f8\u00f9\7\2\2\3\u00f9%\3\2\2\2!-\64>"
            + "ENV_emv|\u0082\u008a\u0092\u0099\u009e\u00a5\u00aa\u00af\u00b6\u00bb\u00c0" + "\u00c2\u00c8\u00d1\u00d6\u00db\u00e4\u00ed\u00ef\u00f6";
    public static final ATN _ATN = new ATNDeserializer().deserialize(_serializedATN.toCharArray());
    static {
        _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
        for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
            _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
        }
    }
}