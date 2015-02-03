package org.jcoffee.orm.base;

import java.util.Map;

/**
 * Created by Aleksandr Simonchuk on 31.01.15.
 */
public interface TransformerI<T> {
    T fromMap(Map<String, Object> map);

    Map<String, Object> toMap(T object);
}
