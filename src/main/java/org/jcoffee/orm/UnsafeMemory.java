package org.jcoffee.orm;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * Created by Aleksandr Simonchuk on 31.01.15.
 */
public class UnsafeMemory {

    private static final Unsafe UNSAFE;

    static {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            UNSAFE = (Unsafe) field.get(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Object allocateInstance(Class aClass) {
        Object object = null;
        try {
            object = UNSAFE.allocateInstance(aClass);
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return object;
    }

    public static Object getFieldObject(Object baseObject, long offset) {
        return UNSAFE.getObject(baseObject, offset);
    }

    public static long getFieldOffset(Field field) {
        return UNSAFE.objectFieldOffset(field);
    }

    public static void putObject(Object instance, long fieldOffset, Object object) {
        UNSAFE.putObject(instance, fieldOffset, object);
    }
}
