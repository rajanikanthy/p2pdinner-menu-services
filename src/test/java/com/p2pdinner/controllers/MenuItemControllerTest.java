package com.p2pdinner.controllers;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.util.HashMap;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.p2pdinner.domain.MenuItem;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.MockMvcResponse;

/**
 * Created by rajaniy on 11/2/16.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"eureka.client.enabled=false"})
@WebAppConfiguration
public class MenuItemControllerTest {

    private static final Logger _logger = LoggerFactory.getLogger(MenuItemControllerTest.class);

    private MockMvc mockMvc;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(7001);

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
        stubFor(com.github.tomakehurst.wiremock.client.WireMock.get(urlMatching("/gateway/api/profiles/[0-9]"))
                .withHeader("X-Service-Request", equalTo("p2pdinner-profile-services"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("valid-profile.json"))
        );
        stubFor(com.github.tomakehurst.wiremock.client.WireMock.get(urlEqualTo("/gateway/api/profiles/9999"))
                .withHeader("X-Service-Request", equalTo("p2pdinner-profile-services"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.NOT_FOUND.value())
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("invalid-profile.json"))
        );
    }


    @Test
    public void testItemById() throws Exception {
        MockMvcResponse response = given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "\t\"title\" : \"Chicken Biriyani3\",\n" +
                        "\t\"description\" : \"Rice mixed with tender chicken and indian spices\"\n" +
                        "}")
                .when().post("/api/{profileId}/menuitem", 1)
                .then().statusCode(201).extract().response();

        String location = response.getHeader("location");
        Integer menuItemId = Integer.valueOf(location.substring(location.lastIndexOf('/') + 1, location.length()));


        given()
                .when().get("/api/{profileId}/menuitem/{id}", 1, menuItemId)
                .then().statusCode(200);
    }

    @Test
    @Ignore
    public void invalidProfileIdTest() throws Exception {
        given()
                .when().get("/api/{profileId}/menuitem/{id}", 9999, 1)
                .then().assertThat().statusCode(400);
    }

    @Test
    public void testCreateMenuItem() throws Exception {
        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "\t\"title\" : \"Chicken Biriyani2\",\n" +
                        "\t\"description\" : \"Rice mixed with tender chicken and indian spices\"\n" +
                        "}")
                .when().post("/api/{profileId}/menuitem", 1)
                .then().statusCode(201);
    }

    @Test
    public void testCreateMenuItemAllFields() throws Exception {
        String payload = "{\n" +
                "   \"title\":\"Chicken Biriyani\",\n" +
                "   \"addressLine1\":\"5338 Piazza Ct\",\n" +
                "   \"city\":\"Pleasanton\",\n" +
                "   \"state\":\"CA\",\n" +
                "   \"zipCode\":\"94588\",\n" +
                "   \"availableQty\":\"10\",\n" +
                "   \"costPerItem\":\"6.99\",\n" +
                "   \"startDateAsString\":\"01/01/19\",\n" +
                "   \"endDateAsString\":\"01/01/19\",\n" +
                "   \"closeDateAsString\":\"01/01/19\",\n" +
                "   \"categories\":[\n" +
                "      \"American\",\n" +
                "      \"Chinese\"\n" +
                "   ],\n" +
                "   \"specialNeeds\":[\n" +
                "      \"Diabetic\",\n" +
                "      \"Gluten Free\"\n" +
                "   ],\n" +
                "   \"deliveries\":[\n" +
                "      \"Eat-In\"\n" +
                "   ],\n" +
                "   \"startDate\":1546329600000,\n" +
                "   \"endDate\":1546329600000,\n" +
                "   \"closeDate\":1546329600000\n" +
                "}";
        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(payload)
                .when().post("/api/{profileId}/menuitem", 1)
                .then().statusCode(201);
    }

    @Test
    public void testPatchMenuItem() throws Exception {
        MockMvcResponse createItemResponse = given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "\t\"title\" : \"Test_PartialUpdate\",\n" +
                        "\t\"description\" : \"Test_PartialUpdate\"\n" +
                        "}")
                .when().post("/api/{profileId}/menuitem", 1)
                .then().assertThat().statusCode(201).extract().response();
        String location = createItemResponse.getHeader("location");
        Integer menuItemId = Integer.valueOf(location.substring(location.lastIndexOf('/') + 1, location.length()));

        MockMvcResponse partialUpdateResponse = given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "\t\"addressLine1\" : \"Test_PartialUpdate_Address1\",\n" +
                        "\t\"addressLine2\" : \"Test_PartialUpdate_Address2\"\n" +
                        "}")
                .when().patch("/api/{profileId}/menuitem/{menuItemId}", 1, menuItemId)
                .then().assertThat().statusCode(200).extract().response();

        MenuItem menuItem = partialUpdateResponse.as(MenuItem.class);

        assertThat(menuItem.getAddressLine1(), Matchers.equalTo("Test_PartialUpdate_Address1"));
        assertThat(menuItem.getAddressLine2(), Matchers.equalTo("Test_PartialUpdate_Address2"));
    }

    @Test
    public void testUpdateMenuItem() throws Exception {
        MockMvcResponse mockMvcResponse = given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "\t\"title\" : \"Test001\",\n" +
                        "\t\"description\" : \"Test 001\"\n" +
                        "}")
                .when().post("/api/1/menuitem")
                .then().statusCode(201).extract().response();


        String location = mockMvcResponse.getHeader("location");

        Integer menuItemId = Integer.valueOf(location.substring(location.lastIndexOf('/') + 1, location.length()));

        MockMvcResponse putResponse = given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "\t\"title\" : \"Test001\",\n" +
                        "\t\"description\" : \"Test001\",\n" +
                        "\t\"addressLine1\" : \"5338 Piazza Ct\",\n" +
                        "\t\"state\" : \"CA\",\n" +
                        "\t\"city\" : \"Pleasanton\",\n" +
                        "\t\"zipCode\" : 94588\n" +
                        "}")
                .when().put("/api/{profileId}/menuitem/{menuItemId}", 1, menuItemId)
                .then().assertThat().statusCode(200).extract().response();

        MenuItem menuItem = putResponse.as(MenuItem.class);

        assertThat(menuItem.getAddressLine1(), Matchers.equalTo("5338 Piazza Ct"));
        assertThat(menuItem.getZipCode(), Matchers.equalTo("94588"));
    }

    @Test
    public void testDeleteMenuItem() throws Exception {

        String payload = "{\n" +
                "   \"title\":\"Lasaniya\",\n" +
                "   \"addressLine1\":\"5338 Piazza Ct\",\n" +
                "   \"city\":\"Pleasanton\",\n" +
                "   \"state\":\"CA\",\n" +
                "   \"zipCode\":\"94588\",\n" +
                "   \"availableQty\":\"10\",\n" +
                "   \"costPerItem\":\"6.99\",\n" +
                "   \"startDateAsString\":\"01/01/19\",\n" +
                "   \"endDateAsString\":\"01/01/19\",\n" +
                "   \"closeDateAsString\":\"01/01/19\",\n" +
                "   \"categories\":[\n" +
                "      \"American\",\n" +
                "      \"Chinese\"\n" +
                "   ],\n" +
                "   \"specialNeeds\":[\n" +
                "      \"Diabetic\",\n" +
                "      \"Gluten Free\"\n" +
                "   ],\n" +
                "   \"deliveries\":[\n" +
                "      \"Eat-In\"\n" +
                "   ],\n" +
                "   \"startDate\":1546329600000,\n" +
                "   \"endDate\":1546329600000,\n" +
                "   \"closeDate\":1546329600000\n" +
                "}";
        MockMvcResponse mockMvcResponse = given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(payload)
                .when().post("/api/1/menuitem")
                .then().statusCode(201).extract().response();


        String location = mockMvcResponse.getHeader("location");

        Integer menuItemId = Integer.valueOf(location.substring(location.lastIndexOf('/') + 1, location.length()));

        given()
                .delete("/api/{profileId}/menuitem/{menuItemId}", 1, menuItemId)
                .then().assertThat().statusCode(200);

    }
}