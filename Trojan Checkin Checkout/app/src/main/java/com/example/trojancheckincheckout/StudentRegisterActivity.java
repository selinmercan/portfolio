package com.example.trojancheckincheckout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class StudentRegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    Database db;
    Integer spinnerIdx;
    EditText first;
    EditText last;
    EditText email;
    EditText id;
    Spinner major;
    EditText password;
    Button registerButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_register);

        TextView textView = findViewById(R.id.studentreg);

        Typeface typeface = ResourcesCompat.getFont(
                this,
                R.font.poppins);
        textView.setTypeface(typeface);

        db = new Database();
        spinnerIdx = 0;
        first = (EditText)findViewById(R.id.fname);
        last = (EditText)findViewById(R.id.lname);
        email = (EditText)findViewById(R.id.email);
        id = (EditText)findViewById(R.id.id);
        major = (Spinner)findViewById(R.id.major);
        password = (EditText)findViewById(R.id.password);
        registerButton = (Button) findViewById(R.id.registerbutton);

        major.setOnItemSelectedListener(this);

        String[] items = new String[]{"Anthropology", "Business Administration", "Chemistry", "Computer Science", "Economics", "History"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        major.setAdapter(adapter);
    }

    public void onClick(View v) {
        String firstStr = first.getText().toString();
        String lastStr = last.getText().toString();
        String emailStr = email.getText().toString();
        String idNew = id.getText().toString();

        String passStr = password.getText().toString();

        if(firstStr.isEmpty() || lastStr.isEmpty() || emailStr.isEmpty()
                || idNew.isEmpty() || passStr.isEmpty()) {
            alert("Please fill out all the fields");
            return;
        }
        else if(!emailStr.substring(emailStr.length()-8).equals("@usc.edu")) {
            alert("You must register with your USC email");
            return;
        }
        else if (idNew.length() != 10) {
            alert("Student ID must be 10 digits.");
            return;
        }
        else if(passStr.length() < 4){
            alert("Password too short. Your password must be at least 4 characters");
            return;
        }

        // checks that password contains at least one digit
        Boolean containsDigit = false;
        for (char c : passStr.toCharArray()) {
            if (containsDigit = Character.isDigit(c)) {
                break;
            }
        }
        if (!containsDigit) {
            alert("Password must contain at least 1 digit");
            return;
        }

        // checks if user id input only includes digit before parseInt
        // else line int idStr = Integer.parseInt(idNew);  would need a try catch exception
        Boolean allNumbers = false;
        for (char c : idNew.toCharArray()) {
            allNumbers = Character.isDigit(c);
            if (!allNumbers) {
                break;
            }
        }
        if (!allNumbers) {
            alert("Student ID has invalid characters. Student ID must be 10 digits.");
            return;
        }
        System.out.println(idNew.toCharArray()[0]);
//        int idStr = Integer.parseInt(String.valueOf(idNew.toCharArray()));

        String majorStr = (String) major.getItemAtPosition(spinnerIdx);

        System.out.println("checking if email is valid");
        db.checkEmail(emailStr, new checkEmailInterface() {
            public void onCheckEmail(boolean exists) {
                if(exists) {

                    db.checkAccountStatus(emailStr, new checkAccountStatusInterface() {

                        @Override
                        public void checkAccountStatus(boolean deleted) {
                            if (deleted) {
                                alert("Account with the given email was deleted. Please try to restore account instead.");
                                return;
                            } else {
                                alert("Account with the given email already exists.");
                                return;
                            }
                        }
                    });
                } else {
                    db.checkID(idNew, new checkIdInterface() {
                        public void onCheckId(boolean exists) {
                            if (exists) {
                                alert("Account with the given ID already exists.");
                                return;
                            } else {
                                long idStr = Long.parseLong(idNew);
                                db.addUser(emailStr, firstStr, lastStr, passStr, "student", majorStr, idStr, new addUserInterface() {
                                    @Override
                                    public void addUser(boolean status) {
                                        if (status) {
                                            db.getStudent(emailStr, new getUserInterface() {
                                                @Override
                                                public void getUser(User user) {
                                                    System.out.println("should be going to profile now");
                                                    goToProfile(emailStr);
                                                }
                                            });
                                        } else {
                                            alert("An error has occurred. Please try again.");
                                        }
                                    }
                                });
                            }
                        }
                    });

                }
            }
        });

        //TODO: Send email to confirm usc student

    }

//    public void restorePress(View v) {
//        String emailStr = email.getText().toString();
//        String passStr = password.getText().toString();
//
//        if(!emailStr.substring(emailStr.length()-8).equals("@usc.edu")) {
//            alert("You must register with your USC email");
//            return;
//        }
//
//        System.out.println("checking if email is valid in restore");
//        db.checkEmail(emailStr, new checkEmailInterface() {
//            public void onCheckEmail(boolean exists) {
//                if(exists) {
//
//                    db.checkAccountStatus(emailStr, new checkAccountStatusInterface() {
//
//                        @Override
//                        public void checkAccountStatus(boolean deleted) {
//                            if (!deleted) {
//                                alert("Account with the given email was not deleted. Please login.");
//                                return;
//                            } else {
//
//                                db.verifyPassword(emailStr, passStr, new verifyPasswordInterface() {
//                                    @Override
//                                    public void verifyPassword(boolean status) {
//                                        if (!status) {
//                                            alert("Account restore failed. Incorrect password.");
//                                            return;
//                                        } else {
//                                            db.reactivateUser(emailStr, new reactivateUserInterface() {
//                                                @Override
//                                                public void reactivateUser(boolean status) {
//                                                    if (status) {
//                                                        goToProfile(emailStr);
//                                                    }
//                                                }
//                                            });
//                                        }
//                                    }
//                                });
//                            }
//                        }
//                    });
//
//                }
//                else {
//                    alert("Account restore failed. Account with the given email does not exist.");
//                    return;
//                }
//            }
//        });
//
//    }



    public void goToProfile(String email){
        Intent i = new Intent(this, StudentProfileActivity.class);
        i.putExtra("user", email);
        startActivity(i);

    }

    public void onBack(View view){
        Intent i = new Intent(this, RegisterActivity.class);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinnerIdx = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}