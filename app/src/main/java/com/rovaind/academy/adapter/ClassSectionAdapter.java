package com.rovaind.academy.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;

import com.rovaind.academy.R;
import com.rovaind.academy.Utils.ExpandableRecyclerView;
import com.rovaind.academy.Utils.views.TextViewAr;
import com.rovaind.academy.interfaces.OnCategoryClickListener;
import com.rovaind.academy.model.Category;

import java.util.List;


public class ClassSectionAdapter extends ExpandableRecyclerView.Adapter<ClassSectionAdapter.ChildViewHolder, ExpandableRecyclerView.SimpleGroupViewHolder,String,String>
{
    private int last_selected_group = -1;
    private int last_selected_position = -1;
    private int selected_group = -1;
    private int selected_position = -1;
    public Context context;
    List<Category> categoryList;

    private OnCategoryClickListener onCategoryClickListener;
    public ClassSectionAdapter(Context context, List<Category> categoryList , OnCategoryClickListener onCategoryClickListener) {
        this.context = context;
        this.categoryList = categoryList;
        this.onCategoryClickListener = onCategoryClickListener;
    }

    @Override
    public int getGroupItemCount() {
        Log.d("Group", "getGroupItemCount: " + categoryList.size());
        return categoryList.size() - 1;

    }

    @Override
    public int getChildItemCount(int i) {
        return categoryList.get(i).getSubcategory().size();
    }

    @Override
    public String getGroupItem(int i) {
        return  categoryList.get(i).getCatName();
    }

    @Override
    public String getChildItem(int group, int child) {
        return categoryList.get(group).getSubcategory().get(child).getCatName();
    }

    @Override
    protected ExpandableRecyclerView.SimpleGroupViewHolder onCreateGroupViewHolder(ViewGroup parent)
    {
        return new ExpandableRecyclerView.SimpleGroupViewHolder(parent.getContext());
    }

    @Override
    protected ChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType)
    {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_row_item,parent,false);
        return new ChildViewHolder(rootView);
    }

    @Override
    public int getChildItemViewType(int group, int position, int type) {
        return 1;
    }

    @Override
    public void onBindGroupViewHolder(ExpandableRecyclerView.SimpleGroupViewHolder holder, int group) {
        super.onBindGroupViewHolder(holder, group);
        holder.setText(getGroupItem(group));
        /*
        final LessonSection lessonSection = lessonSectionList.get(2);
        holder.setText(lessonSection.getLectureName());

         */

    }

    @Override
    public void onBindChildViewHolder(ChildViewHolder holder, int group, int position)
    {
        super.onBindChildViewHolder(holder, group, position);
        //here we will bind data to our layout
        final Category mainCategory = categoryList.get(group);
        final Category.Subcategory subcategory = categoryList.get(group).getSubcategory().get(position);

        holder.lessonName.setText(subcategory.getCatName());

        if (selected_position == position && selected_group == group) {
            holder.lessonName.setTextColor(context.getResources().getColor(R.color.white));
            holder.CategoryCard.setBackgroundColor(context.getResources().getColor(R.color.blue_800));
        }
        else
        {
            holder.lessonName.setTextColor(context.getResources().getColor(R.color.black));
            holder.CategoryCard.setBackgroundColor(0);
        }
        holder.CategoryCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (selected_position != position) {

                        holder.selected = true;
                        last_selected_group = selected_group;
                        selected_group = group;
                        last_selected_position = selected_position;
                        selected_position = position;
                        holder.lessonName.setTextColor(context.getResources().getColor(R.color.white));
                        holder.CategoryCard.setBackgroundColor(context.getResources().getColor(R.color.blue_800));

                        if(selected_position != -1) {
                            notifyItemChanged(last_selected_position);
                        }


                        notifyDataSetChanged();

                    } else {

                        holder.selected = false;
                        selected_group = -1;
                        selected_position = -1;
                        holder.lessonName.setTextColor(context.getResources().getColor(R.color.black));
                        holder.CategoryCard.setBackgroundColor(0);
                        notifyDataSetChanged();

                    }
                onCategoryClickListener.onCategoryClicked(mainCategory , group, position);

            }
        });

    }



    public class ChildViewHolder extends ExpandableRecyclerView.ViewHolder
    {
        boolean selected = false;
        TextViewAr lessonName ;
        CardView CategoryCard;
        public ChildViewHolder(View itemView) {
            super(itemView);
            lessonName = itemView.findViewById(R.id.lesson_name);
            CategoryCard = itemView.findViewById(R.id.CategoryCard);

        }
    }
}
