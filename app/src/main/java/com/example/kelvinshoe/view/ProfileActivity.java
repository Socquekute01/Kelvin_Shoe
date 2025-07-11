package com.example.kelvinshoe.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kelvinshoe.R;
import com.example.kelvinshoe.model.User;
import com.example.kelvinshoe.utils.DataManager;

public class ProfileActivity extends AppCompatActivity {
    private DataManager dataManager;

    EditText et_full_name, et_email, et_username, et_password;
    Button btn_save;

    private void InitComponent() {
        dataManager = new DataManager(this);
        et_full_name = findViewById(R.id.et_full_name);
        et_email = findViewById(R.id.et_email);
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        btn_save = findViewById(R.id.btnSave);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        InitComponent();

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int userId = Integer.parseInt(sharedPreferences.getString("userId", "-1"));

        // Get userId from Intent
        if (userId == -1) {
            Toast.makeText(this, "Invalid session. Please log in again.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        User user = dataManager.getUserById(userId);
        et_full_name.setText(user.getFullName());
        et_email.setText(user.getEmail());
        et_password.setText(user.getPassword());
        et_username.setText(user.getUsername());

        AddListener();
    }

    private void AddListener() {
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = new User(et_username.toString(), et_password.toString(), et_email.toString(), et_full_name.toString());
                int result = dataManager.updateUser(user);
                if (result != -1)
                    Toast.makeText(getBaseContext(), "Cập nhật dữ liệu thành công.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
