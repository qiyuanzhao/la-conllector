package com.lavector.collector.entity.wechatSmall.favorite;

import java.util.List;

/**
 * Created on 22/12/2017.
 *
 * @author seveniu
 */
public interface FavoriteService {
    List<Favorite> getByUserId(String uid);

    Favorite addFavorite(Favorite favorite);
}
