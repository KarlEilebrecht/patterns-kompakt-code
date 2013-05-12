package de.calamanari.other;

public class FileLine implements CharSequence {
    public String text;//

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
        return text.toString();
    }

}