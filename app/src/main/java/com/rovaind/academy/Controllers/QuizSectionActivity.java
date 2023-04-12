/*
 *  Copyright (c) 2018 Gabriele Corso
 *
 *  Distributed under the MIT software license, see the accompanying
 *  file LICENSE or http://www.opensource.org/licenses/mit-license.php.
 */

package com.rovaind.academy.Controllers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rovaind.academy.HomeActivity;
import com.rovaind.academy.R;
import com.rovaind.academy.Utils.ItemsUI;
import com.rovaind.academy.Utils.views.TextViewAr;
import com.rovaind.academy.manager.Sections;
import com.rovaind.academy.model.Course;
import com.rovaind.academy.model.Lecture;
import com.rovaind.academy.model.quiz.Section;
import com.rovaind.academy.retrofit.ApiInterface;
import com.rovaind.academy.retrofit.RetrofitClient;

import java.io.Serializable;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class QuizSectionActivity extends BaseActivity {

    private static final String TAG = "QuizSectionActivity";
    ApiInterface apiInterface;
    TextViewAr pageTv;
    ImageView navprecBt;
    ImageView navnextBt;
    ImageView closeBt;
    TextViewAr sectiontitleTv;
    ImageView sectionimage;
    TextViewAr sectiontextTv;
    // Section section;
    Lecture lecture;
    Course course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizsection);

        Intent intent = getIntent();
        lecture = (Lecture) getIntent().getExtras().getSerializable(ItemsUI.BUNDLE_COURSES_DETAILS);
        course = (Course) getIntent().getExtras().getSerializable(ItemsUI.BUNDLE_COURSES_DETAILS_LECTURE);
       // Toast.makeText(this, "" + lecture.getLectureName(), Toast.LENGTH_SHORT).show();
        final int lessonid = intent.getIntExtra("lessonid", 0);
        final int sectionn = intent.getIntExtra("sectionn", 0);

       // LessonsLDH lessonsLDH = LessonsLDH.getInstance(this);
        //section = lessonsLDH.getSection(lessonid, sectionn);
        LoadLSections(lessonid , sectionn);

    }

    private void LoadLSections(int lessonid , int sectionID) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final String data = prefs.getString("language", "ar"); //no id: default value
        apiInterface = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);

        Call<Sections> call = apiInterface.getSectionByID(lessonid);

        call.enqueue(new Callback<Sections>() {
            @Override
            public void onResponse(Call<Sections> call, Response<Sections> response) {


                if(response.body() != null)
                {

                    SetSections(response.body().getSections() , lessonid, sectionID);
                }
                else
                {
                    Log.d(TAG, "onResponse: " + response.message()) ;
                    Log.d(TAG, "onResponse: " + response.code()) ;
                    Log.d(TAG, "onResponse: " + response.body()) ;
                    Log.d(TAG, "onResponse: " + response.errorBody().toString()) ;
                }
                // setLessonList(response.body().getLectures());

            }



            @Override
            public void onFailure(Call<Sections> call, Throwable t) {

            }
        });
    }

    private void SetSections(ArrayList<Section> section  , int LessonID , int sectionNumber) {
        if(section == null){
            Toast.makeText(QuizSectionActivity.this, "Problems while loading section "
                    + Integer.toString(sectionNumber), Toast.LENGTH_LONG).show();//TODO section number
        }

        pageTv = findViewById(R.id.page);
        navprecBt  = findViewById(R.id.navprec);
        navnextBt = findViewById(R.id.navnext);
        closeBt = findViewById(R.id.closebt);
        sectiontitleTv = findViewById(R.id.sectiontitle);
        sectionimage = findViewById(R.id.sectionimage);
        sectiontextTv = findViewById(R.id.sectiontext);

        sectiontitleTv.setText(section.get(sectionNumber).getSectionTitle());
        String imagename = "s" + Integer.toString(LessonID) + Integer.toString(sectionNumber);

        sectiontextTv.setText(section.get(sectionNumber).getSectionContent());

        String page = section.get(sectionNumber).getSectionTitle() + "  " + Integer.toString(sectionNumber+1)
                + "/" + Integer.toString(section.size());//TODO Lesson Count
        pageTv.setText(page);

        Glide.with(QuizSectionActivity.this)
                .load(RetrofitClient.BASE_SECTION_URL +  section.get(sectionNumber).getThumbImage()) // image url
                .apply(new RequestOptions()
                        .centerCrop())
                .into(sectionimage);  // imageview object
        closeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuizSectionActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        if(sectionNumber == 0){
            navprecBt.setVisibility(View.GONE);
        } else {
            navprecBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeSection(LessonID, sectionNumber-1);
                }
            });
        }

        navnextBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sectionNumber<section.size()-1){
                    changeSection(LessonID, sectionNumber+1);
                } else {
                    startQuiz(LessonID);
                }
            }
        });
    }

    void changeSection(int lessonid, int sectionn){
        Intent intent = new Intent(QuizSectionActivity.this, QuizSectionActivity.class);
        intent.putExtra(ItemsUI.BUNDLE_COURSES_DETAILS, (Serializable) lecture);
        intent.putExtra(ItemsUI.BUNDLE_COURSES_DETAILS_LECTURE, (Serializable) course);
        intent.putExtra("sectionn", sectionn);
        intent.putExtra("lessonid", lessonid);
        startActivity(intent);
    }

    void startQuiz(int lessonid){

        Intent intent = new Intent(QuizSectionActivity.this, QuizActivity.class);
        intent.putExtra(ItemsUI.BUNDLE_COURSES_DETAILS, (Serializable) lecture);
        intent.putExtra(ItemsUI.BUNDLE_COURSES_DETAILS_LECTURE, (Serializable) course);
        intent.putExtra("lessonid", lessonid);
        startActivity(intent);


    }
}
