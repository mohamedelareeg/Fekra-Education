package com.rovaind.academy;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.rovaind.academy.Controllers.BaseActivity;
import com.rovaind.academy.Controllers.CategorySelectionActivity;
import com.rovaind.academy.Controllers.SliderActivity;
import com.rovaind.academy.manager.SharedPrefManager;
import com.rovaind.academy.manager.fcm.FcmVolley;
import com.rovaind.academy.retrofit.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends BaseActivity {

    private static int TIME_OUT = 3000; //Time to launch the another activity
    private static final String TAG = "MainActivity" ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.rovaind.elmohader",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        Typeface typeface = ResourcesCompat.getFont(this, R.font.jomhuriaregular);

        TextView appname= findViewById(R.id.appname);
        appname.setTypeface(typeface);

        YoYo.with(Techniques.Bounce)
                .duration(7000)
                .playOn(findViewById(R.id.logo));

        YoYo.with(Techniques.FadeInUp)
                .duration(5000)
                .playOn(findViewById(R.id.appname));

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                sendtoHome();
                //sendToken();
            }
        }, TIME_OUT);


    }
    private void sendtoHome() {

        finish();
        if(SharedPrefManager.getInstance(getApplicationContext()).getGuestVisit() != 1) {
            startActivity(new Intent(this, SliderActivity.class));
        }
        else
        {
            startActivity(new Intent(this, CategorySelectionActivity.class));
        }



    }

    public void sendToken() {
        showProgressDialog();

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {

               // final String token = SharedPreference.getInstance(this).getDeviceToken();

                final String token = task.getResult().getToken();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, RetrofitClient.URL_REGISTER_DEVICE,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                hideProgressDialog();
                                try {
                                    JSONObject obj = new JSONObject(response);
                                    Toast.makeText(MainActivity.this, obj.getString("message"), Toast.LENGTH_LONG).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                hideProgressDialog();
                                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }) {

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("email", "mohaa.coder@yahoo.com");
                        params.put("token", token);
                        return params;
                    }
                };
                FcmVolley.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

}

