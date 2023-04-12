/*
 *  Copyright (c) 2018 Gabriele Corso
 *
 *  Distributed under the MIT software license, see the accompanying
 *  file LICENSE or http://www.opensource.org/licenses/mit-license.php.
 */

package com.rovaind.academy.Controllers;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;

import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;


import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.cunoraz.gifview.library.GifView;
import com.rovaind.academy.HomeActivity;
import com.rovaind.academy.R;
import com.rovaind.academy.Utils.ItemsUI;
import com.rovaind.academy.Utils.Toasty.Toasty;
import com.rovaind.academy.Utils.views.TextViewAr;
import com.rovaind.academy.manager.Questions;
import com.rovaind.academy.model.Course;
import com.rovaind.academy.model.Lecture;
import com.rovaind.academy.model.quiz.Question;
import com.rovaind.academy.retrofit.ApiInterface;
import com.rovaind.academy.retrofit.RetrofitClient;
import com.thekhaeng.pushdownanim.PushDownAnim;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizActivity extends BaseActivity {
    private static final String TAG =  "QuizActivity";
    ApiInterface apiInterface;
    String[] questions;
  //  String lessontitle;
    //LessonsLDH lessonsLDH;
    int curq;
    int lessonid;
    public static final String Q_SEP = "<<-->>";
    public static final String A_SEP = "<->";
    boolean[] results;

    TextViewAr lessontitleTv;
    TextViewAr questiontitleTv;
    AppCompatButton[] answersBt;
    ImageView[] resqIv;
    RelativeLayout emptyview;
    ImageView closeBt;
    Lecture lecture;
    Course course;
    private AppCompatTextView timer_text;
    private ProgressBar timer_progress;
    private CountDownTimer timer;
    private ObjectAnimator animator;
    private GifView gif_questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        Intent intent = getIntent();
        lessonid = intent.getIntExtra("lessonid", 0);
        lecture = (Lecture) getIntent().getExtras().getSerializable(ItemsUI.BUNDLE_COURSES_DETAILS);
        course = (Course) getIntent().getExtras().getSerializable(ItemsUI.BUNDLE_COURSES_DETAILS_LECTURE);
       // lessonsLDH = LessonsLDH.getInstance(this);
       // String q_questions = lessonsLDH.getQuestions(lessonid); //TODO
      //  lessontitle = lessonsLDH.getLessonTitle(lessonid);
        gif_questions = findViewById(R.id.gif_questions);
        timer_text = findViewById(R.id.timer_text);
        timer_progress = findViewById(R.id.timer_progress);
        LoadLQuestions(lessonid);
        makeButtonsClickable();
        playGifAnimation(R.mipmap.gif_thinking);
        prepareTimer(20000L);


    }

    private void LoadLQuestions(int lessonid ) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final String data = prefs.getString("language", "ar"); //no id: default value
        apiInterface = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);

        Call<Questions> call = apiInterface.getQuestionByID(lessonid);

        call.enqueue(new Callback<Questions>() {
            @Override
            public void onResponse(Call<Questions> call, Response<Questions> response) {


                if(response.body() != null)
                {

                    SetQuestions(response.body().getQuestions() , lessonid);
                }
                else
                {
                    Log.d(TAG, "onResponse: " + response.message()) ;
                    Log.d(TAG, "onResponse: " + response.code()) ;
                    Log.d(TAG, "onResponse: " + response.body()) ;
                    Log.d(TAG, "onResponse: " + response.errorBody().toString()) ;
                }
                // setLessonList(response.body().getLectures());

            }



            @Override
            public void onFailure(Call<Questions> call, Throwable t) {

            }
        });
    }

    private void SetQuestions(ArrayList<Question> questionsList, int lessonid) {
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < questionsList.size() ; i++)
        {
            sb.append(questionsList.get(i).getQuestion() + questionsList.get(i).getAnswers());
        }
        String q_questions = sb.toString();
        questions = q_questions.split(Q_SEP);
        curq = 0;

        lessontitleTv = findViewById(R.id.lesson_title);
        questiontitleTv = findViewById(R.id.question_title);

        answersBt = new AppCompatButton[]{findViewById(R.id.answer_text_a), findViewById(R.id.answer_text_b), findViewById(R.id.answer_text_c), findViewById(R.id.answer_text_d)};
        resqIv = new ImageView[]{findViewById(R.id.resq0), findViewById(R.id.resq1), findViewById(R.id.resq2),
                findViewById(R.id.resq3), findViewById(R.id.resq4),findViewById(R.id.resq5),findViewById(R.id.resq6),
                findViewById(R.id.resq7),findViewById(R.id.resq8),findViewById(R.id.resq9)};
        emptyview = findViewById(R.id.emptyview);
        emptyview.setVisibility(View.GONE);
        PushDownAnim.setPushDownAnimTo(answersBt);
        lessontitleTv.setText(lecture.getLectureName());

        results = new boolean[]{false, false, false, false, false, false, false, false, false, false};

        emptyview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextQuestion();
            }
        });

        setCurrentQuestion();

        closeBt = findViewById(R.id.closebt);
        closeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuizActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void nextQuestion(){
        if(curq == 9){
            int tot = 0;
            for(int i = 0; i<10; i++){
                if(results[i]) {
                    tot++;
                }
            }

            //int ch_result = 5*lessonsLDH.updateResult(lessonid, tot);

            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.result_dialog, null);

            final android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(QuizActivity.this).setView(dialogView).show();

            TextViewAr tvCompl = dialogView.findViewById(R.id.tvcompl);
            TextViewAr tvScore = dialogView.findViewById(R.id.tvscore);
            TextViewAr tvResult = dialogView.findViewById(R.id.tvresult);

            if(tot == 10){
                tvCompl.setText(getResources().getString(R.string.perfect));
            } else if (tot>=7){
                tvCompl.setText(getResources().getString(R.string.well_done));
            } else {
                tvCompl.setText(getResources().getString(R.string.try_again));
                tvCompl.setTextColor(getResources().getColor(R.color.wrong));
                TextViewAr tvExtra = dialogView.findViewById(R.id.tvextra);
                tvExtra.setVisibility(View.VISIBLE);
            }


            String res = getResources().getString(R.string.score)+"\n" + Integer.toString(tot) + "/10";
            tvResult.setText(res);


            String change = "+" + Integer.toString(tot) + "\n" + getResources().getString(R.string.pointscored);
            tvScore.setText(change);





            /*
            // level
            Level level = lessonsLDH.getLevel();
            FitDoughnut doughnut = (FitDoughnut) dialogView.findViewById(R.id.doughnuttot);
            doughnut.animateSetPercent((float) level.getPerctot());
            TextViewAr tvperctot = dialogView.findViewById(R.id.tvpercentage);
            String p = Integer.toString(level.getPerctot())+ "%";
            tvperctot.setText(p);
            TextViewAr tvLev = dialogView.findViewById(R.id.tvlevel);
            tvLev.setText(level.getLiv());
            TextViewAr tvProg = dialogView.findViewById(R.id.tvprogress);
            String prog = Integer.toString(level.getProg()) + " / " + Integer.toString(level.getTot());
            tvProg.setText(prog);

            */
            Button btTryAgain = dialogView.findViewById(R.id.btTryAgain);
            btTryAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    timer.cancel();
                    Intent intent = new Intent(QuizActivity.this, QuizSectionActivity.class);
                    intent.putExtra(ItemsUI.BUNDLE_COURSES_DETAILS, (Serializable) lecture);
                    intent.putExtra(ItemsUI.BUNDLE_COURSES_DETAILS_LECTURE, (Serializable) course);
                    intent.putExtra("sectionn", 0);
                    intent.putExtra("lessonid", lecture.getId());
                    startActivity(intent);
                }
            });

            Button btHome = dialogView.findViewById(R.id.btHome);
            btHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    timer.cancel();
                    dialog.dismiss();
                    Intent intent = new Intent(QuizActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
            });

        } else {
            emptyview.setVisibility(View.GONE);
            curq++;
            setCurrentQuestion();
        }
    }

    private void setCurrentQuestion(){

        if (animator != null)
            animator.cancel();
        playGifAnimation(R.mipmap.gif_thinking);
        prepareTimer(20000L);

        String[] parts = questions[curq].split(A_SEP);
        questiontitleTv.setText(parts[0]);
        final int correctans;

        if(parts.length == 2){
            // true or false
            answersBt[0].setBackground(getResources().getDrawable(R.drawable.answerbox));
            answersBt[1].setBackground(getResources().getDrawable(R.drawable.answerbox));
            answersBt[0].setText(getResources().getString(R.string.trueanswer));
            answersBt[1].setText(getResources().getString(R.string.falseanswer));
            answersBt[2].setVisibility(View.GONE);
            answersBt[3].setVisibility(View.GONE);

            if(parts[1].charAt(0)== 'F'){
                correctans = 1;
            } else {
                correctans = 0;
            }
        } else {
            // 4 multiple choices

            List<Integer> list = new ArrayList<Integer>();
            list.add(0);
            list.add(1);
            list.add(2);
            list.add(3);
            java.util.Collections.shuffle(list);

            correctans = list.indexOf(0);

            for(int i = 0; i<4; i++){
                answersBt[i].setVisibility(View.VISIBLE);
                answersBt[i].setBackground(getResources().getDrawable(R.drawable.answerbox));
                answersBt[i].setText(parts[list.get(i)+1]);
            }
        }

        answersBt[correctans].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answersBt[correctans].setBackground(getResources().getDrawable(R.drawable.answerboxcorrect));
                results[curq] = true;
                resqIv[curq].setBackground(getResources().getDrawable(R.drawable.qcorrect));
                emptyview.setVisibility(View.VISIBLE);

                //makeBlinkEffect(clickedButton, Color.GREEN);
                playGifAnimation(R.mipmap.gif_true);
                playSound(R.raw.sound_tick);
               // makeButtonsUnClickable();
                Toasty.success(QuizActivity.this, getResources().getString(R.string.correct_question), Toast.LENGTH_SHORT, true).show();

            }
        });

        for(int i = 0; i<4; i++){
            if(i != correctans){
                final int finalI = i;
                answersBt[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        answersBt[correctans].setBackground(getResources().getDrawable(R.drawable.answerboxcorrect));
                        answersBt[finalI].setBackground(getResources().getDrawable(R.drawable.answerboxwrong));
                        resqIv[curq].setBackground(getResources().getDrawable(R.drawable.qwrong));
                        emptyview.setVisibility(View.VISIBLE);

                        playGifAnimation(R.mipmap.gif_false);
                        //makeBlinkEffect(clickedButton, Color.RED);
                        playSound(R.raw.sound_false);
                        //makeButtonsUnClickable();
                        Toasty.error(QuizActivity.this, getResources().getString(R.string.wrong_answer), Toast.LENGTH_SHORT, true).show();

                    }
                });
            }
        }
    }

    public void prepareTimer(Long totalTime) {
        if (timer != null)
            timer.cancel();

        timer = new CountDownTimer(totalTime, 1000) {
            public void onTick(long millisUntilFinished) {
                int seconds  = (int) (millisUntilFinished / 1000);
                int hours    = seconds / (60 * 60);
                int tempMint = (seconds - (hours * 60 * 60));
                int minutes  = tempMint / 60;
                seconds = tempMint - (minutes * 60);

                timer_progress.setProgress(seconds);
                timer_text.setText(String.valueOf(seconds));
            }

            public void onFinish() {
                Toasty.warning(QuizActivity.this, getResources().getString(R.string.time_out), Toast.LENGTH_SHORT, true).show();
                timer_text.setText("0");
                makeButtonsUnClickable();
            }
        }.start();
    }

    private void playGifAnimation(int id) {
        gif_questions.setGifResource(id);
        gif_questions.play();
    }

    private void playSound(int id) {
        final MediaPlayer mp = MediaPlayer.create(this, id);
        mp.start();
    }

    private void makeBlinkEffect(Button button, int color) {
        animator = ObjectAnimator.ofInt(button, "backgroundColor",
                getResources().getColor(R.color.answer_button_color), color);
        animator.setDuration(1000);
        animator.setEvaluator(new ArgbEvaluator());
        animator.setRepeatCount(Animation.REVERSE);
        animator.start();
    }

    private void makeButtonsUnClickable() {


        resqIv[curq].setBackground(getResources().getDrawable(R.drawable.qwrong));
        emptyview.setVisibility(View.VISIBLE);
        playGifAnimation(R.mipmap.gif_false);
        //makeBlinkEffect(clickedButton, Color.RED);
        playSound(R.raw.sound_false);
        /*
        answersBt[1].setClickable(false);
        answersBt[2].setClickable(false);
        answersBt[3].setClickable(false);
        answersBt[4].setClickable(false);

         */

    }

    private void makeButtonsClickable() {
        /*
        answersBt[1].setClickable(true);
        answersBt[2].setClickable(true);
        answersBt[3].setClickable(true);
        answersBt[4].setClickable(true);

         */
    }
}
