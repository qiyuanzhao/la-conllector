package com.lavector.collector.entity.wechatSmall.brand;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created on 22/12/2017.
 *
 * @author seveniu
 */
public interface BrandService {
    List<Brand> findByCategoryId(Long categoryId);

    Page<Brand> findAll(Pageable pageable);

    List<Brand> findAll();

    Brand findById(Long id);
}
