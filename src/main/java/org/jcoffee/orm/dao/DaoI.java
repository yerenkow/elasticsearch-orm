package org.jcoffee.orm.dao;

import org.jcoffee.orm.base.EntityBuilderI;

import java.util.List;
import java.util.Map;

public interface DaoI {

    <T> boolean save(T object, EntityBuilderI builder);

    <T> boolean update(T object, EntityBuilderI builder);

    boolean delete(String id, EntityBuilderI builder);

    void delete(Map<String, Object> queryParams, EntityBuilderI builder);

    <T> T getById(String id, EntityBuilderI<T> builder);

    <T> List<T> getByQuery(Map<String, Object> queryParams, EntityBuilderI builder);
}
