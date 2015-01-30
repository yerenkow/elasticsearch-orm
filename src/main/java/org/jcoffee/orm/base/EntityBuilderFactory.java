package org.jcoffee.orm.base;

import java.util.HashMap;
import java.util.Map;

public class EntityBuilderFactory {
    private static final Map<Class, EntityBuilder> ENTITY_BUILDER_MAP = new HashMap<>();

    private EntityBuilderFactory() {
    }

    @SuppressWarnings("unchecked")
    public static <T> EntityBuilder<T> getEntityBuilder(Class<T> clazz) {
        if (!ENTITY_BUILDER_MAP.containsKey(clazz)) {
            synchronized (ENTITY_BUILDER_MAP) {
                if (!ENTITY_BUILDER_MAP.containsKey(clazz)) {
                    ENTITY_BUILDER_MAP.put(clazz, new EntityBuilder(clazz));
                }
            }
        }
        return ENTITY_BUILDER_MAP.get(clazz);
    }
}
