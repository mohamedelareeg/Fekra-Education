package com.rovaind.academy.retrofit;


import com.rovaind.academy.manager.Users;
import com.rovaind.academy.manager.response.UserResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserAPIService {

    @FormUrlEncoded
    @POST("register")
    Call<UserResponse> createUser(
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password,
            @Field("thumb_image") String thumb_image,
            @Field("gender") String gender,
            @Field("gcmtoken") String gcmtoken

    );
    @FormUrlEncoded
    @POST("register")
    Call<UserResponse> createUserWP(
            @Field("username") String username,
            @Field("email") String email,
            @Field("password") String password
    );

    @POST("register")
    Call<UserResponse> createUserWPBody(
            @Query("rest_route") String url,
            @Body RequestBody params
    );
    @FormUrlEncoded
    @POST("login")
    Call<UserResponse> userLogin(
            @Field("email") String email,
            @Field("password") String password
    );
    @FormUrlEncoded
    @POST("getid")
    Call<UserResponse> usergetID(
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST("login")
    Call<UserResponse> userLoginAuth(
            @Field("user_email") String user_email,
            @Field("user_pass") String user_pass
    );

    @GET("users")
    Call<Users> getUsers();

    //updating user
    @FormUrlEncoded
    @POST("update/{id}")
    Call<UserResponse> updateUser(
            @Path("id") int id,
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password,
            @Field("gender") String gender
    );

    //updating user gcmToken
    @FormUrlEncoded
    @POST("storegcmtoken/{id}")
    Call<UserResponse> updateGcmToken(
            @Path("id") int id,
            @Field("gcmtoken") String gcmtoken
    );



}