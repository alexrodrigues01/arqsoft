package com.isep.acme.rabbitmq;

import java.io.Serializable;

public enum TypeOfEvent implements Serializable {
    CREATE,
    DELETE,
    UPDATE,

    GET
}
