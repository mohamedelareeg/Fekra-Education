package com.rovaind.academy.adapter;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.rovaind.academy.R;
import com.rovaind.academy.Utils.views.TextViewAr;
import com.rovaind.academy.interfaces.OnCataClickListener;
import com.rovaind.academy.model.Category;

import java.util.List;

public class SubjectsAdapter extends RecyclerView.Adapter<SubjectsAdapter.ViewHolder>   {
    private List<Category> productsList;
    public Context context;


    private OnCataClickListener onCataClickListener;
    public SubjectsAdapter(List<Category> _tradersList , OnCataClickListener onCataClickListener )
    {
        this.productsList = _tradersList;

        this.onCataClickListener = onCataClickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_list_subject_item , viewGroup , false);
        context = viewGroup.getContext();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        final Category category = productsList.get(i);

        String userName =category.getCatName();
        viewHolder.setUserData(userName , category.getThumbImage());
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

        CardView subjectPanel;
       // private KenBurnsView SubjectImg;
        private TextViewAr SubjectName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            subjectPanel =mView.findViewById(R.id.SubjectPanel);
         //   SubjectImg = mView.findViewById(R.id.SubjectIMG);
            SubjectName = mView.findViewById(R.id.SubjectName);



        }
        public void setUserData(String name , String thumb  ){


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                SubjectName.setText(Html.fromHtml(String.valueOf(name), Html.FROM_HTML_MODE_COMPACT));

                //StoriesUserName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_student, 0, 0, 0);
            } else {
                SubjectName.setText(Html.fromHtml(String.valueOf(name)));
            }





        }

    }

}
