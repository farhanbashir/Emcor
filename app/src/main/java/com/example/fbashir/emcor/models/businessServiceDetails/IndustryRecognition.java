package com.example.fbashir.emcor.models.businessServiceDetails;

/**
 * Created by fbashir on 8/22/2016.
 */

public class IndustryRecognition {
    public String title;
    public String description;
    public String file;

    public IndustryRecognition(String title, String description, String file) {
        this.title = title;
        this.description = description;
        this.file = file;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }
    public String getFile() {
        return this.file;
    }
}
