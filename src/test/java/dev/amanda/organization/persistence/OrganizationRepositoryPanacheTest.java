package dev.amanda.organization.persistence;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.connection.ConnectionHolder;
import com.github.database.rider.core.api.dataset.DataSet;
import dev.amanda.organization.application.filters.OrganizationStatusFilter;
import dev.amanda.organization.domain.Organization;
import dev.amanda.organization.exceptions.OrganizationNotFoundException;
import io.agroal.api.AgroalDataSource;
import io.quarkus.panache.common.Sort;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.database.rider.junit5.api.DBRider;

@QuarkusTest
@DBRider
@DataSet(value = "datasets/organizations.yml", cleanBefore = true)
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE, schema = "public")
public class OrganizationRepositoryPanacheTest {
    @Inject
    OrganizationRepositoryPanache repository;

    @Inject
    AgroalDataSource dataSource;

    ConnectionHolder connectionHolder = () -> dataSource.getConnection();

    @Test
    void shouldReturnOrganizationWhenNameExists() {
        var result = repository.findByName("Friends");

        assertThat(result).isPresent();
        assertThat(result.orElseThrow().getName()).isEqualTo("Friends");
    }

    @Test
    void shouldReturnEmptyWhenNameDoesNotExist() {
        var result = repository.findByName("non existing name");

        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnOrganizationWhenHostnameExists() {
        var result = repository.findByHostname("friends-hostname");

        assertThat(result).isPresent();
        assertThat(result.orElseThrow().getHostname()).isEqualTo("friends-hostname");
    }

    @Test
    void shouldReturnEmptyWhenHostnameDoesNotExist() {
        var result = repository.findByHostname("non existing hostname");

        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    void shouldReturnOrganizationWhenRealmExists() {
        var result = repository.findByRealm("friends-realm");

        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getRealm()).isEqualTo("friends-realm");
    }

    @Test
    void shouldReturnEmptyWhenRealmDoesNotExist() {
        var result = repository.findByRealm("non existing realm");

        assertThat(result.isEmpty()).isTrue();
    }

    @Nested
    class FindPaginated {
        @Test
        void shouldReturnAllOrganizationsWhenFilterIsAll() {
            var results = repository.findPaginated(0, 10, Sort.by("name").ascending(), OrganizationStatusFilter.ALL);

            assertThat(results).extracting(Organization::getName).containsExactlyInAnyOrder("Friends", "The Big Bang Theory");
        }

        @Test
        void shouldReturnOnlyActivesOrganizationsWhenFilterIsActive() {
            var results = repository.findPaginated(0, 10, Sort.by("name").ascending(), OrganizationStatusFilter.ACTIVE);

            assertThat(results).extracting(Organization::getName).containsExactlyInAnyOrder("Friends");
        }

        @Test
        void shouldReturnOnlyDeletedOrganizationsWhenFilterIsDeleted() {
            var results = repository.findPaginated(0, 10, Sort.by("name").ascending(), OrganizationStatusFilter.DELETED);

            assertThat(results).extracting(Organization::getName).containsExactlyInAnyOrder("The Big Bang Theory");
        }
    }

    @Nested
    class Count {
        @Test
        void shouldReturnCorrectCountWhenFilterIsAll() {
            var countResult = repository.count(OrganizationStatusFilter.ALL);

            assertThat(countResult).isEqualTo(2);
        }

        @Test
        void shouldReturnCorrectCountWhenFilterIsActive() {
            var countResult = repository.count(OrganizationStatusFilter.ACTIVE);

            assertThat(countResult).isEqualTo(1);
        }

        @Test
        void shouldReturnCorrectCountWhenFilterIsDeleted() {
            var countResult = repository.count(OrganizationStatusFilter.DELETED);

            assertThat(countResult).isEqualTo(1);
        }
    }

    @Nested
    class FindByIdOrThrow {
        @Test
        void shouldReturnOrganizationWhenOrganizationExists() {
            var result = repository.findByIdOrThrow(1L);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getName()).isEqualTo("Friends");
        }

        @Test
        void shouldThrowWhenOrganizationByIdDoesNotExist() {
            assertThatThrownBy(() -> repository.findByIdOrThrow(123456L))
                    .isInstanceOf(OrganizationNotFoundException.class);
        }
    }

    @Nested
    class FindActiveByIdOrThrow {
        @Test
        void shouldReturnOrganizationWhenOrganizationByIdAndIsActiveExists() {
            var result = repository.findActiveByIdOrThrow(1L);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getName()).isEqualTo("Friends");
        }

        @Test
        void shouldThrowWhenOrganizationExistButIsNotActive() {
            assertThatThrownBy(() -> repository.findActiveByIdOrThrow(2L))
                    .isInstanceOf(OrganizationNotFoundException.class);
        }

        @Test
        void shouldThrowWhenOrganizationDoesNotExistAndIsNotActive() {
            assertThatThrownBy(() -> repository.findActiveByIdOrThrow(123456L))
                    .isInstanceOf(OrganizationNotFoundException.class);
        }
    }
}
