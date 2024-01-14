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

//Generated from Bbq.g4 by ANTLR 4.13.1 

@SuppressWarnings({ "all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape" })
public class BbqLexer extends Lexer {
    static {
        RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION);
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

    public static final String _serializedATN = "\u0004\u0000\u0015\u00ac\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002"
            + "\u0001\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002"
            + "\u0004\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002"
            + "\u0007\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002"
            + "\u000b\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e"
            + "\u0002\u000f\u0007\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011"
            + "\u0002\u0012\u0007\u0012\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014"
            + "\u0002\u0015\u0007\u0015\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017"
            + "\u0002\u0018\u0007\u0018\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a"
            + "\u0002\u001b\u0007\u001b\u0002\u001c\u0007\u001c\u0002\u001d\u0007\u001d"
            + "\u0002\u001e\u0007\u001e\u0002\u001f\u0007\u001f\u0002 \u0007 \u0001\u0000"
            + "\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0003"
            + "\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0006"
            + "\u0001\u0006\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001\t\u0001\t\u0001"
            + "\n\u0001\n\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0001\r\u0001\r\u0001"
            + "\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0001\u0010\u0001\u0010\u0001"
            + "\u0011\u0001\u0011\u0001\u0012\u0001\u0012\u0001\u0013\u0001\u0013\u0001"
            + "\u0014\u0001\u0014\u0001\u0015\u0001\u0015\u0001\u0016\u0001\u0016\u0001"
            + "\u0016\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0018\u0001\u0018\u0001"
            + "\u0018\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u001a\u0001"
            + "\u001a\u0001\u001a\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001"
            + "\u001c\u0001\u001c\u0001\u001c\u0001\u001d\u0001\u001d\u0001\u001d\u0001"
            + "\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001e\u0004\u001e\u008f"
            + "\b\u001e\u000b\u001e\f\u001e\u0090\u0001\u001f\u0001\u001f\u0001\u001f"
            + "\u0001\u001f\u0001\u001f\u0005\u001f\u0098\b\u001f\n\u001f\f\u001f\u009b"
            + "\t\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0001 \u0001 \u0001 \u0001"
            + " \u0001 \u0005 \u00a5\b \n \f \u00a8\t \u0001 \u0001 \u0001 \u0000\u0000"
            + "!\u0001\u0001\u0003\u0002\u0005\u0003\u0007\u0004\t\u0005\u000b\u0006"
            + "\r\u0007\u000f\b\u0011\t\u0013\n\u0015\u0000\u0017\u0000\u0019\u0000\u001b"
            + "\u0000\u001d\u0000\u001f\u0000!\u0000#\u0000%\u0000\'\u0000)\u0000+\u0000"
            + "-\u000b/\f1\r3\u000e5\u000f7\u00109\u0011;\u0012=\u0013?\u0014A\u0015"
            + "\u0001\u0000\f\u0002\u0000AAaa\u0002\u0000NNnn\u0002\u0000DDdd\u0002\u0000"
            + "OOoo\u0002\u0000RRrr\u0002\u0000IIii\u0002\u0000TTtt\u0002\u0000MMmm\u0002"
            + "\u0000XXxx\u0007\u0000\t\n\r\r \"\'),,;;==\u0002\u0000\'\'\\\\\u0002\u0000"
            + "\"\"\\\\\u00a8\u0000\u0001\u0001\u0000\u0000\u0000\u0000\u0003\u0001\u0000"
            + "\u0000\u0000\u0000\u0005\u0001\u0000\u0000\u0000\u0000\u0007\u0001\u0000"
            + "\u0000\u0000\u0000\t\u0001\u0000\u0000\u0000\u0000\u000b\u0001\u0000\u0000"
            + "\u0000\u0000\r\u0001\u0000\u0000\u0000\u0000\u000f\u0001\u0000\u0000\u0000"
            + "\u0000\u0011\u0001\u0000\u0000\u0000\u0000\u0013\u0001\u0000\u0000\u0000"
            + "\u0000-\u0001\u0000\u0000\u0000\u0000/\u0001\u0000\u0000\u0000\u00001"
            + "\u0001\u0000\u0000\u0000\u00003\u0001\u0000\u0000\u0000\u00005\u0001\u0000"
            + "\u0000\u0000\u00007\u0001\u0000\u0000\u0000\u00009\u0001\u0000\u0000\u0000"
            + "\u0000;\u0001\u0000\u0000\u0000\u0000=\u0001\u0000\u0000\u0000\u0000?"
            + "\u0001\u0000\u0000\u0000\u0000A\u0001\u0000\u0000\u0000\u0001C\u0001\u0000"
            + "\u0000\u0000\u0003E\u0001\u0000\u0000\u0000\u0005G\u0001\u0000\u0000\u0000"
            + "\u0007I\u0001\u0000\u0000\u0000\tK\u0001\u0000\u0000\u0000\u000bM\u0001"
            + "\u0000\u0000\u0000\rO\u0001\u0000\u0000\u0000\u000fQ\u0001\u0000\u0000"
            + "\u0000\u0011S\u0001\u0000\u0000\u0000\u0013U\u0001\u0000\u0000\u0000\u0015"
            + "W\u0001\u0000\u0000\u0000\u0017Y\u0001\u0000\u0000\u0000\u0019[\u0001"
            + "\u0000\u0000\u0000\u001b]\u0001\u0000\u0000\u0000\u001d_\u0001\u0000\u0000"
            + "\u0000\u001fa\u0001\u0000\u0000\u0000!c\u0001\u0000\u0000\u0000#e\u0001"
            + "\u0000\u0000\u0000%g\u0001\u0000\u0000\u0000\'i\u0001\u0000\u0000\u0000"
            + ")k\u0001\u0000\u0000\u0000+m\u0001\u0000\u0000\u0000-o\u0001\u0000\u0000"
            + "\u0000/r\u0001\u0000\u0000\u00001u\u0001\u0000\u0000\u00003x\u0001\u0000"
            + "\u0000\u00005|\u0001\u0000\u0000\u00007\u007f\u0001\u0000\u0000\u0000" + "9\u0083\u0001\u0000\u0000\u0000;\u0086\u0001\u0000\u0000\u0000=\u008e"
            + "\u0001\u0000\u0000\u0000?\u0092\u0001\u0000\u0000\u0000A\u009f\u0001\u0000"
            + "\u0000\u0000CD\u0005 \u0000\u0000D\u0002\u0001\u0000\u0000\u0000EF\u0005"
            + "\t\u0000\u0000F\u0004\u0001\u0000\u0000\u0000GH\u0005\r\u0000\u0000H\u0006"
            + "\u0001\u0000\u0000\u0000IJ\u0005\n\u0000\u0000J\b\u0001\u0000\u0000\u0000"
            + "KL\u0005,\u0000\u0000L\n\u0001\u0000\u0000\u0000MN\u0005=\u0000\u0000"
            + "N\f\u0001\u0000\u0000\u0000OP\u0005!\u0000\u0000P\u000e\u0001\u0000\u0000"
            + "\u0000QR\u0005(\u0000\u0000R\u0010\u0001\u0000\u0000\u0000ST\u0005;\u0000"
            + "\u0000T\u0012\u0001\u0000\u0000\u0000UV\u0005)\u0000\u0000V\u0014\u0001"
            + "\u0000\u0000\u0000WX\u0007\u0000\u0000\u0000X\u0016\u0001\u0000\u0000"
            + "\u0000YZ\u0007\u0001\u0000\u0000Z\u0018\u0001\u0000\u0000\u0000[\\\u0007"
            + "\u0002\u0000\u0000\\\u001a\u0001\u0000\u0000\u0000]^\u0007\u0003\u0000"
            + "\u0000^\u001c\u0001\u0000\u0000\u0000_`\u0007\u0004\u0000\u0000`\u001e"
            + "\u0001\u0000\u0000\u0000ab\u0007\u0005\u0000\u0000b \u0001\u0000\u0000"
            + "\u0000cd\u0007\u0006\u0000\u0000d\"\u0001\u0000\u0000\u0000ef\u0007\u0007"
            + "\u0000\u0000f$\u0001\u0000\u0000\u0000gh\u0007\b\u0000\u0000h&\u0001\u0000"
            + "\u0000\u0000ij\u0005\'\u0000\u0000j(\u0001\u0000\u0000\u0000kl\u0005\""
            + "\u0000\u0000l*\u0001\u0000\u0000\u0000mn\u0005\\\u0000\u0000n,\u0001\u0000"
            + "\u0000\u0000op\u0003+\u0015\u0000pq\u0003+\u0015\u0000q.\u0001\u0000\u0000"
            + "\u0000rs\u0003+\u0015\u0000st\u0003\'\u0013\u0000t0\u0001\u0000\u0000"
            + "\u0000uv\u0003+\u0015\u0000vw\u0003)\u0014\u0000w2\u0001\u0000\u0000\u0000"
            + "xy\u0003\u0015\n\u0000yz\u0003\u0017\u000b\u0000z{\u0003\u0019\f\u0000"
            + "{4\u0001\u0000\u0000\u0000|}\u0003\u001b\r\u0000}~\u0003\u001d\u000e\u0000"
            + "~6\u0001\u0000\u0000\u0000\u007f\u0080\u0003\u0017\u000b\u0000\u0080\u0081"
            + "\u0003\u001b\r\u0000\u0081\u0082\u0003!\u0010\u0000\u00828\u0001\u0000"
            + "\u0000\u0000\u0083\u0084\u0003\u001f\u000f\u0000\u0084\u0085\u0003\u0017"
            + "\u000b\u0000\u0085:\u0001\u0000\u0000\u0000\u0086\u0087\u0003#\u0011\u0000"
            + "\u0087\u0088\u0003\u001f\u000f\u0000\u0088\u0089\u0003\u0017\u000b\u0000"
            + "\u0089\u008a\u0003#\u0011\u0000\u008a\u008b\u0003\u0015\n\u0000\u008b" + "\u008c\u0003%\u0012\u0000\u008c<\u0001\u0000\u0000\u0000\u008d\u008f\b"
            + "\t\u0000\u0000\u008e\u008d\u0001\u0000\u0000\u0000\u008f\u0090\u0001\u0000"
            + "\u0000\u0000\u0090\u008e\u0001\u0000\u0000\u0000\u0090\u0091\u0001\u0000"
            + "\u0000\u0000\u0091>\u0001\u0000\u0000\u0000\u0092\u0099\u0003\'\u0013"
            + "\u0000\u0093\u0098\b\n\u0000\u0000\u0094\u0098\u0005 \u0000\u0000\u0095"
            + "\u0098\u0003-\u0016\u0000\u0096\u0098\u0003/\u0017\u0000\u0097\u0093\u0001"
            + "\u0000\u0000\u0000\u0097\u0094\u0001\u0000\u0000\u0000\u0097\u0095\u0001"
            + "\u0000\u0000\u0000\u0097\u0096\u0001\u0000\u0000\u0000\u0098\u009b\u0001"
            + "\u0000\u0000\u0000\u0099\u0097\u0001\u0000\u0000\u0000\u0099\u009a\u0001"
            + "\u0000\u0000\u0000\u009a\u009c\u0001\u0000\u0000\u0000\u009b\u0099\u0001"
            + "\u0000\u0000\u0000\u009c\u009d\u0003\'\u0013\u0000\u009d\u009e\u0006\u001f"
            + "\u0000\u0000\u009e@\u0001\u0000\u0000\u0000\u009f\u00a6\u0003)\u0014\u0000"
            + "\u00a0\u00a5\b\u000b\u0000\u0000\u00a1\u00a5\u0005 \u0000\u0000\u00a2"
            + "\u00a5\u0003-\u0016\u0000\u00a3\u00a5\u00031\u0018\u0000\u00a4\u00a0\u0001"
            + "\u0000\u0000\u0000\u00a4\u00a1\u0001\u0000\u0000\u0000\u00a4\u00a2\u0001"
            + "\u0000\u0000\u0000\u00a4\u00a3\u0001\u0000\u0000\u0000\u00a5\u00a8\u0001"
            + "\u0000\u0000\u0000\u00a6\u00a4\u0001\u0000\u0000\u0000\u00a6\u00a7\u0001"
            + "\u0000\u0000\u0000\u00a7\u00a9\u0001\u0000\u0000\u0000\u00a8\u00a6\u0001"
            + "\u0000\u0000\u0000\u00a9\u00aa\u0003)\u0014\u0000\u00aa\u00ab\u0006 \u0001"
            + "\u0000\u00abB\u0001\u0000\u0000\u0000\u0006\u0000\u0090\u0097\u0099\u00a4" + "\u00a6\u0002\u0001\u001f\u0000\u0001 \u0001";
    public static final ATN _ATN = new ATNDeserializer().deserialize(_serializedATN.toCharArray());
    static {
        _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
        for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
            _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
        }
    }
}