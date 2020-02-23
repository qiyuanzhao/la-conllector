package com.lavector.collector.crawler.nonce.dianping;

import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.nonce.dianping.entity.DianPingMessage;
import com.lavector.collector.crawler.nonce.dianping.entity.MessageType;
import com.lavector.collector.crawler.nonce.dianping.entity.Person;
import com.lavector.collector.crawler.project.edu.WriteFile;
import com.lavector.collector.crawler.util.JsonMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 11/05/2018.
 *
 * @author zeng.zhao
 */
public class DianPingPersonCommentPage implements PageParse {

    private Map<String, String> commentRank = new HashMap<>();

    {
        commentRank.put("item-rank-rst irr-star50", "5");
        commentRank.put("item-rank-rst irr-star40", "4");
        commentRank.put("item-rank-rst irr-star30", "3");
        commentRank.put("item-rank-rst irr-star20", "2");
        commentRank.put("item-rank-rst irr-star10", "1");
    }

    private JsonMapper mapper = JsonMapper.buildNormalBinder();

    @Override
    public boolean handleUrl(String url) {
        return url.contains("/reviews");
    }

    @Override
    public String pageName() {
        return null;
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        Html html = page.getHtml();
        String userId = page.getUrl().regex("member/(\\d+)").get();
        String userName = html.xpath("//h2[@class='name']/text()").get();
        List<Selectable> arriveNodes = html.xpath("//div[@class='txt J_rptlist']").nodes();
        List<DianPingMessage> messages = new ArrayList<>();
        arriveNodes.forEach(node -> {
            String shopId = node.xpath("//div[@class='tit']/h6/a/@href").regex("\\d+").get();
            String shopName = node.xpath("//div[@class='tit']/h6/a/text()").get();
            String rank = node.xpath("//div[@class='mode-tc comm-rst']/span/@class").get();
            String content = node.xpath("//div[@class='mode-tc comm-entry']/text()").get();
            String time = node.xpath("//div[@class='mode-tc info']/span[1]/text()")
                    .regex("发表于(.*)")
                    .get();
            if (time == null) {
                time = node.xpath("//div[@class='mode-tc info']/span[1]/text()")
                        .regex("更新于(.*)")
                        .get();
            }
            try {
                if (!(DateUtils.parseDate("20" + time, "yyyy-MM-dd").after(DianPingCommentListPage.startTime) && DateUtils.parseDate("20" + time, "yyyy-MM-dd").before(DianPingCommentListPage.endTime))) {
                    return;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            DianPingMessage message = new DianPingMessage();
            message.setTime("20" + time);
            message.setShopId(shopId);
            message.setRank(commentRank.get(rank) == null ? "0" : commentRank.get(rank));
            message.setContent(content);
            message.setType(MessageType.PERSON);
            message.setShopName(shopName);
            message.setPerson(new Person(userId, userName));
            messages.add(message);

        });

        if (CollectionUtils.isNotEmpty(messages)) {
//            DianPingPageProcessor.write(mapper.toJson(messages));
            WriteFile.write(mapper.toJson(messages), "/Users/zeng.zhao/Desktop/dianping_message_person.json");
            String nextUrl = html.xpath("//a[@class='page-next']/@href").get();
            if (StringUtils.isNotBlank(nextUrl)) {
                result.addRequest(new Request("http://www.dianping.com/member/" + userId +
                        "/reviews" + nextUrl).putExtra("referer", page.getUrl().get()));
            }
        }

        return result;
    }

    public static void main(String[] args) {

    }
}
