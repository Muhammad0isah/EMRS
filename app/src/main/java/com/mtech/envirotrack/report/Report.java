package com.mtech.envirotrack.report;

import java.io.Serializable;
import java.util.HashMap;

public class Report implements Serializable {
    public String impactType;
    public HashMap<String, String> data;

    public Report(String impactType, HashMap<String, String> data) {
        this.impactType = impactType;
        this.data = data;
    }

    // getters and setters
}