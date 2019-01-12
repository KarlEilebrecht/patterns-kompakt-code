package de.calamanari.other;

import java.util.ArrayList;
import java.util.List;

public class SourceLineInterpreter {

    CommentType commentType = CommentType.NONE;

    List<Phrase> phrases = new ArrayList<>();

    int phraseStart = -1;
    StringBuilder currentPhrase = new StringBuilder();
    boolean lastCharWasAsterisk = false;

    int positionInLine = -1;

    public void readNext(SourceLine line) {
        phrases.clear();
        phraseStart = line.startPos;
        currentPhrase.setLength(0);
        positionInLine = line.startPos;
        for (; positionInLine < line.endPos; positionInLine++) {
            char ch = line.charAt(positionInLine);
            if (commentType != CommentType.NONE) {
                handleCharacterInsideComment(ch);
            }
            else if (ch == '"') {
                handleDoubleQuote();
            }
            else if (ch == '\'') {
                handleSingleQuote();
            }
            else if (ch == '/') {
                handleSlash(line);
            }
            else if (Character.isJavaIdentifierPart(ch) || ch == '@') {
                currentPhrase.append(ch);
            }
            else {
                addPendingPhrase();
                beginNewPhrase();
            }
        }
        addPendingPhrase();
        if (commentType == CommentType.END_OF_LINE || commentType == CommentType.CHAR_LITERAL || commentType == CommentType.STRING_LITERAL) {
            commentType = CommentType.NONE;
        }
    }

    private void handleCharacterInsideComment(char ch) {
        currentPhrase.append(ch);
        if (commentType == CommentType.STRING_LITERAL) {
            handleCharacterInStringLiteral(ch);
        }
        else if (commentType == CommentType.CHAR_LITERAL) {
            handleCharacterInCharacterLiteral(ch);
        }
        else if (ch == '*') {
            lastCharWasAsterisk = true;
        }
        else if (ch == '/' && lastCharWasAsterisk) {
            handleEndOfComment();
        }
        else {
            lastCharWasAsterisk = false;
        }
    }

    private void handleSlash(SourceLine line) {
        if (positionInLine < line.endPos - 2 && line.charAt(positionInLine + 1) == '*' && line.charAt(positionInLine + 2) == '*') {
            addPendingPhrase();
            currentPhrase.setLength(0);
            phraseStart = positionInLine;
            commentType = CommentType.DOUBLE_STAR;
            currentPhrase.append('/');
            currentPhrase.append(line.charAt(positionInLine + 1));
            currentPhrase.append(line.charAt(positionInLine + 2));
            positionInLine = positionInLine + 2;
        }
        else if (positionInLine < line.endPos - 1 && line.charAt(positionInLine + 1) == '*') {
            addPendingPhrase();
            currentPhrase.setLength(0);
            phraseStart = positionInLine;
            commentType = CommentType.SIMPLE_STAR;
            currentPhrase.append('/');
            currentPhrase.append(line.charAt(positionInLine + 1));
            positionInLine++;
        }
        else if (positionInLine < line.endPos - 1 && line.charAt(positionInLine + 1) == '/') {
            addPendingPhrase();
            currentPhrase.setLength(0);
            currentPhrase.append(line.subSequence(positionInLine, line.endPos));
            phraseStart = positionInLine;
            commentType = CommentType.END_OF_LINE;
            positionInLine = line.endPos;
        }
        else {
            currentPhrase.append('/');
        }
    }

    private void handleSingleQuote() {
        addPendingPhrase();
        currentPhrase.setLength(0);
        phraseStart = positionInLine;
        commentType = CommentType.CHAR_LITERAL;
        currentPhrase.append('\'');
    }

    private void handleDoubleQuote() {
        addPendingPhrase();
        currentPhrase.setLength(0);
        phraseStart = positionInLine;
        commentType = CommentType.STRING_LITERAL;
        currentPhrase.append('"');
    }

    private void handleEndOfComment() {
        lastCharWasAsterisk = false;
        phrases.add(new Phrase(phraseStart, currentPhrase.toString(), commentType));
        commentType = CommentType.NONE;
        beginNewPhrase();
    }

    private void handleCharacterInCharacterLiteral(char ch) {
        if (ch == '\'') {
            phrases.add(new Phrase(phraseStart, currentPhrase.toString(), commentType));
            commentType = CommentType.NONE;
            beginNewPhrase();
        }
    }

    private void handleCharacterInStringLiteral(char ch) {
        if (ch == '"') {
            phrases.add(new Phrase(phraseStart, currentPhrase.toString(), commentType));
            commentType = CommentType.NONE;
            beginNewPhrase();
        }
    }

    private void beginNewPhrase() {
        currentPhrase.setLength(0);
        phraseStart = positionInLine + 1;
    }

    private void addPendingPhrase() {
        if (currentPhrase.toString().trim().length() > 0) {
            phrases.add(new Phrase(phraseStart, currentPhrase.toString(), commentType));
        }
    }

    public enum CommentType {
        NONE, SIMPLE_STAR, DOUBLE_STAR, STRING_LITERAL, CHAR_LITERAL, END_OF_LINE
    }

    public static class Phrase {
        public final String text;

        public final int startPos;

        public final CommentType commentType;

        public Phrase(int startPos, String text, CommentType commentType) {
            this.startPos = startPos;
            this.text = text;
            this.commentType = commentType;
        }

        @Override
        public String toString() {
            return "Phrase [text=" + text + ", index=" + startPos + ", commentType=" + commentType + "]";
        }

    }

}
