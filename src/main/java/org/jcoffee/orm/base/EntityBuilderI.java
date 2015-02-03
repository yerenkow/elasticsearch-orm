package org.jcoffee.orm.base;

import java.util.Map;

/**
 * Created by Aleksandr Simonchuk on 31.01.15.
 */


public interface EntityBuilderI<T> {
    T buildFromMap(Map<String, Object> map);

    Map<String, Object> buildToMap(T object);

    String getIndexName();

    String getTypeName();
}
