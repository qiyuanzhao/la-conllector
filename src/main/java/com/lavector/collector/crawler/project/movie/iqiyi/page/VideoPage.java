package com.lavector.collector.crawler.project.movie.iqiyi.page;

import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.util.RegexUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;

import java.util.List;
import java.util.Objects;

/**
 * Created on 2018/1/24.
 *
 * @author zeng.zhao
 */
public class VideoPage implements PageParse {

    private Logger logger = LoggerFactory.getLogger(VideoPage.class);

    @Override
    public boolean handleUrl(String url) {
        return RegexUtil.isMatch(".*www.iqiyi.com/v_.*\\.html.*", url)
                || RegexUtil.isMatch(".*www.iqiyi.com/ad/\\d+/.*\\.html", url)
                || RegexUtil.isMatch(".*www.iqiyi.com/.*\\.html", url);
    }

    @Override
    public String pageName() {
        return null;
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        Html html = page.getHtml();
        List<String> scripts = html.xpath("//script").all();
        String pageInfo = null;
        for (String script : scripts) {
            if (script.contains("Q.PageInfo.playPageInfo")) {
                pageInfo = new Json(script).regex("<script.*?>(.*?)</script>").get().replaceAll("\r\n", "");
                break;
            }
        }

        //获取视频信息url(json)
        if (Objects.nonNull(pageInfo)) {
            String albumId = new Json(pageInfo).regex("albumId: (\\d+)").get();
            if (StringUtils.isBlank(albumId)) {
                albumId = new Json(pageInfo).regex("albumId:(\\d+)").get();
            }
            if (StringUtils.isNotBlank(albumId)) {
                result.addRequest(new us.codecraft.webmagic.Request("http://mixer.video.iqiyi.com/jp/mixin/videos/" + albumId + "?callback=&status=1"));
            }
        } else {
            String albumId = new Json(page.getRawText()).regex("albumId:(\\d+)").get();

            if (StringUtils.isBlank(albumId)) {
                albumId = new Json(page.getRawText()).regex("albumId: (\\d+)").get();
            }
            if (StringUtils.isNotBlank(albumId)) {
                result.addRequest(new us.codecraft.webmagic.Request("http://mixer.video.iqiyi.com/jp/mixin/videos/" + albumId + "?callback=&status=1"));
                return result;
            }

        }
        return result;
    }


    public static void main(String[] args) throws Exception {
        String url = "http://www.iqiyi.com/w_19rtwmf2lx.html";
        String content = Request.Get(url)
                .execute()
                .returnContent()
                .asString();
        Page page = new Page();
        page.setUrl(new Json(url));
        page.setRequest(new us.codecraft.webmagic.Request(url));
        page.setRawText(content);
        VideoPage videoPage = new VideoPage();
        videoPage.parse(page);
    }
}
