package com.lavector.collector.entity.source;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created on 18/10/2017.
 *
 * @author seveniu
 */
@Service
public class OriginServiceImpl implements OriginService {
    @Autowired
    OriginRepository repository;

    @Override
    public List<Origin> getByCategoryId(Long cid) {
        return repository.findByCategoryId(cid);
    }

    @Override
    public Origin getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Origin add(Origin origin) {
        origin.setTimeCreated(new Date());
        return repository.save(origin);
    }

    @Override
    public Origin update(Origin origin) {
        return repository.save(origin);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
