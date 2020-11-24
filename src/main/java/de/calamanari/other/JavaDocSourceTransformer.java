package de.calamanari.other;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.other.SourceLineInterpreter.CommentType;
import de.calamanari.other.SourceLineInterpreter.Phrase;

public class JavaDocSourceTransformer {

    private static final String HTML_END_OF_SPAN = "</span>";

    private static final Logger LOGGER = LoggerFactory.getLogger(JavaDocSourceTransformer.class);

    private static final Set<String> JAVA_KEYWORDS = new HashSet<>(Arrays.asList("abstract", "assert", "boolean", "break", "byte", "case", "catch", "char",
            "class", "const", "continue", "default", "do", "double", "else", "enum", "extends", "final", "finally", "float", "for", "if", "goto", "implements",
            "import", "instanceof", "int", "interface", "long", "native", "new", "package", "private", "protected", "public", "return", "short", "static",
            "strictfp", "super", "switch", "synchronized", "this", "throw", "throws", "transient", "try", "void", "volatile", "while"));

    private final File baseDir;

    private final Map<String, JavaDocSourceHolder> classIndex = new ConcurrentHashMap<>();

    public JavaDocSourceTransformer(File baseDir) {
        this.baseDir = baseDir;

    }

    public void doTransformation() throws IOException {
        Files.walkFileTree(baseDir.toPath(), new FileVisitor<Path>() {

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                JavaDocSourceHolder holder = new JavaDocSourceHolder(baseDir, file.toFile());
                classIndex.put(holder.className, holder);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                return FileVisitResult.TERMINATE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }
        });

        for (JavaDocSourceHolder holder : classIndex.values()) {
            if (!holder.isAlreadyProcessed()) {
                SourceLineInterpreter interpreter = new SourceLineInterpreter();
                LOGGER.info("Processing {} ...", holder.className);
                for (SourceLine line : holder.getSourceLines()) {
                    interpreter.readNext(line);
                    highlightPhrases(holder, line, interpreter.phrases);
                }
                holder.storeFile();
            }
            else {
                LOGGER.info("Skipped {} - already transformed.", holder.className);
            }
        }

    }

    /**
     * 
     * @param line
     * @param phrases
     */
    private void highlightPhrases(JavaDocSourceHolder holder, SourceLine line, List<Phrase> phrases) {
        if (!phrases.isEmpty()) {
            StringBuilder sb = new StringBuilder(line.length());
            int lastPos = 0;
            for (Phrase phrase : phrases) {
                sb.append(line.text.substring(lastPos, phrase.startPos));
                if (phrase.commentType == CommentType.DOUBLE_STAR) {
                    formatDoubleStarComment(sb, phrase);
                }
                else if (phrase.commentType == CommentType.SIMPLE_STAR || phrase.commentType == CommentType.END_OF_LINE) {
                    formatSimpleComment(sb, phrase);
                }
                else if (phrase.commentType == CommentType.CHAR_LITERAL || phrase.commentType == CommentType.STRING_LITERAL) {
                    formatLiteral(sb, phrase);
                }
                else if (JAVA_KEYWORDS.contains(phrase.text)) {
                    formatKeyword(sb, phrase);
                }
                else {
                    formatLink(sb, holder, phrase);
                }
                lastPos = phrase.startPos + phrase.text.length();
            }
            sb.append(line.text.substring(lastPos));
            line.text = sb.toString();
        }
    }

    private void formatLink(StringBuilder sb, JavaDocSourceHolder holder, Phrase phrase) {
        String searchString = phrase.text;
        if (phrase.text.startsWith("@")) {
            sb.append("<span style=\"color: #9B6464;\">");
            searchString = searchString.substring(1);
        }

        String link = findLink(searchString, holder);
        if (link == null) {
            link = findLink(holder.simpleClassName + "." + searchString, holder);
        }
        if (link != null) {
            sb.append("<a href=\"" + link + "\"><u>");
            sb.append(phrase.text);
            sb.append("</u></a>");
        }
        else {
            sb.append(phrase.text);
        }
        if (phrase.text.startsWith("@")) {
            sb.append(HTML_END_OF_SPAN);
        }
    }

    private void formatKeyword(StringBuilder sb, Phrase phrase) {
        sb.append("<span style=\"font-weight : bold; color: #7F0055;\">");
        sb.append(phrase.text);
        sb.append(HTML_END_OF_SPAN);
    }

    private void formatLiteral(StringBuilder sb, Phrase phrase) {
        sb.append("<span style=\"color: #2A00FF;\">");
        sb.append(phrase.text);
        sb.append(HTML_END_OF_SPAN);
    }

    private void formatSimpleComment(StringBuilder sb, Phrase phrase) {
        sb.append("<span style=\"color: #3F7F5F;\">");
        sb.append(phrase.text);
        sb.append(HTML_END_OF_SPAN);
    }

    private void formatDoubleStarComment(StringBuilder sb, Phrase phrase) {
        sb.append("<span style=\"color: #3F5FBF;\">");
        sb.append(phrase.text);
        sb.append(HTML_END_OF_SPAN);
    }

    private String findLink(String classNameCandidate, JavaDocSourceHolder holder) {
        String suffix = "." + classNameCandidate;
        for (String packageName : holder.importPackageNames) {
            String candidate = packageName;
            if (!candidate.endsWith(suffix)) {
                candidate = candidate + suffix;
            }

            JavaDocSourceHolder destHolder = classIndex.get(candidate);
            if (destHolder != null) {
                return createHtmlRefFrom(holder.packagePath, destHolder.packagePath, classNameCandidate);
            }
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        File baseDir = new File(args[0]);
        JavaDocSourceTransformer transformer = new JavaDocSourceTransformer(baseDir);
        transformer.doTransformation();
    }

    public static String createHtmlRefFrom(String[] fromPackagePath, String[] toPackagePath, String toSimpleClassName) {
        int toBeReduced = fromPackagePath.length;
        int toBeBuilt = toPackagePath.length;
        int toBeCompared = Math.min(toBeReduced, toBeBuilt);
        for (int i = 0; i < toBeCompared; i++) {
            if (toPackagePath[i].equals(fromPackagePath[i])) {
                toBeReduced--;
                toBeBuilt--;
            }
            else {
                break;
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < toBeReduced; i++) {
            sb.append("../");
        }
        for (int i = (toPackagePath.length - toBeBuilt); i < toPackagePath.length; i++) {
            if (sb.length() == 0) {
                sb.append("./");
            }
            sb.append(toPackagePath[i]);
            sb.append("/");
        }
        sb.append(toSimpleClassName);
        sb.append(".html");
        return sb.toString();
    }

}
