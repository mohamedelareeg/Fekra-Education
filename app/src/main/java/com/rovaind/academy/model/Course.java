package com.rovaind.academy.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Course implements Serializable {

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
    @SerializedName("owner_id")
    @Expose
    private Integer ownerId;
    @SerializedName("owner_image")
    @Expose
    private String ownerImage;
    @SerializedName("owner_name")
    @Expose
    private String ownerName;
    @SerializedName("course_name")
    @Expose
    private String courseName;
    @SerializedName("course_description")
    @Expose
    private String courseDescription;
    @SerializedName("thumb_image")
    @Expose
    private String thumbImage;
    @SerializedName("lecture_count")
    @Expose
    private Integer lectureCount;
    @SerializedName("videoUrlyou")
    @Expose
    private String videoUrlyou;
    @SerializedName("rate")
    @Expose
    private Integer rate;
    @SerializedName("ratecount")
    @Expose
    private Integer ratecount;
    @SerializedName("enrolled")
    @Expose
    private Integer enrolled;
    @SerializedName("course_status")
    @Expose
    private Integer courseStatus;
    @SerializedName("created_at")
    @Expose
    private Long createdAt;
    @SerializedName("view_count")
    @Expose
    private Integer viewCount;
    @SerializedName("order_count")
    @Expose
    private Integer orderCount;
    @SerializedName("share_count")
    @Expose
    private Integer shareCount;
    @SerializedName("wish_count")
    @Expose
    private Integer wishCount;
    @SerializedName("sponsored")
    @Expose
    private Integer sponsored;

    public Course(Integer id, Integer rate, Integer ratecount) {
        this.id = id;
        this.rate = rate;
        this.ratecount = ratecount;
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

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerImage() {
        return ownerImage;
    }

    public void setOwnerImage(String ownerImage) {
        this.ownerImage = ownerImage;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public String getThumbImage() {
        return thumbImage;
    }

    public void setThumbImage(String thumbImage) {
        this.thumbImage = thumbImage;
    }

    public Integer getLectureCount() {
        return lectureCount;
    }

    public void setLectureCount(Integer lectureCount) {
        this.lectureCount = lectureCount;
    }

    public String getVideoUrlyou() {
        return videoUrlyou;
    }

    public void setVideoUrlyou(String videoUrlyou) {
        this.videoUrlyou = videoUrlyou;
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

    public Integer getEnrolled() {
        return enrolled;
    }

    public void setEnrolled(Integer enrolled) {
        this.enrolled = enrolled;
    }

    public Integer getCourseStatus() {
        return courseStatus;
    }

    public void setCourseStatus(Integer courseStatus) {
        this.courseStatus = courseStatus;
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

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    public Integer getShareCount() {
        return shareCount;
    }

    public void setShareCount(Integer shareCount) {
        this.shareCount = shareCount;
    }

    public Integer getWishCount() {
        return wishCount;
    }

    public void setWishCount(Integer wishCount) {
        this.wishCount = wishCount;
    }

    public Integer getSponsored() {
        return sponsored;
    }

    public void setSponsored(Integer sponsored) {
        this.sponsored = sponsored;
    }

}