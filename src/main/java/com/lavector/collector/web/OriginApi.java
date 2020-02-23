package com.lavector.collector.web;

import com.lavector.collector.entity.source.Origin;
import com.lavector.collector.entity.source.OriginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created on 18/10/2017.
 *
 * @author seveniu
 */
@RestController
@RequestMapping("/api/origin")
@CrossOrigin
public class OriginApi {

    @Autowired
    OriginService originService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<Origin> getByCategoryId(@RequestParam(value = "cid") Long cid) {
        return originService.getByCategoryId(cid);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public Origin add(@RequestBody Origin origin) {
        return originService.add(origin);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    public Origin update(@RequestBody Origin origin) {
        return originService.update(origin);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Long id) {
        originService.delete(id);
    }
}
