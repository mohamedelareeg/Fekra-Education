package com.rovaind.academy.interfaces;



import com.rovaind.academy.model.Category;

import java.io.Serializable;

public interface OnCategoryClickListener extends Serializable {
    void onCategoryClicked(Category contact , int mainPosition, int subPosition);
}
