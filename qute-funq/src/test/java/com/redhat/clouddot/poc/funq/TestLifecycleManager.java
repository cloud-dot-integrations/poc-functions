package com.redhat.clouddot.poc.funq;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class TestLifecycleManager implements QuarkusTestResourceLifecycleManager {

    private PostgreSQLContainer postgreSQLContainer;

    @Override
    public Map<String, String> start() {

        String sep = System.lineSeparator();
        String rules = "key3: .meta.source" + sep + "key1: $key1 + .meta.id" + sep + "-key2" + sep;

        HashMap<String, String> props = new HashMap<>();
        props.put("rules",rules);

        postgreSQLContainer = new PostgreSQLContainer<>("postgres");
        postgreSQLContainer.start();
        // Now that postgres is started, we need to get its URL and tell Quarkus
        String jdbcUrl = postgreSQLContainer.getJdbcUrl();
        props.put("quarkus.datasource.jdbc.url", jdbcUrl);
        props.put("quarkus.datasource.username", "test");
        props.put("quarkus.datasource.password", "test");

        return props;
    }

    @Override
    public void stop() {
        if (postgreSQLContainer != null) {
            postgreSQLContainer.stop();
        }
    }
}
