import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.csv.CsvData;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvRow;
import cn.hutool.core.text.csv.CsvUtil;
import com.google.common.collect.Lists;
import com.zzj.translate.Application;
import com.zzj.translate.model.entity.EtymaAffix3k;
import com.zzj.translate.model.service.IEtymaAffix3kService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.charset.Charset;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class EtymaAffixTest {
    @Autowired
    IEtymaAffix3kService etymaAffix3kService;

    @Test
    public void importCsv() throws Exception {
        CsvReader reader = CsvUtil.getReader();
        CsvData data = reader.read(FileUtil.file("/Users/xingchuan/Desktop/1.csv"), Charset.forName("gb18030"));

        List<EtymaAffix3k> entityList = Lists.newArrayList();
        int count = 0;
        for (CsvRow csvRow : data.getRows()) {
            count++;
            if (count == 1) continue;

            List<String> raw = csvRow.getRawList();
            EtymaAffix3k entity = EtymaAffix3k.builder().build();
            entity.setId(count);
            entity.setAffix(raw.get(0));
            entity.setMean(raw.get(1));
            entity.setExample(raw.get(2));
            entityList.add(entity);
        }
        etymaAffix3kService.saveBatch(entityList, 1000);
    }
}