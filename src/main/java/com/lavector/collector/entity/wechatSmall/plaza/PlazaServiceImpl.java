package com.lavector.collector.entity.wechatSmall.plaza;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created on 22/12/2017.
 *
 * @author seveniu
 */
@Service
public class PlazaServiceImpl implements PlazaService {
    @Autowired
    PlazaRepository plazaRepository;
    @Override
    public List<Plaza> getAll() {
        return plazaRepository.findAll();
    }

    @Override
    public Plaza getNearest(double lat, double lng) {
        List<Plaza> all = getAll();
        double nearest = Double.MAX_VALUE;
        Plaza nearestPlaza = null;
        for (Plaza plaza : all) {
            double distance = DistanceUtil.getDistance(lat, lng, plaza.getLatitude(), plaza.getLongitude());
            if (distance < nearest) {
                nearest = distance;
                nearestPlaza = plaza;
            }
        }
        return nearestPlaza;
    }
}
