import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.zzj.translate.Application;
import com.zzj.translate.model.entity.W133kMeans;
import com.zzj.translate.model.entity.W133kPos;
import com.zzj.translate.model.entity.W133kWords;
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

    private Gson gson = new Gson();

    @Test
    public void readDictTables() {
        Map<Integer, String> wordId2fullMean = getWordId2fullMean();

        Map<String, String> type2name = Maps.newHashMap();
        type2name.put("word_ing", "进行时");
        type2name.put("word_third", "第三人称");
        type2name.put("word_pl", "第三人称");
        type2name.put("word_done", "完成时");
        type2name.put("word_past", "过去时");
        type2name.put("word_er", "比较级");
        type2name.put("word_est", "最高级");

        Map<String, String> wordName2trans = Maps.newTreeMap();
        List<W133kWords> wordsList = wordsService.list();
        for (W133kWords word : wordsList) {
            Integer wordId = word.getId();
            String fullMean = wordId2fullMean.get(wordId);
            if (StringUtils.isBlank(fullMean)) continue;
            StringBuilder trans = new StringBuilder(fullMean);

            String wordName = word.getWord();
            String exchange = word.getExchange();
            if (StringUtils.isBlank(exchange)) continue;

            trans.append("\n");
            Map<String, Object> exchangeMap = gson.fromJson(exchange, Map.class);
            for (Map.Entry<String, Object> entry : exchangeMap.entrySet()) {
                String type = entry.getKey();
                Object wordList = entry.getValue();
                if (wordList == null || StringUtils.isBlank(wordList.toString())) continue;

//                String typeName = type2name.get(type);
                String typeName = type.replaceAll("word_", "");
                trans.append(typeName).append(":").append(wordList).append(", ");
                if (!(wordList instanceof String) && StringUtils.isBlank(typeName)) {
//                    System.out.println();
                }
            }

            wordName2trans.put(wordName, trans.toString());
        }
        System.out.println();
    }

    private Map<Integer, String> getWordId2fullMean() {
        Map<Integer, String> wordId2fullMean = Maps.newTreeMap();

        Map<Integer, W133kPos> id2pos = getId2pos();

        List<W133kMeans> meansList = meansService.list();
        Map<Integer, List<W133kMeans>> wordId2meanList = meansList.stream().collect(Collectors.groupingBy(k -> k.getWordId()));
        wordId2meanList.forEach((wordId, means) -> {
            StringBuilder sb = new StringBuilder();
            for (W133kMeans mean : means) {
                Integer posId = mean.getPosId();
                String meanDesc = mean.getMeans();

                W133kPos pos = id2pos.get(posId);
                if (pos == null) continue;

                sb.append(pos.getName()).append(pos.getMeans()).append(": ").append(meanDesc).append("\n");
            }
            if (StringUtils.isBlank(sb.toString())) return;

            sb.setLength(sb.length() - 1);
            wordId2fullMean.put(wordId, sb.toString());
        });
        return wordId2fullMean;
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