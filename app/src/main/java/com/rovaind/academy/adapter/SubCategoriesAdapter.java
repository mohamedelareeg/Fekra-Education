package com.rovaind.academy.adapter;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import com.rovaind.academy.R;
import com.rovaind.academy.Utils.views.TextViewAr;
import com.rovaind.academy.interfaces.OnCataClickListener;
import com.rovaind.academy.model.Category;
import com.rovaind.academy.retrofit.RetrofitClient;

import java.util.List;

public class SubCategoriesAdapter extends RecyclerView.Adapter<SubCategoriesAdapter.ViewHolder>   {
    private List<Category> productsList;
    public Context context;


    private OnCataClickListener onCataClickListener;
    public SubCategoriesAdapter(List<Category> _tradersList , OnCataClickListener onCataClickListener )
    {
        this.productsList = _tradersList;

        this.onCataClickListener = onCataClickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_list_pcategory_item , viewGroup , false);
        context = viewGroup.getContext();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        final Category category = productsList.get(i);



            if (category.getThumbImage() != null && !category.getThumbImage().equals("")) {
                String img = category.getThumbImage();
                Glide.with(context)
                        .load(RetrofitClient.BASE_CategoryImage_URL +img) // image url
                        .apply(new RequestOptions()
                                .override(512, 512) // resizing
                                .centerCrop())
                        .into(viewHolder.imageView);  // imageview object
            }



        String userName =category.getCatName();
        viewHolder.setUserData(userName);
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCataClickListener.onCategoryClicked(category, i);

            }
        });

        //viewHolder.price.setText(price);//cant cast to float

    }


    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private View mView;


        private ImageView imageView;
        private TextViewAr StoriesUserName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            imageView = mView.findViewById(R.id.ivImage);
            StoriesUserName = mView.findViewById(R.id.tvName);



        }
        public void setUserData(String name  ){


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                StoriesUserName.setText(Html.fromHtml(String.valueOf(name), Html.FROM_HTML_MODE_COMPACT));
            } else {
                StoriesUserName.setText(Html.fromHtml(String.valueOf(name)));
            }





        }

    }

}
