package com.isep.acme.configs;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;

import javax.annotation.PostConstruct;

public class Neo4JPostConstruct {

    @PostConstruct
    public void createUniqueConstraints(final Driver driver) {
        try (Session session = driver.session()) {
            session.writeTransaction(tx -> {
                tx.run("CREATE CONSTRAINT sku_unique IF NOT EXISTS ON (p:Product) ASSERT p.sku IS UNIQUE");
                return null;
            });
        }
    }
}