package org.jcoffee.orm;

import org.jcoffee.orm.annotation.Index;
import org.jcoffee.orm.annotation.Type;
import org.jcoffee.orm.base.EntityBuilder;
import org.jcoffee.orm.base.EntityBuilderFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Aleksandr Simonchuk on 31.01.15.
 */
public class BuilderTest {
    private static final Random RANDOM = new Random(System.currentTimeMillis());

    @Test
    public void testBuilder() {
        EntityBuilder<TestClass> entityBuilder = EntityBuilderFactory.getEntityBuilder(TestClass.class);
        TestClass testClass = new TestClass();
        testClass.setSomeByteVar((byte) RANDOM.nextInt());
        testClass.setSomeShortVar((short) RANDOM.nextInt());
        testClass.setSomeIntegerVar(RANDOM.nextInt());
        testClass.setSomeLongVar(RANDOM.nextLong());
        testClass.setSomeFloatVar(RANDOM.nextFloat());
        testClass.setSomeDoubleVar(RANDOM.nextDouble());
        testClass.setSomeBooleanVar(RANDOM.nextBoolean());
        testClass.setSomeStringVar(UUID.randomUUID().toString());
        testClass.setSomeUuidVar(UUID.randomUUID());

        Assert.assertEquals(entityBuilder.getIndexName(), "stat");
        Assert.assertEquals(entityBuilder.getTypeName(), "adv");

        Map<String, Object> toMap = entityBuilder.buildToMap(testClass);

        Assert.assertEquals(testClass.getSomeByteVar(), toMap.get("someByteVar"));
        Assert.assertEquals(testClass.getSomeShortVar(), toMap.get("someShortVar"));
        Assert.assertEquals(testClass.getSomeIntegerVar(), toMap.get("someIntegerVar"));
        Assert.assertEquals(testClass.getSomeLongVar(), toMap.get("someLongVar"));
        Assert.assertEquals(testClass.getSomeFloatVar(), toMap.get("someFloatVar"));
        Assert.assertEquals(testClass.getSomeDoubleVar(), toMap.get("someDoubleVar"));
        Assert.assertEquals(testClass.getSomeBooleanVar(), toMap.get("someBooleanVar"));
        Assert.assertEquals(testClass.getSomeStringVar(), toMap.get("someStringVar"));
        Assert.assertEquals(testClass.getSomeUuidVar(), toMap.get("someUuidVar"));

        TestClass fromMap = entityBuilder.buildFromMap(toMap);

        Assert.assertEquals(fromMap.getSomeByteVar(), testClass.getSomeByteVar());
        Assert.assertEquals(fromMap.getSomeShortVar(), testClass.getSomeShortVar());
        Assert.assertEquals(fromMap.getSomeIntegerVar(), testClass.getSomeIntegerVar());
        Assert.assertEquals(fromMap.getSomeLongVar(), testClass.getSomeLongVar());
        Assert.assertEquals(fromMap.getSomeFloatVar(), testClass.getSomeFloatVar());
        Assert.assertEquals(fromMap.getSomeDoubleVar(), testClass.getSomeDoubleVar());
        Assert.assertEquals(fromMap.getSomeBooleanVar(), testClass.getSomeBooleanVar());
        Assert.assertEquals(fromMap.getSomeStringVar(), testClass.getSomeStringVar());
        Assert.assertEquals(fromMap.getSomeUuidVar(), testClass.getSomeUuidVar());
    }

    @Test
    public void testBuilderNullValues() {
        EntityBuilder<TestClass> entityBuilder = EntityBuilderFactory.getEntityBuilder(TestClass.class);
        TestClass testClass = new TestClass();

        final Map<String, Object> toMap = entityBuilder.buildToMap(testClass);

        Assert.assertNull(toMap.get("someByteVar"));
        Assert.assertNull(toMap.get("someShortVar"));
        Assert.assertNull(toMap.get("someIntegerVar"));
        Assert.assertNull(toMap.get("someLongVar"));
        Assert.assertNull(toMap.get("someFloatVar"));
        Assert.assertNull(toMap.get("someDoubleVar"));
        Assert.assertNull(toMap.get("someBooleanVar"));
        Assert.assertNull(toMap.get("someStringVar"));
        Assert.assertNull(toMap.get("someUuidVar"));

        final TestClass fromMap = entityBuilder.buildFromMap(toMap);

        Assert.assertNull(fromMap.getSomeByteVar());
        Assert.assertNull(fromMap.getSomeShortVar());
        Assert.assertNull(fromMap.getSomeIntegerVar());
        Assert.assertNull(fromMap.getSomeLongVar());
        Assert.assertNull(fromMap.getSomeFloatVar());
        Assert.assertNull(fromMap.getSomeDoubleVar());
        Assert.assertNull(fromMap.getSomeBooleanVar());
        Assert.assertNull(fromMap.getSomeStringVar());
        Assert.assertNull(fromMap.getSomeUuidVar());
    }

    @Index(name = "stat")
    @Type(name = "adv")
    public static class TestClass {
        private Byte someByteVar;
        private Short someShortVar;
        private Integer someIntegerVar;
        private Long someLongVar;
        private Float someFloatVar;
        private Double someDoubleVar;
        private Boolean someBooleanVar;
        private String someStringVar;
        private UUID someUuidVar;

        public Byte getSomeByteVar() {
            return someByteVar;
        }

        public void setSomeByteVar(Byte someByteVar) {
            this.someByteVar = someByteVar;
        }

        public Short getSomeShortVar() {
            return someShortVar;
        }

        public void setSomeShortVar(Short someShortVar) {
            this.someShortVar = someShortVar;
        }

        public Integer getSomeIntegerVar() {
            return someIntegerVar;
        }

        public void setSomeIntegerVar(Integer someIntegerVar) {
            this.someIntegerVar = someIntegerVar;
        }

        public Long getSomeLongVar() {
            return someLongVar;
        }

        public void setSomeLongVar(Long someLongVar) {
            this.someLongVar = someLongVar;
        }

        public Float getSomeFloatVar() {
            return someFloatVar;
        }

        public void setSomeFloatVar(Float someFloatVar) {
            this.someFloatVar = someFloatVar;
        }

        public Double getSomeDoubleVar() {
            return someDoubleVar;
        }

        public void setSomeDoubleVar(Double someDoubleVar) {
            this.someDoubleVar = someDoubleVar;
        }

        public Boolean getSomeBooleanVar() {
            return someBooleanVar;
        }

        public void setSomeBooleanVar(Boolean someBooleanVar) {
            this.someBooleanVar = someBooleanVar;
        }

        public String getSomeStringVar() {
            return someStringVar;
        }

        public void setSomeStringVar(String someStringVar) {
            this.someStringVar = someStringVar;
        }

        public UUID getSomeUuidVar() {
            return someUuidVar;
        }

        public void setSomeUuidVar(UUID someUuidVar) {
            this.someUuidVar = someUuidVar;
        }
    }

}