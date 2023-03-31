package com.gesturegenius;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button catalogRedirect;
    Button aslRedirect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        catalogRedirect = findViewById(R.id.catalogRedirect);

        catalogRedirect.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CatalogActivity.class));
            }
        });

        aslRedirect = findViewById(R.id.aslredirect);

        aslRedirect.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ASLActivity.class));
            }
        });

    }


}