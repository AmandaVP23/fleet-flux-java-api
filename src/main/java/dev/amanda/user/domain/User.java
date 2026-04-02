package dev.amanda.user.domain;


import dev.amanda.organization.domain.Organization;
import dev.amanda.shared.domain.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "app_user")
public class User extends BaseEntity {

    @Column(nullable = false)
    private String email;

    @Column(name = "keycloak_id", nullable = false, unique = true)
    private String keycloakId;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    public String getKeycloakId() {
        return keycloakId;
    }

    public void setKeycloakId(String keycloakId) {
        this.keycloakId = keycloakId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }
}
