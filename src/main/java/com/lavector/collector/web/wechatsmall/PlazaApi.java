package com.lavector.collector.web.wechatsmall;

import com.lavector.collector.entity.wechatSmall.plaza.Plaza;
import com.lavector.collector.entity.wechatSmall.plaza.PlazaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created on 22/12/2017.
 *
 * @author seveniu
 */
@RestController
@RequestMapping("/api/wsa/plaza")
@CrossOrigin
public class PlazaApi {
    @Autowired
    PlazaService plazaService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Plaza> getNews() {
        return plazaService.getAll();
    }

    @RequestMapping(value = "/nearest", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Plaza getNews(@RequestParam("latitude") Double latitude, @RequestParam("longitude") Double longitude) {
        return plazaService.getNearest(latitude, longitude);
    }
}
