package com.rovaind.academy.Controllers;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rovaind.academy.R;
import com.rovaind.academy.Utils.ItemsUI;
import com.rovaind.academy.Utils.networksync.CheckInternetConnection;
import com.rovaind.academy.Utils.shimmer.ShimmerFrameLayout;
import com.rovaind.academy.Utils.views.TextViewAr;
import com.rovaind.academy.adapter.SubjectCoursesAdapter;
import com.rovaind.academy.manager.Courses;
import com.rovaind.academy.model.Category;
import com.rovaind.academy.model.Course;
import com.rovaind.academy.retrofit.ApiInterface;
import com.rovaind.academy.retrofit.RetrofitClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SubjectCoursesActivity extends BaseActivity  {

    private static final String TAG = "SubCategoryActivity";
    private Category category;
    private ArrayList<Course> courseArrayList;
    private SubjectCoursesAdapter subjectCoursesAdapter;
    private RecyclerView recList;
    private int class_id , level_id , section_id , center_id , type_id;
    private ShimmerFrameLayout shimmerFrameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setTheme(ThemeUtil.getThemeId(getIntent().getIntExtra(ItemsUI.BUNDLE_THEME , 0)));
        setContentView(R.layout.activity_sub_category);

        TextViewAr appname = findViewById(R.id.appname);
        appname.setText(getResources().getString(R.string.courses));
        // Back Button
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        //check Internet Connection

        new CheckInternetConnection(this).checkConnection();


        shimmerFrameLayout = findViewById(R.id.parentShimmerLayout);
        shimmerFrameLayout.startShimmer();

        category = (Category) getIntent().getExtras().getSerializable(ItemsUI.BUNDLE_CATEGORIES_LIST);
        class_id = getIntent().getIntExtra(ItemsUI.BUNDLE_CATEGORIES_CLASS , 0);
        level_id = getIntent().getIntExtra(ItemsUI.BUNDLE_CATEGORIES_LEVEL , 0);
        section_id = getIntent().getIntExtra(ItemsUI.BUNDLE_CATEGORIES_SECTION , 0);
        center_id = getIntent().getIntExtra(ItemsUI.BUNDLE_CATEGORIES_CENTER_ID , 0);
        type_id = getIntent().getIntExtra(ItemsUI.BUNDLE_CATEGORIES_TYPE , 0);
        //BUNDLE_CATEGORIES_TYPE
        recList = (RecyclerView) findViewById(R.id.recyclerview);
        courseArrayList = new ArrayList<>();
        subjectCoursesAdapter = new SubjectCoursesAdapter(this , courseArrayList);
        LinearLayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recList.setLayoutManager(mLayoutManager);
        recList.setItemAnimator(new DefaultItemAnimator());
        recList.setAdapter(subjectCoursesAdapter);

        if(class_id != 0 && center_id != 0 )
        {
            getCourses( level_id , class_id  , category.getId() , section_id , center_id , type_id);
        }
        else if(class_id != 0)
        {
            getCourses( class_id , category.getId() , section_id , type_id);
        }
        else
        {
            getCourses( category.getId() , section_id , type_id);
        }




    }
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
    @Override
    protected void onResume() {
        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();
        super.onResume();
    }

    public void getCourses(int levelID , int classID , int subjectID , int Section , int CenterID , int TypeID)
    {
        Log.d(TAG, "onCreateView: level " + levelID);
        Log.d(TAG, "onCreateView: class " + classID);
        Log.d(TAG, "onCreateView: subject " + subjectID);
        Log.d(TAG, "onCreateView: section " + Section);
        Log.d(TAG, "onCreateView: center " + CenterID);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final String data = prefs.getString("language", "ar"); //no id: default value
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface service = retrofit.create(ApiInterface.class);

        Call<Courses> call = service.getCoursesBySubject( CenterID + "@" +  classID + "#" + subjectID   , Section  , TypeID ,1);

        call.enqueue(new Callback<Courses>() {
            @Override
            public void onResponse(Call<Courses> call, Response<Courses> response) {
                Log.d(TAG, "onResponse: " +response.body().getCourses() );
                courseArrayList.addAll(response.body().getCourses());
                subjectCoursesAdapter.notifyDataSetChanged();
                shimmerFrameLayout.setVisibility(View.GONE);
                shimmerFrameLayout.stopShimmer();
                if (courseArrayList.size() > 0) {
                    recList.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Courses> call, Throwable t) {

            }
        });
    }
    public void getCourses(int classID , int subjectID , int Section , int TypeID )
    {
        Log.d(TAG, "onCreateView: class " + classID);
        Log.d(TAG, "onCreateView: subject " + subjectID);
        Log.d(TAG, "onCreateView: section " + Section);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final String data = prefs.getString("language", "ar"); //no id: default value
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface service = retrofit.create(ApiInterface.class);

        Call<Courses> call = service.getCoursesBySubject( classID + "#" + subjectID   , Section , TypeID ,1);

        call.enqueue(new Callback<Courses>() {
            @Override
            public void onResponse(Call<Courses> call, Response<Courses> response) {
                Log.d(TAG, "onResponse: " +response.body().getCourses() );
                courseArrayList.addAll(response.body().getCourses());
                subjectCoursesAdapter.notifyDataSetChanged();
                shimmerFrameLayout.setVisibility(View.GONE);
                shimmerFrameLayout.stopShimmer();
                if (courseArrayList.size() > 0) {
                    recList.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Courses> call, Throwable t) {

            }
        });
    }
    public void getCourses( int subjectID , int Section , int TypeID)
    {

        Log.d(TAG, "onCreateView: subject " + subjectID);
        Log.d(TAG, "onCreateView: section " + Section);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final String data = prefs.getString("language", "ar"); //no id: default value
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface service = retrofit.create(ApiInterface.class);

        Call<Courses> call = service.getCoursesBySubject(String.valueOf(subjectID) , Section , TypeID ,1);

        call.enqueue(new Callback<Courses>() {
            @Override
            public void onResponse(Call<Courses> call, Response<Courses> response) {
                Log.d(TAG, "onResponse: " +response.body().getCourses() );
                courseArrayList.addAll(response.body().getCourses());
                subjectCoursesAdapter.notifyDataSetChanged();
                shimmerFrameLayout.setVisibility(View.GONE);
                shimmerFrameLayout.stopShimmer();
                if (courseArrayList.size() > 0) {
                    recList.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Courses> call, Throwable t) {

            }
        });
    }

}