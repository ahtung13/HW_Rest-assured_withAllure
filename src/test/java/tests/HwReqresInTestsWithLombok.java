package tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tests.models.CreateModel;
import tests.models.ListOfUsersModel;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static tests.Specs.*;
import static tests.helpers.CustomApiListener.withCustomTemplates;

public class HwReqresInTestsWithLombok {

    @Test
    @DisplayName("Check success creating")
    void successfulCreating() {
        CreateModel createModel = given()
                .spec(postRequest)
                .filter(withCustomTemplates())
                .when()
                .post("/users")
                .then()
                .spec(response201)
                .log().status()
                .log().body()
                .extract().as(CreateModel.class);
        assertEquals("Denis", createModel.getName());
        assertEquals("QAA", createModel.getJob());
    }

    @Test
    void getListOfUsersWithGroovy() {
        ListOfUsersModel listOfUsers = given()
                .spec(getRequest)
                .filter(withCustomTemplates())
                .when()
                .get("/users?page=2")
                .then()
                .spec(response200)
                .log().status()
                .body("data.findAll{it.id == 8}.last_name.flatten()",
                        hasItem("Ferguson"))
                .extract().as(ListOfUsersModel.class);
        assertEquals(2, listOfUsers.getPage());
        assertEquals(12, listOfUsers.getTotal());
        assertEquals(2, listOfUsers.getTotalPages());
    }

    @Test
    void singleResourceNotFound() {
        given()
                .spec(getRequest)
                .filter(withCustomTemplates())
                .when()
                .get("/unknown/23")
                .then()
                .log().status()
                .log().body()
                .spec(response404);
    }
    @Test
    void updateData() {
        CreateModel createModel = given()
                .spec(putRequest)
                .filter(withCustomTemplates())
                .when()
                .put("/users/5")
                .then()
                .log().status()
                .log().body()
                .spec(response200)
                .extract().as(CreateModel.class);
        assertEquals("Denis", createModel.getName());
        assertEquals("QAA", createModel.getJob());
    }

    @Test
    void deleteUser() {
        given()
                .spec(deleteRequest)
                .filter(withCustomTemplates())
                .when()
                .delete("/users/5")
                .then()
                .log().status()
                .spec(response204);
    }
}
