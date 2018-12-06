import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlSelector {

    public static Set<String> getUrls(String html) {
        Set<String> set = new HashSet<>();
        String regex = "<a target=_blank href=\"view.asp\\?id=\\d+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(html);
        while (matcher.find()) {
            String temp = matcher.group();
            set.add("https://www1.szu.edu.cn/board/" + temp.substring(temp.indexOf("\"") + 1));
        }
        return set;
    }
}