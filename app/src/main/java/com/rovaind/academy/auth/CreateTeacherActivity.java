package com.rovaind.academy.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.rovaind.academy.Controllers.BaseActivity;
import com.rovaind.academy.HomeActivity;
import com.rovaind.academy.R;
import com.rovaind.academy.Utils.Toasty.Toasty;
import com.rovaind.academy.Utils.networksync.CheckInternetConnection;
import com.rovaind.academy.Utils.views.TextViewAr;
import com.rovaind.academy.manager.Categories;
import com.rovaind.academy.manager.Cities;
import com.rovaind.academy.manager.Goverments;
import com.rovaind.academy.manager.SharedPrefManager;
import com.rovaind.academy.manager.response.InstructorResponse;
import com.rovaind.academy.manager.response.UserResponse;
import com.rovaind.academy.model.Category;
import com.rovaind.academy.model.City;
import com.rovaind.academy.model.Goverment;
import com.rovaind.academy.model.Instructor;
import com.rovaind.academy.model.User;
import com.rovaind.academy.retrofit.ApiInterface;
import com.rovaind.academy.retrofit.RetrofitClient;
import com.rovaind.academy.retrofit.UserAPIService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateTeacherActivity extends BaseActivity {

    private static final String TAG = CreateTeacherActivity.class.getSimpleName();
    private AutoCompleteTextView main_ip;
    private Spinner spinner_main_ip;
    private ArrayAdapter<String> adapter_main;

    private AutoCompleteTextView sub_ip;
    private Spinner spinner_sub_ip;
    private ArrayAdapter<String> adapter_sub;

    private AutoCompleteTextView level_ip;
    private Spinner spinner_level_ip;
    private ArrayAdapter<String> adapter_level;

    private AutoCompleteTextView class_ip;
    private Spinner spinner_class_ip;
    private ArrayAdapter<String> adapter_class;

    private AutoCompleteTextView subject_ip;
    private Spinner spinner_subject_ip;
    private ArrayAdapter<String> adapter_subject;

    //
    private Bitmap TeacherIMG;
    private ImageView setup_img;
    private TextView addTeacher;


    private com.rengwuxian.materialedittext.MaterialEditText instructoremail;
    private com.rengwuxian.materialedittext.MaterialEditText instructorpassword;
    private com.rengwuxian.materialedittext.MaterialEditText instructorconfirmpassword;


    private com.rengwuxian.materialedittext.MaterialEditText instructor_name;
    private com.rengwuxian.materialedittext.MaterialEditText instructordescription;
    private com.rengwuxian.materialedittext.MaterialEditText instructorposition;
    private com.rengwuxian.materialedittext.MaterialEditText instructor_age;
    private com.rengwuxian.materialedittext.MaterialEditText instructor_phone;
    private com.rengwuxian.materialedittext.MaterialEditText instructor_school;


    //ProgressDialog
    private ProgressDialog mLoginProgress;

    private final int GALLERY = 1;
    private final int REQUEST_CODE_PERMISSIONS  = 1;
    private final int REQUEST_CODE_READ_STORAGE = 2;
        private int GovermentID , CityID , LevelID , ClassID , SubjectID;
    Toolbar toolbar;
    private String check;
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
                    Toast.makeText(this, getResources().getString(R.string.permisson_denied), Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_create_teacher);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Hide Title
        TextViewAr appname = findViewById(R.id.appname);
        appname.setText(getResources().getString(R.string.create_new_teacher));

        // Back Button
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();
        //set auto complete
        setup_img = findViewById(R.id.teacherIMG);
        instructoremail = findViewById(R.id.instructoremail);
        instructorpassword = findViewById(R.id.instructorpassword);
        instructorconfirmpassword = findViewById(R.id.instructorconfirmpassword);

        instructor_name = findViewById(R.id.instructor_name);
        instructordescription = findViewById(R.id.instructordescription);
        instructorposition = findViewById(R.id.instructorposition);
        instructor_age = findViewById(R.id.instructor_age);
        instructor_phone = findViewById(R.id.instructor_phone);
        instructor_school = findViewById(R.id.instructor_school);
        mLoginProgress = new ProgressDialog(this);

        addTeacher = findViewById(R.id.addTeacher);

        setup_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(galleryIntent, GALLERY);
            }
        });
        init();
    }

    private void init() {
        main_ip = (AutoCompleteTextView) findViewById(R.id.main_ip);
        sub_ip = (AutoCompleteTextView) findViewById(R.id.sub_ip);

        level_ip = (AutoCompleteTextView) findViewById(R.id.level_ip);
        class_ip = (AutoCompleteTextView) findViewById(R.id.class_ip);
        subject_ip = (AutoCompleteTextView) findViewById(R.id.subject_ip);

        //
        spinner_main_ip = (Spinner) findViewById(R.id.spinner_main_ip);
        spinner_sub_ip = (Spinner) findViewById(R.id.spinner_sub_ip);

        spinner_level_ip = (Spinner) findViewById(R.id.spinner_level_ip);
        spinner_class_ip = (Spinner) findViewById(R.id.spinner_class_ip);
        spinner_subject_ip = (Spinner) findViewById(R.id.spinner_subject_ip);

        getMainCategory();
        getLevelCategory();

        addTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validateEmail() && validatePass()){

                    String email = instructoremail.getText().toString().trim();
                    String password = instructorpassword.getText().toString().trim();
                    String confpassword = instructorconfirmpassword.getText().toString().trim();

                    Toast.makeText(CreateTeacherActivity.this, "4", Toast.LENGTH_SHORT).show();
                    if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {

                        if(password.equals(confpassword)) {
                            Toast.makeText(CreateTeacherActivity.this, "3", Toast.LENGTH_SHORT).show();
                            attemptSignup(instructoremail.getText().toString(), instructorpassword.getText().toString());
                        }
                    }
                    //Validation Success
                    //convertBitmapToString(profilePicture);

                }

            }
        });
    }

    private void attemptSignup(String email, String password) {

        final String name = instructor_name.getText().toString();//name
        final String desc = instructordescription.getText().toString();
        final String position = instructorposition.getText().toString();
        final String age = instructor_age.getText().toString();
        final String phone = instructor_phone.getText().toString();
        final String school = instructor_school.getText().toString();

        if (!TextUtils.isEmpty(name)  &&!TextUtils.isEmpty(desc) &&!TextUtils.isEmpty(position) &&!TextUtils.isEmpty(age)  &&!TextUtils.isEmpty(phone) &&!TextUtils.isEmpty(school) && getTeacherIMG()  != null) {

            showProgressDialog();
            signUp(getTeacherIMG() , name  ,desc  , position   , age , phone , school ,email  , password , "Male");


        }
    }
    private void signUp(Bitmap bitmap ,final String name   , final String desc    , final String Position , final String Age , final String Phone , final String School ,final String email , final String password , final String gender) {
        Log.d(TAG, "signUp");



        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        String imgname = String.valueOf(Calendar.getInstance().getTimeInMillis());


        ApiInterface api = RetrofitClient.retrofitWrite().create(ApiInterface.class);

        Log.d("helloo",encodedImage +"   >>"+imgname);
        Log.d("imggggg","   >>"+imgname);
        Call<String> call_ = api.setProductImage(imgname,encodedImage , generateRandom(12));

        call_.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call_, Response<String> response) {
                Log.i("Responsestring", response.body().toString());
                //Toast.makeText()
                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        Log.i("Upload", response.body().toString());

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
                                User user = new User(name, email, password, imgname + ".JPG", "none", task.getResult().getToken());

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
                                        if (response.body().getMessage().equals("This email already registered")) {

                                            SharedPrefManager.getInstance(getApplicationContext()).userLogin(response.body().getUser());

                                        }

                                        //if there is no error
                                        if (!response.body().getError()) {
                                            uploadImage(imgname, name, desc, Position, Age, Phone, School, response.body().getUser());

                                            //starting profile activity
                                            finish();
                                            SharedPrefManager.getInstance(getApplicationContext()).userLogin(response.body().getUser());
                                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));

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
                    } else {
                        Log.i("Upload", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });


    }
    public static long generateRandom(int length) {
        Random random = new Random();
        char[] digits = new char[length];
        digits[0] = (char) (random.nextInt(9) + '1');
        for (int i = 1; i < length; i++) {
            digits[i] = (char) (random.nextInt(10) + '0');
        }
        return Long.parseLong(new String(digits));
    }
    private void uploadImage(String imgname ,final String name   , final String desc    , final String Position , final String Age , final String Phone , final String School   , User currentUser ) {

        final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        long currentTime = timestamp.getTime();
        ApiInterface api = RetrofitClient.retrofitWrite().create(ApiInterface.class);

        //Defining the trader object as we need to pass it with the call
        //Integer centerId, Integer categoryId, Integer levelId, Integer classId, Integer subjectId, Integer totalStudents, Integer rate, Integer ratecount, Integer phone, String age, String school, String cityCode, String govermentCode, String socialFacebook, String videoUrlyou, Integer sponsored, Long createdAt, Integer viewCount
        Instructor courses = new Instructor(currentUser.getId(), 0, 1, getLevelID(), getClassID(), String.valueOf(getSubjectID()), 0, 0, 0, Integer.parseInt(Phone), Age, School, "", "DOav7sKOIjw", 0, currentTime, 0);

        // Log.d("helloo",encodedImage +"   >>"+imgname);
        //Log.d("imggggg","   >>"+imgname);

        //defining the call
        Call<InstructorResponse> call = api.createInstructor(
                courses.getUsrId(),
                courses.getCenterId(),
                courses.getCategoryId(),
                courses.getLevelId(),
                courses.getSubjectId(),
                courses.getClassId(),
                courses.getTotalStudents(),
                courses.getRate(),
                courses.getRatecount(),
                courses.getPhone(),
                courses.getAge(),
                courses.getSchool(),
                getGovermentID(),
                getCityID(),
                courses.getSocialFacebook(),
                courses.getVideoUrlyou(),
                courses.getSponsored(),
                courses.getCreatedAt(),
                courses.getViewCount()
        );

        call.enqueue(new Callback<InstructorResponse>() {
            @Override
            public void onResponse(Call<InstructorResponse> call, Response<InstructorResponse> response) {

                int course_id = response.body().getInstructor().getId();

                Call<InstructorResponse> call_details = api.createInstructorDetails(
                        course_id,
                        name,
                        Position,
                        desc,
                        RetrofitClient.BASE_TeacherImage_URL + imgname + ".JPG",
                        0

                );
                call_details.enqueue(new Callback<InstructorResponse>() {
                    @Override
                    public void onResponse(Call<InstructorResponse> call, Response<InstructorResponse> response) {
                        Call<InstructorResponse> call_details = api.createInstructorDetails(
                                course_id,
                                name,
                                Position,
                                desc,
                                RetrofitClient.BASE_TeacherImage_URL + imgname + ".JPG",
                                1

                        );
                        call_details.enqueue(new Callback<InstructorResponse>() {
                            @Override
                            public void onResponse(Call<InstructorResponse> call, Response<InstructorResponse> response) {
                                hideProgressDialog();
                            }

                            @Override
                            public void onFailure(Call<InstructorResponse> call, Throwable t) {
                                hideProgressDialog();
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<InstructorResponse> call, Throwable t) {
                        hideProgressDialog();
                    }
                });
                //uploadImagesToServer(response.body().getProduct().getId());

                //Toast.makeText()

                Toast.makeText(CreateTeacherActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                Log.i("retrofit", response.body().getMessage());//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();


            }


            @Override
            public void onFailure(Call<InstructorResponse> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(CreateTeacherActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                Log.i("retrofit", t.getMessage());//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
            }
        });
        //Toast.makeText(AddTraderActivity.this, "Image Uploaded Successfully!!", Toast.LENGTH_SHORT).show();


    }

    @Override
    protected void onResume() {
        super.onResume();
        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }


        if (requestCode == GALLERY) {
            if (resultData != null) {
                Uri contentURI = resultData.getData();
                try {

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    setup_img.setImageBitmap(bitmap);
                    setTeacherIMG(bitmap);


                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(CreateTeacherActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void getMainCategory() {
        ArrayList<String> Values_main = new ArrayList<>();


        ApiInterface service = RetrofitClient.retrofitRead().create(ApiInterface.class);


        Call<Goverments> call = service.getAllGoverments();

        call.enqueue(new Callback<Goverments>() {
            @Override
            public void onResponse(Call<Goverments> call, Response<Goverments> response) {

                ArrayList<Goverment> jsonresponse  = new ArrayList<>(response.body().getGoverments());
                for (int i = 0; i <jsonresponse.size() ; i++) {
                    Values_main.add(jsonresponse.get(i).getGovernorateName());
                }
                ArrayAdapter<String> spinnerMainAdapter = new ArrayAdapter<String>(CreateTeacherActivity.this,  android.R.layout.simple_dropdown_item_1line, Values_main);
                spinnerMainAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                spinner_main_ip.setAdapter(spinnerMainAdapter);
                spinner_main_ip.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        main_ip.setText(spinner_main_ip.getSelectedItem().toString());
                        main_ip.dismissDropDown();
                        //int retval=jsonresponse.indexOf(spinner_main_ip.getSelectedItem().toString());

                        for (int i = 0; i < jsonresponse.size(); i++) {
                            if (jsonresponse.get(i).getGovernorateName().equals(spinner_main_ip.getSelectedItem().toString())) {
                                // Toast.makeText(AddTeacherActivity.this, "" + jsonresponse.get(i).getId() + "  " + jsonresponse.get(i).getGovernorateName(), Toast.LENGTH_SHORT).show();
                                getSubCategory(jsonresponse.get(i).getId());
                                setGovermentID(jsonresponse.get(i).getId());
                            }
                        }



                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        main_ip.setText(spinner_main_ip.getSelectedItem().toString());
                        main_ip.dismissDropDown();
                    }
                });

            }

            @Override
            public void onFailure(Call<Goverments> call, Throwable t) {
                Toast.makeText(CreateTeacherActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });




    }
    private void getSubCategory(int type) {

        List<String> Values_sub = new ArrayList<>();


        ApiInterface service = RetrofitClient.retrofitRead().create(ApiInterface.class);


        Call<Cities> call = service.getAllCities(type);

        call.enqueue(new Callback<Cities>() {
            @Override
            public void onResponse(Call<Cities> call, Response<Cities> response) {

                List<City> jsonresponse  = new ArrayList<>(response.body().getCities());
                for (int i = 0; i <jsonresponse.size() ; i++) {
                    Values_sub.add(jsonresponse.get(i).getCityName());
                }
                ArrayAdapter<String> spinnerMainAdapter = new ArrayAdapter<String>(CreateTeacherActivity.this,  android.R.layout.simple_dropdown_item_1line, Values_sub);
                spinnerMainAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                spinner_sub_ip.setAdapter(spinnerMainAdapter);

                spinner_sub_ip.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        sub_ip.setText(spinner_sub_ip.getSelectedItem().toString());
                        sub_ip.dismissDropDown();
                        for (int i = 0; i < jsonresponse.size(); i++) {
                            if (jsonresponse.get(i).getCityName().equals(spinner_sub_ip.getSelectedItem().toString())) {
                                //Toast.makeText(AddTeacherActivity.this, "" + jsonresponse.get(i).getId() + "  " + jsonresponse.get(i).getCityName(), Toast.LENGTH_SHORT).show();
                                //getGroupCategory(jsonresponse.get(i).getId());
                                setCityID(jsonresponse.get(i).getId());
                            }
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        sub_ip.setText(spinner_sub_ip.getSelectedItem().toString());
                        sub_ip.dismissDropDown();
                    }
                });


            }

            @Override
            public void onFailure(Call<Cities> call, Throwable t) {
                Toast.makeText(CreateTeacherActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void getLevelCategory() {
        ArrayList<String> Values_main = new ArrayList<>();
        ArrayList<String> Values_sub = new ArrayList<>();
        ArrayList<String> Values_group = new ArrayList<>();

        ApiInterface service = RetrofitClient.retrofitRead().create(ApiInterface.class);


        Call<Categories> call = service.getSubCategories(0 , 1 , 1);

        call.enqueue(new Callback<Categories>() {
            @Override
            public void onResponse(Call<Categories> call, Response<Categories> response) {

                ArrayList<Category> jsonresponse  = new ArrayList<>(response.body().getCategories());
                for (int i = 0; i <jsonresponse.size() ; i++) {
                    Values_main.add(jsonresponse.get(i).getCatName());
                }
                ArrayAdapter<String> spinnerMainAdapter = new ArrayAdapter<String>(CreateTeacherActivity.this,  android.R.layout.simple_dropdown_item_1line, Values_main);
                spinnerMainAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                spinner_level_ip.setAdapter(spinnerMainAdapter);
                spinner_level_ip.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        level_ip.setText(spinner_level_ip.getSelectedItem().toString());
                        level_ip.dismissDropDown();
                        //int retval=jsonresponse.indexOf(spinner_main_ip.getSelectedItem().toString());
                        for (int i = 0; i < jsonresponse.size(); i++) {
                            if (jsonresponse.get(i).getCatName().equals(spinner_level_ip.getSelectedItem().toString())) {
                                //Toast.makeText(CreateTeacherActivity.this, "" + jsonresponse.get(i).getId() + "  " + jsonresponse.get(i).getCatName(), Toast.LENGTH_SHORT).show();
                                getClassCategory(jsonresponse.get(i).getId());
                                setLevelID(jsonresponse.get(i).getId());
                                // setCat_gparent_index(jsonresponse.get(i).getId());
                            }
                        }

                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        level_ip.setText(spinner_level_ip.getSelectedItem().toString());
                        level_ip.dismissDropDown();
                    }
                });

            }

            @Override
            public void onFailure(Call<Categories> call, Throwable t) {
                Toast.makeText(CreateTeacherActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });




    }
    private void getClassCategory(int subCategory) {

        List<String> Values_sub = new ArrayList<>();


        ApiInterface service = RetrofitClient.retrofitRead().create(ApiInterface.class);


        Call<Categories> call = service.getSubCategories(subCategory , 2 , 1);

        call.enqueue(new Callback<Categories>() {
            @Override
            public void onResponse(Call<Categories> call, Response<Categories> response) {

                List<Category> jsonresponse  = new ArrayList<>(response.body().getCategories());
                for (int i = 0; i <jsonresponse.size() ; i++) {
                    Values_sub.add(jsonresponse.get(i).getCatName());
                }
                ArrayAdapter<String> spinnerMainAdapter = new ArrayAdapter<String>(CreateTeacherActivity.this,  android.R.layout.simple_dropdown_item_1line, Values_sub);
                spinnerMainAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                spinner_class_ip.setAdapter(spinnerMainAdapter);

                spinner_class_ip.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        class_ip.setText(spinner_class_ip.getSelectedItem().toString());
                        class_ip.dismissDropDown();
                        for (int i = 0; i < jsonresponse.size(); i++) {
                            if (jsonresponse.get(i).getCatName().equals(spinner_class_ip.getSelectedItem().toString())) {
                                //Toast.makeText(CreateTeacherActivity.this, "" + jsonresponse.get(i).getId() + "  " + jsonresponse.get(i).getCatName(), Toast.LENGTH_SHORT).show();
                                getSubjectCategory(subCategory);
                                setClassID(jsonresponse.get(i).getId());
                                // setCat_parent_index(jsonresponse.get(i).getId());
                            }
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        class_ip.setText(spinner_class_ip.getSelectedItem().toString());
                        class_ip.dismissDropDown();
                    }
                });


            }

            @Override
            public void onFailure(Call<Categories> call, Throwable t) {
                Toast.makeText(CreateTeacherActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });




    }
    private void getSubjectCategory(int subCategory) {


        List<String> Values_group = new ArrayList<>();

        ApiInterface service = RetrofitClient.retrofitRead().create(ApiInterface.class);


        Call<Categories> call = service.getSubCategories(subCategory , 3 , 1);

        call.enqueue(new Callback<Categories>() {
            @Override
            public void onResponse(Call<Categories> call, Response<Categories> response) {

                List<Category> jsonresponse  = new ArrayList<>(response.body().getCategories());
                for (int i = 0; i <jsonresponse.size() ; i++) {
                    Values_group.add(jsonresponse.get(i).getCatName());
                }
                ArrayAdapter<String> spinnerMainAdapter = new ArrayAdapter<String>(CreateTeacherActivity.this,  android.R.layout.simple_dropdown_item_1line, Values_group);
                spinnerMainAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                spinner_subject_ip.setAdapter(spinnerMainAdapter);
                spinner_subject_ip.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        subject_ip.setText(spinner_subject_ip.getSelectedItem().toString());
                        subject_ip.dismissDropDown();
                        for (int i = 0; i < jsonresponse.size(); i++) {
                            if (jsonresponse.get(i).getCatName().equals(spinner_subject_ip.getSelectedItem().toString())) {
                                //Toast.makeText(CreateTeacherActivity.this, "" + jsonresponse.get(i).getId() + "  " + jsonresponse.get(i).getCatName(), Toast.LENGTH_SHORT).show();
                                setSubjectID(jsonresponse.get(i).getId());
                                // setCat_parent_index(jsonresponse.get(i).getId());
                            }
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        subject_ip.setText(spinner_subject_ip.getSelectedItem().toString());
                        subject_ip.dismissDropDown();
                    }
                });
            }

            @Override
            public void onFailure(Call<Categories> call, Throwable t) {
                Toast.makeText(CreateTeacherActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });




    }

    private boolean validatePass() {


        check = instructorpassword.getText().toString();

        if (check.length() < 4 || check.length() > 20) {
            return false;
        } else if (!check.matches("^[A-za-z0-9@]+")) {
            return false;
        }
        return true;
    }

    private boolean validateEmail() {

        check = instructoremail.getText().toString();

        if (check.length() < 4 || check.length() > 40) {
            return false;
        } else if (!check.matches("^[A-za-z0-9.@]+")) {
            return false;
        } else if (!check.contains("@") || !check.contains(".")) {
            return false;
        }

        return true;
    }

    //TextWatcher for Email -----------------------------------------------------

    TextWatcher emailWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //none
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //none
        }

        @Override
        public void afterTextChanged(Editable s) {

            check = s.toString();

            if (check.length() < 4 || check.length() > 40) {
                instructoremail.setError("Email Must consist of 4 to 20 characters");
            } else if (!check.matches("^[A-za-z0-9.@]+")) {
                instructoremail.setError("Only . and @ characters allowed");
            } else if (!check.contains("@") || !check.contains(".")) {
                instructoremail.setError("Enter Valid Email");
            }

        }

    };

    //TextWatcher for pass -----------------------------------------------------

    TextWatcher passWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //none
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //none
        }

        @Override
        public void afterTextChanged(Editable s) {

            check = s.toString();

            if (check.length() < 4 || check.length() > 20) {
                instructorpassword.setError("Password Must consist of 4 to 20 characters");
            } else if (!check.matches("^[A-za-z0-9@]+")) {
                instructorpassword.setError("Only @ special character allowed");
            }
        }

    };


    public int getGovermentID() {
        return GovermentID;
    }

    public void setGovermentID(int govermentID) {
        GovermentID = govermentID;
    }

    public int getCityID() {
        return CityID;
    }

    public void setCityID(int cityID) {
        CityID = cityID;
    }

    public int getLevelID() {
        return LevelID;
    }

    public void setLevelID(int levelID) {
        LevelID = levelID;
    }

    public int getClassID() {
        return ClassID;
    }

    public void setClassID(int classID) {
        ClassID = classID;
    }

    public int getSubjectID() {
        return SubjectID;
    }

    public void setSubjectID(int subjectID) {
        SubjectID = subjectID;
    }

    public Bitmap getTeacherIMG() {
        return TeacherIMG;
    }

    public void setTeacherIMG(Bitmap teacherIMG) {
        TeacherIMG = teacherIMG;
    }
}