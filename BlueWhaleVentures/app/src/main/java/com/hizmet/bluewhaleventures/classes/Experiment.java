package com.hizmet.bluewhaleventures.classes;

import java.util.Map;

public class Experiment {
    private Map data;
    private String experimentId = null;

    public Experiment(Map experimentData) {
        this.data = experimentData;
    }

    public Map getData() {
        return data;
    }

    public void setExperimentId(String id) {
        this.experimentId = id;
    }

    public String getExperimentId() {
        return experimentId;
    }
}