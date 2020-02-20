package com.zemoga.portfolio.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Portfolio {

    @Id
    private int idportfolio;
    private String twitterUserName;
    private String imageUrl;
    private String description;
    private String title;

    public int getIdportfolio() {
        return idportfolio;
    }

    public void setIdportfolio(int idportfolio) {
        this.idportfolio = idportfolio;
    }

    public String getTwitterUserName() {
        return twitterUserName;
    }

    public void setTwitterUserName(String twitterUserName) {
        this.twitterUserName = twitterUserName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
