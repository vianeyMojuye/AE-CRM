package com.aecrm.Models;

public class  CountryModel
{
    String CountryId;
    String Country;
    String Status;

    public CountryModel(){}
    public CountryModel(String CountryId, String Country,String Status)
    {
        this.CountryId = CountryId;
        this.Country = Country;
        this.Status = Status;
    }

    public String getCountry() {
        return Country;
    }

    public String getCountryId() {
        return CountryId;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public void setCountryId(String countryId) {
        CountryId = countryId;
    }
}
