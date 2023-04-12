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
import com.rovaind.academy.Controllers.CourseDetails;
import com.rovaind.academy.Controllers.GroupDetails;
import com.rovaind.academy.R;
import com.rovaind.academy.Utils.ItemsUI;
import com.rovaind.academy.Utils.views.TextViewAr;
import com.rovaind.academy.manager.SharedPrefManager;
import com.rovaind.academy.model.Course;
import com.rovaind.academy.retrofit.RetrofitClient;

import java.io.Serializable;
import java.util.List;

public class FilterableCoursesAdapter extends RecyclerView.Adapter<FilterableCoursesAdapter.PopulatViewHolder> {

        Context context;
        List<Course> popularCourseList;

    public FilterableCoursesAdapter(Context context, List<Course> popularCourseList) {
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

        final Course popularCourse = popularCourseList.get(position);
        holder.courseName.setText(popularCourse.getCourseName());
        holder.OwnerName.setText( context.getResources().getString(R.string.by)+" / " + popularCourse.getOwnerName());
        holder.totalLessons.setText(popularCourse.getLectureCount() + " " + context.getResources().getString(R.string.lectures));

        // setting up image using GLIDE

        Glide.with(context).load(RetrofitClient.BASE_CourseImage_URL +popularCourse.getThumbImage()).into(holder.courseImage);
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

                if(SharedPrefManager.getInstance(context).CheckSubcribedGroup(popularCourse.getId()) != null) {
                    Intent i = new Intent(context, CourseDetails.class);
                    i.putExtra(ItemsUI.BUNDLE_COURSES_DETAILS, (Serializable) popularCourse);
                    context.startActivity(i);
                }
                else
                {
                    Intent idetails = new Intent(context, GroupDetails.class);
                    idetails.putExtra(ItemsUI.BUNDLE_COURSES_DETAILS, (Serializable) popularCourse);
                    context.startActivity(idetails);
                }


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
