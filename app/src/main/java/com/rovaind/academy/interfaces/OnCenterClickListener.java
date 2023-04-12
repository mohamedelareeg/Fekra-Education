package com.rovaind.academy.interfaces;


import com.rovaind.academy.model.Center;

import java.io.Serializable;


public interface OnCenterClickListener extends Serializable {
    void onCategoryClicked(Center contact, int position);

}
