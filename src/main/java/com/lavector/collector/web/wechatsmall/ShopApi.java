package com.lavector.collector.web.wechatsmall;

import com.lavector.collector.entity.wechatSmall.brand.BrandService;
import com.lavector.collector.entity.wechatSmall.shop.Shop;
import com.lavector.collector.entity.wechatSmall.shop.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created on 22/12/2017.
 *
 * @author seveniu
 */
@RestController
@RequestMapping("/api/wsa/shop")
@CrossOrigin
public class ShopApi {

    @Autowired
    ShopService shopService;

    @Autowired
    BrandService brandService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Shop getNews(@PathVariable("id") Long id) {
        Shop byId = shopService.getById(id);
        return byId;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Shop> getShopByPlazaAndBrand(@RequestParam("plazaId") Long plazaId,@RequestParam("brandId")Long brandId) {
        List<Shop> byId = shopService.getByPlazaAndBrand(plazaId, brandId);
        return byId;
    }
}
