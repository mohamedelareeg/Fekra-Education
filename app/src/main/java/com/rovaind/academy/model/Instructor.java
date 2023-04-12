package com.rovaind.academy.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Instructor implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("usr_id")
    @Expose
    private Integer usrId;
    @SerializedName("center_id")
    @Expose
    private Integer centerId;
    @SerializedName("category_id")
    @Expose
    private Integer categoryId;
    @SerializedName("level_id")
    @Expose
    private Integer levelId;
    @SerializedName("class_id")
    @Expose
    private Integer classId;
    @SerializedName("subject_id")
    @Expose
    private String subjectId;
    @SerializedName("total_students")
    @Expose
    private Integer totalStudents;
    @SerializedName("instructor_name")
    @Expose
    private String instructorName;
    @SerializedName("instructor_position")
    @Expose
    private String instructorPosition;
    @SerializedName("instructor_description")
    @Expose
    private String instructorDescription;
    @SerializedName("thumb_image")
    @Expose
    private String thumbImage;
    @SerializedName("rate")
    @Expose
    private Integer rate;
    @SerializedName("ratecount")
    @Expose
    private Integer ratecount;
    @SerializedName("phone")
    @Expose
    private Integer phone;
    @SerializedName("age")
    @Expose
    private String age;
    @SerializedName("school")
    @Expose
    private String school;
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

    public Instructor(Integer usrId ,Integer centerId , Integer categoryId, Integer levelId, Integer classId, String subjectId, Integer totalStudents, Integer rate, Integer ratecount, Integer phone, String age, String school, String socialFacebook, String videoUrlyou, Integer sponsored, Long createdAt, Integer viewCount) {
        this.usrId = usrId;
        this.centerId = centerId;
        this.categoryId = categoryId;
        this.levelId = levelId;
        this.classId = classId;
        this.subjectId = subjectId;
        this.totalStudents = totalStudents;
        this.rate = rate;
        this.ratecount = ratecount;
        this.phone = phone;
        this.age = age;
        this.school = school;

        this.socialFacebook = socialFacebook;
        this.videoUrlyou = videoUrlyou;
        this.sponsored = sponsored;
        this.createdAt = createdAt;
        this.viewCount = viewCount;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUsrId() {
        return usrId;
    }

    public void setUsrId(Integer usrId) {
        this.usrId = usrId;
    }

    public Integer getCenterId() {
        return centerId;
    }

    public void setCenterId(Integer centerId) {
        this.centerId = centerId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getLevelId() {
        return levelId;
    }

    public void setLevelId(Integer levelId) {
        this.levelId = levelId;
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public Integer getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(Integer totalStudents) {
        this.totalStudents = totalStudents;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public String getInstructorPosition() {
        return instructorPosition;
    }

    public void setInstructorPosition(String instructorPosition) {
        this.instructorPosition = instructorPosition;
    }

    public String getInstructorDescription() {
        return instructorDescription;
    }

    public void setInstructorDescription(String instructorDescription) {
        this.instructorDescription = instructorDescription;
    }

    public String getThumbImage() {
        return thumbImage;
    }

    public void setThumbImage(String thumbImage) {
        this.thumbImage = thumbImage;
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
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