package com.lavector.collector.entity.wechatSmall.shop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created on 22/12/2017.
 *
 * @author seveniu
 */
@Service
public class ShopServiceImpl implements ShopService {
    @Autowired
    ShopRepository shopRepository;

    @Override
    public Shop getById(Long id) {
        return shopRepository.getOne(id);
    }

    @Override
    public List<Shop> getByPlazaId(Long plazaId) {
        return shopRepository.getByPlazaId(plazaId);
    }

    @Override
    public List<Shop> getByIds(List<Long> ids) {
        return shopRepository.findAllById(ids);
    }

    @Override
    public List<Shop> getByPlazaAndBrand(Long plazaId, Long brandId) {
        return shopRepository.getByPlazaIdAndBrandId(plazaId, brandId);
    }
}
