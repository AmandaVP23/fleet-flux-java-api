package support;

import io.restassured.http.ContentType;
import org.hamcrest.Matchers;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class TestKeycloak {
    public static final String URL = TestConfig.get("quarkus.keycloak.admin-client.server-url");

    private static final String ADMIN_CLIENT_ID = TestConfig.get("quarkus.keycloak.admin-client.client-id");
    private static final String ADMIN_CLIENT_SECRET = TestConfig.get("quarkus.keycloak.admin-client.client-secret");

    private static final String TEST_CLIENT = "it-test-client";

    public static final String SUPER_ADMIN_REALM = "fleet-flux-admin";
    public static final String SUPER_ADMIN_USER = "superadmin@fleet-flux.com";
    public static final String SUPER_ADMIN_PASSWORD = "superadmin";

    private TestKeycloak() {}

    public static String adminToken() {
        return given()
                    .contentType(ContentType.URLENC)
                    .formParam("grant_type", "client_credentials")
                    .formParam("client_id", ADMIN_CLIENT_ID)
                    .formParam("client_secret", ADMIN_CLIENT_SECRET)
                .when()
                    .post(URL + "/realms/master/protocol/openid-connect/token")
                .then()
                    .statusCode(200)
                    .extract().path("access_token");
    }

    public static void ensureTestClient(String realm) {
       String admin = adminToken();

       List<Object> existing = given()
                   .auth().oauth2(admin)
                   .queryParam("clientId", TEST_CLIENT)
               .when()
                .get(URL + "/admin/realms/{realm}/clients", realm)
               .then()
                   .statusCode(200)
                   .extract().jsonPath().getList("$");
       if (!existing.isEmpty()) {
           return;
       }

       int status = given()
                   .auth().oauth2(admin)
                   .contentType(ContentType.JSON)
                   .body(Map.of(
                           "clientId", TEST_CLIENT,
                           "publicClient", true,
                           "directAccessGrantsEnabled", true,
                           "standardFlowEnabled", false
                   ))
               .when()
                    .post(URL + "/admin/realms/{realm}/clients", realm)
               .then()
                    .extract().statusCode();

       if (status != 201 && status != 409) {
           throw new IllegalStateException("Creating test client in realm " + realm + " failed: " + status);
       }
    }

    public static void setPassword(String realm, String username, String password) {
        String admin = adminToken();

        List<String> ids = given()
                .auth().oauth2(admin)
                .queryParam("username", username)
                .queryParam("exact", true)
                .when()
                    .get(URL + "/admin/realms/{realm}/users", realm)
                .then()
                    .statusCode(200)
                    .extract().jsonPath().getList("id");

        if (ids.isEmpty()) {
            throw new IllegalStateException("User " + username + " not found in realm " + realm);
        }

        given()
                .auth().oauth2(admin)
                .contentType(ContentType.JSON)
                .body(Map.of("type", "password", "value", password, "temporary", false))
                .when()
                    .put(URL + "/admin/realms/{realm}/users/{id}/reset-password", realm, ids.getFirst())
                .then()
                    .statusCode(204);
    }

    public static String token(String realm, String username, String password) {
        disableSslRequirement(realm);
        ensureTestClient(realm);
        return given()
                    .contentType(ContentType.URLENC)
                    .formParam("grant_type", "password")
                    .formParam("client_id", TEST_CLIENT)
                    .formParam("username", username)
                    .formParam("password", password)
                .when()
                    .post(URL + "/realms/{realm}/protocol/openid-connect/token", realm)
                .then()
                    .statusCode(200)
                    .extract().path("access_token");
    }

    public static String superAdminToken() {
        return token(SUPER_ADMIN_REALM, SUPER_ADMIN_USER, SUPER_ADMIN_PASSWORD);
    }

    public static String tokenForCreatedUser(String realm, String username, String password) {
        ensureTestClient(realm);
        setPassword(realm, username, password);
        return token(realm, username, password);
    }

    public static void deleteRealm(String realm) {
        try {
            given()
                    .auth().oauth2(adminToken())
                    .when()
                        .delete(URL + "/admin/realms/{realm}", realm)
                    .then()
                        .statusCode(Matchers.anyOf(Matchers.is(204), Matchers.is(404)));
        } catch (Exception ignored) {

        }
    }

    public static void disableSslRequirement(String realm) {
        given()
                .auth().oauth2(adminToken())
                .contentType(ContentType.JSON)
                .body(Map.of("sslRequired", "none"))
                .when()
                .put(URL + "/admin/realms/{realm}", realm)
                .then()
                .statusCode(204);
    }

    public static List<String> listRealms() {
        return given()
                .auth().oauth2(adminToken())
                .when()
                .get(URL + "/admin/realms")
                .then()
                .statusCode(200)
                .extract().jsonPath().getList("realm");
    }

    public static void deleteAllOrgRealms() {
        for (String realm : listRealms()) {
            if (realm.equals("master") || realm.equals(SUPER_ADMIN_REALM)) {
                continue;
            }
            deleteRealm(realm);
        }
    }
}
