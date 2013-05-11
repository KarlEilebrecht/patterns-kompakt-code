package de.calamanari.other;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaDocSourceHolder {

    private static final String IMPORT_SEARCH_PATTERN_2 = "^\\s*(import\\s+)?((\\w+\\.)+\\w+)(\\.\\*)?(\\s*;\\s*)?$";

    private final List<FileLine> lines;

    private final int sourceLinesStart;
    private final int sourceLinesEnd;

    public final Path path;

    public final String className;

    public final String simpleClassName;

    public final String[] packagePath;

    public final String[] importPackageNames;

    public JavaDocSourceHolder(File baseDir, File file) throws IOException, UnsupportedEncodingException,
            ClassNotFoundException {
        this.path = file.toPath();
        List<String> rawLines = Files.readAllLines(path, Charset.forName("UTF-8"));

        List<FileLine> lines = new ArrayList<>(rawLines.size());
        int firstSourceLine = -1;
        int afterSourceLine = -1;

        int idx = 0;
        for (String line : rawLines) {
            if (isSourceLine(line)) {
                lines.add(new SourceLine(line));
                if (firstSourceLine == -1) {
                    firstSourceLine = idx;
                }
            }
            else {
                lines.add(new FileLine(line));
                if (firstSourceLine > -1 && afterSourceLine == -1) {
                    afterSourceLine = idx;
                }
            }
            idx++;
        }
        this.lines = Collections.unmodifiableList(lines);
        this.sourceLinesStart = firstSourceLine;
        this.sourceLinesEnd = afterSourceLine;

        String baseName = baseDir.getAbsolutePath();
        String fileName = file.getAbsolutePath();
        fileName = fileName.substring(baseName.length() + 1);
        int pos = fileName.lastIndexOf('.');
        fileName = fileName.substring(0, pos);

        this.className = fileName.replace(File.separatorChar, '.');

        pos = className.lastIndexOf('.');
        String pkgName = null;
        if (pos > -1) {
            this.simpleClassName = className.substring(pos + 1);
            pkgName = className.substring(0, pos);
            this.packagePath = pkgName.split("[\\.]");
        }
        else {
            this.simpleClassName = className;
            this.packagePath = new String[0];
        }
        this.importPackageNames = extractImportPackageNames(pkgName);
    }

    private String[] extractImportPackageNames(String currentPackage) {
        Matcher matcher = Pattern.compile(IMPORT_SEARCH_PATTERN_2).matcher("");
        List<String> pkgList = new ArrayList<>();
        if (currentPackage != null) {
            pkgList.add(currentPackage);
        }
        for (SourceLine line : getSourceLines()) {
            matcher.reset(line.text.substring(line.startPos, line.endPos));
            if (matcher.find()) {
                String pkgName = matcher.group(2);
                if (!pkgName.startsWith("org.") && !pkgName.startsWith("java.")) {
                    pkgList.add(matcher.group(2));
                }
            }
        }
        return pkgList.toArray(new String[pkgList.size()]);
    }

    private boolean isSourceLine(String rawLine) {
        return rawLine.indexOf("<span class=\"sourceLineNo\">") > -1;
    }

    public List<SourceLine> getSourceLines() {
        List<SourceLine> res = Collections.EMPTY_LIST;
        if (sourceLinesStart > -1) {
            int endIdx = sourceLinesEnd;
            if (endIdx == -1) {
                endIdx = lines.size();
            }
            @SuppressWarnings("unchecked")
            List<SourceLine> subLines = (List<SourceLine>) (Object) lines.subList(sourceLinesStart, endIdx);
            res = subLines;
        }
        return res;
    }

    public void storeFile() throws IOException, UnsupportedEncodingException {
        Files.write(path, lines, Charset.forName("UTF-8"));
    }

}