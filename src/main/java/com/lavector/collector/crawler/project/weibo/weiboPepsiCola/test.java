package com.lavector.collector.crawler.project.weibo.weiboPepsiCola;


import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class test {

    public static void main(String[] args) throws ParseException, IOException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = simpleDateFormat.parse("2018-06-01");
        Date parse = simpleDateFormat.parse("2018-08-31");

        long shiwu = date1.getTime() / 1000;
        long shiliu = parse.getTime()/1000;

        System.out.println(shiwu);
        System.out.println(shiliu);
        Date date = new Date();
        long l = date.getTime() / 1000;
        System.out.println(l);

        Date date2 = new Date(shiwu*1000);
        String format = simpleDateFormat.format(date2);
        System.out.println(format);

        Duration duration = Duration.of(1, ChronoUnit.DAYS);

        long seconds = duration.getSeconds();
        System.out.println(seconds*1000);



//        String url = "https://c.api.weibo.com/2/search/statuses/limited.json?source=674587526&access_token=" +
//                "2.00Byjh5G0IqI9v98fc9582ccrGseQD&starttime=1527782400&endtime=1535644800&count=30&dup=0&antispam=1&page=1&q=%E7%99%BE%E4%BA%8B%E6%97%A0%E7%B3%96";
//        HttpClient httpClient = new DefaultHttpClient();
//        HttpGet httpGet = new HttpGet(url);
//        HttpResponse httpResponse = httpClient.execute(httpGet);
//        String string = EntityUtils.toString(httpResponse.getEntity());
//        System.out.println(string);

//        https://s.weibo.com/weibo?q=%E7%99%BE%E4%BA%8B%E6%97%A0%E7%B3%96&wvr=6&b=1&Refer=SWeibo_box
//https://c.api.weibo.com/2/search/statuses/limited.json?source=674587526&access_token=2.00Byjh5G0IqI9v98fc9582ccrGseQD&starttime=1527782400&endtime=1535644800&count=30&dup=0&antispam=1&page=1&q=%E7%99%BE%E4%BA%8B%E6%97%A0%E7%B3%96


//https://s.weibo.com/weibo?q=%E7%99%BE%E4%BA%8B%E9%BB%91%E7%BD%90&wvr=6&b=1&Refer=SWeibo_box
        //https://s.weibo.com/weibo?q=%E7%99%BE%E4%BA%8B%E6%97%A0%E7%B3%96&typeall=1&suball=1&timescope=custom:2018-11-27:2018-12-01&Refer=g
//        String string = "居然还有百事无糖\n雪碧纤维、可口纤维可乐\n我都买了";
//        String replace = string.replace("\\u000a", "");
//
//        Pattern pattern = Pattern.compile("\\n");
//        Matcher matcher = pattern.matcher(string);
//
//        String replaceAll = matcher.replaceAll("");
//
//        System.out.println(replaceAll);

    }

}
