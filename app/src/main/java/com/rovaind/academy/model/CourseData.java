
package com.rovaind.academy.model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CourseData implements Serializable {

    @SerializedName("popularCourses")
    @Expose
    private List<PopularCourse> popularCourses = null;
    @SerializedName("courseForYou")
    @Expose
    private List<CourseForYou> courseForYou = null;

    public List<PopularCourse> getPopularCourses() {
        return popularCourses;
    }

    public void setPopularCourses(List<PopularCourse> popularCourses) {
        this.popularCourses = popularCourses;
    }

    public List<CourseForYou> getCourseForYou() {
        return courseForYou;
    }

    public void setCourseForYou(List<CourseForYou> courseForYou) {
        this.courseForYou = courseForYou;
    }

}
