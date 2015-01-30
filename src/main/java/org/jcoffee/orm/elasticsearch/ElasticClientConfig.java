package org.jcoffee.orm.elasticsearch;

public class ElasticClientConfig {

    public static final int DEFAULT_RETRY_ON_CONFLICT = 5;
    public static final int DEFAULT_SEARCH_SIZE = 100;

    private int retryOnConflict = DEFAULT_RETRY_ON_CONFLICT;
    private int searchSize = DEFAULT_SEARCH_SIZE;

    public int getRetryOnConflict() {
        return retryOnConflict;
    }

    public void setRetryOnConflict(int retryOnConflict) {
        this.retryOnConflict = retryOnConflict;
    }

    public int getSearchSize() {
        return searchSize;
    }

    public void setSearchSize(int searchSize) {
        this.searchSize = searchSize;
    }
}
