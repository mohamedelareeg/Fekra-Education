package com.rovaind.academy.Controllers.fragment_home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rovaind.academy.Controllers.PostBottomSheetFragment;
import com.rovaind.academy.Controllers.PostHomeBottomSheetFragment;
import com.rovaind.academy.R;
import com.rovaind.academy.Utils.EndlessRecyclerViewScrollListener;
import com.rovaind.academy.adapter.BlogHomeRecyclyerAdapter;
import com.rovaind.academy.auth.FacebookLoginActivity;
import com.rovaind.academy.interfaces.OnPostClickListener;
import com.rovaind.academy.manager.Posts;
import com.rovaind.academy.manager.SharedPrefManager;
import com.rovaind.academy.model.Course;
import com.rovaind.academy.model.Post;
import com.rovaind.academy.retrofit.ApiInterface;
import com.rovaind.academy.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TweetsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener ,  OnPostClickListener , PostBottomSheetFragment.onSomeEventListener {
    public  TweetsFragment()
    {
        // Required empty public constructor
    }
    ApiInterface apiInterface;
    private List<Post> postList;
    private BlogHomeRecyclyerAdapter postsAdapter;
    private RecyclerView recPosts;
    private int userPage = 1;
    int classID = 0;
    private SwipeRefreshLayout swipeRefreshLayout;
    PostHomeBottomSheetFragment fragmentpost = new PostHomeBottomSheetFragment();
    FloatingActionButton addpostBtn;
    private ProgressDialog mLoginProgress;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweets, container, false);

        recPosts = view.findViewById(R.id.blog_list_view);
        addpostBtn = view.findViewById(R.id.floating_action_button);
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeContainer);
        swipeRefreshLayout.setOnRefreshListener(this);
        mLoginProgress = new ProgressDialog(getContext());
        classID = SharedPrefManager.getInstance(getContext()).getSelectedClass();
        Log.d("TAG", "onCreateView: " + classID);
        AssignPostList();
        getData(classID  ,  userPage);
        // Inflate the layout for this fragment
        addpostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SharedPrefManager.getInstance(getContext()).isLoggedIn()) {
                    showPost();
                }
                else
                {
                    Intent intent = new Intent(getContext(), FacebookLoginActivity.class);
                    startActivity(intent);
                }
            }
        });
        return view;
    }
    private void AssignPostList(){

        postList = new ArrayList<>();

        LinearLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);

        recPosts.setLayoutManager(mLayoutManager);
        recPosts.setItemAnimator(new DefaultItemAnimator());
        recPosts.setHasFixedSize(true);
        recPosts.addOnScrollListener(new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                userPage++;
                getData(classID  ,  userPage);

            }
        });
        postsAdapter = new BlogHomeRecyclyerAdapter(postList  , this);
        recPosts.setAdapter(postsAdapter);

    }

    private void getData(int ClassID, int userPage) {
        if(userPage == 1) {
            showProgressDialog();
        }
        swipeRefreshLayout.setRefreshing(true);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final String data = prefs.getString("language", "ar"); //no id: default value
        apiInterface = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);

        Call<Posts> call = apiInterface.getPublicPosts(ClassID ,userPage);//(data.equals("en") ? 0 :1)

        call.enqueue(new Callback<Posts>() {
            @Override
            public void onResponse(Call<Posts> call, Response<Posts> response) {


                if(response.body() != null)
                {

                    postList.addAll(response.body().getPosts());
                    postsAdapter.notifyDataSetChanged();
                    hideProgressDialog();

                }
                else
                {
                    hideProgressDialog();
                }
                // setLessonList(response.body().getLectures());

            }



            @Override
            public void onFailure(Call<Posts> call, Throwable t) {
                Log.d("ERROR", "onFailure: Posts " + t.getLocalizedMessage());
                hideProgressDialog();
            }
        });
        swipeRefreshLayout.setRefreshing(false);
    }

    void showPost() {

        fragmentpost.show(getActivity().getSupportFragmentManager(), fragmentpost.getTag());
    }


    @Override
    public void PostEvent(Post post) {

        postList.add(0 ,post);
        postsAdapter.notifyDataSetChanged();
    }
    @Override
    public void onPostClicked(Post post, Course course, int position) {

    }

    @Override
    public void onRefresh() {

        postList.clear();
        userPage = 1;
        getData(classID  ,  userPage);
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
}