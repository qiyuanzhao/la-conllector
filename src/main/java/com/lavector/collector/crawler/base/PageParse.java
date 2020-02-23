package com.lavector.collector.crawler.base;


import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;

import java.io.IOException;
import java.util.*;

import static com.lavector.collector.crawler.base.RequestExtraKey.KEY_PAGE_NAME;


/**
 * Created on 10/10/16.
 *
 */

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public interface PageParse {


    default <T> T getRequestExtra(String key, Page page) {
        return (T) page.getRequest().getExtra(key);
    }

    default boolean handleRequest(Request request) {
        if (request.getExtra(KEY_PAGE_NAME) != null) {
            Object type = request.getExtra(KEY_PAGE_NAME);
            if (type instanceof String) {
                return type.equals(pageName());
            }
        }
        return handleUrl(request.getUrl());
    }

    boolean handleUrl(String url);

    Result parse(Page page);

    String pageName();

    class Result {
        public static Result get() {
            return new Result();
        }

        List<Request> requests = new LinkedList<>();
        Map<Class, List<Object>> dataMap = new HashMap<>();

        public void addRequest(Request request) {
            requests.add(request);
        }

        public synchronized Result addRequests(Collection<Request> requests) {
            for (Request request : requests) {
                if (request.getUrl() == null) {
                    throw new IllegalArgumentException("url is null");
                }
            }
            this.requests.addAll(requests);
            return this;
        }

        public List<Request> getRequests() {
            return requests;
        }

        public Result setRequests(List<Request> requests) {
            this.requests = requests;
            return this;
        }

        public synchronized Result addData(Object obj) {
            Class clazz = obj.getClass();
            if (obj.getClass().equals(clazz)) {
                if (dataMap.containsKey(clazz)) {
                    dataMap.get(clazz).add(obj);
                } else {
                    LinkedList<Object> linkedList = new LinkedList<>();
                    linkedList.add(obj);
                    dataMap.put(clazz, linkedList);
                }
            }
            return this;
        }

        public <T> List<T> getDatas(Class<T> clazz) {
            return (List<T>)dataMap.get(clazz);
        }
    }

    class ResultData<T> {
    }
}
