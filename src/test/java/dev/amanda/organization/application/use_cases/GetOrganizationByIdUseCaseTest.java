package dev.amanda.organization.application.use_cases;

import dev.amanda.organization.application.mappers.OrganizationMapper;
import dev.amanda.organization.domain.Organization;
import dev.amanda.organization.domain.OrganizationRepository;
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
    OrganizationRepository organizationRepository;

    @InjectMock
    OrganizationMapper organizationMapper;

    @Test
    void shouldReturnDtoWhenOrganizationExists() {
        Long id = 123L;

        Organization organization = new Organization();
        OrganizationResponseDTO organizationResponseDTO = new OrganizationResponseDTO();

        when(organizationRepository.findByIdOrThrow(id)).thenReturn(organization);
        when(organizationMapper.toDto(organization)).thenReturn(organizationResponseDTO);

        OrganizationResponseDTO result = getOrganizationByIdUseCase.execute(id);

        assertEquals(organizationResponseDTO, result);
        verify(organizationRepository).findByIdOrThrow(id);
        verify(organizationMapper).toDto(organization);
    }
}
