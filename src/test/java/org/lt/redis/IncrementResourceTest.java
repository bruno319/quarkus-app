package org.lt.redis;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@QuarkusTest
public class IncrementResourceTest {

    @Test
    public void testRedisOperations() {
        // create a first increment key
        given().contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{\"key\":\"first-key\",\"value\":0}")
            .when()
            .post("/increments")
            .then()
            .statusCode(200)
            .body("key", is("first-key"))
            .body("value", is(0));

        // increment first key by 20
        given().contentType(ContentType.JSON)
            .body("20")
            .when()
            .put("/increments/first-key")
            .then()
            .statusCode(204);

        // verify that key has been incremented
        given().accept(ContentType.JSON)
            .when()
            .get("/increments/first-key")
            .then()
            .statusCode(200)
            .body("key", is("first-key"))
            .body("value", is(20));

        // delete first key
        given().accept(ContentType.JSON)
            .when()
            .delete("/increments/first-key")
            .then()
            .statusCode(204);
    }
}
