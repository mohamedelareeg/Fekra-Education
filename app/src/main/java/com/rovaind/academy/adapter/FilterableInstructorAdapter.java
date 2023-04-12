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
import com.rovaind.academy.Utils.views.TextViewAr;
import com.rovaind.academy.model.Instructor;
import com.rovaind.academy.retrofit.RetrofitClient;

import java.io.Serializable;
import java.util.List;

public class FilterableInstructorAdapter extends RecyclerView.Adapter<FilterableInstructorAdapter.PopulatViewHolder> {

        Context context;
        List<Instructor> popularCourseList;

    public FilterableInstructorAdapter(Context context, List<Instructor> popularCourseList) {
        this.context = context;
        this.popularCourseList = popularCourseList;
    }

    @NonNull
    @Override
    public PopulatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.subjectcourses_row_item, parent, false);

        //here we need to create a row item layout file
        return new PopulatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PopulatViewHolder holder, int position) {

        //here we will bind data to our layout

        final Instructor popularCourse = popularCourseList.get(position);
        holder.courseName.setText(popularCourse.getInstructorName());
        holder.OwnerName.setText(popularCourse.getInstructorPosition());
        holder.totalLessons.setText( context.getResources().getString(R.string.phonenumber) +""+ popularCourse.getPhone());

        // setting up image using GLIDE

        Glide.with(context).load(RetrofitClient.BASE_TeacherImage_URL +popularCourse.getThumbImage()).into(holder.courseImage);
        int rate = popularCourse.getRate();
        int ratecount = popularCourse.getRatecount();
        if(rate != 0  && ratecount != 0) {
            //double rating = rate;
            holder.rating.setRating((float) (rate/ratecount));
            //viewHolder.review_panel.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.rating.setRating((float) 0);
            //  viewHolder.review_panel.setVisibility(View.GONE);

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, InstructorActivity.class);
                i.putExtra(ItemsUI.BUNDLE_INSTRUCTOR_DETAILS, (Serializable) popularCourse);
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
        return popularCourseList.size();
    }

    public  static class PopulatViewHolder extends RecyclerView.ViewHolder{


        ImageView courseImage;
        TextViewAr courseName , OwnerName, totalLessons;
        private RatingBar rating;

        public PopulatViewHolder(@NonNull View itemView) {
            super(itemView);

            courseImage = itemView.findViewById(R.id.media_image);
            OwnerName = itemView.findViewById(R.id.sub_text);
            courseName = itemView.findViewById(R.id.primary_text);
            totalLessons = itemView.findViewById(R.id.total_lesson);

            rating = itemView.findViewById(R.id.ProductRating);




        }
    }




}
