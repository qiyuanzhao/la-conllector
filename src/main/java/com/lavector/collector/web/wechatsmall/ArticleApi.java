package com.lavector.collector.web.wechatsmall;

import com.lavector.collector.entity.BaseEntity;
import com.lavector.collector.entity.wechatSmall.article.Article;
import com.lavector.collector.entity.wechatSmall.article.ArticleService;
import com.lavector.collector.entity.wechatSmall.brand.Brand;
import com.lavector.collector.entity.wechatSmall.brand.BrandService;
import com.lavector.collector.entity.wechatSmall.shop.Shop;
import com.lavector.collector.entity.wechatSmall.shop.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created on 22/12/2017.
 *
 * @author seveniu
 */
@RestController
@RequestMapping("/api/wsa/article")
@CrossOrigin
public class ArticleApi {

    @Autowired
    ArticleService articleService;

    @Autowired
    BrandService brandService;
    @Autowired
    ShopService shopService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Article getNews(@PathVariable("id") Long id) {
        return articleService.getById(id);
    }

    @RequestMapping(value = "/{plaza}/{category}/{type}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Page<ArticleDTO> getNews(@PathVariable("plaza") Long plazaId, @PathVariable("category") Long category, @PathVariable("type") String type, Pageable pageable) {
        List<Shop> shops = shopService.getByPlazaId(plazaId);
        if (category < 0) {
            if (category == -1) { // 精选文章
                Page<Article> page = articleService.findByScoreThan(9, pageable);
                return convertToDTO(page, shops, pageable);
            } else {
                throw new IllegalArgumentException("unknow category :" + category);
            }
        } else {

            Set<Brand> brandSet = shops.stream().map(Shop::getBrand).collect(Collectors.toSet());
            List<Long> brands = brandSet.stream()
                    .filter(brand -> brand.getCategory().getId().equals(category))
                    .map(BaseEntity::getId)
                    .collect(Collectors.toList());
            Page<Article> byBrands = articleService.findByBrands(brands, pageable);
            return convertToDTO(byBrands, shops, pageable);
        }
    }

    private Page<ArticleDTO> convertToDTO(Page<Article> articlePage, List<Shop> shops, Pageable pageable) {

        List<ArticleDTO> lists = new ArrayList<>(articlePage.getNumber());
        for (Article byBrand : articlePage) {
            ArticleDTO articleDTO = new ArticleDTO();
            articleDTO.article = byBrand;
            for (Shop shop : shops) {
                if (shop.getBrand().getId().equals(byBrand.getBrandId())) {
                    articleDTO.shop = shop;
                }
            }
            lists.add(articleDTO);
        }
        return new PageImpl<>(lists, pageable, articlePage.getTotalElements());
    }

    @RequestMapping(value = "/brand/{brandId}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Page<Article> getNews(@PathVariable("brandId") Long brandId, Pageable pageable) {
        return articleService.findByBrand(brandId, pageable);
    }

    public static class ArticleDTO {
        public Article article;
        public Shop shop;
    }
}
