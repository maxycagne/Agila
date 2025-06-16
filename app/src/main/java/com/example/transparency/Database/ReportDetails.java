package com.example.transparency.Database;

import java.io.Serializable;

public class ReportDetails implements Serializable {
    private String reportedId;
    private String reportedFrom;
    private String reportedTo;

    private String typeOfReport;

    private String typeOfDescription;

    private String imageEvidence;

    public ReportDetails() {
    }


    public ReportDetails(String reportedId, String reportedFrom, String reportedTo, String typeOfReport, String typeOfDescription, String imageEvidence) {
        this.reportedId = reportedId;
        this.reportedFrom = reportedFrom;
        this.reportedTo = reportedTo;
        this.typeOfReport = typeOfReport;
        this.typeOfDescription = typeOfDescription;
        this.imageEvidence = imageEvidence;
    }

    public String getReportedId() {
        return reportedId;
    }

    public void setReportedId(String reportedId) {
        this.reportedId = reportedId;
    }

    public String getReportedFrom() {
        return reportedFrom;
    }

    public void setReportedFrom(String reportedFrom) {
        this.reportedFrom = reportedFrom;
    }

    public String getReportedTo() {
        return reportedTo;
    }

    public void setReportedTo(String reportedTo) {
        this.reportedTo = reportedTo;
    }

    public String getTypeOfReport() {
        return typeOfReport;
    }

    public void setTypeOfReport(String typeOfReport) {
        this.typeOfReport = typeOfReport;
    }

    public String getTypeOfDescription() {
        return typeOfDescription;
    }

    public void setTypeOfDescription(String typeOfDescription) {
        this.typeOfDescription = typeOfDescription;
    }

    public String getImageEvidence() {
        return imageEvidence;
    }

    public void setImageEvidence(String imageEvidence) {
        this.imageEvidence = imageEvidence;
    }
}
