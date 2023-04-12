package com.rovaind.academy.Controllers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.rovaind.academy.R;
import com.rovaind.academy.Utils.CircleImageView;
import com.rovaind.academy.Utils.ItemsUI;
import com.rovaind.academy.Utils.views.TextViewAr;
import com.rovaind.academy.auth.FacebookLoginActivity;
import com.rovaind.academy.manager.SharedPrefManager;
import com.rovaind.academy.manager.response.GroupResponse;
import com.rovaind.academy.model.Course;
import com.rovaind.academy.model.Instructor;
import com.rovaind.academy.retrofit.ApiInterface;
import com.rovaind.academy.retrofit.RetrofitClient;

import java.io.Serializable;
import java.sql.Timestamp;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupDetails extends BaseActivity {

    private static final String TAG = "CourseDetails" ;
    ApiInterface apiInterface;
    Course courses;

    TextViewAr TxtName , TxtDescription , TxtPhone , TxtLocation  , btnJoin;
    CircleImageView OwnerImage;

    LinearLayout PersonalInfoPanel , ContactPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_details);
        // Back Button
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        OwnerImage = findViewById(R.id.OwnerImage);
        TxtName = findViewById(R.id.TxtName);
        TxtDescription = findViewById(R.id.TxtDescription);
        TxtPhone = findViewById(R.id.TxtPhone);
        TxtLocation = findViewById(R.id.TxtLocation);

        PersonalInfoPanel = findViewById(R.id.personalinfopanel);
        ContactPanel = findViewById(R.id.contactPanel);
        btnJoin = findViewById(R.id.btnJoin);
        /*
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            videoWeb.getSettings().setJavaScriptEnabled(true);
            videoWeb.setWebChromeClient(new WebChromeClient() {} );

        }

         */

        if(SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()){
            btnJoin.setText(getResources().getString(R.string.join_group));
          //  Toast.makeText(this, "FF", Toast.LENGTH_SHORT).show();
        }
        else
        {
            btnJoin.setText(getResources().getString(R.string.visit_group));
           // Toast.makeText(this, "TT", Toast.LENGTH_SHORT).show();
        }
        courses = (Course) getIntent().getExtras().getSerializable(ItemsUI.BUNDLE_COURSES_DETAILS);


        //popularCourse = (PopularCourse) getIntent().getExtras().getSerializable(ItemsUI.BUNDLE_COURSES_DETAILS);
        if (courses != null) {


            if(SharedPrefManager.getInstance(getApplicationContext()).CheckSubcribedGroup(courses.getId()) == null) {




                if (courses.getOwnerId() != 0 && courses.getOwnerName() != null && !courses.getOwnerName().equals("")) {

                    ContactPanel.setVisibility(View.VISIBLE);
                }
                //Log.d(TAG, "onCreate: " + SharedPrefManager.getInstance(getApplicationContext()).getCoursesCodes());
                TxtName.setText(courses.getCourseName());
                TxtDescription.setText(courses.getCourseDescription());

            /*
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                videoWeb.loadData("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/" + courses.getVideoUrlyou() + "\" frameborder=\"0\" allowfullscreen></iframe>", "text/html", "utf-8");
            }

             */
            }
            else
            {
                btnJoin.setText(getResources().getString(R.string.visit_group));
            }

        }
        LoadInstructor();
        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToCourse();
            }
        });

    }

    @Override
    protected void onResume() {
        if(SharedPrefManager.getInstance(getApplicationContext()).CheckSubcribedGroup(courses.getId()) == null) {
            if(SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()){
                btnJoin.setText(getResources().getString(R.string.join_group));
            }
            else
            {
                btnJoin.setText(getResources().getString(R.string.visit_group));
            }

        }
        else
        {
            btnJoin.setText(getResources().getString(R.string.visit_group));
        }
        super.onResume();
    }

    private void returnToCourse() {

        if(SharedPrefManager.getInstance(getApplicationContext()).CheckSubcribedGroup(courses.getId()) != null) {
            Intent loginIntent = new Intent(GroupDetails.this, CourseDetails.class);
            loginIntent.putExtra(ItemsUI.BUNDLE_COURSES_DETAILS, (Serializable) courses);
            startActivity(loginIntent);
        }
        else
        {
            if(SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()){
                btnJoin.setText(getResources().getString(R.string.join_group));
                showProgressDialog();
                final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                long CurrentTime = timestamp.getTime();

                ApiInterface api = RetrofitClient.retrofitWrite().create(ApiInterface.class);

                //defining the call
                Call<GroupResponse> call = api.MemberJoinGroup(
                        courses.getId(),
                        SharedPrefManager.getInstance(getApplicationContext()).getUser().getId(),
                        SharedPrefManager.getInstance(getApplicationContext()).getUser().getEmail(),
                        CurrentTime

                );

                call.enqueue(new Callback<GroupResponse>() {
                    @Override
                    public void onResponse(Call<GroupResponse> call, Response<GroupResponse> response) {
                        //uploadImagesToServer(response.body().getProduct().getId());
                        //hideProgressDialog();
                        //Toast.makeText()

                        if(response.body() != null)
                        {
                            SharedPrefManager.getInstance(getApplicationContext()).SubcribeGroup(courses);
                            hideProgressDialog();
                            Intent loginIntent = new Intent(GroupDetails.this, CourseDetails.class);
                            loginIntent.putExtra(ItemsUI.BUNDLE_COURSES_DETAILS, (Serializable) courses);
                            startActivity(loginIntent);
                        }
                        else
                        {
                            hideProgressDialog();
                        }

                        //Toast.makeText(GroupDetails.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        Log.i("retrofit", response.body().getMessage());//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();




                    }

                    @Override
                    public void onFailure(Call<GroupResponse> call, Throwable t) {
                        //hideProgressDialog();
                        // Toast.makeText(GroupDetails.this, t.getMessage(), Toast.LENGTH_LONG).show();
                        Log.i("retrofit", t.getMessage());//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                        hideProgressDialog();
                    }
                });
            }
            else
            {
                btnJoin.setText(getResources().getString(R.string.visit_group));
                Intent loginIntent = new Intent(GroupDetails.this, CourseDetails.class);
                loginIntent.putExtra(ItemsUI.BUNDLE_COURSES_DETAILS, (Serializable) courses);
                startActivity(loginIntent);
            }

        }
    }

    public void sendtoLogin()
    {
        Intent loginIntent = new Intent(GroupDetails.this, FacebookLoginActivity.class);
        startActivity(loginIntent);
        // getActivity().finish();

    }

    private void LoadInstructor() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final String data = prefs.getString("language", "ar"); //no id: default value
        apiInterface = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);

        Call<Instructor> call = apiInterface.getInstructorsbyid(courses.getOwnerId() , 1);

        call.enqueue(new Callback<Instructor>() {
            @Override
            public void onResponse(Call<Instructor> call, Response<Instructor> response) {



                setinstructor(response.body());

            }



            @Override
            public void onFailure(Call<Instructor> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());

            }
        });
    }
    private void setinstructor(Instructor instructor){
        //TxtOwner.setText(instructor.getInstructorName());
        if(instructor.getThumbImage() != null) {
            Glide.with(this).load(RetrofitClient.BASE_TeacherImage_URL + instructor.getThumbImage()).into(OwnerImage);
        }
        TxtPhone.setText("0" + instructor.getPhone());
        TxtLocation.setText(instructor.getCityCode() + ", " + instructor.getGovermentCode() +  ", " + getResources().getString(R.string.egypt));
        //PersonalInfoPanel.setVisibility(View.VISIBLE);

    }
}
