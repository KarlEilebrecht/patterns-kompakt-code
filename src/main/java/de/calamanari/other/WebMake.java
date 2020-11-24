package de.calamanari.other;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebMake {

    private static final String PATTERNS = "patterns";

    private static final String YEAR_PLACEHOLDER = "year::";

    private static final String DATE_PLACEHOLDER = "date::";

    private static final String ROOT_PLACEHOLDER = "root::";

    private static final String CONTENT_PLACEHOLDER = "content::";

    private static final String DIRECTORY_PLACEHOLDER = "directory::";

    private static final String DOT_HTML = ".html";

    private static final Logger LOGGER = LoggerFactory.getLogger(WebMake.class);

    private static final Map<Character, String> HTML_ESCAPE = new HashMap<>();
    static {
        HTML_ESCAPE.put('\u00E4', "&auml;");
        HTML_ESCAPE.put('\u00F6', "&ouml;");
        HTML_ESCAPE.put('\u00FC', "&uuml;");
        HTML_ESCAPE.put('\u00C4', "&Auml;");
        HTML_ESCAPE.put('\u00D6', "&Ouml;");
        HTML_ESCAPE.put('\u00DC', "&Uuml;");
        HTML_ESCAPE.put('\u00DF', "&szlig;");
    }

    private static String today = "";
    private static String year = "";
    static {
        SimpleDateFormat sdf = new SimpleDateFormat("d. MMMMM yyyy", Locale.GERMANY);
        today = sdf.format(new Date(System.currentTimeMillis()));
        year = today.substring(today.length() - 4);
    }

    // @formatter:off
    private static final Comparator<RawData> RAW_DATA_SORT_COMPARATOR = (RawData o1, RawData o2) -> String.valueOf(o1.orderId)
                                                                                                        .compareTo(String.valueOf(o2.orderId));
    // @formatter:on

    private int lastImageMapId = 0;

    private File rootPath = null;

    private Map<String, String> javaDocReferenceMap = new HashMap<>();

    private Set<String> excludeFileNames = new HashSet<>(
            Arrays.asList("class-use", "package-frame.html", "package-summary.html", "package-tree.html", "package-use.html"));

    private static final String DEFAULT_INDENT = "    ";

    private String rootContent = "";

    private DirNode root = null;

    private String pkTemplate = "";

    public WebMake(File rootPath) {
        this.rootPath = rootPath;
    }

    private DirNode createDirectoryTree(List<RawData> rawdataList) {

        DirNode rootNode = new DirNode();
        rootNode.categoryFlag = true;
        rootNode.linkRef = "patterns.htm";
        rootNode.rootFlag = true;
        rootNode.name = "Patterns";
        DirNode current = rootNode;
        String lastCategory = "";
        for (RawData data : rawdataList) {
            String currentCategory = data.categoryName;
            if (!lastCategory.equals(currentCategory)) {
                current = new DirNode();
                current.categoryFlag = true;
                current.linkRef = data.categoryTechName + DOT_HTML;
                current.name = currentCategory;
                lastCategory = currentCategory;
                rootNode.subNodes.add(current);
            }
            DirNode leaf = new DirNode();
            leaf.linkRef = data.patternTechName + DOT_HTML;
            leaf.name = data.patternName;
            current.subNodes.add(leaf);
        }
        return rootNode;
    }

    public void run() throws IOException {
        indexJavaDoc();
        List<RawData> rawDataList = this.loadRawFiles();
        for (RawData item : rawDataList) {
            LOGGER.atInfo().addArgument(item::toString).log("{}");
        }
        LOGGER.info("{} items found", rawDataList.size());
        root = createDirectoryTree(rawDataList);
        LOGGER.atInfo().addArgument(() -> root.createNodeString("", false, false, "Strukturmuster", "Verteilung", "Integration", "Persistenz",
                "Datenbankschl&uuml;ssel", "Sonstige Patterns")).log("{}");
        rootContent = loadRootContent();
        LOGGER.info(rootContent);
        pkTemplate = loadPkPattern();
        LOGGER.info(pkTemplate);
        createRootPage();
        createPages(rawDataList);

    }

    private String getRelativeJavaDocPath(File javadocFile) {
        String path = javadocFile.getAbsolutePath().replace('\\', '/');
        int pos = path.indexOf("/de/");
        return "../javadoc" + path.substring(pos);
    }

    private void indexPatternFolder(String patternTechName, File patternFolder) {
        for (File file : patternFolder.listFiles()) {
            String fileName = file.getName();
            if (!excludeFileNames.contains(fileName)) {
                if (file.isDirectory()) {
                    indexPatternFolder(patternTechName, file);
                }
                else {
                    javaDocReferenceMap.put(patternTechName + "/" + fileName.substring(0, fileName.length() - 5), getRelativeJavaDocPath(file));
                }
            }
        }

    }

    private String createLinx(String refString) {
        String[] refTags = refString.split("[,]");
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (String ref : refTags) {
            if (i > 0) {
                sb.append(", ");
            }
            ref = ref.trim();
            String link = "<a href=\"../linx.htm#" + ref + "\">" + ref + "</a>";
            sb.append(link);
            i++;
        }
        return sb.toString();
    }

    private int findImageMapData(String patternTechName, List<String> source, int startIdx, Map<String, String> result) {
        int skipLines = 0;
        for (int i = startIdx; i < source.size(); i++) {
            String line = source.get(i);
            if (line.startsWith("<area shape=\"rect\" coords=\"")) {
                skipLines = processImageMapArea(patternTechName, line, skipLines, result);
            }
            else {
                break;
            }
        }
        return skipLines;
    }

    private int processImageMapArea(String patternTechName, String line, int skipLines, Map<String, String> result) {
        skipLines++;
        int coordEndPos = line.indexOf('\"', 27);
        String rawCoordString = line.substring(27, coordEndPos);
        int refStartPos = line.indexOf("href=\"") + 6;
        int refEndPos = line.indexOf('\"', refStartPos);
        String rawRef = line.substring(refStartPos, refEndPos);
        String[] rawCoords = rawCoordString.split("[,]");
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < 4; j++) {
            if (j > 0) {
                sb.append(",");
            }
            int coord = Integer.parseInt(rawCoords[j]);
            if (j % 2 == 0) {
                coord = coord + 15;
            }
            else {
                coord = coord + 25;
            }
            sb.append("" + coord);
        }
        String key = sb.toString();
        if (rawRef.indexOf("::") < 0) {
            rawRef = String.join("", patternTechName, "/", rawRef);
        }
        else {
            rawRef = rawRef.replace("::", "/");
        }
        String value = javaDocReferenceMap.get(rawRef);
        result.put(key, value);
        return skipLines;
    }

    private String createImageLink(String indent, String rawLink, Map<String, String> imageMap) {

        // img::
        String imageFileName = rawLink.substring(5);
        String usemap = "";
        String mapString = "";
        if (imageMap != null && imageMap.size() > 0) {
            lastImageMapId++;
            int mapId = lastImageMapId;
            usemap = "usemap=\"#IM" + mapId + "\" ";
            StringBuilder sb = new StringBuilder();
            sb.append("\n" + indent + "<MAP NAME=\"IM" + mapId + "\">\n");
            for (Map.Entry<String, String> entry : imageMap.entrySet()) {
                sb.append(indent + DEFAULT_INDENT + "<area shape=\"rect\" coords=" + entry.getKey() + "\" ");
                sb.append("href=\"" + entry.getValue() + "\" target=\"_blank\">\n");
            }
            sb.append("</MAP>");
            mapString = sb.toString();
        }

        return indent + "<p />\n" + indent + "<div style=\"overflow:auto;width:675px;\">\n" + indent + DEFAULT_INDENT + "<img src=\"./images/" + imageFileName
                + "\" " + usemap + "border=\"0\"/>" + mapString + "\n" + indent + "</div>\n" + indent + "<p />";
    }

    private void createRootPage() throws IOException {

        File outputPath = new File(rootPath, PATTERNS);
        File outputFile = new File(outputPath, "patterns.htm");

        String directory = root.createNodeString("", false, true, "Patterns");
        String fileContent = pkTemplate.replace(DIRECTORY_PLACEHOLDER, directory);
        fileContent = fileContent.replace(CONTENT_PLACEHOLDER, rootContent);
        fileContent = fileContent.replace(ROOT_PLACEHOLDER, "");
        fileContent = fileContent.replace(DATE_PLACEHOLDER, today);
        fileContent = fileContent.replace(YEAR_PLACEHOLDER, year);
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), StandardCharsets.ISO_8859_1))) {
            bw.write(fileContent);
        }

    }

    private DirNode findCategoryNode(String categoryName) {
        DirNode res = null;
        for (DirNode node : root.subNodes) {
            if (node.name.equals(categoryName)) {
                res = node;
            }
        }
        return res;
    }

    private RawData findPatternRawData(DirNode patternNode, List<RawData> rawDataList) {
        RawData res = null;
        for (RawData data : rawDataList) {
            if (patternNode.name.equals(data.patternName)) {
                res = data;
            }
        }
        return res;
    }

    private void createCategoryPage(RawData category, List<RawData> rawDataList) throws IOException {
        File outputPath = new File(rootPath, PATTERNS);
        File outputFile = new File(outputPath, category.categoryTechName + DOT_HTML);

        String directory = root.createNodeString("", false, false, category.categoryName);

        StringBuilder content = new StringBuilder("<h2>" + category.categoryName + "</h2>\n");
        content.append("<dl>");
        DirNode categoryNode = findCategoryNode(category.categoryName);
        if (categoryNode != null) {
            for (DirNode node : categoryNode.subNodes) {
                RawData patternRawData = findPatternRawData(node, rawDataList);
                content.append(node.createNodeString(DEFAULT_INDENT, true, false)).append("<dd><i>")
                        .append(patternRawData == null ? "null" : patternRawData.patternDescription).append("</i><p align=\"right\">siehe: ")
                        .append(patternRawData == null ? "null" : createLinx(patternRawData.bookRefs)).append("</p></dd>\n");
            }
        }
        content.append("</dl>\n");
        String fileContent = pkTemplate.replace(DIRECTORY_PLACEHOLDER, directory);
        fileContent = fileContent.replace(CONTENT_PLACEHOLDER, content.toString());
        fileContent = fileContent.replace(ROOT_PLACEHOLDER, "../");
        fileContent = fileContent.replace(DATE_PLACEHOLDER, today);
        fileContent = fileContent.replace(YEAR_PLACEHOLDER, year);

        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), StandardCharsets.ISO_8859_1))) {
            bw.write(fileContent);
        }
    }

    private void createPatternPage(RawData pattern) throws IOException {
        File outputPath = new File(rootPath, PATTERNS);
        File outputFile = new File(outputPath, pattern.patternTechName + DOT_HTML);
        String directory = root.createNodeString("", false, false, pattern.categoryName, pattern.patternName);

        StringBuilder content = new StringBuilder("<h2>" + pattern.patternName + "</h2>\n");
        content.append("<i>" + pattern.patternDescription + "</i><p>siehe: " + createLinx(pattern.bookRefs) + "</p>\n");

        String patternContent = "";

        content.append(patternContent);
        int len = pattern.contentLines.size();
        int lineIndex = 0;
        for (; lineIndex < len; lineIndex++) {
            String line = pattern.contentLines.get(lineIndex);
            if (line.startsWith("img::")) {
                Map<String, String> imageMapData = new HashMap<>();
                int skipLines = findImageMapData(pattern.patternTechName, pattern.contentLines, lineIndex + 1, imageMapData);
                content.append(createImageLink("", line, imageMapData) + "\n");
                lineIndex = lineIndex + skipLines;
            }
            else {
                content.append(line + "\n");
            }
        }

        content.append("\n<h3>Ressourcen</h3><ul><li><a href=\"../patterns.htm#CODE\">Quellcode (Projekt zum Download)</a></li><li><a href=\"")
                .append(javaDocReferenceMap.get(pattern.patternTechName) + "\" target=\"_blank\">JavaDoc (API mit Quelltext online verlinkt)</a></li></ul>\n");

        String fileContent = pkTemplate.replace(DIRECTORY_PLACEHOLDER, directory);
        fileContent = fileContent.replace(CONTENT_PLACEHOLDER, content.toString());
        fileContent = fileContent.replace(ROOT_PLACEHOLDER, "../");
        fileContent = fileContent.replace(DATE_PLACEHOLDER, today);
        fileContent = fileContent.replace(YEAR_PLACEHOLDER, year);

        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), StandardCharsets.ISO_8859_1))) {
            bw.write(fileContent);
        }

    }

    private void createPages(List<RawData> rawDataList) throws IOException {
        Set<String> writtenCategories = new HashSet<>();
        for (RawData data : rawDataList) {
            if (!writtenCategories.contains(data.categoryName)) {
                createCategoryPage(data, rawDataList);
                writtenCategories.add(data.categoryName);
            }
            createPatternPage(data);
        }
    }

    private void indexJavaDoc() {

        javaDocReferenceMap.put("javadoc_start", "../javadoc/index.html");

        File javaDocRootPath = new File(rootPath, "javadoc");
        File javaDocStartPath = new File(javaDocRootPath, "de/calamanari/pk");
        for (File patternFolder : javaDocStartPath.listFiles()) {
            if (patternFolder.isDirectory()) {
                File summary = new File(patternFolder, "package-summary.html");
                String patternTechName = patternFolder.getName();
                javaDocReferenceMap.put(patternTechName, getRelativeJavaDocPath(summary));
                indexPatternFolder(patternTechName, patternFolder);
            }
        }

    }

    private String escape(String source) {
        StringBuilder sb = new StringBuilder(source.length());
        for (int i = 0; i < source.length(); i++) {
            char ch = source.charAt(i);
            if (HTML_ESCAPE.containsKey(ch)) {
                sb.append(HTML_ESCAPE.get(ch));
            }
            else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    private String loadRootContent() throws IOException {
        StringBuilder sb = new StringBuilder();
        File rawRootPath = new File(rootPath, "patterns/raw");
        File rootContentFile = new File(rawRootPath, "zz_root.htm");
        String line = null;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(rootContentFile), StandardCharsets.ISO_8859_1))) {
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.length() > 0) {
                    if (sb.length() > 0) {
                        sb.append("\n");
                    }
                    sb.append(escape(line));
                }
            }
        }
        return sb.toString();
    }

    private String loadPkPattern() throws IOException {
        StringBuilder sb = new StringBuilder();
        File rawRootPath = new File(rootPath, "patterns/templates");
        File rootContentFile = new File(rawRootPath, "patterns-template.html");
        String line = null;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(rootContentFile), StandardCharsets.ISO_8859_1))) {
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (sb.length() > 0) {
                    sb.append("\n");
                }
                sb.append(line);
            }
        }
        return sb.toString();

    }

    private List<RawData> loadRawFiles() throws IOException {
        List<RawData> res = new ArrayList<>();
        File rawRootPath = new File(rootPath, "patterns/raw");
        File[] rawFiles = rawRootPath.listFiles();
        for (File rawFile : rawFiles) {
            if (rawFile.isFile() && rawFile.getName().endsWith(DOT_HTML)) {
                RawData data = new RawData();
                data.patternTechName = rawFile.getName().substring(0, rawFile.getName().length() - 5);
                res.add(data);
                String line = null;
                try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(rawFile), StandardCharsets.ISO_8859_1))) {
                    while ((line = br.readLine()) != null) {
                        processRawLine(br, line, data);
                    }
                }
            }
        }

        Collections.sort(res, RAW_DATA_SORT_COMPARATOR);
        return res;
    }

    private void processRawLine(BufferedReader br, String line, RawData data) throws IOException {
        line = line.trim();
        if (line.length() > 0) {
            if (line.startsWith("ord::")) {
                data.orderId = line.substring(5);
            }
            else {
                String h2 = extractReadAhead(line, br, "<h2>");
                if (h2 != null) {
                    data.patternName = escape(h2);
                }
                else {
                    String h5 = extractReadAhead(line, br, "<h5>");
                    if (h5 != null) {
                        parseCategoryFromRawLine(data, h5);
                    }
                    else {
                        parsePatternDetailsFromRawLine(br, data, line);
                    }
                }
            }

        }
    }

    private void parsePatternDetailsFromRawLine(BufferedReader br, RawData data, String line) throws IOException {
        String h4 = extractReadAhead(line, br, "<h4>");
        if (h4 != null) {
            data.patternDescription = escape(h4);
        }
        else if (line.startsWith("ref::")) {
            data.bookRefs = line.substring(5) + ",PK";
        }
        else {
            data.contentLines.add(escape(line));
        }
    }

    private void parseCategoryFromRawLine(RawData data, String h5) {
        data.categoryName = escape(h5);
        String categoryTechName = data.categoryName.replace(' ', '_').replace("&uuml;", "ue").toLowerCase();
        data.categoryTechName = categoryTechName;
    }

    private String extractReadAhead(String line, BufferedReader br, String tagName) throws IOException {
        String res = null;
        String tagNameClose = "</" + tagName.substring(1);
        if (line.startsWith(tagName)) {
            if (line.endsWith(tagNameClose)) {
                res = line.substring(4, line.length() - 5).trim();
            }
            else {
                StringBuilder sb = new StringBuilder();
                sb.append(line.substring(4).trim());
                readAheadLines(br, tagNameClose, sb);
                res = sb.toString();
            }
        }
        return res;
    }

    private void readAheadLines(BufferedReader br, String tagNameClose, StringBuilder sb) throws IOException {
        String line;
        boolean finished = false;
        while (!finished) {
            line = br.readLine();
            line = line.trim();
            if (line.length() > 0) {
                sb.append(" ");
                if (line.endsWith(tagNameClose)) {
                    sb.append(line.substring(0, line.length() - 5).trim());
                    finished = true;
                }
                else {
                    sb.append(line);
                }
            }
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        WebMake wm = new WebMake(new File(args[0]));
        wm.run();

    }

    private class RawData {
        String orderId = "";

        String patternName = "";

        String patternTechName = "";

        String patternDescription = "";

        String categoryName = "";

        String categoryTechName = "";

        String bookRefs = "";

        List<String> contentLines = new ArrayList<>();

        @Override
        public String toString() {
            return "RawData [orderId=" + orderId + ", patternName=" + patternName + ", patternTechName=" + patternTechName + ", patternDescription="
                    + patternDescription + ", categoryName=" + categoryName + ", categoryTechName=" + categoryTechName + ", bookRefs=" + bookRefs
                    + ", contentLines=" + contentLines + "]";
        }

    }

    private class DirNode {

        String name = "";

        String linkRef = "";

        boolean categoryFlag = false;

        List<DirNode> subNodes = new ArrayList<>();

        boolean rootFlag = false;

        public String createNodeString(String indent, boolean isDt, boolean linkFromRootFolder, String... selectedNodes) {

            List<String> selectedNodesCollection = Arrays.asList(selectedNodes);

            String fullRef = createFullRef(linkFromRootFolder);

            StringBuilder res = new StringBuilder(indent);

            if (isDt) {
                res.append("<dt>");
            }
            else {

                if (!rootFlag && categoryFlag) {
                    res.append("- ");
                }
                else {
                    res.append("<li type=\"");
                    res.append("square\">");
                }
            }
            res.append("<span class=\"text\"><a href=\"" + fullRef + "\">");
            String fullName = name;
            if (selectedNodesCollection.contains(name) || rootFlag) {
                fullName = "<u>" + fullName + "</u>";
            }
            res.append(fullName);
            res.append("</a></span>");

            if (rootFlag) {
                addRootNodeClosed(isDt, res);
            }
            addSubNodeStrings(indent, linkFromRootFolder, selectedNodesCollection, res, selectedNodes);
            if (!rootFlag) {
                addNonRootNodeClosed(isDt, res);
            }

            return res.toString();
        }

        private void addNonRootNodeClosed(boolean isDt, StringBuilder res) {
            if (isDt) {
                res.append("</dt>");
            }
            else {
                res.append("<br />");
            }
        }

        private void addRootNodeClosed(boolean isDt, StringBuilder res) {
            if (isDt) {
                res.append("</dt>");
            }
            else {
                res.append("<br /></li>");
            }
        }

        private void addSubNodeStrings(String indent, boolean linkFromRootFolder, List<String> selectedNodesCollection, StringBuilder res,
                String... selectedNodes) {
            if (rootFlag || selectedNodesCollection.contains(name)) {
                if (rootFlag && !subNodes.isEmpty()) {
                    res.append("\n");
                    for (DirNode node : subNodes) {
                        res.append(node.createNodeString(indent, false, linkFromRootFolder, selectedNodes) + "\n");
                    }
                }
                else if (!subNodes.isEmpty()) {
                    res.append("\n" + indent + DEFAULT_INDENT + "<ul>\n");
                    for (DirNode node : subNodes) {
                        res.append(node.createNodeString(indent + DEFAULT_INDENT + DEFAULT_INDENT, false, linkFromRootFolder, selectedNodes) + "\n");
                    }
                    res.append(indent + DEFAULT_INDENT + "</ul>\n" + indent);

                }
            }
        }

        private String createFullRef(boolean linkFromRootFolder) {
            String fullRef = linkRef;

            if (linkFromRootFolder) {
                if (!rootFlag) {
                    fullRef = "./patterns/" + fullRef;
                }
            }
            else {
                if (rootFlag) {
                    fullRef = "../" + fullRef;
                }
            }
            return fullRef;
        }

    }

}
