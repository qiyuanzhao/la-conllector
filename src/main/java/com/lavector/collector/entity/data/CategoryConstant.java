package com.lavector.collector.entity.data;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

/**
 * Created on 27/09/2017.
 *
 * @author seveniu
 */
public class CategoryConstant {
    public static final String MOVIE = "movie";
    public static final String SPORT = "sport";
    public static final String EGAME = "egame";

    public static List<String> getAllCategories() {
        Field[] declaredFields = CategoryConstant.class.getDeclaredFields();
        List<String> staticFields = new LinkedList<>();
        for (Field field : declaredFields) {
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                try {
                    staticFields.add((String) field.get(null));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return staticFields;
    }

}
