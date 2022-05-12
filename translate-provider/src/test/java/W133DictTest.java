import com.zzj.translate.Application;
import com.zzj.translate.model.entity.W133kPos;
import com.zzj.translate.model.service.IW133kMeansService;
import com.zzj.translate.model.service.IW133kPosService;
import com.zzj.translate.model.service.IW133kWordsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class W133DictTest {
    @Autowired
    IW133kMeansService meansService;
    @Autowired
    IW133kWordsService wordsService;
    @Autowired
    IW133kPosService posService;

    @Test
    public void readDictTables() {
        Map<Integer, W133kPos> id2pos = getId2pos();

        System.out.println();

//        List<W133kMeans> meansList = meansService.list();
//        List<W133kWords> wordsList = wordsService.list();
    }

    private Map<Integer, W133kPos> getId2pos() {
        List<W133kPos> posList = posService.list();

        Map<String, String> posName2posMean = posList.stream().filter(k -> k.getMeans() != null).collect(Collectors.toMap(k -> k.getName(), k -> k.getMeans()));
        posName2posMean.put("v.", "动词");
        posName2posMean.put("v..", "动词");
        posName2posMean.put("det.", "限定词");
        posName2posMean.put("prep.", "介词");
        posName2posMean.put("abbr.", "缩写");
        posName2posMean.put("det.", "限定词");
        posName2posMean.put("link-v.", "系动词");
        posName2posMean.put("quant.", "数量");
        posName2posMean.put("pref.", "县|优先|词头|序言");
        posName2posMean.put("a.", "形容词");
        posName2posMean.put("adj..", "形容词");
        posName2posMean.put("na.", "变体");
        posName2posMean.put("aux.", "辅助|助动词");
        posName2posMean.put("suf.", "后缀");
        for (W133kPos pos : posList) {
            if (StringUtils.isNotBlank(pos.getMeans())) continue;

            String name = pos.getName();
            String[] names = name.split("&");
            StringBuilder sb = new StringBuilder();
            for (String s : names) {
                String m = posName2posMean.get(s.trim());
                sb.append(m == null ? s : m).append(" & ");
            }
            sb.setLength(sb.length() - 3);
            pos.setMeans(sb.toString());
        }

        return posList.stream().filter(k -> StringUtils.isNotBlank(k.getName())).collect(Collectors.toMap(k -> k.getId(), k -> k));
    }
}