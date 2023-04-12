package com.rovaind.academy.model;


import java.io.Serializable;


public class Ad implements Serializable {

    public Ad() {
    }

    private static final String TAG = "Ads";
    private int id;
    private int admin_id;

    private int merchant_id;
    private String thumb_image;
    private int cat_id;
    private int type;// 1 = TraderADS , 2 = CategoryADS , 3 = ProductADS , 4 = URLADS
    private long created_at;
    private long end_at;
    private int count;

    public Ad(int id, int count) {
        this.id = id;
        this.count = count;
    }

    public Ad(int admin_id, int merchant_id, String thumb_image, int cat_id, int type, long created_at, long end_at, int count) {
        this.admin_id = admin_id;

        this.merchant_id = merchant_id;
        this.thumb_image = thumb_image;
        this.cat_id = cat_id;
        this.type = type;
        this.created_at = created_at;
        this.end_at = end_at;
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(int admin_id) {
        this.admin_id = admin_id;
    }



    public int getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(int merchant_id) {
        this.merchant_id = merchant_id;
    }

    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }

    public int getCat_id() {
        return cat_id;
    }

    public void setCat_id(int cat_id) {
        this.cat_id = cat_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    public long getEnd_at() {
        return end_at;
    }

    public void setEnd_at(long end_at) {
        this.end_at = end_at;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
