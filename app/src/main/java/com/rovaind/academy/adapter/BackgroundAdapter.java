package com.rovaind.academy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rovaind.academy.R;
import com.rovaind.academy.interfaces.OnSelectionListener;
import com.rovaind.academy.model.Postbackground;
import com.rovaind.academy.retrofit.RetrofitClient;

import java.util.List;

public class BackgroundAdapter extends RecyclerView.Adapter<BackgroundAdapter.CourseForYouViewHolder> {

    Context context;
    List<Postbackground> courseForYouList;
    OnSelectionListener onSelectionListener;
    public BackgroundAdapter(Context context, List<Postbackground> courseForYouList , OnSelectionListener onSelectionListener) {
        this.context = context;
        this.courseForYouList = courseForYouList;
        this.onSelectionListener = onSelectionListener;
    }

    @NonNull
    @Override
    public CourseForYouViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.inital_image, parent, false);

        //here we need to create a row item layout file
        return new CourseForYouViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CourseForYouViewHolder holder, int position) {

        //here we will bind data to our layout
        final Postbackground postbackground = courseForYouList.get(position);


        Glide.with(context).load(RetrofitClient.BASE_POST_IMAGE_URL +postbackground.getBackImg()).into(holder.preview);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*
                if (holder.selection.getVisibility() == View.VISIBLE) {
                    // Its visible
                    holder.selection.setVisibility(View.GONE);
                } else {
                    // Either gone or invisible
                    holder.selection.setVisibility(View.VISIBLE);
                }

                 */
                onSelectionListener.OnClick(postbackground , position);
                //onSelectionListener.OnLongClick(postbackground , position);

            }
        });


    }

    @Override
    public int getItemCount() {
        return courseForYouList.size();
    }

    public  static class CourseForYouViewHolder extends RecyclerView.ViewHolder {


        ImageView preview;
        ImageView selection;

        public CourseForYouViewHolder(@NonNull View itemView) {
            super(itemView);

            preview = itemView.findViewById(R.id.preview);
            selection = itemView.findViewById(R.id.selection);

        }


    }




}
