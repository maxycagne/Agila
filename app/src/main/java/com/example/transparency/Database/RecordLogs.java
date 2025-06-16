package com.example.transparency.Database;

import java.io.Serializable;

public class RecordLogs implements Serializable {

    public String logID;
    public String requestType;
    public String requestAmmount;
    public String spendingType;
    public String desc;
    public String politicianID;
    public String recordLogStatus;
    public String recordLogFeedback;
    public String staus;

    public RecordLogs(String logID, String requestType, String requestAmmount, String spendingType, String description, String politicianID, String recordLogStatus, String recordLogFeedback, String staus) {
        this.logID = logID;
        this.requestType = requestType;
        this.requestAmmount = requestAmmount;
        this.spendingType = spendingType;
        this.desc = description;
        this.politicianID = politicianID;
        this.recordLogStatus = recordLogStatus;
        this.recordLogFeedback = recordLogFeedback;
        this.staus = staus;
    }

    public RecordLogs(){}

    public String getLogID() {
        return logID;
    }

    public void setLogID(String logID) {
        this.logID = logID;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getRequestAmmount() {
        return requestAmmount;
    }

    public void setRequestAmmount(String requestAmmount) {
        this.requestAmmount = requestAmmount;
    }

    public String getSpendingType() {
        return spendingType;
    }

    public void setSpendingType(String spendingType) {
        this.spendingType = spendingType;
    }

    public String getDescription() {
        return desc;
    }

    public void setDescription(String description) {
        this.desc = description;
    }

    public String getPoliticianID() {
        return politicianID;
    }

    public void setPoliticianID(String politicianID) {
        this.politicianID = politicianID;
    }

    public String getRecordLogStatus() {
        return recordLogStatus;
    }

    public void setRecordLogStatus(String recordLogStatus) {
        this.recordLogStatus = recordLogStatus;
    }

    public String getRecordLogFeedback() {
        return recordLogFeedback;
    }

    public void setRecordLogFeedback(String recordLogFeedback) {
        this.recordLogFeedback = recordLogFeedback;
    }

    public String getStatus() {
        return staus;
    }

    public void setStatus(String status) {
        this.staus = status;
    }
}
