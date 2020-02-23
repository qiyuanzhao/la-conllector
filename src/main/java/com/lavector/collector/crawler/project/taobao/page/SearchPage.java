package com.lavector.collector.crawler.project.taobao.page;


import com.jayway.jsonpath.JsonPath;
import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.project.babyTreeUserInfo.page.TaoBaoPo;
import com.lavector.collector.entity.readData.SkuData;
import net.sf.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Html;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchPage implements PageParse {


    @Override
    public boolean handleUrl(String url) {

        return url.contains("https://s.taobao.com/search");
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        SkuData skuData = (SkuData) page.getRequest().getExtra("skuData");
        Integer number = (Integer) page.getRequest().getExtra("number");
        if (number <= 10000) {
            String html = page.getHtml().xpath("//script[8]/html()").get();
            if (html == null) {
                throw new RuntimeException("验证码....");
            }
            String substring = html.substring(html.indexOf("\"auctions\":") + 11, html.indexOf("\"recommendAuctions\"") - 1);
            JSONArray jsonArray = JSONArray.fromObject(substring);

            List<TaoBaoPo> taoBaoPoList = new ArrayList<>();
            for (Object str : jsonArray) {
                TaoBaoPo taoBaoPo = new TaoBaoPo();
                String title = JsonPath.read(str, "$.raw_title");
                String detailUrl = JsonPath.read(str, "$.detail_url");
                String viewSales = JsonPath.read(str, "$.view_sales");
                String nick = JsonPath.read(str, "$.nick");
                Pattern pattern = Pattern.compile("[0-9]+");
                Matcher matcher = pattern.matcher(viewSales);
                while (matcher.find()) {
                    String group = matcher.group();
                    taoBaoPo.setViewSales(group);
                }
                String commentCount = JsonPath.read(str, "$.comment_count");
                String viewPrice = JsonPath.read(str, "$.view_price");
                taoBaoPo.setTitle(title);
                taoBaoPo.setDetailUrl("https:" + detailUrl);
                taoBaoPo.setCommentCount(commentCount);
                taoBaoPo.setViewPrice(viewPrice);
                taoBaoPo.nick = nick;

                List<Map<String, String>> read = JsonPath.read(str, "$.icon");

                if (read != null && read.size() > 0) {
                    check(read, taoBaoPo);
                    if (StringUtils.isEmpty(taoBaoPo.type)) {
                        taoBaoPo.type = "淘宝";
                    }
                    taoBaoPoList.add(taoBaoPo);
                }
            }
            page.getRequest().putExtra("taoBaoPoList", taoBaoPoList);
            page.putField("taoBaoPoList", taoBaoPoList);
            page.putField("skuData", skuData);
            String url = page.getUrl().get();
            Integer pagenumber = Integer.parseInt(url.substring(url.lastIndexOf("=") + 1));
            if (jsonArray.size() > 43 && pagenumber < 350) {
                int newNumber = pagenumber + 44;
                String newUrl = url.replace("s=" + pagenumber, "s=" + newNumber);
                Request request = new Request(newUrl).putExtra("skuData", skuData).putExtra("number", number + taoBaoPoList.size());
                request.addHeader("referer", url);
                request.addHeader(":path", url.substring(url.indexOf("/search")));
                result.addRequest(request);
            }
        }

        return result;
    }

    private void check(List<Map<String, String>> read, TaoBaoPo taoBaoPo) {
        for (Map<String, String> title : read) {
            if (title.get("title").contains("全球")) {
                taoBaoPo.type = "全球购";
                break;
            } else if (title.get("title").contains("尚天猫")) {
                taoBaoPo.type = "尚天猫";
                break;
            } else if (title.get("title").contains("天猫国际")) {
                taoBaoPo.type = "天猫国际";
                break;
            }
        }
    }

    @Override
    public String pageName() {
        return null;
    }

}
