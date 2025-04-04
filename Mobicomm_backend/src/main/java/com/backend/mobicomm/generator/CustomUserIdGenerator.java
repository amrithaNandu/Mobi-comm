package com.backend.mobicomm.generator;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.Configurable;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Properties;
import java.util.stream.Stream;

public class CustomUserIdGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        String prefix = "us_"; 
        String query = "SELECT u.userid FROM User u"; 

        // Fetch existing IDs from the database
        Stream<String> ids = session.createQuery(query, String.class).stream();

        // Find the maximum numeric part of the ID
        Long max = ids
                .map(id -> id.replace(prefix, "")) // Remove the prefix
                .mapToLong(Long::parseLong) // Convert to Long
                .max() // Find the maximum value
                .orElse(0L); // Default to 0 if no IDs exist

        // Generate the next ID
        return prefix + (max + 1);
    }

    @Override
    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) {
        // Configuration logic (if needed)
    }
}