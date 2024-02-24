package com.mtech.envirotrack.report;

import java.io.Serializable;
import java.util.HashMap;

public class UserReport implements Serializable {
    public String reportNumber;
    public String userName;
    public String userEmail;
    public String impactType;
    public HashMap<String, String> data;

    public UserReport() {

    }

    public UserReport(String reportNumber, String userName, String userEmail, String impactType, HashMap<String, String> data) {
        this.reportNumber = reportNumber;
        this.userName = userName;
        this.userEmail = userEmail;
        this.impactType = impactType;
        this.data = data;
    }

    // getters and setters
    public String getImpactType() {
        return impactType;
    }
}