package com.rovaind.academy.interfaces;


import com.rovaind.academy.model.Category;

import java.io.Serializable;


public interface OnCataClickListener extends Serializable {
    void onCategoryClicked(Category contact, int position);

}
