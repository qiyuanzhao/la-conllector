package com.lavector.collector.entity.wechatSmall.shop;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created on 22/12/2017.
 *
 * @author seveniu
 */
@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {
    List<Shop> getByPlazaId(Long plazaId);

    List<Shop> getByPlazaIdAndBrandId(Long plazaId, Long brandId);
}
