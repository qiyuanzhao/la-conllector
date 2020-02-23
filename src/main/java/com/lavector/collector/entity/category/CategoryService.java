package com.lavector.collector.entity.category;

import java.util.List;

/**
 * Created on 18/10/2017.
 *
 * @author seveniu
 */
public interface CategoryService {
    Category getById(Long id);

    List<Category> getAll();

    List<Category> getFirstLevel();

    List<Category> getChildren(Long pid);

    List<Category> getAllChildren(Long pid);

    Category add(Category category);

    Category update(Category category);

    void delete(Long id);
}
