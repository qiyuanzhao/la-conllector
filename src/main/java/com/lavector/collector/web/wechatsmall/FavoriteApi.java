package com.lavector.collector.web.wechatsmall;

import com.lavector.collector.entity.wechatSmall.brand.BrandService;
import com.lavector.collector.entity.wechatSmall.favorite.Favorite;
import com.lavector.collector.entity.wechatSmall.favorite.FavoriteService;
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
@RequestMapping("/api/wsa/favorite")
@CrossOrigin
public class FavoriteApi {

    @Autowired
    FavoriteService favoriteService;

    @Autowired
    BrandService brandService;

    @RequestMapping(value = "/{uid}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Favorite> getNews(@PathVariable("uid") String uid) {
        return favoriteService.getByUserId(uid);
    }
}
