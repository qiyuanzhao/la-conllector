package com.lavector.collector.crawler.nonce.dianping;

import com.google.common.collect.Sets;
import com.google.common.io.CharSink;
import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;
import com.jayway.jsonpath.JsonPath;
import com.lavector.collector.crawler.nonce.dianping.entity.DianPingMessage;
import com.lavector.collector.crawler.nonce.dianping.entity.MessageType;
import com.lavector.collector.crawler.nonce.dianping.entity.Shop;
import com.lavector.collector.crawler.util.JsonMapper;
import com.lavector.collector.crawler.util.StringToDateConverter;
import net.minidev.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.text.translate.CsvTranslators;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Created on 15/05/2018.
 *
 * @author zeng.zhao
 */
public class DataProcess {

    private static final String BASE_PATH = "/Users/zeng.zhao/Desktop";

    private static JsonMapper jsonMapper = JsonMapper.buildNonNullBinder();

    private static ZoneId zoneId = ZoneId.systemDefault();
    private static LocalDate localDate = LocalDate.of(2018, 1, 1);
    private static ZonedDateTime zonedDateTime = localDate.atStartOfDay(zoneId);
    public static Date startTime = Date.from(zonedDateTime.toInstant());

    public static void main(String[] args) throws Exception {
//        aggregate();
//       shopComment();
        shopToProvince();
    }

    private static void shopComment() throws Exception {
        List<DianPingMessage> messages = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader("/Users/zeng.zhao/Desktop/dianping_message.json"));
        String line = reader.readLine();
        int[] a = new int[1];
        int[] b = new int[1];
        while (line != null) {
            JSONArray items = JsonPath.read(line, "$.[*]");
            items.forEach(item -> {
                DianPingMessage message = jsonMapper.fromJson(jsonMapper.toJson(item), DianPingMessage.class);
                if (message.getType().equals(MessageType.SHOP)) {
                    messages.add(message);
                    a[0]++;
                } else {
                    b[0]++;
                }
            });
            line = reader.readLine();
        }

        System.out.println("a = " + a[0]);
        System.out.println("b = " + b[0]);
        List<Shop> shops = new ArrayList<>();
        BufferedReader reader1 = new BufferedReader(new FileReader("/Users/zeng.zhao/Desktop/dianping_shop_1.json"));
        String line1 = reader1.readLine();
        while (line1 != null) {
            Shop shop = jsonMapper.fromJson(line1, Shop.class);
            shops.add(shop);
            line1 = reader1.readLine();
        }

//        Set<String> citys = shops.stream().map(Shop::getProvince).collect(Collectors.toSet());
//        citys.forEach(city -> new File(BASE_PATH + "/" + city).mkdir());

        shops.forEach(shop -> {
            List<DianPingMessage> messageList = messages.stream().filter(message -> message.getShopId().equals(shop.getShopId())).collect(Collectors.toList());
            messageList.forEach(message -> {
                String content = null;
                try {
                    content = message.getShopId()
                            + "," + shop.getName() + ","
                            + shop.getProvince() + ","
                            + shop.getCity() + ","
                            + (shop.getAddress().replaceAll(",", "，")) + ","
                            + (message.getContent().replaceAll(",", "，")) + "," + message.getTime() + "," + message.getRank()
                            + "," + (message.getPerson().getUsername().replaceAll(",", "，")) + "," + shop.getUrl();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (DateUtils.parseDate(message.getTime().trim(), "yyyy-MM-dd HH:mm").after(startTime)) {
                        try {
                            File file = new File(BASE_PATH + "/shop-comment(2018).csv");
                            CharSink charSink = Files.asCharSink(file,
                                    StandardCharsets.UTF_8, FileWriteMode.APPEND);
                            if (!file.exists()) {
                                charSink.write("店铺ID,店铺名,省份,城市,详细地址,评论内容,时间,评星,用户名,店铺URL\n");
                            } else {
                                charSink.write(content + "\n");
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            File file = new File(BASE_PATH + "/shop-comment(2017).csv");
                            CharSink charSink = Files.asCharSink(file,
                                    StandardCharsets.UTF_8, FileWriteMode.APPEND);
                            if (!file.exists()) {
                                charSink.write("店铺ID,店铺名,省份,城市,详细地址,评论内容,时间,评星,用户名,店铺URL\n");
                            } else {
                                charSink.write(content + "\n");
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            });
        });

    }


    private static void personComment() throws Exception {
        Set<DianPingMessage> messages = new HashSet<>();
        BufferedReader reader = new BufferedReader(new FileReader("/Users/zeng.zhao/Desktop/dianping_message_2.json"));
        String line = reader.readLine();
        while (line != null) {
            JSONArray items = JsonPath.read(line, "$.[*]");
            items.forEach(item -> {
                DianPingMessage message = jsonMapper.fromJson(jsonMapper.toJson(item), DianPingMessage.class);
                messages.add(message);
            });
            line = reader.readLine();
        }

        messages.forEach(message -> {
            String type;
            if (message.getType().equals(MessageType.PERSON)) {
                type = "点评";
            } else {
                type = "签到";
            }
            String content = message.getShopId() + "," + (message.getPerson().getUsername().replaceAll(",", "，")) + ","
                    + message.getShopName() + "," + type;
            try {
                File file = new File(BASE_PATH + "/person.csv");
                CharSink charSink = Files.asCharSink(file,
                        StandardCharsets.UTF_8, FileWriteMode.APPEND);
                if (!file.exists()) {
                    charSink.write("店铺ID(来源),用户名,(签到/点评)店铺,类型\n");
                } else {
                    charSink.write(content + "\n");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private static void aggregate() throws Exception {
        Set<DianPingMessage> messages = new HashSet<>();
        BufferedReader reader = new BufferedReader(new FileReader("/Users/zeng.zhao/Desktop/dianping_message_2.json"));
        String line = reader.readLine();
        while (line != null) {
            JSONArray items = JsonPath.read(line, "$.[*]");
            items.forEach(item -> {
                DianPingMessage message = jsonMapper.fromJson(jsonMapper.toJson(item), DianPingMessage.class);
                messages.add(message);
            });
            line = reader.readLine();
        }

        HashMap<String, Integer> map = new HashMap<>();
        messages.stream()
                .filter(message -> message.getShopName() != null)
                .forEach(message -> {
                    String shopName = message.getShopName();
                    Integer count = map.get(shopName);
                    if (count == null) {
                        map.put(shopName, 1);
                    } else {
                        Integer c = map.get(shopName);
                        c++;
                        map.put(shopName, c);
                    }
                });

        List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
        list.sort(Comparator.comparing(Map.Entry::getValue));
        int count = 0;
        CharSink charSink = Files.asCharSink(Paths.get("/Users/zeng.zhao/Desktop/dianping(频次).csv").toFile(),
                StandardCharsets.UTF_8, FileWriteMode.APPEND);
        charSink.write("店铺,频次\n");
        for (int i = list.size() - 1; i >= 0 && count < 100; i--) {
            String content = list.get(i).getKey() + "," + list.get(i).getValue() + ",";
            charSink.write(content + "\n");
            count++;
        }

    }


    private static void shopToProvince() throws Exception {
        StringToDateConverter converter = new StringToDateConverter();
        List<Shop> shops = new ArrayList<>();
        BufferedReader reader1 = java.nio.file.Files.newBufferedReader(Paths.get("/Users/zeng.zhao/Desktop/dianping_shop_1.json"));
        String line1 = reader1.readLine();
        while (line1 != null) {
            Shop shop = jsonMapper.fromJson(line1, Shop.class);
            shops.add(shop);
            line1 = reader1.readLine();
        }

        Set<DianPingMessage> shopMessages = new HashSet<>();
        Set<DianPingMessage> personMessages = new HashSet<>();
        BufferedReader reader = java.nio.file.Files.newBufferedReader(Paths.get("/Users/zeng.zhao/Desktop/dianping_sign.json"));
        String line = reader.readLine();
        while (line != null) {
            JSONArray items = JsonPath.read(line, "$.[*]");
            items.forEach(item -> {
                DianPingMessage message = jsonMapper.fromJson(jsonMapper.toJson(item), DianPingMessage.class);
                if (converter.convert(message.getTime().trim()).before(startTime)) {
                    return;
                }
                if (message.getType().equals(MessageType.SHOP)) {
                    shopMessages.add(message);
                } else {
                    personMessages.add(message);
                }
            });
            line = reader.readLine();
        }
        if (shopMessages.size() == 0) {
            BufferedReader reader2 = java.nio.file.Files.newBufferedReader(Paths.get("/Users/zeng.zhao/Desktop/dianping_message.json"));
            String line2 = reader2.readLine();
            while (line2 != null) {
                JSONArray items = JsonPath.read(line2, "$.[*]");
                items.forEach(item -> {
                    DianPingMessage message = jsonMapper.fromJson(jsonMapper.toJson(item), DianPingMessage.class);
                    if (message.getType().equals(MessageType.SHOP)) {
                        shopMessages.add(message);
                    } else {
                        if (converter.convert(message.getTime().trim()).before(startTime)) {
                            return;
                        }
                        personMessages.add(message);
                    }
                });
                line2 = reader2.readLine();
            }
        }

        Map<String, List<DianPingMessage>> map = new HashMap<>();
        personMessages.forEach(personMessage -> {
//            if (personMessage.getType().equals(MessageType.PERSON)) {
                shopMessages.stream()
                        .filter(shop -> shop.getPerson().getUserId().equals(personMessage.getPerson().getUserId()))
                        .findFirst()
                        .ifPresent(shop -> {
                            shops.stream().filter(shop1 -> shop1.getShopId().equals(shop.getShopId()))
                                    .findFirst()
                                    .ifPresent(shop1 -> {
                                        List<DianPingMessage> messages = map.get(shop1.getProvince());
                                        if (messages == null) {
                                            messages = new ArrayList<>();
                                        }
                                        messages.add(personMessage);
                                        map.put(shop1.getProvince(), messages);
                                    });

                        });
//            }
        });


//        Map<String, Set<String>> shopMap = new HashMap<>(); //按省份归类店铺
//        shops.forEach(shop -> {
//            Set<String> set = shopMap.get(shop.getProvince());
//            if (set == null) {
//                shopMap.put(shop.getProvince(), Sets.newHashSet(shop.getShopId()));
//            } else {
//                set.add(shop.getShopId());
//                shopMap.put(shop.getProvince(), set);
//            }
//        });
//
//
//        Map<String, Set<String>> personMap = new HashMap<>();//按省份归类用户
//        shopMessages.forEach(shopMessage -> {
//            shopMap.forEach((province, shopIds) -> {
//                if (shopIds.contains(shopMessage.getShopId())) {
//                    Set<String> set = personMap.get(province);
//                    if (set == null) {
//                        personMap.put(province, Sets.newHashSet(shopMessage.getPerson().getUserId()));
//                    } else {
//                        set.add(shopMessage.getPerson().getUserId());
//                        personMap.put(province, set);
//                    }
//                }
//            });
//        });
//
//        Map<String, Set<DianPingMessage>> map = new HashMap<>();
//
//        personMap.forEach((province, personIds) -> {
//            personIds.forEach(personId -> {
//                Set<DianPingMessage> persons = personMessages
//                        .stream()
//                        .filter(personMessage -> personMessage.getPerson().getUserId().equals(personId))
////                        .map(personMessage -> personMessage.getShopId())
//                        .collect(Collectors.toSet());
//                Set<DianPingMessage> provincePersons = map.get(province);//签到信息
//                if (provincePersons == null) {
//                    map.put(province, persons);
//                } else {
//                    provincePersons.addAll(persons);
//                    map.put(province, provincePersons);
//
//                }
//            });
//        });
        reader1.close();
        reader.close();
//
        map.forEach((pro, messages) -> {
            HashMap<String, Integer> personSign = new HashMap<>();
            HashMap<String, Integer> personDianping = new HashMap<>();
            messages.forEach(message -> {
                if (message.getType().equals(MessageType.PERSON)) {
                    Integer count = personDianping.get(message.getShopName());
                    if (count == null) {
                        count = 0;
                    }
                    personDianping.put(message.getShopName(), ++count);
                } else {
                    Integer count = personSign.get(message.getShopName());
                    if (count == null) {
                        count = 0;
                    }
                    personSign.put(message.getShopName(), ++count);
                }
            });

            System.out.println();
            System.out.println("点评 ： pro = " + pro);
            List<Map.Entry<String, Integer>> list = new ArrayList<>(personDianping.entrySet());
            list.sort(Comparator.comparing(Map.Entry::getValue));
            int c = 0;
            for (int i = list.size() - 1; i >= 0 ; i--) {
                if (c > 20) {
                    break;
                }
                if (StringUtils.isBlank(list.get(i).getKey())) {
                    continue;
                }
                System.out.println(list.get(i).getKey() + "," + list.get(i).getValue());
                c++;
            }

            System.out.println();
            System.out.println("签到 ： pro = " + pro);
            List<Map.Entry<String, Integer>> list1 = new ArrayList<>(personSign.entrySet());
            list1.sort(Comparator.comparing(Map.Entry::getValue));
            int cc = 0;
            for (int i = list1.size() - 1; i >= 0 ; i--) {
                if (cc > 20) {
                    break;
                }
                if (StringUtils.isBlank(list1.get(i).getKey())) {
                    continue;
                }
                System.out.println(list1.get(i).getKey() + "," + list1.get(i).getValue());
                cc++;
            }
        });


    }
}
