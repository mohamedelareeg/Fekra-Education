package com.rovaind.academy.Controllers;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rovaind.academy.R;
import com.rovaind.academy.Utils.EndlessRecyclerViewScrollListener;
import com.rovaind.academy.Utils.ItemsUI;
import com.rovaind.academy.Utils.Toasty.Toasty;
import com.rovaind.academy.adapter.CommentsRecyclerAdapter;
import com.rovaind.academy.auth.FacebookLoginActivity;
import com.rovaind.academy.manager.Comments;
import com.rovaind.academy.manager.SharedPrefManager;
import com.rovaind.academy.manager.response.CommentResponse;
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

public class ReplyActivity extends BaseActivity {
    private static final String TAG = "ReplyActivity";
    private Comment comment;
    private Course course;
    private Lecture lecture;

    //=========== Comments =============
    private RecyclerView commentsRecyclerView;
    private CommentsRecyclerAdapter commentsRecyclerAdapter;
    private List<Comment> commentsList;
    private EditText commentEditText;
    private Button sendButton;
    private int userPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);

        comment = (Comment) getIntent().getExtras().getSerializable(ItemsUI.BUNDLE_COMMENTS);
        course = (Course) getIntent().getExtras().getSerializable(ItemsUI.BUNDLE_COURSES_DETAILS);
        lecture = (Lecture) getIntent().getExtras().getSerializable(ItemsUI.BUNDLE_COURSES_DETAILS_LECTURE);

        init();
        initListeners();
    }
    private void init() {
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
        Intent loginIntent = new Intent(ReplyActivity.this, FacebookLoginActivity.class);
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
            Comment trader = new Comment(owner_id, commentEditText.getText().toString(), lecture.getId(), lecture.getCourseId(), 0, timestamp.getTime());

            // Log.d("helloo",encodedImage +"   >>"+imgname);
            //Log.d("imggggg","   >>"+imgname);

            //defining the call
            Call<CommentResponse> call = api.createComment(
                    trader.getUsrId(),
                    comment.getId(),
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
                   // IncRate(lecture.getCourseId(), (int) ratingbar.getRating());
                    if (response.body() != null) {
                        hideProgressDialog();
                        commentEditText.setText("");
                        commentsList.add(response.body().getComment());
                        commentsRecyclerAdapter.notifyDataSetChanged();
                        Toast.makeText(ReplyActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
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
        Call<Comments> call = apiInterface.getReplyComments(comment.getId() , pageno);
        call.enqueue(new Callback<Comments>() {
            @Override
            public void onResponse(Call<Comments> call, Response<Comments> response) {
                // products_listAdapter = new Products_ListAdapter(response.body().getProducts(), ProductsTraderActivity.this);
                // recList.setAdapter(products_listAdapter);
                //Toast.makeText(CommentsActivity.this, response.body().getComments().get(1).getContent(), Toast.LENGTH_SHORT).show();


                if (response.body() != null) {
                    //Toast.makeText(ReplyActivity.this, "" + response.body().getComments().size(), Toast.LENGTH_SHORT).show();
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
}