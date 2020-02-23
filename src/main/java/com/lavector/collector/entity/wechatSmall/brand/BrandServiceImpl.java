package com.lavector.collector.entity.wechatSmall.brand;

import com.lavector.collector.entity.category.Category;
import com.lavector.collector.entity.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

/**
 * Created on 22/12/2017.
 *
 * @author seveniu
 */
@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    BrandRepository brandRepository;
    @Autowired
    CategoryService categoryService;

    @Override
    public List<Brand> findByCategoryId(Long categoryId) {
        Category category = categoryService.getById(categoryId);
        if (category == null) {
            throw new EntityNotFoundException("category id : " + categoryId + " not found");
        }
        return brandRepository.findByCategory(category);
    }

    @Override
    public Page<Brand> findAll(Pageable pageable) {
        return brandRepository.findAll(pageable);
    }

    @Override
    public List<Brand> findAll() {
        return brandRepository.findAll();
    }

    @Override
    public Brand findById(Long id) {
        return brandRepository.getOne(id);
    }
}
