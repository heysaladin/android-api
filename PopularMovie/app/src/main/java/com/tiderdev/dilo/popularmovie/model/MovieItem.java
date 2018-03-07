package com.tiderdev.dilo.popularmovie.model;

/**
 * Created by dwikadarmawan on 6/29/16.
 */
public class MovieItem {

    private String urlImage;
    private double vote;

    public MovieItem(String urlImage, double vote) {
        this.urlImage = urlImage;
        this.vote = vote;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public double getVote() {
        return vote;
    }
}
