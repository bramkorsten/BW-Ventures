package com.hizmet.bluewhaleventures.classes;

import java.util.Map;

public class Person {
    private Map data;
    private String personId;

    public Person(Map personData) {
        this.data = personData;
    }

    public Map getData() {
        return data;
    }

    public void setPersonId(String id) {
        this.personId = id;
    }

    public String getPersonId() {
        return this.personId;
    }
}