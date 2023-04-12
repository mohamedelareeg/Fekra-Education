package com.rovaind.academy.model.quiz;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Section implements Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("lesson_Id")
    @Expose
    private Integer lessonId;
    @SerializedName("section_title")
    @Expose
    private String sectionTitle;
    @SerializedName("section_content")
    @Expose
    private String sectionContent;
    @SerializedName("thumb_image")
    @Expose
    private String thumbImage;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLessonId() {
        return lessonId;
    }

    public void setLessonId(Integer lessonId) {
        this.lessonId = lessonId;
    }

    public String getSectionTitle() {
        return sectionTitle;
    }

    public void setSectionTitle(String sectionTitle) {
        this.sectionTitle = sectionTitle;
    }

    public String getSectionContent() {
        return sectionContent;
    }

    public void setSectionContent(String sectionContent) {
        this.sectionContent = sectionContent;
    }

    public String getThumbImage() {
        return thumbImage;
    }

    public void setThumbImage(String thumbImage) {
        this.thumbImage = thumbImage;
    }

}