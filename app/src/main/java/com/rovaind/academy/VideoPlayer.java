package com.rovaind.academy;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rovaind.academy.Controllers.BaseActivity;
import com.rovaind.academy.Utils.EndlessRecyclerViewScrollListener;
import com.rovaind.academy.Utils.ItemsUI;
import com.rovaind.academy.Utils.Toasty.Toasty;
import com.rovaind.academy.Utils.exoplayerview.extension.MultiQualitySelectorAdapter;
import com.rovaind.academy.Utils.exoplayerview.media.ExoMediaSource;
import com.rovaind.academy.Utils.exoplayerview.media.SimpleMediaSource;
import com.rovaind.academy.Utils.exoplayerview.ui.ExoVideoView;
import com.rovaind.academy.Utils.reviewratings.RatingReviews;
import com.rovaind.academy.adapter.CommentsRecyclerAdapter;
import com.rovaind.academy.auth.FacebookLoginActivity;
import com.rovaind.academy.manager.Comments;
import com.rovaind.academy.manager.SharedPrefManager;
import com.rovaind.academy.manager.response.CommentResponse;
import com.rovaind.academy.manager.response.CourseResponse;

import com.rovaind.academy.model.Comment;
import com.rovaind.academy.model.Course;
import com.rovaind.academy.model.Lecture;
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

import static com.rovaind.academy.Utils.exoplayerview.orientation.OnOrientationChangedListener.SENSOR_LANDSCAPE;
import static com.rovaind.academy.Utils.exoplayerview.orientation.OnOrientationChangedListener.SENSOR_PORTRAIT;

public class VideoPlayer extends BaseActivity {
    private static final String TAG = "VideoPlayer";
    private ExoVideoView videoView;
    private View wrapper;
    private final String[] modes = new String[]{"RESIZE_MODE_FIT", "RESIZE_MODE_FIXED_WIDTH"
            , "RESIZE_MODE_FIXED_HEIGHT", "RESIZE_MODE_FILL", "RESIZE_MODE_ZOOM"};
    private Spinner modeSpinner;
    private ArrayAdapter<String> adapter;
    Lecture lecture;
    Course course;

    //=========== Comments =============
    private RecyclerView commentsRecyclerView;
    private CommentsRecyclerAdapter commentsRecyclerAdapter;
    private List<Comment> commentsList;
    private EditText commentEditText;
    private Button sendButton;
    private int userPage = 1;
    private RatingReviews ratingReviews;
    private RatingBar ratingbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

         */
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_video_player);
        wrapper = findViewById(R.id.wrapper);
        Intent i=getIntent();
        lecture = (Lecture) getIntent().getExtras().getSerializable(ItemsUI.BUNDLE_COURSES_DETAILS);
        course = (Course) getIntent().getExtras().getSerializable(ItemsUI.BUNDLE_COURSES_DETAILS_LECTURE);
        init();
        initListeners();
        initVideoView();
        int owner_id = SharedPrefManager.getInstance(this).getUser().getId();




    }
    public boolean containsID(final List<Comment> list, final int name){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return list.stream().filter(o -> o.getUsrId().equals (name)).findFirst().isPresent();
        }
        else {
           // boolean found = false;
            for (Comment s : list) {
                if (s.getUsrId().equals(name)) {
                  return true;
                }
            }
            return false;
        }
    }
    private void init() {
        commentsList = new ArrayList<>();
        ratingbar=(RatingBar)findViewById(R.id.ratingBar);
        commentEditText = findViewById(R.id.commentEditText);
        sendButton = findViewById(R.id.sendButton);
        commentsRecyclerView = findViewById(R.id.commentsRecyclerView);



    }

    private void initVideoViewG() {

        /*
        Intent i=getIntent();

        String url=i.getStringExtra("url");
        String videourl = RetrofitClient.BASE_VIDEO_URL + url ; //+ "/preview"
        VideoView video = findViewById(R.id.video_player);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(video);
        video.setMediaController(mediaController);
        video.setVideoPath(videourl);
        video.setSecure(true);
        video.start();

         */
    }


    private void initVideoView() {
        videoView = findViewById(R.id.videoView);
        videoView.setPortrait(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
        videoView.setBackListener((view, isPortrait) -> {
            if (isPortrait) {
                finish();
            }
            return false;
        });

        videoView.setOrientationListener(orientation -> {
            if (orientation == SENSOR_PORTRAIT) {
                changeToPortrait();
            } else if (orientation == SENSOR_LANDSCAPE) {
                changeToLandscape();
            }
        });

//        videoView.setGestureEnabled(false);
//
//
//        SimpleMediaSource mediaSource = new SimpleMediaSource("http://flv2.bn.netease.com/videolib3/1604/28/fVobI0704/SD/fVobI0704-mobile.mp4");
//        mediaSource.setDisplayName("Apple HLS");

        String videourl = RetrofitClient.BASE_VIDEO_URL + lecture.getVideoUrlyou() ; //+ "/preview"
        SimpleMediaSource mediaSource = new SimpleMediaSource(videourl);
        mediaSource.setDisplayName(lecture.getLectureName());

        //demo only,not real multi quality, urls are the same actually

        List<ExoMediaSource.Quality> qualities = new ArrayList<>();
        ExoMediaSource.Quality quality;
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.YELLOW);
        SpannableString spannableString = new SpannableString("720p");
        spannableString.setSpan(colorSpan, 0, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        quality = new com.jarvanmo.exoplayerview.media.SimpleQuality(spannableString, mediaSource.uri());
        qualities.add(quality);

        /*
        spannableString = new SpannableString("720p");
        colorSpan = new ForegroundColorSpan(Color.LTGRAY);
        spannableString.setSpan(colorSpan, 0, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        quality = new com.jarvanmo.exoplayerview.media.SimpleQuality(spannableString, mediaSource.uri());
        qualities.add(quality);

         */

        mediaSource.setQualities(qualities);
//        videoView.changeWidgetVisibility(R.id.exo_player_controller_back,View.INVISIBLE);
        videoView.setMultiQualitySelectorNavigator(new MultiQualitySelectorAdapter.MultiQualitySelectorNavigator() {
            @Override
            public boolean onQualitySelected(ExoMediaSource.Quality quality) {
                //quality.setUri(Uri.parse("https://media.w3.org/2010/05/sintel/trailer.mp4"));
                return false;
            }
        });
        videoView.play(mediaSource, false);

    }



    private void changeToPortrait() {

        // WindowManager operation is not necessary
        WindowManager.LayoutParams attr = getWindow().getAttributes();
//        attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Window window = getWindow();
        window.setAttributes(attr);
        window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);


        wrapper.setVisibility(View.VISIBLE);
    }


    private void changeToLandscape() {

        // WindowManager operation is not necessary

        WindowManager.LayoutParams lp = getWindow().getAttributes();
//        lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        Window window = getWindow();
        window.setAttributes(lp);
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        wrapper.setVisibility(View.GONE);
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT > 23) {
            videoView.resume();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ((Build.VERSION.SDK_INT <= 23)) {
            videoView.resume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT <= 23) {
            videoView.pause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Build.VERSION.SDK_INT > 23) {
            videoView.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoView.releasePlayer();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return videoView.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initListeners() {

        // ExpandableListView on child click listener
        ratingbar.setRating(5);
        updateRecycleView();
        loadComments(userPage);
        commentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                sendButton.setEnabled(charSequence.toString().trim().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ratingbar.getRating()!= 0 && commentEditText.getText()!= null)
                {
                    sendComment();
                }




            }
        });

    }
    public void sendtoLogin()
    {
        Intent loginIntent = new Intent(VideoPlayer.this, FacebookLoginActivity.class);
        startActivity(loginIntent);
        // getActivity().finish();



    }

    private void sendComment() {

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            Toasty.error(this, getResources().getString(R.string.you_have_to_reg_to_comment), Toast.LENGTH_SHORT, true).show();
            //Toast.makeText(this, getResources().getString(R.string.you_have_to_reg_to_comment), Toast.LENGTH_SHORT).show();
            sendtoLogin();

        }
        else {
            showProgressDialog();
            final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            int owner_id = SharedPrefManager.getInstance(this).getUser().getId();

            String owner_name = SharedPrefManager.getInstance(this).getUser().getName();
            //Toast.makeText(this, "" + owner_name, Toast.LENGTH_SHORT).show();
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();


            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(RetrofitClient.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            ApiInterface api = retrofit.create(ApiInterface.class);

            //Defining the trader object as we need to pass it with the call
            Comment trader = new Comment(owner_id, commentEditText.getText().toString(), lecture.getId(), lecture.getCourseId(), (int) ratingbar.getRating(), timestamp.getTime());

            // Log.d("helloo",encodedImage +"   >>"+imgname);
            //Log.d("imggggg","   >>"+imgname);

            //defining the call
            Call<CommentResponse> call = api.createComment(
                    trader.getUsrId(),
                    0,
                    trader.getContent(),
                    trader.getLectureId(),
                    trader.getCourseId(),
                    trader.getRate(),
                    trader.getCreatedAt()


            );
            // Comment fullcomment = new Comment(owner_id , commentEditText.getText().toString(), lecture.getCourseId()  , (int)ratingbar.getRating() ,timestamp.getTime());

            call.enqueue(new Callback<CommentResponse>() {
                @Override
                public void onResponse(Call<CommentResponse> call, Response<CommentResponse> response) {
                    IncRate(lecture.getCourseId(), (int) ratingbar.getRating());
                    if (response.body() != null) {
                        hideProgressDialog();
                        commentEditText.setText("");
                        commentsList.add(response.body().getComment());
                        commentsRecyclerAdapter.notifyDataSetChanged();
                        Toasty.success(VideoPlayer.this, getResources().getString(R.string.comment_succ), Toast.LENGTH_SHORT, true).show();
                       // Toast.makeText(VideoPlayer.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        hideProgressDialog();
                    }
                }

                @Override
                public void onFailure(Call<CommentResponse> call, Throwable t) {
                    hideProgressDialog();
                    Log.d(TAG, "onFailure: " + t.getMessage());
                    //Toast.makeText(VideoPlayer.this, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void updateRecycleView() {
        //RecyclerView Firebase List


        commentsRecyclerAdapter = new CommentsRecyclerAdapter(commentsList , course , lecture);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this , LinearLayoutManager.VERTICAL, false);
        commentsRecyclerView.setHasFixedSize(true);
        commentsRecyclerView.setLayoutManager(mLayoutManager);
        //mLayoutManager.setReverseLayout(true);

        commentsRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                userPage++;
                loadComments(userPage);
            }
        });
        commentsRecyclerView.setAdapter(commentsRecyclerAdapter);
        //mUserDatabase.keepSynced(true);
    }
    private void loadComments(int pageno)
    {


        ApiInterface apiInterface = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);
        Call<Comments> call = apiInterface.getComments(lecture.getId() , 0 , pageno);
        call.enqueue(new Callback<Comments>() {
            @Override
            public void onResponse(Call<Comments> call, Response<Comments> response) {
                // products_listAdapter = new Products_ListAdapter(response.body().getProducts(), ProductsTraderActivity.this);
                // recList.setAdapter(products_listAdapter);
                //Toast.makeText(CommentsActivity.this, response.body().getComments().get(1).getContent(), Toast.LENGTH_SHORT).show();


                if (response.body() != null) {
                    // Toast.makeText(VideoPlayer.this, "" + response.body().getComments().size(), Toast.LENGTH_SHORT).show();
                    commentsList.addAll(response.body().getComments());
                    commentsRecyclerAdapter.notifyDataSetChanged();
                    commentsRecyclerView.scrollToPosition(commentsList.size()-1);
                }

            }

            @Override
            public void onFailure(Call<Comments> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
    }

    public void IncRate(int courseID , int rate)
    {
        int owner_id = SharedPrefManager.getInstance(this).getUser().getId();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface service = retrofit.create(ApiInterface.class);

        Course user = new Course(courseID  , rate , 1);

        Call<CourseResponse> call = service.updateRate(
                course.getId(),
                course.getOwnerId(),
                owner_id,
                rate,
                1

        );

        call.enqueue(new Callback<CourseResponse>() {
            @Override
            public void onResponse(Call<CourseResponse> call, Response<CourseResponse> response) {

                //onBackPressed();
              // Toast.makeText(VideoPlayer.this, "" + course.getOwnerId(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<CourseResponse> call, Throwable t) {

            }
        });
    }

}
