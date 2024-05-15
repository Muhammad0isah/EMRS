package com.mtech.envirotrack.report;

import java.io.Serializable;
import java.util.HashMap;

public class UserReport implements Serializable {
    private String reportNumber;
    private String userName;
    private String userEmail;
    private String impactType;
    private String status;

    private HashMap<String, String> data;

    public UserReport() {
        // Default constructor required for calls to DataSnapshot.getValue(UserReport.class)
    }




    public String getStatus() {
            return status;
    }

    public void setStatus(String status) {
            this.status = status;
    }


    public UserReport(String reportNumber, String userName, String userEmail, String impactType, HashMap<String, String> data,String status) {
        this.reportNumber = reportNumber;
        this.userName = userName;
        this.userEmail = userEmail;
        this.impactType = impactType;
        this.data = data;
        this.status = status;

    }

    // Getters and setters
    public String getReportNumber() {
        return reportNumber;
    }


    public void setReportNumber(String reportNumber) {
        this.reportNumber = reportNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getImpactType() {
        return impactType;
    }

    public void setImpactType(String impactType) {
        this.impactType = impactType;
    }

    public HashMap<String, String> getData() {
        return data;
    }

    public void setData(HashMap<String, String> data) {
        this.data = data;
    }
}