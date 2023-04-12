package com.rovaind.academy.adapter;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.rovaind.academy.R;
import com.rovaind.academy.Utils.views.TextViewAr;
import com.rovaind.academy.interfaces.OnCataClickListener;
import com.rovaind.academy.model.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ClassCategoriesAdapter extends RecyclerView.Adapter<ClassCategoriesAdapter.ViewHolder>   {
    private List<Category> productsList;
    public Context context;
    Random random = new Random();

    private OnCataClickListener onCataClickListener;
    public ClassCategoriesAdapter(List<Category> _tradersList , OnCataClickListener onCataClickListener )
    {
        this.productsList = _tradersList;

        this.onCataClickListener = onCataClickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_list_ccategory_item , viewGroup , false);
        context = viewGroup.getContext();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        final Category category = productsList.get(i);


        String userName =category.getCatName();
        viewHolder.setUserData(userName);
        int Color = viewHolder.colors.get(random.nextInt(8));
        viewHolder.StoriesUserName.setBackgroundColor(Color);
        viewHolder.backClass.setBackgroundColor(Color);
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
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
        private LinearLayout backClass;
        private CardView cardView;
        List<Integer> colors = new ArrayList<>();
        private TextViewAr StoriesUserName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            cardView = mView.findViewById(R.id.card_view);
            StoriesUserName = mView.findViewById(R.id.tvName);
            backClass = mView.findViewById(R.id.backClass);
            colors.add(context.getResources().getColor(R.color.red_500));
            colors.add(context.getResources().getColor(R.color.pink_500));
            colors.add(context.getResources().getColor(R.color.purple_500));
            colors.add(context.getResources().getColor(R.color.indigo_500));
            colors.add(context.getResources().getColor(R.color.blue_500));
            colors.add(context.getResources().getColor(R.color.cyan_500));
            colors.add(context.getResources().getColor(R.color.green_500));
            colors.add(context.getResources().getColor(R.color.lime_500));



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
