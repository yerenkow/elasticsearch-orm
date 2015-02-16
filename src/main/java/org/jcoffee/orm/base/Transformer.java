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

    public T fromMap(final Map<String, Object> map) {
        final T object = UnsafeMemory.allocateInstance(clazz);
        Class<?> declaredFieldType;
        for (int i = 0; i < declaredFields.length; i++) {
            declaredFieldType = declaredFields[i].getType();
            if (declaredFieldType == Byte.class) {
                final Number byteValue = (Number) map.get(declaredFieldsNames[i]);
                UnsafeMemory.putObject(object, declaredFieldsOffsets[i], byteValue != null ? byteValue.byteValue() : null);
            } else if (declaredFieldType == Short.class) {
                final Number shortValue = (Number) map.get(declaredFieldsNames[i]);
                UnsafeMemory.putObject(object, declaredFieldsOffsets[i], shortValue != null ? shortValue.shortValue() : null);
            } else if (declaredFieldType == Integer.class) {
                final Number intValue = (Number) map.get(declaredFieldsNames[i]);
                UnsafeMemory.putObject(object, declaredFieldsOffsets[i], intValue != null ? intValue.intValue() : null);
            } else if (declaredFieldType == Long.class) {
                final Number longValue = (Number) map.get(declaredFieldsNames[i]);
                UnsafeMemory.putObject(object, declaredFieldsOffsets[i], longValue != null ? longValue.longValue() : null);
            } else if (declaredFieldType == Float.class) {
                final Number floatValue = (Number) map.get(declaredFieldsNames[i]);
                UnsafeMemory.putObject(object, declaredFieldsOffsets[i], floatValue != null ? floatValue.floatValue() : null);
            } else if (declaredFieldType == Double.class) {
                final Number doubleValue = (Number) map.get(declaredFieldsNames[i]);
                UnsafeMemory.putObject(object, declaredFieldsOffsets[i], doubleValue != null ? doubleValue.doubleValue() : null);
            } else if (declaredFieldType == UUID.class) {
                final Object uuidValue = map.get(declaredFieldsNames[i]);
                UnsafeMemory.putObject(object, declaredFieldsOffsets[i], uuidValue != null ? UUID.fromString("" + uuidValue) : null);
            } else {
                UnsafeMemory.putObject(object, declaredFieldsOffsets[i], map.get(declaredFieldsNames[i]));
            }
        }
        return object;
    }

    public Map<String, Object> toMap(final T object) {
        final Map<String, Object> map = new HashMap<>(declaredFields.length);
        int i = declaredFieldsNames.length;
        while (i-- > 0) {
            map.put(declaredFieldsNames[i], UnsafeMemory.getFieldObject(object, declaredFieldsOffsets[i]));
        }
        return map;
    }
}
