package org.jcoffee.orm.base;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Aleksandr Simonchuk on 31.01.15.
 */
public class EntityBuilderFactory {
    private static final Map<Class, EntityBuilder> ENTITY_BUILDER_MAP = new HashMap<>();
    public static final Lock LOCK = new ReentrantLock();

    private EntityBuilderFactory() {
    }

    @SuppressWarnings("unchecked")
    public static <T> EntityBuilder<T> getEntityBuilder(final Class<T> clazz) {
        if (!ENTITY_BUILDER_MAP.containsKey(clazz)) {
            try {
                LOCK.lock();
                if (!ENTITY_BUILDER_MAP.containsKey(clazz)) {
                    ENTITY_BUILDER_MAP.put(clazz, new EntityBuilder(clazz));
                }
            } finally {
                LOCK.unlock();
            }
        }
        return ENTITY_BUILDER_MAP.get(clazz);
    }
}
