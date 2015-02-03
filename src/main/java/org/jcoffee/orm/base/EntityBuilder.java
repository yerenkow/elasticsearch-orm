package org.jcoffee.orm.base;

import org.jcoffee.orm.annotation.Index;
import org.jcoffee.orm.annotation.Type;
import org.jcoffee.orm.exception.AnnotationNotFoundException;

import java.util.Map;

/**
 * Created by Aleksandr Simonchuk on 31.01.15.
 */
public class EntityBuilder<T> implements EntityBuilderI<T> {

    private final TransformerI<T> transformer;

    private String indexName;
    private String typeName;

    public EntityBuilder(Class<T> clazz) {
        this.transformer = new Transformer<>(clazz);

        Index indexAnnotation = clazz.getAnnotation(Index.class);
        if (indexAnnotation == null)
            throw new AnnotationNotFoundException(
                    "Required annotation [" + Index.class.getName() + "] not found, class [" + clazz.getName() + "]");
        this.indexName = indexAnnotation.name();

        Type typeAnnotation = clazz.getAnnotation(Type.class);
        if (typeAnnotation == null)
            throw new AnnotationNotFoundException(
                    "Required annotation [" + Type.class.getName() + "] not found, class [" + clazz.getName() + "]");
        this.typeName = typeAnnotation.name();
    }

    @Override
    public T buildFromMap(Map<String, Object> map) {
        return transformer.fromMap(map);
    }

    @Override
    public Map<String, Object> buildToMap(T object) {
        return transformer.toMap(object);
    }

    @Override
    public String getIndexName() {
        return indexName;
    }

    @Override
    public String getTypeName() {
        return typeName;
    }
}
