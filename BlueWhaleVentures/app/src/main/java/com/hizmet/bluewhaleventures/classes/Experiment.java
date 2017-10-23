package com.hizmet.bluewhaleventures.classes;

/**
 * Created by Bram Korsten on 10/21/2017.
 */

public class Experiment {
    private String title, desc, id, created;
    private int number;

    public Experiment() {
    }

    public Experiment(String title, String desc, int number, String id, String created) {
        this.title = title;
        this.desc = desc;
        this.number = number;
        this.id = id;
        this.created = created;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getDesc() {
        return desc;
    }

}
