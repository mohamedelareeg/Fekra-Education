package com.rovaind.academy.Controllers.fragment_home;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.rovaind.academy.Controllers.SubCategoryActivity;
import com.rovaind.academy.R;
import com.rovaind.academy.Utils.ItemsUI;
import com.rovaind.academy.Utils.exoplayerview.extension.MultiQualitySelectorAdapter;
import com.rovaind.academy.Utils.exoplayerview.media.ExoMediaSource;
import com.rovaind.academy.Utils.exoplayerview.media.SimpleMediaSource;
import com.rovaind.academy.Utils.exoplayerview.ui.ExoVideoView;
import com.rovaind.academy.Utils.shimmer.ShimmerFrameLayout;
import com.rovaind.academy.adapter.AdsAdapter;
import com.rovaind.academy.interfaces.OnAdsClickListener;
import com.rovaind.academy.manager.Ads;
import com.rovaind.academy.manager.SharedPrefManager;
import com.rovaind.academy.model.Ad;
import com.rovaind.academy.retrofit.ApiInterface;
import com.rovaind.academy.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.rovaind.academy.Utils.ThemeUtil.THEME_RED;


public class HomeFragment extends Fragment  implements OnAdsClickListener {


    ApiInterface apiInterface;
    private ExoVideoView videoView;
   // MultiSnapRecyclerView popularRecycler, instructorRecycler;
    //PopularCourseAdapter popularCourseAdapter;

   // InstructorAdapter instructorAdapter;
    //com.rovaind.elmohader.Utils.views.TextViewAr btn_viewcourses , textviewinstructors;

    LinearLayout publiccenter_panel , private_panel , extra_panel;

    //private RecyclerView AdsrecList;
   // private ArrayList<Ad> ads_list;
    //private AdsAdapter adsAdapter;

    private RecyclerView AdsrecList;
    private ArrayList<Ad> ads_list;
    private AdsAdapter adsAdapter;
    private ShimmerFrameLayout shimmerFrameLayoutads ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    int LevelID;
    int ClassID;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        LevelID = SharedPrefManager.getInstance(getContext()).getSelectedLevel();
        ClassID = SharedPrefManager.getInstance(getContext()).getSelectedClass();

        //shimmerFrameLayout = view.findViewById(R.id.parentShimmerLayout);
       // shimmerFrameLayout2 = view.findViewById(R.id.parentShimmerLayout2);
        shimmerFrameLayoutads = view.findViewById(R.id.parentShimmerLayoutads);

        //shimmerFrameLayout.startShimmer();
        //shimmerFrameLayout2.startShimmer();
        shimmerFrameLayoutads.startShimmer();
        //videoView = view.findViewById(R.id.videoView);

        //textviewinstructors = view.findViewById(R.id.textviewinstructor);
        //btn_viewcourses = view.findViewById(R.id.textviewcourses);
        publiccenter_panel = view.findViewById(R.id.public_courses);
        private_panel = view.findViewById(R.id.privatecourses);
        extra_panel = view.findViewById(R.id.extra_courses);
      //  presents_panel = view.findViewById(R.id.presents);

        //instructorRecycler = view.findViewById(R.id.instructorList);
        //popularRecycler = view.findViewById(R.id.popular_recycler);

       // initVideoView();

        publiccenter_panel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SubCategoryActivity.class);
                intent.putExtra(ItemsUI.BUNDLE_CATEGORIES_LEVEL, LevelID);
                intent.putExtra(ItemsUI.BUNDLE_CATEGORIES_CLASS, ClassID);
                intent.putExtra(ItemsUI.BUNDLE_CATEGORIES_CENTER_ID, 0);
                intent.putExtra(ItemsUI.BUNDLE_CATEGORIES_SECTION, 2);
                intent.putExtra(ItemsUI.BUNDLE_CATEGORIES_TYPE, 1);
                intent.putExtra(ItemsUI.BUNDLE_THEME, THEME_RED);
                startActivity(intent);
            }
        });
        private_panel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SubCategoryActivity.class);
                intent.putExtra(ItemsUI.BUNDLE_CATEGORIES_LEVEL, LevelID);
                intent.putExtra(ItemsUI.BUNDLE_CATEGORIES_CLASS, ClassID);
                intent.putExtra(ItemsUI.BUNDLE_CATEGORIES_CENTER_ID, 0);
                intent.putExtra(ItemsUI.BUNDLE_CATEGORIES_SECTION, 2);
                intent.putExtra(ItemsUI.BUNDLE_CATEGORIES_TYPE, 2);
                intent.putExtra(ItemsUI.BUNDLE_THEME, THEME_RED);
                startActivity(intent);
            }
        });
        extra_panel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SubCategoryActivity.class);
                intent.putExtra(ItemsUI.BUNDLE_CATEGORIES_LEVEL, LevelID);
                intent.putExtra(ItemsUI.BUNDLE_CATEGORIES_CLASS, ClassID);
                intent.putExtra(ItemsUI.BUNDLE_CATEGORIES_CENTER_ID, 0);
                intent.putExtra(ItemsUI.BUNDLE_CATEGORIES_SECTION, 2);
                intent.putExtra(ItemsUI.BUNDLE_CATEGORIES_TYPE, 3);
                intent.putExtra(ItemsUI.BUNDLE_THEME, THEME_RED);
                startActivity(intent);
            }
        });

        AdsrecList = (RecyclerView) view.findViewById(R.id.slider);
        ads_list = new ArrayList<>();
        adsAdapter = new AdsAdapter(ads_list , this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity()) {

            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
                try {
                    LinearSmoothScroller smoothScroller = new LinearSmoothScroller(Objects.requireNonNull(getActivity())) {
                        private static final float SPEED = 3500f;// Change this value (default=25f)

                        @Override
                        protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                            return SPEED / displayMetrics.densityDpi;
                        }
                    };
                    smoothScroller.setTargetPosition(position);
                    startSmoothScroll(smoothScroller);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        //  LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        autoScrollAnother();
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        AdsrecList.setLayoutManager(layoutManager);//products_staggeredGridLayoutManager
        //
        AdsrecList.setHasFixedSize(true);
        AdsrecList.setItemViewCacheSize(1000);
        AdsrecList.setDrawingCacheEnabled(true);
        AdsrecList.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        //
        AdsrecList.setAdapter(adsAdapter);
        loadAds();
//        LoadCourses();
  //      LoadInstructors();


        //AdsrecList = (RecyclerView) view.findViewById(R.id.slider);

        //  LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        // Inflate the layout for this fragment
        return view;
    }

    private void changeToPortrait() {

        // WindowManager operation is not necessary
        WindowManager.LayoutParams attr = getActivity().getWindow().getAttributes();
//        attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Window window = getActivity(). getWindow();
        window.setAttributes(attr);
        window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);



    }


    private void changeToLandscape() {

        // WindowManager operation is not necessary

        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
//        lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        Window window = getActivity().getWindow();
        window.setAttributes(lp);
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

    }
    private void initVideoView() {

        videoView.setPortrait(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);


//
//        SimpleMediaSource mediaSource = new SimpleMediaSource("http://flv2.bn.netease.com/videolib3/1604/28/fVobI0704/SD/fVobI0704-mobile.mp4");
//        mediaSource.setDisplayName("Apple HLS");

        String videourl = RetrofitClient.BASE_VIDEO_URL + RetrofitClient.INTRO_LINK ; //+ "/preview"
        SimpleMediaSource mediaSource = new SimpleMediaSource(videourl);
        mediaSource.setDisplayName("اكاديميه_المحاضر");

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
    int scrollCount = 0;
    public void autoScrollAnother() {
        scrollCount = 0;
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                AdsrecList.smoothScrollToPosition((scrollCount++));
                if (scrollCount == adsAdapter.getItemCount() - 4) {
                    ads_list.addAll(ads_list);
                    adsAdapter.notifyDataSetChanged();
                }
                handler.postDelayed(this, 3000);
            }
        };
        handler.postDelayed(runnable, 3000);
    }
    private void loadAds() {
        apiInterface = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);

        Call<Ads> call = apiInterface.getAds(0);

        call.enqueue(new Callback<Ads>() {
            @Override
            public void onResponse(Call<Ads> call, final Response<Ads> response) {

                if(response.body() != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ads_list.addAll(response.body().getAds());
                            adsAdapter.notifyDataSetChanged();
                            shimmerFrameLayoutads.setVisibility(View.GONE);
                            shimmerFrameLayoutads.stopShimmer();
                        }
                    }).run();
                }


            }

            @Override
            public void onFailure(Call<Ads> call, Throwable t) {

            }
        });
    }

    @Override
    public void onAdsClicked(Ad contact, int position) {

    }

}