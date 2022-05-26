package docTrans;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.zzj.translate.util.TransUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class TransDocTest {
    private static String fileDir = "/Users/xingchuan/Desktop/";
    private static String filePathPrefix = fileDir + "The White Owl Legends";
    private static String filePathOld = filePathPrefix + ".docx";
    private static String filePathNew = filePathPrefix + "_" + System.currentTimeMillis() + ".docx";
    private static String filePathCacheBaiduTxt = filePathPrefix + "_baidu.txt";

    private static String txtSeperator = "==========";
    private static String txtSeperatorRegex = "={5,}";

    private static Gson gson = new Gson();

    @Test
    public void translateWord() throws Exception {
        Map<String, String> en2zh = collectTransTxt();
        generateWordByTxt(en2zh);
    }

    private static Map<String, String> readTransTxt(String path) throws Exception {
        Map<String, String> retMap = Maps.newLinkedHashMap();
        File txtFile = new File(path);
        if (!txtFile.exists()) {
            txtFile.createNewFile();
        }
        List<String> lines = Files.readLines(txtFile, StandardCharsets.UTF_8);
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

        XWPFDocument document = new XWPFDocument(new FileInputStream(filePathOld));

        List<String> textList = Lists.newArrayList();
        for (XWPFParagraph para : document.getParagraphs()) {
            String text = para.getText();
            text = text.replaceAll("\n", "").replaceAll("\r", "");

            if (StringUtils.isBlank(text)) continue;
            if (en2zh.containsKey(text)) continue;

            textList.add(text);
        }

        FileWriter fw = new FileWriter(filePathCacheBaiduTxt, true);
        BufferedWriter bw = new BufferedWriter(fw);

        int succ = 0, fail = 0, ex = 0;
        StringBuilder batchTexts = new StringBuilder();
        for (int i = 0; i < textList.size(); i++) {
            String text = textList.get(i);
            try {
                batchTexts.append(text).append(txtSeperator);

                if (batchTexts.length() > 500 || i == textList.size() - 1) {
                    String strBatchTexts = batchTexts.toString();
                    batchTexts.setLength(0);
                    String batchChineses = TransUtil.getZhByEn(strBatchTexts);
                    String[] ssChinese = batchChineses.split(txtSeperatorRegex);
                    String[] ssEnglish = strBatchTexts.split(txtSeperatorRegex);
                    if (ssChinese.length != ssEnglish.length) {
                        fail++;
                        for (String en : ssEnglish) {
                            String zh = TransUtil.getZhByEn(en);

                            String enSeperateZh = en + txtSeperator + zh;

                            bw.newLine();
                            bw.write(enSeperateZh);
                            bw.flush();

                            en2zh.put(en, zh);
                        }
                    } else {
                        succ++;
                        for (int j = 0; j < ssEnglish.length; j++) {
                            bw.newLine();
                            bw.write(ssEnglish[j] + txtSeperator + ssChinese[j]);
                            bw.flush();

                            en2zh.put(ssEnglish[j], ssChinese[j]);
                        }
                    }
                    System.out.println("ex=" + ex + "\tsucc=" + succ + "\tfail=" + fail);
                }
            } catch (Exception e) {
                e.printStackTrace();
                ex++;
            }
        }
        System.out.println("ex=" + ex + "\tsucc=" + succ + "\tfail=" + fail);


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