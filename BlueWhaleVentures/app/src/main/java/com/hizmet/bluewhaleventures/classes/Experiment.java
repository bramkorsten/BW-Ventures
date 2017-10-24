package com.hizmet.bluewhaleventures.classes;

import java.util.Map;

/**
 * Created by Bram Korsten on 10/21/2017.
 */

public class Experiment {
    private Map data;

    public Experiment() {
    }

    public Experiment(Map ExperimentData) {
        data = ExperimentData;
    }

    public Map getData() {
        return data;
    }


}
