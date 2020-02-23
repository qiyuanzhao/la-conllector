package com.lavector.collector.crawler.base;

import com.lavector.collector.def.Site;

/**
 * Created on 25/09/2017.
 *
 * @author seveniu
 */
public class CrawlerType {
    public static final String QQ_SPORT_TOP = Site.QQ + "_sport_top"; //http://sports.qq.com/
    public static final String SINA_SPORT_NEWS = Site.SINA + "_sport_news"; //http://sports.sina.com.cn/
    public static final String SINA_EGAME = Site.SINA + "_egame"; //http://dj.sina.com.cn/information/
    public static final String NETEASE_EGAME = Site.NETEASE + "_egame"; //http://play.163.com/special/00318QFH/ggindexdata2017.js/
    public static final String YIDIANZIXUN_SPORT_NEWS = Site.YIDIANZIXUN + "_sport_news"; //http://www.yidianzixun.com/home/q/news_list_for_channel?channel_id=sc4&cstart=20&cend=30&infinite=true&refresh=1&__from__=pc&multi=5&appid=web_yidian
    public static final String WEIBO_SPORT = Site.WEIBO + "_sport"; //http://weibo.com/ufc?profile_ftype=1&is_all=1
    public static final String OOQIU_EGAME = Site.OOQIU + "_egame"; //http://fight.ooqiu.com/

    // movie
    public static final String QQ_MOVIE = Site.QQ + "_movie_news"; //http://ent.qq.com/movie/
    public static final String MTIME_NEWS_TOP = Site.MTIME + "_movie_news_top"; //http://news.mtime.com/movie/all/
    public static final String TOUTIAO_MOVIE = Site.TOUTIAO + "_movie"; //http://www.toutiao.com/api/pc/feed/?category=movie&utm_source=toutiao&widen=1&max_behot_time=0&max_behot_time_tmp=0&tadrequire=true

    // nba
    public static final String HUPU_NBA_NEWS = Site.HUPU + "_nba_news"; //https://voice.hupu.com/nba/
    public static final String NBA_CHINA_NEWS = Site.NBA_CHINA + "_sport_news"; //http://china.nba.com/news/

    // football
    public static final String QQ_SPORT_FOOTBALL = Site.QQ + "_sport_football"; //http://sports.qq.com/csocce/cft/

    // mma
    public static final String MMA_SPORT = Site.MMA + "_sport"; //http://www.vs.cm/

    // ufc
    public static final String UFC_SPORT = Site.UFC + "_sport"; //http://interface.sina.cn/sports/get_ufc_news.d.json?pageNum=1&pageSize=16

    // dota
    public static final String DOTA_PCGAMES_EGAME = Site.PCGAMES + "_dota"; //http://fight.pcgames.com.cn/dota2/news/
    public static final String DOTA_178_EGAME = Site.GAME178 + "_dota"; //http://dota2.178.com/
    public static final String DOTA_SGAMER_EGAME = Site.SGAMEAR + "_dota"; //http://dota2.sgamer.com/

    // csgo
    public static final String CSGO178_EGAME = Site.GAME178 + "_csgo"; //http://csgo.178.com/
    public static final String CSGO_PCGAMES_EGAME = Site.PCGAMES + "_csgo"; //http://fight.pcgames.com.cn/csgo/news/
    public static final String CSGO_SGAMER_EGAME = Site.SGAMEAR + "_csgo"; //http://csgo.sgamer.com/news/list/1.html

    // overwatch
    public static final String OVERWATCH_178_EGAME = Site.GAME178 + "_overwatch"; //http://ow.178.com/
    public static final String OVERWATCH_PCGAMES_EGAME = Site.PCGAMES + "_overwatch"; //http://wangyou.pcgames.com.cn/ow/
    public static final String OVERWATCH_WEIBO_EGAME = Site.WEIBO + "_overwatch"; //http://weibo.com/blizzoverwatch?refer_flag=1001030101_&is_all=1
    public static final String OVERWATCH_CN_EGAME = Site.BLIZZARD_CN + "_egame"; //http://ow.blizzard.cn/article/news/
    public static final String OVERWATCH_DUOWAN_EGAME = Site.DUOWAN + "_overwatch"; //http://ow.duowan.com/tag/289238611958.html

    //article
    public static final String TOUTIAO_ARTICLE = Site.TOUTIAO + "_article"; //https://www.toutiao.com/search_content/?offset=20&format=json&keyword=innisfree&autoload=true&count=20&cur_tab=1&from=search_tab
    public static final String HUXIU_ARTICLE = Site.HUXIU + "_article"; //https://www.huxiu.com/search.html?s=GUCCI
    public static final String TIANYA_ARTICLE = Site.TIANYA + "_article";  //http://search.tianya.cn/bbs?q=GUCCI
    public static final String YIDIANZIXUN_ARTICLE = Site.YIDIANZIXUN + "_article"; //http://www.yidianzixun.com/channel/w/GUCCI?searchword=GUCCI
    public static final String ZAKER_ARTICLE = Site.ZAKER + "_article"; //http://www.myzaker.com/news/search_new.php?f=myzaker_com&keyword=GUCCI
}
