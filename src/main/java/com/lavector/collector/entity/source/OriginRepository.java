package com.lavector.collector.entity.source;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created on 20/10/2017.
 *
 * @author seveniu
 */
public interface OriginRepository extends JpaRepository<Origin, Long> {
    List<Origin> findByCategoryId(Long cid);
}
