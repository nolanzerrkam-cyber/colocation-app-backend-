package be.dikkenek.colocationbackend.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.HashMap;
import java.util.Map;

public class DatabaseConfig {
    private static final String PERSISTENCE_UNIT_NAME = "colocation-backend";
    private static EntityManagerFactory factory;

    private static EntityManagerFactory getEntityManagerFactory() {
        if (factory == null || !factory.isOpen()) {
            Map<String, String> props = new HashMap<>();
            props.put("jakarta.persistence.jdbc.url", System.getenv("DB_URL"));
            props.put("jakarta.persistence.jdbc.user", System.getenv("DB_USERNAME"));
            props.put("jakarta.persistence.jdbc.password", System.getenv("DB_PASSWORD"));
            factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME, props);
        }
        return factory;
    }

    public static EntityManager createEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }

    public static void close() {
        if (factory != null && factory.isOpen()) {
            factory.close();
        }
    }
}
