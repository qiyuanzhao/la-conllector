package com.lavector.collector.email;

import java.util.Map;

/**
 * Created by zeng.zhao on 2017/10/11
 */
public interface EmailTemplateService {

    String mergeIntoTemplate(String template, Map config);
}
