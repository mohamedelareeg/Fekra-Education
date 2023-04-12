package com.rovaind.academy.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rovaind.academy.Controllers.PDFViewerActivity;
import com.rovaind.academy.Controllers.QuizSectionActivity;
import com.rovaind.academy.R;
import com.rovaind.academy.Utils.ExpandableRecyclerView;
import com.rovaind.academy.Utils.ItemsUI;
import com.rovaind.academy.Utils.views.TextViewAr;
import com.rovaind.academy.VideoPlayer;
import com.rovaind.academy.manager.SharedPrefManager;
import com.rovaind.academy.model.Code;
import com.rovaind.academy.model.Course;
import com.rovaind.academy.model.Lecture;
import com.rovaind.academy.model.Lecturesection;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;



public class LessonSectionAdapter extends ExpandableRecyclerView.Adapter<LessonSectionAdapter.ChildViewHolder,ExpandableRecyclerView.SimpleGroupViewHolder,String,String>
{

    public Context context;
    List<Lecturesection> lecturesectionList;

    Course SelectedCourse;
    private static final int VIDEO_LESSON  = 1;
    private static final int QUESTION_LESSON = 2;
    public LessonSectionAdapter(Context context, List<Lecturesection> lecturesectionList, Course SelectedCourse) {
        this.context = context;
        this.lecturesectionList = lecturesectionList;
        this.SelectedCourse = SelectedCourse;
    }

    @Override
    public int getGroupItemCount() {
        Log.d("Group", "getGroupItemCount: " + lecturesectionList.size());
        return lecturesectionList.size() - 1;

    }

    @Override
    public int getChildItemCount(int i) {
        return lecturesectionList.get(i).getLectures().size();
    }

    @Override
    public String getGroupItem(int i) {
        return  lecturesectionList.get(i).getLectureName();
    }

    @Override
    public String getChildItem(int group, int child) {
        return lecturesectionList.get(group).getLectures().get(child).getLectureName();
    }

    @Override
    protected ExpandableRecyclerView.SimpleGroupViewHolder onCreateGroupViewHolder(ViewGroup parent)
    {
        return new ExpandableRecyclerView.SimpleGroupViewHolder(parent.getContext());
    }


    @Override
    protected ChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType)
    {
        //View view = LayoutInflater.from(context).inflate(R.layout.lesson_row_item, parent, false);
        context = parent.getContext();
        //here we need to create a row item layout file
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        ChildViewHolder holder;
        switch (viewType) {
            case VIDEO_LESSON:
                view = inflater.inflate(R.layout.lesson_row_item, parent, false);//ForTEST

                break;
            case QUESTION_LESSON:
                view = inflater.inflate(R.layout.quiz_row_item, parent, false);

                break;

            default:
                view = inflater.inflate(R.layout.lesson_row_item, parent, false);

                break;
        }

        return new ChildViewHolder(view);
    }

    @Override
    public void onBindGroupViewHolder(ExpandableRecyclerView.SimpleGroupViewHolder holder, int group) {
        super.onBindGroupViewHolder(holder, group);
        holder.setText(getGroupItem(group));
        /*
        final LessonSection lessonSection = lessonSectionList.get(2);
        holder.setText(lessonSection.getLectureName());

         */

    }

    @Override
    public void onBindChildViewHolder(ChildViewHolder holder, int group, int position)
    {
        if(getItemViewType(position) == 0)
        {
            onBindViewHolder_Lesson((ChildViewHolder) holder , group, position);
        }
        else if(getItemViewType(position) == 1)
        {
            onBindViewHolder_Lesson((ChildViewHolder) holder , group, position);
            //onBindViewHolder_Quiz((ChildViewHolder) holder, group , position);
        }
        else
        {
            //onBindViewHolder_Selectable((ChildViewHolder) holder , group, position);
            onBindViewHolder_Lesson((ChildViewHolder) holder , group, position);
        }
    }
    public void onBindViewHolder_Lesson(@NonNull ChildViewHolder holder , int group, final int position) {

        //here we will bind data to our layout
        final Lecture lecture = lecturesectionList.get(group).getLectures().get(position);
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

        long to_mil = SharedPrefManager.getInstance(context).getEndTime(lecture.getCourseId());
        Code c = SharedPrefManager.getInstance(context).getCoursesCodes(lecture.getCourseId());
        if (lecturesectionList.get(group).getType_c() == 0){
            c = SharedPrefManager.getInstance(context).getCoursesCodes(lecture.getCourseId());
             to_mil = SharedPrefManager.getInstance(context).getEndTime(lecture.getCourseId());

        }else if (lecturesectionList.get(group).getType_c() == 1) {
            c = SharedPrefManager.getInstance(context).getCoursesExtraCodes(lecture.getCourseId());
             to_mil = SharedPrefManager.getInstance(context).getEndTimeExtra(lecture.getCourseId());

        }

        final Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        long diff = to_mil - timestamp.getTime();
        if(lecture.getAccess() == 1 ) {//TODO
            holder.video_avaliabilty.setVisibility(View.VISIBLE);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(lecture.getType() == 0 ) {
                    Intent i = new Intent(context, VideoPlayer.class);
                    i.putExtra(ItemsUI.BUNDLE_COURSES_DETAILS, (Serializable) lecture);
                    i.putExtra(ItemsUI.BUNDLE_COURSES_DETAILS_LECTURE, (Serializable) course);
                    context.startActivity(i);
                    }else if(lecture.getType() == 1 ){
                        Intent intent = new Intent(context, QuizSectionActivity.class);
                        intent.putExtra(ItemsUI.BUNDLE_COURSES_DETAILS, (Serializable) lecture);
                        intent.putExtra(ItemsUI.BUNDLE_COURSES_DETAILS_LECTURE, (Serializable) course);
                        intent.putExtra("sectionn", 0);
                        intent.putExtra("lessonid", lecture.getId());
                        context.startActivity(intent);

                    }else if(lecture.getType() == 2 ){
                        Intent intent = new Intent(context, PDFViewerActivity.class);
                        intent.putExtra(ItemsUI.BUNDLE_COURSES_DETAILS, (Serializable) lecture);
                        intent.putExtra(ItemsUI.BUNDLE_COURSES_DETAILS_LECTURE, (Serializable) course);
                        intent.putExtra("sectionn", 0);
                        intent.putExtra("lessonid", lecture.getId());
                        context.startActivity(intent);

                    }

                }
            });

        }

        else  if(c != null) {
            if(c.getCourseId().equals(lecture.getCourseId()) & diff > 0)
            {
                holder.video_avaliabilty.setVisibility(View.VISIBLE);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(lecture.getType() == 0 ) {
                            Intent i = new Intent(context, VideoPlayer.class);
                            i.putExtra(ItemsUI.BUNDLE_COURSES_DETAILS, (Serializable) lecture);
                            i.putExtra(ItemsUI.BUNDLE_COURSES_DETAILS_LECTURE, (Serializable) course);
                            context.startActivity(i);
                        }else if(lecture.getType() == 1 ){
                            Intent intent = new Intent(context, QuizSectionActivity.class);
                            intent.putExtra(ItemsUI.BUNDLE_COURSES_DETAILS, (Serializable) lecture);
                            intent.putExtra(ItemsUI.BUNDLE_COURSES_DETAILS_LECTURE, (Serializable) course);
                            intent.putExtra("sectionn", 0);
                            intent.putExtra("lessonid", lecture.getId());
                            context.startActivity(intent);

                        }else if(lecture.getType() == 2 ){
                            Intent intent = new Intent(context, PDFViewerActivity.class);
                            intent.putExtra(ItemsUI.BUNDLE_COURSES_DETAILS, (Serializable) lecture);
                            intent.putExtra(ItemsUI.BUNDLE_COURSES_DETAILS_LECTURE, (Serializable) course);
                            intent.putExtra("sectionn", 0);
                            intent.putExtra("lessonid", lecture.getId());
                            context.startActivity(intent);

                        }
                    }
                });
            }

        }


    }
    public void onBindViewHolder_Quiz(@NonNull ChildViewHolder holder , int group, final int position) {

        //here we will bind data to our layout
        final Lecture lecture = lecturesectionList.get(group).getLectures().get(position);
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

    public void onBindViewHolder_Selectable(@NonNull ChildViewHolder holder , int group, final int position) {

        //here we will bind data to our layout
        final Lecture lecture = lecturesectionList.get(group).getLectures().get(position);
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
        if(lecture.getType() == 1)
        {
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
        else if(lecture.getType() == 2)
        {
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


    }
    @Override
    public int getChildItemViewType(int i, int i1 , int type) {

        int data_type = lecturesectionList.get(i).getLectures().get(i1).getType() + 1;
        Log.d("DataType", "getChildItemViewType: " + lecturesectionList.get(i).getLectures().get(i1).getLectureName() +  " " + data_type);
        return data_type;

    }



    public class ChildViewHolder extends RecyclerView.ViewHolder
    {
        TextViewAr lessonName , lessonLength , video_avaliabilty;
        public ChildViewHolder(View itemView) {
            super(itemView);
            lessonName = itemView.findViewById(R.id.lesson_name);
            lessonLength = itemView.findViewById(R.id.lesson_length);
            video_avaliabilty = itemView.findViewById(R.id.video_avaliabilty);
        }
    }
}
