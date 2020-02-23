package com.lavector.collector.entity.category;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created on 19/10/2017.
 *
 * @author seveniu
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByPid(Long pid);

}
