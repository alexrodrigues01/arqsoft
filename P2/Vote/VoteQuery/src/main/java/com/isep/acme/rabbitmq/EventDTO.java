package com.isep.acme.rabbitmq;

import java.io.Serializable;

public class EventDTO implements Serializable {


    private final TypeOfEvent typeOfEvent;

    private final Object entity;

    private final String domain;

    public EventDTO(TypeOfEvent typeOfEvent,String domain, Object entity) {
        this.typeOfEvent = typeOfEvent;
        this.domain=domain;
        this.entity = entity;
    }

    public TypeOfEvent getTypeOfEvent() {
        return typeOfEvent;
    }

    public String getDomain() {
        return domain;
    }

    public Object getEntity() {
        return entity;
    }

    @Override
    public String toString() {
        return "EventDTO{" +
                "typeOfEvent='" + typeOfEvent + '\'' +
                ", entity=" + entity +
                ", domain='" + domain + '\'' +
                '}';
    }
}

