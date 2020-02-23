package com.lavector.collector.web;

import com.lavector.collector.entity.category.Category;
import com.lavector.collector.entity.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created on 18/10/2017.
 *
 * @author seveniu
 */
@CrossOrigin
@RestController
@RequestMapping("/api/category")
//@CrossOrigin(origins = {"http://localhost:9528"}, methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.POST, RequestMethod.PUT})
public class CategoryApi {
    @Autowired
    CategoryService categoryService;

    @CrossOrigin
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Category> getNews() {
        return categoryService.getAll();
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public Category add(@RequestBody Category category) {
        return categoryService.add(category);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public Category update(@RequestBody Category category) {
        return categoryService.update(category);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Long id) {
        categoryService.delete(id);
    }
}
