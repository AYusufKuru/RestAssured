import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
public class Task {
    /*
    Task 2
     create a request to https://httpstat.us/203
     expect status 203
     expect content type TEXT
     */
    @Test
    public void Task2(){
        given()
                .when()
                .get("https://httpstat.us/203")
                .then()
                .statusCode(203)
                .contentType(ContentType.TEXT)
        ;

    }
}
