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
import com.rovaind.academy.adapter.CentersAdapter;
import com.rovaind.academy.interfaces.OnCenterClickListener;
import com.rovaind.academy.manager.Centers;
import com.rovaind.academy.manager.SharedPrefManager;
import com.rovaind.academy.model.Center;
import com.rovaind.academy.retrofit.ApiInterface;
import com.rovaind.academy.retrofit.RetrofitClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.rovaind.academy.Utils.ThemeUtil.THEME_PURPLE;

public class CentersActivity extends BaseActivity implements OnCenterClickListener {

    private static final String TAG = "SubCategoryActivity";

    private ArrayList<Center> categoeries_list;
    private CentersAdapter categoriesAdapte;
    private RecyclerView recList;
    private int dcategoryID;
    private ShimmerFrameLayout shimmerFrameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTheme(ThemeUtil.getThemeId(getIntent().getIntExtra(ItemsUI.BUNDLE_THEME , 0)));
        setContentView(R.layout.activity_centers);

        TextViewAr appname = findViewById(R.id.appname);
        appname.setText(getResources().getString(R.string.centers));
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


        dcategoryID = getIntent().getIntExtra(ItemsUI.BUNDLE_CATEGORIES_SECTION , 0);
        recList = (RecyclerView) findViewById(R.id.recyclerview);
        categoeries_list = new ArrayList<>();
        categoriesAdapte = new CentersAdapter(categoeries_list ,this);
        LinearLayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recList.setLayoutManager(mLayoutManager);
        recList.setItemAnimator(new DefaultItemAnimator());
        recList.setAdapter(categoriesAdapte);

        getSubCategory();



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

    public void getSubCategory()
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final String data = prefs.getString("language", "ar"); //no id: default value
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface service = retrofit.create(ApiInterface.class);


        Call<Centers> call = service.getAllCenters(1);

        call.enqueue(new Callback<Centers>() {
            @Override
            public void onResponse(Call<Centers> call, Response<Centers> response) {
                Log.d(TAG, "onResponse: " +response.body().getCenters() );
                categoeries_list.addAll(response.body().getCenters());
                categoriesAdapte.notifyDataSetChanged();
                shimmerFrameLayout.setVisibility(View.GONE);
                shimmerFrameLayout.stopShimmer();
                if (categoeries_list.size() > 0) {
                    recList.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Centers> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
    }



    @Override
    public void onCategoryClicked(Center contact, int position) {
        /*
        Intent intent = new Intent(CentersActivity.this, SectionActivity.class);
        intent.putExtra(ItemsUI.BUNDLE_CATEGORIES_CENTERS, contact.getId());
        intent.putExtra(ItemsUI.BUNDLE_CATEGORIES_SECTION, section_id);
        intent.putExtra(ItemsUI.BUNDLE_THEME, THEME_ORANGE);
        startActivity(intent);

         */
        int LevelID = SharedPrefManager.getInstance(getApplicationContext()).getSelectedLevel();
        int ClassID = SharedPrefManager.getInstance(getApplicationContext()).getSelectedClass();
        Log.d(TAG, "onCategoryClicked: " + LevelID + ClassID);
        Intent intent = new Intent(CentersActivity.this, SubCategoryActivity.class);
       // intent.putExtra(ItemsUI.BUNDLE_CATEGORIES_LIST, (Serializable) contact);
        intent.putExtra(ItemsUI.BUNDLE_CATEGORIES_CENTER_ID, contact.getId());
        intent.putExtra(ItemsUI.BUNDLE_CATEGORIES_LEVEL, LevelID);
        intent.putExtra(ItemsUI.BUNDLE_CATEGORIES_CLASS, ClassID);
        intent.putExtra(ItemsUI.BUNDLE_CATEGORIES_SECTION, dcategoryID);
        intent.putExtra(ItemsUI.BUNDLE_THEME, THEME_PURPLE);
        startActivity(intent);
    }
}