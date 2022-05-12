import com.zzj.translate.baidu.api.document.TransApi;
import com.google.gson.Gson;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class TransUtil {

    // 在平台申请的APP_ID 详见 http://api.fanyi.baidu.com/api/trans/product/desktop?req=developer
    private static final String APP_ID = "20220508001206977";
    private static final String SECURITY_KEY = "mTMF7U55lHyV8KW_FpsI";

    private static TransApi api = new TransApi(APP_ID, SECURITY_KEY);
    private static Gson gson = new Gson();
    private static long lastInvokeTimestamp = 0L;

    public static String getZhByEn(String en) {
        String transResult = invoke(en, "en", "zh");
        return getDstByResult(transResult);
    }

    private static String invoke(String query, String from, String to) {
        wait1s();
        String result = api.getTransResult(query, from, to);
        try {
            if (result.contains("\"error_code\":\"54003\"")) {
                Thread.sleep(1000L);
                result = api.getTransResult(query, from, to);
            }
        } catch (Exception e) {
        }
        System.out.println(new Date() + "\t" + query + "\t" + unicode2String(result));
        return result;
    }

    private static void wait1s() {
        try {
            long diff = System.currentTimeMillis() - lastInvokeTimestamp;
            if (diff < 1000L) Thread.sleep(1010 - diff);
            lastInvokeTimestamp = System.currentTimeMillis();
        } catch (Exception e) {
        }
    }

    private static String getDstByResult(String transResult) {
        Map map = gson.fromJson(transResult, Map.class);

        List resList = (List) map.get("trans_result");
        if (CollectionUtils.isNotEmpty(resList)) {
            Map map2 = (Map) resList.get(0);
            String dst = (String) map2.get("dst");
            if (StringUtils.isNotBlank(dst)) {
                return dst;
            }
        }
        return "发生异常";
    }

    private static String unicode2String(String unicode) {
        if (unicode == null || unicode.trim().equals("")) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        int i = -1;
        int pos = 0;

        while ((i = unicode.indexOf("\\u", pos)) != -1) {
            sb.append(unicode.substring(pos, i));
            if (i + 5 < unicode.length()) {
                pos = i + 6;
                sb.append((char) Integer.parseInt(unicode.substring(i + 2, i + 6), 16));
            }
        }
        //如果pos位置后，有非中文字符，直接添加
        sb.append(unicode.substring(pos));
        return sb.toString();
    }
}