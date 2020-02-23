package com.lavector.collector.crawler.project.tmall;

import com.lavector.collector.crawler.util.ExcelTest;
import com.lavector.collector.crawler.util.ExcelUtil;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created on 2018/11/18.
 *
 * @author zeng.zhao
 */
public class TmallConfig implements Serializable {

    private static final long serialVersionUID = 2703285700370707027L;
    private String category;

    private String brand;

    private String product;

    private String url;

    private TmallConfig(String category, String brand, String product, String url) {
        this.category = category;
        this.brand = brand;
        this.product = product;
        this.url = url;
    }

    public String getCategory() {
        return category;
    }

    public String getBrand() {
        return brand;
    }

    public String getProduct() {
        return product;
    }

    public String getUrl() {
        return url;
    }

    private static List<TmallConfig> tmallConfigs = new ArrayList<>();

    static {
//        tmallConfigs.add(new TmallConfig("Hot", "西麦", "西麦纯燕麦片", "https://detail.tmall.com/item.htm?spm=a1z10.3-b-s.w4011-14681897648.73.1ca8aa2e0rJsAc&id=534062342920&rn=ec0dff629df6da0ce74b289f309ce07e&abbucket=12&skuId=4028023926055"));
//        tmallConfigs.add(new TmallConfig("Hot", "西麦", "西麦原味牛奶燕麦片", "https://detail.tmall.com/item.htm?spm=a1z10.3-b-s.w4011-14681897648.88.5f8eaa2ecSJuDl&id=523000480138&rn=c506fabe168fd952c205e2320d5e58d2&abbucket=12"));
//        tmallConfigs.add(new TmallConfig("Hot", "智力", "智力燕麦片 小袋装冲饮", "https://detail.tmall.com/item.htm?spm=a1z10.5-b-s.w4011-16766627665.48.7f03753fkbvip6&id=15816406541&rn=8b7849d80f41cd52e3967522970c5f68&abbucket=12"));
//        tmallConfigs.add(new TmallConfig("Hot", "福事多", "福事多 纯燕麦片即食", "https://detail.tmall.com/item.htm?spm=a1z10.5-b-s.w4011-18815827655.63.4d701a9b317BfW&id=39327839306&rn=ce747c3a4804cbb777e60aeaa3ccfe42&abbucket=12"));
//        tmallConfigs.add(new TmallConfig("Hot", "Creme de la Cream", "克德拉克原味即食燕麦片", "https://detail.tmall.hk/hk/item.htm?spm=a220m.1000858.1000725.2.10fc208aClXzeI&id=581074246940&areaId=110100&user_id=1879183449&cat_id=2&is_b=1&rn=ecd3f881afe63eca4997c316888d3c74"));

//        tmallConfigs.add(new TmallConfig("Cold", "家乐氏", "谷兰诺拉营养小袋装", "https://detail.tmall.com/item.htm?spm=a1z10.3-b-s.w4011-15151652200.56.24b94c6bIiX2F7&id=547102176615&rn=999e35ce2e6796390a3c4906f36a0428&abbucket=7"));
//        tmallConfigs.add(new TmallConfig("Cold", "家乐氏", "谷兰诺拉谷物早餐", "https://detail.tmall.com/item.htm?spm=a1z10.3-b-s.w4011-15151652200.59.71e64c6bpXhz9g&id=529483669189&rn=a78d3616291b2413e7a6bc4267db748b&abbucket=14"));
//        tmallConfigs.add(new TmallConfig("Cold", "家乐氏", "可可球", "https://detail.tmall.com/item.htm?spm=a1z10.3-b-s.w4011-15151652200.62.71e64c6bpXhz9g&id=565090066140&rn=a78d3616291b2413e7a6bc4267db748b&abbucket=14"));
//        tmallConfigs.add(new TmallConfig("Cold", "家乐氏", "谷维滋", "https://detail.tmall.com/item.htm?spm=a1z10.3-b-s.w4011-15151652200.74.71e64c6bpXhz9g&id=522908262411&rn=a78d3616291b2413e7a6bc4267db748b&abbucket=14&skuId=3939560511333"));
//        tmallConfigs.add(new TmallConfig("Cold", "家乐氏", "玉米片", "https://detail.tmall.com/item.htm?spm=a1z10.3-b-s.w4011-15151652200.74.71e64c6bpXhz9g&id=522908262411&rn=a78d3616291b2413e7a6bc4267db748b&abbucket=14&skuId=3939560511333"));


//        tmallConfigs.add(new TmallConfig("Cold", "卡乐比", "进口水果麦片", "https://detail.tmall.hk/hk/item.htm?spm=a1z10.3-b-s.w4011-15334716812.70.681561e3yoOzsh&id=555548214750&rn=48a5afe7a9c44aa4173a96b6578b7d1e&abbucket=17"));
//        tmallConfigs.add(new TmallConfig("Cold", "卡乐比", "水果麦片苹果多多风味", "https://detail.tmall.hk/hk/item.htm?spm=a1z10.3-b-s.w4011-15334716812.160.681561e3yoOzsh&id=565189875794&rn=48a5afe7a9c44aa4173a96b6578b7d1e&abbucket=17"));
//        tmallConfigs.add(new TmallConfig("Cold", "卡乐比", "水果麦片巧克力曲奇风味", "https://detail.tmall.hk/hk/item.htm?spm=a1z10.3-b-s.w4011-15334716812.109.681561e3yoOzsh&id=560935485626&rn=48a5afe7a9c44aa4173a96b6578b7d1e&abbucket=17"));
//        tmallConfigs.add(new TmallConfig("Cold", "卡乐比", "水果麦片乳酸菌酸奶风味", "https://detail.tmall.hk/hk/item.htm?spm=a1z10.3-b-s.w4011-15334716812.217.681561e3yoOzsh&id=572112870784&rn=48a5afe7a9c44aa4173a96b6578b7d1e&abbucket=17"));
//
//        tmallConfigs.add(new TmallConfig("Cold", "ICA", "进口即食水果麦片", "https://detail.tmall.hk/hk/item.htm?spm=a220m.1000858.1000725.1.ac166a17hYEZdZ&id=542622977449&skuId=3262045672044&areaId=110100&user_id=2549841410&cat_id=2&is_b=1&rn=bdb6986fa60a28c5852812c77778149c"));
//        tmallConfigs.add(new TmallConfig("Cold", "ICA", "50%水果坚果燕麦片", "https://chaoshi.detail.tmall.com/item.htm?spm=a220m.1000858.1000725.6.ac166a17hYEZdZ&id=535725953287&areaId=110100&user_id=725677994&cat_id=2&is_b=1&rn=bdb6986fa60a28c5852812c77778149c"));
//        tmallConfigs.add(new TmallConfig("Cold", "ICA", "45%混合水果燕麦片", "https://chaoshi.detail.tmall.com/item.htm?spm=a220m.1000858.1000725.36.ac166a17hYEZdZ&id=535763648804&areaId=110100&user_id=725677994&cat_id=2&is_b=1&rn=bdb6986fa60a28c5852812c77778149c"));
//
//        tmallConfigs.add(new TmallConfig("Cold", "欧扎克", "50%水果坚果麦片自然味道", "https://detail.tmall.com/item.htm?spm=a1z10.3-b-s.w4011-14957217890.54.2cf62de2c6SPv1&id=535786796994&rn=deb6b28fd29bd9beeb5007792627f610&abbucket=14&skuId=3854904257998"));
//        tmallConfigs.add(new TmallConfig("Cold", "欧扎克", "酸奶果粒麦片", "https://detail.tmall.com/item.htm?spm=a1z10.3-b-s.w4011-14957217890.57.2cf62de2c6SPv1&id=577380472169&rn=deb6b28fd29bd9beeb5007792627f610&abbucket=14&skuId=4020836106007"));
//        tmallConfigs.add(new TmallConfig("Cold", "欧扎克", "50%水果坚果麦片小麦薄脆片", "https://detail.tmall.com/item.htm?spm=a1z10.3-b-s.w4011-14957217890.60.2cf62de2c6SPv1&id=561338852782&rn=deb6b28fd29bd9beeb5007792627f610&abbucket=14&skuId=3657411852651"));
//
//        tmallConfigs.add(new TmallConfig("Cold", "五谷磨房", "奇亚籽谷物燕麦片", "https://detail.tmall.com/item.htm?spm=a1z10.3-b-s.w4011-14470423595.74.2c7e6d83JFBrdi&id=550094534789&rn=2fb5ed0834698211e9a0df5fe9ea1d9e&abbucket=14&skuId=3513920359970"));

//        tmallConfigs.add(new TmallConfig("Cold", "杂粮先生", "混合坚果大燕麦片", "https://detail.tmall.com/item.htm?spm=a1z10.3-b-s.w4011-16123602526.46.40113520wlLRwk&id=527818104230&rn=68cd09278b83c16b6bd5d907d3067211&abbucket=14"));
//        tmallConfigs.add(new TmallConfig("Cold", "杂粮先生", "水果麦片", "https://detail.tmall.com/item.htm?spm=a1z10.3-b-s.w4011-16123602526.52.40113520wlLRwk&id=535698610587&rn=68cd09278b83c16b6bd5d907d3067211&abbucket=14"));
//
//        tmallConfigs.add(new TmallConfig("CPD", "西麦", "奶香营养燕麦片", "https://detail.tmall.com/item.htm?spm=a1z10.3-b-s.w4011-14681897648.84.1a6faa2e3Bqzz2&id=520116264956&rn=f386b1d37d627e9be687e38277d5b1f7&abbucket=14"));
//        tmallConfigs.add(new TmallConfig("CPD", "西麦", "特浓牛奶营养燕麦片", "https://detail.tmall.com/item.htm?spm=a1z10.3-b-s.w4011-14681897648.121.1a6faa2e3Bqzz2&id=522972463387&rn=f386b1d37d627e9be687e38277d5b1f7&abbucket=14&skuId=3862126945184"));
//        tmallConfigs.add(new TmallConfig("CPD", "西麦", "特浓牛奶营养燕麦片", "https://detail.tmall.com/item.htm?spm=a1z10.3-b-s.w4011-14681897648.141.1a6faa2e3Bqzz2&id=45694705161&rn=f386b1d37d627e9be687e38277d5b1f7&abbucket=14"));
//        tmallConfigs.add(new TmallConfig("CPD", "西麦", "红枣高铁营养燕麦片", "https://detail.tmall.com/item.htm?spm=a1z10.3-b-s.w4011-14681897648.157.1a6faa2e3Bqzz2&id=18254562587&rn=f386b1d37d627e9be687e38277d5b1f7&abbucket=14"));
//        tmallConfigs.add(new TmallConfig("CPD", "西麦", "果奶营养燕麦片", "https://detail.tmall.com/item.htm?spm=a1z10.3-b-s.w4011-14681897648.225.1a6faa2e3Bqzz2&id=24897232035&rn=f386b1d37d627e9be687e38277d5b1f7&abbucket=14"));
//        tmallConfigs.add(new TmallConfig("CPD", "西麦", "学生成长营养燕麦片", "https://detail.tmall.com/item.htm?spm=a1z10.3-b-s.w4011-14681897648.234.1a6faa2e3Bqzz2&id=45339829526&rn=f386b1d37d627e9be687e38277d5b1f7&abbucket=14"));
//
//        tmallConfigs.add(new TmallConfig("CPD", "金味", "营养麦片家庭装", "https://detail.tmall.com/item.htm?spm=a1z10.3-b-s.w4011-17278082337.42.20d64efc8vDcyZ&id=537495535171&rn=51de92950595905c0ad017b8ad4f7bc6&abbucket=14"));
//        tmallConfigs.add(new TmallConfig("CPD", "金味", "营养麦片原味", "https://detail.tmall.com/item.htm?spm=a1z10.3-b-s.w4011-17278082337.45.7cf44efcRrJxSp&id=537636737767&rn=6cc0546b860a6c67e50a626ace927dac&abbucket=14"));
//
//        tmallConfigs.add(new TmallConfig("CPD", "雀巢", "优麦", "https://detail.tmall.com/item.htm?spm=a1z10.5-b-s.w4011-14449664578.87.6caa5a13024LN4&id=12673535239&rn=93fd52d8995b3fc9934dc39a3758d699&abbucket=14"));
//        tmallConfigs.add(new TmallConfig("CPD", "雀巢", "优麦牛奶香芋黑芝麻三口味", "https://detail.tmall.com/item.htm?spm=a1z10.3-b-s.w4011-14449664542.87.6e6c195ffRlmv3&id=536394443662&rn=90fdbfa7a94f9ab6dd955e05d28f043a&abbucket=14"));
//        tmallConfigs.add(new TmallConfig("CPD", "雀巢", "优麦牛奶燕麦片", "https://detail.tmall.com/item.htm?spm=a1z10.3-b-s.w4011-14449664542.90.6e6c195ffRlmv3&id=39363479779&rn=90fdbfa7a94f9ab6dd955e05d28f043a&abbucket=14"));
//        tmallConfigs.add(new TmallConfig("CPD", "雀巢", "优麦紫薯香芋燕麦片", "https://detail.tmall.com/item.htm?spm=a1z10.3-b-s.w4011-14449664542.102.6e6c195ffRlmv3&id=523168964622&rn=90fdbfa7a94f9ab6dd955e05d28f043a&abbucket=14"));
//        tmallConfigs.add(new TmallConfig("CPD", "雀巢", "优麦紫薯香芋+黑米黑芝麻燕麦片", "https://detail.tmall.com/item.htm?spm=a1z10.3-b-s.w4011-14449664542.132.6e6c195ffRlmv3&id=545485484014&rn=90fdbfa7a94f9ab6dd955e05d28f043a&abbucket=14"));
//
//        tmallConfigs.add(new TmallConfig("CPD", "五谷磨房", "提子燕麦片", "https://detail.tmall.com/item.htm?spm=a1z10.3-b-s.w4011-14470423595.71.2c7e6d83JFBrdi&id=19673324092&rn=2fb5ed0834698211e9a0df5fe9ea1d9e&abbucket=14&skuId=3104199655619"));
//        tmallConfigs.add(new TmallConfig("CPD", "五谷磨房", "芒果燕麦片", "https://detail.tmall.com/item.htm?spm=a1z10.3-b-s.w4011-14470423595.80.1da66d835yk8Ta&id=18622396331&rn=2eb01c88b9561d9ad7c82d404f5ed654&abbucket=14&skuId=3104212494967"));
//        tmallConfigs.add(new TmallConfig("CPD", "五谷磨房", "椰子燕麦片", "https://detail.tmall.com/item.htm?spm=a1z10.3-b-s.w4011-14470423595.98.1da66d835yk8Ta&id=549327888088&rn=2eb01c88b9561d9ad7c82d404f5ed654&abbucket=14"));
    }

    public static List<TmallConfig> getTmallConfigs() throws IOException {
        List<TmallConfig> tmallConfigs = new ArrayList<>();
        List<Map<String, List<Map<String, Object>>>> list = ExcelUtil.readExcel(Paths.get("/Users/zeng.zhao/Desktop/tmall-config.xlsx").toFile());
        list.forEach(exc ->
                exc.forEach((sheetName, rowList) -> {
                    rowList.forEach(rowMap -> {
                        tmallConfigs.add(new TmallConfig(rowMap.get("Category").toString(), rowMap.get("Brand").toString(), rowMap.get("Product").toString(), rowMap.get("URL").toString()));
                    });
                }));
        return tmallConfigs;
    }

    static List<TmallShopConfig> getTmallShopConfigs() throws IOException {
        List<TmallShopConfig> tmallShopConfigs = new ArrayList<>();
        List<Map<String, List<Map<String, Object>>>> list = ExcelUtil.readExcel(Paths.get("/Users/zeng.zhao/Desktop/tmall-config.xlsx").toFile());
        list.forEach(exc ->
                exc.forEach((sheetName, rowList) -> {
                    rowList.forEach(rowMap -> {
                        TmallShopConfig tmallShopConfig = new TmallShopConfig();
                        tmallShopConfig.setBrand(rowMap.get("brand").toString());
                        tmallShopConfig.setUrl(rowMap.get("url").toString());

                        Object keyword = rowMap.get("keyword");
                        if (keyword != null) {
                            tmallShopConfig.keywords.addAll(Arrays.asList(keyword.toString().split(",")));
                        }

                        tmallShopConfigs.add(tmallShopConfig);
                    });
                }));
        return tmallShopConfigs;
    }

    public static class TmallShopConfig implements Serializable {
        private static final long serialVersionUID = 4260815203931474566L;
        private String brand;
        private String url;
        private List<String> keywords = new ArrayList<>();

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public List<String> getKeywords() {
            return keywords;
        }

        public void addKeyword(String keyword) {
            this.keywords.add(keyword);
        }
    }
}

