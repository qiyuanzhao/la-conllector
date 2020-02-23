package com.lavector.collector.thirdpartyapi.weixin;

import com.lavector.collector.thirdpartyapi.weixin.entity.ApiEntity;
import com.lavector.collector.thirdpartyapi.weixin.entity.ArticleEntity;

import java.util.List;

/**
 * Created on 2018/1/4.
 *
 * @author zeng.zhao
 */
public interface WeiXinApiService {
    String getArticleJson(String wechatId, int pageNum, int pageSize);

    ApiEntity getArticleEntityList(String wechatId, int pageNum, int pageSize);
}
