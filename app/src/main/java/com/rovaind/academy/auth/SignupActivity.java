package com.rovaind.academy.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.card.MaterialCardView;
import com.rovaind.academy.Controllers.BaseActivity;
import com.rovaind.academy.R;

public class SignupActivity extends BaseActivity {
    MaterialCardView customerPanel , agentPanel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        customerPanel = findViewById(R.id.customerPanel);
        agentPanel = findViewById(R.id.agentPanel);

        customerPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, CreateStudentActivity.class);
                startActivity(intent);
            }
        });
        agentPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, CreateTeacherActivity.class);
                startActivity(intent);

            }
        });
    }
}