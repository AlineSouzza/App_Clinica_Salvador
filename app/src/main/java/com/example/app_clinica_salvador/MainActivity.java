package com.example.app_clinica_salvador;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.app_clinica_salvador.register.FormActivity;

public class MainActivity extends AppCompatActivity {

    private Button buttonRegister;
    private Button buttonList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        buttonRegister = (Button) findViewById(R.id.button_register);
        buttonList = (Button) findViewById(R.id.button_list);

        buttonRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FormActivity.class);
                startActivity(intent);
            }
        });

    }
}