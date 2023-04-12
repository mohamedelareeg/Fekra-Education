package com.rovaind.academy.Controllers;


import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.rovaind.academy.R;
import com.rovaind.academy.Utils.Toasty.Toasty;
import com.rovaind.academy.Utils.networksync.CheckInternetConnection;
import com.rovaind.academy.Utils.views.AutoFitEditText;
import com.rovaind.academy.Utils.views.TextViewAr;
import com.rovaind.academy.adapter.BackgroundAdapter;

import com.rovaind.academy.interfaces.OnCallbackReceived;
import com.rovaind.academy.interfaces.OnSelectionListener;
import com.rovaind.academy.manager.PostBackgrounds;
import com.rovaind.academy.manager.SharedPrefManager;
import com.rovaind.academy.manager.response.ImageResponse;
import com.rovaind.academy.manager.response.PostImageResponse;
import com.rovaind.academy.manager.response.PostResponse;
import com.rovaind.academy.model.Post;
import com.rovaind.academy.model.Postbackground;
import com.rovaind.academy.retrofit.ApiInterface;
import com.rovaind.academy.retrofit.RetrofitClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;


public class PostHomeBottomSheetFragment extends BottomSheetDialogFragment implements OnSelectionListener {

    public interface onSomeEventListener {
        public void PostEvent(Post post);
    }

    onSomeEventListener someEventListener;
    int textPhoto = 0;
    int postType = 1;
    String selectedIMG , selectedCOLOR;

    OnCallbackReceived mCallback;

    private int ClassID;
    private EditText mBodyField;
    private AutoFitEditText mBodyFieldPhoto;
    //
    private RecyclerView instantRecyclerView;
    private BackgroundAdapter initaliseadapter;
    ArrayList<Postbackground> INSTANTLIST;
    FloatingActionButton photoSelect , pdfSelect , mSubmitButton;

    private Bitmap product_logo;
    private ImageView setup_img;

    private final int GALLERY = 1;
    private final int REQUEST_CODE_PERMISSIONS  = 1;
    private final int REQUEST_CODE_READ_STORAGE = 2;
    public PostHomeBottomSheetFragment() {
        // Required empty public constructor
    }
    private ProgressDialog mLoginProgress;
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toasty.error(getActivity(), getResources().getString(R.string.permisson_denied), Toast.LENGTH_SHORT, true).show();
                    //Toast.makeText(getActivity(), getResources().getString(R.string.permisson_denied), Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
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

        onAttachToParentFragment(getParentFragment());
    }

    // In the child fragment.
    public void onAttachToParentFragment(Fragment fragment)
    {
        try
        {
            someEventListener = (onSomeEventListener) fragment;

        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(
                    fragment.toString() + " must implement OnPlayerSelectionSetListener");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_bottom_sheet, container, false);

        ClassID = SharedPrefManager.getInstance(getContext()).getSelectedClass();
        mLoginProgress = new ProgressDialog(getContext());
        TextViewAr appname = view.findViewById(R.id.appname);
        appname.setText(getResources().getString(R.string.ask_question));

        photoSelect = view.findViewById(R.id.photoSelect);
        setup_img = view.findViewById(R.id.product_logo);
        mBodyField = view.findViewById(R.id.field_body);
        mBodyFieldPhoto = view.findViewById(R.id.field_body_txt_photo);
        mSubmitButton = view.findViewById(R.id.fab_submit_post);
        //
        INSTANTLIST = new ArrayList<>();
        instantRecyclerView = view.findViewById(R.id.instantRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        instantRecyclerView.setLayoutManager(linearLayoutManager);
        initaliseadapter = new BackgroundAdapter(getContext() , INSTANTLIST , this);
        //initaliseadapter.addOnSelectionListener(onSelectionListener);
        instantRecyclerView.setAdapter(initaliseadapter);
        LoadBackgrounds();

        //


        photoSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    askForPermission(view);
                } else {
                    showChooser();
                }

            }
        });
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //submitPost();
                showProgressDialog();
                switch (postType) {
                    case 1: {
                        final String postTXT = mBodyField.getText().toString();//name
                        if (!TextUtils.isEmpty(postTXT)) {
                            UploadTweet(postTXT);
                        }
                        break;
                    }
                    case 2: {
                        //Toast.makeText(getActivity(), "before", Toast.LENGTH_SHORT).show();
                        String postTXT = mBodyField.getText().toString();//name
                        if (getProduct_logo()  != null) {
                            if(!TextUtils.isEmpty(postTXT))
                            {
                                UploadPhoto(getProduct_logo() , postTXT);

                            }
                            else
                            {
                                postTXT = "تم ارفاق صورة";
                                UploadPhoto(getProduct_logo() , postTXT);

                            }

                        }

                        break;
                    }
                    case 4: {
                        final String postTXT = mBodyFieldPhoto.getText().toString();//name
                        if (!TextUtils.isEmpty(postTXT)) {
                            UploadPhotoTXT(postTXT , selectedIMG , selectedCOLOR);
                        }
                        break;
                    }
                    default:{

                    }
                }
            }
        });
        return view;
    }
    private void UploadTweet(String postTXT) {
        int owner_id = SharedPrefManager.getInstance(getActivity()).getUser().getId();
        String owner_name = SharedPrefManager.getInstance(getActivity()).getUser().getName();
        final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        long CurrentTime = timestamp.getTime();

        ApiInterface api = RetrofitClient.retrofitWrite().create(ApiInterface.class);
        // public Post(Integer usrId, Integer courseId, Integer ownerId, String content, String url, String backImg, String txtColor, Integer postType, Long createdAt) {
        Post post = new Post(owner_id , 0 ,ClassID , 0 , postTXT , "none" ,"none" , "none" , postType ,CurrentTime );

        //defining the call
        Call<PostResponse> call = api.createPost(
                post.getUsrId(),
                post.getCourseId(),
                post.getClassId(),
                post.getOwnerId(),
                post.getContent(),
                post.getUrl(),
                post.getBackImg(),
                post.getTxtColor(),
                post.getPostType(),
                post.getCreatedAt()
        );

        call.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                //uploadImagesToServer(response.body().getProduct().getId());
                //hideProgressDialog();
                //Toast.makeText()
                Toasty.success(getActivity(), getResources().getString(R.string.posted_suc), Toast.LENGTH_SHORT, true).show();
               // Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                Log.i("retrofit", response.body().getMessage());//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                Post post = response.body().getPost();
                int post_id =  post.getId();

                if (someEventListener != null)
                {
                    someEventListener.PostEvent(post);
                }

                setProduct_logo(null);
                mBodyField.setText("");
                mBodyFieldPhoto.setText("");
                dismiss();
                hideProgressDialog();

            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                //hideProgressDialog();
                Toasty.error(getActivity(), t.getLocalizedMessage(), Toast.LENGTH_SHORT, true).show();
               // Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                Log.i("retrofit", t.getMessage());//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                hideProgressDialog();
            }
        });
    }

    private void UploadPhoto( Bitmap bitmap , String postTXT) {



        int owner_id = SharedPrefManager.getInstance(getActivity()).getUser().getId();
        String owner_name = SharedPrefManager.getInstance(getActivity()).getUser().getName();
        final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        long CurrentTime = timestamp.getTime();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        String imgname = String.valueOf(Calendar.getInstance().getTimeInMillis());

        ApiInterface api = RetrofitClient.retrofitWrite().create(ApiInterface.class);
        //Toast.makeText(getActivity(), "between", Toast.LENGTH_SHORT).show();
        Log.d("helloo",encodedImage +"   >>"+imgname);
        Log.d("imggggg","   >>"+imgname);
        Call<ImageResponse> call_ = api.setPostImage(imgname,encodedImage , 0);
        call_.enqueue(new Callback<ImageResponse>() {
            @Override
            public void onResponse(Call<ImageResponse> call_, Response<ImageResponse> response) {
                Log.i("Responsestring", response.body().toString());
                //Toast.makeText()
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("Upload", response.body().toString());
                       // Toast.makeText(getActivity(), "after", Toast.LENGTH_SHORT).show();
                        ApiInterface api = RetrofitClient.retrofitWrite().create(ApiInterface.class);
                        // public Post(Integer usrId, Integer courseId, Integer ownerId, String content, String url, String backImg, String txtColor, Integer postType, Long createdAt) {
                        Post post = new Post(owner_id , 0 , ClassID , 0 , postTXT , "none" ,"none" , "none" , postType ,CurrentTime );

                        //defining the call
                        Call<PostResponse> call = api.createPost(
                               post.getUsrId(),
                                post.getCourseId(),
                                post.getClassId(),
                                post.getOwnerId(),
                                post.getContent(),
                                post.getUrl(),
                                post.getBackImg(),
                                post.getTxtColor(),
                                post.getPostType(),
                                post.getCreatedAt()
                        );

                        call.enqueue(new Callback<PostResponse>() {
                            @Override
                            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                                //uploadImagesToServer(response.body().getProduct().getId());
                                //hideProgressDialog();
                                //Toast.makeText()

                                //Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                                Log.i("retrofit", response.body().getMessage());//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                                Post post = response.body().getPost();
                                int post_id =  post.getId();

                                Call<PostImageResponse> call_details = api.createpostImage(
                                        post_id,
                                        imgname + ".jpg",
                                        postTXT,
                                        CurrentTime

                                );
                                call_details.enqueue(new Callback<PostImageResponse>() {
                                    @Override
                                    public void onResponse(Call<PostImageResponse> call, Response<PostImageResponse> response) {
                                        Log.d(TAG, "onResponse: " + response.message());
                                        Log.d(TAG, "onResponse: " + response.errorBody());
                                        Log.d(TAG, "onResponse: " + response.body().getPostgallery().getThumbImage());
                                        Post.Gallery gallery = response.body().getPostgallery();
                                        List<Post.Gallery> galleryList = new ArrayList<>();
                                        galleryList.add(gallery);
                                        post.setGallery(galleryList);
                                      //  Toast.makeText(getActivity(), "" + post.getGallery().get(0).getThumbImage(), Toast.LENGTH_SHORT).show();
                                       // someEventListener = (PostHomeBottomSheetFragment.onSomeEventListener) getContext();
                                        if (someEventListener != null)
                                        {
                                            someEventListener.PostEvent(post);
                                        }
                                        setProduct_logo(null);
                                        mBodyField.setText("");
                                        mBodyFieldPhoto.setText("");
                                        dismiss();
                                        hideProgressDialog();
                                    }

                                    @Override
                                    public void onFailure(Call<PostImageResponse> call, Throwable t) {
                                        Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
                                        hideProgressDialog();
                                    }
                                });

                            }

                            @Override
                            public void onFailure(Call<PostResponse> call, Throwable t) {
                                //hideProgressDialog();
                                hideProgressDialog();
                                Toasty.error(getActivity(),t.getLocalizedMessage(), Toast.LENGTH_SHORT, true).show();
                                //Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                                Log.i("retrofit", t.getMessage());//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                            }
                        });
                        //Toast.makeText(AddTraderActivity.this, "Image Uploaded Successfully!!", Toast.LENGTH_SHORT).show();

                    } else {
                        hideProgressDialog();
                        Log.i("Upload", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ImageResponse> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
                hideProgressDialog();
            }
        });
    }
    private void UploadPhotoTXT(String postTXT , String IMG , String Color) {
        int owner_id = SharedPrefManager.getInstance(getActivity()).getUser().getId();
        String owner_name = SharedPrefManager.getInstance(getActivity()).getUser().getName();
        final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        long CurrentTime = timestamp.getTime();

        ApiInterface api = RetrofitClient.retrofitWrite().create(ApiInterface.class);
        // public Post(Integer usrId, Integer courseId, Integer ownerId, String content, String url, String backImg, String txtColor, Integer postType, Long createdAt) {
        Post post = new Post(owner_id , 0 , ClassID , 0 , postTXT , "none" ,IMG , Color , postType ,CurrentTime );

        //defining the call
        Call<PostResponse> call = api.createPost(
                post.getUsrId(),
                post.getCourseId(),
                post.getClassId(),
                post.getOwnerId(),
                post.getContent(),
                post.getUrl(),
                post.getBackImg(),
                post.getTxtColor(),
                post.getPostType(),
                post.getCreatedAt()
        );

        call.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                //uploadImagesToServer(response.body().getProduct().getId());
                //hideProgressDialog();
                //Toast.makeText()
                Toasty.success(getActivity(), getResources().getString(R.string.posted_suc), Toast.LENGTH_SHORT, true).show();
                //Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                Log.i("retrofit", response.body().getMessage());//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                Post post = response.body().getPost();
                int post_id =  post.getId();
               // someEventListener = (PostHomeBottomSheetFragment.onSomeEventListener) getContext();
                if (someEventListener != null)
                {
                    someEventListener.PostEvent(post);
                }
                setProduct_logo(null);
                mBodyField.setText("");
                mBodyFieldPhoto.setText("");
                dismiss();
                hideProgressDialog();



            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                //hideProgressDialog();
                Toasty.error(getActivity(), t.getLocalizedMessage(), Toast.LENGTH_SHORT, true).show();
               // Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                Log.i("retrofit", t.getMessage());//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                hideProgressDialog();
            }
        });
    }
    public ProgressDialog mProgressDialog;
    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage(getActivity().getResources().getString(R.string.loading));
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
    private void LoadBackgrounds() {

        ApiInterface apiInterface = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);

        Call<PostBackgrounds> call = apiInterface.getBackGrounds();//(data.equals("en") ? 0 :1)

        call.enqueue(new Callback<PostBackgrounds>() {
            @Override
            public void onResponse(Call<PostBackgrounds> call, Response<PostBackgrounds> response) {


                if(response.body() != null)
                {

                    INSTANTLIST.addAll(response.body().getPostbackgrounds());
                    initaliseadapter.notifyDataSetChanged();
                    setBottomSheetBehavior();
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
            public void onFailure(Call<PostBackgrounds> call, Throwable t) {

            }
        });
    }
    private void AddBackgrounds() {
        /*
        INSTANTLIST.add(new Img("http://cdn.playbuzz.com/cdn/62b7af36-65b7-41aa-8db2-e34fd8a76acf/62c5efd3-fa55-464b-8ee5-9a3e2543c830.jpg"));
        INSTANTLIST.add(new Img("http://cdn.playbuzz.com/cdn/62b7af36-65b7-41aa-8db2-e34fd8a76acf/62c5efd3-fa55-464b-8ee5-9a3e2543c830.jpg"));
        INSTANTLIST.add(new Img("http://cdn.playbuzz.com/cdn/fa415381-3e73-4678-915d-7abf8983ce09/813d91c3-f7c9-4a20-9e7b-7e7b6da78941.jpg"));
        INSTANTLIST.add(new Img("http://cdn.playbuzz.com/cdn/62b7af36-65b7-41aa-8db2-e34fd8a76acf/1e93e32c-7662-4de7-a441-59d4c29d6faf.jpg"));
        INSTANTLIST.add(new Img("http://cdn.playbuzz.com/cdn/5cb29908-40a5-42d4-831d-5bea595bcf05/3e9f0c63-60c6-4a0c-964c-1302d56295da.jpg"));
        INSTANTLIST.add(new Img("https://pmcfootwearnews.files.wordpress.com/2015/06/michael-jordan-chicago-bulls.jpg"));
        INSTANTLIST.add(new Img("http://healthyceleb.com/wp-content/uploads/2015/03/Michael-Jordan.jpg"));
        INSTANTLIST.add(new Img("http://thelegacyproject.co.za/wp-content/uploads/2015/04/Michael_Jordan_Net_Worth.jpg"));
        INSTANTLIST.add(new Img("http://www.guinnessworldrecords.com/Images/Michael-Jordan-main_tcm25-15662.jpg"));
        INSTANTLIST.add(new Img("http://sportsmockery.com/wp-content/uploads/2015/03/michael-jordan-1600x1200.jpg"));


        initaliseadapter.addImageList(INSTANTLIST);
        initaliseadapter.notifyDataSetChanged();
        setBottomSheetBehavior();

         */
    }
    private void setBottomSheetBehavior() {

    };



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

    /*
    @Override
    public void onAttachFragment(@NonNull Fragment childFragment) {
            super.onAttachFragment(childFragment);
        try {
            mCallback = (OnCallbackReceived) childFragment;
            someEventListener = (onSomeEventListener) childFragment;
        } catch (ClassCastException e) {

        }
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //resultreceiver = (CourseResultReceiver) context;


        try {
            mCallback = (OnCallbackReceived) context;
            someEventListener = (onSomeEventListener) context;
        } catch (ClassCastException e) {

        }
    }



     */


    @Override
    public void OnClick(Postbackground Img, int position) {

        if(getProduct_logo() == null) {
            if (textPhoto == Img.getId() ) {
                mBodyField.setText(mBodyFieldPhoto.getText());
                mBodyFieldPhoto.setVisibility(View.GONE);
                mBodyField.setVisibility(View.VISIBLE);

                mBodyField.setTextColor(Color.parseColor("#ff000000"));
                mBodyField.setBackgroundResource(0);
                textPhoto = 0;
                postType = 1;
            } else if (textPhoto != Img.getId() && textPhoto != 0) {
               // mBodyFieldPhoto.setText(mBodyField.getText());
              //  mBodyField.setVisibility(View.GONE);
                //mBodyFieldPhoto.setVisibility(View.VISIBLE);
                mBodyFieldPhoto.setTextColor(Color.parseColor(Img.getTxtColor()));


                Glide.with(getActivity())
                        .load(RetrofitClient.BASE_POST_IMAGE_URL + Img.getBackImg())

                        .into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource,
                                                        @Nullable Transition<? super Drawable> transition) {

                                mBodyFieldPhoto.setBackground(resource);

                            }
                        });
                selectedCOLOR = Img.getTxtColor();
                selectedIMG = Img.getBackImg();
                textPhoto = Img.getId();
                postType = 4;
            }
            else
            {
                mBodyFieldPhoto.setText(mBodyField.getText());
                mBodyField.setVisibility(View.GONE);
                mBodyFieldPhoto.setVisibility(View.VISIBLE);
                mBodyFieldPhoto.setTextColor(Color.parseColor(Img.getTxtColor()));


                Glide.with(getActivity())
                        .load(RetrofitClient.BASE_POST_IMAGE_URL + Img.getBackImg())

                        .into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource,
                                                        @Nullable Transition<? super Drawable> transition) {

                                mBodyFieldPhoto.setBackground(resource);

                            }
                        });
                selectedCOLOR = Img.getTxtColor();
                selectedIMG = Img.getBackImg();
                textPhoto = Img.getId();
                postType = 4;
            }

        }
        else
        {
            setProduct_logo(null);
            setup_img.setImageDrawable(null);
            setup_img.setVisibility(View.GONE);
            if (textPhoto == Img.getId() ) {
                mBodyField.setText(mBodyFieldPhoto.getText());
                mBodyFieldPhoto.setVisibility(View.GONE);
                mBodyField.setVisibility(View.VISIBLE);

                mBodyField.setTextColor(Color.parseColor("#ff000000"));
                mBodyField.setBackgroundResource(0);
                textPhoto = 0;
                postType = 1;
            } else if (textPhoto != Img.getId() && textPhoto != 0) {
                // mBodyFieldPhoto.setText(mBodyField.getText());
                //  mBodyField.setVisibility(View.GONE);
                //mBodyFieldPhoto.setVisibility(View.VISIBLE);
                mBodyFieldPhoto.setTextColor(Color.parseColor(Img.getTxtColor()));


                Glide.with(getActivity())
                        .load(RetrofitClient.BASE_POST_IMAGE_URL + Img.getBackImg())

                        .into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource,
                                                        @Nullable Transition<? super Drawable> transition) {

                                mBodyFieldPhoto.setBackground(resource);

                            }
                        });
                selectedCOLOR = Img.getTxtColor();
                selectedIMG = Img.getBackImg();
                textPhoto = Img.getId();
                postType = 4;
            }
            else
            {
                mBodyFieldPhoto.setText(mBodyField.getText());
                mBodyField.setVisibility(View.GONE);
                mBodyFieldPhoto.setVisibility(View.VISIBLE);
                mBodyFieldPhoto.setTextColor(Color.parseColor(Img.getTxtColor()));


                Glide.with(getActivity())
                        .load(RetrofitClient.BASE_POST_IMAGE_URL + Img.getBackImg())

                        .into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource,
                                                        @Nullable Transition<? super Drawable> transition) {

                                mBodyFieldPhoto.setBackground(resource);

                            }
                        });
                selectedCOLOR = Img.getTxtColor();
                selectedIMG = Img.getBackImg();
                textPhoto = Img.getId();
                postType = 4;
            }
        }
    }

    @Override
    public void OnLongClick(Postbackground img, int position) {

    }

    public Bitmap getProduct_logo() {
        return product_logo;
    }

    public void setProduct_logo(Bitmap product_logo) {
        this.product_logo = product_logo;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (resultCode == getActivity().RESULT_CANCELED) {
            return;
        }



        if (requestCode == GALLERY) {
            if (resultData != null) {
                Uri contentURI = resultData.getData();
                try {

                    if(textPhoto !=0) {
                        mBodyField.setText(mBodyFieldPhoto.getText());
                        mBodyFieldPhoto.setVisibility(View.GONE);
                        mBodyField.setVisibility(View.VISIBLE);

                        mBodyField.setTextColor(Color.parseColor("#ff000000"));
                        mBodyField.setBackgroundResource(0);

                    }
                    textPhoto = 0;
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
                    setup_img.setImageBitmap(bitmap);
                    setProduct_logo(bitmap);
                    setup_img.setVisibility(View.VISIBLE);
                    postType = 2;


                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.failed), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    /**
     *  Runtime Permission
     */
    private void askForPermission(View view) {
        if ((ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE) +
                ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE))
                != PackageManager.PERMISSION_GRANTED) {
            /* Ask for permission */
            // need to request permission
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                Snackbar.make(view.findViewById(android.R.id.content),
                        "Please grant permissions to write data in sdcard",
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        v -> ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                REQUEST_CODE_PERMISSIONS)).show();
            } else {
                /* Request for permission */
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_PERMISSIONS);
            }

        } else {
            showChooser();
        }
    }
    private void showChooser() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }


}
