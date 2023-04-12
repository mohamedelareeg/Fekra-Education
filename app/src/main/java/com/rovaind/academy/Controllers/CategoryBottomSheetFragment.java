package com.rovaind.academy.Controllers;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.rovaind.academy.R;
import com.rovaind.academy.Utils.ItemsUI;
import com.rovaind.academy.Utils.networksync.CheckInternetConnection;
import com.rovaind.academy.Utils.shimmer.ShimmerFrameLayout;
import com.rovaind.academy.adapter.SubCategoriesAdapter;
import com.rovaind.academy.interfaces.IntResultReceiver;
import com.rovaind.academy.interfaces.OnCallbackReceived;
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

import static android.content.ContentValues.TAG;
import static com.rovaind.academy.Utils.ThemeUtil.THEME_PURPLE;


public class CategoryBottomSheetFragment extends BottomSheetDialogFragment implements OnCataClickListener {

    public IntResultReceiver resultreceiver;
    OnCallbackReceived mCallback;

    private Button btnCheckout;


    private ArrayList<Category> categoeries_list;
    private SubCategoriesAdapter categoriesAdapte;
    private RecyclerView categories_recycleView;
    private RelativeLayout background;

    private ShimmerFrameLayout  ShimmerLayoutcategory;
    private Integer category_id , section_id , center_id;

    //ProgressDialog
    private ProgressDialog mLoginProgress;
    public CategoryBottomSheetFragment() {
        // Required empty public constructor
    }

    private  int colorID , sizeID;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Making bottom sheet expanding to full height by default
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(dialog1 -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog1;

            FrameLayout bottomSheet = d.findViewById(R.id.design_bottom_sheet);
            BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
        });
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_bottom_sheet, container, false);
        mLoginProgress = new ProgressDialog(getContext());
        category_id = resultreceiver.getCategoryResult();
        section_id = resultreceiver.getSectionResult();
        center_id = resultreceiver.getCenterResult();

        Log.d(TAG, "onCreateView: category " + category_id);
        Log.d(TAG, "onCreateView: section " + section_id);
        Log.d(TAG, "onCreateView: center " + center_id);
        btnCheckout = view.findViewById(R.id.btn_checkout);


        ShimmerLayoutcategory = view.findViewById(R.id.parentShimmerLayoutcategory);
        ShimmerLayoutcategory.startShimmer();

        //category_id = getActivity().getIntent().getIntExtra(ItemsUI.BUNDLE_CATEGORIES_ID , 0);
        init();

        //
        categoeries_list = new ArrayList<>();
        categoriesAdapte = new SubCategoriesAdapter(categoeries_list ,this);
        categories_recycleView = view.findViewById(R.id.slider_cata);

        LinearLayoutManager cataManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        categories_recycleView.setItemAnimator(new DefaultItemAnimator());
        categories_recycleView.setLayoutManager(cataManager);
        categories_recycleView.setAdapter(categoriesAdapte);
        getSubCategory(category_id);

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // onCheckoutClick();
                dismiss();
            }
        });



        return view;
    }

    public void getSubCategory(int subCategory)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void init() {


    }

    @Override
    public void onResume() {

        new CheckInternetConnection(getContext()).checkConnection();

        super.onResume();
    }





    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        resultreceiver = (IntResultReceiver) context;

        try {
            mCallback = (OnCallbackReceived) context;
        } catch (ClassCastException e) {

        }
    }
    public ProgressDialog mProgressDialog;
    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Loading....");
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onCategoryClicked(Category contact, int position) {
        Intent intent = new Intent(getContext(), SubCategoryActivity.class);
        intent.putExtra(ItemsUI.BUNDLE_CATEGORIES_LIST, (Serializable) contact);
        intent.putExtra(ItemsUI.BUNDLE_CATEGORIES_CENTER_ID, center_id);
        intent.putExtra(ItemsUI.BUNDLE_CATEGORIES_ID, category_id);
        intent.putExtra(ItemsUI.BUNDLE_CATEGORIES_SECTION, section_id);
        intent.putExtra(ItemsUI.BUNDLE_THEME, THEME_PURPLE);
        startActivity(intent);
    }
}
