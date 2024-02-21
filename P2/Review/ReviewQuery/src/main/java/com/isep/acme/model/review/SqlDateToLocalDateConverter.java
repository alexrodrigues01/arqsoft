package com.isep.acme.model.review;

import org.neo4j.driver.Value;
import org.neo4j.driver.internal.value.DateValue;
import org.springframework.stereotype.Component;

import java.sql.Date;

import org.springframework.data.neo4j.core.convert.Neo4jPersistentPropertyConverter;

@Component
public class SqlDateToLocalDateConverter implements  Neo4jPersistentPropertyConverter<Date>{

    @Override
    public Value write(Date source) {
        return new DateValue(source.toLocalDate());
    }

    @Override
    public Date read(Value source) {
        DateValue date = (DateValue) source;
        return Date.valueOf(date.asLocalDate());
    }
}
