package org.jcoffee.orm.elasticsearch;

import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.deletebyquery.DeleteByQueryRequestBuilder;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.action.index.IndexRequest.OpType.CREATE;

/**
 * Created by Aleksandr Simonchuk on 31.01.15.
 */
public class BaseElasticClient {

    private final Client client;
    private final ElasticClientConfig config;

    private static final MatchAllQueryBuilder MATCH_ALL_QUERY = QueryBuilders.matchAllQuery();

    public BaseElasticClient(final Client client, final ElasticClientConfig config) {
        this.client = client;
        this.config = config;
    }

    public boolean index(String index, String type, String id, Map<String, Object> source) {
        final IndexRequestBuilder indexRequestBuilder = client.prepareIndex(index, type, id);
        indexRequestBuilder.setSource(source).setOpType(CREATE);
        return indexRequestBuilder.execute().actionGet().isCreated();
    }

    public boolean update(String index, String type, Map<String, Object> doc) {
        final UpdateRequestBuilder updateRequestBuilder = client.prepareUpdate(index, type, doc.get("id").toString());
        updateRequestBuilder.setRetryOnConflict(config.getRetryOnConflict()).setDoc(doc);
        return !updateRequestBuilder.execute().actionGet().isCreated();
    }

    public boolean delete(String index, String type, String id) {
        final DeleteRequestBuilder deleteRequestBuilder = client.prepareDelete(index, type, id);
        return deleteRequestBuilder.execute().actionGet().isFound();
    }

    public void delete(String index, String type, Map<String, Object> queryParams) {
        final FilteredQueryBuilder filteredQueryBuilder =
                QueryBuilders.filteredQuery(MATCH_ALL_QUERY, this.filterBuilderFromParams(queryParams));
        final DeleteByQueryRequestBuilder deleteByQueryRequestBuilder = client.prepareDeleteByQuery(index).setTypes(type);
        deleteByQueryRequestBuilder.setQuery(filteredQueryBuilder);
        deleteByQueryRequestBuilder.execute().actionGet();
    }

    public Map<String, Object> getById(String index, String type, String id) {
        final GetRequestBuilder getRequestBuilder = client.prepareGet(index, type, id);
        return getRequestBuilder.execute().actionGet().getSourceAsMap();
    }

    public List<Map<String, Object>> getByQuery(String index, String type, Map<String, Object> queryParams) {

        final FilteredQueryBuilder filteredQueryBuilder =
                QueryBuilders.filteredQuery(MATCH_ALL_QUERY, this.filterBuilderFromParams(queryParams));

        final SearchRequestBuilder searchRequestBuilder =
                client.prepareSearch(index).setTypes(type).setQuery(filteredQueryBuilder).setSize(config.getSearchSize());

        final SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();

        final SearchHit[] hits = searchResponse.getHits().getHits();
        final List<Map<String, Object>> resultList = new ArrayList<>(hits.length);
        for (SearchHit searchHit : hits) {
            resultList.add(searchHit.sourceAsMap());
        }

        return resultList;
    }

    void closeClient() {
        client.close();
    }

    public Client getClient() {
        return client;
    }

    public ElasticClientConfig getConfig() {
        return config;
    }

    private FilterBuilder filterBuilderFromParams(Map<String, Object> queryParams) {
        if (queryParams != null && queryParams.size() != 0) {
            final List<FilterBuilder> filterBuilders = new ArrayList<>(queryParams.size());
            final BoolFilterBuilder boolFilterBuilder = FilterBuilders.boolFilter();
            for (String key : queryParams.keySet()) {
                filterBuilders.add(FilterBuilders.termFilter(key, queryParams.get(key)));
            }
            boolFilterBuilder.must(filterBuilders.toArray(new FilterBuilder[queryParams.size()]));
            return boolFilterBuilder;
        } else {
            return null;
        }
    }
}
