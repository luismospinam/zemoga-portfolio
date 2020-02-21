package com.zemoga.portfolio.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Portfolio {

    @Id
    @Column(name = "idportfolio")
    private String idPortfolio;
    private String twitterUserName;
    private String imageUrl;
    private String description;
    private String title;

    public String getIdPortfolio() {
        return idPortfolio;
    }

    public void setIdPortfolio(String idPortfolio) {
        this.idPortfolio = idPortfolio;
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
