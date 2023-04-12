package com.rovaind.academy.adapter;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rovaind.academy.Controllers.ReplyActivity;
import com.rovaind.academy.R;
import com.rovaind.academy.Utils.CircleImageView;
import com.rovaind.academy.Utils.ExpandableTextView;
import com.rovaind.academy.Utils.FormatterUtil;
import com.rovaind.academy.Utils.GetTimeAgo;
import com.rovaind.academy.Utils.ItemsUI;
import com.rovaind.academy.Utils.views.TextViewAr;
import com.rovaind.academy.manager.SharedPrefManager;
import com.rovaind.academy.manager.response.LikeResponse;
import com.rovaind.academy.model.Comment;
import com.rovaind.academy.model.Course;
import com.rovaind.academy.model.Lecture;
import com.rovaind.academy.retrofit.ApiInterface;
import com.rovaind.academy.retrofit.RetrofitClient;

import java.sql.Timestamp;
import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Mohamed El Sayed
 */
public class CommentsRecyclerAdapter extends RecyclerView.Adapter<CommentsRecyclerAdapter.ViewHolder> {

    public List<Comment> commentsList;
    public Course SelectedCourse;
    public Lecture SelectedLecture;
    public Context context;



    public CommentsRecyclerAdapter(List<Comment> commentsList , Course SelectedCourse , Lecture SelectedLecture){

        this.commentsList = commentsList;
        this.SelectedCourse = SelectedCourse;
        this.SelectedLecture = SelectedLecture;

    }

    @Override
    public CommentsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_list_item, parent, false);
        context = parent.getContext();

        return new CommentsRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CommentsRecyclerAdapter.ViewHolder holder, int position) {

        holder.setIsRecyclable(false);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String data = prefs.getString("blog_post_id", ""); //no id: default value
        final int user_id = commentsList.get(position).getId();
        String userName = commentsList.get(position).getOwnerName();
        String userImage = commentsList.get(position).getThumbImage();
        holder.setUserData(userImage);
        long time = commentsList.get(position).getCreatedAt();
        holder.setTime(time);
        holder.comment_like_count.setText(commentsList.get(position).getLikeCount() + " " + context.getResources().getString(R.string.likes));
        holder.comment_reply_count.setText(commentsList.get(position).getCommentCount() +" " + context.getResources().getString(R.string.comment));
        //final String blogPostId = comment_.getBlog_post_id();
        final String blogPostId = data;
        String commentMessage = commentsList.get(position).getContent();
        holder.setComment_message(commentMessage);

        Comment c = commentsList.get(position);
        holder.fillComment(userName, c, holder.commentTextView, holder.comment_time_stamp);
//        holder.ratingBar.setRating(commentsList.get(position).getRate());

        holder.show_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent commentIntent = new Intent(context, ReplyActivity.class);
                commentIntent.putExtra(ItemsUI.BUNDLE_COMMENTS,  commentsList.get(position) );
                commentIntent.putExtra(ItemsUI.BUNDLE_COURSES_DETAILS ,SelectedCourse);
                commentIntent.putExtra(ItemsUI.BUNDLE_COURSES_DETAILS_LECTURE ,SelectedLecture);
                context.startActivity(commentIntent);
            }
        });

        //like comment
        holder.image_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.CreateLike(commentsList.get(position) , SelectedCourse , SelectedLecture , time , position);
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



    }


    @Override
    public int getItemCount() {

        if(commentsList != null) {

            return commentsList.size();

        } else {

            return 0;

        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private TextViewAr comment_like_count , comment_reply_count;
        private TextViewAr comment_message;
        private CircleImageView owner_image;
        private TextViewAr blogUserName;
        //private CircleImageView blogUserImage;
        private TextViewAr comment_time_stamp;
        private final ExpandableTextView commentTextView;
        private RatingBar ratingBar;
        private TextView show_reply;
        private TextView image_like;

        // reply
        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            commentTextView = (ExpandableTextView) itemView.findViewById(R.id.commentText);
            image_like = mView.findViewById(R.id.comment_like);
            comment_like_count = itemView.findViewById(R.id.comment_like_count);
            comment_reply_count = itemView.findViewById(R.id.comment_reply_count);
            owner_image = itemView.findViewById(R.id.Comments_image);
            //blogUserImage = mView.findViewById(R.id.Comments_image);
            blogUserName = mView.findViewById(R.id.Comments_username);
            comment_message = mView.findViewById(R.id.Comments_message);
            comment_time_stamp = mView.findViewById(R.id.comment_time_stamp);
            ratingBar = mView.findViewById(R.id.ratingBar);
            show_reply = mView.findViewById(R.id.comment_reply);

            //

        }
        private void fillComment(String userName, Comment comment, ExpandableTextView commentTextView, TextView dateTextView) {
            Spannable contentString = new SpannableStringBuilder(userName + "   " + comment.getContent());
            contentString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.highlight_text)),
                    0, userName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            commentTextView.setText(contentString );

            CharSequence date = FormatterUtil.getRelativeTimeSpanString(context, comment.getCreatedAt());
            dateTextView.setText(date);
        }

        public void setComment_message(String message){


            comment_message.setText(message);

        }

        public void setUserData(String name, String image){


            blogUserName.setText(name);

            if(image != null) {
                Glide.with(context).load(image).into(owner_image);
            }

        }

        public void setUserData(String image){
            if(image != null) {
                Glide.with(context).load(image).into(owner_image);
            }

        }
        public void setTime(long time) {

            GetTimeAgo getTimeAgo = new GetTimeAgo();

            long lastTime = time;

            String lastSeenTime = GetTimeAgo.getTimeAgo(lastTime, context);

            comment_time_stamp.setText(lastSeenTime);

        }

        private void CreateLike(Comment comment , Course course , Lecture lecture , long time , int position) {



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
            Call<LikeResponse> call = api.createLike(
                    owner_id,
                    comment.getId(),
                    comment.getParentId(),
                    lecture.getId(),
                    course.getId(),
                    time



            );
            call.enqueue(new Callback<LikeResponse>() {
                @Override
                public void onResponse(Call<LikeResponse> call, Response<LikeResponse> response) {
                    // IncRate(lecture.getCourseId(), (int) ratingbar.getRating());
                    if (response.body() != null) {


                        if(response.message().equals("Like Added"))
                        {
                            image_like.setPressed(true);
                            comment_like_count.setText(commentsList.get(position).getLikeCount() + " " + context.getResources().getString(R.string.likes));

                        }
                        else if(response.message().equals("Like Already Added"))
                        {
                            image_like.setPressed(false);
                        }
                    } else {
                        Log.d("Likes", "onResponse: " + response.errorBody());
                    }
                }

                @Override
                public void onFailure(Call<LikeResponse> call, Throwable t) {

                    Log.d("Likes", "onFailure: " + t.getMessage());
                    //Toast.makeText(VideoPlayer.this, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }

    }
}
