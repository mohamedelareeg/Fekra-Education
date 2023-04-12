package com.rovaind.academy.Controllers;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rovaind.academy.R;
import com.rovaind.academy.Utils.EndlessRecyclerViewScrollListener;
import com.rovaind.academy.Utils.FormatterUtil;
import com.rovaind.academy.Utils.ItemsUI;
import com.rovaind.academy.Utils.Toasty.Toasty;
import com.rovaind.academy.adapter.CommentsPostsHomeRecyclerAdapter;
import com.rovaind.academy.auth.FacebookLoginActivity;
import com.rovaind.academy.manager.CommentsPosts;
import com.rovaind.academy.manager.SharedPrefManager;
import com.rovaind.academy.manager.response.CommentPostResponse;
import com.rovaind.academy.manager.response.PostResponse;
import com.rovaind.academy.model.CommentPost;
import com.rovaind.academy.model.Post;
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

public class PostHomeCommentsActivity extends BaseActivity {

    private static final String TAG = "PostCommentsActivity";
    private Post post;


    private ScrollView scrollView;
    private ViewGroup likesContainer;
    private ImageView likesImageView;
    private TextView commentsLabel;
    private TextView likeCounterTextView;
    private TextView commentsCountTextView;
    private TextView watcherCounterTextView;
    private TextView authorTextView;
    private TextView dateTextView;
    //private ImageView authorImageView;
    private TextView titleTextView;
    private TextView descriptionEditText;
    private ProgressBar commentsProgressBar;
    private TextView warningCommentsTextView;
    private ImageView postImageView;
    private TextView postTxtView;
    //=========== Comments =============
    private RecyclerView commentsRecyclerView;
    private CommentsPostsHomeRecyclerAdapter commentsRecyclerAdapter;
    private List<CommentPost> commentsList;
    private EditText commentEditText;
    private Button sendButton;
    private int userPage = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_comments);

        post = (Post) getIntent().getExtras().getSerializable(ItemsUI.BUNDLE_POSTS);


        init();
        initListeners();
        //UserImage
        //Glide.with(this).load(post.getThumbImage()).into(authorImageView);
        switch (post.getPostType())
        {
            case 1 :
            {
                /*
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)authorImageView.getLayoutParams();
                params.setMargins(8, 50, 8, 8); //substitute parameters for left, top, right, bottom
                authorImageView.setLayoutParams(params);

                 */
                //Descrption
                descriptionEditText.setText(post.getContent());
                break;
            }
            case 2 :
            {
                //Descrption
                descriptionEditText.setText(post.getContent());
                Glide.with(this).load(RetrofitClient.BASE_POST_URL + post.getGallery().get(0).getThumbImage()).into(postImageView);
                postImageView.setVisibility(View.VISIBLE);
                break;
            }
            case 4 :
            {
                //Descrption
                //descriptionEditText.setText(post.getContent());
                postTxtView.setText(post.getContent());
                postTxtView.setTextColor(Color.parseColor(post.getTxtColor()));
                Glide.with(PostHomeCommentsActivity.this)
                        .load(RetrofitClient.BASE_POST_IMAGE_URL +  post.getBackImg())

                        .into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource,
                                                        @Nullable Transition<? super Drawable> transition) {

                                postTxtView.setBackground(resource);

                            }
                        });
                postTxtView.setVisibility(View.VISIBLE);
                break;
            }
            default:
            {
                descriptionEditText.setText(post.getContent());
                break;
            }
        }


        //Glide.with(this).load(blogPost.getImage_url()).into(postImageView);
        //UserName
        authorTextView.setText(post.getOwnerName());

        //PostTime
        //  String lastSeenTime = GetTimeAgo.getTimeAgo(post.getCreatedAt(), this);
        CharSequence date = FormatterUtil.getRelativeTimeSpanString(this, post.getCreatedAt());

        dateTextView.setText(date);
        //LikesCount
        likeCounterTextView.setText(post.getLikeCount() + " " + getResources().getString(R.string.likes));
        commentsCountTextView.setText(post.getCommentCount() +" " + getResources().getString(R.string.comment));
        watcherCounterTextView.setText(post.getViewCount() +" " + getResources().getString(R.string.student));
        incViewedCount();
    }

    private void init() {
        postTxtView = findViewById(R.id.postTxtView);
        postImageView = findViewById(R.id.postImageView);
        titleTextView = findViewById(R.id.titleTextView);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        scrollView = findViewById(R.id.scrollView);
        commentsLabel = findViewById(R.id.commentsLabel);
        likesContainer = findViewById(R.id.likesContainer);
        likesImageView = findViewById(R.id.likesImageView);
        //authorImageView = findViewById(R.id.authorImageView);
        authorTextView = findViewById(R.id.authorTextView);
        likeCounterTextView = findViewById(R.id.likeCounterTextView);
        commentsCountTextView = findViewById(R.id.commentsCountTextView);
        watcherCounterTextView = findViewById(R.id.watcherCounterTextView);
        dateTextView = findViewById(R.id.dateTextView);
        commentsProgressBar = findViewById(R.id.commentsProgressBar);
        warningCommentsTextView = findViewById(R.id.warningCommentsTextView);

        commentsList = new ArrayList<>();
        commentEditText = findViewById(R.id.commentEditText);
        sendButton = findViewById(R.id.sendButton);
        commentsRecyclerView = findViewById(R.id.commentsRecyclerView);



    }
    private void initListeners() {

        // ExpandableListView on child click listener
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

                if(commentEditText.getText()!= null)
                {
                    sendComment();
                }




            }
        });

    }
    public void sendtoLogin()
    {
        Intent loginIntent = new Intent(PostHomeCommentsActivity.this, FacebookLoginActivity.class);
        startActivity(loginIntent);
        // getActivity().finish();



    }
    private void sendComment() {

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            Toasty.error(this, getResources().getString(R.string.you_have_to_reg_to_comment), Toast.LENGTH_SHORT, true).show();
           // Toast.makeText(this, getResources().getString(R.string.you_have_to_reg_to_comment), Toast.LENGTH_SHORT).show();
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
            CommentPost trader = new CommentPost(owner_id, commentEditText.getText().toString(), 0 , post.getId(), 0, timestamp.getTime());

            // Log.d("helloo",encodedImage +"   >>"+imgname);
            //Log.d("imggggg","   >>"+imgname);

            //defining the call
            Call<CommentPostResponse> call = api.createCommentPost(
                    trader.getUsrId(),
                    0,
                    trader.getContent(),
                    trader.getCourseId(),
                    trader.getPostId(),
                    trader.getRate(),
                    trader.getCreatedAt()


            );
            // Comment fullcomment = new Comment(owner_id , commentEditText.getText().toString(), lecture.getCourseId()  , (int)ratingbar.getRating() ,timestamp.getTime());

            call.enqueue(new Callback<CommentPostResponse>() {
                @Override
                public void onResponse(Call<CommentPostResponse> call, Response<CommentPostResponse> response) {
                    // IncRate(lecture.getCourseId(), (int) ratingbar.getRating());
                    if (response.body() != null) {
                        hideProgressDialog();
                        commentEditText.setText("");
                        commentsList.add(response.body().getCommentposts());
                        commentsRecyclerAdapter.notifyDataSetChanged();
                        Toasty.success(PostHomeCommentsActivity.this, getResources().getString(R.string.comment_succ), Toast.LENGTH_SHORT, true).show();
                       // Toast.makeText(PostHomeCommentsActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        hideProgressDialog();
                        Log.d(TAG, "onResponse: " + response.message()) ;
                        Log.d(TAG, "onResponse: " + response.code()) ;
                        Log.d(TAG, "onResponse: " + response.body()) ;
                        Log.d(TAG, "onResponse: " + response.errorBody().toString()) ;
                    }
                }

                @Override
                public void onFailure(Call<CommentPostResponse> call, Throwable t) {
                    hideProgressDialog();
                    Log.d(TAG, "onFailure: " + t.getMessage());
                    //Toast.makeText(VideoPlayer.this, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void updateRecycleView() {
        //RecyclerView Firebase List


        commentsRecyclerAdapter = new CommentsPostsHomeRecyclerAdapter(commentsList ,post );
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
        Call<CommentsPosts> call = apiInterface.getCommentsPosts(post.getId() , 0 , pageno);
        call.enqueue(new Callback<CommentsPosts>() {
            @Override
            public void onResponse(Call<CommentsPosts> call, Response<CommentsPosts> response) {
                // products_listAdapter = new Products_ListAdapter(response.body().getProducts(), ProductsTraderActivity.this);
                // recList.setAdapter(products_listAdapter);
                //Toast.makeText(CommentsActivity.this, response.body().getComments().get(1).getContent(), Toast.LENGTH_SHORT).show();


                if (response.body() != null) {
                    //Toast.makeText(ReplyActivity.this, "" + response.body().getComments().size(), Toast.LENGTH_SHORT).show();
                    // Toast.makeText(VideoPlayer.this, "" + response.body().getComments().size(), Toast.LENGTH_SHORT).show();
                    commentsList.addAll(response.body().getCommentPosts());
                    commentsRecyclerAdapter.notifyDataSetChanged();
                    commentsRecyclerView.scrollToPosition(commentsList.size()-1);
                }

            }

            @Override
            public void onFailure(Call<CommentsPosts> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
    }

    private void incViewedCount() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface service = retrofit.create(ApiInterface.class);


        Call<PostResponse> call = service.updatePostView(
                post.getId(),
                1

        );

        call.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {

            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
    }
}