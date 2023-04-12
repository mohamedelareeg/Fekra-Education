package com.rovaind.academy.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Like  implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("comment_id")
    @Expose
    private Integer commentId;
    @SerializedName("usr_id")
    @Expose
    private Integer usrId;
    @SerializedName("parent_id")
    @Expose
    private Integer parentId;
    @SerializedName("owner_name")
    @Expose
    private String ownerName;
    @SerializedName("thumb_image")
    @Expose
    private String thumbImage;
    @SerializedName("lecture_id")
    @Expose
    private Integer lectureId;
    @SerializedName("course_id")
    @Expose
    private Integer courseId;
    @SerializedName("created_at")
    @Expose
    private Long createdAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public Integer getUsrId() {
        return usrId;
    }

    public void setUsrId(Integer usrId) {
        this.usrId = usrId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getThumbImage() {
        return thumbImage;
    }

    public void setThumbImage(String thumbImage) {
        this.thumbImage = thumbImage;
    }

    public Integer getLectureId() {
        return lectureId;
    }

    public void setLectureId(Integer lectureId) {
        this.lectureId = lectureId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }
}