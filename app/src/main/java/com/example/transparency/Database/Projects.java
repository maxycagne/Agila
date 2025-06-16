package com.example.transparency.Database;

import java.io.Serializable;

public class Projects implements Serializable {
    private String projectId;
    private String projectName;

    private String projectImage;

    private String projectLocation;

    private String startDate;

    private String endDate;

    private String dateOfCompletion;

    private String description;

    private int projectRating;

    private String status;

    private String projectOf;

    private String feedBack;

    private Double budget;

    public Projects() {
        // Default constructor required for calls to DataSnapshot.getValue(Projects.class)
    }

    public Projects(String projectId, String projectName, String projectImage, String projectLocation, String startDate, String endDate, String dateOfCompletion, String description, int projectRating, String status, String projectOf, String feedBack, Double budget) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.projectImage = projectImage;
        this.projectLocation = projectLocation;
        this.startDate = startDate;
        this.endDate = endDate;
        this.dateOfCompletion = dateOfCompletion;
        this.description = description;
        this.projectRating = projectRating;
        this.status = status;
        this.projectOf = projectOf;
        this.feedBack = feedBack;
        this.budget = budget;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectImage() {
        return projectImage;
    }

    public void setProjectImage(String projectImage) {
        this.projectImage = projectImage;
    }

    public String getProjectLocation() {
        return projectLocation;
    }

    public void setProjectLocation(String projectLocation) {
        this.projectLocation = projectLocation;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getDateOfCompletion() {
        return dateOfCompletion;
    }

    public void setDateOfCompletion(String dateOfCompletion) {
        this.dateOfCompletion = dateOfCompletion;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getProjectRating() {
        return projectRating;
    }

    public void setProjectRating(int projectRating) {
        this.projectRating = projectRating;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProjectOf() {
        return projectOf;
    }

    public void setProjectOf(String projectOf) {
        this.projectOf = projectOf;
    }

    public String getFeedBack() {
        return feedBack;
    }

    public void setFeedBack(String feedBack) {
        this.feedBack = feedBack;
    }

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }
}
