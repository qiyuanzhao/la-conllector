package com.lavector.collector.entity.config;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created on 19/10/2017.
 *
 * @author seveniu
 */
public interface ConfigRepository extends JpaRepository<Config, Long> {

    Config findByName(String name);
}
