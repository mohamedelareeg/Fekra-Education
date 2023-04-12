package com.rovaind.academy.manager;








import com.rovaind.academy.model.LikePost;

import java.util.ArrayList;

/**
 * Created by Belal on 15/04/17.
 */

public class LikesPost {

    private ArrayList<LikePost> likespost;

    public LikesPost() {

    }

    public ArrayList<LikePost> getLikespost() {
        return likespost;
    }

    public void setLikespost(ArrayList<LikePost> likespost) {
        this.likespost = likespost;
    }
}