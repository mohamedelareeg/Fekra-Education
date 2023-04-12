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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.card.MaterialCardView;
import com.rovaind.academy.Controllers.PostCommentsActivity;
import com.rovaind.academy.R;
import com.rovaind.academy.Utils.ExpandableTweetView;
import com.rovaind.academy.Utils.FormatterUtil;
import com.rovaind.academy.Utils.GetTimeAgo;
import com.rovaind.academy.Utils.ItemsUI;
import com.rovaind.academy.Utils.views.AutoFitTextView;
import com.rovaind.academy.Utils.views.TextViewAr;
import com.rovaind.academy.interfaces.OnPostClickListener;
import com.rovaind.academy.manager.SharedPrefManager;
import com.rovaind.academy.model.Course;
import com.rovaind.academy.model.Post;
import com.rovaind.academy.model.User;
import com.rovaind.academy.retrofit.RetrofitClient;

import java.util.List;

/**
 * Created by Mohamed El Sayed
 */


public class BlogTeacherRecyclyerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "BlogTeacherRecyclyerAdapter";
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
    public BlogTeacherRecyclyerAdapter(List<Post> posts , Course selectedCourse , OnPostClickListener onPostClickListener )
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
                view = inflater.inflate(R.layout.single_list_item_teacher_tweet, parent, false);//ForTEST
                holder = new TweetsViewHolder(view);
                break;
            case Image_Text:
                view = inflater.inflate(R.layout.single_list_item_teacher_imagetxt, parent, false);
                holder = new ImagePhotoViewHolder(view);
                break;
            case Ordinary_Photo:
                view = inflater.inflate(R.layout.single_list_item_teacher_photo, parent, false);
                holder = new PhotosViewHolder(view);
                break;
            case PDF:
                view = inflater.inflate(R.layout.single_list_item_teacher_pdf, parent, false);
                holder = new PDFViewHolder(view);
                break;

            default:
                view = inflater.inflate(R.layout.single_list_item_teacher_tweet, parent, false);
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
        holder.fillTweet("", post, holder.expandableTweetView);
        holder.post_card.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent commentIntent = new Intent(context, PostCommentsActivity.class);
                commentIntent.putExtra(ItemsUI.BUNDLE_POSTS, post );
                commentIntent.putExtra(ItemsUI.BUNDLE_COURSES_DETAILS ,selectedCourse);
                context.startActivity(commentIntent);
            }
        });
        //gestureDetector = new GestureDetectorCompat((Activity) context, new SingleTapConfirm( blogPostId, c));
        //holder.fillTweet("", post, holder.expandableTweetView);;
        ///////////////////////////////////////////////////////////////////////



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
        //Tweets_Text
        final String desc_post = post.getContent();
        holder.setDescText_Post(desc_post , post.getBackImg() ,post.getTxtColor());
        holder.post_card.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent commentIntent = new Intent(context, PostCommentsActivity.class);
                commentIntent.putExtra(ItemsUI.BUNDLE_POSTS, post );
                commentIntent.putExtra(ItemsUI.BUNDLE_COURSES_DETAILS ,selectedCourse);
                context.startActivity(commentIntent);
            }
        });




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


        //BlogPost
        String image_url = post.getGallery().get(0).getThumbImage();
        Log.d("Photo", "onBindViewHolder_Photos: " + image_url);
        holder.setBlogImage(RetrofitClient.BASE_POST_URL + image_url);
        holder.fillTweet("", post, holder.expandableTweetView);
        holder.post_card.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent commentIntent = new Intent(context, PostCommentsActivity.class);
                commentIntent.putExtra(ItemsUI.BUNDLE_POSTS, post );
                commentIntent.putExtra(ItemsUI.BUNDLE_COURSES_DETAILS ,selectedCourse);
                context.startActivity(commentIntent);
            }
        });
        //gestureDetector = new GestureDetectorCompat((Activity) context, new SingleTapConfirm( blogPostId, c));
     //   holder.fillTweet("",post, holder.expandableTweetView , RetrofitClient.BASE_POST_URL + image_url);
        ///////////////////////////////////////////////////////////////////////



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



        holder.blogTweet.setOnClickListener(new OnClickListener() {
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



    }


    @Override
    public int getItemCount() {

        return posts.size();
    }
    public class TweetsViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private TextViewAr blogUserName;
        private ImageView blogUserImage;
        private TextViewAr blogDate;
        private ExpandableTweetView expandableTweetView;
        MaterialCardView post_card;

        public TweetsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            //Upper_Layout
            blogUserName = mView.findViewById(R.id.blog_user_name);
            blogUserImage = mView.findViewById(R.id.blog_user_image);
            blogDate = mView.findViewById(R.id.blog_date);
            post_card = mView.findViewById(R.id.post_card);
            //Content

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







    }
    public class ImagePhotoViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private TextViewAr blogUserName;
        private ImageView blogUserImage;
        private TextViewAr blogDate;
        private AutoFitTextView blogTweet;
        MaterialCardView post_card;
        LinearLayout post_relative_layout;
        public ImagePhotoViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            //Upper_Layout
            blogUserName = mView.findViewById(R.id.blog_user_name);
            blogUserImage = mView.findViewById(R.id.blog_user_image);
            blogDate = mView.findViewById(R.id.blog_date);
            post_card = mView.findViewById(R.id.post_card);
            //Content
            blogTweet = mView.findViewById(R.id.blog_Tweet);
            post_relative_layout = mView.findViewById(R.id.post_relative_layout);

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

                            post_relative_layout.setBackground(resource);

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



    }
    public class PhotosViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private TextView blogUserName;
        private ImageView blogUserImage;
        private TextView blogDate;
        private ImageView blogPhoto;
        private ExpandableTweetView expandableTweetView;
        MaterialCardView post_card;
        public PhotosViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            //Upper_Layout
            blogUserName = mView.findViewById(R.id.blog_user_name);
            blogUserImage = mView.findViewById(R.id.blog_user_image);
            blogDate = mView.findViewById(R.id.blog_date);
            post_card = mView.findViewById(R.id.post_card);
            //Content

            blogPhoto = mView.findViewById(R.id.blog_image);




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


        public void setBlogImage(String downloadUri){

            Glide.with(context).load(downloadUri).into(blogPhoto);

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





    }
    public class PDFViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private TextView blogUserName;
        private ImageView blogUserImage;
        private TextView blogDate;
        private ConstraintLayout blogTweet;
        public PDFViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            //Upper_Layout

            blogUserName = mView.findViewById(R.id.blog_user_name);
            blogUserImage = mView.findViewById(R.id.blog_user_image);
            blogDate = mView.findViewById(R.id.blog_date);

            //Content
            blogTweet = mView.findViewById(R.id.post_cont);


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




    }
}
