package com.rovaind.academy.Controllers;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.rovaind.academy.R;
import com.rovaind.academy.Utils.ItemsUI;
import com.rovaind.academy.model.Course;
import com.rovaind.academy.model.Lecture;
import com.rovaind.academy.model.Post;
import com.rovaind.academy.retrofit.RetrofitClient;

public class PDFViewerActivity extends BaseActivity {

    private static final String TAG = "PDFViewerActivity" ;
    WebView webView;

    private Post post;

    Lecture lecture;
    private Course course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_d_f_viewer);
        lecture = (Lecture) getIntent().getExtras().getSerializable(ItemsUI.BUNDLE_COURSES_DETAILS);
        course = (Course) getIntent().getExtras().getSerializable(ItemsUI.BUNDLE_COURSES_DETAILS_LECTURE);


        webView = findViewById(R.id.WV);


        //String path="https://github.github.com/training-kit/downloads/github-git-cheat-sheet.pdf";

        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setJavaScriptEnabled(true);
        String url = "https://docs.google.com/gview?embedded=true&url=" +  RetrofitClient.BASE_VIDEO_URL + lecture.getVideoUrlyou();
        webView.loadUrl(url);

        /*
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setWebChromeClient(new WebChromeClient());
        Intent intent = getIntent();
        final int position = intent.getIntExtra("position",0);
        webView.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageFinished(WebView view, String url) {
                webView.loadUrl("javascript:(function() { " +
                        "document.querySelector('[role=\"toolbar\"]').remove();})()");
                progressBar.setVisibility(View.GONE);
            }
        });
        //https://docs.google.com/viewerng/viewer?embedded=true&url=
        webView.loadUrl(RetrofitClient.BASE_VIDEO_URL + lecture.getVideoUrlyou());

         */
    }
}