package com.rovaind.academy.auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.emoji.bundled.BundledEmojiCompatConfig;
import androidx.emoji.text.EmojiCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.rovaind.academy.Controllers.BaseActivity;
import com.rovaind.academy.HomeActivity;
import com.rovaind.academy.MainActivity;
import com.rovaind.academy.R;
import com.rovaind.academy.Utils.ItemsUI;
import com.rovaind.academy.Utils.Toasty.Toasty;
import com.rovaind.academy.Utils.networksync.CheckInternetConnection;
import com.rovaind.academy.Utils.views.TextViewAr;
import com.rovaind.academy.manager.Groups;
import com.rovaind.academy.manager.SharedPrefManager;
import com.rovaind.academy.manager.response.UserResponse;
import com.rovaind.academy.model.User;
import com.rovaind.academy.retrofit.ApiInterface;
import com.rovaind.academy.retrofit.RetrofitClient;
import com.rovaind.academy.retrofit.UserAPIService;

import org.json.JSONObject;

import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FacebookLoginActivity extends BaseActivity {
    private static final String TAG = "Login" ;
    LottieAnimationView remembermeAnim;
    private EditText emailEditText;
    private EditText passwordEditText;
    private MaterialButton signinButton;
    private TextView signupText;

    private Toolbar toolbar;

    private CallbackManager callbackManager;
    private LoginButton loginButton;

    //
    private TextView appname;

    private int loginHistory = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_login);
        new CheckInternetConnection(this).checkConnection();

        Window w = getWindow();
        w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextViewAr appname = findViewById(R.id.appname);
        appname.setVisibility(View.VISIBLE);
        appname.setText(getResources().getString(R.string.sign_in));
        EmojiCompat.Config config = new BundledEmojiCompatConfig(this);
        EmojiCompat.init(config);
        //initializing vars
        emailEditText = findViewById(R.id.signin_email_editText);
        passwordEditText = findViewById(R.id.signin_password_editText);
        signinButton = findViewById(R.id.signin_button);
        signupText = findViewById(R.id.signup_textview);
        remembermeAnim = findViewById(R.id.signin_checkbox);

        FacebookSdk.sdkInitialize(getApplicationContext());
      //  AppEventsLogger.activateApp(this);

        loginHistory = getIntent().getIntExtra(ItemsUI.LOGIN_COURSE , 0);
        Typeface typeface = ResourcesCompat.getFont(this, R.font.blacklist);
        appname = findViewById(R.id.appname);
        appname.setTypeface(typeface);





        callbackManager = CallbackManager.Factory.create();
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        if(isLoggedIn)
        {
            AccessTokenTracker tokenTracker  = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                    if(currentAccessToken == null)
                    {
                        SharedPrefManager.getInstance(getApplicationContext()).logout();
                        finish();
                        startActivity(new Intent(FacebookLoginActivity.this, MainActivity.class));

                    }
            }
        };
            //onBackPressed();

        }
        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions("email" , "user_location" , "user_gender" , "user_photos" , "user_birthday" , "user_friends");
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signIn();
            }
        });
        // printKeyHash();

        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmailValid(emailEditText.getText().toString())){
                    if (passwordEditText.getText().length() > 6){
                        attemptLogin(emailEditText.getText().toString(), passwordEditText.getText().toString());
                    }else {
                        passwordEditText.setError(getResources().getString(R.string.password_should_longer_than_six));
                    }
                }else {
                    emailEditText.setError(getResources().getString(R.string.enter_valid_email));
                }
            }
        });


        remembermeAnim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                remembermeAnim.playAnimation();
                remembermeAnim.setRepeatMode(LottieDrawable.REVERSE);


            }
        });


        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FacebookLoginActivity.this, SignupActivity.class);
                /*
                View sharedView = findViewById(R.id.logo);
                String transName = "splash_anim";
                ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(FacebookLoginActivity.this, sharedView, transName);
                startActivity(intent, transitionActivityOptions.toBundle());

                 */
                startActivity(intent);
            }
        });

    }


    @Override
    public void onStart() {
        super.onStart();

        // Check auth on Activity start
        /*
        //TODO
        if (mAuth.getCurrentUser() != null) {
            onAuthSuccess(mAuth.getCurrentUser());
        }

         */
    }
    private void signIn() {

        if(!SharedPrefManager.getInstance(this).isLoggedIn()) {
            showProgressDialog();
        }
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {


                //handleFacebookAccessToken(loginResult.getAccessToken());
               // Toast.makeText(FacebookLoginActivity.this, "User ID :"  + loginResult.getAccessToken().getUserId() + "ImageURL" + loginResult.getAccessToken().ge, Toast.LENGTH_SHORT).show();

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.e("response: ", response + "");
                                try {
                                    String id = object.getString("id").toString();
                                    String email = object.getString("email").toString();
                                    String name = object.getString("name").toString();
                                    String profilePicUrl = "https://graph.facebook.com/" + loginResult.getAccessToken().getUserId() + "/picture?type=large";
                                    Log.d("imageFB", profilePicUrl);
                                    Log.d("FB_ID:", id);
                                    Log.d("name:", name);
                                    Log.d("email:", email);
                                  //  hideProgressDialog();
                                    final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                                    //checkFBLogin(id, email, name, profilePicUrl);
                                    signUp(email , id, name , profilePicUrl , timestamp.getTime());
                                   // handleFacebookAccessToken(loginResult.getAccessToken() , id, email, name, profilePicUrl);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    hideProgressDialog();
                                }
                                showProgressDialog();
                              //  finish();
                            }



                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday,friends,likes,hometown,education,work");
                request.setParameters(parameters);
                request.executeAsync();




            }

            @Override
            public void onCancel() {
               hideProgressDialog();
            }

            @Override
            public void onError(FacebookException error) {
                hideProgressDialog();
            }
        });
    }

    private void signUp(final String email , final String password , final String name , final String profilePic , final long  created_AT) {
        Log.d(TAG, "signUp");


        showProgressDialog();

        //building retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Defining retrofit api service
        final UserAPIService service = retrofit.create(UserAPIService.class);

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {


                //Defining the user object as we need to pass it with the call
                User user = new User(name, email, password , profilePic, "none" , task.getResult().getToken());

                //defining the call
                Call<UserResponse> call = service.createUser(
                        user.getName(),
                        user.getEmail(),
                        user.getPassword(),
                        user.getThumb_image(),
                        user.getGender(),
                        user.getGcmtoken()
                );

                //calling the api
                call.enqueue(new Callback<UserResponse>() {
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                        //hiding progress dialog
                        hideProgressDialog();

                        //displaying the message from the response as toast
                        if(response.body().getMessage().equals("This email already registered"))
                        {

                            SharedPrefManager.getInstance(getApplicationContext()).userLogin(response.body().getUser());
                            getData();

                        }

                        //if there is no error
                        if (!response.body().getError()) {
                            //starting profile activity
                            if(loginHistory == 1)
                            {
                                onBackPressed();
                                Log.d(TAG, "onResponse: onBackPressed " );
                            }
                            else
                            {
                                finish();
                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(response.body().getUser());
                                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {
                        hideProgressDialog();
                        Toasty.error(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT, true).show();
                        //Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    private void getData() {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final String data = prefs.getString("language", "ar"); //no id: default value
        ApiInterface  apiInterface = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);

        Call<Groups> call = apiInterface.getGroupMemberByID(SharedPrefManager.getInstance(getApplicationContext()).getUser().getId(),1);//(data.equals("en") ? 0 :1)

        call.enqueue(new Callback<Groups>() {
            @Override
            public void onResponse(Call<Groups> call, Response<Groups> response) {


                if(response.body() != null)
                {

                    for (int i = 0; i < response.body().getGroups().size() ; i++)
                    {
                        if(!response.body().getGroups().get(i).getCode().equals("") && response.body().getGroups().get(i).getCode()  != null) {
                            if(response.body().getGroups().get(i).getUsedCode().getMac().equals(getMacAddr())) {
                                SharedPrefManager.getInstance(getApplicationContext()).CoursesCodes(response.body().getGroups().get(i).getUsedCode());
                            }
                        }
                    }
                    if(loginHistory == 1)
                    {
                        onBackPressed();
                        Log.d(TAG, "onResponse: onBackPressed " );
                    }
                    else
                    {
                        finish();
                        startActivity(new Intent(FacebookLoginActivity.this, HomeActivity.class));
                        //Toast.makeText(FacebookLoginActivity.this, "" + getResources().getString(R.string.you_login_into), Toast.LENGTH_SHORT).show();
                        Toasty.success(FacebookLoginActivity.this, getResources().getString(R.string.you_login_into), Toast.LENGTH_SHORT, true).show();


                    }


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
    private void checkFBLogin(String id, String email, String name, String profilePicUrl) {
        Toasty.success(FacebookLoginActivity.this, "Welcome : " + name, Toast.LENGTH_SHORT, true).show();
        //Toast.makeText(FacebookLoginActivity.this, "Welcome : " + name , Toast.LENGTH_SHORT).show();

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode ,data);

    }


    private void printKeyHash() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature  : packageInfo.signatures)
            {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA");
                messageDigest.update(signature.toByteArray());
                Log.e("KEYHASH", Base64.encodeToString(messageDigest.digest(), Base64.DEFAULT));
            }
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }






    @Override
    protected void onResume() {
        super.onResume();
        Log.e("Login CheckPoint","LoginActivity resumed");
        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();

    }

    @Override
    protected void onStop () {
        super.onStop();
        Log.e("Login CheckPoint","LoginActivity stopped");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(loginHistory != 1)
        {
            Intent loginIntent = new Intent(FacebookLoginActivity.this, HomeActivity.class);
            startActivity(loginIntent);
            finish();
        }

    }
    private void attemptLogin(String email, String password){



        startActivity(new Intent(FacebookLoginActivity.this, HomeActivity.class));
        FacebookLoginActivity.this.finish();


    }

    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}
