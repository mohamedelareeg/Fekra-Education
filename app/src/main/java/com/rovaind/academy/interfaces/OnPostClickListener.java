package com.rovaind.academy.interfaces;


import com.rovaind.academy.model.Course;
import com.rovaind.academy.model.Post;

import java.io.Serializable;


public interface OnPostClickListener extends Serializable {
    void onPostClicked(Post post , Course course, int position);

}
