package com.example.fbashir.emcor.models.companyDetails;

/**
 * Created by fbashir on 9/17/2016.
 */

public class Download {
    public String pdf_name;
    public String pdf_link;

    public Download(String name, String link)
    {
        this.pdf_name = name;
        this.pdf_link = link;
    }
}
