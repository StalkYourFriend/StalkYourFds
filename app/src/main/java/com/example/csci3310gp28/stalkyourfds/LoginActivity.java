package com.example.csci3310gp28.stalkyourfds;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    TextView registerLink;
    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Go to register screen
        registerLink = (TextView) findViewById(R.id.login_register_tv);
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });

        // TEMPORARY Go to stalk screen
        loginBtn = (Button) findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(loginIntent);
            }
        });
    }
}
