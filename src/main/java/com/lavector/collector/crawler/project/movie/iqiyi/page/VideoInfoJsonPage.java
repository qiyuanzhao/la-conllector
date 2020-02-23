package com.lavector.collector.crawler.project.movie.iqiyi.page;

import com.lavector.collector.crawler.base.PageParse;
import com.lavector.collector.crawler.project.edu.WriteFile;
import com.lavector.collector.crawler.util.JsonMapper;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Json;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created on 2018/1/26.
 *
 * @author zeng.zhao
 */
public class VideoInfoJsonPage implements PageParse {

    private Logger logger = LoggerFactory.getLogger(VideoInfoJsonPage.class);

    private JsonMapper jsonMapper = JsonMapper.buildNonNullBinder();

    private ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
    private ScriptEngine nashorn = scriptEngineManager.getEngineByName("nashorn");
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public boolean handleUrl(String url) {
        return url.contains("mixer.video.iqiyi.com/jp/mixin/videos");
    }

    @Override
    public String pageName() {
        return null;
    }

    @Override
    public Result parse(Page page) {
        Result result = Result.get();
        String json = page.getJson().get();


        try {
            nashorn.eval(json);
        } catch (ScriptException e) {
            e.printStackTrace();
        }

        ScriptObjectMirror scriptObjectMirror = (ScriptObjectMirror) nashorn.get("tvInfoJs");
        String name = scriptObjectMirror.get("name").toString();
        String videoUrl = scriptObjectMirror.get("url").toString();
        String playCount = scriptObjectMirror.get("playCount").toString();
        String albumId = scriptObjectMirror.get("albumQipuId").toString();
        String description = scriptObjectMirror.get("description").toString();
        Object publishTime = scriptObjectMirror.get("publishTime");
        DecimalFormat df = new DecimalFormat("#,##0");
        Long timeStamp = Long.parseLong(df.format(publishTime).replaceAll(",", ""));
        Integer likes = Integer.parseInt(scriptObjectMirror.get("upCount").toString());

        Map<String, String> map = new HashMap<>();
        map.put("url", videoUrl);
        map.put("name", name);
        map.put("playCount", playCount);
        map.put("like", likes.toString());
        map.put("description", description);
        map.put("publishTime", dateFormat.format(new Date(timeStamp)));
        String jsonData = jsonMapper.toJson(map);
        String filePath = "/Users/zeng.zhao/Desktop/iqiyi.json";
        WriteFile.write(jsonData, filePath);
        logger.info("写入成功！");
        return result;
    }

    private Integer getLikeCount(String albumId) throws IOException {
        String url = "http://up-video.iqiyi.com/ugc-updown/quud.do?dataid=" +
                albumId  +
                "&type=2&userid=&flashuid=&appID=&callback=";
        String content = Request.Get(url)
                .execute()
                .returnContent()
                .asString();

        String upCount = new Json(content).regex("\"up\":(\\d+)").get();
        return Integer.parseInt(upCount);
    }
}
