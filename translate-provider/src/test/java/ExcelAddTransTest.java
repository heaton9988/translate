import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.zzj.translate.Application;
import com.zzj.translate.model.entity.Stardict;
import com.zzj.translate.model.entity.W133kTrans;
import com.zzj.translate.model.service.IStardictService;
import com.zzj.translate.model.service.IW133kTransService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileOutputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.zzj.translate.util.POIExcelUtil.*;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ExcelAddTransTest {
    @Autowired
    IStardictService stardictService;
    @Autowired
    IW133kTransService transService;

    private static Gson gson = new Gson();

    @Test
    public void addTrans() throws Exception {
        String excelPath = "/Users/xingchuan/Desktop/80天词汇.xlsx";
        ExcelReader reader = ExcelUtil.getReader(excelPath);
        List<List<Object>> rowsRead = reader.read();

        List<List<String>> wordsList = Lists.newArrayList();
        HashSet<String> allWords = Sets.newHashSet();
        for (List<Object> row : rowsRead) {
            List<String> words = row.stream().map(k -> k.toString()).collect(Collectors.toList());
            wordsList.add(words);
            allWords.addAll(words);
        }

        List<Stardict> stardictList = stardictService.lambdaQuery().in(Stardict::getWord, Lists.newArrayList(allWords)).list();
        List<W133kTrans> w133kTransList = transService.lambdaQuery().in(W133kTrans::getWord, Lists.newArrayList(allWords)).list();

        Map<String, Stardict> word2stardict = stardictList.stream().collect(Collectors.toMap(k -> k.getWord(), k -> k));
        Map<String, W133kTrans> word2w133kTrans = w133kTransList.stream().collect(Collectors.toMap(k -> k.getWord(), k -> k));

        List<List<String>> rows = Lists.newArrayList();
        wordsList.forEach((words) -> {
            for (int i = 0; i < words.size(); i++) {
                String word = words.get(i);

                Stardict stardict = word2stardict.get(word);
                W133kTrans w133kTrans = word2w133kTrans.get(word);

                String phonetic = stardict.getPhonetic();

                String transStar = stardict.getTranslation();
                String transW133 = w133kTrans.getMean();
                String trans = StringUtils.isBlank(transW133) ? transStar : transW133;

                String exchangeStar = stardict.getExchange();
                String exchangeW133 = w133kTrans.getExchange();
                String exchange = StringUtils.isBlank(exchangeW133) ? exchangeStar : exchangeW133;

                System.out.println();
            }

        });

        Workbook workbook = createWorkbook();
        Sheet day01 = workbook.createSheet("day01");

        setCellValue(day01, 1, 1, "a");
        setCellValue(day01, 1, 2, "b");
        setCellValue(day01, 2, 1, "c");
        setCellValue(day01, 2, 2, "d");

        FileOutputStream fileOutputStream = new FileOutputStream("/Users/xingchuan/Desktop/Conquer80.xlsx");
        workbook.write(fileOutputStream);
        fileOutputStream.close();
        System.out.println("excel生成完毕");
    }
}