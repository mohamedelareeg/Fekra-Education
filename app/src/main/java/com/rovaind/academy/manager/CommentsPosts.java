package com.rovaind.academy.manager;








import com.rovaind.academy.model.CommentPost;

import java.util.ArrayList;

/**
 * Created by Belal on 15/04/17.
 */

public class CommentsPosts {

    private ArrayList<CommentPost> commentPosts;

    public CommentsPosts() {

    }

    public ArrayList<CommentPost> getCommentPosts() {
        return commentPosts;
    }

    public void setCommentPosts(ArrayList<CommentPost> commentPosts) {
        this.commentPosts = commentPosts;
    }
}