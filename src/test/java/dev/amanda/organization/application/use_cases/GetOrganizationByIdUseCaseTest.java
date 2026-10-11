package dev.amanda.organization.application.use_cases;

import dev.amanda.organization.domain.Organization;
import dev.amanda.organization.persistence.OrganizationRepositoryPanache;
import dev.amanda.organization.dto.OrganizationResponseDTO;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusTest
public class GetOrganizationByIdUseCaseTest {
    @Inject
    GetOrganizationByIdUseCase getOrganizationByIdUseCase;

    @InjectMock
    OrganizationRepositoryPanache organizationRepositoryPanache;

    @Test
    void shouldReturnDtoWhenOrganizationExists() {
        Long id = 123L;

        Organization organization = new Organization();

        when(organizationRepositoryPanache.findActiveByIdOrThrow(id)).thenReturn(organization);

        OrganizationResponseDTO result = getOrganizationByIdUseCase.execute(id);

        assertEquals(organization.getName(), result.getName());
        verify(organizationRepositoryPanache).findActiveByIdOrThrow(id);
    }
}
