package dev.amanda.startup;

import dev.amanda.infrastructure.config.SuperAdminConfig;
import dev.amanda.infrastructure.oidc.KeycloakAdmin;
import dev.amanda.user.domain.Role;
import dev.amanda.user.domain.User;
import dev.amanda.user.persistence.UserRepositoryPersistence;
import io.quarkus.runtime.LaunchMode;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import com.github.lalyos.jfiglet.FigletFont;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.java.Log;
import org.keycloak.representations.idm.RealmRepresentation;

import java.util.Optional;

@ApplicationScoped
@Log
public class ApplicationStartUp {
    public static final String GREEN = "\u001B[32m";
    public static final String RESET = "\u001B[0m";

    @Inject
    SuperAdminConfig superAdminConfig;

    @Inject
    KeycloakAdmin keycloakAdmin;

    @Inject
    UserRepositoryPersistence userRepositoryPersistence;

    void onStart(@Observes StartupEvent ev) throws Exception {
        log.info("The application is starting...");

        System.out.println("Launch mode: " + LaunchMode.current());

        String ascii = FigletFont.convertOneLine("FleetFlux");
        System.out.println(GREEN + ascii + RESET);

        if (LaunchMode.current() == LaunchMode.TEST) {
            return;
        }

        createAdminRealmIfNotExist();
        createDefaultSuperAdmin();
    }

    public void createAdminRealmIfNotExist() {
        Optional<RealmRepresentation> realmRepresentation = keycloakAdmin.getRealm(superAdminConfig.realm());
        if (realmRepresentation.isEmpty()) {
            log.info("Creating admin realm");
            keycloakAdmin.createRealm(superAdminConfig.realm(), "Fleet Flux Admin");
            log.info("Created realm: " + superAdminConfig.realm());
        } else {
            log.info(superAdminConfig.realm() + "realm already exists");
        }
    }

    @Transactional
    public void createDefaultSuperAdmin() {
        String keycloakId = keycloakAdmin.getUser(superAdminConfig.realm(),  superAdminConfig.email())
                .orElseGet(() -> keycloakAdmin.createSuperAdminUser())
                .getId();

        userRepositoryPersistence.findByEmail(superAdminConfig.email())
                .ifPresentOrElse(
                        user -> log.info("SuperAdmin already exists in DB, skipping creation"),
                        () -> {
                            try {
                                userRepositoryPersistence.persist(buildSuperAdminUser(keycloakId));
                                log.info("SuperAdmin User created!");
                            } catch (Exception e) {
                                log.severe("SuperAdmin User creation failed!" + e.getMessage());
                            }
                        }
                );

    }

    private User buildSuperAdminUser(String keycloakId) {
        User superAdmin = new User();
        superAdmin.setKeycloakId(keycloakId);
        superAdmin.setEmail(superAdminConfig.email());
        superAdmin.setFirstName(superAdminConfig.firstName());
        superAdmin.setLastName(superAdminConfig.lastName());
        superAdmin.setRole(Role.SUPER_ADMIN);
        return superAdmin;
    }
}
