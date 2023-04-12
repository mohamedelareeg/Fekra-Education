package com.rovaind.academy.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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

public class CoursesOwnersAdapter extends RecyclerView.Adapter<CoursesOwnersAdapter.LessonViewHolder> {


    Context context;
    List<Course> videoLessionList;

    public CoursesOwnersAdapter(Context context, List<Course> videoLessionList) {
        this.context = context;
        this.videoLessionList = videoLessionList;
    }

    @NonNull
    @Override
    public LessonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.coursesowners_row_item, parent, false);

        //here we need to create a row item layout file
        return new LessonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final LessonViewHolder holder, final int position) {

        //here we will bind data to our layout
        final Course lecture = videoLessionList.get(position);
        holder.lessonName.setText(lecture.getCourseName());

        holder.totalLessons.setText(lecture.getLectureCount() + " Lecture");

        // setting up image using GLIDE

        Glide.with(context).load(RetrofitClient.BASE_CourseImage_URL + lecture.getThumbImage()).into(holder.courseImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(SharedPrefManager.getInstance(context).CheckSubcribedGroup(lecture.getId()) != null) {
                    Intent i = new Intent(context, CourseDetails.class);
                    i.putExtra(ItemsUI.BUNDLE_COURSES_DETAILS, (Serializable) lecture);
                    context.startActivity(i);
                }
                else
                {
                    Intent idetails = new Intent(context, GroupDetails.class);
                    idetails.putExtra(ItemsUI.BUNDLE_COURSES_DETAILS, (Serializable) lecture);
                    context.startActivity(idetails);
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return videoLessionList.size();
    }

    public  static class LessonViewHolder extends RecyclerView.ViewHolder{



        TextViewAr lessonName , totalLessons;
        ImageView courseImage;
        public LessonViewHolder(@NonNull View itemView) {
            super(itemView);
            totalLessons = itemView.findViewById(R.id.total_lesson);
            courseImage = itemView.findViewById(R.id.course_image);
            lessonName = itemView.findViewById(R.id.lesson_name);

        }
    }



}
