package de.calamanari.other;

import java.util.ArrayList;
import java.util.List;

public class SourceLineInterpreter {

    public CommentType commentType = CommentType.NONE;

    public List<Phrase> phrases = new ArrayList<>();

    public void readNext(SourceLine line) {
        phrases.clear();
        int phraseStart = line.startPos;
        StringBuilder currentPhrase = new StringBuilder();
        boolean lastWasStar = false;
        for (int i = line.startPos; i < line.endPos; i++) {
            char ch = line.charAt(i);
            if (commentType != CommentType.NONE) {
                currentPhrase.append(ch);
                if (commentType == CommentType.STRING_LITERAL) {
                    if (ch == '"') {
                        phrases.add(new Phrase(phraseStart, currentPhrase.toString(), commentType));
                        commentType = CommentType.NONE;
                        currentPhrase.setLength(0);
                        phraseStart = i + 1;
                    }
                }
                else if (commentType == CommentType.CHAR_LITERAL) {
                    if (ch == '\'') {
                        phrases.add(new Phrase(phraseStart, currentPhrase.toString(), commentType));
                        commentType = CommentType.NONE;
                        currentPhrase.setLength(0);
                        phraseStart = i + 1;
                    }
                }
                else if (ch == '*') {
                    lastWasStar = true;
                }
                else if (ch == '/' && lastWasStar) {
                    lastWasStar = false;
                    phrases.add(new Phrase(phraseStart, currentPhrase.toString(), commentType));
                    commentType = CommentType.NONE;
                    currentPhrase.setLength(0);
                    phraseStart = i + 1;
                }
                else {
                    lastWasStar = false;
                }
            }
            else if (ch == '"') {
                if (currentPhrase.toString().trim().length() > 0) {
                    phrases.add(new Phrase(phraseStart, currentPhrase.toString(), commentType));
                }
                currentPhrase.setLength(0);
                phraseStart = i;
                commentType = CommentType.STRING_LITERAL;
                currentPhrase.append(ch);
            }
            else if (ch == '\'') {
                if (currentPhrase.toString().trim().length() > 0) {
                    phrases.add(new Phrase(phraseStart, currentPhrase.toString(), commentType));
                }
                currentPhrase.setLength(0);
                phraseStart = i;
                commentType = CommentType.CHAR_LITERAL;
                currentPhrase.append(ch);
            }
            else if (ch == '/') {
                if (i < line.endPos - 2 && line.charAt(i + 1) == '*' && line.charAt(i + 2) == '*') {
                    if (currentPhrase.toString().trim().length() > 0) {
                        phrases.add(new Phrase(phraseStart, currentPhrase.toString(), commentType));
                    }
                    currentPhrase.setLength(0);
                    phraseStart = i;
                    commentType = CommentType.DOUBLE_STAR;
                    currentPhrase.append(ch);
                    currentPhrase.append(line.charAt(i + 1));
                    currentPhrase.append(line.charAt(i + 2));
                    i = i + 2;
                }
                else if (i < line.endPos - 1 && line.charAt(i + 1) == '*') {
                    if (currentPhrase.toString().trim().length() > 0) {
                        phrases.add(new Phrase(phraseStart, currentPhrase.toString(), commentType));
                    }
                    currentPhrase.setLength(0);
                    phraseStart = i;
                    commentType = CommentType.SIMPLE_STAR;
                    currentPhrase.append(ch);
                    currentPhrase.append(line.charAt(i + 1));
                    i++;
                }
                else if (i < line.endPos - 1 && line.charAt(i + 1) == '/') {
                    if (currentPhrase.toString().trim().length() > 0) {
                        phrases.add(new Phrase(phraseStart, currentPhrase.toString(), commentType));
                    }
                    currentPhrase.setLength(0);
                    currentPhrase.append(line.subSequence(i, line.endPos));
                    phraseStart = i;
                    commentType = CommentType.END_OF_LINE;
                    i = line.endPos;
                }
                else {
                    currentPhrase.append(ch);
                }
            }
            else if (Character.isJavaIdentifierPart(ch) || ch == '@') {
                currentPhrase.append(ch);
            }
            else {
                if (currentPhrase.toString().trim().length() > 0) {
                    phrases.add(new Phrase(phraseStart, currentPhrase.toString(), commentType));
                }
                currentPhrase.setLength(0);
                phraseStart = i + 1;
            }
        }
        if (currentPhrase.toString().trim().length() > 0) {
            phrases.add(new Phrase(phraseStart, currentPhrase.toString(), commentType));
        }
        if (commentType == CommentType.END_OF_LINE || commentType == CommentType.CHAR_LITERAL
                || commentType == CommentType.STRING_LITERAL) {
            commentType = CommentType.NONE;
        }
    }

    public static enum CommentType {
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
