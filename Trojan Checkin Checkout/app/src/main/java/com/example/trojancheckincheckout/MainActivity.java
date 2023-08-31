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

public class MainActivity extends AppCompatActivity {

    Database db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = findViewById(R.id.welcome);
        TextView textView2 = findViewById(R.id.trojancheck);
        TextView textView3 = findViewById(R.id.contact);
        TextView textView4 = findViewById(R.id.contactactual);

        Typeface typeface = ResourcesCompat.getFont(
                this,
                R.font.poppins);
        textView.setTypeface(typeface);
        textView2.setTypeface(typeface);
        textView3.setTypeface(typeface);
        textView4.setTypeface(typeface);

        db = new Database();

        EditText email = (EditText) findViewById(R.id.email);
        EditText password = (EditText) findViewById(R.id.password);
        Button login = (Button) findViewById(R.id.loginbutton);

        if (login != null) {
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String emailStr = email.getText().toString();
                    String passStr = password.getText().toString();

                    if(emailStr.isEmpty() || passStr.isEmpty()) {
                        alert("Please fill out all the fields.");
                        return;
                    }

                    db.checkEmail(emailStr, new checkEmailInterface() {
                        @Override
                        public void onCheckEmail(boolean exists) {
                            if (exists) {
                                db.checkAccountStatus(emailStr, new checkAccountStatusInterface() {
                                    @Override
                                    public void checkAccountStatus(boolean deleted) {
                                        if (deleted) {
                                            Log.d("DB", "cant sign into a deleted acct");
                                            alert("Cannot sign into a deleted account");
                                        } else {
                                            db.verifyPassword(emailStr, passStr, new verifyPasswordInterface() {
                                                @Override
                                                public void verifyPassword(boolean status) {
                                                    if (status) {
                                                        Log.d("DB", "password is a match - valid login");
                                                        db.getUserRole(emailStr, new userRoleInterface() {
                                                            @Override
                                                            public void getUserRole(String role) {
                                                                Log.d("Testing", role);
                                                                if (role.equals("student")) {
                                                                    goToProfile(emailStr, true);
                                                                } else {
                                                                    goToProfile(emailStr, false);
                                                                }
                                                            }
                                                        });
                                                    } else {
                                                        Log.d("DB", "password does not match - invalid login");
                                                        alert("Invalid password");
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                            } else {
                                Log.d("DB", "no match found for given email");
                                alert("Invalid email");
                            }
                        }
                    });
                }
            });
        }
    }

    public void createAccPress(View view){
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
    }

    public void forgotPasswordPressed(View view){
        Intent i = new Intent(this, ForgotPasswordAccountActivity.class);
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

    public void goToProfile(String email, Boolean student){

        if(student) {
            Log.d("Testing", "sending to student profile");
            Intent i = new Intent(this, StudentProfileActivity.class);
            i.putExtra("user", email);
            startActivity(i);
        }
        else {
            Log.d("Testing", "sending to manager profile");
            Intent i = new Intent(this, ManagerProfileActivity.class);
            i.putExtra("user", email);
            startActivity(i);
        }

    }

}