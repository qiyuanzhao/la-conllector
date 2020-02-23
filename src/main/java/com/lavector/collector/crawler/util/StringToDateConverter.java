package com.lavector.collector.crawler.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
public class StringToDateConverter extends JsonDeserializer<Date>
        implements Converter<String, Date> {
    private static String[] patterns = new String[]{
            "yyyyMMdd", " ", "yyyy-MM-dd",
            "yyyyMMdd'T'HH:mm:ss", "yyyyMMdd'T'HH:mm", "yyyyMMdd'T'HH", "yyyy-MM-dd'T'HH:mm:ss",
            "yyyyMMdd HH:mm:ss", "yyyyMMdd HH", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm",
            "yyyy-MM-dd HH", "EEE MMM d HH:mm:ss z yyyy",};

    @Override
    public Date convert(String source) {
        try {
            Date date = null;
            if (source != null && source.length() > 12 && StringUtils.isNumeric(source)) {
                date = new Date(Long.parseLong(source));
            } else {
                date = DateUtils.parseDate(source, patterns);
            }
            return date;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException {
        String text = jsonParser.getText();
        Date date = convert(text);
        return date;
    }


}
