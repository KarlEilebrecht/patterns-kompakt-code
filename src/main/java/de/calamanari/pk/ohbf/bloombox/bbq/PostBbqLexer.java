//@formatter:off
/*
 * PostBbqLexer
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
public class PostBbqLexer extends Lexer {
    static {
        RuntimeMetaData.checkVersion("4.9.2", RuntimeMetaData.VERSION);
    }

    protected static final DFA[] _decisionToDFA;
    protected static final PredictionContextCache _sharedContextCache = new PredictionContextCache();
    public static final int T__0 = 1, T__1 = 2, T__2 = 3, T__3 = 4, T__4 = 5, T__5 = 6, T__6 = 7, T__7 = 8, T__8 = 9, T__9 = 10, T__10 = 11, ESCESC = 12,
            ESCSQ = 13, ESCDQ = 14, UNION = 15, INTERSECT = 16, MINUS = 17, MINMAX = 18, SIMPLE_WORD = 19, SQWORD = 20, DQWORD = 21;
    public static String[] channelNames = { "DEFAULT_TOKEN_CHANNEL", "HIDDEN" };

    public static String[] modeNames = { "DEFAULT_MODE" };

    private static String[] makeRuleNames() {
        return new String[] { "T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", "T__9", "T__10", "A", "U", "N", "I", "O", "E", "R", "T",
                "S", "C", "M", "X", "SQUOTE", "DQUOTE", "ESCAPE", "ESCESC", "ESCSQ", "ESCDQ", "UNION", "INTERSECT", "MINUS", "MINMAX", "SIMPLE_WORD", "SQWORD",
                "DQWORD" };
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

    public PostBbqLexer(CharStream input) {
        super(input);
        _interp = new LexerATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
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
        case 34:
            SQWORD_action(_localctx, actionIndex);
            break;
        case 35:
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

    public static final String _serializedATN = "\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\27\u00c4\b\1\4\2"
            + "\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4" + "\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"
            + "\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"
            + "\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t" + " \4!\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6"
            + "\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3\16\3" + "\16\3\17\3\17\3\20\3\20\3\21\3\21\3\22\3\22\3\23\3\23\3\24\3\24\3\25\3"
            + "\25\3\26\3\26\3\27\3\27\3\30\3\30\3\31\3\31\3\32\3\32\3\33\3\33\3\34\3"
            + "\34\3\34\3\35\3\35\3\35\3\36\3\36\3\36\3\37\3\37\3\37\3\37\3\37\3\37\3"
            + " \3 \3 \3 \3 \3 \3 \3 \3 \3 \3!\3!\3!\3!\3!\3!\3\"\3\"\3\"\3\"\3\"\3\""
            + "\3\"\3#\6#\u00a7\n#\r#\16#\u00a8\3$\3$\3$\3$\3$\7$\u00b0\n$\f$\16$\u00b3"
            + "\13$\3$\3$\3$\3%\3%\3%\3%\3%\7%\u00bd\n%\f%\16%\u00c0\13%\3%\3%\3%\2\2"
            + "&\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\2\33\2\35\2\37" + "\2!\2#\2%\2\'\2)\2+\2-\2/\2\61\2\63\2\65\2\67\169\17;\20=\21?\22A\23C"
            + "\24E\25G\26I\27\3\2\21\4\2CCcc\4\2WWww\4\2PPpp\4\2KKkk\4\2QQqq\4\2GGg" + "g\4\2TTtt\4\2VVvv\4\2UUuu\4\2EEee\4\2OOoo\4\2ZZzz\f\2\13\f\17\17\"\"$"
            + "$&&)+..==}}\177\177\4\2))^^\4\2$$^^\2\u00bd\2\3\3\2\2\2\2\5\3\2\2\2\2" + "\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2"
            + "\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2" + ";\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3"
            + "\2\2\2\2I\3\2\2\2\3K\3\2\2\2\5M\3\2\2\2\7O\3\2\2\2\tQ\3\2\2\2\13S\3\2" + "\2\2\rU\3\2\2\2\17W\3\2\2\2\21Y\3\2\2\2\23[\3\2\2\2\25]\3\2\2\2\27_\3"
            + "\2\2\2\31a\3\2\2\2\33c\3\2\2\2\35e\3\2\2\2\37g\3\2\2\2!i\3\2\2\2#k\3\2" + "\2\2%m\3\2\2\2\'o\3\2\2\2)q\3\2\2\2+s\3\2\2\2-u\3\2\2\2/w\3\2\2\2\61y"
            + "\3\2\2\2\63{\3\2\2\2\65}\3\2\2\2\67\177\3\2\2\29\u0082\3\2\2\2;\u0085" + "\3\2\2\2=\u0088\3\2\2\2?\u008e\3\2\2\2A\u0098\3\2\2\2C\u009e\3\2\2\2E"
            + "\u00a6\3\2\2\2G\u00aa\3\2\2\2I\u00b7\3\2\2\2KL\7&\2\2L\4\3\2\2\2MN\7}" + "\2\2N\6\3\2\2\2OP\7\"\2\2P\b\3\2\2\2QR\7\13\2\2R\n\3\2\2\2ST\7\17\2\2"
            + "T\f\3\2\2\2UV\7\f\2\2V\16\3\2\2\2WX\7\177\2\2X\20\3\2\2\2YZ\7*\2\2Z\22" + "\3\2\2\2[\\\7.\2\2\\\24\3\2\2\2]^\7=\2\2^\26\3\2\2\2_`\7+\2\2`\30\3\2"
            + "\2\2ab\t\2\2\2b\32\3\2\2\2cd\t\3\2\2d\34\3\2\2\2ef\t\4\2\2f\36\3\2\2\2" + "gh\t\5\2\2h \3\2\2\2ij\t\6\2\2j\"\3\2\2\2kl\t\7\2\2l$\3\2\2\2mn\t\b\2"
            + "\2n&\3\2\2\2op\t\t\2\2p(\3\2\2\2qr\t\n\2\2r*\3\2\2\2st\t\13\2\2t,\3\2" + "\2\2uv\t\f\2\2v.\3\2\2\2wx\t\r\2\2x\60\3\2\2\2yz\7)\2\2z\62\3\2\2\2{|"
            + "\7$\2\2|\64\3\2\2\2}~\7^\2\2~\66\3\2\2\2\177\u0080\5\65\33\2\u0080\u0081"
            + "\5\65\33\2\u00818\3\2\2\2\u0082\u0083\5\65\33\2\u0083\u0084\5\61\31\2" + "\u0084:\3\2\2\2\u0085\u0086\5\65\33\2\u0086\u0087\5\63\32\2\u0087<\3\2"
            + "\2\2\u0088\u0089\5\33\16\2\u0089\u008a\5\35\17\2\u008a\u008b\5\37\20\2" + "\u008b\u008c\5!\21\2\u008c\u008d\5\35\17\2\u008d>\3\2\2\2\u008e\u008f"
            + "\5\37\20\2\u008f\u0090\5\35\17\2\u0090\u0091\5\'\24\2\u0091\u0092\5#\22"
            + "\2\u0092\u0093\5%\23\2\u0093\u0094\5)\25\2\u0094\u0095\5#\22\2\u0095\u0096"
            + "\5+\26\2\u0096\u0097\5\'\24\2\u0097@\3\2\2\2\u0098\u0099\5-\27\2\u0099"
            + "\u009a\5\37\20\2\u009a\u009b\5\35\17\2\u009b\u009c\5\33\16\2\u009c\u009d"
            + "\5)\25\2\u009dB\3\2\2\2\u009e\u009f\5-\27\2\u009f\u00a0\5\37\20\2\u00a0"
            + "\u00a1\5\35\17\2\u00a1\u00a2\5-\27\2\u00a2\u00a3\5\31\r\2\u00a3\u00a4" + "\5/\30\2\u00a4D\3\2\2\2\u00a5\u00a7\n\16\2\2\u00a6\u00a5\3\2\2\2\u00a7"
            + "\u00a8\3\2\2\2\u00a8\u00a6\3\2\2\2\u00a8\u00a9\3\2\2\2\u00a9F\3\2\2\2" + "\u00aa\u00b1\5\61\31\2\u00ab\u00b0\n\17\2\2\u00ac\u00b0\7\"\2\2\u00ad"
            + "\u00b0\5\67\34\2\u00ae\u00b0\59\35\2\u00af\u00ab\3\2\2\2\u00af\u00ac\3"
            + "\2\2\2\u00af\u00ad\3\2\2\2\u00af\u00ae\3\2\2\2\u00b0\u00b3\3\2\2\2\u00b1"
            + "\u00af\3\2\2\2\u00b1\u00b2\3\2\2\2\u00b2\u00b4\3\2\2\2\u00b3\u00b1\3\2"
            + "\2\2\u00b4\u00b5\5\61\31\2\u00b5\u00b6\b$\2\2\u00b6H\3\2\2\2\u00b7\u00be"
            + "\5\63\32\2\u00b8\u00bd\n\20\2\2\u00b9\u00bd\7\"\2\2\u00ba\u00bd\5\67\34"
            + "\2\u00bb\u00bd\5;\36\2\u00bc\u00b8\3\2\2\2\u00bc\u00b9\3\2\2\2\u00bc\u00ba"
            + "\3\2\2\2\u00bc\u00bb\3\2\2\2\u00bd\u00c0\3\2\2\2\u00be\u00bc\3\2\2\2\u00be"
            + "\u00bf\3\2\2\2\u00bf\u00c1\3\2\2\2\u00c0\u00be\3\2\2\2\u00c1\u00c2\5\63"
            + "\32\2\u00c2\u00c3\b%\3\2\u00c3J\3\2\2\2\b\2\u00a8\u00af\u00b1\u00bc\u00be" + "\4\3$\2\3%\3";
    public static final ATN _ATN = new ATNDeserializer().deserialize(_serializedATN.toCharArray());
    static {
        _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
        for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
            _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
        }
    }
}