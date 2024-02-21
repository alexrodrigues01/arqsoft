package com.isep.acme.model;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.security.core.GrantedAuthority;

@Value
@AllArgsConstructor
@Node("Role")

public class RoleNeo4J implements GrantedAuthority {

    public static final String Admin = "Admin";

    public static final String Mod = "Mod";

    public static final String RegisteredUser = "RegisteredUser";

    @Property
    private String authority;

    @Id
    @GeneratedValue
    private Long id;
}
