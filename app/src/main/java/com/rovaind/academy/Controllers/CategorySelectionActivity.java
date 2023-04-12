package com.rovaind.academy.Controllers;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.rovaind.academy.HomeActivity;
import com.rovaind.academy.R;
import com.rovaind.academy.Utils.ExpandableRecyclerView;
import com.rovaind.academy.Utils.Toasty.Toasty;
import com.rovaind.academy.Utils.views.TextViewAr;
import com.rovaind.academy.adapter.ClassSectionAdapter;
import com.rovaind.academy.interfaces.OnCategoryClickListener;
import com.rovaind.academy.manager.Categories;
import com.rovaind.academy.manager.SharedPrefManager;
import com.rovaind.academy.model.Category;
import com.rovaind.academy.retrofit.ApiInterface;
import com.rovaind.academy.retrofit.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CategorySelectionActivity extends BaseActivity implements OnCategoryClickListener {

    private static final String TAG = "CategorySelectionActivity";
    ExpandableRecyclerView categoryRecycler;
    ClassSectionAdapter classSectionAdapter;
    boolean selectClass = false;
    int SelectedClass = 0;
    int SelectedLevel = 0;
    private TextViewAr btnSelectClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_selection);
        btnSelectClass = findViewById(R.id.btnSelectClass);
        showProgressDialog();
        getSubCategory(0);
        btnSelectClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectClass && SelectedClass != 0 && SelectedLevel !=0)
                {
                    SharedPrefManager.getInstance(CategorySelectionActivity.this).SelectedClass(SelectedClass);
                    SharedPrefManager.getInstance(CategorySelectionActivity.this).SelectedLevel(SelectedLevel);
                    finish();
                    startActivity(new Intent(CategorySelectionActivity.this, HomeActivity.class));
                   // Toasty.info(CategorySelectionActivity.this, getResources().getString(R.string.welcome), Toast.LENGTH_SHORT, true).show();
                }
                else
                {
                    Toasty.error(CategorySelectionActivity.this, getResources().getString(R.string.please_select_class_first), Toast.LENGTH_SHORT, true).show();
                }
            }
        });
    }

    public void getSubCategory(int subCategory)
    {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(CategorySelectionActivity.this);
        final String data = prefs.getString("language", "ar"); //no id: default value
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface service = retrofit.create(ApiInterface.class);


        Call<Categories> call = service.getSubCategories(subCategory , 1 , 1);

        call.enqueue(new Callback<Categories>() {
            @Override
            public void onResponse(Call<Categories> call, Response<Categories> response) {

                if(response.body() != null)
                {

                    setCategoryList(response.body().getCategories());
                    hideProgressDialog();
                }
                else
                {

                }
                // setLessonList(response.body().getLectures());

            }

            @Override
            public void onFailure(Call<Categories> call, Throwable t) {
                Log.d("ERROR", "onFailure: " + t.getLocalizedMessage());
            }
        });
    }
    private void setCategoryList(List<Category> categoryList){


        categoryRecycler = findViewById(R.id.class_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        categoryRecycler.setLayoutManager(layoutManager);
        classSectionAdapter = new ClassSectionAdapter(this , categoryList , this);
        categoryRecycler.setAdapter(classSectionAdapter);

    }

    @Override
    public void onCategoryClicked(Category contact, int mainPosition, int subPosition) {

        showProgressDialog();
        if (SelectedClass == contact.getSubcategory().get(subPosition).getId() ) {
            btnSelectClass.setEnabled(false);
            selectClass = false;
            SelectClassID(0 , 0);

        }
        else if (SelectedClass != contact.getSubcategory().get(subPosition).getId()  && SelectedClass != 0) {

            SelectClassID(contact.getSubcategory().get(subPosition).getId() , contact.getSubcategory().get(subPosition).getParentId());
            selectClass = true;
            Toasty.success(CategorySelectionActivity.this, contact.getSubcategory().get(subPosition).getCatName(), Toast.LENGTH_SHORT, true).show();
        }
        else
        {
            selectClass = true;
            btnSelectClass.setEnabled(true);
            SelectClassID(contact.getSubcategory().get(subPosition).getId() , contact.getSubcategory().get(subPosition).getParentId());
            Toasty.success(CategorySelectionActivity.this, contact.getSubcategory().get(subPosition).getCatName(), Toast.LENGTH_SHORT, true).show();
        }


    }

    private void SelectClassID(int ClassID , int LevelID) {
        SelectedClass = ClassID;
        SelectedLevel = LevelID;
        hideProgressDialog();
    }

    public ProgressDialog mProgressDialog;
    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage(this.getResources().getString(R.string.loading));
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}