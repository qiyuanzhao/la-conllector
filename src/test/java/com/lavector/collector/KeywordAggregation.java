package com.lavector.collector;

import com.lavector.collector.crawler.util.ReadTextUtils;
import com.lavector.collector.entity.readData.SkuData;

import java.util.List;

/**
 * Created by qyz on 2019/8/26.
 */
public class KeywordAggregation {

    public static void main(String[] args) {
//SINAGLOBAL=9262762579760.895.1542259856140; UOR=,,login.sina.com.cn; un=odfk75ex@anjing.cool; wvr=6; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WhhcleUFdgXq15.EXFGBKCr5JpX5KMhUgL.FoM0eoecS0.R1KB2dJLoIERLxKML12zLBoyki--Ni-2ci-iFi--fi-isiKn0i--fiK.EiK.Ei--Xi-zRi-zc; webim_unReadCount=%7B%22time%22%3A1577931395556%2C%22dm_pub_total%22%3A2%2C%22chat_group_client%22%3A0%2C%22allcountNum%22%3A33%2C%22msgbox%22%3A0%7D; ALF=1609469022; SSOLoginState=1577933023; SCF=AtgnXFBDvRA-ACAFTJbJ_U2hd-M9-tcEg6lbqA47ypoCGIU9W-D94Qva_r7lyASIELiCKFAgeot0iVhPjQGILaI.; SUB=_2A25zCSiPDeRhGeFN6VEX9yfEwjiIHXVQfx1HrDV8PUNbmtAKLW34kW9NQEcmI5P-OWbE3jvx1Hid3Vnj1ediKGrW; SUHB=0U1ZAVnQ4HN2l0; _s_tentry=weibo.com; Apache=1903420621650.609.1577933032612; ULV=1577933032629:13:2:3:1903420621650.609.1577933032612:1577931392435
        List<SkuData> skuDatas = ReadTextUtils.getSkuData("G:/text/newWeibo/qqq.txt", ",");

        String keywods = "";

        int code = 0;
        for (SkuData skuData : skuDatas) {
            String brand = skuData.getBrand();

            keywods += /*"\"" +*/ brand /*+ "\""*/ + ",";
//            keywods += brand + ",";
            if (code > 40) {
                System.out.println(keywods);
                keywods = "";
                code = 0;
                continue;
            }
            code++;

        }
        System.out.println(keywods);

    }


}
