package com.rovaind.academy.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Postbackground implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("back_img")
    @Expose
    private String backImg;
    @SerializedName("txt_color")
    @Expose
    private String txtColor;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBackImg() {
        return backImg;
    }

    public void setBackImg(String backImg) {
        this.backImg = backImg;
    }

    public String getTxtColor() {
        return txtColor;
    }

    public void setTxtColor(String txtColor) {
        this.txtColor = txtColor;
    }


}
