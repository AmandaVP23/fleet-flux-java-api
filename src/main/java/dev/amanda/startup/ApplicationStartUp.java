package dev.amanda.startup;

import dev.amanda.config.SuperAdminConfig;
import dev.amanda.oidc.KeycloakAdmin;
import dev.amanda.user.domain.User;
import dev.amanda.user.domain.UserRepository;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import com.github.lalyos.jfiglet.FigletFont;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.jbosslog.JBossLog;
import org.jboss.logging.Logger;

@ApplicationScoped
@JBossLog
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
        log.info("The application is starting...");
        String ascii = FigletFont.convertOneLine("FleetFlux");
        System.out.println(GREEN + ascii + RESET);

        createDefaultSuperAdmin();
    }

    @Transactional
    public void createDefaultSuperAdmin() {
        String keycloakId = keycloakAdmin.getUser(superAdminConfig.realm(),  superAdminConfig.email())
                .orElseGet(() -> keycloakAdmin.createSuperAdminUser())
                .getId();

        userRepository.findByEmail(superAdminConfig.email())
                .ifPresentOrElse(
                        user -> log.info("SuperAdmin already exists in DB, skipping creation"),
                        () -> {
                            userRepository.persist(buildSuperAdminUser(keycloakId));
                            log.info("SuperAdmin User created!");
                        }
                );

    }

    private User buildSuperAdminUser(String keycloakId) {
        User superAdmin = new User();
        superAdmin.setKeycloakId(keycloakId);
        superAdmin.setEmail(superAdminConfig.email());
        return superAdmin;
    }
}
