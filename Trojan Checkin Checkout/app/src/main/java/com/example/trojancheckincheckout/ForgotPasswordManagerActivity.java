package com.example.trojancheckincheckout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
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
import java.util.Random;

import androidx.core.content.res.ResourcesCompat;
import android.graphics.Typeface;
import android.widget.TextView;

public class ForgotPasswordManagerActivity extends AppCompatActivity {

    Database db;
    String emailStr;
    String passStr;
    String passConfirmStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_manager);
        EditText email = findViewById(R.id.email);
        EditText newpass = findViewById(R.id.newpassword);
        EditText confirm = findViewById(R.id.confirm);

        Typeface typeface = ResourcesCompat.getFont(
                this,
                R.font.poppins);
        email.setTypeface(typeface);
        newpass.setTypeface(typeface);
        confirm.setTypeface(typeface);

        db = new Database();

        Button changePass = (Button) findViewById(R.id.changepassbutton);

        if (changePass != null) {
            changePass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    emailStr = email.getText().toString();
                    passStr = newpass.getText().toString();
                    passConfirmStr = confirm.getText().toString();
                    if(passStr.compareTo(passConfirmStr)!=0) {
                        alert("Please confirm your new password.");
                        return;
                    }
                    db.checkEmail(emailStr, new checkEmailInterface() {
                        @Override
                        public void onCheckEmail(boolean exists) {
                            if (exists) {
                                db.checkAccountStatus(emailStr, new checkAccountStatusInterface(){
                                    @Override
                                    public void checkAccountStatus(boolean deleted) {
                                        if (deleted) {
                                            Log.d("DB", "cant sign into a deleted acct");
                                            alert("Account under the email does not exist.");
                                        }
                                        else{
                                            db.updatePassword(emailStr, passStr, new updatePasswordInterface(){
                                                @Override
                                                public void updatePasswordStatus(boolean updated){
                                                    if(updated){
                                                        alert("Password updated.");
                                                        updatePressed();
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                            else{
                                alert("User does not exist.");
                                return;
                            }
                        }
                    });
                }
            });
        }
    }

    public void goBack(View view){
        Intent i = new Intent(this, ForgotPasswordAccountActivity.class);
        startActivity(i);
    }

    public static String getRandomNumberString() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }

    public void updatePressed(){
        Intent i = new Intent(this, MainActivity.class);
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


}