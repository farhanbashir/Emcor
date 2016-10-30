package com.example.fbashir.emcor.models.businessServiceDetails;

import static com.example.fbashir.emcor.R.id.population;
import static com.example.fbashir.emcor.R.id.rank;

/**
 * Created by fbashir on 8/22/2016.
 */

public class Team {
    public String first_name;
    public String last_name;
    public String email;
    public String contact_number;
    public String designation;
    public String photo;

    public Team(String first_name, String last_name, String email, String contact_number, String designation, String photo) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.contact_number = contact_number;
        this.designation = designation;
        this.photo = photo;
    }

    public String getName() {
        return this.first_name.concat(" ").concat(this.last_name);
    }

    public String getEmail(){return this.email;}
    public String getContact_number(){return this.contact_number;}
    public String getDesignation() {
        return this.designation;
    }
    public String getPhoto(){return this.photo;}


}
