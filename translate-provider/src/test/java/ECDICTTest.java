import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.csv.CsvData;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvRow;
import cn.hutool.core.text.csv.CsvUtil;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.zzj.translate.Application;
import com.zzj.translate.model.entity.Stardict;
import com.zzj.translate.model.service.IStardictService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ECDICTTest {
    @Autowired
    IStardictService stardictService;

    private static Gson gson = new Gson();

    @Test
    public void importStarDict() throws Exception {
        CsvReader reader = CsvUtil.getReader();
        CsvData data = reader.read(FileUtil.file("/Users/xingchuan/Downloads/dict收集/ECDICT-master/词典文件/stardict.csv"));

        Collection<Stardict> entityList = Lists.newArrayList();
        int count = 0;
        for (CsvRow csvRow : data.getRows()) {
            count++;
            if (count == 1) {
                continue;
            }

            List<String> raw = csvRow.getRawList();
            Stardict entity = new Stardict();

            entity.setWord(raw.get(0));
            entity.setPhonetic(raw.get(1));
            entity.setDefinition(raw.get(2));
            entity.setTranslation(raw.get(3));
            entity.setPos(raw.get(4));
            entity.setCollins(raw.get(5));
            entity.setOxford(raw.get(6));
            entity.setTag(raw.get(7));
            entity.setBnc(raw.get(8));
            entity.setFrq(raw.get(9));
            entity.setExchange(raw.get(10));
            entity.setDetail(raw.get(11));
            entity.setAudio(raw.get(12));

            entityList.add(entity);

            if (entityList.size() >= 10000) {
                boolean b = stardictService.saveBatch(entityList, 10000);
                System.out.println(new Date() + "\t" + count + "\tb=" + b);
                entityList.clear();
            }
        }
        if (entityList.size() > 0) {
            boolean b = stardictService.saveBatch(entityList, 10000);
            System.out.println(new Date() + "\t" + count + "\tb=" + b);
            entityList.clear();
        }
    }
}