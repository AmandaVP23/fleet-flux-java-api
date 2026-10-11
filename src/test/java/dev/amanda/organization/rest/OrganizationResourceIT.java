package dev.amanda.organization.rest;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import support.TestKeycloak;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import static io.restassured.RestAssured.given;

@QuarkusIntegrationTest
public class OrganizationResourceIT {

    @AfterAll
    static void cleanup() {
        TestKeycloak.deleteAllOrgRealms();
    }

    @Test
    void shouldCreateAndRetrieveOrganization() {
        String superToken = TestKeycloak.superAdminToken();

        Long id = given()
                .contentType(ContentType.JSON)
                .body("""
                    {
                        "name": "Test Organization",
                        "adminFirstName": "Super",
                        "adminLastName": "Super",
                        "adminEmail": "super.mario@example.com",
                        "hostname": "test-org"
                    }
                """)
                .auth().oauth2(superToken)
                .when()
                    .post("/organizations")
                .then()
                    .statusCode(Response.Status.CREATED.getStatusCode())
                    .body("id", notNullValue())
                    .extract()
                    .jsonPath()
                    .getLong("id");

        given()
                .auth().oauth2(superToken)
                .when()
                    .get("/organizations/{id}", id)
                .then()
                    .statusCode(Response.Status.OK.getStatusCode())
                    .body("id", equalTo(id.intValue()))
                    .body("name", equalTo("Test Organization"));
    }
}
