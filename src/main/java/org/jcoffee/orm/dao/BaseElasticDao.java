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

    private final String host;
    private final int port;
    private final Map<String, String> settings;

    public BaseElasticDao(String host, int port, Map<String, String> settings) {
        this.host = host;
        this.port = port;
        this.settings = settings;
    }

    private BaseElasticClient getInstance() {
        return ElasticClientFactory.getInstance(host, port, settings);
    }

    @Override
    public <T> boolean save(T object, EntityBuilderI builder) {
        return getInstance().index(builder.getIndexName(), builder.getTypeName(), null, builder.buildToMap(object));
    }

    @Override
    public <T> boolean update(T object, EntityBuilderI builder) {
        return getInstance().update(builder.getIndexName(), builder.getTypeName(), builder.buildToMap(object));
    }

    @Override
    public boolean delete(String id, EntityBuilderI builder) {
        return getInstance().delete(builder.getIndexName(), builder.getTypeName(), id);
    }

    @Override
    public void delete(Map<String, Object> queryParams, EntityBuilderI builder) {
        getInstance().delete(builder.getIndexName(), builder.getTypeName(), queryParams);
    }

    @Override
    public <T> T getById(String id, EntityBuilderI<T> builder) {
        Map<String, Object> stringObjectMap = getInstance().getById(builder.getIndexName(), builder.getTypeName(), id);
        if (stringObjectMap == null) {
            return null;
        }
        return builder.buildFromMap(stringObjectMap);
    }

    @Override
    public <T> List<T> getByQuery(Map<String, Object> queryParams, EntityBuilderI builder) {
        List<Map<String, Object>> maps = getInstance().getByQuery(builder.getIndexName(), builder.getTypeName(), queryParams);
        List<T> tList = new ArrayList<>(maps.size());
        for (Map<String, Object> stringObjectMap : maps) {
            tList.add((T) builder.buildFromMap(stringObjectMap));
        }
        return tList;
    }

}
