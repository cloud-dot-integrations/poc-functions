package com.redhat.clouddot.poc.funq;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ResponseBodyExtractionOptions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
@QuarkusTestResource(TestLifecycleManager.class)
public class RewriteTest {

    @Test
    public void testFunq() {

        String jsonPayload = "{\"bundle\":\"my-bundle\",\"application\":\"my-app\",\"event_type\":\"a_type\",\"timestamp\":\"2021-06-17T11:51:51.354544\",\"account_id\":\"123\",\"context\":\"{}\",\"events\":[{\"metadata\":{},\"payload\":\"{\\\"key1\\\":\\\"value1\\\",\\\"key2\\\":\\\"value2\\\"}\"}]}";


        String id = UUID.randomUUID().toString();
        ResponseBodyExtractionOptions body = given().contentType("application/json")
                .header("ce-specversion", "1.0")
                .header("ce-id", id)
                .header("ce-type", "notification")
                .header("ce-source", "test")
                .header("ce-rhaccount", 54321)
                .body(jsonPayload)
                .post("/")
                .then().statusCode(200)
                .header("ce-id", notNullValue())
                .header("ce-type", "email")
                .header("ce-source", "qute-func")
//                .body(Matchers.allOf(containsString("_rewriter"), containsString("my-app")))
                .extract().body();

        System.out.println(body.asString());

//        assertTrue(ra.payload.containsKey("key3"));
//        assertTrue(!ra.payload.containsKey("key2"));
//        assertEquals(ra.payload.get("key3"),"test" ); // ce-source of the input above
//        assertEquals(ra.payload.get("key1"), "value1"+id);
    }

}
