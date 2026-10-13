package support;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class TestConfig {

    private static final String PROFILE = System.getProperty("quarkus.test.integration-test-profile", "it");

    private static final Properties PROPS = load();

    private static Properties load() {
        Properties p = new Properties();
        try (InputStream in = TestConfig.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (in == null) {
                throw new IllegalStateException("Cannot find application.properties");
            }
            p.load(in);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        return p;
    }

    public static String get(String key) {
        String v = PROPS.getProperty("%" + PROFILE + "." + key);
        if (v == null) {
            v = PROPS.getProperty(key);
        }
        if (v == null) {
            throw new IllegalStateException(String.format("Missing value for %s.%s", PROFILE, key));
        }
        return v.trim();
    }
}
