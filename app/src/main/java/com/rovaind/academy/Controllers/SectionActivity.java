package com.rovaind.academy.Controllers;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rovaind.academy.R;
import com.rovaind.academy.Utils.ItemsUI;
import com.rovaind.academy.Utils.Toasty.Toasty;
import com.rovaind.academy.Utils.networksync.CheckInternetConnection;
import com.rovaind.academy.Utils.views.TextViewAr;
import com.rovaind.academy.adapter.ClassCategoriesAdapter;
import com.rovaind.academy.interfaces.IntResultReceiver;
import com.rovaind.academy.interfaces.OnCataClickListener;
import com.rovaind.academy.manager.Categories;
import com.rovaind.academy.model.Category;
import com.rovaind.academy.retrofit.ApiInterface;
import com.rovaind.academy.retrofit.RetrofitClient;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.rovaind.academy.Utils.ThemeUtil.THEME_PURPLE;

public class SectionActivity extends BaseActivity implements IntResultReceiver , OnCataClickListener {

    LinearLayout primary , middle, high , univ;
    RelativeLayout sectionPanel;
    int course_id = 0;
    int section_id = 0;
    int center_id = 0;
    CategoryBottomSheetFragment fragment = new CategoryBottomSheetFragment();
    //Dialog
    public static Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_section);

        TextViewAr appname = findViewById(R.id.appname);
        appname.setText(getResources().getString(R.string.levels));
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
        sectionPanel = findViewById(R.id.sectionPanel);
        primary = findViewById(R.id.primary_panel);
        middle = findViewById(R.id.middle_panel);
        high = findViewById(R.id.high_panel);
        univ = findViewById(R.id.univ_panel);
        center_id = getIntent().getIntExtra(ItemsUI.BUNDLE_CATEGORIES_CENTER_ID , 0);
        section_id = getIntent().getIntExtra(ItemsUI.BUNDLE_CATEGORIES_SECTION , 0);
        primary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                course_id = 101;
                getSubCategory(course_id);
               // showDialog(SectionActivity.this);
                //fragment.show(getSupportFragmentManager(), fragment.getTag());
            }
        });

        middle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                course_id = 102;
                getSubCategory(course_id);
                //fragment.show(getSupportFragmentManager(), fragment.getTag());
            }
        });
        high.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                course_id = 103;
                getSubCategory(course_id);
                //fragment.show(getSupportFragmentManager(), fragment.getTag());
            }
        });

        univ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //course_id = 104; TODO
                Toasty.info(SectionActivity.this, getResources().getString(R.string.coming_soon), Toast.LENGTH_SHORT, true).show();
                //Toast.makeText(SectionActivity.this, getResources().getText(R.string.coming_soon), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onResume() {
        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();
        super.onResume();
    }

    @Override
    public int getCategoryResult() {
        return course_id;
    }

    @Override
    public int getCenterResult() {
        return center_id;
    }

    @Override
    public int getSectionResult() {
        return section_id;
    }

    public void getSubCategory(int subCategory)
    {
        showProgressDialog();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SectionActivity.this);
        final String data = prefs.getString("language", "ar"); //no id: default value
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface service = retrofit.create(ApiInterface.class);


        Call<Categories> call = service.getSubCategories(subCategory , 2 , 1);

        call.enqueue(new Callback<Categories>() {
            @Override
            public void onResponse(Call<Categories> call, Response<Categories> response) {
                showDialog(SectionActivity.this , response.body().getCategories());
                hideProgressDialog();
            }

            @Override
            public void onFailure(Call<Categories> call, Throwable t) {
                hideProgressDialog();
            }
        });
    }
    public void showDialog(Activity activity , List<Category> categoryList){

        dialog = new Dialog(activity);
        // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_recycler);

        Button btndialog = (Button) dialog.findViewById(R.id.btndialog);
        btndialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                sectionPanel.setEnabled(true);
            }
        });


        RecyclerView recyclerView = dialog.findViewById(R.id.recycler);
        ClassCategoriesAdapter adapterRe = new ClassCategoriesAdapter(categoryList , this);
        recyclerView.setAdapter(adapterRe);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        dialog.show();
        sectionPanel.setEnabled(false);


    }

    @Override
    public void onCategoryClicked(Category contact, int position) {
        Intent intent = new Intent(SectionActivity.this, SubCategoryActivity.class);
        intent.putExtra(ItemsUI.BUNDLE_CATEGORIES_LIST, (Serializable) contact);
        intent.putExtra(ItemsUI.BUNDLE_CATEGORIES_CENTER_ID, center_id);
        intent.putExtra(ItemsUI.BUNDLE_CATEGORIES_ID, course_id);
        intent.putExtra(ItemsUI.BUNDLE_CATEGORIES_SECTION, section_id);
        intent.putExtra(ItemsUI.BUNDLE_THEME, THEME_PURPLE);
        startActivity(intent);
    }
}