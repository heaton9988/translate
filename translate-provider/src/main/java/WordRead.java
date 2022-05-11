import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class WordRead {
    private static String fileDir = "/Users/xingchuan/Desktop/";
    private static String filePathPrefix = fileDir + "Blue Blood True Blood";
    private static String filePathOld = filePathPrefix + ".docx";
    private static String filePathNew = filePathPrefix + "_" + System.currentTimeMillis() + ".docx";
    private static String filePathCacheBaiduTxt = filePathPrefix + "_baidu.txt";

    private static String txtSeperator = "==========";

    private static Gson gson = new Gson();

    public static void main(String[] args) throws Exception {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8610; i++) {
            sb.append("a");
        }
        System.out.println(sb.length());
        System.out.println(new Date() + "\t" + sb.toString());
        String zh = TransUtil.getZhByEn(sb.toString());
        System.out.println(new Date() + "\t" + zh);
    }

    public static void translateWord() throws Exception {
        Map<String, String> en2zh = collectTransTxt();
        generateWordByTxt(en2zh);
    }

    private static Map<String, String> readTransTxt(String path) throws Exception {
        Map<String, String> retMap = Maps.newLinkedHashMap();
        List<String> lines = Files.readLines(new File(path), StandardCharsets.UTF_8);
        for (String line : lines) {
            if (StringUtils.isBlank(line)) continue;
            String[] ss = line.split(txtSeperator);
            if (ss.length != 2) throw new RuntimeException("split error: line=" + line);
            retMap.put(ss[0], ss[1]);
        }
        return retMap;
    }

    private static Map<String, String> collectTransTxt() throws Exception {
        Map<String, String> en2zh = readTransTxt(filePathCacheBaiduTxt);

        FileWriter fw = new FileWriter(filePathCacheBaiduTxt, true);
        BufferedWriter bw = new BufferedWriter(fw);
        XWPFDocument document = new XWPFDocument(new FileInputStream(filePathOld));

        List<XWPFParagraph> paras = document.getParagraphs();
        for (XWPFParagraph para : paras) {
            String text = para.getText();
            text = text.replaceAll("\n", "").replaceAll("\r", "");

            if (StringUtils.isBlank(text)) continue;

            if (en2zh.containsKey(text)) {
                continue;
            }

            String zh = TransUtil.getZhByEn(text);
            String enSeperateZh = text + txtSeperator + zh;

            bw.newLine();
            bw.write(enSeperateZh);
            bw.flush();

            en2zh.put(text, zh);
        }
        bw.close();

        return en2zh;
    }

    private static void generateWordByTxt(Map<String, String> en2zh) throws Exception {
        XWPFDocument document = new XWPFDocument(new FileInputStream(filePathOld));

        List<XWPFParagraph> paras = document.getParagraphs();
        for (XWPFParagraph para : paras) {
            String text = para.getText();
            text = text.replaceAll("\n", "").replaceAll("\r", "");

            if (StringUtils.isBlank(text)) continue;

            String zh = en2zh.get(text);
            if (StringUtils.isBlank(zh)) continue;

            XWPFRun run = para.createRun();
            run.setText("[百度译文: " + zh + "]");
            para.addRun(run);
        }

        writeDocToFile(document, filePathNew);
    }


    private static void writeDocToFile(XWPFDocument document, String filePath) throws IOException {
        FileOutputStream fos = new FileOutputStream(filePath);
        document.write(fos);
        document.close();
    }
}