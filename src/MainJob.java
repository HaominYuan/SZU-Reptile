import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import javax.mail.MessagingException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

public class MainJob implements Job {

    private static String initUrl = "https://www1.szu.edu.cn/board/";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Set<String> urlSet = DbUtil.getUrl();
        Set<String> newUrlSet = new HashSet<>();
        StringBuilder body = new StringBuilder();
        // 访问公文通页面
        String urlsHtml = initQuery();
        // 获取页面上的链接
        Set<String> set = UrlSelector.getUrls(urlsHtml);
        ExecutorService pool = Executors.newFixedThreadPool(set.size());
        List<Future> list = new ArrayList<>();
        for (String url : set) {
            if (!urlSet.contains(url)) {
                urlSet.add(url);
                newUrlSet.add(url);
                HtmlGetter temp = new HtmlGetter(url);
                list.add(pool.submit(temp));
            }
        }

        pool.shutdown();
        for (Future f : list) {
            try {
                body.append(f.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        if ("".equals(body.toString())) {
            return;
        }

//        System.out.println(body);

        System.out.println(body);
        Mail mail = Mail.getInstance();
        try {
            mail.send(body.toString());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        System.out.println("fuck");
        DbUtil.insert(newUrlSet);
    }

    private String initQuery() {
        try {
            URL url = new URL(initUrl);
            URLConnection connection = url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();
            // dayy=30#30天内 ; keyword=计算机与软件学院 searchb1=搜索
            String params = "dayy=30%2330%CC%EC%C4%DA&search_type=fu&keyword=%BC%C6%CB%E3%BB%FA%D3%EB%C8%ED%BC%FE%D1%A7%D4%BA&keyword_user=j_soft&searchb1=%CB%D1%CB%F7";
            PrintWriter out = new PrintWriter(connection.getOutputStream());
            out.write(params);
            out.flush();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "gb2312"));
            StringBuilder html = new StringBuilder();
            String temp;
            while ((temp = bufferedReader.readLine()) != null) {
                html.append(temp).append("\n");
            }
            return html.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }



}
