package com.rovaind.academy.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Center implements Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("center_name")
    @Expose
    private String centerName;
    @SerializedName("center_description")
    @Expose
    private String centerDescription;
    @SerializedName("thumb_image")
    @Expose
    private String thumbImage;
    @SerializedName("total_students")
    @Expose
    private Integer totalStudents;
    @SerializedName("rate")
    @Expose
    private Integer rate;
    @SerializedName("ratecount")
    @Expose
    private Integer ratecount;
    @SerializedName("phone")
    @Expose
    private Integer phone;
    @SerializedName("city_code")
    @Expose
    private String cityCode;
    @SerializedName("goverment_code")
    @Expose
    private String govermentCode;
    @SerializedName("social_facebook")
    @Expose
    private String socialFacebook;
    @SerializedName("videoUrlyou")
    @Expose
    private String videoUrlyou;
    @SerializedName("sponsored")
    @Expose
    private Integer sponsored;
    @SerializedName("created_at")
    @Expose
    private Long createdAt;
    @SerializedName("view_count")
    @Expose
    private Integer viewCount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCenterName() {
        return centerName;
    }

    public void setCenterName(String centerName) {
        this.centerName = centerName;
    }

    public String getCenterDescription() {
        return centerDescription;
    }

    public void setCenterDescription(String centerDescription) {
        this.centerDescription = centerDescription;
    }

    public String getThumbImage() {
        return thumbImage;
    }

    public void setThumbImage(String thumbImage) {
        this.thumbImage = thumbImage;
    }

    public Integer getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(Integer totalStudents) {
        this.totalStudents = totalStudents;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public Integer getRatecount() {
        return ratecount;
    }

    public void setRatecount(Integer ratecount) {
        this.ratecount = ratecount;
    }

    public Integer getPhone() {
        return phone;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getGovermentCode() {
        return govermentCode;
    }

    public void setGovermentCode(String govermentCode) {
        this.govermentCode = govermentCode;
    }

    public String getSocialFacebook() {
        return socialFacebook;
    }

    public void setSocialFacebook(String socialFacebook) {
        this.socialFacebook = socialFacebook;
    }

    public String getVideoUrlyou() {
        return videoUrlyou;
    }

    public void setVideoUrlyou(String videoUrlyou) {
        this.videoUrlyou = videoUrlyou;
    }

    public Integer getSponsored() {
        return sponsored;
    }

    public void setSponsored(Integer sponsored) {
        this.sponsored = sponsored;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

}
