package dev.amanda.startup;

import dev.amanda.config.SuperAdminConfig;
import dev.amanda.oidc.KeycloakAdmin;
import dev.amanda.user.domain.Role;
import dev.amanda.user.domain.User;
import dev.amanda.user.domain.UserRepository;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import com.github.lalyos.jfiglet.FigletFont;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.keycloak.representations.idm.UserRepresentation;

@ApplicationScoped
public class ApplicationStartUp {
    public static final String GREEN = "\u001B[32m";
    public static final String RESET = "\u001B[0m";

    @Inject
    SuperAdminConfig superAdminConfig;

    @Inject
    KeycloakAdmin keycloakAdmin;

    @Inject
    UserRepository userRepository;

    void onStart(@Observes StartupEvent ev) throws Exception {
        System.out.println("The application is starting...");
        String ascii = FigletFont.convertOneLine("FleetFlux");
        System.out.println(GREEN + ascii + RESET);

        createDefaultSuperAdmin();
    }

    @Transactional
    public void createDefaultSuperAdmin() {
        String keycloakId = keycloakAdmin.getSuperAdminUser()
                .orElseGet(() -> keycloakAdmin.createSuperAdminUser())
                .getId();

        userRepository.findByEmail(superAdminConfig.email())
                .ifPresentOrElse(
                        user -> System.out.println("SuperAdmin already exists in DB, skipping creation"),
                        () -> {
                            userRepository.persist(buildSuperAdminUser(keycloakId));
                            System.out.println("SuperAdmin User created!");
                        }
                );

    }

    private User buildSuperAdminUser(String keycloakId) {
        User superAdmin = new User();
        superAdmin.setKeycloakId(keycloakId);
        superAdmin.setEmail(superAdminConfig.email());
        superAdmin.setUsername(superAdminConfig.username());
        superAdmin.setRole(Role.SUPER_ADMIN);
        return superAdmin;
    }
}
