package com.rovaind.academy.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Post implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("usr_id")
    @Expose
    private Integer usrId;
    @SerializedName("course_id")
    @Expose
    private Integer courseId;
    @SerializedName("class_id")
    @Expose
    private Integer classId;
    @SerializedName("owner_id")
    @Expose
    private Integer ownerId;
    @SerializedName("owner_name")
    @Expose
    private String ownerName;
    @SerializedName("thumb_image")
    @Expose
    private String thumbImage;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("Url")
    @Expose
    private String url;
    @SerializedName("back_img")
    @Expose
    private String backImg;
    @SerializedName("txt_color")
    @Expose
    private String txtColor;
    @SerializedName("like_count")
    @Expose
    private Integer likeCount;
    @SerializedName("comment_count")
    @Expose
    private Integer commentCount;
    @SerializedName("post_type")
    @Expose
    private Integer postType;
    @SerializedName("view_count")
    @Expose
    private Integer viewCount;
    @SerializedName("created_at")
    @Expose
    private Long createdAt;
    @SerializedName("gallery")
    @Expose
    private List<Gallery> gallery = null;

    public Post(Integer usrId, Integer courseId , Integer classId, Integer ownerId, String content, String url, String backImg, String txtColor, Integer postType, Long createdAt) {
        this.usrId = usrId;
        this.courseId = courseId;
        this.classId = classId;
        this.ownerId = ownerId;

        this.content = content;
        this.url = url;
        this.backImg = backImg;
        this.txtColor = txtColor;
        this.postType = postType;
        this.createdAt = createdAt;
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

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Integer getPostType() {
        return postType;
    }

    public void setPostType(Integer postType) {
        this.postType = postType;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public List<Gallery> getGallery() {
        return gallery;
    }

    public void setGallery(List<Gallery> gallery) {
        this.gallery = gallery;
    }

    public class Gallery implements Serializable {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("post_id")
        @Expose
        private Integer postId;
        @SerializedName("thumb_image")
        @Expose
        private String thumbImage;
        @SerializedName("content")
        @Expose
        private String content;
        @SerializedName("like_count")
        @Expose
        private Integer likeCount;
        @SerializedName("comment_count")
        @Expose
        private Integer commentCount;
        @SerializedName("created_at")
        @Expose
        private Long createdAt;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getPostId() {
            return postId;
        }

        public void setPostId(Integer postId) {
            this.postId = postId;
        }

        public String getThumbImage() {
            return thumbImage;
        }

        public void setThumbImage(String thumbImage) {
            this.thumbImage = thumbImage;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public Integer getLikeCount() {
            return likeCount;
        }

        public void setLikeCount(Integer likeCount) {
            this.likeCount = likeCount;
        }

        public Integer getCommentCount() {
            return commentCount;
        }

        public void setCommentCount(Integer commentCount) {
            this.commentCount = commentCount;
        }

        public Long getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(Long createdAt) {
            this.createdAt = createdAt;
        }
    }

}