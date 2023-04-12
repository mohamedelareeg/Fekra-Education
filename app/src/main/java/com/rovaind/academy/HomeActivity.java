package com.rovaind.academy;

import androidx.appcompat.widget.Toolbar;
import androidx.emoji.bundled.BundledEmojiCompatConfig;
import androidx.emoji.text.EmojiCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;


import com.bumptech.glide.Glide;

import com.google.firebase.iid.FirebaseInstanceId;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.mikepenz.iconics.context.IconicsContextWrapper;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.moos.navigation.BottomBarLayout;
import com.moos.navigation.BottomTabView;
import com.rovaind.academy.Controllers.BaseActivity;
import com.rovaind.academy.Controllers.ProfileActivity;
import com.rovaind.academy.Controllers.SearchActivity;
import com.rovaind.academy.Controllers.SettingsActivity;
import com.rovaind.academy.Controllers.fragment_home.GroupsFragment;
import com.rovaind.academy.Controllers.fragment_home.TweetsFragment;
import com.rovaind.academy.Utils.BottomNavigationViewHelper;
import com.rovaind.academy.Utils.SectionsPagerAdapter;
import com.rovaind.academy.about.AboutActivity;
import com.rovaind.academy.Controllers.fragment_home.HomeFragment;
import com.rovaind.academy.auth.FacebookLoginActivity;
import com.rovaind.academy.manager.SharedPrefManager;
import com.rovaind.academy.manager.response.UserResponse;
import com.rovaind.academy.model.User;
import com.rovaind.academy.retrofit.ApiInterface;
import com.rovaind.academy.retrofit.RetrofitClient;

import java.util.List;


import klogi.com.RtlViewPager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends BaseActivity {
    private static final String TAG = "HomeActivity" ;
    private String username = "عضو جديد فى العائلة";
    private String profilePic;
    private int following;
    private int followers;
    private List<String> followingList;
    private AccountHeader headerResult;
    private Toolbar toolbar;

    private static final int ACTIVITY_NUM = 0;
    private static final int HOME_FRAGMENT = 1;

    //widgets
    private RtlViewPager mViewPager;
    private FrameLayout mFrameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);



        mViewPager = (RtlViewPager) findViewById(R.id.viewpager_container);
        mFrameLayout = (FrameLayout) findViewById(R.id.container);
        EmojiCompat.Config config = new BundledEmojiCompatConfig(this);
        EmojiCompat.init(config);
        ImageView searchImg = findViewById(R.id.search);
        searchImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });





        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            //registerGCM();
            sendRegistrationTokenToServer(FirebaseInstanceId.getInstance().getToken());

            username  = SharedPrefManager.getInstance(this).getUser().getName();
            profilePic = SharedPrefManager.getInstance(this).getUser().getThumb_image();

            showProfile();
            //Toast.makeText(this, String.valueOf( SharedPrefManager.getInstance(getApplicationContext()).getLastViewed()) + " " , Toast.LENGTH_SHORT).show();
        }
        else
        {
            /*
            List<String> supplierNames = new ArrayList<>();
            supplierNames.add("sup1");
            supplierNames.add("sup2");
            supplierNames.add("sup3");

             */
            username = "عضو جديد فى العائلة";
            profilePic = RetrofitClient.BASE_MAIN + "logo.jpg";
           /*
            followingList = supplierNames;
            following = 50;
            followers = 50;

            */
            showProfile();
        }
        /*
        ProgressBar progressBar = (ProgressBar)findViewById(R.id.progress);
        Sprite doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
        progressBar.setVisibility(View.VISIBLE);

         */




       // setupBottomNavigationView();
        setupViewPager();
        /*
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new HomeFragment())
                .commit();

         */

    }

    private void setupViewPager(){
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment()); //index 1
        adapter.addFragment(new TweetsFragment()); //index 2
        adapter.addFragment(new GroupsFragment()); //index 0



        BottomBarLayout tabLayout = (BottomBarLayout ) findViewById(R.id.tabs);

        tabLayout.bindViewPager(mViewPager);
        BottomTabView tab_home = new BottomTabView(this);
        tab_home.setBackgroundColor(getResources().getColor(R.color.teal_900));
        tab_home.setSelectedColor(getResources().getColor(R.color.white));
        tab_home.setTabIcon(R.drawable.ic_house);
        tab_home.setTabTitle(getResources().getString(R.string.home));

        BottomTabView tab_look = new BottomTabView(this);
        tab_look.setBackgroundColor(getResources().getColor(R.color.teal_900));
        tab_look.setSelectedColor(getResources().getColor(R.color.white));
        tab_look.setTabIcon(R.drawable.ic_news);
        tab_look.setTabTitle(getResources().getString(R.string.news));

        BottomTabView tab_mine = new BottomTabView(this);
        tab_mine.setBackgroundColor(getResources().getColor(R.color.teal_900));
        tab_mine.setSelectedColor(getResources().getColor(R.color.white));
        tab_mine.setTabIcon(R.drawable.ic_groups);
        tab_mine.setTabTitle(getResources().getString(R.string.my_groups));
        tab_mine.setUnreadCount(SharedPrefManager.getInstance(getApplicationContext()).getCourseCount());


        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            tabLayout
                    .addTab(tab_home)
                    .addTab(tab_look)
                    .addTab(tab_mine)
                    .create(new BottomBarLayout.OnTabSelectedListener() {
                        @Override
                        public void onTabSelected(BottomTabView tab) {
                            //you can switch the fragment here
                            Log.e(TAG, "onTabSelected: ====="+tab.getTabPosition() );
                        }

                        @Override
                        public void onTabUnselected(BottomTabView tab) {

                        }

                        @Override
                        public void onTabReselected(BottomTabView tab) {

                        }
                    });
        }
        else {
            tabLayout
                    .addTab(tab_home)
                    .addTab(tab_look)
                    .create(new BottomBarLayout.OnTabSelectedListener() {
                        @Override
                        public void onTabSelected(BottomTabView tab) {
                            //you can switch the fragment here
                            Log.e(TAG, "onTabSelected: =====" + tab.getTabPosition());
                        }

                        @Override
                        public void onTabUnselected(BottomTabView tab) {

                        }

                        @Override
                        public void onTabReselected(BottomTabView tab) {

                        }
                    });
        }
        mViewPager.setAdapter(adapter);
        /*
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_house);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_news);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_person);

         */

    }

    /**
     * BottomNavigationView setup
     */
    private void setupBottomNavigationView(){
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(getApplicationContext(), this,bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(IconicsContextWrapper.wrap(newBase));
    }


    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);

        MenuItem mSearch = menu.findItem(R.id.action_search);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) mSearch.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
                intent.putExtra(ItemsUI.BUNDLE_SEARCH_TXT, query);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        searchView.setQueryHint("Search");
        return true;
    }



     */


    private void showProfile(){


        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {

                Glide.with(imageView.getContext()).load(uri).placeholder(placeholder).into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                Glide.with(imageView.getContext()).clear(imageView);            }
        });
       // String userInfoText = following + " " + getResources().getString(R.string.following) + "  |  " + followers + " " + getResources().getString(R.string.followers);


        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withOnAccountHeaderProfileImageListener(new AccountHeader.OnAccountHeaderProfileImageListener() {
                    @Override
                    public boolean onProfileImageClick(View view, IProfile profile, boolean current) {
                        if(SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()) {
                            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                            startActivity(intent);
                        }
                        else
                        {
                            Intent intent = new Intent(HomeActivity.this, FacebookLoginActivity.class);
                            startActivity(intent);
                        }
                        return false;
                    }

                    @Override
                    public boolean onProfileImageLongClick(View view, IProfile profile, boolean current) {
                        return false;
                    }
                })
                .addProfiles(new ProfileDrawerItem().withName(username).withIcon(profilePic))//.withEmail(userInfoText)
                .build();



        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName(getResources().getString(R.string.mainpage)).withIcon(R.drawable.md_paper);
       // SecondaryDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(2).withName(getResources().getString(R.string.action_settings)).withIcon(R.drawable.md_settings).withSelectable(false);


        Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        item1,
                        new DividerDrawerItem(),
                      //  item2,
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName(getResources().getString(R.string.about_application)).withSelectable(false).withIcon(R.drawable.md_information_circle_outline)
                     //   new SecondaryDrawerItem().withName((SharedPrefManager.getInstance(this).isLoggedIn() ? getResources().getString(R.string.signout) :getResources().getString(R.string.login))).withSelectable(false).withIcon(R.drawable.md_log_out)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch (position){
                            case 3:
                                startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
                                break;
                            case 4:
                                startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
                                break;
                            case 5:
                                startActivity(new Intent(HomeActivity.this, AboutActivity.class));
                                break;
                            case 6:
                                signOut();
                                break;
                        }
                        return true;
                    }
                })
                .build();




    }

    private void signOut() {

        Intent intent = new Intent(HomeActivity.this, FacebookLoginActivity.class);
        startActivity(intent);
    }



    private void sendRegistrationTokenToServer(final String token) {
        //Getting the user id from shared preferences
        //We are storing gcm token for the user in our mysql database
        Log.d(TAG, "sendRegistrationTokenToServer: " + token);
        ApiInterface service = RetrofitClient.retrofitWrite().create(ApiInterface.class);

        User user = new User(SharedPrefManager.getInstance(getApplicationContext()).getUser().getId(), token);
        SharedPrefManager.getInstance(getApplicationContext()).userToken(token);
        Log.d(TAG, "sendRegistrationTokenToServer: " + SharedPrefManager.getInstance(getApplicationContext()).getUser().getId());
        Call<UserResponse> call = service.updateGcmToken(
                user.getId(),
                user.getGcmtoken()

        );

        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {

                if(response.body() != null)
                {

                }
                // Toast.makeText(HomeActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                //Log.d("gcmKey", "onResponse: " + response.body().getMessage());
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
                //Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }

}