package org.jcoffee.orm.base;

import java.util.Map;

public interface TransformerI<T> {
    T fromMap(Map<String, Object> map);

    Map<String, Object> toMap(T object);
}
