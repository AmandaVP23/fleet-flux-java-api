package dev.amanda.organization.application.use_cases;

import dev.amanda.organization.application.mappers.OrganizationMapper;
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

    @InjectMock
    OrganizationMapper organizationMapper;

    @Test
    void shouldReturnDtoWhenOrganizationExists() {
        Long id = 123L;

        Organization organization = new Organization();
        OrganizationResponseDTO organizationResponseDTO = new OrganizationResponseDTO();

        when(organizationRepositoryPanache.findByIdOrThrow(id)).thenReturn(organization);
        when(organizationMapper.toDto(organization)).thenReturn(organizationResponseDTO);

        OrganizationResponseDTO result = getOrganizationByIdUseCase.execute(id);

        assertEquals(organizationResponseDTO, result);
        verify(organizationRepositoryPanache).findByIdOrThrow(id);
        verify(organizationMapper).toDto(organization);
    }
}
