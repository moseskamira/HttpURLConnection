package com.example.httpurlconnection.models;

import androidx.annotation.NonNull;

public class Country {


    private String countryName;
    private String countryCapital;
    private String callingCode;
    private String alpha2Code;
    private String region;
    private String population;
    private String countryFlagUrl;

    public Country() {
    }

    public Country(String countryName, String countryCapital, String callingCode, String alpha2Code, String region, String population, String countryFlagUrl) {
        this.countryName = countryName;
        this.countryCapital = countryCapital;
        this.callingCode = callingCode;
        this.alpha2Code = alpha2Code;
        this.region = region;
        this.population = population;
        this.countryFlagUrl = countryFlagUrl;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryCapital() {
        return countryCapital;
    }

    public void setCountryCapital(String countryCapital) {
        this.countryCapital = countryCapital;
    }

    public String getCallingCode() {
        return callingCode;
    }

    public void setCallingCode(String callingCode) {
        this.callingCode = callingCode;
    }

    public String getAlpha2Code() {
        return alpha2Code;
    }

    public void setAlpha2Code(String alpha2Code) {
        this.alpha2Code = alpha2Code;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPopulation() {
        return population;
    }

    public void setPopulation(String population) {
        this.population = population;
    }

    public String getCountryFlagUrl() {
        return countryFlagUrl;
    }

    public void setCountryFlagUrl(String countryFlagUrl) {
        this.countryFlagUrl = countryFlagUrl;
    }

    @Override
    public @NonNull String  toString() {
        return "Country{" +
                "countryName='" + countryName + '\'' +
                ", countryCapital='" + countryCapital + '\'' +
                ", callingCode='" + callingCode + '\'' +
                ", alpha2Code='" + alpha2Code + '\'' +
                ", region='" + region + '\'' +
                ", population='" + population + '\'' +
                ", countryFlagUrl='" + countryFlagUrl + '\'' +
                '}';
    }
}
