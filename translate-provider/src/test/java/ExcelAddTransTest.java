import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.zzj.translate.Application;
import com.zzj.translate.model.entity.Stardict;
import com.zzj.translate.model.entity.W133kTrans;
import com.zzj.translate.model.service.IStardictService;
import com.zzj.translate.model.service.IW133kTransService;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.zzj.translate.util.POIExcelUtil.createWorkbook;
import static com.zzj.translate.util.POIExcelUtil.setCellValue;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ExcelAddTransTest {
    @Autowired
    IStardictService stardictService;
    @Autowired
    IW133kTransService transService;

    private static Gson gson = new Gson();

    private List<List<String>> getWordsList() {
        String excelPath = "/Users/xingchuan/Desktop/攻克80.xlsx";
        ExcelReader reader = ExcelUtil.getReader(excelPath);
        List<List<Object>> rowsRead = reader.read();

        List<List<String>> wordsList = Lists.newArrayList();
        for (List<Object> row : rowsRead) {
            List<String> words = row.stream().map(k -> k.toString().trim()).collect(Collectors.toList());
            wordsList.add(words);
        }
        return wordsList;
    }

    @Test
    public void addTrans() throws Exception {
        List<List<String>> wordsList = getWordsList();


        Map<String, WordCard> word2card = getWord2card(wordsList);

        writeAnkiTxt(word2card);

//        writeExcel(wordsList, word2card);
    }

    private void writeAnkiTxt(Map<String, WordCard> word2card) throws Exception {
        BufferedWriter bw = Files.newWriter(new File("/Users/xingchuan/Desktop/anki_Conquer80.txt"), StandardCharsets.UTF_8);
        word2card.forEach((word, card) -> {
            try {
                bw.write(card.getAnki());
                bw.newLine();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        bw.close();
    }

    private Map<String, WordCard> getWord2card(List<List<String>> wordsList) {
        Map<String, List<String>> word2synonymList = Maps.newHashMap();
        for (List<String> words : wordsList) {
            for (String word : words) word2synonymList.put(word, words);
        }
        Set<String> allWords = word2synonymList.keySet();

        List<Stardict> stardictList = stardictService.lambdaQuery().in(Stardict::getWord, Lists.newArrayList(allWords)).list();
        List<W133kTrans> w133kTransList = transService.lambdaQuery().in(W133kTrans::getWord, Lists.newArrayList(allWords)).list();

        Map<String, Stardict> word2stardict = stardictList.stream().collect(Collectors.toMap(k -> k.getWord(), k -> k));
        Map<String, W133kTrans> word2w133kTrans = w133kTransList.stream().collect(Collectors.toMap(k -> k.getWord(), k -> k));

        Map<String, WordCard> word2card = Maps.newHashMap();
        allWords.forEach((word) -> {
            Stardict stardict = word2stardict.get(word);
            W133kTrans w133kTrans = word2w133kTrans.get(word);

            String phonetic = stardict.getPhonetic();

            String transStar = stardict.getTranslation();
            String transW133 = w133kTrans == null ? "" : w133kTrans.getMean();
            String trans = StringUtils.isBlank(transW133) ? transStar : transW133;

            String exchangeStar = stardict.getExchange();
            String exchangeW133 = w133kTrans == null ? "" : w133kTrans.getExchange();
            String exchange = StringUtils.isBlank(exchangeW133) ? exchangeStar : exchangeW133;

            List<String> synonymList = word2synonymList.get(word);
            WordCard card = WordCard.builder()
                    .word(word)
                    .phonetic(phonetic)
                    .trans(trans + "\n" + exchange)
                    .synonym(synonymList.toString())
                    .build();
            word2card.put(word, card);
        });
        return word2card;
    }

    private void writeExcel(List<List<String>> wordsList, Map<String, WordCard> word2card) throws Exception {
        List<List<String>> rows = Lists.newArrayList();
        wordsList.forEach((words) -> {
            for (int colIdex = 0; colIdex < words.size(); colIdex++) {
                String word = words.get(colIdex);
                WordCard wordCard = word2card.get(word);
                rows.add(wordCard.getExcelRow());
            }
            rows.add(Lists.newArrayList());
        });

        Workbook workbook = createWorkbook();
        Sheet day01 = workbook.createSheet("day01");
        for (int i = 0; i < rows.size(); i++) {
            List<String> row = rows.get(i);
            for (int j = 0; j < row.size(); j++) setCellValue(day01, i + 1, j + 1, row.get(j));
        }

        FileOutputStream fileOutputStream = new FileOutputStream("/Users/xingchuan/Desktop/Conquer80.xlsx");
        workbook.write(fileOutputStream);
        fileOutputStream.close();
    }

    @Builder
    @Data
    static class WordCard {
        private String word;
        private String phonetic;
        private String trans;
        private String synonym;

        public List<String> getExcelRow() {
            return Lists.newArrayList(getWord(), getPhonetic(), getTrans());
        }

        public String getAnki() {
            return word + "&nbsp;&nbsp;&nbsp;&nbsp;" + phonetic.replaceAll("\n", "<br>")
                    + "\t" +
                    trans.replaceAll("\n", "<br>")
                    + "<br>"
                    + "同义词: " + synonym;
        }
    }
}