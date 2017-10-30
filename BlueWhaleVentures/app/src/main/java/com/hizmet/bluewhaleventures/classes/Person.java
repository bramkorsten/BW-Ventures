package com.hizmet.bluewhaleventures.classes;

import java.util.Map;

/**
 * Created by Bram Korsten on 10/27/2017.
 */

public class Person {
    private Map data;
    private String personId;

    public Person() {
    }

    public Person(Map personData) {
        data = personData;
    }

    public Map getData() {
        return data;
    }

    public void setPersonId(String id){
        this.personId = id;
    }

    public String getPersonId(){ return this.personId;}
}
