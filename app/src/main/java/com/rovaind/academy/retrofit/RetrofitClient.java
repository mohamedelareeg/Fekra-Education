package com.rovaind.academy.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {


     static Retrofit retrofit;
    public static final String APP_URL            = "http://5.9.230.241";
    public static final String BASE_URL = APP_URL + "/admin_panel/ecourse/public/";//95.217.91.196
    public static final String BASE_INCLUDES = APP_URL + "/admin_panel/ecourse/includes/";
    public static final String BASE_VIDEO_URL = APP_URL + "/admin_panel/ecourse/public/Videos/";//95.217.91.196
    public static final String BASE_MAIN = APP_URL + "/admin_panel/admin/";//95.217.91.196
    public static final String BASE_TeacherImage_URL = APP_URL + "/admin_panel/admin/images_owner/";//95.217.91.196
    public static final String BASE_CourseImage_URL = APP_URL + "/admin_panel/admin/images_course/";//95.217.91.196
    public static final String BASE_CategoryImage_URL = APP_URL + "/admin_panel/admin/images_category/";//95.217.91.196
    public static final String BASE_CenterImage_URL = APP_URL + "/admin_panel/admin/images_center/";//95.217.91.196
    public static final String BASE_ADSImage_URL = APP_URL + "/admin_panel/admin/images_ads/";//95.217.91.196
    public static final String BASE_SECTION_URL = APP_URL + "/admin_panel/admin/images_section/";//95.217.91.196
    public static final String BASE_QUESTION_URL = APP_URL + "/admin_panel/admin/images_questions/";//95.217.91.196
    public static final String BASE_POST_URL = APP_URL + "/admin_panel/admin/images_post/";//95.217.91.196
    public static final String BASE_POST_IMAGE_URL = APP_URL + "/admin_panel/admin/images_post_txt/";//95.217.91.196
    public static final String BASE_PDF_URL = APP_URL + "/admin_panel/admin/pdf_url/";//95.217.91.196
    public static final String BASE_VIDEO_GOOGLE_URL = "https://drive.google.com/file/d/";

    public static final String INTRO_LINK = "intro.mp4";//95.217.91.196
    public static final String URL_REGISTER_DEVICE = BASE_INCLUDES + "RegisterDevice.php";
    public static final String URL_SEND_SINGLE_PUSH = BASE_INCLUDES +"sendSinglePush.php";
    public static final String URL_SEND_MULTIPLE_PUSH = BASE_INCLUDES +"sendMultiplePush.php";
    public static final String URL_FETCH_DEVICES = BASE_INCLUDES +"GetRegisteredDevices.php";
    public static Retrofit getRetrofitInstance(){

        if(retrofit == null){

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient oauthsignature = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .build();
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create()) //important
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(oauthsignature)
                    .build();
        }
        return retrofit;
    }
    public static Retrofit retrofitWrite()
    {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();


        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
    public static Retrofit retrofitRead()
    {

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }



}
