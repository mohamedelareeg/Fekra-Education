package com.rovaind.academy.Controllers.fragment_home;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rovaind.academy.R;
import com.rovaind.academy.adapter.MyGroupsAdapter;
import com.rovaind.academy.manager.Groups;
import com.rovaind.academy.manager.SharedPrefManager;
import com.rovaind.academy.model.group;
import com.rovaind.academy.retrofit.ApiInterface;
import com.rovaind.academy.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GroupsFragment extends Fragment {

    ApiInterface apiInterface;
    private List<group> courseList;
    private MyGroupsAdapter myGroupsAdapter;
    private RecyclerView recgroups;
    public GroupsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_groups, container, false);
        recgroups = view.findViewById(R.id.my_groups_recycler);

        AssignPostList();
        getData();
        // Inflate the layout for this fragment
        return view;
    }

    private void AssignPostList(){

        courseList = new ArrayList<>();

        LinearLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);

        recgroups.setLayoutManager(mLayoutManager);
        recgroups.setItemAnimator(new DefaultItemAnimator());
        recgroups.setHasFixedSize(true);

        myGroupsAdapter = new MyGroupsAdapter(getContext() , courseList );
        recgroups.setAdapter(myGroupsAdapter);

    }

    private void getData() {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final String data = prefs.getString("language", "ar"); //no id: default value
        apiInterface = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);

        Call<Groups> call = apiInterface.getGroupMemberByID(SharedPrefManager.getInstance(getContext()).getUser().getId(),1);//(data.equals("en") ? 0 :1)

        call.enqueue(new Callback<Groups>() {
            @Override
            public void onResponse(Call<Groups> call, Response<Groups> response) {


                if(response.body() != null)
                {

                    courseList.addAll(response.body().getGroups());
                    myGroupsAdapter.notifyDataSetChanged();


                }
                else
                {

                }
                // setLessonList(response.body().getLectures());

            }



            @Override
            public void onFailure(Call<Groups> call, Throwable t) {
                Log.d("ERROR", "onFailure: Posts " + t.getLocalizedMessage());

            }
        });

    }

}