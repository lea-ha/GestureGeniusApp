package com.gesturegenius;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FirstTimeActivity extends AppCompatActivity {

    Button firsttimelogin, firsttimesignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time);

        firsttimelogin = findViewById(R.id.loginmain_button);
        firsttimesignup = findViewById(R.id.signupmain_button);

        firsttimesignup.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                startActivity(new Intent(FirstTimeActivity.this, SignUpActivity.class));
            }
        });

        firsttimelogin.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                startActivity(new Intent(FirstTimeActivity.this, LoginActivity.class));
            }
        });
    }
}