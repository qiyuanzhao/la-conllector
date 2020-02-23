package com.lavector.collector.crawler.project.edu;

import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;

/**
 * Created on 2017/11/15.
 *
 * @author zeng.zhao
 */
public interface Keywords {

    Date targetDate = DateUtils.addMonths(new Date(), -12);

    String[] keywords = new String[]
            {
//                    "幼升小",
//                    "幼小衔接",
                    "兴趣班",
//                    "小学面试",
//                    "学区",
//                    "幼儿园毕业"
            };

}
