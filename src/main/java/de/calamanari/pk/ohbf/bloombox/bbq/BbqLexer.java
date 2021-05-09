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
// Generated from Bbq.g4 by ANTLR 4.9.2

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

@SuppressWarnings({ "all", "warnings", "unchecked", "unused", "cast" })
public class BbqLexer extends Lexer {
    static {
        RuntimeMetaData.checkVersion("4.9.2", RuntimeMetaData.VERSION);
    }

    protected static final DFA[] _decisionToDFA;
    protected static final PredictionContextCache _sharedContextCache = new PredictionContextCache();
    public static final int T__0 = 1, T__1 = 2, T__2 = 3, T__3 = 4, T__4 = 5, T__5 = 6, T__6 = 7, T__7 = 8, T__8 = 9, ESCESC = 10, ESCSQ = 11, ESCDQ = 12,
            AND = 13, OR = 14, NOT = 15, IN = 16, SIMPLE_WORD = 17, SQWORD = 18, DQWORD = 19;
    public static String[] channelNames = { "DEFAULT_TOKEN_CHANNEL", "HIDDEN" };

    public static String[] modeNames = { "DEFAULT_MODE" };

    private static String[] makeRuleNames() {
        return new String[] { "T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", "A", "N", "D", "O", "R", "I", "T", "SQUOTE", "DQUOTE",
                "ESCAPE", "ESCESC", "ESCSQ", "ESCDQ", "AND", "OR", "NOT", "IN", "SIMPLE_WORD", "SQWORD", "DQWORD" };
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
        case 27:
            SQWORD_action(_localctx, actionIndex);
            break;
        case 28:
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

    public static final String _serializedATN = "\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\25\u0099\b\1\4\2"
            + "\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4" + "\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"
            + "\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31" + "\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\3\2\3\2\3\3\3"
            + "\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3" + "\f\3\f\3\r\3\r\3\16\3\16\3\17\3\17\3\20\3\20\3\21\3\21\3\22\3\22\3\23"
            + "\3\23\3\24\3\24\3\25\3\25\3\25\3\26\3\26\3\26\3\27\3\27\3\27\3\30\3\30"
            + "\3\30\3\30\3\31\3\31\3\31\3\32\3\32\3\32\3\32\3\33\3\33\3\33\3\34\6\34"
            + "|\n\34\r\34\16\34}\3\35\3\35\3\35\3\35\3\35\7\35\u0085\n\35\f\35\16\35"
            + "\u0088\13\35\3\35\3\35\3\35\3\36\3\36\3\36\3\36\3\36\7\36\u0092\n\36\f"
            + "\36\16\36\u0095\13\36\3\36\3\36\3\36\2\2\37\3\3\5\4\7\5\t\6\13\7\r\b\17"
            + "\t\21\n\23\13\25\2\27\2\31\2\33\2\35\2\37\2!\2#\2%\2\'\2)\f+\r-\16/\17" + "\61\20\63\21\65\22\67\239\24;\25\3\2\f\4\2CCcc\4\2PPpp\4\2FFff\4\2QQq"
            + "q\4\2TTtt\4\2KKkk\4\2VVvv\b\2\13\f\17\17\"$)+..??\4\2))^^\4\2$$^^\2\u0097"
            + "\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2" + "\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3"
            + "\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2" + "\29\3\2\2\2\2;\3\2\2\2\3=\3\2\2\2\5?\3\2\2\2\7A\3\2\2\2\tC\3\2\2\2\13"
            + "E\3\2\2\2\rG\3\2\2\2\17I\3\2\2\2\21K\3\2\2\2\23M\3\2\2\2\25O\3\2\2\2\27"
            + "Q\3\2\2\2\31S\3\2\2\2\33U\3\2\2\2\35W\3\2\2\2\37Y\3\2\2\2![\3\2\2\2#]" + "\3\2\2\2%_\3\2\2\2\'a\3\2\2\2)c\3\2\2\2+f\3\2\2\2-i\3\2\2\2/l\3\2\2\2"
            + "\61p\3\2\2\2\63s\3\2\2\2\65w\3\2\2\2\67{\3\2\2\29\177\3\2\2\2;\u008c\3" + "\2\2\2=>\7\"\2\2>\4\3\2\2\2?@\7\13\2\2@\6\3\2\2\2AB\7\17\2\2B\b\3\2\2"
            + "\2CD\7\f\2\2D\n\3\2\2\2EF\7.\2\2F\f\3\2\2\2GH\7?\2\2H\16\3\2\2\2IJ\7#" + "\2\2J\20\3\2\2\2KL\7*\2\2L\22\3\2\2\2MN\7+\2\2N\24\3\2\2\2OP\t\2\2\2P"
            + "\26\3\2\2\2QR\t\3\2\2R\30\3\2\2\2ST\t\4\2\2T\32\3\2\2\2UV\t\5\2\2V\34" + "\3\2\2\2WX\t\6\2\2X\36\3\2\2\2YZ\t\7\2\2Z \3\2\2\2[\\\t\b\2\2\\\"\3\2"
            + "\2\2]^\7)\2\2^$\3\2\2\2_`\7$\2\2`&\3\2\2\2ab\7^\2\2b(\3\2\2\2cd\5\'\24" + "\2de\5\'\24\2e*\3\2\2\2fg\5\'\24\2gh\5#\22\2h,\3\2\2\2ij\5\'\24\2jk\5"
            + "%\23\2k.\3\2\2\2lm\5\25\13\2mn\5\27\f\2no\5\31\r\2o\60\3\2\2\2pq\5\33" + "\16\2qr\5\35\17\2r\62\3\2\2\2st\5\27\f\2tu\5\33\16\2uv\5!\21\2v\64\3\2"
            + "\2\2wx\5\37\20\2xy\5\27\f\2y\66\3\2\2\2z|\n\t\2\2{z\3\2\2\2|}\3\2\2\2"
            + "}{\3\2\2\2}~\3\2\2\2~8\3\2\2\2\177\u0086\5#\22\2\u0080\u0085\n\n\2\2\u0081"
            + "\u0085\7\"\2\2\u0082\u0085\5)\25\2\u0083\u0085\5+\26\2\u0084\u0080\3\2"
            + "\2\2\u0084\u0081\3\2\2\2\u0084\u0082\3\2\2\2\u0084\u0083\3\2\2\2\u0085"
            + "\u0088\3\2\2\2\u0086\u0084\3\2\2\2\u0086\u0087\3\2\2\2\u0087\u0089\3\2"
            + "\2\2\u0088\u0086\3\2\2\2\u0089\u008a\5#\22\2\u008a\u008b\b\35\2\2\u008b"
            + ":\3\2\2\2\u008c\u0093\5%\23\2\u008d\u0092\n\13\2\2\u008e\u0092\7\"\2\2"
            + "\u008f\u0092\5)\25\2\u0090\u0092\5-\27\2\u0091\u008d\3\2\2\2\u0091\u008e"
            + "\3\2\2\2\u0091\u008f\3\2\2\2\u0091\u0090\3\2\2\2\u0092\u0095\3\2\2\2\u0093"
            + "\u0091\3\2\2\2\u0093\u0094\3\2\2\2\u0094\u0096\3\2\2\2\u0095\u0093\3\2"
            + "\2\2\u0096\u0097\5%\23\2\u0097\u0098\b\36\3\2\u0098<\3\2\2\2\b\2}\u0084" + "\u0086\u0091\u0093\4\3\35\2\3\36\3";
    public static final ATN _ATN = new ATNDeserializer().deserialize(_serializedATN.toCharArray());
    static {
        _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
        for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
            _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
        }
    }
}