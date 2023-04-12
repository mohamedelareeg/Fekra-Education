package com.rovaind.academy.Controllers;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;


import com.rovaind.academy.R;
import com.rovaind.academy.Utils.EndlessRecyclerViewScrollListener;
import com.rovaind.academy.Utils.ItemsUI;
import com.rovaind.academy.Utils.networksync.CheckInternetConnection;
import com.rovaind.academy.Utils.shimmer.ShimmerFrameLayout;
import com.rovaind.academy.Utils.views.TextViewAr;
import com.rovaind.academy.adapter.FilterableCoursesAdapter;
import com.rovaind.academy.adapter.FilterableInstructorAdapter;
import com.rovaind.academy.adapter.FilterItemListAdapter;
import com.rovaind.academy.adapter.SortItemListAdapter;
import com.rovaind.academy.adapter.SubCategoriesAdapter;
import com.rovaind.academy.interfaces.OnCataClickListener;
import com.rovaind.academy.manager.Categories;
import com.rovaind.academy.manager.Courses;
import com.rovaind.academy.manager.Instructors;
import com.rovaind.academy.manager.SharedPrefManager;
import com.rovaind.academy.model.Category;
import com.rovaind.academy.model.Course;
import com.rovaind.academy.model.Instructor;
import com.rovaind.academy.retrofit.ApiInterface;
import com.rovaind.academy.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CategoriesActivity extends BaseActivity implements  OnCataClickListener {



    private Menu menu;
    private static final String TAG = "CategoriesActivity";
    private RecyclerView recList;
    private ArrayList<Course> products_list;
    private StaggeredGridLayoutManager products_staggeredGridLayoutManager;
    private FilterableCoursesAdapter products_listAdapter;
    private Category category;

    private int userPage = 1;

    private ArrayList<Instructor> instructorList;
    private FilterableInstructorAdapter instructorAdapter;

    TableLayout sortFilter;
    RelativeLayout sort, filter;
    TextViewAr sortByText;
    Map<Integer , String> sortBy = new HashMap<>();
    //String[] sortByArray = {"Most Recent", "Most Order", "Top Rated", "Most Viewed"};
    int sortById = 0;
    List<String> departmentFilter = new ArrayList<>();
    List<String> companyFilter = new ArrayList<>();
    List<String> packFilter = new ArrayList<>();
    List<String> statusFilter = new ArrayList<>();

    private String type;
    //

    private ArrayList<Category> categoeries_list;
    private SubCategoriesAdapter categoriesAdapte;
    private RecyclerView categories_recycleView;
    private RelativeLayout background;

    private ShimmerFrameLayout shimmerFrameLayout , ShimmerLayoutcategory;
    private Integer category_id;
    private String typeSwitch;

    //
    int LevelID;
    int ClassID;
    private int selectedKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);



        // Set Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Hide Title
        TextViewAr titleToolbar = findViewById(R.id.appname);
        titleToolbar.setVisibility(View.GONE);

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
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // getSupportActionBar().setDisplayShowHomeEnabled(true);


        init();
    }


    private void init() {

        LevelID = SharedPrefManager.getInstance(getApplicationContext()).getSelectedLevel();
        ClassID = SharedPrefManager.getInstance(getApplicationContext()).getSelectedClass();

        //String[] sortByArray = {"Most Recent", "Most Order", "Top Rated", "Most Viewed"};
        sortBy.put(0 , getResources().getString(R.string.most_recent));
        sortBy.put(1 , getResources().getString(R.string.most_order));
        sortBy.put(2 , getResources().getString(R.string.top_rated));
        sortBy.put(3 , getResources().getString(R.string.most_viewed));
        selectedKey = 0;
        shimmerFrameLayout = findViewById(R.id.parentShimmerLayout);
        ShimmerLayoutcategory = findViewById(R.id.parentShimmerLayoutcategory);

        ShimmerLayoutcategory.startShimmer();
        shimmerFrameLayout.startShimmer();
        category_id = getIntent().getIntExtra(ItemsUI.BUNDLE_CATEGORIES_ID , 0);
        type = getIntent().getStringExtra(ItemsUI.BUNDLE_CATEGORIES_LIST_TYPE);
        category = (Category) getIntent().getExtras().getSerializable(ItemsUI.BUNDLE_CATEGORIES_LIST);
        typeSwitch = getIntent().getStringExtra(ItemsUI.BUNDLE_CATEGORIES_SWITCH);
        recList = (RecyclerView) findViewById(R.id.recyclerview);


        background = findViewById(R.id.background);
        sortFilter = findViewById(R.id.sortFilter);


        instructorList = new ArrayList<>();
        instructorAdapter = new FilterableInstructorAdapter( this, instructorList);

        products_list = new ArrayList<>();
        products_listAdapter = new FilterableCoursesAdapter( this, products_list);


        LinearLayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recList.setLayoutManager(mLayoutManager);
        recList.setItemAnimator(new DefaultItemAnimator());
        recList.addOnScrollListener(new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                userPage++;
                if(category == null)
                {
                    getData( type , 0  ,  userPage, sortBy.get(selectedKey));
                }
                else {
                    getData(type , category.getId()  , userPage , sortByText.getText().toString());
                }

            }
        });

        switch (typeSwitch)
        {

            case "Course": {
                recList.setAdapter(products_listAdapter);
                break;
            }
            case "Instructor":
            {
                recList.setAdapter(instructorAdapter);
                break;
            }
            case "Center": {
                //TODO
                break;
            }

            default:
            {
                recList.setAdapter(products_listAdapter);
                break;
            }
        }


        sort = findViewById(R.id.sortLay);
        filter = findViewById(R.id.filterLay);
        sortByText = findViewById(R.id.sortBy);

        setSortListener();
        setFilterListener();
        sortByText.setText(sortBy.get(selectedKey));



        //
        categoeries_list = new ArrayList<>();
        categoriesAdapte = new SubCategoriesAdapter(categoeries_list ,this);
        categories_recycleView = findViewById(R.id.slider_cata);

        LinearLayoutManager cataManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        categories_recycleView.setItemAnimator(new DefaultItemAnimator());
        categories_recycleView.setLayoutManager(cataManager);
        categories_recycleView.setAdapter(categoriesAdapte);
        if(category == null)
        {
            //sortFilter.setVisibility(View.GONE);
           // Toast.makeText(this, "EEE", Toast.LENGTH_SHORT).show();
            getData(type , 0  ,1 , sortBy.get(selectedKey));
            //getSubCategory(category_id);
        }
        else
        {
            getData(type , category.getId()  ,1 , sortByText.getText().toString());
            //sortFilter.setVisibility(View.VISIBLE);

           // getSubCategory(category.getId());
        }
       // getCustomers();
       // getProducts();
       // getCustomer();

    }

    private int dpToPx_(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, dp, r.getDisplayMetrics()));
    }
    //Responsible For Adding the 3 tabs : Camera  , Home , Messages
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
    @Override
    protected void onResume() {
        super.onResume();
        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();
        //mShimmerViewContainer.startShimmer();
        // Update Cart Count


    }
    public void getAllPosts_old(String type , int pagenumbr , String sortById)
    {
/*
        ProductsAPIService service = RetrofitApi.retrofitReadWP3ProductsByCategory("products" , String.valueOf(category.getId()), "price", String.valueOf(pagenumbr)).create(ProductsAPIService.class);
        // String[] sortByArray = {"Most Recent", "Most Order", "Most Shares", "Most Viewed"};
        switch (type) {
            case "Main":
            case "Group":
            case "Sub":


                switch (selectedKey) {
                    case 0:

                        service = RetrofitApi.retrofitReadWP3ProductsByCategory("products" , String.valueOf(category.getId()), "date" , String.valueOf(pagenumbr)).create(ProductsAPIService.class);
                        break;

                    case 1:

                        service = RetrofitApi.retrofitReadWP3ProductsByCategory("products" , String.valueOf(category.getId()), "menu_order", String.valueOf(pagenumbr)).create(ProductsAPIService.class);
                        break;

                    case 2:

                        service = RetrofitApi.retrofitReadWP3ProductsByCategory("products" , String.valueOf(category.getId()), "rating", String.valueOf(pagenumbr)).create(ProductsAPIService.class);
                        break;
                    case 3:

                        service = RetrofitApi.retrofitReadWP3ProductsByCategory("products" , String.valueOf(category.getId()), "popularity", String.valueOf(pagenumbr)).create(ProductsAPIService.class);
                        break;


                    default:
                    {
                        service = RetrofitApi.retrofitReadWP3ProductsByCategory("products" , String.valueOf(category.getId()), "date", String.valueOf(pagenumbr)).create(ProductsAPIService.class);
                        break;
                    }
                }
                break;
            case "like":

                service = RetrofitApi.retrofitReadWP3ProductsByCategory("products" , String.valueOf(category_id), "popularity", String.valueOf(pagenumbr)).create(ProductsAPIService.class);
                break;
            case "recently":

                Toast.makeText(this, "TTT", Toast.LENGTH_SHORT).show();
                service = RetrofitApi.retrofitReadWP3ProductsByCategory("products" , String.valueOf(category.getId()), "date", String.valueOf(pagenumbr)).create(ProductsAPIService.class);
                break;

            case "sponsored":
                Toast.makeText(this, "TTT", Toast.LENGTH_SHORT).show();
                service = RetrofitApi.retrofitReadWP3ProductsByCategory("products" , String.valueOf(category.getId()), "title", String.valueOf(pagenumbr)).create(ProductsAPIService.class);
                break;


            case "suggest":
                Toast.makeText(this, "TTT", Toast.LENGTH_SHORT).show();
                service = RetrofitApi.retrofitReadWP3ProductsByCategory("products" , String.valueOf(category.getId()), "rating", String.valueOf(pagenumbr)).create(ProductsAPIService.class);
                break;

            case "viewed":
                Toast.makeText(this, "TTT", Toast.LENGTH_SHORT).show();
                service = RetrofitApi.retrofitReadWP3ProductsByCategory("products" , String.valueOf(category.getId()), "popularity", String.valueOf(pagenumbr)).create(ProductsAPIService.class);
                break;

            default:
            {
                service = RetrofitApi.retrofitReadWP3ProductsByCategory("products" , String.valueOf(category.getId()), "date", String.valueOf(pagenumbr)).create(ProductsAPIService.class);
                break;
            }
        }


        Call<ArrayList<com.mohaa.dokan.models.wp.Product>> call = service.getWP3Products();
        // String[] sortByArray = {"Most Recent", "Most Order", "Most Shares", "Most Viewed"};
        call.enqueue(new Callback<ArrayList<com.mohaa.dokan.models.wp.Product>>() {
            @Override
            public void onResponse(Call<ArrayList<com.mohaa.dokan.models.wp.Product>> call, Response<ArrayList<com.mohaa.dokan.models.wp.Product>> response) {
                // products_listAdapter = new Products_ListAdapter(response.body().getProducts(), ProductsTraderActivity.this);
                // recList.setAdapter(products_listAdapter);

                // Toast.makeText(ProductsActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();


                // Log.d("onFailure:",  "ProductName:"+response.body().getProducts().get(1).getTitle());

                if(response.body() != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            products_list.addAll(response.body());
                            products_listAdapter.notifyDataSetChanged();
                            shimmerFrameLayout.setVisibility(View.GONE);
                            shimmerFrameLayout.stopShimmer();
                        }
                    }).run();
                }






            }

            @Override
            public void onFailure(Call<ArrayList<com.mohaa.dokan.models.wp.Product>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());


            }
        });

 */
    }
    public void getAllCourses(String type , int cat_gparent_id, int pagenumbr , String sortById)
    {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final String data = prefs.getString("language", "ar"); //no id: default value
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface service = retrofit.create(ApiInterface.class);
        Call<Courses> call = service.getCoursesMain(cat_gparent_id , pagenumbr , 1 , 1 );
        switch (type) {
            case "Main":
            case "Group":
            case "Sub":


                switch (selectedKey) {
                    case 0:

                        //service = RetrofitApi.retrofitReadWP3ProductsByCategory("products" , String.valueOf(category.getId()), "date" , String.valueOf(pagenumbr)).create(ProductsAPIService.class);
                        call = service.getCoursesMain(cat_gparent_id , pagenumbr , 0 ,1 );
                        break;

                    case 1:
                        call = service.getCoursesMain(cat_gparent_id , pagenumbr , 1 , 1 );
                        //service = RetrofitApi.retrofitReadWP3ProductsByCategory("products" , String.valueOf(category.getId()), "menu_order", String.valueOf(pagenumbr)).create(ProductsAPIService.class);
                        break;

                    case 2:
                        call = service.getCoursesMain(cat_gparent_id , pagenumbr , 2 , 1 );
                        //service = RetrofitApi.retrofitReadWP3ProductsByCategory("products" , String.valueOf(category.getId()), "rating", String.valueOf(pagenumbr)).create(ProductsAPIService.class);
                        break;
                    case 3:
                        call = service.getCoursesMain(cat_gparent_id , pagenumbr , 3 , 1 );
                        //service = RetrofitApi.retrofitReadWP3ProductsByCategory("products" , String.valueOf(category.getId()), "popularity", String.valueOf(pagenumbr)).create(ProductsAPIService.class);
                        break;


                    default:
                    {
                        call = service.getCoursesMain(cat_gparent_id , pagenumbr , 0 , 1 );
                        //service = RetrofitApi.retrofitReadWP3ProductsByCategory("products" , String.valueOf(category.getId()), "date", String.valueOf(pagenumbr)).create(ProductsAPIService.class);
                        break;
                    }
                }
                break;
            case "like":

                //service = RetrofitApi.retrofitReadWP3ProductsByCategory("products" , String.valueOf(category_id), "popularity", String.valueOf(pagenumbr)).create(ProductsAPIService.class);
                break;
            case "recently":

                //call = service.getProductsGroup(SharedPrefManager.getInstance(getApplicationContext()).getLastViewed() ,  pagenumbr , 3 );
                break;

            case "sponsored":
                call = service.getCoursesBySort( ClassID ,  pagenumbr , 3 , 1 );
                Log.d(TAG, "getAllCourses: sponsored");
                break;


            case "suggest":
                call = service.getCoursesBySort(ClassID , pagenumbr , 2 , 1);
                Log.d(TAG, "getAllCourses: suggest");
                break;

            case "viewed":
                call = service.getCoursesBySort(ClassID , pagenumbr , 4 , 1 );
                Log.d(TAG, "getAllCourses: viewed");
                break;

            default:
            {
                call = service.getCoursesBySort( ClassID , pagenumbr , 3 , 1);
                Log.d(TAG, "getAllCourses: default");
                break;
            }
        }





        call.enqueue(new Callback<Courses>() {
            @Override
            public void onResponse(Call<Courses> call, final Response<Courses> response) {
                // products_listAdapter = new Products_ListAdapter(response.body().getProducts(), ProductsTraderActivity.this);
                // recList.setAdapter(products_listAdapter);


                products_list.addAll(response.body().getCourses());
                products_listAdapter.notifyDataSetChanged();
                shimmerFrameLayout.setVisibility(View.GONE);
                shimmerFrameLayout.stopShimmer();
                Log.d(TAG, "run:  loaded");



            }

            @Override
            public void onFailure(Call<Courses> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
                shimmerFrameLayout.setVisibility(View.GONE);
                shimmerFrameLayout.stopShimmer();

            }
        });



    }
    public void getAllCoursesAlike(String type , int pagenumbr , String sortById)
    {
        /*
        //Toast.makeText(this, "SSS", Toast.LENGTH_SHORT).show();

        ProductsAPIService service = RetrofitApi.retrofitReadWP3ProductsByCategory("products" , String.valueOf(category_id), "popularity", String.valueOf(pagenumbr)).create(ProductsAPIService.class);


        Call<ArrayList<com.mohaa.dokan.models.wp.Product>> call = service.getWP3Products();
        // String[] sortByArray = {"Most Recent", "Most Order", "Most Shares", "Most Viewed"};
        call.enqueue(new Callback<ArrayList<com.mohaa.dokan.models.wp.Product>>() {
            @Override
            public void onResponse(Call<ArrayList<com.mohaa.dokan.models.wp.Product>> call, Response<ArrayList<com.mohaa.dokan.models.wp.Product>> response) {
                // products_listAdapter = new Products_ListAdapter(response.body().getProducts(), ProductsTraderActivity.this);
                // recList.setAdapter(products_listAdapter);

                // Toast.makeText(ProductsActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();


                // Log.d("onFailure:",  "ProductName:"+response.body().getProducts().get(1).getTitle());

                if(response.body() != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            products_list.addAll(response.body());
                            products_listAdapter.notifyDataSetChanged();
                            shimmerFrameLayout.setVisibility(View.GONE);
                            shimmerFrameLayout.stopShimmer();
                        }
                    }).run();
                }






            }

            @Override
            public void onFailure(Call<ArrayList<com.mohaa.dokan.models.wp.Product>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());


            }
        });

         */
    }
    public void getAllCourses_Filter(String name ,String value)
    {

    }

    public void getAllInstructors(String type , int cat_gparent_id, int pagenumbr , String sortById)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final String data = prefs.getString("language", "ar"); //no id: default value
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface service = retrofit.create(ApiInterface.class);
        Call<Instructors> call = service.getInstructorsMain(cat_gparent_id , pagenumbr , 1 , 1 );
        switch (type) {
            case "Main":
            case "Group":
            case "Sub":


                switch (selectedKey) {
                    case 0:

                        //service = RetrofitApi.retrofitReadWP3ProductsByCategory("products" , String.valueOf(category.getId()), "date" , String.valueOf(pagenumbr)).create(ProductsAPIService.class);
                        call = service.getInstructorsMain(cat_gparent_id , pagenumbr , 0 , 1 );
                        break;

                    case 1:
                        call = service.getInstructorsMain(cat_gparent_id , pagenumbr , 1 , 1 );
                        //service = RetrofitApi.retrofitReadWP3ProductsByCategory("products" , String.valueOf(category.getId()), "menu_order", String.valueOf(pagenumbr)).create(ProductsAPIService.class);
                        break;

                    case 2:
                        call = service.getInstructorsMain(cat_gparent_id , pagenumbr , 2 , 1 );
                        //service = RetrofitApi.retrofitReadWP3ProductsByCategory("products" , String.valueOf(category.getId()), "rating", String.valueOf(pagenumbr)).create(ProductsAPIService.class);
                        break;
                    case 3:
                        call = service.getInstructorsMain(cat_gparent_id , pagenumbr , 3 , 1 );
                        //service = RetrofitApi.retrofitReadWP3ProductsByCategory("products" , String.valueOf(category.getId()), "popularity", String.valueOf(pagenumbr)).create(ProductsAPIService.class);
                        break;


                    default:
                    {
                        call = service.getInstructorsMain(cat_gparent_id , pagenumbr , 0 ,1 );
                        //service = RetrofitApi.retrofitReadWP3ProductsByCategory("products" , String.valueOf(category.getId()), "date", String.valueOf(pagenumbr)).create(ProductsAPIService.class);
                        break;
                    }
                }
                break;
            case "like":

                //service = RetrofitApi.retrofitReadWP3ProductsByCategory("products" , String.valueOf(category_id), "popularity", String.valueOf(pagenumbr)).create(ProductsAPIService.class);
                break;
            case "recently":

                //call = service.getProductsGroup(SharedPrefManager.getInstance(getApplicationContext()).getLastViewed() ,  pagenumbr , 3 );
                break;

            case "sponsored":
                call = service.getInstructorsBySort(LevelID , pagenumbr , 3 , 1 );
                break;


            case "suggest":
                call = service.getInstructorsBySort(LevelID , pagenumbr , 2 , 1 );
                break;

            case "viewed":
                call = service.getInstructorsBySort(LevelID , pagenumbr , 4 , 1 );
                break;

            default:
            {
                call = service.getInstructorsBySort(LevelID , pagenumbr , 3 , 1 );
                break;
            }
        }





        call.enqueue(new Callback<Instructors>() {
            @Override
            public void onResponse(Call<Instructors> call, final Response<Instructors> response) {
                // products_listAdapter = new Products_ListAdapter(response.body().getProducts(), ProductsTraderActivity.this);
                // recList.setAdapter(products_listAdapter);


                instructorList.addAll(response.body().getInstructors());
                instructorAdapter.notifyDataSetChanged();
                shimmerFrameLayout.setVisibility(View.GONE);
                shimmerFrameLayout.stopShimmer();



            }

            @Override
            public void onFailure(Call<Instructors> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });



    }
    public void getAllInstructorsAlike(String type , int pagenumbr , String sortById)
    {

    }
    public void getAllPostsHome(String type , int pagenumbr , String sortById)
    {
/*
        ProductsAPIService service = RetrofitApi.retrofitReadWP3ProductsDefault("products" , "rating" , "1").create(ProductsAPIService.class);
        // String[] sortByArray = {"Most Recent", "Most Order", "Most Shares", "Most Viewed"};
        switch (type) {
            case "recently":

               // Toast.makeText(this, "TTT", Toast.LENGTH_SHORT).show();
                service = RetrofitApi.retrofitReadWP3ProductsDefault("products" , "date" , String.valueOf(pagenumbr)).create(ProductsAPIService.class);
                //service = RetrofitApi.retrofitReadWP3ProductsByCategory("products" , String.valueOf(category.getId()), "date", String.valueOf(pagenumbr)).create(ProductsAPIService.class);
                break;

            case "sponsored":
               // Toast.makeText(this, "TTT", Toast.LENGTH_SHORT).show();
                service = RetrofitApi.retrofitReadWP3ProductsDefault("products" , "title" , String.valueOf(pagenumbr)).create(ProductsAPIService.class);
               // service = RetrofitApi.retrofitReadWP3ProductsByCategory("products" , String.valueOf(category.getId()), "title", String.valueOf(pagenumbr)).create(ProductsAPIService.class);
                break;


            case "suggest":
              //  Toast.makeText(this, "TTT", Toast.LENGTH_SHORT).show();
                service = RetrofitApi.retrofitReadWP3ProductsDefault("products" , "rating" , String.valueOf(pagenumbr)).create(ProductsAPIService.class);
                //service = RetrofitApi.retrofitReadWP3ProductsByCategory("products" , String.valueOf(category.getId()), "rating", String.valueOf(pagenumbr)).create(ProductsAPIService.class);
                break;

            case "viewed":
              //  Toast.makeText(this, "TTT", Toast.LENGTH_SHORT).show();
                service = RetrofitApi.retrofitReadWP3ProductsDefault("products" , "popularity" , String.valueOf(pagenumbr)).create(ProductsAPIService.class);
                //service = RetrofitApi.retrofitReadWP3ProductsByCategory("products" , String.valueOf(category.getId()), "popularity", String.valueOf(pagenumbr)).create(ProductsAPIService.class);
                break;

            default:
            {
                //Toast.makeText(this, "DDD", Toast.LENGTH_SHORT).show();
                service = RetrofitApi.retrofitReadWP3ProductsDefault("products" , "rating" , String.valueOf(pagenumbr)).create(ProductsAPIService.class);
                break;
            }
        }


        Call<ArrayList<com.mohaa.dokan.models.wp.Product>> call = service.getWP3Products();
        // String[] sortByArray = {"Most Recent", "Most Order", "Most Shares", "Most Viewed"};
        call.enqueue(new Callback<ArrayList<com.mohaa.dokan.models.wp.Product>>() {
            @Override
            public void onResponse(Call<ArrayList<com.mohaa.dokan.models.wp.Product>> call, Response<ArrayList<com.mohaa.dokan.models.wp.Product>> response) {
                // products_listAdapter = new Products_ListAdapter(response.body().getProducts(), ProductsTraderActivity.this);
                // recList.setAdapter(products_listAdapter);

                // Toast.makeText(ProductsActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();


                // Log.d("onFailure:",  "ProductName:"+response.body().getProducts().get(1).getTitle());

                if(response.body() != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            products_list.addAll(response.body());
                            products_listAdapter.notifyDataSetChanged();
                            shimmerFrameLayout.setVisibility(View.GONE);
                            shimmerFrameLayout.stopShimmer();
                        }
                    }).run();
                }






            }

            @Override
            public void onFailure(Call<ArrayList<com.mohaa.dokan.models.wp.Product>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());


            }
        });

 */
    }
    public void getData(String type,  int cat_gparent_id , int userPage   , String _sortById){
        try {
            switch (typeSwitch)
            {

                case "Course": {
                    if (category == null && type.equals("like")) {

                        getAllCoursesAlike(type, userPage, _sortById);
                    }
                    /*
                    if(category == null && !type.equals("like"))
                    {
                       // Toast.makeText(this, "" + type, Toast.LENGTH_SHORT).show();
                        getAllPostsHome(type, userPage, _sortById);

                    }
                    else {
                       // Toast.makeText(this, "ZZZ", Toast.LENGTH_SHORT).show();
                        getAllPosts(type, cat_gparent_id ,  userPage , _sortById);
                    }

                     */
                    getAllCourses(type, cat_gparent_id, userPage, _sortById);
                    break;
                }
                case "Instructor":
                {
                    if (category == null && type.equals("like")) {

                        getAllInstructorsAlike(type, userPage, _sortById);
                    }
                    /*
                    if(category == null && !type.equals("like"))
                    {
                       // Toast.makeText(this, "" + type, Toast.LENGTH_SHORT).show();
                        getAllPostsHome(type, userPage, _sortById);

                    }
                    else {
                       // Toast.makeText(this, "ZZZ", Toast.LENGTH_SHORT).show();
                        getAllPosts(type, cat_gparent_id ,  userPage , _sortById);
                    }

                     */
                    getAllInstructors(type, cat_gparent_id, userPage, _sortById);
                    break;
                 }
                case "Center": {
                   //TODO
                    break;
                }

                default:
                {
                    getAllCourses(type, cat_gparent_id, userPage, _sortById);//TODO
                    break;
                }
            }
            //swipeRefreshLayout.setRefreshing(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }













    // Set Sort Listener
    private void setSortListener() {
        sort.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void onClick(View view) {
                // Create Dialog
                final Dialog dialog = new Dialog(CategoriesActivity.this);
                dialog.setContentView(R.layout.sort_listview);

                ListView listView = dialog.findViewById(R.id.sort_listview);
                listView.setAdapter(new SortItemListAdapter(CategoriesActivity.this, sortBy, sortById));
                listView.setDividerHeight(1);
                listView.setFocusable(true);
                listView.setClickable(true);
                listView.setFocusableInTouchMode(false);
                dialog.show();

                // ListView Click Listener
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        sortById = i;
                        selectedKey = sortById;
                        sortByText.setText(sortBy.get(sortById));

                        // Reload Products List
                        userPage = 1;
                        products_list.clear();
                        products_listAdapter.notifyDataSetChanged();
                        if(category == null)
                        {
                            // 1. First, clear the array of data
                            products_list.clear();
                            // 2. Notify the adapter of the update
                            products_listAdapter.notifyDataSetChanged(); // or notifyItemRangeRemoved
                            shimmerFrameLayout.setVisibility(View.VISIBLE);
                            shimmerFrameLayout.startShimmer();
                            getData( type  , 0 ,  1, sortBy.get(selectedKey));
                        }
                        else {
                            // 1. First, clear the array of data
                            products_list.clear();
                            // 2. Notify the adapter of the update
                            products_listAdapter.notifyDataSetChanged(); // or notifyItemRangeRemoved
                            shimmerFrameLayout.setVisibility(View.VISIBLE);
                            shimmerFrameLayout.startShimmer();
                            getData( type   ,category.getId() ,  1, sortByText.getText().toString());
                        }

                        dialog.dismiss();
                    }
                });
            }
        });
    }
    private void setFilterListener() {
        filter.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void onClick(View view) {
                // Create Dialog

                final Dialog dialog = new Dialog(CategoriesActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.filterlayout);

                final List<String> department = getVarients(1);
                final List<String> company = getVarients(2);
                final List<String> pack = getVarients(3);
                final List<String> status= getVarients(4);

                // Add into hash map
                HashMap<String, List<String>> listHashMap = new HashMap<>();
                listHashMap.put("department", department);
                listHashMap.put("company", company);
                listHashMap.put("pack", pack);
                listHashMap.put("status", status);

                // Add Headers
                List<String> headers = new ArrayList<>();
                headers.add("department");
                headers.add("company");
                headers.add("pack");
                headers.add("status");

                final ExpandableListView listView = dialog.findViewById(R.id.expandableList);
                final FilterItemListAdapter filterItemListAdapter = new FilterItemListAdapter(CategoriesActivity.this, headers, listHashMap, departmentFilter, companyFilter, packFilter, statusFilter);
                listView.setAdapter(filterItemListAdapter);
                listView.setDividerHeight(1);
                listView.setFocusable(true);
                listView.setClickable(true);
                listView.setFocusableInTouchMode(false);
                dialog.show();

                // ListView Click Listener
                listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                        switch (groupPosition) {
                            case 0: // department
                                if (!departmentFilter.contains(department.get(childPosition))) {
                                    companyFilter.clear();
                                    packFilter.clear();
                                    statusFilter.clear();
                                    departmentFilter.clear();
                                    departmentFilter.add(department.get(childPosition));
                                } else {
                                    departmentFilter.remove(department.get(childPosition));
                                }
                                break;

                            case 1: // occasion
                                if (!companyFilter.contains(company.get(childPosition))) {
                                    companyFilter.clear();
                                    packFilter.clear();
                                    statusFilter.clear();
                                    departmentFilter.clear();
                                    companyFilter.add(company.get(childPosition));
                                } else {
                                    companyFilter.remove(company.get(childPosition));
                                }
                                break;
                            case 2: // material
                                if (!packFilter.contains(pack.get(childPosition))) {
                                    companyFilter.clear();
                                    packFilter.clear();
                                    statusFilter.clear();
                                    departmentFilter.clear();
                                    packFilter.add(pack.get(childPosition));
                                } else {
                                    packFilter.remove(pack.get(childPosition));
                                }
                                break;
                            case 3: // occasion
                                if (!statusFilter.contains(status.get(childPosition))) {
                                    companyFilter.clear();
                                    packFilter.clear();
                                    statusFilter.clear();
                                    departmentFilter.clear();
                                    statusFilter.add(status.get(childPosition));
                                } else {
                                    statusFilter.remove(status.get(childPosition));
                                }
                                break;
                        }
                        filterItemListAdapter.notifyDataSetChanged();
                        return false;
                    }
                });

                // Filter Apply Button Click
                Button apply = dialog.findViewById(R.id.apply);
                apply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (companyFilter.size() > 0) {
                            getAllCourses_Filter("company", companyFilter.get(0));
                        } else if (departmentFilter.size() > 0) {
                            getAllCourses_Filter("department", departmentFilter.get(0));
                        } else if (packFilter.size() > 0) {
                            getAllCourses_Filter("pack", packFilter.get(0));
                        } else if (statusFilter.size() > 0) {
                            getAllCourses_Filter("status", statusFilter.get(0));
                        } else {
                            if(category == null)
                            {
                                getData( type  , 0 ,  1, sortBy.get(selectedKey));
                            }
                            else {
                                getData( type , category.getId()   ,  1, sortByText.getText().toString());
                            }
                        }
                        // Reload Products List By Filter
                        // fillGridView();
                        dialog.dismiss();
                    }
                });

                // Clear All Button Click
                Button clear = dialog.findViewById(R.id.clear);
                clear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        try {
                            departmentFilter.clear();
                        } catch (NullPointerException ignore) {

                        }

                        try {
                            companyFilter.clear();
                        } catch (NullPointerException ignore) {

                        }
                        try {
                            packFilter.clear();
                        } catch (NullPointerException ignore) {

                        }
                        try {
                            statusFilter.clear();
                        } catch (NullPointerException ignore) {

                        }
                        filterItemListAdapter.notifyDataSetChanged();
                    }
                });

                // Close Button
                final ImageView close = dialog.findViewById(R.id.close);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

            }
        });
    }



    private List<String> getVarients(int type) {
        final List<String> vairent = new ArrayList<>();
        /*

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface service = retrofit.create(ApiInterface.class);


        Call<Variants> call = service.getVariants(type);

        call.enqueue(new Callback<Variants>() {
            @Override
            public void onResponse(Call<Variants> call, Response<Variants> response) {

                for (int i = 0; i < response.body().getVariants().size(); i++) {
                    vairent.add(response.body().getVariants().get(i).getName());

                   // if(response.body().getVariants().get(i).getType() == type)
                   // {

                     //   vairent.add(response.body().getVariants().get(i).getName());

                   // }



                }


            }

            @Override
            public void onFailure(Call<Variants> call, Throwable t) {
                Toast.makeText(CategoriesActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });




         */
        return vairent;

    }


    public void getSubCategory(int subCategory)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final String data = prefs.getString("language", "ar"); //no id: default value
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface service = retrofit.create(ApiInterface.class);


        Call<Categories> call = service.getMainCategories(subCategory , 1);

        call.enqueue(new Callback<Categories>() {
            @Override
            public void onResponse(Call<Categories> call, Response<Categories> response) {
                categoeries_list.addAll(response.body().getCategories());
                categoriesAdapte.notifyDataSetChanged();
                ShimmerLayoutcategory.setVisibility(View.GONE);
                ShimmerLayoutcategory.stopShimmer();
                if (categoeries_list.size() > 0) {
                    categories_recycleView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Categories> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onPause() {

        super.onPause();
    }



    @Override
    public void onCategoryClicked(Category contact, int position) {
        Intent intent = new Intent(CategoriesActivity.this, CategoriesActivity.class);
        switch (type) {
            case "Main":


                intent.putExtra(ItemsUI.BUNDLE_CATEGORIES_LIST, contact);
                intent.putExtra(ItemsUI.BUNDLE_CATEGORIES_LIST_TYPE, "Sub");
                intent.putExtra(ItemsUI.BUNDLE_CATEGORIES_SWITCH, typeSwitch);
                startActivity(intent);
                break;

            case "Sub":


                intent.putExtra(ItemsUI.BUNDLE_CATEGORIES_LIST, contact);
                intent.putExtra(ItemsUI.BUNDLE_CATEGORIES_LIST_TYPE, "Group");
                intent.putExtra(ItemsUI.BUNDLE_CATEGORIES_SWITCH, typeSwitch);
                startActivity(intent);
                break;


            default:
            {

                intent.putExtra(ItemsUI.BUNDLE_CATEGORIES_LIST, contact);
                intent.putExtra(ItemsUI.BUNDLE_CATEGORIES_LIST_TYPE, "Main");
                intent.putExtra(ItemsUI.BUNDLE_CATEGORIES_SWITCH, typeSwitch);
                startActivity(intent);
                break;
            }
        }
    }




}
