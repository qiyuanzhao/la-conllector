package com.lavector.collector.crawler.project.yanying.bilibili;


import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.scheduler.PriorityScheduler;
import us.codecraft.webmagic.scheduler.QueueScheduler;

/**
 * Created on 27/04/2018.
 *
 * @author zeng.zhao
 */
public class MyScheduler extends QueueScheduler {

    @Override
    public void pushWhenNoDuplicate(Request request, Task task) {
        super.pushWhenNoDuplicate(request, task);
        System.out.println("total : " + getTotalRequestsCount(task));
        System.out.println("left : " + getLeftRequestsCount(task));
    }
}
