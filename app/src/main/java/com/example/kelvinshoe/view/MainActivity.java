package com.example.kelvinshoe.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kelvinshoe.R;
import com.example.kelvinshoe.model.User;
import com.example.kelvinshoe.utils.DataManager;
import com.example.kelvinshoe.utils.DatabaseHelper;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private DataManager dbManager;
    private EditText txtUsername, txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dbHelper = new DatabaseHelper(this);
        dbManager = new DataManager(this);

        Button btnLogin = findViewById(R.id.btn_login);
        txtUsername = findViewById(R.id.et_username);
        txtPassword = findViewById(R.id.et_password);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                String username = txtUsername.getText().toString();
                String password = txtPassword.getText().toString();
                User userInformation = dbManager.login(username, password);

                if (userInformation != null) {
                    Toast.makeText(MainActivity.this, "Login success!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, ProductListActivity.class);
                    intent.putExtra("userId", userInformation.getUserId());
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(MainActivity.this, "Username or password wrong. Try again!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        TextView tvSignUp = findViewById(R.id.tv_sign_up);
        tvSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}