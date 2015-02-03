package org.jcoffee.orm.elasticsearch;

/**
 * Created by Aleksandr Simonchuk on 31.01.15.
 */
public class ElasticClientConfig {

    public static final int DEFAULT_RETRY_ON_CONFLICT;
    public static final int DEFAULT_SEARCH_SIZE;

    static {
        DEFAULT_RETRY_ON_CONFLICT = System.getenv("DEFAULT_RETRY_ON_CONFLICT") != null
                ? Integer.valueOf(System.getenv("DEFAULT_RETRY_ON_CONFLICT")) : 5;

        DEFAULT_SEARCH_SIZE = System.getenv("DEFAULT_SEARCH_SIZE") != null
                ? Integer.valueOf(System.getenv("DEFAULT_SEARCH_SIZE")) : 100;
    }

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
