package com.isep.acme.repositories;

public interface Idable<ID> {
    ID getId();
    ID generateId();
}
