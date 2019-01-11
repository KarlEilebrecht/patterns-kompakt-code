package de.calamanari.other;

public class SourceLine extends FileLine {

    public final int startPos;

    public final int endPos;

    public final String ancorName;

    public SourceLine(String rawLine) {
        super(rawLine);
        this.startPos = rawLine.indexOf("</span>") + 7;
        int ancorNameStartPos = rawLine.lastIndexOf("<a name=\"line.");
        int ancorNameEndPos = rawLine.indexOf('\"', ancorNameStartPos);
        this.ancorName = rawLine.substring(ancorNameStartPos, ancorNameEndPos);
        this.endPos = ancorNameStartPos;
    }

    public int indexOf(char ch) {
        return text.indexOf(ch, startPos);
    }

    public int indexOf(String s) {
        return text.indexOf(s, startPos);
    }

    public int lastIndexOf(char ch) {
        return text.lastIndexOf(ch, startPos);
    }

    public int lastIndexOf(String s) {
        return text.lastIndexOf(s, startPos);
    }

}