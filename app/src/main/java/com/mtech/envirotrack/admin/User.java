package com.mtech.envirotrack.admin;

import java.util.Map;

public class User {
    private String reportNumber;
    private String userEmail;
    private String userName;
    private String impactType;
    private int serialNumber;
    private String attachment;
    private Map<String, String> data;


    public User(String reportNumber, String userEmail, String userName, String impactType, Map<String, String> data, String attachment) {
        this.reportNumber = reportNumber;
        this.userEmail = userEmail;
        this.userName = userName;
        this.impactType = impactType;
        this.data = data;
        this.attachment = attachment;

    }

    public String getReportNumber() {
        return reportNumber;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public String getImpactType() {
        return impactType;
    }

    public Map<String, String> getData() {
        return data;
    }

    public String getAttachment() {
        return attachment;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    @Override
    public String toString() {
        return "Report Number: " + reportNumber + "\n" +
                "User Email: " + userEmail + "\n" +
                "User Name: " + userName + "\n" +
                "Impact Type: " + impactType + "\n" +
                "Data: " + (data == null ? "null" : data.toString()) + "\n\n";
    }
}