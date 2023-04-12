package com.rovaind.academy.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rovaind.academy.Controllers.InstructorActivity;
import com.rovaind.academy.R;
import com.rovaind.academy.Utils.ItemsUI;
import com.rovaind.academy.Utils.countdownview.CountdownView;
import com.rovaind.academy.Utils.views.TextViewAr;
import com.rovaind.academy.model.Instructor;
import com.rovaind.academy.retrofit.RetrofitClient;

import java.io.Serializable;
import java.util.List;

public class InstructorAdapter extends RecyclerView.Adapter<InstructorAdapter.CourseForYouViewHolder> {

    Context context;
    List<Instructor> courseForYouList;

    public InstructorAdapter(Context context, List<Instructor> courseForYouList) {
        this.context = context;
        this.courseForYouList = courseForYouList;
    }

    @NonNull
    @Override
    public CourseForYouViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.courseforyou_row_item, parent, false);

        //here we need to create a row item layout file
        return new CourseForYouViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CourseForYouViewHolder holder, int position) {

        //here we will bind data to our layout
        final Instructor courseForYou = courseForYouList.get(position);
        holder.courseName.setText(courseForYou.getInstructorName());
        holder.totalLessons.setText(courseForYou.getInstructorPosition());

        // setting up image using GLIDE

        Glide.with(context).load(RetrofitClient.BASE_TeacherImage_URL +courseForYou.getThumbImage()).into(holder.courseImage);

        int rate = courseForYou.getRate();
        int ratecount = courseForYou.getRatecount();
        if(rate != 0  && ratecount != 0) {
            //double rating = rate;
            holder.rating.setRating((float) (rate/ratecount));
            //viewHolder.review_panel.setVisibility(View.VISIBLE);
            holder.Best_Seller.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.rating.setRating((float) 0);
            //  viewHolder.review_panel.setVisibility(View.GONE);
            holder.Best_Seller.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, InstructorActivity.class);
                i.putExtra(ItemsUI.BUNDLE_INSTRUCTOR_DETAILS, (Serializable) courseForYou);
/*
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(holder.courseImage, "image");
                ActivityOptions activityOptions = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    activityOptions = ActivityOptions.makeSceneTransitionAnimation((Activity)context, pairs);
                }


 */
                context.startActivity(i);

            }
        });


    }

    @Override
    public int getItemCount() {
        return courseForYouList.size();
    }

    public  static class CourseForYouViewHolder extends RecyclerView.ViewHolder{


        ImageView courseImage;
        TextViewAr courseName, totalLessons;
        private TextViewAr Discount;
        private RatingBar rating;
        private TextViewAr Best_Seller;
        private CountdownView mCvCountdownView;
        public CourseForYouViewHolder(@NonNull View itemView) {
            super(itemView);

            courseImage = itemView.findViewById(R.id.course_image);
            courseName = itemView.findViewById(R.id.lesson_name);
            totalLessons = itemView.findViewById(R.id.total_lesson);
            rating = itemView.findViewById(R.id.ProductRating);

            Best_Seller = itemView.findViewById(R.id.Best_Seller);

            Discount = itemView.findViewById(R.id.ProductDiscount);

            mCvCountdownView = (CountdownView) itemView.findViewById(R.id.cv_countdownView);
        }
    }




}
