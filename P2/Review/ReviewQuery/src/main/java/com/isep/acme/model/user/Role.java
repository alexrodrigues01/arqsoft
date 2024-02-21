package com.isep.acme.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Node
public class Role implements GrantedAuthority, Serializable {

    public static final String Admin = "Admin";

    public static final String Mod = "Mod";

    public static final String RegisteredUser = "RegisteredUser";

    @org.springframework.data.annotation.Id
    private String authority;
}
