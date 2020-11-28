package de.calamanari.other;

/**
 * Representation of a single text line from a file.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class FileLine implements CharSequence {
    String text;

    public FileLine(String rawLine) {
        if (rawLine == null) {
            throw new IllegalArgumentException("null not allowed");
        }
        this.text = rawLine;
    }

    @Override
    public int length() {
        return text.length();
    }

    @Override
    public char charAt(int index) {
        return text.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return text.subSequence(start, end);
    }

    @Override
    public String toString() {
        return text;
    }

}