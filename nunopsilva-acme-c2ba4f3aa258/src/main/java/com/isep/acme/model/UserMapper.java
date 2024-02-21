package com.isep.acme.model;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class UserMapper {
    public abstract User toUser(UserNeo4J user);
    public abstract UserNeo4J toUserNeo4J(User user);
}
