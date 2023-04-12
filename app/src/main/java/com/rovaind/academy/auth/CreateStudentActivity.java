package com.rovaind.academy.auth;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.snackbar.Snackbar;
import com.rovaind.academy.Controllers.BaseActivity;
import com.rovaind.academy.HomeActivity;
import com.rovaind.academy.R;
import com.rovaind.academy.interfaces.OnEmailCheckListener;
import com.rovaind.academy.interfaces.OnUsernameCheckListener;

import java.io.IOException;

public class CreateStudentActivity extends BaseActivity {

    private ImageView profilePic;
    private EditText emailEditText;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText passwordConfirmEditText;
    private Button signupButton;
    private TextView signinTextView;
    private LottieAnimationView emailCheck;
    private LottieAnimationView usernameCheck;
    private LottieAnimationView passwordCheck;
    private Bitmap product_logo;

    private final int GALLERY = 1;
    private final int REQUEST_CODE_PERMISSIONS  = 1;
    private final int REQUEST_CODE_READ_STORAGE = 2;
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
                    Toast.makeText(CreateStudentActivity.this, getResources().getString(R.string.permisson_denied), Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_student);
        Window w = getWindow();
        w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        //initializing views
        profilePic = findViewById(R.id.signup_details_pic);
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    askForPermission(view);
                } else {
                    showChooser();
                }

            }
        });
        emailEditText = findViewById(R.id.signin_email_editText);
        usernameEditText = findViewById(R.id.signin_user_editText);
        passwordEditText = findViewById(R.id.signin_password_editText);
        passwordConfirmEditText = findViewById(R.id.signin_password_conf_editText);
        signupButton = findViewById(R.id.signin_button);
        signinTextView = findViewById(R.id.signup_textview);
        emailCheck = findViewById(R.id.signup_email_check);
        usernameCheck = findViewById(R.id.signup_username_check);
        passwordCheck = findViewById(R.id.signup_password_check);


        emailEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT){
                    emailCheck.setAnimation(R.raw.loading);
                    emailCheck.playAnimation();
                    if (isEmailValid(emailEditText.getText().toString())){
                        isCheckEmail(emailEditText.getText().toString(), new OnEmailCheckListener() {
                            @Override
                            public void onSuccess(boolean isRegistered) {
                                if (isRegistered){
                                    emailCheck.setAnimation(R.raw.error);
                                    emailCheck.playAnimation();
                                }else {
                                    emailCheck.setAnimation(R.raw.success_checkmark);
                                    emailCheck.playAnimation();
                                }
                            }
                        });
                    } else {
                        emailCheck.setAnimation(R.raw.error);
                        emailCheck.playAnimation();
                    }
                }
                return false;
            }
        });

        usernameEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT){
                    usernameCheck.setAnimation(R.raw.loading);
                    usernameCheck.playAnimation();
                    if (usernameEditText.getText().length() > 0){
                        isCheckUsername(usernameEditText.getText().toString(), new OnUsernameCheckListener() {
                            @Override
                            public void onSuccess(boolean isRegistered) {
                                if (isRegistered){
                                    usernameCheck.setAnimation(R.raw.error);
                                    usernameCheck.playAnimation();
                                }else {
                                    usernameCheck.setAnimation(R.raw.success_checkmark);
                                    usernameCheck.playAnimation();
                                }
                            }
                        });
                    }

                }
                return false;
            }
        });

        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    if (passwordEditText.getText().length() < 4){
                        passwordCheck.setAnimation(R.raw.error);
                        passwordCheck.playAnimation();
                    }else {
                        passwordCheck.setAnimation(R.raw.success_checkmark);
                        passwordCheck.playAnimation();
                    }
                }
                return false;
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (emailEditText.getText().length() != 0
                        && usernameEditText.getText().length() != 0
                        && passwordEditText.getText().length() != 0
                        && passwordEditText.getText().equals(passwordConfirmEditText.getText())){
                    attemptSignup(emailEditText.getText().toString(), usernameEditText.getText().toString(), passwordEditText.getText().toString());
                }
            }
        });

        signinTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateStudentActivity.this, FacebookLoginActivity.class);
                /*
                View sharedView = findViewById(R.id.logo);
                String transName = "splash_anim";
                ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(CreateStudentActivity.this, sharedView, transName);
                startActivity(intent, transitionActivityOptions.toBundle());

                 */
                startActivity(intent);
            }
        });

    }


    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void isCheckEmail(final String email,final OnEmailCheckListener listener){

        listener.onSuccess(true);

    }


    private void isCheckUsername(final String username, final OnUsernameCheckListener listener){
        if (!username.contains(" ")){

            listener.onSuccess(false);

        }else {

            listener.onSuccess(true);

        }

    }

    private void attemptSignup(String email, String username, String password){


        startActivity(new Intent(CreateStudentActivity.this, HomeActivity.class));
        CreateStudentActivity.this.finish();
    }

    /*
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CreateCustomerActivity.this, LoginActivity.class);
        View sharedView = findViewById(R.id.logo);
        String transName = "splash_anim";
        ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(CreateCustomerActivity.this, sharedView, transName);
        startActivity(intent, transitionActivityOptions.toBundle());
        super.onBackPressed();
    }

     */
    public Bitmap getProduct_logo() {
        return product_logo;
    }

    public void setProduct_logo(Bitmap product_logo) {
        this.product_logo = product_logo;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (resultCode == RESULT_CANCELED) {
            return;
        }



        if (requestCode == GALLERY) {
            if (resultData != null) {
                Uri contentURI = resultData.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    profilePic.setImageBitmap(bitmap);
                    setProduct_logo(bitmap);
                    profilePic.setVisibility(View.VISIBLE);


                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, getResources().getString(R.string.failed), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    /**
     *  Runtime Permission
     */
    private void askForPermission(View view) {
        if ((ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) +
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE))
                != PackageManager.PERMISSION_GRANTED) {
            /* Ask for permission */
            // need to request permission
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                Snackbar.make(view.findViewById(android.R.id.content),
                        "Please grant permissions to write data in sdcard",
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        v -> ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                REQUEST_CODE_PERMISSIONS)).show();
            } else {
                /* Request for permission */
                ActivityCompat.requestPermissions(this,
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
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }
}