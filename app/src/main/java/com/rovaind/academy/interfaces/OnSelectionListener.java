package com.rovaind.academy.interfaces;


import com.rovaind.academy.model.Postbackground;


/**
 * Created by Mohamed El Sayed
 */
public interface OnSelectionListener {
    void OnClick(Postbackground Img, int position);

    void OnLongClick(Postbackground img, int position);
}