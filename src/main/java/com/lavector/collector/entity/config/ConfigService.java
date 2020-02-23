package com.lavector.collector.entity.config;

/**
 * Created on 01/11/2017.
 *
 * @author seveniu
 */
public interface ConfigService {
    Config findByName(String name);
    Config save(Config config);

    Config update(Config config);
}
