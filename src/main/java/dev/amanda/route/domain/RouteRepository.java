package dev.amanda.route.domain;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RouteRepository implements PanacheRepository<Route> {
}
