package com.lavector.collector.crawler.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.stream.Collectors;

/**
 * Created on 23/06/2018.
 *
 * @author zeng.zhao
 */
public class HtmlParser {
    private String url;
    private Document doc;
    private Element body;

    public HtmlParser(String text) {
        doc = Jsoup.parseBodyFragment(text);
        this.body = doc.body();
    }

    public HtmlParser(String text, String url) {
        this(text);
        this.url = url;
    }

    /**
     * 删除 标签 及内容
     */
    public synchronized HtmlParser delTagWithContent(CharSequence... delTag) {
        if (delTag.length > 0) {
            String select = String.join(",", delTag);
            body.select(select).remove();
        }
        return this;
    }


    /**
     * 仅删除 标签
     */
    public synchronized void removeTagWithoutContent(CharSequence... delTag) {
        if (delTag.length > 0) {
            body.select(String.join(",", delTag)).unwrap();
        }
    }

    /**
     * 仅删除 标签
     */
    public synchronized HtmlParser removeTagWithoutContentIgnore(String... delTagWithoutContent) {
        if (delTagWithoutContent.length > 0) {
            Iterator<Element> iterator = body.getAllElements().iterator();
            while (iterator.hasNext()) {
                Element element = iterator.next();
                boolean flag = false;
                for (CharSequence charSequence : delTagWithoutContent) {
                    if (element.tagName().equals(charSequence)) {
                        flag = true;
                    }
                }
                if (flag) {
                    continue;
                }
                if (!element.tagName().equals("body") && element.parent() != null) {
                    element.unwrap();
                }
            }
        }
        return this;
    }

    /**
     * 删除 注释
     */
    private synchronized HtmlParser delHtmlComment() {
        body.select("#comment").remove();
        return this;
    }


    public synchronized HtmlParser parseA() {
        return parseA(null);
    }

    public synchronized HtmlParser parseA(String url) {

        for (Element a : body.select("a")) {
            removeAttribute(a, "href");
            String href = a.attr("href");
            if (url != null) {
                href = canonicalizeUrl(href, url);
            }
            a.attr("href", href);
        }
        return this;
    }


    public synchronized HtmlParser parseImage() {
        return parseImage(null);
    }

    public synchronized HtmlParser parseImage(String url) {
        for (Element a : body.select("img")) {
            removeAttribute(a, "src");
            String src = a.attr("src");
            if (url != null) {
                src = canonicalizeUrl(src, url);
            }
            a.attr("src", src);
        }
        return this;
    }

    @Override
    public String toString() {
        return body.html().trim();
    }

    public static void main(String[] args) {
        String html = null;
        try {
            // http请求
//            String url = "http://www.ahga.gov.cn/jhjs/index_7.html";
//            html = HttpUtil.getHttpContent(url,
//                    "gbk", Site.me().setUserAgent(UserAgent.getUserAgent())
//                            .addHeader("Referer", UrlUtil.getHost(url))
//            );
//            html = HttpUtil.getHttpContent("http://www.baidu.com", "utf-8", Site.me());
            String s = new HtmlParser("<img name=\"yyimg\" class=\"quoteimg\" style=\"max-width:400px;width:450px;\" src=\"http://club2.autoimg.cn/album/g21/M05/5F/E0/userphotos/2016/10/22/13/500_wKgFWlgK8o2ANLK-AAMNPp7aIXU193.jpg\" onerror=\"tz.picNotFind(this)\">")
                    .removeAttribute("img","src")
                    .toString();
            System.out.println(s);

            // 本地文件
            html = Files.lines(Paths.get("/Users/seveniu/Downloads/temp/tieba2.html")).collect(Collectors.joining("\n"));
            HtmlParser parser = new HtmlParser(html);
            parser.delTagWithContent("script", "style", "frame", "iframe", "noscript", "embed")
                    .removeAllAttribute("href", "src")
                    .removeTagWithoutContentIgnore("a", "img")
                    .parseImage()
                    .parseA();
//            System.out.println(parser.toString());
//            System.out.println(html);
//            System.out.println(new HtmlContentParser(html).parseHtml());
//            System.out.println(html);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String canonicalizeUrl(String url, String refer) {
        try {
            URL base;
            try {
                base = new URL(refer);
            } catch (MalformedURLException var5) {
                URL abs = new URL(refer);
                return abs.toExternalForm();
            }

            if (url.startsWith("?")) {
                url = base.getPath() + url;
            }

            URL e = new URL(base, url);
            return encodeIllegalCharacterInUrl(e.toExternalForm());
        } catch (MalformedURLException var6) {
            return "";
        }
    }

    public HtmlParser removeAttribute(String select, String... ignoreAttrs) {
        for (Element e : body.select(select)) {
            removeAttribute(e, ignoreAttrs);
        }
        return this;
    }

    public HtmlParser removeAllAttribute(String... ignoreAttrs) {
        for (Element e : body.getAllElements()) {
            removeAttribute(e, ignoreAttrs);
        }
        return this;
    }


    public static void removeAttribute(Element element, String... ignoreAttrs) {
        Attributes at = element.attributes();
        LinkedList<String> removeAttrList = new LinkedList<>();
        for (Attribute a : at) {
            boolean isIgnore = false;
            for (String ignore : ignoreAttrs) {
                if (a.getKey().equals(ignore)) {
                    isIgnore = true;
                }
            }
            if (!isIgnore) {
                removeAttrList.add(a.getKey());
            }
        }
        for (String attrKey : removeAttrList) {
            at.remove(attrKey);
        }
    }

    public static String encodeIllegalCharacterInUrl(String url) {
        return url.replace(" ", "%20");
    }

}
