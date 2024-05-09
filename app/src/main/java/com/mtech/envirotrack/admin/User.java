package com.mtech.envirotrack.admin;

import java.util.Map;

public class User {
    private String reportNumber;
    private String userEmail;
    private String userName;
    private String impactType;
    private int serialNumber;
    private String status;


    private Map<String, String> attachments;
    private Map<String, String> data;


    public User(String reportNumber, String userEmail, String userName, String impactType, String status, Map<String, String> data, Map<String, String> attachments) {
        this.reportNumber = reportNumber;
        this.userEmail = userEmail;
        this.userName = userName;
        this.impactType = impactType;
        this.status = status;
        this.data = data;
        this.attachments = attachments;

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

    public Map<String, String> getAttachments() {
        return attachments;
    }
    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Report Number: " + reportNumber + "\n" +
                "User Email: " + userEmail + "\n" +
                "User Name: " + userName + "\n" +
                "Impact Type: " + impactType + "\n" +
                "Status: " + status + "\n" +
                "Data: " + (data == null ? "null" : data.toString()) + "\n\n";
    }
    public String formatData() {
        StringBuilder sb = new StringBuilder();
        if (data != null) {
            for (Map.Entry<String, String> entry : data.entrySet()) {
                sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
        }
        return sb.toString();
    }
}