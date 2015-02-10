package org.jcoffee.orm.dao;

import org.jcoffee.orm.base.EntityBuilderI;
import org.jcoffee.orm.elasticsearch.BaseElasticClient;
import org.jcoffee.orm.elasticsearch.ElasticClientFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Aleksandr Simonchuk on 31.01.15.
 */
public class BaseElasticDao implements DaoI {

    private BaseElasticClient baseElasticClient;

    public BaseElasticDao(String host, int port, Map<String, String> settings) {
        this.baseElasticClient = ElasticClientFactory.getInstance(host, port, settings);
    }

    public BaseElasticDao(BaseElasticClient baseElasticClient) {
        this.baseElasticClient = baseElasticClient;
    }

    @Override
    public <T> boolean save(T object, EntityBuilderI builder) {
        return baseElasticClient.index(builder.getIndexName(), builder.getTypeName(), null, builder.buildToMap(object));
    }

    @Override
    public <T> boolean update(T object, EntityBuilderI builder) {
        return baseElasticClient.update(builder.getIndexName(), builder.getTypeName(), builder.buildToMap(object));
    }

    @Override
    public boolean delete(String id, EntityBuilderI builder) {
        return baseElasticClient.delete(builder.getIndexName(), builder.getTypeName(), id);
    }

    @Override
    public void delete(Map<String, Object> queryParams, EntityBuilderI builder) {
        baseElasticClient.delete(builder.getIndexName(), builder.getTypeName(), queryParams);
    }

    @Override
    public <T> T getById(String id, EntityBuilderI<T> builder) {
        Map<String, Object> stringObjectMap =
                baseElasticClient.getById(builder.getIndexName(), builder.getTypeName(), id);
        if (stringObjectMap == null) {
            return null;
        }
        return builder.buildFromMap(stringObjectMap);
    }

    @Override
    public <T> List<T> getByQuery(Map<String, Object> queryParams, EntityBuilderI builder) {
        List<Map<String, Object>> maps =
                baseElasticClient.getByQuery(builder.getIndexName(), builder.getTypeName(), queryParams);
        List<T> tList = new ArrayList<>(maps.size());
        for (Map<String, Object> stringObjectMap : maps) {
            tList.add((T) builder.buildFromMap(stringObjectMap));
        }
        return tList;
    }

}
