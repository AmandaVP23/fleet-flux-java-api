package dev.amanda.organization.application;

import dev.amanda.oidc.KeycloakAdmin;
import dev.amanda.oidc.OrgTenantConfigResolver;
import dev.amanda.organization.domain.Organization;
import dev.amanda.organization.domain.OrganizationRepository;
import dev.amanda.organization.dto.CreateOrganizationDTO;
import dev.amanda.organization.dto.OrganizationResponseDTO;
import dev.amanda.organization.exceptions.OrganizationAlreadyExistsException;
import dev.amanda.organization.exceptions.OrganizationWithSameRealmAlreadyExistsException;
import dev.amanda.organization.exceptions.UserSameEmailAlreadyExistsException;
import dev.amanda.shared.exception.GenericApiException;
import dev.amanda.user.domain.User;
import dev.amanda.user.domain.UserRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.keycloak.representations.idm.RealmRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@QuarkusTest
public class CreateOrganizationUseCaseTest {
    @Inject
    CreateOrganizationUseCase createOrganizationUseCase;

    @InjectMock
    OrganizationRepository organizationRepository;

    @InjectMock
    UserRepository userRepository;

    @InjectMock
    SaveOrganizationUseCase saveOrganizationUseCase;

    @InjectMock
    KeycloakAdmin keycloakAdmin;

    @InjectMock
    OrgTenantConfigResolver orgTenantConfigResolver;

    private CreateOrganizationDTO createDto;
    private RealmRepresentation realmRepresentation;
    private UserRepresentation userRepresentation;

    private static final String ORGANIZATION_NAME = "Example Corp";
    private static final String EXPECTED_REALM = "example_corp";
    private static final String USER_KC_ID = "user-uuid-456";
    private static final String ADMIN_EMAIL = "admin@acme.com";

    @BeforeEach
    public void setUp() {
        createDto = new CreateOrganizationDTO();
        createDto.name = ORGANIZATION_NAME;
        createDto.adminEmail = ADMIN_EMAIL;
        createDto.adminFirstName = "John";
        createDto.adminLastName = "Doe";

        realmRepresentation = new RealmRepresentation();
        realmRepresentation.setId("realm-uuid-123");

        userRepresentation = new UserRepresentation();
        userRepresentation.setId(USER_KC_ID);
        when(keycloakAdmin.createRealm(anyString(), anyString())).thenReturn(realmRepresentation);
        when(keycloakAdmin.createRealmUser(anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(userRepresentation);
    }

    @Nested
    class WhenEverythingIsValid {
//        @Test
//        void shouldReturnResponseFromSaveUseCase() {
//            var expected = new OrganizationResponseDTO();
//            expected.setId(123L);
//            expected.setName(ORGANIZATION_NAME);
//
//            when(saveOrganizationUseCase.execute(createDto, EXPECTED_REALM, USER_KC_ID)).thenReturn(expected);
//
//            var result = createOrganizationUseCase.execute(createDto);
//
//            assertThat(result).isEqualTo(expected);
//        }

        @Test
        void shouldCreateRealmWithGeneratedRealmValue() {
            createOrganizationUseCase.execute(createDto);

            verify(keycloakAdmin).createRealm(EXPECTED_REALM, ORGANIZATION_NAME);
        }

        @Test
        void shouldEvictAndReloadTenantCacheAfterCreation() {
            createOrganizationUseCase.execute(createDto);

            verify(orgTenantConfigResolver).evictAndReload(EXPECTED_REALM);
        }

        @Test
        void shouldDelegateSaveWithCorrectRealmAndUserId() {
            createOrganizationUseCase.execute(createDto);

            verify(saveOrganizationUseCase).execute(createDto, EXPECTED_REALM, USER_KC_ID);
        }
    }

    // =========================================================================
    // Duplicate-detection guards (before Keycloak is touched)
    // =========================================================================

    @Nested
    class WhenOrganizationAlreadyExists {

        @Test
        void shouldThrowOrganizationAlreadyExistsException() {
            when(organizationRepository.findByName(ORGANIZATION_NAME))
                    .thenReturn(Optional.of(new Organization()));

            assertThatThrownBy(() -> createOrganizationUseCase.execute(createDto))
                    .isInstanceOf(OrganizationAlreadyExistsException.class);
        }

        @Test
        void shouldNotTouchKeycloakWhenNameConflicts() {
            when(organizationRepository.findByName(ORGANIZATION_NAME))
                    .thenReturn(Optional.of(new Organization()));

            catchThrowable(() -> createOrganizationUseCase.execute(createDto));

            verifyNoInteractions(keycloakAdmin);
        }
    }

    @Nested
    class WhenRealmAlreadyExists {

        @Test
        void shouldThrowOrganizationWithSameRealmAlreadyExistsException() {
            when(organizationRepository.findByRealm(EXPECTED_REALM))
                    .thenReturn(Optional.of(new Organization()));

            assertThatThrownBy(() -> createOrganizationUseCase.execute(createDto))
                    .isInstanceOf(OrganizationWithSameRealmAlreadyExistsException.class);
        }

        @Test
        void shouldNotTouchKeycloakWhenRealmConflicts() {
            when(organizationRepository.findByRealm(EXPECTED_REALM))
                    .thenReturn(Optional.of(new Organization()));

            catchThrowable(() -> createOrganizationUseCase.execute(createDto));

            verifyNoInteractions(keycloakAdmin);
        }
    }

    @Nested
    class WhenAdminEmailAlreadyExists {
        @Test
        void shouldThrowUserSameEmailAlreadyExistsException() {
            when(userRepository.findByEmail(ADMIN_EMAIL))
                    .thenReturn(Optional.of(new User()));

            assertThatThrownBy(() -> createOrganizationUseCase.execute(createDto))
                    .isInstanceOf(UserSameEmailAlreadyExistsException.class);
        }

        @Test
        void shouldNotTouchKeycloakWhenEmailConflicts() {
            when(userRepository.findByEmail(ADMIN_EMAIL))
                    .thenReturn(Optional.of(new User()));

            catchThrowable(() -> createOrganizationUseCase.execute(createDto));

            verifyNoInteractions(keycloakAdmin);
        }
    }

    // =========================================================================
    // Rollback / compensation logic
    // =========================================================================
    @Nested
    class WhenKeycloakRealmCreationFails {
        @Test
        void shouldEvictTenantCache() {
            when(keycloakAdmin.createRealm(anyString(), anyString()))
                    .thenThrow(new RuntimeException("Keycloak Down"));

            catchThrowable(() -> createOrganizationUseCase.execute(createDto));

            verify(orgTenantConfigResolver).evict(EXPECTED_REALM);
        }

        @Test
        void shouldNotDeleteUserBecauseItWasNeverCreated() {
            when(keycloakAdmin.createRealm(anyString(), anyString()))
                    .thenThrow(new RuntimeException("Keycloak down"));

            catchThrowable(() -> createOrganizationUseCase.execute(createDto));

            verify(keycloakAdmin, never()).deleteUser(anyString(), anyString());
        }

        @Test
        void shouldNotDeleteRealmBecauseItWasNeverCreated() {
            when(keycloakAdmin.createRealm(anyString(), anyString()))
                    .thenThrow(new RuntimeException("Keycloak down"));

            catchThrowable(() -> createOrganizationUseCase.execute(createDto));

            verify(keycloakAdmin, never()).deleteRealm(anyString());
        }

        @Test
        void shouldThrowGenericApiException() {
            when(keycloakAdmin.createRealm(anyString(), anyString()))
                    .thenThrow(new RuntimeException("Keycloak down"));

            assertThatThrownBy(() -> createOrganizationUseCase.execute(createDto))
                    .isInstanceOf(GenericApiException.class);
        }
    }

    @Nested
    class WhenKeycloakUserCreationFails {

        @Test
        void shouldDeleteTheRealmThatWasCreated() {
            when(keycloakAdmin.createRealmUser(anyString(), anyString(), anyString(), anyString(), any()))
                    .thenThrow(new RuntimeException("User creation failed"));

            catchThrowable(() -> createOrganizationUseCase.execute(createDto));

            verify(keycloakAdmin).deleteRealm(EXPECTED_REALM);
        }

        @Test
        void shouldNotDeleteUserBecauseItWasNeverCreated() {
            when(keycloakAdmin.createRealmUser(anyString(), anyString(), anyString(), anyString(), any()))
                    .thenThrow(new RuntimeException("User creation failed"));

            catchThrowable(() -> createOrganizationUseCase.execute(createDto));

            verify(keycloakAdmin, never()).deleteUser(anyString(), anyString());
        }

        @Test
        void shouldEvictTenantCache() {
            when(keycloakAdmin.createRealmUser(anyString(), anyString(), anyString(), anyString(), any()))
                    .thenThrow(new RuntimeException("User creation failed"));

            catchThrowable(() -> createOrganizationUseCase.execute(createDto));

            verify(orgTenantConfigResolver).evict(EXPECTED_REALM);
        }

        @Test
        void shouldThrowGenericApiException() {
            when(keycloakAdmin.createRealmUser(anyString(), anyString(), anyString(), anyString(), any()))
                    .thenThrow(new RuntimeException("User creation failed"));

            assertThatThrownBy(() -> createOrganizationUseCase.execute(createDto))
                    .isInstanceOf(GenericApiException.class);
        }
    }

    @Nested
    class WhenSaveUseCaseFails {

        @Test
        void shouldDeleteBothRealmAndUser() {
            when(saveOrganizationUseCase.execute(any(), anyString(), anyString()))
                    .thenThrow(new RuntimeException("DB error"));

            catchThrowable(() -> createOrganizationUseCase.execute(createDto));

            verify(keycloakAdmin).deleteUser(EXPECTED_REALM, USER_KC_ID);
            verify(keycloakAdmin).deleteRealm(EXPECTED_REALM);
        }

        @Test
        void shouldEvictTenantCache() {
            when(saveOrganizationUseCase.execute(any(), anyString(), anyString()))
                    .thenThrow(new RuntimeException("DB error"));

            catchThrowable(() -> createOrganizationUseCase.execute(createDto));

            verify(orgTenantConfigResolver).evict(EXPECTED_REALM);
        }

        @Test
        void shouldRethrowBaseApiExceptionAsIs() {
            var cause = new OrganizationAlreadyExistsException();
            when(saveOrganizationUseCase.execute(any(), anyString(), anyString())).thenThrow(cause);

            assertThatThrownBy(() -> createOrganizationUseCase.execute(createDto)).isSameAs(cause);
        }

        @Test
        void shouldWrapNonBaseApiExceptionInGenericApiException() {
            when(saveOrganizationUseCase.execute(any(), anyString(), anyString()))
                    .thenThrow(new RuntimeException("unexpected"));

            assertThatThrownBy(() -> createOrganizationUseCase.execute(createDto))
                    .isInstanceOf(GenericApiException.class);
        }
    }

    // =========================================================================
    // Realm name derivation (getOrganizationRealm)
    // =========================================================================

    @Nested
    class RealmNameDerivation {

        @Test
        void shouldLowercaseAndReplaceSpacesWithUnderscores() {
            createDto.name = "My Organization";
            createOrganizationUseCase.execute(createDto);
            verify(keycloakAdmin).createRealm(eq("my_organization"), any());
        }

        @Test
        void shouldStripAccents() {
            createDto.name = "Ação Rápida";
            createOrganizationUseCase.execute(createDto);
            verify(keycloakAdmin).createRealm(eq("acao_rapida"), any());
        }

        @Test
        void shouldRemovePunctuationAndSymbols() {
            createDto.name = "Hello, World!";
            createOrganizationUseCase.execute(createDto);
            verify(keycloakAdmin).createRealm(eq("hello_world"), any());
        }

        @Test
        void shouldCollapseMultipleSpacesIntoSingleUnderscore() {
            createDto.name = "Lots   Of   Spaces";
            createOrganizationUseCase.execute(createDto);
            verify(keycloakAdmin).createRealm(eq("lots_of_spaces"), any());
        }

        @Test
        void shouldTrimLeadingAndTrailingWhitespace() {
            createDto.name = "  Trimmed  ";
            createOrganizationUseCase.execute(createDto);
            verify(keycloakAdmin).createRealm(eq("trimmed"), any());
        }

        @Test
        void shouldHandleAlphanumericNamesWithNoChanges() {
            createDto.name = "Acme123";
            createOrganizationUseCase.execute(createDto);
            verify(keycloakAdmin).createRealm(eq("acme123"), any());
        }
    }
}
    