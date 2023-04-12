package com.rovaind.academy.manager;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rovaind.academy.model.Lecturesection;

import java.util.List;

public class LectureSections {


    @SerializedName("lecturesections")
    @Expose
    private List<Lecturesection> lecturesections = null;

    public List<Lecturesection> getLecturesections() {
        return lecturesections;
    }

    public void setLecturesections(List<Lecturesection> lecturesections) {
        this.lecturesections = lecturesections;
    }
}
