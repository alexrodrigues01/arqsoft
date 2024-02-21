package com.isep.acme.model;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class UserViewMapper {

    public abstract UserView toUserView(User user);

    public abstract UserView toUserView(UserNeo4J user);

}
