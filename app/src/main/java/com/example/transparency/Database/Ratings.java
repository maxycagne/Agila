package com.example.transparency.Database;

import java.io.Serializable;

public class Ratings implements Serializable {

    public String ratingID;
    public String rating;
    public String ratingByID;
    public String ratingToID;
    public String message;

    public Ratings() {

    }

    public Ratings(String ratingID, String rating, String ratingByID, String ratingToID, String message) {
        this.ratingID = ratingID;
        this.rating = rating;
        this.ratingByID = ratingByID;
        this.ratingToID = ratingToID;
        this.message = message;
    }

    public String getRatingID() {
        return ratingID;
    }

    public void setRatingID(String ratingID) {
        this.ratingID = ratingID;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRatingByID() {
        return ratingByID;
    }

    public void setRatingByID(String ratingByID) {
        this.ratingByID = ratingByID;
    }

    public String getRatingToID() {
        return ratingToID;
    }

    public void setRatingToID(String ratingToID) {
        this.ratingToID = ratingToID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
