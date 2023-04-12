package com.rovaind.academy.Controllers;

import android.content.Intent;
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
import com.rovaind.academy.adapter.SubjectsAdapter;
import com.rovaind.academy.interfaces.OnCataClickListener;
import com.rovaind.academy.manager.Categories;
import com.rovaind.academy.model.Category;
import com.rovaind.academy.retrofit.ApiInterface;
import com.rovaind.academy.retrofit.RetrofitClient;

import java.io.Serializable;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.rovaind.academy.Utils.ThemeUtil.THEME_RED;

public class ExtraCategoryActivity extends BaseActivity implements OnCataClickListener {

    private static final String TAG = "SubCategoryActivity";
    private ArrayList<Category> categoeries_list;
    private SubjectsAdapter categoriesAdapte;
    private RecyclerView recList;
    private int category_id , section_id , center_id;
    private ShimmerFrameLayout shimmerFrameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTheme(ThemeUtil.getThemeId(getIntent().getIntExtra(ItemsUI.BUNDLE_THEME , 0)));
        setContentView(R.layout.activity_sub_category);

        TextViewAr appname = findViewById(R.id.appname);
        appname.setText(getResources().getString(R.string.extra_courses));
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


        category_id = getIntent().getIntExtra(ItemsUI.BUNDLE_CATEGORIES_ID , 0);
        section_id = getIntent().getIntExtra(ItemsUI.BUNDLE_CATEGORIES_SECTION , 0);
        center_id = getIntent().getIntExtra(ItemsUI.BUNDLE_CATEGORIES_CENTER_ID , 0);
        recList = (RecyclerView) findViewById(R.id.recyclerview);
        categoeries_list = new ArrayList<>();
        categoriesAdapte = new SubjectsAdapter(categoeries_list ,this);
        LinearLayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recList.setLayoutManager(mLayoutManager);
        recList.setItemAnimator(new DefaultItemAnimator());
        recList.setAdapter(categoriesAdapte);

        getSubCategory(category_id);



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

    public void getSubCategory(int subCategory)
    {

        //Log.d(TAG, "onCreateView: mcategory " + MainCategory);
        Log.d(TAG, "onCreateView: category " + subCategory);
      //  Log.d(TAG, "onCreateView: section " + Section);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final String data = prefs.getString("language", "ar"); //no id: default value
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface service = retrofit.create(ApiInterface.class);


        Call<Categories> call = service.getSubCategories(subCategory , 7 ,1);

        call.enqueue(new Callback<Categories>() {
            @Override
            public void onResponse(Call<Categories> call, Response<Categories> response) {
                Log.d(TAG, "onResponse: " +response.body().getCategories() );
                categoeries_list.addAll(response.body().getCategories());
                categoriesAdapte.notifyDataSetChanged();
                shimmerFrameLayout.setVisibility(View.GONE);
                shimmerFrameLayout.stopShimmer();
                if (categoeries_list.size() > 0) {
                    recList.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Categories> call, Throwable t) {

            }
        });
    }

    @Override
    public void onCategoryClicked(Category contact, int position) {
        Intent intent = new Intent(ExtraCategoryActivity.this, SubjectCoursesActivity.class);
        intent.putExtra(ItemsUI.BUNDLE_CATEGORIES_LIST, (Serializable) contact);
        intent.putExtra(ItemsUI.BUNDLE_CATEGORIES_SECTION, section_id);
        intent.putExtra(ItemsUI.BUNDLE_CATEGORIES_CENTER_ID, center_id);
        intent.putExtra(ItemsUI.BUNDLE_THEME, THEME_RED);
        startActivity(intent);
    }
}