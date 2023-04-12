package com.rovaind.academy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.viewpager.widget.PagerAdapter;

import com.rovaind.academy.R;
import com.rovaind.academy.Utils.views.TextViewAr;

public class SliderAdapter  extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;

//    private TextView slideHeading, slideDescription;
//    private ImageView slide_imageView;


    public SliderAdapter(Context context) {

        this.context = context;
    }

    // img Array
    public int[] image_slide ={
            R.drawable.ic_book,
            R.drawable.ic_house,
            R.drawable.ic_school
    };

    // heading Array
    public String[] heading_slide ={
            "تعلم فى اى وقت",
            "تعلم من المنزل",
            "تواصل مع نخبة من المدرسين والمدرسات"
    };

    // description Array
    public String[] description_slide ={
            "العلم هو الذي يزين الإنسان و يزيده جمالاً",
           "العلم يبني بيوتاً لا عماد لها، و الجهل يهدم بيت العز و الكرم",
            "رأسمالك علمك و عدوك هو جهلك"
    };




    @Override
    public int getCount() {

        return heading_slide.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container,false);
        container.addView(view);

        ImageView slide_imageView = view.findViewById(R.id.imageView1);
        TextViewAr slideHeading = view.findViewById(R.id.tvHeading);
        TextViewAr  slideDescription = view.findViewById(R.id.tvDescription);

        slide_imageView.setImageResource(image_slide[position]);
        slideHeading.setText(heading_slide[position]);
        slideDescription.setText(description_slide[position]);

        return view;
    }



    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout)object);
    }

//    @Override
//    public void destroyItem(ViewGroup container, int position, Object object) {
//        View view = (View) object;
//        container.removeView(view);
//    }

}



