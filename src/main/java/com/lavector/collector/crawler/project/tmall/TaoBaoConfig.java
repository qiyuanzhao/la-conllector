package com.lavector.collector.crawler.project.tmall;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2018/11/14.
 *
 * @author zeng.zhao
 */
public class TaoBaoConfig {

    private String brand;

    private String[] keywords;

    private List<String> excludeKeywords;

    private TaoBaoConfig(String brand, String[] keywords, List<String> excludeKeywords) {
        this.brand = brand;
        this.keywords = keywords;
        this.excludeKeywords = excludeKeywords;
    }

    public String getBrand() {
        return brand;
    }

    public String[] getKeywords() {
        return keywords;
    }

    public List<String> getExcludeKeywords() {
        return excludeKeywords;
    }

    private static List<TaoBaoConfig> taoBaoConfigs = new ArrayList<>();

    static {
//        taoBaoConfigs.add(new TaoBaoConfig("资生堂", new String[]{"资生堂"}, Lists.newArrayList("珊珂", "senka", "uno", "洗颜专科", "CPB", "ipsa", "nars", "elixir", "aupres", "urara", "anessa", "泊美", "Za", "水之密语", "丝蓓绮", "惠润", "Super Mild", "悠莱", "玛馨妮", "d program", "六角眉笔", "心机", "意境")));
//        taoBaoConfigs.add(new TaoBaoConfig("兰蔻", new String[]{"兰蔻"}, Lists.newArrayList("香水", "男香", "女香")));
//        taoBaoConfigs.add(new TaoBaoConfig("雅诗兰黛", new String[]{"雅诗兰黛"}, Lists.newArrayList("香水", "男香", "女香")));
//        taoBaoConfigs.add(new TaoBaoConfig("SK-II", new String[]{"SK-II", "sk2", "SKII"}, Lists.newArrayList()));
//        taoBaoConfigs.add(new TaoBaoConfig("迪奥", new String[]{"迪奥"}, Lists.newArrayList("香水", "男香", "女香", "衣", "包", "裙", "裤", "服饰", "饰品", "耳环", "项链", "鞋", "外套", "T恤", "墨镜")));
//        taoBaoConfigs.add(new TaoBaoConfig("香奈儿", new String[]{"香奈儿"}, Lists.newArrayList("香水", "男香", "女香", "衣", "包", "裙", "裤", "服饰", "饰品", "耳环", "项链", "鞋", "外套", "手表", "T恤", "丝巾", "围巾", "墨镜")));
//        taoBaoConfigs.add(new TaoBaoConfig("CPB", new String[]{"CPB"}, Lists.newArrayList()));
//        taoBaoConfigs.add(new TaoBaoConfig("La Mer", new String[]{"La Mer", "LaMer", "海蓝之谜"}, Lists.newArrayList()));
//        taoBaoConfigs.add(new TaoBaoConfig("La Prairie", new String[]{"La Prairie", "laprarie", "莱伯妮"}, Lists.newArrayList()));
//        taoBaoConfigs.add(new TaoBaoConfig("Sisley", new String[]{"Sisley", "希思黎"}, Lists.newArrayList()));
//        taoBaoConfigs.add(new TaoBaoConfig("Guerlain", new String[]{"娇兰", "Guerlain"}, Lists.newArrayList("香水", "男香", "女香")));
//        taoBaoConfigs.add(new TaoBaoConfig("阿玛尼", new String[]{"阿玛尼"}, Lists.newArrayList("香水", "男香", "女香", "衣", "裙", "裤", "服饰", "鞋", "外套", "T恤", "手表", "墨镜")));
//        taoBaoConfigs.add(new TaoBaoConfig("Ipsa", new String[]{"Ipsa", "茵芙莎"}, Lists.newArrayList()));
//        taoBaoConfigs.add(new TaoBaoConfig("科颜氏", new String[]{"科颜氏"}, Lists.newArrayList()));
//        taoBaoConfigs.add(new TaoBaoConfig("兰芝", new String[]{"兰芝"}, Lists.newArrayList()));
//        taoBaoConfigs.add(new TaoBaoConfig("倩碧", new String[]{"倩碧"}, Lists.newArrayList("香水", "男香", "女香")));
//        taoBaoConfigs.add(new TaoBaoConfig("碧欧泉", new String[]{"碧欧泉"}, Lists.newArrayList("香水", "男香", "女香")));
//        taoBaoConfigs.add(new TaoBaoConfig("悦木之源", new String[]{"悦木之源"}, Lists.newArrayList()));
//        taoBaoConfigs.add(new TaoBaoConfig("Nars", new String[]{"Nars"}, Lists.newArrayList()));
//        taoBaoConfigs.add(new TaoBaoConfig("Bobbi Brown", new String[]{"Bobbi Brown"}, Lists.newArrayList()));
//        taoBaoConfigs.add(new TaoBaoConfig("Tom Ford", new String[]{"Tom Ford", "TF"}, Lists.newArrayList("香水", "男香", "女香", "衣", "包", "裙", "裤", "服饰", "饰品", "耳环", "项链", "鞋", "外套", "手表", "T恤", "丝巾", "围巾", "墨镜")));
//        taoBaoConfigs.add(new TaoBaoConfig("YSL", new String[]{"YSL"}, Lists.newArrayList("香水", "男香", "女香", "包")));
//        taoBaoConfigs.add(new TaoBaoConfig("MAC", new String[]{"MAC", "M.A.C."}, Lists.newArrayList("笔记本", "电脑", "全面屏", "新机", "book", "测评")));
//        taoBaoConfigs.add(new TaoBaoConfig("Elixir", new String[]{"怡丽丝尔", "Elixir"}, Lists.newArrayList()));
//        taoBaoConfigs.add(new TaoBaoConfig("欧莱雅", new String[]{"欧莱雅"}, Lists.newArrayList()));
//        taoBaoConfigs.add(new TaoBaoConfig("Fresh", new String[]{"馥蕾诗"}, Lists.newArrayList()));
//        taoBaoConfigs.add(new TaoBaoConfig("雪肌精", new String[]{"雪肌精"}, Lists.newArrayList()));
//        taoBaoConfigs.add(new TaoBaoConfig("Aupres", new String[]{"欧珀莱"}, Lists.newArrayList()));
//        taoBaoConfigs.add(new TaoBaoConfig("Olay", new String[]{"玉兰油"}, Lists.newArrayList()));
//        taoBaoConfigs.add(new TaoBaoConfig("Urara", new String[]{"悠莱"}, Lists.newArrayList()));
//        taoBaoConfigs.add(new TaoBaoConfig("梦妆", new String[]{"梦妆"}, Lists.newArrayList()));
//        taoBaoConfigs.add(new TaoBaoConfig("安热沙", new String[]{"安热沙", "安耐晒"}, Lists.newArrayList()));
//        taoBaoConfigs.add(new TaoBaoConfig("泊美", new String[]{"泊美", "Pure Mild"}, Lists.newArrayList()));
//        taoBaoConfigs.add(new TaoBaoConfig("悦诗风吟", new String[]{"悦诗风吟", "Innisfree"}, Lists.newArrayList()));
//        taoBaoConfigs.add(new TaoBaoConfig("Za", new String[]{"Za"}, Lists.newArrayList()));
//        taoBaoConfigs.add(new TaoBaoConfig("美宝莲", new String[]{"美宝莲"}, Lists.newArrayList()));
//        taoBaoConfigs.add(new TaoBaoConfig("伊蒂之屋", new String[]{"爱丽小屋", "伊蒂之屋", "edute house"}, Lists.newArrayList()));


        taoBaoConfigs.add(new TaoBaoConfig("Allie", new String[]{"Allie防晒"}, Lists.newArrayList()));
        taoBaoConfigs.add(new TaoBaoConfig("Sofina", new String[]{"Sofina防晒"}, Lists.newArrayList()));
        taoBaoConfigs.add(new TaoBaoConfig("雪花秀", new String[]{"雪花秀"}, Lists.newArrayList()));
        taoBaoConfigs.add(new TaoBaoConfig("Albion", new String[]{"奥尔滨", "奥比虹"}, Lists.newArrayList()));
        taoBaoConfigs.add(new TaoBaoConfig("Cosme Decorte", new String[]{"黛珂"}, Lists.newArrayList()));
        taoBaoConfigs.add(new TaoBaoConfig("Pola", new String[]{"Pola"}, Lists.newArrayList()));
    }

    public static List<TaoBaoConfig> getTaoBaoConfigs() {
        return taoBaoConfigs;
    }

}
