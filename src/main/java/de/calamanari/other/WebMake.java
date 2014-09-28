package de.calamanari.other;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class WebMake {

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

    private int lastImageMapId = 0;

    private File rootPath = null;

    private Map<String, String> javaDocReferenceMap = new HashMap<>();

    private Set<String> excludeFileNames = new HashSet<>(Arrays.asList(new String[] { "class-use", "package-frame.html", "package-summary.html",
            "package-tree.html", "package-use.html" }));

    private static final String DEFAULT_INDENT = "    ";

    private String rootContent = "";

    private DirNode root = null;

    private String pkTemplate = "";

    public WebMake(File rootPath) throws Exception {
        this.rootPath = rootPath;
    }

    private DirNode createDirectoryTree(List<RawData> rawdataList) {

        DirNode root = new DirNode();
        root.categoryFlag = true;
        root.linkRef = "patterns.htm";
        root.rootFlag = true;
        root.name = "Patterns";
        DirNode current = root;
        String lastCategory = "";
        for (RawData data : rawdataList) {
            String currentCategory = data.categoryName;
            if (!lastCategory.equals(currentCategory)) {
                current = new DirNode();
                current.categoryFlag = true;
                current.linkRef = data.categoryTechName + ".html";
                current.name = currentCategory;
                lastCategory = currentCategory;
                root.subNodes.add(current);
            }
            DirNode leaf = new DirNode();
            leaf.linkRef = data.patternTechName + ".html";
            leaf.name = data.patternName;
            current.subNodes.add(leaf);
        }
        return root;
    }

    public void run() throws Exception {
        indexJavaDoc();
        // System.out.println(javaDocReferenceMap.toString());
        List<RawData> rawDataList = this.loadRawFiles();
        for (RawData item : rawDataList) {
            System.out.println(item);
        }
        System.out.println("" + rawDataList.size() + " items found");
        root = createDirectoryTree(rawDataList);
        System.out.println(root.createString("", false, false, "Strukturmuster", "Verteilung", "Integration", "Persistenz", "Datenbankschl&uuml;ssel",
                "Sonstige Patterns"));
        rootContent = loadRootContent();
        System.out.println(rootContent);
        pkTemplate = loadPkPattern();
        System.out.println(pkTemplate);
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
                    rawRef = patternTechName + "/" + rawRef;
                }
                else {
                    rawRef = rawRef.replace("::", "/");
                }
                String value = javaDocReferenceMap.get(rawRef);
                result.put(key, value);
            }
            else {
                break;
            }
        }
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

        String link = indent + "<p />\n" + indent + "<div style=\"overflow:auto;width:675px;\">\n" + indent + DEFAULT_INDENT + "<img src=\"./images/"
                + imageFileName + "\" " + usemap + "border=\"0\"/>" + mapString + "\n" + indent + "</div>\n" + indent + "<p />";
        return link;
    }

    private void createRootPage() throws Exception {

        File outputPath = new File(rootPath, "patterns");
        File outputFile = new File(outputPath, "patterns.htm");

        String directory = root.createString("", false, true, "Patterns");
        String fileContent = pkTemplate.replace("directory::", directory);
        fileContent = fileContent.replace("content::", rootContent);
        fileContent = fileContent.replace("root::", "");
        fileContent = fileContent.replace("date::", today);
        fileContent = fileContent.replace("year::", year);
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "ISO-8859-1"))) {
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

    private void createCategoryPage(RawData category, List<RawData> rawDataList) throws Exception {
        File outputPath = new File(rootPath, "patterns");
        File outputFile = new File(outputPath, category.categoryTechName + ".html");

        String directory = root.createString("", false, false, category.categoryName);

        String content = "<h2>" + category.categoryName + "</h2>\n";
        content = content + "<dl>";
        DirNode categoryNode = findCategoryNode(category.categoryName);
        for (DirNode node : categoryNode.subNodes) {
            RawData patternRawData = findPatternRawData(node, rawDataList);
            content = content + node.createString(DEFAULT_INDENT, true, false) + "<dd><i>" + patternRawData.patternDescription
                    + "</i><p align=\"right\">siehe: " + createLinx(patternRawData.bookRefs) + "</p></dd>\n";
        }
        content = content + "</dl>\n";
        String fileContent = pkTemplate.replace("directory::", directory);
        fileContent = fileContent.replace("content::", content);
        fileContent = fileContent.replace("root::", "../");
        fileContent = fileContent.replace("date::", today);
        fileContent = fileContent.replace("year::", year);

        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "ISO-8859-1"))) {
            bw.write(fileContent);
        }
    }

    private void createPatternPage(RawData pattern) throws Exception {
        File outputPath = new File(rootPath, "patterns");
        File outputFile = new File(outputPath, pattern.patternTechName + ".html");
        String directory = root.createString("", false, false, pattern.categoryName, pattern.patternName);

        String content = "<h2>" + pattern.patternName + "</h2>\n";
        content = content + "<i>" + pattern.patternDescription + "</i><p>siehe: " + createLinx(pattern.bookRefs) + "</p>\n";

        String patternContent = "";

        content = content + patternContent;
        int len = pattern.contentLines.size();
        for (int i = 0; i < len; i++) {
            String line = pattern.contentLines.get(i);
            if (line.startsWith("img::")) {
                Map<String, String> imageMapData = new HashMap<>();
                int skipLines = findImageMapData(pattern.patternTechName, pattern.contentLines, i + 1, imageMapData);
                content = content + createImageLink("", line, imageMapData) + "\n";
                i = i + skipLines;
            }
            else {
                content = content + line + "\n";
            }
        }

        content = content + "\n<h3>Ressourcen</h3><ul><li><a href=\"../patterns.htm#CODE\">Quellcode</a></li><li><a href=\""
                + javaDocReferenceMap.get(pattern.patternTechName) + "\" target=\"_blank\">JavaDoc</a></li></ul>\n";

        String fileContent = pkTemplate.replace("directory::", directory);
        fileContent = fileContent.replace("content::", content);
        fileContent = fileContent.replace("root::", "../");
        fileContent = fileContent.replace("date::", today);
        fileContent = fileContent.replace("year::", year);

        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "ISO-8859-1"))) {
            bw.write(fileContent);
        }

    }

    private void createPages(List<RawData> rawDataList) throws Exception {
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

    private String loadRootContent() throws Exception {
        StringBuilder sb = new StringBuilder();
        File rawRootPath = new File(rootPath, "patterns/raw");
        File rootContentFile = new File(rawRootPath, "zz_root.htm");
        String line = null;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(rootContentFile), "ISO-8859-1"))) {
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

    private String loadPkPattern() throws Exception {
        StringBuilder sb = new StringBuilder();
        File rawRootPath = new File(rootPath, "patterns/templates");
        File rootContentFile = new File(rawRootPath, "patterns-template.html");
        String line = null;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(rootContentFile), "ISO-8859-1"))) {
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

    private List<RawData> loadRawFiles() throws Exception {
        List<RawData> res = new ArrayList<>();
        File rawRootPath = new File(rootPath, "patterns/raw");
        File[] rawFiles = rawRootPath.listFiles();
        for (File rawFile : rawFiles) {
            if (rawFile.isFile() && rawFile.getName().endsWith(".html")) {
                RawData data = new RawData();
                data.patternTechName = rawFile.getName().substring(0, rawFile.getName().length() - 5);
                res.add(data);
                String line = null;
                try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(rawFile), "ISO-8859-1"))) {
                    while ((line = br.readLine()) != null) {
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
                                        data.categoryName = escape(h5);
                                        String categoryTechName = data.categoryName.replace(' ', '_').replace("&uuml;", "ue").toLowerCase();
                                        data.categoryTechName = categoryTechName;
                                    }
                                    else {
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
                                }
                            }

                        }
                    }
                }
            }
        }

        Collections.sort(res);
        return res;
    }

    private String extractReadAhead(String line, BufferedReader br, String tagName) throws Exception {
        String res = null;
        String tagNameClose = "</" + tagName.substring(1);
        if (line.startsWith(tagName)) {
            if (line.endsWith(tagNameClose)) {
                res = line.substring(4, line.length() - 5).trim();
            }
            else {
                StringBuilder sb = new StringBuilder();
                sb.append(line.substring(4).trim());
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
                res = sb.toString();
            }
        }
        return res;
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        WebMake wm = new WebMake(new File(args[0]));
        wm.run();

    }

    private class RawData implements Comparable<RawData> {
        public String orderId = "";

        public String patternName = "";

        public String patternTechName = "";

        public String patternDescription = "";

        public String categoryName = "";

        public String categoryTechName = "";

        public String bookRefs = "";

        public List<String> contentLines = new ArrayList<>();

        @Override
        public int compareTo(RawData o) {
            return this.orderId.compareTo(o.orderId);
        }

        @Override
        public String toString() {
            return "RawData [orderId=" + orderId + ", patternName=" + patternName + ", patternTechName=" + patternTechName + ", patternDescription="
                    + patternDescription + ", categoryName=" + categoryName + ", categoryTechName=" + categoryTechName + ", bookRefs=" + bookRefs
                    + ", contentLines=" + contentLines + "]";
        }

    }

    private class DirNode {

        public String name = "";

        public String linkRef = "";

        public boolean categoryFlag = false;

        public List<DirNode> subNodes = new ArrayList<>();

        public boolean rootFlag = false;

        public String createString(String indent, boolean isDt, boolean linkFromRootFolder, String... selectedNodes) {

            List<String> selectedNodesCollection = Arrays.asList(selectedNodes);

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

            String res = indent;

            if (isDt) {
                res = res + "<dt>";
            }
            else {

                if (!rootFlag && categoryFlag) {
                    res = res + "- ";
                    // res = res + "<li type=\"";
                    // res = res + "circle\">";
                }
                else {
                    res = res + "<li type=\"";
                    res = res + "square\">";
                }
            }
            res = res + "<span class=\"text\"><a href=\"" + fullRef + "\">";
            String fullName = name;
            if (selectedNodesCollection.contains(name) || rootFlag) {
                fullName = "<u>" + fullName + "</u>";
            }
            // if (!rootFlag && categoryFlag) {
            // fullName = "<i>" + fullName + "</i>";
            // }
            res = res + fullName;
            res = res + "</a></span>";

            if (rootFlag) {
                if (isDt) {
                    res = res + "</dt>";
                }
                else {
                    res = res + "<br /></li>";
                }
            }
            if (rootFlag || selectedNodesCollection.contains(name)) {
                if (rootFlag && subNodes.size() > 0) {
                    res = res + "\n";
                    for (DirNode node : subNodes) {
                        res = res + node.createString(indent, false, linkFromRootFolder, selectedNodes) + "\n";
                    }
                }
                else if (subNodes.size() > 0) {
                    res = res + "\n" + indent + DEFAULT_INDENT + "<ul>\n";
                    for (DirNode node : subNodes) {
                        res = res + node.createString(indent + DEFAULT_INDENT + DEFAULT_INDENT, false, linkFromRootFolder, selectedNodes) + "\n";
                    }
                    res = res + indent + DEFAULT_INDENT + "</ul>\n" + indent;

                }
            }
            if (!rootFlag) {
                if (isDt) {
                    res = res + "</dt>";
                }
                else {
                    res = res + "<br />";
                    // res = res + "</li>";
                }
            }

            return res;
        }

    }

}
