package org.jcoffee.orm.base;

import org.jcoffee.orm.UnsafeMemory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Transformer<T> implements TransformerI<T> {

    private final Class<T> clazz;
    private final long[] declaredFieldsOffsets;
    private final String[] declaredFieldsNames;


    public Transformer(Class<T> clazz) {
        this.clazz = clazz;
        Field[] declaredFields = this.clazz.getDeclaredFields();
        declaredFieldsOffsets = new long[declaredFields.length];
        declaredFieldsNames = new String[declaredFields.length];
        for (int i = 0; i < declaredFields.length; i++) {
            Field declaredField = declaredFields[i];
            declaredFieldsOffsets[i] = UnsafeMemory.getFieldOffset(declaredField);
            declaredFieldsNames[i] = declaredField.getName();
        }
    }

    public T fromMap(Map<String, Object> map) {
        Object o = null;
        try {
            o = UnsafeMemory.allocateInstance(clazz);
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < clazz.getDeclaredFields().length; i++) {
            UnsafeMemory.putObject(o, declaredFieldsOffsets[i], map.get(declaredFieldsNames[i]));
        }

        return (T) o;
    }

    public Map<String, Object> toMap(T object) {
        Map<String, Object> map = new HashMap<>(clazz.getDeclaredFields().length);
        for (int i = 0; i < declaredFieldsNames.length; i++) {
            map.put(declaredFieldsNames[i], UnsafeMemory.getFieldObject(object, declaredFieldsOffsets[i]));
        }
        return map;
    }
}
