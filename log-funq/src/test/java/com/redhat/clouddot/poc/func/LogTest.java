package com.redhat.clouddot.poc.func;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

/**
 *
 */
@QuarkusTest
public class LogTest {

    @Test
    void shouldNotBailOnGet() {

        when()
                .get("process")
            .then()
                .statusCode(200);
    }

    @Test
    void shouldNotBailOnBlaType() {

        given()
                .header("Content-Type", "application/json")
                .header("Ce-type", "bla")
                .header("Ce-source", "lilalu")
                .header("ce-id", "123")
                .header("ce-mydata", "something")
                .body("{\"status\":\"fail\"}")
            .when()
                .post("/")
            .then()
                .statusCode(200);
    }
}
