package com.lavector.collector.entity.wechatSmall.shop;

import java.util.List;

/**
 * Created on 22/12/2017.
 *
 * @author seveniu
 */
public interface ShopService {
    Shop getById(Long id);

    List<Shop> getByPlazaId(Long plazaId);

    List<Shop> getByIds(List<Long> ids);

    List<Shop> getByPlazaAndBrand(Long plazaId, Long brandId);
}
