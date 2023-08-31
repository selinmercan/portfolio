package com.example.trojancheckincheckout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import androidx.core.content.res.ResourcesCompat;
import android.graphics.Typeface;
import android.widget.TextView;

public class ForgotPasswordAccountActivity extends AppCompatActivity {

    Database db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_account);

        TextView textView = findViewById(R.id.check);

        Typeface typeface = ResourcesCompat.getFont(
                this,
                R.font.poppins);
        textView.setTypeface(typeface);

        db = new Database();

        Button button = (Button) findViewById(R.id.studentbutton);
        Button button2 = (Button) findViewById(R.id.managerbutton);

    }

    public void managerPressed(View view){
        Intent i = new Intent(this, ForgotPasswordManagerActivity.class);
        startActivity(i);
    }

    public void studentPressed(View view){
        Intent i = new Intent(this, ForgotPasswordActivity.class);
        startActivity(i);
    }

    public void alert(String message){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alert.setMessage(message);

        AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }

    public void goBack(View view){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

}