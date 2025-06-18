package com.example.kelvinshoe.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kelvinshoe.R;
import com.example.kelvinshoe.model.User;
import com.example.kelvinshoe.utils.DataManager;

public class RegisterActivity extends AppCompatActivity {
    private DataManager dbManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbManager = new DataManager(this);

        EditText etFullName = findViewById(R.id.et_full_name);
        EditText etEmail = findViewById(R.id.et_email);
        EditText etUsername = findViewById(R.id.et_username);
        EditText etPassWord = findViewById(R.id.et_password);
        EditText etCFPassword = findViewById(R.id.et_confirm_password);

        Button btnRegister = findViewById(R.id.btn_sign_up);

        btnRegister.setOnClickListener(v -> {
            String fullName = etFullName.getText().toString();
            String email = etEmail.getText().toString();
            String username = etUsername.getText().toString();
            String password = etPassWord.getText().toString();
            String cf_password = etCFPassword.getText().toString();

            if (password.equals(cf_password)) {
                if (fullName.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "All fields not empty", Toast.LENGTH_SHORT).show();
                }
                else {
                    User newUser = new User(username, password, email, fullName);
                    User userInformation = dbManager.addUser(newUser);
                    if (userInformation != null) {
                        // Lưu dữ liệu vào shared preference
                        String userID = String.valueOf(userInformation.getUserId());
                        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("userId", userID);
                        editor.apply();

                        Toast.makeText(RegisterActivity.this, "Register success!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, ProductListActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Toast.makeText(RegisterActivity.this, "User was existed!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            else {
                Toast.makeText(RegisterActivity.this, "Password and confirm password not similar", Toast.LENGTH_SHORT).show();
            }
        });
        TextView tvLogin = findViewById(R.id.tv_login);
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbManager.close();
    }
}
