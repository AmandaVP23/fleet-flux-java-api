package dev.amanda.organization.rest;

import com.github.database.rider.core.api.connection.ConnectionHolder;
import dev.amanda.organization.application.use_cases.CreateOrganizationUseCase;
import dev.amanda.organization.dto.CreateOrganizationDTO;
import dev.amanda.organization.dto.OrganizationResponseDTO;
import dev.amanda.user.domain.Roles;
import io.agroal.api.AgroalDataSource;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusTest
@Tag("integration")
@TestHTTPEndpoint(OrganizationResource.class)
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class OrganizationResourceTest {

    @InjectMock
    CreateOrganizationUseCase createOrganizationUseCase;

    @Inject
    AgroalDataSource dataSource;

    ConnectionHolder connectionHolder = () -> dataSource.getConnection();

    private static final String VALID_BODY = """
        {
          "name": "Test Organization",
          "adminFirstName": "Super",
          "adminLastName": "Mario",
          "adminEmail": "super.mario@example.com",
          "hostname": "test-org"
        }
        """;

    @Nested
    class CreateOrganization {
        @Test
        @TestSecurity(user = "root", roles = Roles.SUPER_ADMIN)
        void shouldCallUseCaseWhenCreatingOrganization() {
            var response = new OrganizationResponseDTO();
            when(createOrganizationUseCase.execute(any(CreateOrganizationDTO.class))).thenReturn(response);

            given()
                .contentType(ContentType.JSON)
                .body(VALID_BODY)
            .when()
                .post("/organizations")
            .then()
                .statusCode(201);

            verify(createOrganizationUseCase).execute(any(CreateOrganizationDTO.class));
        }

        @Test
        @TestSecurity(user = "root", roles = Roles.ORG_ADMIN)
        void shouldThrowForbiddenWhenUserIsNotSuperAdmin() {
            given()
                    .contentType(ContentType.JSON)
                    .body(VALID_BODY)
                    .when()
                    .post("/organizations")
                    .then()
                    .statusCode(403);
        }
    }
}
