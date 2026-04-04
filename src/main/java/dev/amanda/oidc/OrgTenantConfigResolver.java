package dev.amanda.oidc;

import io.quarkus.oidc.OidcRequestContext;
import io.quarkus.oidc.OidcTenantConfig;
import io.quarkus.oidc.OidcTenantConfigBuilder;
import io.quarkus.oidc.TenantConfigResolver;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Base64;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class OrgTenantConfigResolver implements TenantConfigResolver {

    private final ConcurrentHashMap<String, OidcTenantConfig> cache = new ConcurrentHashMap<>();

    @Inject
    KeycloakConfig keycloakConfig;

    @Override
    public Uni<OidcTenantConfig> resolve(RoutingContext routingContext, OidcRequestContext<OidcTenantConfig> requestContext) {
        String tenantId = extractTenantId(routingContext);

        if (tenantId == null) {
            return Uni.createFrom().nullItem();
        }

        return Uni.createFrom().item(
                cache.computeIfAbsent(tenantId, this::buildTenantConfig)
        );
    }

    private String extractTenantId(RoutingContext routingContext) {
        String issuer = extractIssuerFromToken(routingContext);
        if (issuer != null) {
            // issuer = "http://keycloak:8080/realms/acme"  →  extract "acme"
            return issuer.substring(issuer.lastIndexOf("/") + 1);
        }

        return null; // default tenant
    }

    private OidcTenantConfig buildTenantConfig(String realmName) {
        OidcTenantConfigBuilder builder = OidcTenantConfig.builder()
                .tenantId(realmName)
                .authServerUrl(keycloakConfig.serverUrl() + "/realms/" + realmName)
                .clientId("web");
//                .applicationType(OidcTenantConfig.ApplicationType.WEB_APP);

        return builder.build();
    }

    public void evictAndReload(String realmName) {
        cache.put(realmName, buildTenantConfig(realmName));
    }

    private String extractIssuerFromToken(RoutingContext routingContext) {
        try {
            String auth = routingContext.request().getHeader("Authorization");

            if (auth == null || !auth.startsWith("Bearer ")) {
                return null;
            }

            String token = auth.substring("Bearer ".length());

            String[] parts = token.split("\\.");

            if (parts.length < 2) {
                return null;
            }

            String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
            JsonObject claims = new JsonObject(payload);

            return claims.getString("iss");
        } catch (Exception e) {
            return null;
        }
    }

    public void evict(String realmName) {
        cache.remove(realmName);
    }
}