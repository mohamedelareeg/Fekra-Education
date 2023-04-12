package com.rovaind.academy.adapter;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rovaind.academy.R;
import com.rovaind.academy.Utils.views.TextViewAr;
import com.rovaind.academy.interfaces.OnCenterClickListener;
import com.rovaind.academy.model.Center;

import java.util.List;

public class CentersAdapter extends RecyclerView.Adapter<CentersAdapter.ViewHolder>   {
    private List<Center> productsList;
    public Context context;


    private OnCenterClickListener onCataClickListener;
    public CentersAdapter(List<Center> _tradersList , OnCenterClickListener onCataClickListener )
    {
        this.productsList = _tradersList;

        this.onCataClickListener = onCataClickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_list_center_item , viewGroup , false);
        context = viewGroup.getContext();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        final Center category = productsList.get(i);

        String userName =category.getCenterName();
        viewHolder.setUserData(userName);
        viewHolder.subjectPanel.setOnClickListener(new View.OnClickListener() {
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

        RelativeLayout subjectPanel;
        private TextViewAr StoriesUserName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            subjectPanel =mView.findViewById(R.id.subjectPanel);
            StoriesUserName = mView.findViewById(R.id.subject_title);



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
