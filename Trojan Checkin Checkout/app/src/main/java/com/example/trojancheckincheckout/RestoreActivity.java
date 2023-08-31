package com.example.trojancheckincheckout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class RestoreActivity extends AppCompatActivity {

    Database db;
    EditText email;
    EditText password;
    Button restoreButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restore_account);

        TextView textView = findViewById(R.id.restoreText);

        Typeface typeface = ResourcesCompat.getFont(
                this,
                R.font.poppins);
        textView.setTypeface(typeface);

        db = new Database();
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        restoreButton = (Button) findViewById(R.id.restorebutton);

    }

    public void onClick (View v) {
        String emailStr = email.getText().toString();
        String passStr = password.getText().toString();

        if (emailStr.isEmpty() || passStr.isEmpty()) {
            alert("Please fill out all the fields.");
            return;
        }
        else if(!emailStr.substring(emailStr.length()-8).equals("@usc.edu")) {
            alert("You must restore with your USC email.");
            return;
        }
        else if(passStr.length() < 4){
            alert("Password too short. Your password must be at least 4 characters");
            return;
        }

        System.out.println("Checking if email is valid in restore activity");

        db.checkEmail(emailStr, new checkEmailInterface() {
            @Override
            public void onCheckEmail(boolean exists) {
                if (exists) {
                    db. checkAccountStatus(emailStr, new checkAccountStatusInterface() {
                        @Override
                        public void checkAccountStatus(boolean deleted) {
                            if (!deleted) {
                                alert("Account with the given email was not deleted. Please login.");
                                return;
                            } else {
                                db.verifyPassword(emailStr, passStr, new verifyPasswordInterface() {
                                    @Override
                                    public void verifyPassword(boolean status) {
                                        if (!status) {
                                            alert("Account restore failed. Incorrect password.");
                                            return;
                                        } else {
                                            db.reactivateUser(emailStr, new reactivateUserInterface() {
                                                @Override
                                                public void reactivateUser(boolean status) {
                                                    if (status) {
                                                        db.getUserRole(emailStr, new userRoleInterface() {
                                                            @Override
                                                            public void getUserRole(String role) {
                                                                System.out.println("role=" + role);
                                                                if (role.equals("manager")) {
                                                                    goToManagerProfile(emailStr);
                                                                    return;
                                                                } else {
                                                                    goToStudentProfile(emailStr);
                                                                    return;
                                                                }
                                                            }
                                                        });
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        }
                    });
                } else {
                    alert("Account restore failed. Email unknown.");
                    return;
                }
            }
        });
    }

    public void onBack(View view){
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
    }

    public void goToManagerProfile(String email) {
        Intent i = new Intent(this, ManagerProfileActivity.class);
        i.putExtra("user", email);
        startActivity(i);
    }

    public void goToStudentProfile(String email) {
        Intent i = new Intent(this, StudentProfileActivity.class);
        i.putExtra("user", email);
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
