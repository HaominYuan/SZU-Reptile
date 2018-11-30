package myImplement;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlGetter implements Callable<String> {
    private String url;

    public HtmlGetter(String url) {
        this.url = url;
    }


    @Override
    public String call() throws Exception {
        URLConnection connection = new URL(url).openConnection();
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), "gb2312"));
        StringBuilder html = new StringBuilder();
        String temp;
        while ((temp = bufferedReader.readLine()) != null) {
            html.append(temp).append("\n");
        }
        return rToA(html.toString());
    }

    private String rToA(String html) {
        Pattern pattern=Pattern.compile("=\"/");
        Matcher matcher = pattern.matcher(html);
        return matcher.replaceAll("=\\\"http://www1.szu.edu.cn/");
    }
}
