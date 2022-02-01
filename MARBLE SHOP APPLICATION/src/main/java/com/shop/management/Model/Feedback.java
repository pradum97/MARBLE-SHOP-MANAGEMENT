package com.shop.management.Model;

public class Feedback {

    private int feed_id;
    private String feed_phone, fullName, email, star, message, feedDate;


    public Feedback(int feed_id, String feed_phone, String fullName, String email,
                    String star, String message, String feedDate) {
        this.feed_id = feed_id;
        this.feed_phone = feed_phone;
        this.fullName = fullName;
        this.email = email;
        this.star = star;
        this.message = message;
        this.feedDate = feedDate;
    }

    public int getFeed_id() {
        return feed_id;
    }

    public void setFeed_id(int feed_id) {
        this.feed_id = feed_id;
    }

    public String getFeed_phone() {
        return feed_phone;
    }

    public void setFeed_phone(String feed_phone) {
        this.feed_phone = feed_phone;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFeedDate() {
        return feedDate;
    }

    public void setFeedDate(String feedDate) {
        this.feedDate = feedDate;
    }
}
