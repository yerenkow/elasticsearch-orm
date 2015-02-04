package org.jcoffee.orm.base;

import org.jcoffee.orm.UnsafeMemory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Aleksandr Simonchuk on 31.01.15.
 */
public class Transformer<T> implements TransformerI<T> {

    private final Class<T> clazz;
    private final long[] declaredFieldsOffsets;
    private final String[] declaredFieldsNames;
    private final Field[] declaredFields;

    public Transformer(Class<T> clazz) {
        this.clazz = clazz;
        declaredFields = this.clazz.getDeclaredFields();
        declaredFieldsOffsets = new long[declaredFields.length];
        declaredFieldsNames = new String[declaredFields.length];
        for (int i = 0; i < declaredFields.length; i++) {
            Field declaredField = declaredFields[i];
            declaredFieldsOffsets[i] = UnsafeMemory.getFieldOffset(declaredField);
            declaredFieldsNames[i] = declaredField.getName();
        }
    }

    public T fromMap(Map<String, Object> map) {
        Object object = UnsafeMemory.allocateInstance(clazz);
        Class<?> declaredFieldType;
        for (int i = 0; i < declaredFields.length; i++) {
            declaredFieldType = declaredFields[i].getType();
            if (declaredFieldType == Byte.class) {
                UnsafeMemory.putObject(object, declaredFieldsOffsets[i], ((Number) map.get(declaredFieldsNames[i])).byteValue());
            } else if (declaredFieldType == Short.class) {
                UnsafeMemory.putObject(object, declaredFieldsOffsets[i], ((Number) map.get(declaredFieldsNames[i])).shortValue());
            } else if (declaredFieldType == Integer.class) {
                UnsafeMemory.putObject(object, declaredFieldsOffsets[i], ((Number) map.get(declaredFieldsNames[i])).intValue());
            } else if (declaredFieldType == Long.class) {
                UnsafeMemory.putObject(object, declaredFieldsOffsets[i], ((Number) map.get(declaredFieldsNames[i])).longValue());
            } else if (declaredFieldType == Float.class) {
                UnsafeMemory.putObject(object, declaredFieldsOffsets[i], ((Number) map.get(declaredFieldsNames[i])).floatValue());
            } else if (declaredFieldType == Double.class) {
                UnsafeMemory.putObject(object, declaredFieldsOffsets[i], ((Number) map.get(declaredFieldsNames[i])).doubleValue());
            } else if (declaredFieldType == UUID.class) {
                final String uuid = (String) map.get(declaredFieldsNames[i]);
                if (uuid != null) {
                    UnsafeMemory.putObject(object, declaredFieldsOffsets[i], UUID.fromString(uuid));
                }
            } else {
                UnsafeMemory.putObject(object, declaredFieldsOffsets[i], map.get(declaredFieldsNames[i]));
            }

        }
        return (T) object;
    }

    public Map<String, Object> toMap(T object) {
        Map<String, Object> map = new HashMap<>(declaredFields.length);
        for (int i = 0; i < declaredFieldsNames.length; i++) {
            map.put(declaredFieldsNames[i], UnsafeMemory.getFieldObject(object, declaredFieldsOffsets[i]));
        }
        return map;
    }
}
