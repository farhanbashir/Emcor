package com.example.fbashir.emcor.models.businessServiceDetails;

import static com.example.fbashir.emcor.R.id.designation;
import static com.example.fbashir.emcor.R.string.email;

/**
 * Created by fbashir on 8/30/2016.
 */

public class CorporateOffice {
    public String id;
    public String company_id;
    public String business_service_id;
    public String address;
    public String city;
    public String state;
    public String zip;
    public String phone;
    public String fax;
    public String website;
    public String latitude;
    public String longitude;

    public CorporateOffice(String address, String city, String state, String phone, String fax, String website, String latitude, String longitude) {
        this.address = address;
        this.city = city;
        this.state = state;
        this.phone = phone;
        this.fax = fax;
        this.website = website;
        this.latitude = latitude;
        this.longitude = longitude;

    }

    public String getAddress() {
        return this.address;
    }

    public String getCity() {
        return this.city;
    }

    public String getState() {
        return this.state;
    }

    public String getPhone() {
        return this.phone;
    }

    public String getFax() {
        return this.fax;
    }

    public String getWebsite() {
        return this.website;
    }
}
