package com.rovaind.academy.retrofit;

import com.rovaind.academy.manager.Ads;
import com.rovaind.academy.manager.Categories;
import com.rovaind.academy.manager.Centers;
import com.rovaind.academy.manager.Cities;
import com.rovaind.academy.manager.Codes;
import com.rovaind.academy.manager.Comments;
import com.rovaind.academy.manager.CommentsPosts;
import com.rovaind.academy.manager.Courses;
import com.rovaind.academy.manager.Goverments;
import com.rovaind.academy.manager.Groups;
import com.rovaind.academy.manager.Instructors;
import com.rovaind.academy.manager.LectureSections;
import com.rovaind.academy.manager.Lectures;
import com.rovaind.academy.manager.Lessons;
import com.rovaind.academy.manager.PostBackgrounds;
import com.rovaind.academy.manager.Posts;
import com.rovaind.academy.manager.Questions;
import com.rovaind.academy.manager.Sections;
import com.rovaind.academy.manager.response.CodeResponse;
import com.rovaind.academy.manager.response.CommentPostResponse;
import com.rovaind.academy.manager.response.CommentResponse;
import com.rovaind.academy.manager.response.CourseResponse;
import com.rovaind.academy.manager.response.GroupResponse;
import com.rovaind.academy.manager.response.ImageResponse;
import com.rovaind.academy.manager.response.InstructorResponse;
import com.rovaind.academy.manager.response.LikePostResponse;
import com.rovaind.academy.manager.response.LikeResponse;
import com.rovaind.academy.manager.response.PostImageResponse;
import com.rovaind.academy.manager.response.PostResponse;
import com.rovaind.academy.manager.response.UserResponse;
import com.rovaind.academy.model.Code;
import com.rovaind.academy.model.Instructor;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {

    //updating coe
    @FormUrlEncoded
    @POST("updatecode")
    Call<CodeResponse> updateCode(
            @Field("mac") String mac,
            @Field("code") String code,
            @Field("used_date") long used_date,
            @Field("end_date") long end_date,
            @Field("usr_id") int usr_id,
            @Field("course_id") int course_id
    );

    @FormUrlEncoded
    @POST("storecode")
    Call<CodeResponse> codeVerfication(
            @Field("mac") String mac,
            @Field("code") String code,
            @Field("course_id") int course_id,
            @Field("code_type") int code_type
    );

    //getting courses for owners
    @GET("getcode/{code_serial}")
    Call<CodeResponse> getCode(
            @Path("code_serial") String code_serial
    );

    @GET("courses/{lanugage}")
    Call<Courses> getAllCourses(@Path("lanugage") int lanugage);

    @GET("centers/{lanugage}")
    Call<Centers> getAllCenters(@Path("lanugage") int lanugage);

    @GET("lectures/{lanugage}")
    Call<Lectures> getAllLectures(@Path("lanugage") int lanugage);

    @GET("instructors/{lanugage}")
    Call<Instructors> getAllInstructors(@Path("lanugage") int lanugage);

    //getting a lectures by course
    @GET("lecturesbyid/{course_id}{lanugage}")
    Call<Lectures> getLecturesByID(@Path("course_id") int course_id,
                               @Path("lanugage") int lanugage);

    //getting a lectures by courselesson
    @GET("lecturesectionsbyid/{course_id}{type_c}{lanugage}")
    Call<LectureSections> getLecturesLessonByID(@Path("course_id") int course_id,
                                                @Path("type_c") int type_c,
                                                @Path("lanugage") int lanugage);

    //getting courses for owners
    @GET("coursesowners/{merchant_id}{lanugage}")
    Call<Courses> getCoursesByOwner(
            @Path("merchant_id") int merchant_id,
            @Path("lanugage") int lanugage
    );
    //getting categories
    @GET("maincategories/{id}{lanugage}")
    Call<Categories> getMainCategories(@Path("id") int id,
                                       @Path("lanugage") int lanugage);

    //getting categories
    @GET("subcategories/{id}{sort}{lanugage}")
    Call<Categories> getSubCategories(@Path("id") int id,
                                       @Path("sort") int sort,
                                       @Path("lanugage") int lanugage);

    //getting courses for subject
    @GET("coursessubject/{subid}{section}{type}{lanugage}")
    Call<Courses> getCoursesBySubject(
            @Path("subid") String subid,
            @Path("section") int section,
            @Path("type") int type,
            @Path("lanugage") int lanugage
    );
    //getting courses for trader
    @GET("coursessort/{class_id}{page_number}{sort_type}{lanugage}")
    Call<Courses> getCoursesBySort(
            @Path("class_id") int class_id,
            @Path("page_number") int page_number,
            @Path("sort_type") int sort_typess,
            @Path("lanugage") int lanugage
    );

    //getting courses main
    @GET("coursesmain/{cat_gparent_id}{page_number}{sort_type}{lanugage}")
    Call<Courses> getCoursesMain(
            @Path("cat_gparent_id") int cat_gparent_id,
            @Path("page_number") int page_number,
            @Path("sort_type") int sort_type,
            @Path("lanugage") int lanugage
    );


    //getting courses search
    @GET("coursessearch/{course_name}{page_number}{sort_type}{lanugage}")
    Call<Courses> getCoursesSearch(
            @Path("course_name") String course_name,
            @Path("page_number") int page_number,
            @Path("sort_type") int sort_type,
            @Path("lanugage") int lanugage
    );

    //getting instructors
    @GET("instructorssort/{level_id}{page_number}{sort_type}{lanugage}")
    Call<Instructors> getInstructorsBySort(
            @Path("level_id") int level_id,
            @Path("page_number") int page_number,
            @Path("sort_type") int sort_typess,
            @Path("lanugage") int lanugage
    );

    //getting instructorbyid
    @GET("instructorbyid/{id}{lanugage}")
    Call<Instructor> getInstructorsbyid(
            @Path("id") int id,
            @Path("lanugage") int lanugage
    );
    //getting instructors main
    @GET("instructorsmain/{cat_gparent_id}{page_number}{sort_type}{lanugage}")
    Call<Instructors> getInstructorsMain(
            @Path("cat_gparent_id") int cat_gparent_id,
            @Path("page_number") int page_number,
            @Path("sort_type") int sort_type,
            @Path("lanugage") int lanugage
    );

    //getting instructors search
    @GET("instructorssearch/{instructor_name}{page_number}{sort_type}{lanugage}")
    Call<Instructors> getInstructorsSearch(
            @Path("instructor_name") String instructor_name,
            @Path("page_number") int page_number,
            @Path("sort_type") int sort_type,
            @Path("lanugage") int lanugage
    );

    //getting ads
    @GET("getads/{end_at}")
    Call<Ads> getAds(@Path("end_at") int end_at);





    //getting a lesson by course id
    @GET("quizlessons/{course_id}")
    Call<Lessons> getLessonByID(@Path("course_id") int course_id);

    //getting a section by lesson_Id
    @GET("quizsections/{lesson_Id}")
    Call<Sections> getSectionByID(@Path("lesson_Id") int lesson_Id);

    //getting a question by lesson_Id
    @GET("quizquestions/{lesson_Id}")
    Call<Questions> getQuestionByID(@Path("lesson_Id") int lesson_Id);




    //creating new comment
    @FormUrlEncoded
    @POST("createcomment")
    Call<CommentResponse> createComment(
            @Field("usr_id") int usr_id,
            @Field("parent_id") int parent_id,
            @Field("content") String content,
            @Field("lecture_id") int lecture_id,
            @Field("course_id") int course_id,
            @Field("rate") int rate,
            @Field("created_at") long created_at
    );
    //creating new comment
    @FormUrlEncoded
    @POST("createcommentpost")
    Call<CommentPostResponse> createCommentPost(
            @Field("usr_id") int usr_id,
            @Field("parent_id") int parent_id,
            @Field("content") String content,
            @Field("course_id") int course_id,
            @Field("post_id") int post_id,
            @Field("rate") int rate,
            @Field("created_at") long created_at
    );


    //getting Comments
    @GET("comments/{id}{parent}{page_number}")
    Call<Comments> getComments(@Path("id") int id,
                               @Path("parent") int parent,
                               @Path("page_number") int page_number);

    //getting Comments
    @GET("commentsposts/{id}{parent}{page_number}")
    Call<CommentsPosts> getCommentsPosts(@Path("id") int id,
                                         @Path("parent") int parent,
                                         @Path("page_number") int page_number);
    //getting Comments
    @GET("replycomments/{id}{page_number}")
    Call<Comments> getReplyComments(@Path("id") int id,

                               @Path("page_number") int page_number);

    //getting Comments
    @GET("allcomments/{id}{page_number}")
    Call<Comments> getAllComments(@Path("id") int id,
                                  @Path("page_number") int page_number);

    //updating productRate
    @FormUrlEncoded
    @POST("courserate/{id}")
    Call<CourseResponse> updateRate(
            @Path("id") int id,
            @Field("owner_id") int owner_id,
            @Field("usr_id") int usr_id,
            @Field("rate") int rate,
            @Field("ratecount") int ratecount

    );

    //updating courseviewcount
    @FormUrlEncoded
    @POST("courseviewcount/{id}")
    Call<CourseResponse> updateCourseView(
            @Path("id") int id,
            @Field("count") int count

    );
    //updating courseviewcount
    @FormUrlEncoded
    @POST("postviewcount/{id}")
    Call<PostResponse> updatePostView(
            @Path("id") int id,
            @Field("count") int count

    );

    //updating courseenrolled
    @FormUrlEncoded
    @POST("courseenrolledcount/{id}")
    Call<CourseResponse> updateCourseenrolled(
            @Path("id") int id,
            @Field("owner_id") int owner_id

    );

    //creating new comment
    @FormUrlEncoded
    @POST("createlike")
    Call<LikeResponse> createLike(
            @Field("usr_id") int usr_id,
            @Field("comment_id") int comment_id,
            @Field("parent_id") int parent_id,
            @Field("lecture_id") int lecture_id,
            @Field("course_id") int course_id,
            @Field("created_at") long created_at
    );

    //creating new comment
    @FormUrlEncoded
    @POST("createpostlike")
    Call<LikePostResponse> createPostLike(
            @Field("usr_id") int usr_id,
            @Field("post_id") int post_id,
            @Field("parent_id") int parent_id,
            @Field("course_id") int course_id,
            @Field("created_at") long created_at
    );


    //getting posts
    @GET("courseposts/{id}{page_number}")
    Call<Posts> getPosts(@Path("id") int id,
                         @Path("page_number") int page_number);
    //getting posts
    @GET("coursepublicposts/{id}{page_number}")
    Call<Posts> getPublicPosts(@Path("id") int id,
                         @Path("page_number") int page_number);
    //getting posts
    @GET("courseteacherposts/{id}{page_number}")
    Call<Posts> getTeacherPosts(@Path("id") int id,
                         @Path("page_number") int page_number);


    @GET("postbackground")
    Call<PostBackgrounds> getBackGrounds();

    @FormUrlEncoded
    @POST("uploadpostimage.php")
    Call<ImageResponse> setPostImage(
            @Field("name") String name,
            @Field("image") String image,
            @Field("pdt_id") long pdt_id
    );

    //creating new comment
    @FormUrlEncoded
    @POST("createpost")
    Call<PostResponse> createPost(
            @Field("usr_id") int usr_id,
            @Field("course_id") int course_id,
            @Field("class_id") int class_id,
            @Field("owner_id") int owner_id,
            @Field("content") String content,
            @Field("Url") String Url,
            @Field("back_img") String back_img,
            @Field("txt_color") String txt_color,
            @Field("post_type") int post_type,
            @Field("created_at") long created_at
    );

    //creating new product
    @FormUrlEncoded
    @POST("createpostimage")
    Call<PostImageResponse> createpostImage(

            @Field("post_id") int post_id,
            @Field("thumb_image") String thumb_image,
            @Field("content") String content,
            @Field("created_at") long created_at

    );

    //updating user gcmToken
    @FormUrlEncoded
    @POST("storegcmtoken/{id}")
    Call<UserResponse> updateGcmToken(
            @Path("id") int id,
            @Field("gcmtoken") String gcmtoken
    );


    //getting a groupmembers by id
    @GET("groups/{course_id}")
    Call<Groups> getGroupMemberByID(@Path("course_id") int course_id);

    //getting a groupmembers by id
    @GET("groupsbymember/{user_id}{lanugage}")
    Call<Groups> getGroupMemberByID(

            @Path("user_id") int user_id,
            @Path("lanugage") int lanugage);

    @FormUrlEncoded
    @POST("groupcheck")
    Call<GroupResponse> CheckGroup(
            @Field("user_id") String user_id,
            @Field("course_id") String course_id
    );

    //creating new product
    @FormUrlEncoded
    @POST("memberjoingroup")
    Call<GroupResponse> MemberJoinGroup(

            @Field("course_id") int course_id,
            @Field("usr_id") int usr_id,
            @Field("email") String email,
            @Field("created_at") long created_at

    );

    @GET("goverments")
    Call<Goverments> getAllGoverments();

    @GET("cities/{gov_id}")
    Call<Cities> getAllCities(@Path("gov_id") int gov_id);

    @FormUrlEncoded
    @POST("uploadproductimage.php")
    Call<String> setProductImage(
            @Field("name") String name,
            @Field("image") String image,
            @Field("pdt_id") long pdt_id
    );

    //creating new instructor
    @FormUrlEncoded
    @POST("createinstructor")
    Call<InstructorResponse> createInstructor(
            @Field("usr_id") int usr_id,
            @Field("center_id") int center_id,
            @Field("category_id") int category_id,
            @Field("level_id") int level_id,
            @Field("subject_id") String subject_id,
            @Field("class_id") int class_id,
            @Field("total_students") int total_students,
            @Field("rate") int rate,
            @Field("ratecount") int ratecount,
            @Field("phone") int phone,
            @Field("age") String age,
            @Field("school") String school,
            @Field("city_code") int city_code,
            @Field("goverment_code") int goverment_code,
            @Field("social_facebook") String social_facebook,
            @Field("videoUrlyou") String videoUrlyou,
            @Field("sponsored") int sponsored,
            @Field("created_at") Long created_at,
            @Field("view_count") int view_count

    );

    //creating new product
    @FormUrlEncoded
    @POST("createinstructordetails")
    Call<InstructorResponse> createInstructorDetails(

            @Field("instructor_id") int instructor_id,
            @Field("instructor_name") String instructor_name,
            @Field("instructor_position") String instructor_position,
            @Field("instructor_description") String instructor_description,
            @Field("thumb_image") String thumb_image,
            @Field("language") int language

    );

}
