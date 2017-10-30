package com.hizmet.bluewhaleventures.classes;

import java.util.Map;

public class Experiment {
    private Map data;
    private String experimentId = null;

    public Experiment() {
    }

    public Experiment(Map ExperimentData) {
        data = ExperimentData;
    }

    public Map getData() {
        return data;
    }

    public void setExperimentId(String id){
        experimentId = id;
    }

    public String getExperimentId() { return experimentId; }


}
