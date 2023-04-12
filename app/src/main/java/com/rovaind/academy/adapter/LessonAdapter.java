package com.rovaind.academy.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rovaind.academy.Controllers.QuizSectionActivity;
import com.rovaind.academy.R;
import com.rovaind.academy.Utils.ItemsUI;
import com.rovaind.academy.Utils.views.TextViewAr;
import com.rovaind.academy.VideoPlayer;
import com.rovaind.academy.manager.SharedPrefManager;
import com.rovaind.academy.model.Course;
import com.rovaind.academy.model.Lecture;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.TimeUnit;



public class LessonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    public Context context;
    List<Lecture> videoLessionList;
    Course SelectedCourse;
    public LessonAdapter(Context context, List<Lecture> videoLessionList , Course SelectedCourse) {
        this.context = context;
        this.videoLessionList = videoLessionList;
        this.SelectedCourse = SelectedCourse;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //View view = LayoutInflater.from(context).inflate(R.layout.lesson_row_item, parent, false);
        context = parent.getContext();
        //here we need to create a row item layout file
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        RecyclerView.ViewHolder holder;
        switch (viewType) {
            case 0:
                view = inflater.inflate(R.layout.lesson_row_item, parent, false);//ForTEST
                holder = new LessonViewHolder(view);
                break;
            case 1:
                view = inflater.inflate(R.layout.quiz_row_item, parent, false);
                holder = new QuizViewHolder(view);
                break;

            default:
                view = inflater.inflate(R.layout.lesson_row_item, parent, false);
                holder = new LessonViewHolder(view);
                break;
        }

        return holder;
    }



    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        if(getItemViewType(position) == 0)
        {
            onBindViewHolder_Lesson((LessonViewHolder) holder, position);
        }
        else if(getItemViewType(position) == 1)
        {
            onBindViewHolder_Quiz((QuizViewHolder) holder, position);
        }


    }

    public void onBindViewHolder_Lesson(@NonNull LessonViewHolder holder, final int position) {

        //here we will bind data to our layout
        final Lecture lecture = videoLessionList.get(position);
        final Course course = SelectedCourse;
        holder.lessonName.setText(lecture.getLectureName());

        try{
            /*
            MediaMetadataRetriever  mmr = new MediaMetadataRetriever();
            mmr.setDataSource( RetrofitClient.BASE_VIDEO_URL + lecture.getVideoUrlyou());//+  "/preview"


            String mVideoDuration =  mmr .extractMetadata(MediaMetadataRetriever .METADATA_KEY_DURATION);
            long mTimeInMilliseconds= Long.parseLong(mVideoDuration);
            holder.setTime(mTimeInMilliseconds);

             */
        }catch (Exception e)
        {

        }




        final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        long to_mil = SharedPrefManager.getInstance(context).getEndTime(lecture.getCourseId());
        long diff = to_mil - timestamp.getTime();
        if(lecture.getAccess() == 1 ) {//TODO
            holder.video_avaliabilty.setVisibility(View.VISIBLE);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i = new Intent(context, VideoPlayer.class);
                    i.putExtra(ItemsUI.BUNDLE_COURSES_DETAILS, (Serializable) lecture);
                    i.putExtra(ItemsUI.BUNDLE_COURSES_DETAILS_LECTURE, (Serializable) course);
                    context.startActivity(i);

                }
            });

        }

        else  if(SharedPrefManager.getInstance(context).getCoursesCodes(lecture.getCourseId()) != null) {
            if(SharedPrefManager.getInstance(context).getCoursesCodes(lecture.getCourseId()).getCourseId().equals(lecture.getCourseId()) & diff > 0)
            {
                holder.video_avaliabilty.setVisibility(View.VISIBLE);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent i = new Intent(context, VideoPlayer.class);
                        i.putExtra(ItemsUI.BUNDLE_COURSES_DETAILS, (Serializable) lecture);
                        i.putExtra(ItemsUI.BUNDLE_COURSES_DETAILS_LECTURE, (Serializable) course);
                        context.startActivity(i);

                    }
                });
            }

        }


    }
    public void onBindViewHolder_Quiz(@NonNull QuizViewHolder holder, final int position) {

        //here we will bind data to our layout
        final Lecture lecture = videoLessionList.get(position);
        final Course course = SelectedCourse;
        holder.lessonName.setText(lecture.getLectureName());

        try{
            /*
            MediaMetadataRetriever  mmr = new MediaMetadataRetriever();
            mmr.setDataSource( RetrofitClient.BASE_VIDEO_URL + lecture.getVideoUrlyou());//+  "/preview"


            String mVideoDuration =  mmr .extractMetadata(MediaMetadataRetriever .METADATA_KEY_DURATION);
            long mTimeInMilliseconds= Long.parseLong(mVideoDuration);
            holder.setTime(mTimeInMilliseconds);

             */
        }catch (Exception e)
        {

        }




        final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        long to_mil = SharedPrefManager.getInstance(context).getEndTime(lecture.getCourseId());
        long diff = to_mil - timestamp.getTime();
        if(lecture.getAccess() == 1 ) {//TODO
            holder.video_avaliabilty.setVisibility(View.VISIBLE);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context, QuizSectionActivity.class);
                    intent.putExtra(ItemsUI.BUNDLE_COURSES_DETAILS, (Serializable) lecture);
                    intent.putExtra(ItemsUI.BUNDLE_COURSES_DETAILS_LECTURE, (Serializable) course);
                    intent.putExtra("sectionn", 0);
                    intent.putExtra("lessonid", lecture.getId());
                    context.startActivity(intent);

                }
            });

        }

        else  if(SharedPrefManager.getInstance(context).getCoursesCodes(lecture.getCourseId()) != null) {
            if(SharedPrefManager.getInstance(context).getCoursesCodes(lecture.getCourseId()).getCourseId().equals(lecture.getCourseId()) & diff > 0)
            {
                holder.video_avaliabilty.setVisibility(View.VISIBLE);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(context, QuizSectionActivity.class);
                        intent.putExtra(ItemsUI.BUNDLE_COURSES_DETAILS, (Serializable) lecture);
                        intent.putExtra(ItemsUI.BUNDLE_COURSES_DETAILS_LECTURE, (Serializable) course);
                        intent.putExtra("sectionn", 0);
                        intent.putExtra("lessonid", lecture.getId());
                        context.startActivity(intent);

                    }
                });
            }

        }


    }
    @Override
    public int getItemViewType(int position) {
        int data_type = videoLessionList.get(position).getType();
        return data_type;
    }

    @Override
    public int getItemCount() {
        return videoLessionList.size();
    }

    public  class LessonViewHolder extends RecyclerView.ViewHolder{



        TextViewAr lessonName , lessonLength , video_avaliabilty;

        public LessonViewHolder(@NonNull View itemView) {
            super(itemView);


            lessonName = itemView.findViewById(R.id.lesson_name);
            lessonLength = itemView.findViewById(R.id.lesson_length);
            video_avaliabilty = itemView.findViewById(R.id.video_avaliabilty);
        }

        public void setTime(long time) {


            lessonLength.setText(String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes(time),
                    TimeUnit.MILLISECONDS.toSeconds(time) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time))
            ));
            /*

            GetTimeAgo getTimeAgo = new GetTimeAgo();

            long lastTime = time;

            String lastSeenTime = GetTimeAgo.getTimeAgo(lastTime, context);

            lessonLength.setText(lastSeenTime);

             */

        }
    }
    public  class QuizViewHolder extends RecyclerView.ViewHolder{



        TextViewAr lessonName , lessonLength , video_avaliabilty;

        public QuizViewHolder(@NonNull View itemView) {
            super(itemView);


            lessonName = itemView.findViewById(R.id.lesson_name);
            lessonLength = itemView.findViewById(R.id.lesson_length);
            video_avaliabilty = itemView.findViewById(R.id.video_avaliabilty);
        }

        public void setTime(long time) {


            lessonLength.setText(String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes(time),
                    TimeUnit.MILLISECONDS.toSeconds(time) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time))
            ));
            /*

            GetTimeAgo getTimeAgo = new GetTimeAgo();

            long lastTime = time;

            String lastSeenTime = GetTimeAgo.getTimeAgo(lastTime, context);

            lessonLength.setText(lastSeenTime);

             */

        }
    }


}
