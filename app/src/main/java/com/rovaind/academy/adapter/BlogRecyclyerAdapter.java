package com.rovaind.academy.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rovaind.academy.Controllers.PostCommentsActivity;
import com.rovaind.academy.R;
import com.rovaind.academy.Utils.CircleImageView;
import com.rovaind.academy.Utils.ExpandableTweetView;
import com.rovaind.academy.Utils.FormatterUtil;
import com.rovaind.academy.Utils.GetTimeAgo;
import com.rovaind.academy.Utils.ItemsUI;
import com.rovaind.academy.Utils.views.AutoFitTextView;
import com.rovaind.academy.interfaces.OnPostClickListener;
import com.rovaind.academy.manager.SharedPrefManager;
import com.rovaind.academy.manager.response.LikePostResponse;
import com.rovaind.academy.model.Course;
import com.rovaind.academy.model.Post;
import com.rovaind.academy.model.User;
import com.rovaind.academy.retrofit.ApiInterface;
import com.rovaind.academy.retrofit.RetrofitClient;


import java.sql.Timestamp;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Mohamed El Sayed
 */
class SingleTapConfirm extends GestureDetector.SimpleOnGestureListener {

    private static final String TAG = "SingleTapConfirm";
    private Post selectedPost;
    private Course selectedCourse;

    SingleTapConfirm(Post selectedPost , Course selectedCourse)
    {
        this.selectedPost = selectedPost;
        this.selectedCourse = selectedCourse;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        Log.e(TAG, "onSingleTapConfirmed"); // never called..
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Log.e(TAG, "onDoubleTap"); // never called..
        if(selectedPost != null)
        {
            Log.d(TAG, "onDoubleTap: Not nUll");
            /*
            //holder.reactionView.show(holder.motionEvent);
            firebaseFirestore.collection("Posts/" + blogPostId + "/Likes").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if(!task.getResult().exists()){
                        final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                        Map<String, Object> likesMap = new HashMap<>();
                        likesMap.put("timestamp", timestamp.getTime());

                        firebaseFirestore.collection("Posts/" + blogPostId + "/Likes").document(currentUserId).set(likesMap);
                        if(!currentUserId.equals(c.getUser_id())) {
                            firebaseFirestore.collection("users").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                    if (task.isSuccessful()) {
                                        final String userName = task.getResult().getString("username");
                                        final String thumb_image = task.getResult().getString("thumb_image");
                                        firebaseFirestore.collection("users/" + c.getUser_id() + "/Notifications/").document(blogPostId + "%" + userName).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                if(!task.getResult().exists())
                                                {
                                                    String message_noti = userName + " Liked your post";
                                                    Map<String, Object> notificatinMap = new HashMap<>();
                                                    notificatinMap.put("message", message_noti);
                                                    notificatinMap.put("from", currentUserId);
                                                    notificatinMap.put("username", userName);
                                                    notificatinMap.put("thumb_image", thumb_image);
                                                    notificatinMap.put("post", "photo");
                                                    notificatinMap.put("type", "like");
                                                    notificatinMap.put("forward", blogPostId);
                                                    notificatinMap.put("timestamp", timestamp.getTime());
                                                    firebaseFirestore.collection("users/" + c.getUser_id() + "/Notifications/").document(blogPostId + "%" + userName).set(notificatinMap);
                                                }
                                                else
                                                {
                                                    //firebaseFirestore.collection("users/" + Ownder_id + "/Notifications/").document(blogPostId + "@" + userName).delete();
                                                }
                                            }
                                        });



                                    } else {

                                        //Firebase Exception

                                    }

                                }
                            });
                        }

                    } else {
                        firebaseFirestore.collection("Posts/" + blogPostId + "/Likes").document(currentUserId).delete();
                        firebaseFirestore.collection("users").document(c.getUser_id()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                if(task.isSuccessful()){

                                    String userName = task.getResult().getString("username");
                                    firebaseFirestore.collection("users/" + c.getUser_id() + "/Notifications/").document(blogPostId + "%" + userName).delete();
                                }
                            }
                        });

                    }

                }
            });

             */
        }

       // View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
       // int position = recyclerView.getChildPosition(view);
        return super.onDoubleTap(e);
    }
}

public class BlogRecyclyerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public Context context;
    //
    private static final int Ordinary_Tweets = 1;
    private static final int Ordinary_Photo = 2;
    private static final int Video_Post = 3;
    private static final int Image_Text = 4;
    private static final int Image_Collections = 5;
    private static final int PDF = 6;
    private static final int ADS = 9;
    //
    private List<Post> posts;
    private Course selectedCourse;
    private OnPostClickListener onPostClickListener;
    public BlogRecyclyerAdapter(List<Post> posts , Course selectedCourse , OnPostClickListener onPostClickListener )
    {
        this.posts = posts;
        this.selectedCourse = selectedCourse;
        this.onPostClickListener = onPostClickListener;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        RecyclerView.ViewHolder holder;
        switch (viewType) {
            case Ordinary_Tweets:
                view = inflater.inflate(R.layout.single_list_item_tweets_t, parent, false);//ForTEST
                holder = new TweetsViewHolder(view);
                break;
            case Image_Text:
                view = inflater.inflate(R.layout.single_list_item_image_text, parent, false);
                holder = new ImagePhotoViewHolder(view);
                break;
            case Ordinary_Photo:
                view = inflater.inflate(R.layout.single_list_item_odrinary_photo_t, parent, false);
                holder = new PhotosViewHolder(view);
                break;
            case PDF:
                view = inflater.inflate(R.layout.single_list_item_pdf, parent, false);
                holder = new PDFViewHolder(view);
                break;

            default:
                view = inflater.inflate(R.layout.single_list_item_tweets_t, parent, false);
                holder = new TweetsViewHolder(view);
                break;
        }


        return holder;

    }
    @Override
    public int getItemViewType(int position) {


        //return -1;
        int data_type = posts.get(position).getPostType();

        //return -1;
        return data_type;

    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == Ordinary_Tweets)
        {
            onBindViewHolder_Tweets((TweetsViewHolder) holder, position);
        }
        else if(getItemViewType(position) == Image_Text)
        {
            onBindViewHolder_ImagePhoto((ImagePhotoViewHolder) holder, position);
        }
        else if(getItemViewType(position) == Ordinary_Photo)
        {
            onBindViewHolder_Photos((PhotosViewHolder) holder, position);
        }
        else if(getItemViewType(position) == PDF)
        {
            onBindViewHolder_PDF((PDFViewHolder) holder, position);
        }
    }
    //Different_methods
    public void onBindViewHolder_Tweets(final TweetsViewHolder holder, final int position) {

        holder.setIsRecyclable(false);

        final Post post = posts.get(position);
        final Course course = selectedCourse;
        final User user = SharedPrefManager.getInstance(context).getUser();


        String userName = post.getOwnerName();
        final String userImage = post.getThumbImage();
        holder.setUserData(userName , userImage);
        //Time_Stamp[Date]
        final long time = post.getCreatedAt();
        holder.setTime(time);

        holder.blogLikeCount.setText(post.getLikeCount() + " " + context.getResources().getString(R.string.likes));
        holder.blogCommentCount.setText(post.getCommentCount() +" " + context.getResources().getString(R.string.comment));
        holder.fillTweet("", post, holder.expandableTweetView);
        ///////////////////////////////////////////////////////////////////////
        holder.blogCommentBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent commentIntent = new Intent(context, PostCommentsActivity.class);
                commentIntent.putExtra(ItemsUI.BUNDLE_POSTS, post );
                commentIntent.putExtra(ItemsUI.BUNDLE_COURSES_DETAILS ,selectedCourse);
                context.startActivity(commentIntent);
            }
        });
        //like comment
        holder.blogLikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!SharedPrefManager.getInstance(context).isLoggedIn()) {
                    Toast.makeText(context,context.getResources().getString(R.string.you_have_to_reg_to_comment), Toast.LENGTH_SHORT).show();
                    holder.CreateLike(post, selectedCourse, time, position);
                }
                /*
                fStore.collection("Posts/" + blogPostId + "/Comments/" + blog_commentid + "/Likes").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(!task.getResult().exists()){
                            holder.image_like.setPressed(true);


                        } else {
                            holder.image_like.setPressed(false);


                        }

                    }
                });

                 */
            }
        });
        ///////////////////////////////////////////////////////////////////////

        holder.more_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Display option menu

                androidx.appcompat.widget.PopupMenu popupMenu = new androidx.appcompat.widget.PopupMenu(context, holder.more_btn);
                popupMenu.inflate(R.menu.option_menu);
                popupMenu.setOnMenuItemClickListener(new androidx.appcompat.widget.PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.mnu_item_save:
                                Toast.makeText(context, "Saved", Toast.LENGTH_LONG).show();
                                break;
                            case R.id.mnu_item_delete: {
                                //Delete item

                            }
                            default:
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
        //////////////////////////////////////////////////////////////////////

    }
    public void onBindViewHolder_ImagePhoto(final ImagePhotoViewHolder holder, final int position) {

        holder.setIsRecyclable(false);

        final Post post = posts.get(position);
        final Course course = selectedCourse;
        final User user = SharedPrefManager.getInstance(context).getUser();


        String userName = post.getOwnerName();
        final String userImage = post.getThumbImage();
        holder.setUserData(userName , userImage);
        //Time_Stamp[Date]
        final long time = post.getCreatedAt();
        holder.setTime(time);

        holder.blogLikeCount.setText(post.getLikeCount() + " " + context.getResources().getString(R.string.likes));
        holder.blogCommentCount.setText(post.getCommentCount() +" " + context.getResources().getString(R.string.comment));
        //Tweets_Text
        final String desc_post = post.getContent();
        holder.setDescText_Post(desc_post , post.getBackImg() ,post.getTxtColor());


        ///////////////////////////////////////////////////////////////////////
        holder.blogCommentBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent commentIntent = new Intent(context, PostCommentsActivity.class);
                commentIntent.putExtra(ItemsUI.BUNDLE_POSTS, post );
                commentIntent.putExtra(ItemsUI.BUNDLE_COURSES_DETAILS ,selectedCourse);
                context.startActivity(commentIntent);
            }
        });
        //like comment
        holder.blogLikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!SharedPrefManager.getInstance(context).isLoggedIn()) {
                    Toast.makeText(context,context.getResources().getString(R.string.you_have_to_reg_to_comment), Toast.LENGTH_SHORT).show();
                    holder.CreateLike(post, selectedCourse, time, position);
                }
                /*
                fStore.collection("Posts/" + blogPostId + "/Comments/" + blog_commentid + "/Likes").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(!task.getResult().exists()){
                            holder.image_like.setPressed(true);


                        } else {
                            holder.image_like.setPressed(false);


                        }

                    }
                });

                 */
            }
        });
        ///////////////////////////////////////////////////////////////////////

        holder.more_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Display option menu

                androidx.appcompat.widget.PopupMenu popupMenu = new androidx.appcompat.widget.PopupMenu(context, holder.more_btn);
                popupMenu.inflate(R.menu.option_menu);
                popupMenu.setOnMenuItemClickListener(new androidx.appcompat.widget.PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.mnu_item_save:
                                Toast.makeText(context, "Saved", Toast.LENGTH_LONG).show();
                                break;
                            case R.id.mnu_item_delete: {
                                //Delete item

                            }
                            default:
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
        //////////////////////////////////////////////////////////////////////

    }
    public void onBindViewHolder_Photos(final PhotosViewHolder holder, final int position) {

        holder.setIsRecyclable(false);



        final Post post = posts.get(position);
        final Course course = selectedCourse;
        final User user = SharedPrefManager.getInstance(context).getUser();


        String userName = post.getOwnerName();
        final String userImage = post.getThumbImage();
        holder.setUserData(userName , userImage);
        //Time_Stamp[Date]
        final long time = post.getCreatedAt();
        holder.setTime(time);

        holder.blogLikeCount.setText(post.getLikeCount() + " " + context.getResources().getString(R.string.likes));
        holder.blogCommentCount.setText(post.getCommentCount() +" " + context.getResources().getString(R.string.comment));
        //BlogPost
        String image_url = post.getGallery().get(0).getThumbImage();
        holder.setBlogImage(RetrofitClient.BASE_POST_URL + image_url);

        //gestureDetector = new GestureDetectorCompat((Activity) context, new SingleTapConfirm( blogPostId, c));
        holder.fillTweet("",post, holder.expandableTweetView , RetrofitClient.BASE_POST_URL + image_url);
        ///////////////////////////////////////////////////////////////////////

        holder.blogCommentBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent commentIntent = new Intent(context, PostCommentsActivity.class);
                commentIntent.putExtra(ItemsUI.BUNDLE_POSTS, post );
                commentIntent.putExtra(ItemsUI.BUNDLE_COURSES_DETAILS ,selectedCourse);
                context.startActivity(commentIntent);
            }
        });
        //like comment
        holder.blogLikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!SharedPrefManager.getInstance(context).isLoggedIn()) {
                    Toast.makeText(context,context.getResources().getString(R.string.you_have_to_reg_to_comment), Toast.LENGTH_SHORT).show();
                    holder.CreateLike(post, selectedCourse, time, position);
                }
                /*
                fStore.collection("Posts/" + blogPostId + "/Comments/" + blog_commentid + "/Likes").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(!task.getResult().exists()){
                            holder.image_like.setPressed(true);


                        } else {
                            holder.image_like.setPressed(false);


                        }

                    }
                });

                 */
            }
        });
        ///////////////////////////////////////////////////////////////////////

        holder.more_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Display option menu

                androidx.appcompat.widget.PopupMenu popupMenu = new androidx.appcompat.widget.PopupMenu(context, holder.more_btn);
                popupMenu.inflate(R.menu.option_menu);
                popupMenu.setOnMenuItemClickListener(new androidx.appcompat.widget.PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.mnu_item_save:
                                Toast.makeText(context, "Saved", Toast.LENGTH_LONG).show();
                                break;
                            case R.id.mnu_item_delete: {
                                //Delete item

                            }
                            default:
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
        //////////////////////////////////////////////////////////////////////

    }
    public void onBindViewHolder_PDF(final PDFViewHolder holder, final int position) {

        holder.setIsRecyclable(false);

        final Post post = posts.get(position);
        final Course course = selectedCourse;
        final User user = SharedPrefManager.getInstance(context).getUser();


        String userName = post.getOwnerName();
        final String userImage = post.getThumbImage();
        holder.setUserData(userName , userImage);
        //Time_Stamp[Date]
        final long time = post.getCreatedAt();
        holder.setTime(time);

        holder.blogLikeCount.setText(post.getLikeCount() + " " + context.getResources().getString(R.string.likes));
        holder.blogCommentCount.setText(post.getCommentCount() +" " + context.getResources().getString(R.string.comment));

        holder.post_cont.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(RetrofitClient.BASE_PDF_URL + post.getUrl()), "application/pdf");
                    //intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                } catch (Exception e) {
                    Log.d("PDF", "onClick: " + e.getLocalizedMessage());
                }
                /*
                Intent commentIntent = new Intent(context, PDFViewerActivity.class);
                commentIntent.putExtra(ItemsUI.BUNDLE_POSTS, post );
                commentIntent.putExtra(ItemsUI.BUNDLE_COURSES_DETAILS ,selectedCourse);
                context.startActivity(commentIntent);

                 */
            }
        });

        ///////////////////////////////////////////////////////////////////////
        holder.blogCommentBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent commentIntent = new Intent(context, PostCommentsActivity.class);
                commentIntent.putExtra(ItemsUI.BUNDLE_POSTS, post );
                commentIntent.putExtra(ItemsUI.BUNDLE_COURSES_DETAILS ,selectedCourse);
                context.startActivity(commentIntent);
            }
        });
        //like comment
        holder.blogLikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!SharedPrefManager.getInstance(context).isLoggedIn()) {
                    Toast.makeText(context,context.getResources().getString(R.string.you_have_to_reg_to_comment), Toast.LENGTH_SHORT).show();
                    holder.CreateLike(post, selectedCourse, time, position);
                }
                /*
                fStore.collection("Posts/" + blogPostId + "/Comments/" + blog_commentid + "/Likes").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(!task.getResult().exists()){
                            holder.image_like.setPressed(true);


                        } else {
                            holder.image_like.setPressed(false);


                        }

                    }
                });

                 */
            }
        });
        ///////////////////////////////////////////////////////////////////////

        holder.more_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Display option menu

                androidx.appcompat.widget.PopupMenu popupMenu = new androidx.appcompat.widget.PopupMenu(context, holder.more_btn);
                popupMenu.inflate(R.menu.option_menu);
                popupMenu.setOnMenuItemClickListener(new androidx.appcompat.widget.PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.mnu_item_save:
                                Toast.makeText(context, "Saved", Toast.LENGTH_LONG).show();
                                break;
                            case R.id.mnu_item_delete: {
                                //Delete item

                            }
                            default:
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
        //////////////////////////////////////////////////////////////////////

    }


    @Override
    public int getItemCount() {

        return posts.size();
    }
    public class TweetsViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private TextView blogUserName;
        private CircleImageView blogUserImage;
        private TextView blogDate;
        private ImageButton more_btn;

        private ImageView blogLikeBtn;
        private ImageView blogCommentBtn;
        private TextView blogLikeCount;
        private TextView blogCommentCount;


        private TextView blogTweet;


        private ExpandableTweetView expandableTweetView;

        public TweetsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            //Upper_Layout
            blogUserName = mView.findViewById(R.id.blog_user_name);
            blogUserImage = mView.findViewById(R.id.blog_user_image);
            blogDate = mView.findViewById(R.id.blog_date);
            more_btn = mView.findViewById(R.id.imageButton_more);
            //Middle_Layout
            blogLikeBtn = mView.findViewById(R.id.blog_like_btn);
            blogLikeCount = mView.findViewById(R.id.blog_like_count);
            blogCommentBtn = mView.findViewById(R.id.blog_comment_icon);
            blogCommentCount = mView.findViewById(R.id.blog_comment_count);

            //Content
            blogTweet = mView.findViewById(R.id.blog_Tweet);

            expandableTweetView = (ExpandableTweetView) itemView.findViewById(R.id.tweetText);

        }
        private void fillTweet(String userName , Post blogPost, ExpandableTweetView tweetTextView) {
            Spannable contentString = new SpannableStringBuilder(userName + "   " + blogPost.getContent());
            contentString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.highlight_text)),
                    0, userName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            tweetTextView.setText(contentString);
            CharSequence date = FormatterUtil.getRelativeTimeSpanString(context, blogPost.getCreatedAt());
            blogDate.setText(date);
        }

        public void setDescText_Post(String descText){

            blogTweet.setText(descText);

        }
        public void setTime(long time) {

            GetTimeAgo getTimeAgo = new GetTimeAgo();

            long lastTime = time;

            String lastSeenTime = GetTimeAgo.getTimeAgo(lastTime, context);

            blogDate.setText(lastSeenTime);

        }

        public void setUserData(String name, String image){

            blogUserName.setText(name);
            Glide.with(context).load(image).into(blogUserImage);

        }
        public void setUserData(String image){
            Glide.with(context).load(image).into(blogUserImage);

        }

        public void updateLikesCount(int count){

            blogLikeCount.setText(count + " " +context.getString(R.string.likes));

        }
        public void UpdateCommentCount(int count , String image){
            blogCommentCount.setText(count + " " +context.getString(R.string.comments));
        }

        private void CreateLike(Post post , Course course , long time , int position) {



            final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            int owner_id = SharedPrefManager.getInstance(context).getUser().getId();

            String owner_name = SharedPrefManager.getInstance(context).getUser().getName();
            //Toast.makeText(this, "" + owner_name, Toast.LENGTH_SHORT).show();
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();


            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(RetrofitClient.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            ApiInterface api = retrofit.create(ApiInterface.class);


            //defining the call
            Call<LikePostResponse> call = api.createPostLike(
                    owner_id,
                    post.getId(),
                    0,
                    course.getId(),
                    time



            );
            call.enqueue(new Callback<LikePostResponse>() {
                @Override
                public void onResponse(Call<LikePostResponse> call, Response<LikePostResponse> response) {
                    // IncRate(lecture.getCourseId(), (int) ratingbar.getRating());
                    if (response.body() != null) {


                        if(response.message().equals("Like Added"))
                        {
                            blogLikeBtn.setPressed(true);
                            blogLikeBtn.setImageDrawable(ContextCompat.getDrawable(context , R.drawable.ic_active_like));
                            blogLikeCount.setText(post.getLikeCount() + " " + context.getResources().getString(R.string.likes));

                        }
                        else if(response.message().equals("Like Already Added"))
                        {
                            blogLikeBtn.setPressed(false);
                            blogLikeBtn.setImageDrawable(ContextCompat.getDrawable(context ,R.drawable.ic_like));

                        }
                    } else {
                        Log.d("Likes", "onResponse: " + response.errorBody());
                    }
                }

                @Override
                public void onFailure(Call<LikePostResponse> call, Throwable t) {

                    Log.d("Likes", "onFailure: " + t.getMessage());
                    //Toast.makeText(VideoPlayer.this, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }



    }
    public class ImagePhotoViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private TextView blogUserName;
        private CircleImageView blogUserImage;
        private TextView blogDate;
        private ImageButton more_btn;

        private ImageView blogLikeBtn;
        private ImageView blogCommentBtn;
        private TextView blogLikeCount;
        private TextView blogCommentCount;

        private AutoFitTextView blogTweet;
        private ConstraintLayout post_cont;
        public ImagePhotoViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            //Upper_Layout
            blogUserName = mView.findViewById(R.id.blog_user_name);
            blogUserImage = mView.findViewById(R.id.blog_user_image);
            blogDate = mView.findViewById(R.id.blog_date);
            more_btn = mView.findViewById(R.id.imageButton_more);
            //Middle_Layout
            blogLikeBtn = mView.findViewById(R.id.blog_like_btn);
            blogLikeCount = mView.findViewById(R.id.blog_like_count);
            blogCommentBtn = mView.findViewById(R.id.blog_comment_icon);
            blogCommentCount = mView.findViewById(R.id.blog_comment_count);
            //Content
            blogTweet = mView.findViewById(R.id.blog_Tweet);
            post_cont = mView.findViewById(R.id.post_cont);

        }


        public void setDescText_Post(String descText , String ImageURL , String TextColor){

            blogTweet.setText(descText);
            blogTweet.setTextColor(Color.parseColor(TextColor));
            Glide.with(context)
                    .load(RetrofitClient.BASE_POST_IMAGE_URL +  ImageURL)

                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource,
                                                    @Nullable Transition<? super Drawable> transition) {

                            post_cont.setBackground(resource);

                        }
                    });

        }
        public void setTime(long time) {

            /*
            GetTimeAgo getTimeAgo = new GetTimeAgo();

            long lastTime = time;

            String lastSeenTime = GetTimeAgo.getTimeAgo(lastTime, context);

             */

            CharSequence date = FormatterUtil.getRelativeTimeSpanString(context, time);

            blogDate.setText(date);

        }

        public void setUserData(String name, String image){

            blogUserName.setText(name);
            Glide.with(context).load(image).into(blogUserImage);

        }

        public void updateLikesCount(int count){

            blogLikeCount.setText(count +" " + context.getString(R.string.likes));

        }
        public void UpdateCommentCount(int count , String image){
            blogCommentCount.setText(count + " " +context.getString(R.string.comments));
        }

        private void CreateLike(Post post , Course course , long time , int position) {



            final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            int owner_id = SharedPrefManager.getInstance(context).getUser().getId();

            String owner_name = SharedPrefManager.getInstance(context).getUser().getName();
            //Toast.makeText(this, "" + owner_name, Toast.LENGTH_SHORT).show();
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();


            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(RetrofitClient.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            ApiInterface api = retrofit.create(ApiInterface.class);


            //defining the call
            Call<LikePostResponse> call = api.createPostLike(
                    owner_id,
                    post.getId(),
                    0,
                    course.getId(),
                    time



            );
            call.enqueue(new Callback<LikePostResponse>() {
                @Override
                public void onResponse(Call<LikePostResponse> call, Response<LikePostResponse> response) {
                    // IncRate(lecture.getCourseId(), (int) ratingbar.getRating());
                    if (response.body() != null) {


                        if(response.message().equals("Like Added"))
                        {
                            blogLikeBtn.setPressed(true);
                            blogLikeBtn.setImageDrawable(ContextCompat.getDrawable(context , R.drawable.ic_active_like));
                            blogLikeCount.setText(post.getLikeCount() + " " + context.getResources().getString(R.string.likes));

                        }
                        else if(response.message().equals("Like Already Added"))
                        {
                            blogLikeBtn.setPressed(false);
                            blogLikeBtn.setImageDrawable(ContextCompat.getDrawable(context ,R.drawable.ic_like));

                        }
                    } else {
                        Log.d("Likes", "onResponse: " + response.errorBody());
                    }
                }

                @Override
                public void onFailure(Call<LikePostResponse> call, Throwable t) {

                    Log.d("Likes", "onFailure: " + t.getMessage());
                    //Toast.makeText(VideoPlayer.this, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }


    }
    public class PhotosViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private TextView blogUserName;
        private CircleImageView blogUserImage;
        private TextView blogDate;
        private ImageButton more_btn;

        private ImageView blogLikeBtn;
        private ImageView blogCommentBtn;
        private TextView blogLikeCount;
        private TextView blogCommentCount;

        private TextView blogTweet;
        private ImageView blogPhoto;

        View parentLayout;


        private ExpandableTweetView expandableTweetView;



        public PhotosViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            //Upper_Layout
            blogUserName = mView.findViewById(R.id.blog_user_name);
            blogUserImage = mView.findViewById(R.id.blog_user_image);
            blogDate = mView.findViewById(R.id.blog_date);
            more_btn = mView.findViewById(R.id.imageButton_more);
            //Middle_Layout
            blogLikeBtn = mView.findViewById(R.id.blog_like_btn);
            blogLikeCount = mView.findViewById(R.id.blog_like_count);
            blogCommentBtn = mView.findViewById(R.id.blog_comment_icon);
            blogCommentCount = mView.findViewById(R.id.blog_comment_count);
            //Content
            blogTweet = mView.findViewById(R.id.blog_Tweet);
            blogPhoto = mView.findViewById(R.id.blog_image);
            //
            parentLayout = mView.findViewById(R.id.snackbar_action);


            expandableTweetView = (ExpandableTweetView) itemView.findViewById(R.id.tweetText);

        }
        private void fillTweet(String userName , Post blogPost, ExpandableTweetView tweetTextView , String ImageURL) {
            Spannable contentString = new SpannableStringBuilder(userName + "   " + blogPost.getContent());
            contentString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.highlight_text)),
                    0, userName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            tweetTextView.setText(contentString);
            CharSequence date = FormatterUtil.getRelativeTimeSpanString(context, blogPost.getCreatedAt());
            blogDate.setText(date);
            //Glide.with(context).load(blogPost.getImage_url()).into(blogPhoto);
        }


        public void setBlogImage(String downloadUri){

            Glide.with(context).load(downloadUri).into(blogPhoto);

        }
        public void setDescText_Post(String descText){

            blogTweet.setText(descText);

        }
        public void setTime(long time) {

            GetTimeAgo getTimeAgo = new GetTimeAgo();

            long lastTime = time;

            String lastSeenTime = GetTimeAgo.getTimeAgo(lastTime, context);

            blogDate.setText(lastSeenTime);

        }

        public void setUserData(String name, String image){

            blogUserName.setText(name);
            Glide.with(context).load(image).into(blogUserImage);

        }

        public void updateLikesCount(int count){

            blogLikeCount.setText(count +" " + context.getString(R.string.likes));

        }
        public void UpdateCommentCount(int count , String image){
            blogCommentCount.setText(count +" " + context.getString(R.string.comments));
        }

        private void CreateLike(Post post , Course course , long time , int position) {



            final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            int owner_id = SharedPrefManager.getInstance(context).getUser().getId();

            String owner_name = SharedPrefManager.getInstance(context).getUser().getName();
            //Toast.makeText(this, "" + owner_name, Toast.LENGTH_SHORT).show();
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();


            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(RetrofitClient.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            ApiInterface api = retrofit.create(ApiInterface.class);


            //defining the call
            Call<LikePostResponse> call = api.createPostLike(
                    owner_id,
                    post.getId(),
                    0,
                    course.getId(),
                    time



            );
            call.enqueue(new Callback<LikePostResponse>() {
                @Override
                public void onResponse(Call<LikePostResponse> call, Response<LikePostResponse> response) {
                    // IncRate(lecture.getCourseId(), (int) ratingbar.getRating());
                    if (response.body() != null) {


                        if(response.message().equals("Like Added"))
                        {
                            blogLikeBtn.setPressed(true);
                            blogLikeBtn.setImageDrawable(ContextCompat.getDrawable(context , R.drawable.ic_active_like));
                            blogLikeCount.setText(post.getLikeCount() + " " + context.getResources().getString(R.string.likes));

                        }
                        else if(response.message().equals("Like Already Added"))
                        {
                            blogLikeBtn.setPressed(false);
                            blogLikeBtn.setImageDrawable(ContextCompat.getDrawable(context ,R.drawable.ic_like));

                        }
                    } else {
                        Log.d("Likes", "onResponse: " + response.errorBody());
                    }
                }

                @Override
                public void onFailure(Call<LikePostResponse> call, Throwable t) {

                    Log.d("Likes", "onFailure: " + t.getMessage());
                    //Toast.makeText(VideoPlayer.this, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }



    }
    public class PDFViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private TextView blogUserName;
        private CircleImageView blogUserImage;
        private TextView blogDate;
        private ImageButton more_btn;

        private ImageView blogLikeBtn;
        private ImageView blogCommentBtn;
        private TextView blogLikeCount;
        private TextView blogCommentCount;

        private TextView blogTweet;
        private ConstraintLayout post_cont;
        public PDFViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            //Upper_Layout
            post_cont = mView.findViewById(R.id.post_cont);
            blogUserName = mView.findViewById(R.id.blog_user_name);
            blogUserImage = mView.findViewById(R.id.blog_user_image);
            blogDate = mView.findViewById(R.id.blog_date);
            more_btn = mView.findViewById(R.id.imageButton_more);
            //Middle_Layout
            blogLikeBtn = mView.findViewById(R.id.blog_like_btn);
            blogLikeCount = mView.findViewById(R.id.blog_like_count);
            blogCommentBtn = mView.findViewById(R.id.blog_comment_icon);
            blogCommentCount = mView.findViewById(R.id.blog_comment_count);
            //Content
            blogTweet = mView.findViewById(R.id.blog_Tweet);


        }


        public void setDescText_Post(String descText , String ImageURL , String TextColor){

            blogTweet.setText(descText);
            blogTweet.setTextColor(Color.parseColor(TextColor));
            Glide.with(context)
                    .load(RetrofitClient.BASE_POST_IMAGE_URL +  ImageURL)

                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource,
                                                    @Nullable Transition<? super Drawable> transition) {

                            blogTweet.setBackground(resource);

                        }
                    });

        }
        public void setTime(long time) {

            /*
            GetTimeAgo getTimeAgo = new GetTimeAgo();

            long lastTime = time;

            String lastSeenTime = GetTimeAgo.getTimeAgo(lastTime, context);

             */

            CharSequence date = FormatterUtil.getRelativeTimeSpanString(context, time);

            blogDate.setText(date);

        }

        public void setUserData(String name, String image){

            blogUserName.setText(name);
            Glide.with(context).load(image).into(blogUserImage);

        }

        public void updateLikesCount(int count){

            blogLikeCount.setText(count +" " + context.getString(R.string.likes));

        }
        public void UpdateCommentCount(int count , String image){
            blogCommentCount.setText(count + " " +context.getString(R.string.comments));
        }

        private void CreateLike(Post post , Course course , long time , int position) {



            final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            int owner_id = SharedPrefManager.getInstance(context).getUser().getId();

            String owner_name = SharedPrefManager.getInstance(context).getUser().getName();
            //Toast.makeText(this, "" + owner_name, Toast.LENGTH_SHORT).show();
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();


            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(RetrofitClient.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            ApiInterface api = retrofit.create(ApiInterface.class);


            //defining the call
            Call<LikePostResponse> call = api.createPostLike(
                    owner_id,
                    post.getId(),
                    0,
                    course.getId(),
                    time



            );
            call.enqueue(new Callback<LikePostResponse>() {
                @Override
                public void onResponse(Call<LikePostResponse> call, Response<LikePostResponse> response) {
                    // IncRate(lecture.getCourseId(), (int) ratingbar.getRating());
                    if (response.body() != null) {


                        if(response.message().equals("Like Added"))
                        {
                            blogLikeBtn.setPressed(true);
                            blogLikeBtn.setImageDrawable(ContextCompat.getDrawable(context , R.drawable.ic_active_like));
                            blogLikeCount.setText(post.getLikeCount() + " " + context.getResources().getString(R.string.likes));

                        }
                        else if(response.message().equals("Like Already Added"))
                        {
                            blogLikeBtn.setPressed(false);
                            blogLikeBtn.setImageDrawable(ContextCompat.getDrawable(context ,R.drawable.ic_like));

                        }
                    } else {
                        Log.d("Likes", "onResponse: " + response.errorBody());
                    }
                }

                @Override
                public void onFailure(Call<LikePostResponse> call, Throwable t) {

                    Log.d("Likes", "onFailure: " + t.getMessage());
                    //Toast.makeText(VideoPlayer.this, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }


    }
}
