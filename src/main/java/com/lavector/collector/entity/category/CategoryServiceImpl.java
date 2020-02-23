package com.lavector.collector.entity.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created on 18/10/2017.
 *
 * @author seveniu
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public Category getById(Long id) {
        return categoryRepository.getOne(id);
    }

    @Override
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    @Override
    public List<Category> getFirstLevel() {
        return categoryRepository.findByPid(0L);
    }

    @Override
    public List<Category> getChildren(Long pid) {
        return categoryRepository.findByPid(pid);
    }

    @Override
    public List<Category> getAllChildren(Long pid) {
        List<Category> allChildren = new LinkedList<>();
        getChildrenS(pid, allChildren);
        return allChildren;
    }

    private void getChildrenS(Long pid, List<Category> categories) {
        List<Category> children = categoryRepository.findByPid(pid);
        if (children.size() > 0) {
            categories.addAll(children);
            for (Category child : children) {
                getChildrenS(child.getId(), categories);
            }
        }
    }

    @Override
    public Category add(Category category) {
        category.setTimeCreated(new Date());
        return categoryRepository.save(category);
    }

    @Override
    public Category update(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}
