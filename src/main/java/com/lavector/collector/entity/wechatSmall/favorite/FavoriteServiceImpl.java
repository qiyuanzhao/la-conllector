package com.lavector.collector.entity.wechatSmall.favorite;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created on 22/12/2017.
 *
 * @author seveniu
 */
@Service
public class FavoriteServiceImpl implements FavoriteService {
    @Autowired
    private FavoriteRepository favoriteRepository;

    @Override
    public List<Favorite> getByUserId(String uid) {
        return favoriteRepository.findByUid(uid);
    }

    @Override
    public Favorite addFavorite(Favorite favorite) {
        return favoriteRepository.save(favorite);
    }
}
