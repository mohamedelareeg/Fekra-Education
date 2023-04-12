package com.rovaind.academy.Controllers;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rovaind.academy.R;
import com.rovaind.academy.Utils.Toasty.Toasty;
import com.rovaind.academy.Utils.networksync.CheckInternetConnection;
import com.rovaind.academy.Utils.ratingbar.MaterialRatingBar;
import com.rovaind.academy.Utils.views.EditTextRegular;
import com.rovaind.academy.Utils.views.TextViewAr;
import com.rovaind.academy.interfaces.CourseResultReceiver;
import com.rovaind.academy.interfaces.OnCallbackReceived;
import com.rovaind.academy.manager.Codes;
import com.rovaind.academy.manager.SharedPrefManager;
import com.rovaind.academy.manager.response.CodeResponse;
import com.rovaind.academy.manager.response.CourseResponse;
import com.rovaind.academy.model.Code;
import com.rovaind.academy.model.Course;
import com.rovaind.academy.retrofit.ApiInterface;
import com.rovaind.academy.retrofit.RetrofitClient;


import java.net.NetworkInterface;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;


public class ShopBottomSheetFragment extends BottomSheetDialogFragment {

    public interface onCodeEventListener {
        public void ShopEvent(Code code);
    }
    onCodeEventListener someEventListener;
    public CourseResultReceiver resultreceiver;
    OnCallbackReceived mCallback;
    private Boolean promo_codeState = false;
    private Button btnCheckout;
    private EditTextRegular promo_name;
    ImageView ivImage;
    TextViewAr tvName , tvauthor;
    MaterialRatingBar ratingBar;
    private TextViewAr promo_apply ;
    private LinearLayout vailed_code;
    private Course course;
    //ProgressDialog
    private ProgressDialog mLoginProgress;
    private long milionsecPerMonth = 3600672L;
    private long codehours = 1;
    public ShopBottomSheetFragment() {
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
        View view = inflater.inflate(R.layout.fragment_shop_bottom_sheet, container, false);
        mLoginProgress = new ProgressDialog(getContext());
        course = resultreceiver.getResult();
        btnCheckout = view.findViewById(R.id.btn_checkout);
        promo_name = view.findViewById(R.id.promo_name);
        promo_apply = view.findViewById(R.id.promo_apply);
        vailed_code = view.findViewById(R.id.vailed_code);
        tvName = view.findViewById(R.id.tvName);
        tvauthor = view.findViewById(R.id.tvauthor);
        ratingBar = view.findViewById(R.id.ratingBar);
        ivImage = view.findViewById(R.id.ivImage);

        tvName.setText(course.getCourseName());
        tvauthor.setText(course.getOwnerName());
        Glide.with(getContext())
                .load(RetrofitClient.BASE_TeacherImage_URL + course.getOwnerImage()) // image url
                .apply(new RequestOptions()
                        .override(512, 512) // resizing
                        .centerCrop())
                .into(ivImage);  // imageview object
        init();

        promo_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!promo_codeState)
                {
                    getCoupon(promo_name.getText().toString());
                }
                else
                {

                    vailed_code.setVisibility(View.GONE);
                    promo_codeState = false;
                    btnCheckout.setEnabled(false);
                    promo_apply.setText(getResources().getString(R.string.apply));
                }
            }
        });
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCheckoutClick();
            }
        });



        return view;
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



    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(Integer.toHexString(b & 0xFF) + ":");
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }
    void onCheckoutClick() {

        if(promo_codeState)
        {
            showProgressDialog();
            Log.d(TAG, "code");//Log Details
            WifiManager wifiManager = (WifiManager) getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo wInfo = wifiManager.getConnectionInfo();
            String macAddress = wInfo.getMacAddress();

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(RetrofitClient.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            ApiInterface service = retrofit.create(ApiInterface.class);

            final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            long currentDate = timestamp.getTime();
            long endDate = currentDate + (milionsecPerMonth * codehours);
            Call<CodeResponse> call = service.updateCode(getMacAddr().toString() , promo_name.getText().toString() , currentDate , endDate , SharedPrefManager.getInstance(getContext()).getUser().getId(), course.getId());

            call.enqueue(new Callback<CodeResponse>() {
                @Override
                public void onResponse(Call<CodeResponse> call, Response<CodeResponse> response) {


                    if(response.body() != null)
                    {
                        Log.d(TAG, "onResponse: " + getMacAddr().toString());
                        Log.d(TAG, "onResponse:  " + response.body().getMessage() );
                        if (response.body().getMessage().equals("Updated successfully")) {
                            Log.d(TAG, "onResponse: " +  response.body().getCode().getCourseId());
                            incEnrolledCount();
                            SharedPrefManager.getInstance(getContext()).CoursesCodes(response.body().getCode());
                            someEventListener.ShopEvent(response.body().getCode());
                            hideProgressDialog();
                            dismiss();





                        } else {
                            hideProgressDialog();
                           // Log.d(TAG, "onResponse: "+ response.body().getMessage());
                            Toasty.error(getContext(), getResources().getString(R.string.not_used), Toast.LENGTH_SHORT, true).show();
                            //Toast.makeText(getContext(), getResources().getString(R.string.not_used), Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        hideProgressDialog();
                        Log.d(TAG, "onResponse: " + response.message()) ;
                        Log.d(TAG, "onResponse: " + response.code()) ;
                        Log.d(TAG, "onResponse: " + response.body()) ;
                        Log.d(TAG, "onResponse: " + response.errorBody().toString()) ;
                    }

                }

                @Override
                public void onFailure(Call<CodeResponse> call, Throwable t) {
                    hideProgressDialog();
                    Log.d(TAG, "onFailure: " + t.getLocalizedMessage());

                }
            });
        }

    }


    private void getCoupon(String Code) {
        Log.d(TAG, "code");//Log Details
        WifiManager wifiManager = (WifiManager) getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifiManager.getConnectionInfo();
        String macAddress = wInfo.getMacAddress();
        Log.d(TAG, "getCoupon: " + getMacAddr());
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiInterface service = retrofit.create(ApiInterface.class);


        Call<CodeResponse> call = service.codeVerfication( getMacAddr() , Code, course.getId() , 0);

        call.enqueue(new Callback<CodeResponse>() {
            @Override
            public void onResponse(Call<CodeResponse> call, Response<CodeResponse> response) {


                if(response.body() != null)
                {

                    if (response.body().getMessage().equals("Updated successfully")) {
                        vailed_code.setVisibility(View.VISIBLE);
                        Call<CodeResponse> call_t = service.getCode(Code);
                        call_t.enqueue(new Callback<CodeResponse>() {
                            @Override
                            public void onResponse(Call<CodeResponse> call, Response<CodeResponse> response) {
                                if(response.body() != null)
                                {
                                    codehours = response.body().getCode().getValidTime();
                                    Log.d(TAG, "onResponse: " + response.body()) ;
                                }
                            }

                            @Override
                            public void onFailure(Call<CodeResponse> call, Throwable t) {
                                Log.d(TAG, "onResponse: " + t.getLocalizedMessage()) ;

                            }
                        });

//                        milionsecPerMonth = response.body().getCode().getValidTime();
                        promo_apply.setText(getResources().getString(R.string.clear_code));
                        promo_codeState = true;

                        btnCheckout.setEnabled(true);


                    } else {
                        Toasty.error(getContext(), getResources().getString(R.string.invild_code), Toast.LENGTH_SHORT, true).show();
                       // Toast.makeText(getContext(),  getResources().getString(R.string.invild_code), Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Log.d(TAG, "onResponse: " + response.message()) ;
                    Log.d(TAG, "onResponse: " + response.code()) ;
                    Log.d(TAG, "onResponse: " + response.body()) ;
                    Log.d(TAG, "onResponse: " + response.errorBody().toString()) ;
                }

            }

            @Override
            public void onFailure(Call<CodeResponse> call, Throwable t) {

                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());

            }
        });





    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        try {
            someEventListener = (onCodeEventListener) activity;
        } catch (ClassCastException e) {

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        resultreceiver = (CourseResultReceiver) context;


        try {
            mCallback = (OnCallbackReceived) context;
            someEventListener = (onCodeEventListener) context;
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

    private void incEnrolledCount() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface service = retrofit.create(ApiInterface.class);


        Call<CourseResponse> call = service.updateCourseenrolled(
                course.getId(),
                course.getOwnerId()

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
}
