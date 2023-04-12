package com.rovaind.academy.Controllers;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.rovaind.academy.R;
import com.rovaind.academy.Utils.ItemsUI;
import com.rovaind.academy.Utils.views.TextViewAr;
import com.rovaind.academy.adapter.CoursesOwnersAdapter;
import com.rovaind.academy.manager.Courses;
import com.rovaind.academy.model.Course;
import com.rovaind.academy.model.Instructor;
import com.rovaind.academy.retrofit.ApiInterface;
import com.rovaind.academy.retrofit.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InstructorActivity extends BaseActivity {

    LinearLayout personalinfo, experience, review;
    TextViewAr personalinfobtn, coursesbtn, reviewbtn;
    Instructor instructor;
    TextViewAr TXTInstructorName , TXTInstructorPosition, TxtTotalStudent , TxtRate , TxtDescription , TxtPhone , TxtLocation , TxtSchool , TxtAge , TxtEmail;
    WebView videoWeb;
    //ImageView ImgLogo;
    RecyclerView lessonRecycler;
    CoursesOwnersAdapter lessonAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor);

        // Back Button
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        personalinfo = findViewById(R.id.personalinfo);
        experience = findViewById(R.id.experience);
        review = findViewById(R.id.review);
        personalinfobtn = findViewById(R.id.personalinfobtn);
        coursesbtn = findViewById(R.id.coursesbtn);
        reviewbtn = findViewById(R.id.reviewbtn);
        /*making personal info visible*/
        personalinfo.setVisibility(View.VISIBLE);
        experience.setVisibility(View.GONE);
        review.setVisibility(View.GONE);

       // ImgLogo = findViewById(R.id.ImgLogo);
        TXTInstructorName = findViewById(R.id.TXTInstructorName);
        TXTInstructorPosition = findViewById(R.id.TXTInstructorPosition);
        TxtTotalStudent = findViewById(R.id.TxtTotalStudent);
        TxtRate = findViewById(R.id.TxtRate);
        TxtDescription = findViewById(R.id.TxtDescription);
        TxtPhone = findViewById(R.id.TxtPhone);
        TxtLocation = findViewById(R.id.TxtLocation);
        TxtSchool = findViewById(R.id.TxtSchool);
        TxtAge = findViewById(R.id.TxtAge);
        TxtEmail = findViewById(R.id.TxtEmail);
        videoWeb = (WebView) findViewById(R.id.videoWebView);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            videoWeb.getSettings().setJavaScriptEnabled(true);
            videoWeb.setWebChromeClient(new WebChromeClient() {
            });
        }

        instructor = (Instructor) getIntent().getExtras().getSerializable(ItemsUI.BUNDLE_INSTRUCTOR_DETAILS);
        //popularCourse = (PopularCourse) getIntent().getExtras().getSerializable(ItemsUI.BUNDLE_COURSES_DETAILS);
        if (instructor != null) {
            TXTInstructorName.setText(instructor.getInstructorName());
            TXTInstructorPosition.setText(instructor.getInstructorPosition());
            TxtDescription.setText(instructor.getInstructorDescription());
            TxtTotalStudent.setText(instructor.getTotalStudents() + " " + getResources().getString(R.string.student));
            TxtPhone.setText("0" + instructor.getPhone());
            if(instructor.getRatecount() != 0) {
                TxtRate.setText(instructor.getRate() / instructor.getRatecount() + "/5");

            }
            TxtLocation.setText(instructor.getCityCode() + ", " + instructor.getGovermentCode() +  ", " + getResources().getString(R.string.egypt));
            TxtSchool.setText(instructor.getSchool());
            TxtAge.setText(instructor.getAge() + " " + getResources().getString(R.string.years));
            TxtEmail.setText(instructor.getSocialFacebook());
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                videoWeb.loadData("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/" + instructor.getVideoUrlyou() + "\" frameborder=\"0\" allowfullscreen></iframe>", "text/html", "utf-8");
            }


        }


        personalinfobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                personalinfo.setVisibility(View.VISIBLE);
                experience.setVisibility(View.GONE);
                review.setVisibility(View.GONE);
                personalinfobtn.setTextColor(getResources().getColor(R.color.blue));
                coursesbtn.setTextColor(getResources().getColor(R.color.grey));
                reviewbtn.setTextColor(getResources().getColor(R.color.grey));

            }
        });

        coursesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                personalinfo.setVisibility(View.GONE);
                experience.setVisibility(View.VISIBLE);
                review.setVisibility(View.GONE);
                personalinfobtn.setTextColor(getResources().getColor(R.color.grey));
                coursesbtn.setTextColor(getResources().getColor(R.color.blue));
                reviewbtn.setTextColor(getResources().getColor(R.color.grey));

            }
        });

        reviewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                personalinfo.setVisibility(View.GONE);
                experience.setVisibility(View.GONE);
                review.setVisibility(View.VISIBLE);
                personalinfobtn.setTextColor(getResources().getColor(R.color.grey));
                coursesbtn.setTextColor(getResources().getColor(R.color.grey));
                reviewbtn.setTextColor(getResources().getColor(R.color.blue));

            }
        });
        LoadCourses();
    }

    private void LoadCourses() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final String data = prefs.getString("language", "ar"); //no id: default value
        ApiInterface apiInterface = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);

        Call<Courses> call = apiInterface.getCoursesByOwner(instructor.getId() , 1);

        call.enqueue(new Callback<Courses>() {
            @Override
            public void onResponse(Call<Courses> call, Response<Courses> response) {



                setLessonList(response.body().getCourses());

            }



            @Override
            public void onFailure(Call<Courses> call, Throwable t) {

            }
        });
    }
    private void setLessonList(List<Course> videoLessionList){

        lessonRecycler = findViewById(R.id.lesson_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        lessonRecycler.setLayoutManager(layoutManager);
        lessonAdapter = new CoursesOwnersAdapter(this, videoLessionList);
        lessonRecycler.setAdapter(lessonAdapter);

    }
}