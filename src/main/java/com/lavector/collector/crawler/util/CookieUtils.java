package com.lavector.collector.crawler.util;


import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class CookieUtils {

    private static List<String> list = new LinkedList<>();

    static {
        list.add("SINAGLOBAL=9262762579760.895.1542259856140; YF-V5-G0=a5a6106293f9aeef5e34a2e71f04fae4; YF-Page-G0=416186e6974c7d5349e42861f3303251; _s_tentry=passport.weibo.com; Apache=4243628976131.1772.1550561397652; ULV=1550561397693:1:1:1:4243628976131.1772.1550561397652:; Ugrow-G0=7e0e6b57abe2c2f76f677abd9a9ed65d; WBtopGlobal_register_version=2019021915; SCF=AtgnXFBDvRA-ACAFTJbJ_U2hd-M9-tcEg6lbqA47ypoCwMV8V_hZ1qsq0og5jNymSPUXG30sLM-sYcL4Burs--U.; SUB=_2A25xb8DuDeRhGeRK4lEZ8SfLyT2IHXVSHLUmrDV8PUNbmtANLRPQkW9NUzrv1hhHaGL25VfPOISWthovOFpU5qq3; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WFOeO_zx3R91oRRGZqEH8-J5JpX5K2hUgL.FozX1KeReK.Neo22dJLoI0YLxKqL1-eL1h5LxKML1hnLBo2LxKBLB.qL1-BLxK-LB.qL1KMLxK-LBonLB-BLxKML12zLBo5LxK-L1KnL1h.t; SUHB=0Ry7gx_PYedT1p; ALF=1551166269; SSOLoginState=1550561470; un=as1234560123@sina.com; wb_view_log_2493819721=1920*10801");
        list.add("SINAGLOBAL=9262762579760.895.1542259856140; YF-V5-G0=b8115b96b42d4782ab3a2201c5eba25d; YF-Page-G0=0dccd34751f5184c59dfe559c12ac40a; _s_tentry=passport.weibo.com; Apache=4369735051661.345.1550561777676; ULV=1550561777699:1:1:1:4369735051661.345.1550561777676:; Ugrow-G0=8751d9166f7676afdce9885c6d31cd61; WBtopGlobal_register_version=ae9a9ec008078a68; SCF=AtgnXFBDvRA-ACAFTJbJ_U2hd-M9-tcEg6lbqA47ypoCIO06j9E8m8eRHeCRRl4PAU3-B0XSXkoFqTHNw-C-WWk.; SUB=_2A25xb8GuDeRhGeRN4lAS-SvLzTuIHXVSHLRmrDV8PUNbmtAKLXTQkW9NU22WP0zJELJJwHyZthqvPEMzAJo5dzKl; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9W5dnm3.C0RMVCEw2-fZ0MVH5JpX5K2hUgL.Foz01Kz01K-NSoM2dJLoI0YLxKMLB.BL1K2LxKqL1-BLBK-LxKMLB.BL1K2LxK-L12-LB.zLxKnLBo5L1hnLxK-LBKMLB.eLxKML1KBLBo5t; SUHB=0z1RyhinDMXz8n; ALF=1551166590; SSOLoginState=1550561790; un=asd21343@sina.com; wb_view_log_2392395767=1920*10801");
        list.add("SINAGLOBAL=9262762579760.895.1542259856140; YF-V5-G0=f0aa2e6d566ccd1c288fae19df01df56; YF-Page-G0=f3b64f1a7830d84b4697ff4a88f85125; _s_tentry=passport.weibo.com; Apache=800259979691.6155.1550561882533; ULV=1550561882556:1:1:1:800259979691.6155.1550561882533:; Ugrow-G0=ea90f703b7694b74b62d38420b5273df; WBtopGlobal_register_version=ae9a9ec008078a68; SCF=AtgnXFBDvRA-ACAFTJbJ_U2hd-M9-tcEg6lbqA47ypoCF6fIY74eIjEACCWYWn5s9yR_Pzl0crYUU32mp_DXj4Q.; SUB=_2A25xb8I4DeRhGedO7FcQ9yzOzjiIHXVSHLTwrDV8PUNbmtAKLUb4kW9NXPXQ50aVg2BXCZxPllarXQDKAdo9cjcu; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WWoUmiH.ZZJ89NOS-k363eR5JpX5K2hUgL.Fo27S0-pS0zESKB2dJLoIEXLxK-LBoBL1hqLxK-LBKBLBK.LxK-LBK5L1hzLxK.LBonL1-qLxK.LBonL1-qt; SUHB=0z1RM5zKjMXz8k; ALF=1551166696; SSOLoginState=1550561897; un=anyegucheng@sina.com; wb_view_log_1075172254=1920*10801");
    }

    public static String getCookie(){
        Random random = new Random();
        int nextInt = random.nextInt(list.size());
        return list.get(nextInt);
    }

    public static void main(String[] args) {

        String cookie = getCookie();
        System.out.println(cookie);

    }

}


