package com.rovaind.academy.Controllers;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.claudiodegio.msv.FilterMaterialSearchView;
import com.claudiodegio.msv.OnFilterViewListener;
import com.claudiodegio.msv.OnSearchViewListener;
import com.claudiodegio.msv.model.Filter;
import com.claudiodegio.msv.model.Section;
import com.rovaind.academy.R;
import com.rovaind.academy.manager.Categories;
import com.rovaind.academy.manager.SharedPrefManager;
import com.rovaind.academy.retrofit.ApiInterface;
import com.rovaind.academy.retrofit.RetrofitClient;

import java.util.List;

import kotlin.LateinitKt;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends BaseSearchView implements OnSearchViewListener, OnFilterViewListener {

    private static final String TAG = "SearchActivity";
    int levelID;
    FilterMaterialSearchView cast;

    @Override
    protected void initCustom() {
        super.initCustom();
        mSearchView.setOnSearchViewListener(this);


        cast = (FilterMaterialSearchView)mSearchView;

        levelID = SharedPrefManager.getInstance(getApplicationContext()).getSelectedLevel();
        Section section = new Section(getResources().getString(R.string.subjects));
        cast.addSection(section);
        cast.setOnFilterViewListener(this);
        getSubCategory(levelID);
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


        Call<Categories> call = service.getSubCategories(subCategory , 3 ,1);

        call.enqueue(new Callback<Categories>() {
            @Override
            public void onResponse(Call<Categories> call, Response<Categories> response) {
                Log.d(TAG, "onResponse: " +response.body().getCategories() );
                for (int i = 0 ; i  < response.body().getCategories().size() ; i++) {
                    Filter filter = new Filter(1, response.body().getCategories().get(i).getCatName(), response.body().getCategories().get(i).getId(), R.drawable.ic_about_24dp, getResources().getColor(R.color.indigo_500));
                    cast.addFilter(filter);
                }

            }

            @Override
            public void onFailure(Call<Categories> call, Throwable t) {

            }
        });
    }
    @Override
    public int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    public void onSearchViewShown(){
    }

    @Override
    public void onSearchViewClosed() {
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Toast.makeText(this, "onQueryTextSubmit:" + query, Toast.LENGTH_SHORT).show();

        return true;
    }

    @Override
    public void onQueryTextChange(String newText) {

    }

    @Override
    public void onFilterAdded(Filter filter) {

        Log.d("TAG", "onFilterAdded:" + filter.getName());

    }

    @Override
    public void onFilterRemoved(Filter filter) {
        Log.d("TAG", "onFilterRemoved:" + filter.getName());

    }

    @Override
    public void onFilterChanged(List<Filter> list) {
        Log.d("TAG", "onFilterChanged:" + list.size());

    }

    /*
    public class SimpleRVAdapter extends RecyclerView.Adapter<BaseMatSearchViewActivity.SimpleRVAdapter.SimpleViewHolder> {
        private List<String> dataSource;
        public SimpleRVAdapter(List<String> dataArgs){
            dataSource = dataArgs;
        }
        @Override
        public BaseMatSearchViewActivity.SimpleRVAdapter.SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            View view = LayoutInflater.from(BaseMatSearchViewActivity.this).inflate(android.R.layout.simple_list_item_1, parent, false);
            BaseMatSearchViewActivity.SimpleRVAdapter.SimpleViewHolder viewHolder = new BaseMatSearchViewActivity.SimpleRVAdapter.SimpleViewHolder(view);
            return viewHolder;
        }
        public  class SimpleViewHolder extends RecyclerView.ViewHolder{
            public TextView textView;
            public SimpleViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView;
            }
        }
        @Override
        public void onBindViewHolder(BaseMatSearchViewActivity.SimpleRVAdapter.SimpleViewHolder holder, int position) {
            holder.textView.setText(dataSource.get(position));
        }
        @Override
        public int getItemCount() {
            return dataSource.size();
        }
    }*/
}