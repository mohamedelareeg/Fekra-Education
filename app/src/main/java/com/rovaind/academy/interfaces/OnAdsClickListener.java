package com.rovaind.academy.interfaces;



import com.rovaind.academy.model.Ad;

import java.io.Serializable;

public interface OnAdsClickListener extends Serializable {
    void onAdsClicked(Ad contact, int position);
}
