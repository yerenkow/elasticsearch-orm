####Simple Elasticsearch ORM

######Supported types:
* All java wrapper types
* String

#####1. Create plain Java object with @Index and @Type annotations

```@Index``` annotation - Elasticsearch index name

```@Type``` annotation - Elasticsearch type name

Example:
```
@Index(name = "TestIndex")
@Type(name = "TestType")
public class TestObject {
    ....
}
```

#####2. Use ```EntityBuilderFactory.getEntityBuilder``` to get builder.
```
EntityBuilder eb = EntityBuilderFactory.getEntityBuilder(TestObject.class);
```

#####3. Create BaseElasticDao
```
BaseElasticDao elasticDao = new BaseElasticDao("host", port, settings);
```
Use it to save, update, get, delete entities
```
elasticDao.save(testObject, eb);
....
```

That's all :))
