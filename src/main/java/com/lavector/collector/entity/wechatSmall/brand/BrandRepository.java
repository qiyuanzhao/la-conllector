package com.lavector.collector.entity.wechatSmall.brand;

import com.lavector.collector.entity.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created on 22/12/2017.
 *
 * @author seveniu
 */
public interface BrandRepository extends JpaRepository<Brand, Long> {
    List<Brand> findByCategory(Category category);
}
