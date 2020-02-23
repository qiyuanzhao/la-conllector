package com.lavector.collector.entity.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created on 01/11/2017.
 *
 * @author seveniu
 */
@Service
public class ConfigServiceImpl implements ConfigService {
    @Autowired
    ConfigRepository configRepository;

    @Override
    public Config findByName(String name) {
        return configRepository.findByName(name);
    }

    @Override
    public Config save(Config config) {
        config.setTimeCreated(new Date());
        return configRepository.save(config);
    }

    @Override
    public Config update(Config config) {
        config.setTimeUpdated(new Date());
        return configRepository.save(config);
    }
}
