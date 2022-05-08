import com.baidu.translate.demo.TransApi;

public class Main {

    // 在平台申请的APP_ID 详见 http://api.fanyi.baidu.com/api/trans/product/desktop?req=developer
    private static final String APP_ID = "20220508001206977";
    private static final String SECURITY_KEY = "mTMF7U55lHyV8KW_FpsI";

    public static void main(String[] args) {
        TransApi api = new TransApi(APP_ID, SECURITY_KEY);

        String query = "高度600米";
//        System.out.println(api.getTransResult(query, "auto", "en"));

        String transResult = api.getTransResult("height 600m", "en", "zh");
        String s = unicode2String(transResult);
        System.out.println(s);
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