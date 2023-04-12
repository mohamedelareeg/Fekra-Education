package com.rovaind.academy.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.rovaind.academy.model.Code;
import com.rovaind.academy.model.Course;
import com.rovaind.academy.model.User;


public class SharedPrefManager {

    private static SharedPrefManager mInstance;
    private static Context mCtx;
    // Shared Preferences
    SharedPreferences pref;
    private static final String SHARED_PREF_NAME = "ecourseguest";

    private static final String KEY_GUEST_VISIT = "guestvisit";
    private static final String KEY_CUSTOMER_BILLING = "guestcourses_";
    private static final String KEY_CUSTOMER_EXTRA_BILLING = "guestcoursesextra_";
    private static final String KEY_CUSTOMER_ENDTIME = "guestcourses_date_";
    private static final String KEY_CUSTOMEREXTRA_ENDTIME = "guestcoursesextra_date_";
    private static final String KEY_CUSTOMER_COURSES_COUNT = "guestcourses_count";
    private static final String KEY_SUBSCRIBED_GROUP = "subgroup_";

    private static final String KEY_USER_ID = "keyuserid";
    private static final String KEY_USER_NAME = "keyusername";
    private static final String KEY_USER_EMAIL = "keyuseremail";
    private static final String KEY_USER_THUMB = "keyuserthumb";
    private static final String KEY_USER_GENDER = "keyusergender";
    private static final String KEY_USER_GCM = "keyusergcm";

    private static final String KEY_SELECTED_CLASS = "keyselectedclass";
    private static final String KEY_SELECTED_LEVEL = "keyselectedlevel";
    public SharedPrefManager(Context context) {
        mCtx = context;
        pref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public boolean userToken(String token) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_GCM, token);
        editor.apply();
        return true;
    }

    public boolean userLogin(User user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_USER_ID, user.getId());
        editor.putString(KEY_USER_NAME, user.getName());
        editor.putString(KEY_USER_EMAIL, user.getEmail());
        editor.putString(KEY_USER_THUMB, user.getThumb_image());
        editor.putString(KEY_USER_GENDER, user.getGender());
        editor.putString(KEY_USER_GCM, user.getGcmtoken());
        editor.apply();
        return true;
    }

    public boolean SubcribeGroup(Course course ) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(course);
        editor.putString(KEY_SUBSCRIBED_GROUP + course.getId()  , json);
        editor.apply();
        return true;
    }
    public boolean CoursesCodesExtra(Code customer ) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(customer);
        editor.putString(KEY_CUSTOMER_EXTRA_BILLING + customer.getCourseId()  , json);
        editor.putLong(KEY_CUSTOMEREXTRA_ENDTIME + customer.getCourseId() , customer.getEndDate());
        editor.putInt(KEY_CUSTOMER_COURSES_COUNT , SharedPrefManager.getInstance(mCtx).getCourseCount() + 1);
        editor.apply();
        return true;
    }
    public boolean CoursesCodes(Code customer ) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(customer);
        editor.putString(KEY_CUSTOMER_BILLING + customer.getCourseId()  , json);
        editor.putLong(KEY_CUSTOMER_ENDTIME + customer.getCourseId() , customer.getEndDate());
        editor.putInt(KEY_CUSTOMER_COURSES_COUNT , SharedPrefManager.getInstance(mCtx).getCourseCount() + 1);
        editor.apply();
        return true;
    }
    public boolean GuestVisit( ) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(KEY_GUEST_VISIT  , 1);
        editor.apply();
        return true;
    }


    public boolean SelectedClass(int Class) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(KEY_SELECTED_CLASS  , Class);
        editor.apply();
        return true;
    }
    public boolean SelectedLevel(int Level) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(KEY_SELECTED_LEVEL , Level);
        editor.apply();
        return true;
    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        if (sharedPreferences.getString(KEY_USER_EMAIL, null) != null)
            return true;
        return false;
    }

    public User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getInt(KEY_USER_ID, 0),
                sharedPreferences.getString(KEY_USER_NAME, null),
                sharedPreferences.getString(KEY_USER_EMAIL, null),
                sharedPreferences.getString(KEY_USER_THUMB, null),
                sharedPreferences.getString(KEY_USER_GENDER, null),
                sharedPreferences.getString(KEY_USER_GCM, null)
        );
    }

    public Code CheckSubcribedGroup(int course_id) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(KEY_SUBSCRIBED_GROUP + course_id, null );
        Code customer = gson.fromJson(json, Code.class);
        return customer;
    }
    public Code getCoursesCodes(int course_id) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(KEY_CUSTOMER_BILLING + course_id, null );
        Code customer = gson.fromJson(json, Code.class);
        return customer;
    }
    public Code getCoursesExtraCodes(int course_id) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(KEY_CUSTOMER_EXTRA_BILLING + course_id, null );
        Code customer = gson.fromJson(json, Code.class);
        return customer;
    }

    public long getEndTime(int course_id) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getLong(KEY_CUSTOMER_ENDTIME + course_id, 0);
    }
    public long getEndTimeExtra(int course_id) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getLong(KEY_CUSTOMEREXTRA_ENDTIME + course_id, 0);
    }
    public int getGuestVisit() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_GUEST_VISIT, 0);
    }

    public int getCourseCount() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_CUSTOMER_COURSES_COUNT, 0);
    }
    public int getSelectedClass() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_SELECTED_CLASS, 0);
    }

    public int getSelectedLevel() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_SELECTED_LEVEL, 0);
    }
    public boolean clearcourse(int course_id) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_CUSTOMER_BILLING + course_id);
        editor.remove(KEY_CUSTOMER_ENDTIME + course_id);
        editor.apply();
        return true;
    }
    public boolean clearcourseextra(int course_id) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_CUSTOMER_EXTRA_BILLING + course_id);
        editor.remove(KEY_CUSTOMEREXTRA_ENDTIME + course_id);
        editor.apply();
        return true;
    }
    //this method will fetch the device token from shared preferences
    public String getDeviceToken(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(KEY_USER_GCM, null);
    }
    public boolean logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }
}
