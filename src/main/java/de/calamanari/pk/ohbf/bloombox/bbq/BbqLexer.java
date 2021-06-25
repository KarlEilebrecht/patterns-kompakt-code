//@formatter:off
/*
 * BbqLexer
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

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.RuntimeMetaData;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.VocabularyImpl;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.LexerATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;

// Generated from Bbq.g4 by ANTLR 4.9.2

@SuppressWarnings({ "all", "warnings", "unchecked", "unused", "cast" })
public class BbqLexer extends Lexer {
    static {
        RuntimeMetaData.checkVersion("4.9.2", RuntimeMetaData.VERSION);
    }

    protected static final DFA[] _decisionToDFA;
    protected static final PredictionContextCache _sharedContextCache = new PredictionContextCache();
    public static final int T__0 = 1, T__1 = 2, T__2 = 3, T__3 = 4, T__4 = 5, T__5 = 6, T__6 = 7, T__7 = 8, T__8 = 9, T__9 = 10, ESCESC = 11, ESCSQ = 12,
            ESCDQ = 13, AND = 14, OR = 15, NOT = 16, IN = 17, MINMAX = 18, SIMPLE_WORD = 19, SQWORD = 20, DQWORD = 21;
    public static String[] channelNames = { "DEFAULT_TOKEN_CHANNEL", "HIDDEN" };

    public static String[] modeNames = { "DEFAULT_MODE" };

    private static String[] makeRuleNames() {
        return new String[] { "T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", "T__9", "A", "N", "D", "O", "R", "I", "T", "M", "X",
                "SQUOTE", "DQUOTE", "ESCAPE", "ESCESC", "ESCSQ", "ESCDQ", "AND", "OR", "NOT", "IN", "MINMAX", "SIMPLE_WORD", "SQWORD", "DQWORD" };
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

    public BbqLexer(CharStream input) {
        super(input);
        _interp = new LexerATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
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
    public String[] getChannelNames() {
        return channelNames;
    }

    @Override
    public String[] getModeNames() {
        return modeNames;
    }

    @Override
    public ATN getATN() {
        return _ATN;
    }

    @Override
    public void action(RuleContext _localctx, int ruleIndex, int actionIndex) {
        switch (ruleIndex) {
        case 31:
            SQWORD_action(_localctx, actionIndex);
            break;
        case 32:
            DQWORD_action(_localctx, actionIndex);
            break;
        }
    }

    private void SQWORD_action(RuleContext _localctx, int actionIndex) {
        switch (actionIndex) {
        case 0:
            setText(getText().substring(1, getText().length() - 1).replace("\\'", "'").replace("\\\\", "\\"));
            break;
        }
    }

    private void DQWORD_action(RuleContext _localctx, int actionIndex) {
        switch (actionIndex) {
        case 1:
            setText(getText().substring(1, getText().length() - 1).replace("\\\"", "\"").replace("\\\\", "\\"));
            break;
        }
    }

    public static final String _serializedATN = "\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\27\u00ae\b\1\4\2"
            + "\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4" + "\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"
            + "\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"
            + "\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t" + " \4!\t!\4\"\t\"\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3"
            + "\b\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3\16\3\16\3\17\3\17\3\20" + "\3\20\3\21\3\21\3\22\3\22\3\23\3\23\3\24\3\24\3\25\3\25\3\26\3\26\3\27"
            + "\3\27\3\30\3\30\3\30\3\31\3\31\3\31\3\32\3\32\3\32\3\33\3\33\3\33\3\33"
            + "\3\34\3\34\3\34\3\35\3\35\3\35\3\35\3\36\3\36\3\36\3\37\3\37\3\37\3\37" + "\3\37\3\37\3\37\3 \6 \u0091\n \r \16 \u0092\3!\3!\3!\3!\3!\7!\u009a\n"
            + "!\f!\16!\u009d\13!\3!\3!\3!\3\"\3\"\3\"\3\"\3\"\7\"\u00a7\n\"\f\"\16\"" + "\u00aa\13\"\3\"\3\"\3\"\2\2#\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13"
            + "\25\f\27\2\31\2\33\2\35\2\37\2!\2#\2%\2\'\2)\2+\2-\2/\r\61\16\63\17\65" + "\20\67\219\22;\23=\24?\25A\26C\27\3\2\16\4\2CCcc\4\2PPpp\4\2FFff\4\2Q"
            + "Qqq\4\2TTtt\4\2KKkk\4\2VVvv\4\2OOoo\4\2ZZzz\t\2\13\f\17\17\"$)+..==??" + "\4\2))^^\4\2$$^^\2\u00aa\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2"
            + "\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25"
            + "\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2" + "\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\3"
            + "E\3\2\2\2\5G\3\2\2\2\7I\3\2\2\2\tK\3\2\2\2\13M\3\2\2\2\rO\3\2\2\2\17Q"
            + "\3\2\2\2\21S\3\2\2\2\23U\3\2\2\2\25W\3\2\2\2\27Y\3\2\2\2\31[\3\2\2\2\33"
            + "]\3\2\2\2\35_\3\2\2\2\37a\3\2\2\2!c\3\2\2\2#e\3\2\2\2%g\3\2\2\2\'i\3\2"
            + "\2\2)k\3\2\2\2+m\3\2\2\2-o\3\2\2\2/q\3\2\2\2\61t\3\2\2\2\63w\3\2\2\2\65"
            + "z\3\2\2\2\67~\3\2\2\29\u0081\3\2\2\2;\u0085\3\2\2\2=\u0088\3\2\2\2?\u0090"
            + "\3\2\2\2A\u0094\3\2\2\2C\u00a1\3\2\2\2EF\7\"\2\2F\4\3\2\2\2GH\7\13\2\2"
            + "H\6\3\2\2\2IJ\7\17\2\2J\b\3\2\2\2KL\7\f\2\2L\n\3\2\2\2MN\7.\2\2N\f\3\2" + "\2\2OP\7?\2\2P\16\3\2\2\2QR\7#\2\2R\20\3\2\2\2ST\7*\2\2T\22\3\2\2\2UV"
            + "\7=\2\2V\24\3\2\2\2WX\7+\2\2X\26\3\2\2\2YZ\t\2\2\2Z\30\3\2\2\2[\\\t\3" + "\2\2\\\32\3\2\2\2]^\t\4\2\2^\34\3\2\2\2_`\t\5\2\2`\36\3\2\2\2ab\t\6\2"
            + "\2b \3\2\2\2cd\t\7\2\2d\"\3\2\2\2ef\t\b\2\2f$\3\2\2\2gh\t\t\2\2h&\3\2" + "\2\2ij\t\n\2\2j(\3\2\2\2kl\7)\2\2l*\3\2\2\2mn\7$\2\2n,\3\2\2\2op\7^\2"
            + "\2p.\3\2\2\2qr\5-\27\2rs\5-\27\2s\60\3\2\2\2tu\5-\27\2uv\5)\25\2v\62\3"
            + "\2\2\2wx\5-\27\2xy\5+\26\2y\64\3\2\2\2z{\5\27\f\2{|\5\31\r\2|}\5\33\16"
            + "\2}\66\3\2\2\2~\177\5\35\17\2\177\u0080\5\37\20\2\u00808\3\2\2\2\u0081"
            + "\u0082\5\31\r\2\u0082\u0083\5\35\17\2\u0083\u0084\5#\22\2\u0084:\3\2\2"
            + "\2\u0085\u0086\5!\21\2\u0086\u0087\5\31\r\2\u0087<\3\2\2\2\u0088\u0089" + "\5%\23\2\u0089\u008a\5!\21\2\u008a\u008b\5\31\r\2\u008b\u008c\5%\23\2"
            + "\u008c\u008d\5\27\f\2\u008d\u008e\5\'\24\2\u008e>\3\2\2\2\u008f\u0091" + "\n\13\2\2\u0090\u008f\3\2\2\2\u0091\u0092\3\2\2\2\u0092\u0090\3\2\2\2"
            + "\u0092\u0093\3\2\2\2\u0093@\3\2\2\2\u0094\u009b\5)\25\2\u0095\u009a\n"
            + "\f\2\2\u0096\u009a\7\"\2\2\u0097\u009a\5/\30\2\u0098\u009a\5\61\31\2\u0099"
            + "\u0095\3\2\2\2\u0099\u0096\3\2\2\2\u0099\u0097\3\2\2\2\u0099\u0098\3\2"
            + "\2\2\u009a\u009d\3\2\2\2\u009b\u0099\3\2\2\2\u009b\u009c\3\2\2\2\u009c" + "\u009e\3\2\2\2\u009d\u009b\3\2\2\2\u009e\u009f\5)\25\2\u009f\u00a0\b!"
            + "\2\2\u00a0B\3\2\2\2\u00a1\u00a8\5+\26\2\u00a2\u00a7\n\r\2\2\u00a3\u00a7"
            + "\7\"\2\2\u00a4\u00a7\5/\30\2\u00a5\u00a7\5\63\32\2\u00a6\u00a2\3\2\2\2"
            + "\u00a6\u00a3\3\2\2\2\u00a6\u00a4\3\2\2\2\u00a6\u00a5\3\2\2\2\u00a7\u00aa"
            + "\3\2\2\2\u00a8\u00a6\3\2\2\2\u00a8\u00a9\3\2\2\2\u00a9\u00ab\3\2\2\2\u00aa"
            + "\u00a8\3\2\2\2\u00ab\u00ac\5+\26\2\u00ac\u00ad\b\"\3\2\u00adD\3\2\2\2" + "\b\2\u0092\u0099\u009b\u00a6\u00a8\4\3!\2\3\"\3";
    public static final ATN _ATN = new ATNDeserializer().deserialize(_serializedATN.toCharArray());
    static {
        _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
        for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
            _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
        }
    }
}