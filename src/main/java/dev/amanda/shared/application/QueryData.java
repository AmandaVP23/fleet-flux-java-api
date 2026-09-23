package dev.amanda.shared.application;

import java.util.Map;

public record QueryData(
        String query,
        Map<String, Object> params
) {}
