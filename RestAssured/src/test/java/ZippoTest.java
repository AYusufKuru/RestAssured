import Model.Location;
import Model.Place;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.*; //given when than
import static org.hamcrest.Matchers.*;  //equalTo

public class ZippoTest {

    @Test
    public void test() {

        given().
                // hazırlık işlemleri : (token, send body, parametreler)

                        when().
                // endpoint (url), metodu

                        then()
        // assertion, test, data işlemleri


        ;
    }

    @Test
    public void statusCodeTest() {

        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body() //dönen body json datası, log.all() - herşeyi gösterir
                .statusCode(200) //dönüş kodu 200 mü
        ;
    }
    @Test
    public void contentTypeTest() {

        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON) //dönen sonuç JSON mı
        ;
    }
    @Test
    public void checkCountryInResponseBody() {

        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON) //dönen sonuç JSON mı
                // pm.resonse.json().id -> body.id
                .body("country", equalTo("United States"))
        ;
    }
    @Test
    public void checkCountry() {

        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON) //dönen sonuç JSON mı
                // pm.resonse.json().id -> body.id
                .body("places[0].state", equalTo("California"))
        ;
    }
    @Test
    public void checkHasItem() {

        given()
                .when()
                .get("http://api.zippopotam.us/tr/01000")
                .then()
                .log().body()
                .statusCode(200)
                .body("places.'place name'", hasItem("Dörtağaç Köyü"))
        // bütün place name'lerde dörtağaç köyü var mı
        ;
    }
    @Test
    public void combinigTest() {

        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                //.log().body()
                .statusCode(200)
                .body("places", hasSize(1)) // size 1 mi
                .body("places.state", hasItem("California")) // verilen path deki list bu istem e sahip mi
                .body("places[0].'places name'", equalTo("Beverly Hills")) // verilen path deki değer buna eşit mi
        ;
    }
    @Test
    public void pathParamTest() {
        given()
                .pathParam("ulke", "us")
                .pathParam("postaKod", 90310)
                .log().uri()// reques Link
                .when()
                .get("http://api.zippopotam.us/{ulke}/{postaKod}")
                .then()
                .statusCode(200)
        //.log().body()
        ;
    }
    @Test
    public void queryParamTest() {
        given()
                .param("page", 1)
                .log().uri()// reques Link
                .when()
                .get("http://gorest.co.in/public/v1/users")//?page=1 ekleniyor (119)
                .then()
                .statusCode(200)
                .log().body()
        ;
    }
    @Test
    public void queryParamTest2() {
        //https://gorest.co.in/public/v1/users?page=3
        //bu linkteki 1 den 10a kadar sayfaları çağırdığınızda response daki donen page değerlerinin
        //çağırılan page nosu ile aynı olup olmadığını kontrol ediniz

        for (int i = 1; i <= 10; i++) {
            given()
                    .param("page", i)
                    .log().uri()// reques Link
                    .when()
                    .get("http://gorest.co.in/public/v1/users")//?page=1 ekleniyor (119)
                    .then()
                    .statusCode(200)
                    //.log().body()
                    .body("meta.pagination.page", equalTo(i))
            ;
        }
    }
    //----------------------
    RequestSpecification requestSpec;
    ResponseSpecification responseSpec;
    @BeforeClass
    public void Setup() {
        baseURI = "http://gorest.co.in/public/v1"; // https görmezse bunu ekler görürse eklemez
        requestSpec = new RequestSpecBuilder()
                .log(LogDetail.URI)
                .setContentType(ContentType.JSON)
                .build();
        responseSpec = new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .expectStatusCode(200)
                .log(LogDetail.BODY)
                .build();
    }

    @Test
    public void requestResponseSpecification() {
        given()
                .param("page", 1)
                .spec(requestSpec)
                .when()
                .get("/users")//?page=1 ekleniyor (119)
                .then()
                .spec(responseSpec)
        ;
    }

    //-----------------------
    @Test
    public void extractingJsonPath() {

        String countryName =
                given()
                        .when()
                        .get("http://api.zippopotam.us/us/90210")
                        .then()
                        .log().body()
                        .extract().path("country"); //her zaman en sonda extract yapılır
        System.out.println("countryName = " + countryName);
        Assert.assertEquals(countryName, "United States");
    }

    @Test
    public void extractingJsonPath2() {

        String placeName =
                given()
                        .when()
                        .get("http://api.zippopotam.us/us/90210")
                        .then()
                        .log().body()
                        .extract().path("places[0].'place name'");
        System.out.println("countryName = " + placeName);
    }

    @Test
    public void extractingJsonPath3() {
        //"http://gorest.co.in/public/v1/users" dönen değerdeki limit bilgisini yazdırın
        int limit =
                given()
                        .when()
                        .get("http://gorest.co.in/public/v1/users")
                        .then()
                        //.log().body()
                        .extract().path("meta.pagination.limit");
        System.out.println("limit = " + limit);
    }

    @Test
    public void extractingJsonPath4() {
        //"http://gorest.co.in/public/v1/users" dönen değerdeki bütün idleri yazdırın
        List<Integer> idList =
                given()
                        .when()
                        .get("http://gorest.co.in/public/v1/users")
                        .then()
                        //.log().body()
                        .statusCode(200)
                        .extract().path("data.id"); //bütün id leri ver
        System.out.println("idList = " + idList);
    }

    @Test
    public void extractingJsonPath5() {
        //"http://gorest.co.in/public/v1/users" dönen değerdeki bütün idleri yazdırın
        List<String> names =
                given()
                        .when()
                        .get("http://gorest.co.in/public/v1/users")
                        .then()
                        .statusCode(200)
                        .extract().path("data.name");
        System.out.println("names = " + names);
    }

    @Test
    public void extractingJsonPathResponseAll() {
        //"http://gorest.co.in/public/v1/users" dönen değerdeki bütün idleri yazdırın
        Response response =
                given()
                        .when()
                        .get("http://gorest.co.in/public/v1/users")
                        .then()
                        .statusCode(200)
                        .extract().response();
        List<Integer> idList = response.path("data.id");
        List<String> names = response.path("data.name");
        int limit = response.path("meta.pagination.limit");

        System.out.println("idList = " + idList);
        System.out.println("names = " + names);
        System.out.println("limit = " + limit);

        Assert.assertTrue(names.contains("Deeptimoyee Varrier"));
        Assert.assertTrue(idList.contains(1374022));
        Assert.assertEquals(limit, 10, "test sonucu hatalı");
    }

    @Test
    public void extractingJJsonAll_POJO() {//POJO: JSON nesnesi : locationNesnesi
    Location locationNesnesi=
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .extract().body().as(Location.class); //Model.Location şablonuna göre dönüştür
        System.out.println("locationNesnesi.getCountry = " + locationNesnesi.getCountry());
    }
    @Test
    public void extractingPOJO_Soru() {
        //aşağıdaki endpointte Dörtağaç Köyü ait diğer bilgileri yazdırınız.
       Location adana =
        given()
                .when()
                .get("http://api.zippopotam.us/tr/01000")
                .then()
                //.log().body()
                .statusCode(200)
                .extract().body().as(Location.class)
        ;
        for(Place p:adana.getPlaces())
            if (p.getPlacename().equalsIgnoreCase("Dörtağaç Köyü")){
                System.out.println("p = " + p);
            }

    }

}
