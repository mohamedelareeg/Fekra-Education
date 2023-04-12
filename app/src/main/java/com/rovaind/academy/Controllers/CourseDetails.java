package com.rovaind.academy.Controllers;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.rovaind.academy.HomeActivity;
import com.rovaind.academy.R;
import com.rovaind.academy.Utils.CircleImageView;
import com.rovaind.academy.Utils.EndlessRecyclerViewScrollListener;
import com.rovaind.academy.Utils.ExpandableRecyclerView;
import com.rovaind.academy.Utils.ItemsUI;
import com.rovaind.academy.Utils.Toasty.Toasty;
import com.rovaind.academy.Utils.countdownview.CountdownView;
import com.rovaind.academy.Utils.views.TextViewAr;
import com.rovaind.academy.adapter.BlogRecyclyerAdapter;
import com.rovaind.academy.adapter.BlogTeacherRecyclyerAdapter;
import com.rovaind.academy.adapter.LessonSectionAdapter;
import com.rovaind.academy.auth.FacebookLoginActivity;
import com.rovaind.academy.interfaces.CourseResultReceiver;
import com.rovaind.academy.interfaces.OnPostClickListener;
import com.rovaind.academy.manager.LectureSections;
import com.rovaind.academy.manager.Posts;
import com.rovaind.academy.manager.SharedPrefManager;
import com.rovaind.academy.manager.response.CourseResponse;
import com.rovaind.academy.model.Code;
import com.rovaind.academy.model.Course;
import com.rovaind.academy.model.Instructor;
import com.rovaind.academy.model.Lecturesection;
import com.rovaind.academy.model.Post;
import com.rovaind.academy.retrofit.ApiInterface;
import com.rovaind.academy.retrofit.RetrofitClient;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CourseDetails extends BaseActivity implements CourseResultReceiver , OnPostClickListener , PostBottomSheetFragment.onSomeEventListener , ShopBottomSheetFragment.onCodeEventListener , Shop_BottomSheetFragment.onCodeEventListener_ {

    private static final String TAG = "CourseDetails" ;
    LinearLayout personalinfo, content, content_, review , buyPanel , buyPanel_;
    TextViewAr personalinfobtn, experiencebtn , extrabtn;
    ApiInterface apiInterface;
    ExpandableRecyclerView lessonRecycler;
    LessonSectionAdapter lessonAdapter;
    ExpandableRecyclerView lessonRecycler_;
    LessonSectionAdapter lessonAdapter_;

    Course courses;
  //  WebView videoWeb;


    //TextViewAr  TxtDescription , TxtOwner;
    LinearLayout teacherPanel , publishPanel;
    TextViewAr TxtName    , TxtLectureCount , TXTInstructorName , TXTInstructorPosition , TxtPhone , TxtLocation , BtnBuy , BtnBuy_;
    private CountdownView mCvCountdownView , mCvCountdownView_;
    ShopBottomSheetFragment fragment = new ShopBottomSheetFragment();
    Shop_BottomSheetFragment fragment_ = new Shop_BottomSheetFragment();
    PostBottomSheetFragment fragmentpost = new PostBottomSheetFragment();
    CircleImageView OwnerImage;

    private List<Post> postList;
    private BlogRecyclyerAdapter postsAdapter;
    private RecyclerView recPosts;

    private List<Post> postListTeacher;
    private BlogTeacherRecyclyerAdapter postsAdapterTeacher;
    private RecyclerView recPostsTeacher;
    private int userPage = 1;
    boolean intentToReg = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);
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
       // TxtDescription = findViewById(R.id.TxtDescription);
        //TxtOwner = findViewById(R.id.TxtOwner);
        //TxtEnrolled = findViewById(R.id.TxtEnrolled);
        //TxtRate = findViewById(R.id.TxtRate);
        TxtLectureCount = findViewById(R.id.TxtLectureCount);
        TXTInstructorName = findViewById(R.id.TXTInstructorName);
        TXTInstructorPosition = findViewById(R.id.TXTInstructorPosition);
        TxtPhone = findViewById(R.id.TxtPhone);
        TxtLocation = findViewById(R.id.TxtLocation);
        BtnBuy = findViewById(R.id.BtnBuy);
        BtnBuy_ = findViewById(R.id.BtnBuy_);
        buyPanel = findViewById(R.id.buyPanel);
        buyPanel_ = findViewById(R.id.buyPanel_);
        publishPanel = findViewById(R.id.publishPanel);


        //videoWeb = (WebView) findViewById(R.id.videoWebView);
        mCvCountdownView = (CountdownView) findViewById(R.id.cv_countdownView);
        mCvCountdownView_ = (CountdownView) findViewById(R.id.cv_countdownView_);

        teacherPanel = findViewById(R.id.teacherPanel);

        /*
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            videoWeb.getSettings().setJavaScriptEnabled(true);
            videoWeb.setWebChromeClient(new WebChromeClient() {} );

        }

         */


        courses = (Course) getIntent().getExtras().getSerializable(ItemsUI.BUNDLE_COURSES_DETAILS);


        //popularCourse = (PopularCourse) getIntent().getExtras().getSerializable(ItemsUI.BUNDLE_COURSES_DETAILS);
        if (courses != null) {

            if (SharedPrefManager.getInstance(getApplicationContext()).getCoursesExtraCodes(courses.getId()) != null) {
                final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                long to_mil = SharedPrefManager.getInstance(getApplicationContext()).getEndTimeExtra(courses.getId());
                long diff = to_mil - timestamp.getTime();
                if (SharedPrefManager.getInstance(getApplicationContext()).getCoursesExtraCodes(courses.getId()).getCourseId().equals(courses.getId()) && diff > 0) {
                    buyPanel_.setVisibility(View.GONE);

                    Log.d(TAG, "onCreate: " + to_mil);
                    Log.d(TAG, "onCreate: " + timestamp.getTime());
                    Log.d(TAG, "onCreate: " + diff);

                    mCvCountdownView_.setVisibility(View.VISIBLE);
                    mCvCountdownView_.start(diff); // Millisecond
                } else {
                    Toasty.error(this, getResources().getString(R.string.code_has_expired_please_enter_new_code), Toast.LENGTH_SHORT, true).show();
                    //Toast.makeText(this, getResources().getString(R.string.code_has_expired_please_enter_new_code), Toast.LENGTH_SHORT).show();
                    SharedPrefManager.getInstance(getApplicationContext()).clearcourseextra(courses.getId());
                }
            }

                if (SharedPrefManager.getInstance(getApplicationContext()).getCoursesCodes(courses.getId()) != null) {
                    final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    long to_mil = SharedPrefManager.getInstance(getApplicationContext()).getEndTime(courses.getId());
                    long diff = to_mil - timestamp.getTime();
                    if (SharedPrefManager.getInstance(getApplicationContext()).getCoursesCodes(courses.getId()).getCourseId().equals(courses.getId()) && diff > 0) {
                        buyPanel.setVisibility(View.GONE);

                        Log.d(TAG, "onCreate: " + to_mil);
                        Log.d(TAG, "onCreate: " + timestamp.getTime());
                        Log.d(TAG, "onCreate: " + diff);

                        mCvCountdownView.setVisibility(View.VISIBLE);
                        mCvCountdownView.start(diff); // Millisecond
                    } else {
                        Toasty.error(this, getResources().getString(R.string.code_has_expired_please_enter_new_code), Toast.LENGTH_SHORT, true).show();
                        //Toast.makeText(this, getResources().getString(R.string.code_has_expired_please_enter_new_code), Toast.LENGTH_SHORT).show();
                        SharedPrefManager.getInstance(getApplicationContext()).clearcourse(courses.getId());
                    }
                }

                if (courses.getOwnerId() != 0 && courses.getOwnerName() != null && !courses.getOwnerName().equals("")) {
                    teacherPanel.setVisibility(View.VISIBLE);
                }
                //Log.d(TAG, "onCreate: " + SharedPrefManager.getInstance(getApplicationContext()).getCoursesCodes());
                TxtName.setText(courses.getCourseName());
                //TxtDescription.setText(courses.getCourseDescription());
          //      TxtEnrolled.setText(getResources().getString(R.string.enrolled_students) + " : " + courses.getEnrolled() + " " + getResources().getString(R.string.students));
                TxtLectureCount.setText(getResources().getString(R.string.lecture_count) + " : " + courses.getLectureCount() + " " + getResources().getString(R.string.lectures));
            //    if (courses.getRatecount() != 0) {
              //      TxtRate.setText(getResources().getString(R.string.rating_count) + " : " + courses.getRatecount() + " " + getResources().getString(R.string.rating));
                    //TxtRate.setText( getResources().getString(R.string.rating_count) + " : " + courses.getRate() / courses.getRatecount() + "/5");

                //}
            /*
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                videoWeb.loadData("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/" + courses.getVideoUrlyou() + "\" frameborder=\"0\" allowfullscreen></iframe>", "text/html", "utf-8");
            }

             */


        }


        personalinfo = findViewById(R.id.personalinfo);
        content = findViewById(R.id.content);
        content_ = findViewById(R.id.content_);
        review = findViewById(R.id.review);
        personalinfobtn = findViewById(R.id.personalinfobtn);
        experiencebtn = findViewById(R.id.contentbtn);
        extrabtn = findViewById(R.id.pdfcontentbtn);
        /*making personal info visible*/
        personalinfo.setVisibility(View.VISIBLE);
        content.setVisibility(View.GONE);
        review.setVisibility(View.GONE);


        personalinfobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                personalinfo.setVisibility(View.VISIBLE);
                content.setVisibility(View.GONE);
                content_.setVisibility(View.GONE);
                personalinfobtn.setTextColor(getResources().getColor(R.color.white));
                experiencebtn.setTextColor(getResources().getColor(R.color.black));
                extrabtn.setTextColor(getResources().getColor(R.color.black));
                personalinfobtn.setBackgroundColor(getResources().getColor(R.color.black));
                experiencebtn.setBackgroundColor(getResources().getColor(R.color.white));
                extrabtn.setBackgroundColor(getResources().getColor(R.color.white));

            }
        });

        experiencebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                personalinfo.setVisibility(View.GONE);
                content.setVisibility(View.VISIBLE);
                content_.setVisibility(View.GONE);
                personalinfobtn.setTextColor(getResources().getColor(R.color.black));
                experiencebtn.setTextColor(getResources().getColor(R.color.white));
                extrabtn.setTextColor(getResources().getColor(R.color.black));
                personalinfobtn.setBackgroundColor(getResources().getColor(R.color.white));
                experiencebtn.setBackgroundColor(getResources().getColor(R.color.black));
                extrabtn.setBackgroundColor(getResources().getColor(R.color.white));

            }
        });
        extrabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                personalinfo.setVisibility(View.GONE);
                content.setVisibility(View.GONE);
                content_.setVisibility(View.VISIBLE);
                personalinfobtn.setTextColor(getResources().getColor(R.color.black));
                experiencebtn.setTextColor(getResources().getColor(R.color.black));
                extrabtn.setTextColor(getResources().getColor(R.color.white));
                personalinfobtn.setBackgroundColor(getResources().getColor(R.color.white));
                experiencebtn.setBackgroundColor(getResources().getColor(R.color.white));
                extrabtn.setBackgroundColor(getResources().getColor(R.color.black));

            }
        });

        LoadLectures();
        LoadLectures_();
        LoadInstructor();
        BtnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showShop();

            }
        });
        BtnBuy_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showShop_();

            }
        });
        publishPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showPost();

            }
        });

        incViewedCount();

        AssignPostListTeacher();
        AssignPostList();
        getDataTeacher(courses  ,  userPage);
        getData(courses  ,  userPage);

    }

    public void sendToHome()
    {
        Intent loginIntent = new Intent(CourseDetails.this, HomeActivity.class);
        startActivity(loginIntent);
        finish();
    }
    private void LoadLectures() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final String data = prefs.getString("language", "ar"); //no id: default value
        apiInterface = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);

        Call<LectureSections> call = apiInterface.getLecturesLessonByID(courses.getId() ,0 , 1);//(data.equals("en") ? 0 :1)

        call.enqueue(new Callback<LectureSections>() {
            @Override
            public void onResponse(Call<LectureSections> call, Response<LectureSections> response) {


                if(response.body() != null)
                {

                    setLessonList(response.body().getLecturesections());
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
            public void onFailure(Call<LectureSections> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
    }
    private void LoadLectures_() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final String data = prefs.getString("language", "ar"); //no id: default value
        apiInterface = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);

        Call<LectureSections> call = apiInterface.getLecturesLessonByID(courses.getId() ,1 , 1);//(data.equals("en") ? 0 :1)

        call.enqueue(new Callback<LectureSections>() {
            @Override
            public void onResponse(Call<LectureSections> call, Response<LectureSections> response) {


                if(response.body() != null)
                {

                    setLessonList_(response.body().getLecturesections());
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
            public void onFailure(Call<LectureSections> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
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

    private void AssignPostListTeacher(){

        postListTeacher = new ArrayList<>();
        recPostsTeacher = findViewById(R.id.teacherposts_list_view);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        recPostsTeacher.setLayoutManager(layoutManager);
        recPostsTeacher.setItemAnimator(new DefaultItemAnimator());
        recPostsTeacher.setHasFixedSize(true);
        postsAdapterTeacher = new BlogTeacherRecyclyerAdapter(postListTeacher , courses , this);
        recPostsTeacher.setAdapter(postsAdapterTeacher);

    }

    private void AssignPostList(){

        postList = new ArrayList<>();
        recPosts = findViewById(R.id.blog_list_view);
        LinearLayoutManager mLayoutManager = new GridLayoutManager(this, 1);

        recPosts.setLayoutManager(mLayoutManager);
        recPosts.setItemAnimator(new DefaultItemAnimator());
        recPosts.setHasFixedSize(true);
        recPosts.addOnScrollListener(new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                userPage++;
                getData(courses  ,  userPage);

            }
        });
        postsAdapter = new BlogRecyclyerAdapter(postList , courses , this);
        recPosts.setAdapter(postsAdapter);

    }
    private void getDataTeacher(Course courses, int userPage) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final String data = prefs.getString("language", "ar"); //no id: default value
        apiInterface = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);

        Call<Posts> call = apiInterface.getTeacherPosts(courses.getId() ,userPage);//(data.equals("en") ? 0 :1)

        call.enqueue(new Callback<Posts>() {
            @Override
            public void onResponse(Call<Posts> call, Response<Posts> response) {


                if(response.body() != null)
                {

                    postListTeacher.addAll(response.body().getPosts());
                    postsAdapterTeacher.notifyDataSetChanged();
                    Log.d(TAG, "onResponse: Pagecount" + userPage);
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
            public void onFailure(Call<Posts> call, Throwable t) {
                Log.d(TAG, "onFailure: Posts " + t.getLocalizedMessage());
            }
        });
    }
    private void getData(Course courses, int userPage) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final String data = prefs.getString("language", "ar"); //no id: default value
        apiInterface = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);

        Call<Posts> call = apiInterface.getPosts(courses.getId() ,userPage);//(data.equals("en") ? 0 :1)

        call.enqueue(new Callback<Posts>() {
            @Override
            public void onResponse(Call<Posts> call, Response<Posts> response) {


                if(response.body() != null)
                {

                    postList.addAll(response.body().getPosts());
                    postsAdapter.notifyDataSetChanged();
                    Log.d(TAG, "onResponse: Pagecount" + userPage);
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
            public void onFailure(Call<Posts> call, Throwable t) {
                Log.d(TAG, "onFailure: Posts " + t.getLocalizedMessage());
            }
        });
    }

    private void setLessonList(List<Lecturesection> lecturesections){


        lessonRecycler = findViewById(R.id.lesson_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        lessonRecycler.setLayoutManager(layoutManager);
        lessonAdapter = new LessonSectionAdapter(this , lecturesections, courses);
            lessonRecycler.setAdapter(lessonAdapter);

    }
    private void setLessonList_(List<Lecturesection> lecturesections){


        lessonRecycler_ = findViewById(R.id.lesson_recycler_);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        lessonRecycler_.setLayoutManager(layoutManager);
        lessonAdapter_ = new LessonSectionAdapter(this , lecturesections, courses);
        lessonRecycler_.setAdapter(lessonAdapter_);

    }
    private void setinstructor(Instructor instructor){
        //TxtOwner.setText(instructor.getInstructorName());
        Glide.with(this).load(RetrofitClient.BASE_TeacherImage_URL + instructor.getThumbImage()).into(OwnerImage);
        TXTInstructorName.setText(instructor.getInstructorName());
        TXTInstructorPosition.setText(instructor.getInstructorPosition());
        TxtPhone.setText("0" + instructor.getPhone());
        TxtLocation.setText(instructor.getCityCode() + ", " + instructor.getGovermentCode() +  ", " + getResources().getString(R.string.egypt));


    }
    void showShop() {
        if(!fragment.isAdded()) {
            fragment.show(getSupportFragmentManager(), fragment.getTag());


        }
    }
    void showShop_() {
        if(!fragment_.isAdded()) {
            fragment_.show(getSupportFragmentManager(), fragment_.getTag());


        }
    }
    void showPost() {
        if(!fragmentpost.isAdded()) {
            fragmentpost.show(getSupportFragmentManager(), fragmentpost.getTag());
        }

    }

    @Override
    protected void onResume() {

        if(intentToReg)
        {
            Toasty.success(this, getResources().getString(R.string.you_can_continue), Toast.LENGTH_SHORT, true).show();
            intentToReg = false;
            finish();
            overridePendingTransition( 0, 0);
            startActivity(getIntent());
            overridePendingTransition( 0, 0);
        }
        super.onResume();
    }

    public void sendtoLogin()
    {
        intentToReg = true;
        Intent loginIntent = new Intent(CourseDetails.this, FacebookLoginActivity.class);
        loginIntent.putExtra(ItemsUI.LOGIN_COURSE, 1);
        startActivity(loginIntent);
        // getActivity().finish();



    }
    @Override
    public Course getResult() {
        return courses;
    }


    private void incViewedCount() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface service = retrofit.create(ApiInterface.class);


        Call<CourseResponse> call = service.updateCourseView(
               courses.getId(),
                1

        );

        call.enqueue(new Callback<CourseResponse>() {
            @Override
            public void onResponse(Call<CourseResponse> call, Response<CourseResponse> response) {

            }

            @Override
            public void onFailure(Call<CourseResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void onPostClicked(Post post, Course course, int position) {

    }

    @Override
    public void PostEvent(Post post) {

        if(post.getOwnerId() == SharedPrefManager.getInstance(getApplicationContext()).getUser().getId())
        {
            postListTeacher.add(0 ,post);
            postsAdapterTeacher.notifyDataSetChanged();
        }
        else {
            postList.add(0, post);
            postsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void ShopEvent(Code code) {
        finish();
        overridePendingTransition( 0, 0);
        startActivity(getIntent());
        overridePendingTransition( 0, 0);
        Toasty.success(this, getResources().getString(R.string.you_succ_active_course), Toast.LENGTH_SHORT, true).show();

    }
    @Override
    public void ShopEvent_(Code code) {
        finish();
        overridePendingTransition( 0, 0);
        startActivity(getIntent());
        overridePendingTransition( 0, 0);
        Toasty.success(this, getResources().getString(R.string.you_succ_active_course), Toast.LENGTH_SHORT, true).show();

    }
}
