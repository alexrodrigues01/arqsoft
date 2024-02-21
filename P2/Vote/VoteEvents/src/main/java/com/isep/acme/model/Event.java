package com.isep.acme.model;

import com.isep.acme.utils.TypeOfEvent;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Event {

    @Id
    private String eventId;
    private TypeOfEvent typeOfEvent;

    private  Object entity;

    private  String domain;

    public Event(TypeOfEvent typeOfEvent, String domain, Object entity) {
        this.typeOfEvent = typeOfEvent;
        this.domain=domain;
        this.entity = entity;
    }

    public String getEventId() {
        return eventId;
    }

    public TypeOfEvent getTypeOfEvent() {
        return typeOfEvent;
    }

    public Object getEntity() {
        return entity;
    }

    public String getDomain() {
        return domain;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public void setTypeOfEvent(TypeOfEvent typeOfEvent) {
        this.typeOfEvent = typeOfEvent;
    }

    public void setEntity(Object entity) {
        this.entity = entity;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
