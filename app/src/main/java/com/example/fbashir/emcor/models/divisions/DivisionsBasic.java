package com.example.fbashir.emcor.models.divisions;

/**
 * Created by fbashir on 8/22/2016.
 */

public class DivisionsBasic {
    public String division_id;
    public String title;
    public String description;
    public String image;

    public DivisionsBasic(String division_id, String title, String description, String image)
    {
        this.division_id = division_id;
        this.title = title;
        this.description = description;
        this.image = image;
    }
}
