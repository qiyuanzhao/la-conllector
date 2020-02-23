package com.lavector.collector.entity.wechatSmall.plaza;

import java.util.List;

/**
 * Created on 22/12/2017.
 *
 * @author seveniu
 */
public interface PlazaService {
    List<Plaza> getAll();

    Plaza getNearest(double lat, double lng);
}
