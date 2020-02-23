package com.lavector.collector.web;

import com.lavector.collector.entity.config.Config;
import com.lavector.collector.entity.config.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created on 18/10/2017.
 *
 * @author seveniu
 */
@CrossOrigin
@RestController
@RequestMapping("/api/config")
public class ConfigApi {
    @Autowired
    ConfigService configService;

    @RequestMapping(value = "/{name}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Config add(@PathVariable("name") String name) {
        return configService.findByName(name);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public Config add(@RequestBody Config config) {
        return configService.save(config);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public Config update(@RequestBody Config config) {
        return configService.update(config);
    }

//    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
//    public void delete(@PathVariable("id") Long id) {
//        configService.delete(id);
//    }
}
