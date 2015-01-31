package org.jcoffee.orm.dao;

import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.node.Node;
import org.jcoffee.orm.BuilderTest;
import org.jcoffee.orm.base.EntityBuilder;
import org.jcoffee.orm.base.EntityBuilderFactory;
import org.jcoffee.orm.elasticsearch.ElasticClientFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

public class BaseElasticDaoTest {

    private static Path esDataDir;
    private static Node node;
    private static Random random;

    @Before
    public void before() throws IOException {
        esDataDir = Paths.get("/tmp/elasticsearch_test/" + UUID.randomUUID().toString());
        random = new Random(System.currentTimeMillis());
        if (!Files.exists(esDataDir)) {
            Files.createDirectories(esDataDir);
        }

        Settings settings = ImmutableSettings.settingsBuilder()
                .put("network.host", "localhost")
                .put("path.data", esDataDir).build();

        node = nodeBuilder().settings(settings).node();
        node.start();
    }

    @After
    public void after() throws IOException, InterruptedException {
        node.close();
    }

    @Ignore
    @Test
    public void testDao() throws InterruptedException {
        final EntityBuilder<BuilderTest.TestClass> entityBuilder = EntityBuilderFactory.getEntityBuilder(BuilderTest.TestClass.class);
        Map settings = Collections.singletonMap(ElasticClientFactory.SETTING_CLUSTER_NAME, "elasticsearch");
        final BaseElasticDao elasticDao = new BaseElasticDao("localhost", 9300, settings);
        final int i = random.nextInt();
        BuilderTest.TestClass testClass = new BuilderTest.TestClass();
        testClass.setSomeByteVar((byte) random.nextInt());
        testClass.setSomeShortVar((short) random.nextInt());
        testClass.setSomeIntegerVar(i);
        testClass.setSomeLongVar(random.nextLong());
        testClass.setSomeFloatVar(random.nextFloat());
        testClass.setSomeDoubleVar(random.nextDouble());
        testClass.setSomeBooleanVar(random.nextBoolean());
        testClass.setSomeStringVar(UUID.randomUUID().toString());
        testClass.setSomeUuidVar(UUID.randomUUID());


        elasticDao.save(testClass, entityBuilder);

        Thread.sleep(1100L);

        final List<BuilderTest.TestClass> list =
                elasticDao.getByQuery(Collections.singletonMap("someIntegerVar", i), entityBuilder);
        UUID uuid = list.get(0).getSomeUuidVar();

        System.out.println(uuid.getClass().getName());
    }

    public static void deleteDirectory(Path rootDirectory) throws IOException {
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(rootDirectory)) {
            for (Path file : ds) {
                if (Files.isDirectory(file)) {
                    deleteDirectory(file);
                }
                Files.delete(file);
            }
            Files.delete(rootDirectory);
        }
    }
}