package org.jcoffee.orm.elasticsearch;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ElasticClientFactory {

    public static final String SETTING_CLUSTER_NAME = "cluster.name";

    private static final String UNDERSCORE = "_";
    private static final HashMap<String, BaseElasticClient> ELASTIC_CLIENT_MAP = new HashMap<>();

    public static BaseElasticClient getInstance(String host, int port, Map<String, String> settingsMap) {

        Settings settings = (settingsMap == null ? ImmutableSettings.Builder.EMPTY_SETTINGS
                : ImmutableSettings.settingsBuilder().put(settingsMap).build());

        final String key = host + UNDERSCORE + port + UNDERSCORE + getHashCode(settingsMap);

        if (!ELASTIC_CLIENT_MAP.containsKey(key)) {
            synchronized (ELASTIC_CLIENT_MAP) {
                if (!ELASTIC_CLIENT_MAP.containsKey(key)) {
                    Client client = new TransportClient(settings)
                            .addTransportAddress(new InetSocketTransportAddress(host, port));
                    BaseElasticClient baseElasticClient = new BaseElasticClient(client, new ElasticClientConfig());
                    System.out.println("Put key [" + key + "]");
                    ELASTIC_CLIENT_MAP.put(key, baseElasticClient);
                }
            }
        }
        return ELASTIC_CLIENT_MAP.get(key);
    }

    private static String getHashCode(Map<String, String> map) {
        StringBuilder builder = new StringBuilder();
        if (map != null) {
            ArrayList<String> keys = new ArrayList<>(map.keySet());
            for (int i = 0; i < keys.size(); i++) {
                String key = keys.get(i);
                builder.append(UNDERSCORE);
                builder.append(key);
                builder.append(UNDERSCORE);
                builder.append(map.get(key));
            }
        }
        return builder.toString();
    }

    public static BaseElasticClient getInstance(String host, int port) {
        return getInstance(host, port, null);
    }

    public static void destroyClients() {
        final ArrayList<String> arrayList = new ArrayList(ELASTIC_CLIENT_MAP.keySet());
        for (String key : arrayList) {
            final BaseElasticClient baseElasticClient = ELASTIC_CLIENT_MAP.remove(key);
            System.out.println("Key [" + key + "] removed.");
            baseElasticClient.closeClient();
        }
    }

    private ElasticClientFactory() {
    }
}
