package org.jcoffee.orm.base;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class EntityBuilderFactory {
    private static final Map<Class, EntityBuilder> ENTITY_BUILDER_MAP = new HashMap<>();
    public static final ReadWriteLock LOCK = new ReentrantReadWriteLock();

    private EntityBuilderFactory() {
    }

    @SuppressWarnings("unchecked")
    public static <T> EntityBuilder<T> getEntityBuilder(Class<T> clazz) {
        if (!ENTITY_BUILDER_MAP.containsKey(clazz)) {
            try {
                LOCK.readLock().lock();
                if (!ENTITY_BUILDER_MAP.containsKey(clazz)) {
                    ENTITY_BUILDER_MAP.put(clazz, new EntityBuilder(clazz));
                }
            } finally {
                LOCK.readLock().unlock();
            }
        }
        return ENTITY_BUILDER_MAP.get(clazz);
    }
}
