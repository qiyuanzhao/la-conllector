package com.lavector.collector.email;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.tools.generic.DateTool;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

/**
 * Created by zeng.zhao on 2017/10/11
 */
@Service
public class EmailTemplateServiceImpl implements EmailTemplateService {

    static {
        Properties p = new Properties();
        p.setProperty("resource.loader", "class");
        p.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(p);
    }

    @Override
    public String mergeIntoTemplate(String templatePath, Map config) {
        VelocityContext context = new VelocityContext(config);
        context.put("date", new DateTool());
        StringWriter result = new StringWriter();
        Template template = Velocity.getTemplate(templatePath, "UTF-8");
        template.merge(context, result);
        return result.toString();
    }
}
