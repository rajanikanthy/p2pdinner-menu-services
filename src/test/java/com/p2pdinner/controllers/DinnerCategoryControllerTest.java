package com.p2pdinner.controllers;

import com.p2pdinner.domain.DinnerCategory;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import io.restassured.path.json.JsonPath;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collection;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"eureka.client.enabled=false"})
@WebAppConfiguration
public class DinnerCategoryControllerTest {
    private static final Logger _logger = LoggerFactory.getLogger(DinnerCategoryControllerTest.class);

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
    }

    @Test
    public void testAllCategories() throws Exception {
        String mvcResponse = given().when().get("/api/categories").then().statusCode(200).extract().response().asString();
        Collection<String> categories = JsonPath.from(mvcResponse).getList("name");
        assertThat(categories).isNotEmpty();
    }

    @Test
    public void testAssociateCategory() throws Exception {
        MockMvcResponse mvcResponse = given()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "\t\"title\" : \"Test_PartialUpdate\",\n" +
                        "\t\"description\" : \"Test_PartialUpdate\"\n" +
                        "}")
                .when().post("/api/99/menuitem")
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .response();

        String location = mvcResponse.getHeader("location");
        Integer menuItemId = Integer.valueOf(location.substring(location.lastIndexOf('/') + 1, location.length()));

        // Get Dinner Category and associate with menuItemsId
        given()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "\t\"name\" : \"Mexican\"\n" +
                        "}")
                .when()
                .post("/api/{profileId}/menuitem/{menuItemId}/categories", 99, menuItemId)
                .then()
                .assertThat()
                .statusCode(200);

        given()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "\t\"name\" : \"American\"\n" +
                        "}")
                .when()
                .post("/api/{profileId}/menuitem/{menuItemId}/categories", 99, menuItemId)
                .then()
                .assertThat()
                .statusCode(200);

        mvcResponse = given()
                .when().get("/api/{profileId}/menuitem/{menuItemId}", 99, menuItemId)
                .then()
                .assertThat().statusCode(200).extract().response();

        _logger.info(mvcResponse.asString());
    }
}