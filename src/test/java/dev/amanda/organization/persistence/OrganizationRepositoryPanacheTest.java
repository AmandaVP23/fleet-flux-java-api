package dev.amanda.organization.persistence;

import dev.amanda.organization.application.filters.OrganizationStatusFilter;
import dev.amanda.organization.domain.Organization;
import dev.amanda.organization.exceptions.OrganizationNotFoundException;
import io.quarkus.panache.common.Sort;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@QuarkusTest
public class OrganizationRepositoryPanacheTest {
    @Inject
    OrganizationRepositoryPanache repository;

    private static final String ORGANIZATION_NAME = "Friends Organization";
    private static final String ORGANIZATION_REALM = "test-realm";
    private static final String ORGANIZATION_HOSTNAME = "test-hostname";

    private Organization createOrganization(String name, String realm, String hostname, Instant deletedAt) {
        Organization organization = new Organization();
        organization.setName(name);
        organization.setRealm(realm);
        organization.setHostname(hostname);
        organization.setDeletedAt(deletedAt);
        repository.persist(organization);
        return organization;
    }

    private Organization createDefaultOrganization() {
        return createOrganization(ORGANIZATION_NAME, ORGANIZATION_REALM, ORGANIZATION_HOSTNAME, null);
    }

    @Test
    @TestTransaction
    void shouldReturnOrganizationWhenNameExists() {
        createDefaultOrganization();

        var result = repository.findByName(ORGANIZATION_NAME);

        assertThat(result).isPresent();
        assertThat(result.orElseThrow().getName()).isEqualTo(ORGANIZATION_NAME);
    }

    @Test
    @TestTransaction
    void shouldReturnEmptyWhenNameDoesNotExist() {
        createDefaultOrganization();

        var result = repository.findByName("non existing name");

        assertThat(result).isEmpty();
    }

    @Test
    @TestTransaction
    void shouldReturnOrganizationWhenHostnameExists() {
        createDefaultOrganization();

        var result = repository.findByHostname(ORGANIZATION_HOSTNAME);

        assertThat(result).isPresent();
        assertThat(result.orElseThrow().getHostname()).isEqualTo(ORGANIZATION_HOSTNAME);
    }

    @Test
    @TestTransaction
    void shouldReturnEmptyWhenHostnameDoesNotExist() {
        createDefaultOrganization();

        var result = repository.findByHostname("non existing hostname");

        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    @TestTransaction
    void shouldReturnOrganizationWhenRealmExists() {
        createDefaultOrganization();

        var result = repository.findByRealm(ORGANIZATION_REALM);

        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getRealm()).isEqualTo(ORGANIZATION_REALM);
    }

    @Test
    @TestTransaction
    void shouldReturnEmptyWhenRealmDoesNotExist() {
        createDefaultOrganization();

        var result = repository.findByRealm("non existing realm");

        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    @TestTransaction
    void shouldReturnAllOrganizationsWhenFilterIsAll() {
        createOrganization(
                "test one",
                "one-realm",
                "one-hostname",
                null
        );
        createOrganization(
                "test two",
                "two-realm",
                "two-hostname",
                Instant.parse("2026-01-01T22:00:00Z")
        );

        var results = repository.findPaginated(0, 10, Sort.by("name").ascending(), OrganizationStatusFilter.ALL);

        assertThat(results).extracting(Organization::getName).containsExactlyInAnyOrder("test one", "test two");
    }

    @Test
    @TestTransaction
    void shouldReturnOnlyActivesOrganizationsWhenFilterIsActive() {
        createOrganization(
                "test one",
                "one-realm",
                "one-hostname",
                null
        );
        createOrganization(
                "test two",
                "two-realm",
                "two-hostname",
                Instant.parse("2026-01-01T22:00:00Z")
        );

        var results = repository.findPaginated(0, 10, Sort.by("name").ascending(), OrganizationStatusFilter.ACTIVE);

        assertThat(results).extracting(Organization::getName).containsExactlyInAnyOrder("test one");
    }

    @Test
    @TestTransaction
    void shouldReturnOnlyDeletedOrganizationsWhenFilterIsDeleted() {
        createOrganization(
                "test one",
                "one-realm",
                "one-hostname",
                null
        );
        createOrganization(
                "test two",
                "two-realm",
                "two-hostname",
                Instant.parse("2026-01-01T22:00:00Z")
        );

        var results = repository.findPaginated(0, 10, Sort.by("name").ascending(), OrganizationStatusFilter.DELETED);

        assertThat(results).extracting(Organization::getName).containsExactlyInAnyOrder("test two");
    }

    @Test
    @TestTransaction
    void shouldReturnCorrectCountWhenFilterIsAll() {
        createOrganization(
                "test one",
                "one-realm",
                "one-hostname",
                null
        );
        createOrganization(
                "test two",
                "two-realm",
                "two-hostname",
                Instant.parse("2026-01-01T22:00:00Z")
        );

        var countResult = repository.count(OrganizationStatusFilter.ALL);

        assertThat(countResult).isEqualTo(2);
    }

    @Test
    @TestTransaction
    void shouldReturnCorrectCountWhenFilterIsActive() {
        createOrganization(
                "test one",
                "one-realm",
                "one-hostname",
                null
        );
        createOrganization(
                "test two",
                "two-realm",
                "two-hostname",
                Instant.now()
        );

        var countResult = repository.count(OrganizationStatusFilter.ACTIVE);

        assertThat(countResult).isEqualTo(1);
    }

    @Test
    @TestTransaction
    void shouldReturnCorrectCountWhenFilterIsDeleted() {
        createOrganization(
                "test one",
                "one-realm",
                "one-hostname",
                null
        );
        createOrganization(
                "test two",
                "two-realm",
                "two-hostname",
                Instant.now()
        );

        var countResult = repository.count(OrganizationStatusFilter.DELETED);

        assertThat(countResult).isEqualTo(1);
    }

    @Test
    @TestTransaction
    void shouldReturnOrganizationWhenOrganizationByIdExists() {
        Organization organization = createDefaultOrganization();

        var result = repository.findByIdOrThrow(organization.getId());

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(organization.getId());
        assertThat(result.getName()).isEqualTo(organization.getName());
    }

    @Test
    void shouldThrowWhenOrganizationByIdDoesNotExist() {
        assertThatThrownBy(() -> repository.findByIdOrThrow(123456L))
            .isInstanceOf(OrganizationNotFoundException.class);
    }

    @Test
    @TestTransaction
    void shouldReturnOrganizationWhenOrganizationByIdAndIsActiveExists() {
        Organization organization = createDefaultOrganization();

        var result = repository.findActiveByIdOrThrow(organization.getId());

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(organization.getId());
        assertThat(result.getName()).isEqualTo(organization.getName());
    }

    @Test
    @TestTransaction
    void shouldThrowWhenOrganizationExistButIsNotActive() {
        Organization organization = createOrganization("test one", "realm", "hostname", Instant.parse("2026-01-01T22:00:00Z"));

        assertThatThrownBy(() -> repository.findActiveByIdOrThrow(organization.getId()))
                .isInstanceOf(OrganizationNotFoundException.class);
    }

    @Test
    void shouldThrowWhenOrganizationDoesNotExistAndIsNotActive() {
        assertThatThrownBy(() -> repository.findActiveByIdOrThrow(123456L))
                .isInstanceOf(OrganizationNotFoundException.class);
    }
}
