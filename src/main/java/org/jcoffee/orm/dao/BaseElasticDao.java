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

    public BaseElasticDao(final String host, final int port, final Map<String, String> settings) {
        this.baseElasticClient = ElasticClientFactory.getInstance(host, port, settings);
    }

    public BaseElasticDao(final BaseElasticClient baseElasticClient) {
        this.baseElasticClient = baseElasticClient;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> boolean save(final T object, final EntityBuilderI builder) {
        return baseElasticClient.index(builder.getIndexName(), builder.getTypeName(), null, builder.buildToMap(object));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> boolean update(final T object, final EntityBuilderI builder) {
        return baseElasticClient.update(builder.getIndexName(), builder.getTypeName(), builder.buildToMap(object));
    }

    @Override
    public boolean delete(final String id, final EntityBuilderI builder) {
        return baseElasticClient.delete(builder.getIndexName(), builder.getTypeName(), id);
    }

    @Override
    public void delete(final Map<String, Object> queryParams, final EntityBuilderI builder) {
        baseElasticClient.delete(builder.getIndexName(), builder.getTypeName(), queryParams);
    }

    @Override
    public <T> T getById(final String id, final EntityBuilderI<T> builder) {
        final Map<String, Object> stringObjectMap =
                baseElasticClient.getById(builder.getIndexName(), builder.getTypeName(), id);
        if (stringObjectMap == null) {
            return null;
        }
        return builder.buildFromMap(stringObjectMap);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> getByQuery(final Map<String, Object> queryParams, final EntityBuilderI builder) {
        final List<Map<String, Object>> maps =
                baseElasticClient.getByQuery(builder.getIndexName(), builder.getTypeName(), queryParams);
        final List<T> tList = new ArrayList<>(maps.size());
        for (Map<String, Object> stringObjectMap : maps) {
            tList.add((T) builder.buildFromMap(stringObjectMap));
        }
        return tList;
    }

}
