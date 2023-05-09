package GoRest;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class GoRestUsersTest {
    Faker faker = new Faker();
    int userID;
    String rndFullname = faker.name().fullName();
    String rndEmail = faker.internet().emailAddress();
    RequestSpecification reqSpec;
    @BeforeClass
    public void Setup(){
        baseURI="https://gorest.co.in/public/v2/users/"; //baseurini specten önce yazılması gerekiyor.
        reqSpec = new RequestSpecBuilder()
                .addHeader("Authorization", "Bearer 01a1641edfd4e965719444906d3445d3e8f7f5135338272769f8ba8b5d92471e")
                .setContentType(ContentType.JSON)
                .build();
    }
    @Test(enabled = false)
    public void createUserJSON() {
        String rndFullname = faker.name().fullName();
        String rndEmail = faker.internet().emailAddress();
        userID =
                given()
                        .header("Authorization", "Bearer 01a1641edfd4e965719444906d3445d3e8f7f5135338272769f8ba8b5d92471e")
                        .contentType(ContentType.JSON) // gönderilecek data JSON
                        .body("{\"name\":\"" + rndFullname + "\", \"gender\":\"male\", \"email\":\"" + rndEmail + "\", \"status\":\"active\"}")
                        .log().uri()

                        .when()
                        .post("https://gorest.co.in/public/v2/users")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().path("id")
        ;
    }

    @Test(enabled = false)
    public void createUserMap() {
        String rndFullname = faker.name().fullName();
        String rndEmail = faker.internet().emailAddress();

        Map<String, String> newUser = new HashMap<>();
        newUser.put("name", rndFullname);
        newUser.put("gender", "male");
        newUser.put("email", rndEmail);
        newUser.put("status", "active");

        userID =
                given()
                        .header("Authorization", "Bearer 01a1641edfd4e965719444906d3445d3e8f7f5135338272769f8ba8b5d92471e")
                        .contentType(ContentType.JSON) // gönderilecek data JSON
                        .body(newUser)
                        .log().uri()

                        .when()
                        .post("https://gorest.co.in/public/v2/users")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().path("id")
        ;
    }

    @Test
    public void createUserClass() {

        User newUser = new User();
        newUser.name = rndFullname;
        newUser.gender = "male";
        newUser.email = rndEmail;
        newUser.status = "active";

        userID =
                given()
                        .spec(reqSpec)
                        .body(newUser)
                        .log().uri()

                        .when()
                        .post()

                        .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().path("id")
        ;
    }

    @Test(dependsOnMethods = "createUserClass")
    public void getUserByID() {
        given()
                .header("Authorization", "Bearer 01a1641edfd4e965719444906d3445d3e8f7f5135338272769f8ba8b5d92471e")
                .contentType(ContentType.JSON)

                .when()
                .get("" + userID)
                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", equalTo(userID))
        ;
    }

    @Test(dependsOnMethods = "getUserByID")
    public void updateUser() {
        Map<String,String> updateUser =new HashMap<>();
        updateUser.put("name",rndFullname);
        given()
                .spec(reqSpec)
                .body(updateUser)

                .when()
                .put(""+userID)
                .then()
                .log().body()
                .statusCode(200)
                .body("id",equalTo(userID))
                .body("name",equalTo(rndFullname))
        ;
    }

    @Test
    public void deleteUser() {

    }

    @Test
    public void deleteUserNegative() {

    }
}
