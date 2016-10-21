package com.example.fbashir.emcor.models.companies;

/**
 * Created by fbashir on 8/22/2016.
 */

public class CompaniesBasic {
    public String company_id;
    public String name;
    public String logo;
    public String address;
    public String city;
    public String state;
    public String zip;
    public String latitude;
    public String longitude;
    public String phone;
    public String fax;
    public String toll_free;
    public String website;
    public String division_id;

    public CompaniesBasic(String company_id,
                          String name,
                          String logo,
                          String address,
                          String city,
                          String state,
                          String zip,
                          String latitude,
                          String longitude,
                          String phone,
                          String fax,
                          String toll_free,
                          String website,
                          String division_id)
    {
        this.company_id = company_id;
        this.name = name;
        this.logo = logo;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.latitude = latitude;
        this.longitude = longitude;
        this.phone = phone;
        this.fax = fax;
        this.toll_free = toll_free;
        this.division_id = division_id;
        this.website = website;
    }

    public String getCompany_id() {
        return this.company_id;
    }
    public String getName() {
        return this.name;
    }
    public String getAddress() {return this.address;}
    public String getLogo() {
        return this.logo;
    }
    public String getDivision() {
        return this.division_id;
    }

}
