package dev.amanda.infrastructure.shared.rest;

import dev.amanda.infrastructure.oidc.AuthContextProvider;
import io.quarkus.hibernate.orm.PersistenceUnitExtension;
import io.quarkus.hibernate.orm.runtime.tenant.TenantResolver;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.vertx.http.runtime.CurrentVertxRequest;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@PersistenceUnitExtension
@RequestScoped
public class HibernateOrgTenantResolver implements TenantResolver {

    public static final String NO_TENANT = "-1";
    public static final String ORG_HEADER = "X-Org-Id";

    @Inject
    CurrentVertxRequest currentRequest;

    @Inject
    SecurityIdentity identity;

    @Inject
    AuthContextProvider authContextProvider;

    @Override
    public String resolveTenantId() {

    }
}
