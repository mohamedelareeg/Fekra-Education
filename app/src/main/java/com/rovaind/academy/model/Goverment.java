package com.rovaind.academy.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Goverment implements Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("governorate_name")
    @Expose
    private String governorateName;
    @SerializedName("governorate_name_en")
    @Expose
    private String governorateNameEn;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGovernorateName() {
        return governorateName;
    }

    public void setGovernorateName(String governorateName) {
        this.governorateName = governorateName;
    }

    public String getGovernorateNameEn() {
        return governorateNameEn;
    }

    public void setGovernorateNameEn(String governorateNameEn) {
        this.governorateNameEn = governorateNameEn;
    }

}