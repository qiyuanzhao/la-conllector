package com.lavector.collector.entity.wechatSmall.favorite;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created on 22/12/2017.
 *
 * @author seveniu
 */
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findByUid(String uid);
}
