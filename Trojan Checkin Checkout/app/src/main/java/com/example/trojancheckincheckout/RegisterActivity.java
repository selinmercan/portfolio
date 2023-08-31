package com.example.trojancheckincheckout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;
import android.graphics.Typeface;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TextView textView = findViewById(R.id.register);

        Typeface typeface = ResourcesCompat.getFont(
                this,
                R.font.poppins);
        textView.setTypeface(typeface);
    }

    public void studentButtonPress(View view){
        Intent i = new Intent(this, StudentRegisterActivity.class);
        startActivity(i);
    }

    public void managerButtonPress(View view){
        Intent i = new Intent(this, ManagerRegisterActivity.class);
        startActivity(i);
    }

    public void goBack(View view){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void restorePress(View view) {
        Intent i = new Intent(this, RestoreActivity.class);
        startActivity(i);
    }
}