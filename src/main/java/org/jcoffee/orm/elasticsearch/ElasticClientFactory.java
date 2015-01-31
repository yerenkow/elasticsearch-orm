package org.jcoffee.orm.elasticsearch;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ElasticClientFactory {

    public static final String SETTING_CLUSTER_NAME = "cluster.name";

    private static final String UNDERSCORE = "_";
    private static final Map<Integer, BaseElasticClient> ELASTIC_CLIENT_MAP = new HashMap<>();
    private static final ReadWriteLock LOCK = new ReentrantReadWriteLock();

    public static BaseElasticClient getInstance(String host, int port, Map<String, String> settingsMap) {

        Settings settings = (settingsMap == null ? ImmutableSettings.Builder.EMPTY_SETTINGS
                : ImmutableSettings.settingsBuilder().put(settingsMap).build());

        final int key = getHashCode(host, port, settingsMap);

        if (!ELASTIC_CLIENT_MAP.containsKey(key)) {
            try {
                LOCK.readLock().lock();
                if (!ELASTIC_CLIENT_MAP.containsKey(key)) {
                    Client client = new TransportClient(settings)
                            .addTransportAddress(new InetSocketTransportAddress(host, port));
                    BaseElasticClient baseElasticClient = new BaseElasticClient(client, new ElasticClientConfig());
                    System.out.println("Put key [" + key + "]");
                    ELASTIC_CLIENT_MAP.put(key, baseElasticClient);
                }
            } finally {
                LOCK.readLock().unlock();
            }
        }
        return ELASTIC_CLIENT_MAP.get(key);
    }

    private static int getHashCode(String host, int port, Map<String, String> settingsMap) {
        StringBuilder builder = new StringBuilder(host).append(UNDERSCORE).append(port);
        if (settingsMap != null) {
            List<String> keys = new ArrayList<>(settingsMap.keySet());
            Collections.sort(keys);
            for (int i = 0; i < keys.size(); i++) {
                String key = keys.get(i);
                builder.append(UNDERSCORE);
                builder.append(key);
                builder.append(UNDERSCORE);
                builder.append(settingsMap.get(key));
            }
        }
        return builder.toString().hashCode();
    }

    public static BaseElasticClient getInstance(String host, int port) {
        return getInstance(host, port, null);
    }

    public static void destroyClients() {
        final List<Integer> arrayList = new ArrayList<>(ELASTIC_CLIENT_MAP.keySet());
        for (Integer key : arrayList) {
            final BaseElasticClient baseElasticClient = ELASTIC_CLIENT_MAP.remove(key);
            System.out.println("Key [" + key + "] removed.");
            try {
                baseElasticClient.closeClient();
            } catch (ElasticsearchException e) {
                System.out.println("Elasticsearch client with key [" + key + "] was not closed.");
            }
        }
    }

    private ElasticClientFactory() {
    }
}
