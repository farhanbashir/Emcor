package com.example.fbashir.emcor.models.businessServiceDetails;

/**
 * Created by fbashir on 8/22/2016.
 */

public class IndustryRecognition {
    public String title;
    public String description;

    public IndustryRecognition(String title, String description) {
        this.title = title;
        this.description = description;

    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }
}
