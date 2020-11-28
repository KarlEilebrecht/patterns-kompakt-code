package de.calamanari.other;

/**
 * Represents a single line of source code found in the output of javadoc. {@link SourceLine}s are subject to reformatting while other lines will be left
 * unchanged.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class SourceLine extends FileLine {

    public final int startPos;

    public final int endPos;

    public final String ancorName;

    public SourceLine(String rawLine) {
        super(rawLine);
        int ancorNameStartPos = rawLine.lastIndexOf("<span id=\"line.") + 10;
        int ancorNameEndPos = rawLine.indexOf('\"', ancorNameStartPos);
        this.ancorName = rawLine.substring(ancorNameStartPos, ancorNameEndPos);
        this.startPos = ancorNameEndPos + 2;
        this.endPos = rawLine.lastIndexOf("</span>");
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