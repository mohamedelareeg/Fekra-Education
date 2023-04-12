/*
 *  Copyright (c) 2018 Gabriele Corso
 *
 *  Distributed under the MIT software license, see the accompanying
 *  file LICENSE or http://www.opensource.org/licenses/mit-license.php.
 */

package com.rovaind.academy.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import androidx.recyclerview.widget.RecyclerView;

import com.rovaind.academy.Controllers.QuizSectionActivity;
import com.rovaind.academy.R;
import com.rovaind.academy.Utils.FitDoughnut;
import com.rovaind.academy.Utils.views.TextViewAr;
import com.rovaind.academy.model.quiz.Lesson;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter to manage and display the horizontal scroll list view used for lessons in the Home activity and Browse activity
 */

public class QuizLessonsHorizAdapter extends RecyclerView.Adapter<QuizLessonsHorizAdapter.SimpleViewHolder> {

    private Context context;
    private List<Lesson> lessons;
    private int courseid;

    public QuizLessonsHorizAdapter(Context context, List<Lesson> elements, int courseid) {
        this.context = context;
        this.lessons = elements;
        this.courseid = courseid;
        if (lessons == null) {
            lessons = new ArrayList<>();
        }
    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        public final TextViewAr tvLessonTitle;
        public final RelativeLayout allView;
        public final ImageView iconLesson;
        public final FitDoughnut doughnut;

        public SimpleViewHolder(View view) {
            super(view);
            tvLessonTitle = (TextViewAr) view.findViewById(R.id.lesson_title);
            allView = (RelativeLayout) view.findViewById(R.id.allview);
            doughnut = (FitDoughnut) view.findViewById(R.id.doughnutres);
            iconLesson = view.findViewById(R.id.lesson_icon);
        }
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(this.context).inflate(R.layout.quizlesson_item_h, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder holder, final int position) {
        holder.tvLessonTitle.setText(lessons.get(position).getLessonTitle());

        String backgroundname = "course" + Integer.toString(courseid);
        holder.iconLesson.setBackgroundResource(context.getResources().getIdentifier(backgroundname,
                "drawable", context.getPackageName()));

        String iconname = "z" + Integer.toString(lessons.get(position).getId());
        holder.iconLesson.setImageResource(context.getResources().getIdentifier(iconname,
                "drawable", context.getPackageName()));


        /* //TODO
        if (lessons.get(position).getResult() > 0) {
            holder.doughnut.setVisibility(View.VISIBLE);
            holder.doughnut.animateSetPercent(((float) lessons.get(position).getResult() * 10) - 0.01f);
        }

         */


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lessons.get(position).getNsections() == 0) {
/*
                    Toast.makeText(context, "Sorry, this lesson is not available yet. Try instead the lessons on Passwords, Viruses, VPN and GDPR.", Toast.LENGTH_LONG).show();
*/
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.setTitle("Not available");
                    alertDialog.setMessage("Sorry, this lesson is not available yet. Try instead the lessons on Passwords, Viruses, VPN and GDPR.");
                    alertDialog.setPositiveButton("OK",    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.show();
                } else {
                    Intent intent = new Intent(context, QuizSectionActivity.class);
                    intent.putExtra("sectionn", 0);
                    intent.putExtra("lessonid", lessons.get(position).getId());
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return this.lessons.size();
    }
}