package org.jcoffee.orm.base;

import java.util.Map;

public interface EntityBuilderI<T> {
    T buildFromMap(Map<String, Object> map);

    Map<String, Object> buildToMap(T object);

    String getIndexName();

    String getTypeName();
}
