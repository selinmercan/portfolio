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

import androidx.core.content.res.ResourcesCompat;
import android.graphics.Typeface;
import android.widget.TextView;

public class ManagerRegisterActivity extends AppCompatActivity {

    Database db;
    EditText first;
    EditText last;
    EditText email;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_register);

        TextView textView = findViewById(R.id.managerreg);

        Typeface typeface = ResourcesCompat.getFont(
                this,
                R.font.poppins);
        textView.setTypeface(typeface);

        db = new Database();
        first = (EditText)findViewById(R.id.fname);
        last = (EditText)findViewById(R.id.lname);
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        Button registerButton = (Button) findViewById(R.id.registerbutton);
//        registerButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String firstStr = first.getText().toString();
//                String lastStr = last.getText().toString();
//                String emailStr = email.getText().toString();
//                String passStr = password.getText().toString();
//
//                if(firstStr.isEmpty() || lastStr.isEmpty() || emailStr.isEmpty() || passStr.isEmpty()) {
//                    alert("Please fill out all the fields");
//                }
//                else if(!emailStr.substring(emailStr.length()-8).equals("@usc.edu")) {
//                    alert("You must register with your USC email");
//                }
//                else if(passStr.length() < 9){  //TODO: add extra password requirements
//                    alert("Password too short. Your password must be at least 9 characters");
//                }
//
//                db.checkEmail(emailStr, new FirebaseListener() {
//                    public void verifyPassword(boolean passwordStatus) { }
//
//                    @Override
//                    public void updatePasswordStatus(boolean status) {
//
//                    }
//
//                    public void deleteUser(boolean deleteStatus) {  }
//                    public void getUserRole(String role) {}
//                    public void addUser(boolean addStatus) { }
//                    public void getStudent(User student) { }
//                    public void addBuilding(boolean addStatus) { }
//                    public void removeBuilding(boolean removeStatus) { }
//                    public void updateBuildingList(boolean buildingStatus) { }
//                    public void getCurrCapacity(int capacity) { }
//                    public void getMaxCapacity(int capacity) { }
//                    public void onCheckEmail(boolean exists) {
//                        if(exists) {
//                            alert("Account with the given email already exists.");
//                        }
//                    }
//
//                    @Override
//                    public void updateBuildingQRCode(boolean status) {
//
//                    }
//
//                    @Override
//                    public void getBuildingQRCode(Bitmap bitmap) {
//
//                    }
//
//                    public void updateCurrCapacity(boolean updateStatus) { }
//                    public void onGetBuildings(ArrayList<Building> buildings) { }
//                    public void onGetRecordsByBuilding(ArrayList<Record> records) { }
//                    public void onGetRecordsByStudent(ArrayList<Record> records) { }
//                    public void updatePicture(boolean status) { }
//                    public void getPicture(Bitmap encoded_image) { }
//                    public void getStudentStatus(boolean status) {  }
//                    public void onCheckIn(Boolean success) { }
//                    public void onCheckOut(Boolean success) { }
//                });
//                //TODO: Send email to confirm usc student
//                db.addUser(emailStr, firstStr, lastStr, passStr, "manager", null, 0, new FirebaseListener() {
//                    public void verifyPassword(boolean passwordStatus) { }
//
//                    @Override
//                    public void updatePasswordStatus(boolean status) {
//
//                    }
//
//                    public void deleteUser(boolean deleteStatus) {  }
//                    public void getUserRole(String role) {}
//                    public void addUser(boolean addStatus) {
//                        if(addStatus){
//                            goToProfile(emailStr);
//                        }
//                        else {
//                            alert("An error has occurred. Please try again.");
//                        }
//                    }
//                    public void getStudent(User student) { }
//                    public void addBuilding(boolean addStatus) { }
//                    public void removeBuilding(boolean removeStatus) { }
//                    public void updateBuildingList(boolean buildingStatus) { }
//                    public void getCurrCapacity(int capacity) { }
//                    public void getMaxCapacity(int capacity) { }
//                    public void onCheckEmail(boolean exists) {}
//
//                    @Override
//                    public void updateBuildingQRCode(boolean status) {
//
//                    }
//
//                    @Override
//                    public void getBuildingQRCode(Bitmap bitmap) {
//
//                    }
//
//                    public void updateCurrCapacity(boolean updateStatus) { }
//                    public void onGetBuildings(ArrayList<Building> buildings) { }
//                    public void onGetRecordsByBuilding(ArrayList<Record> records) { }
//                    public void onGetRecordsByStudent(ArrayList<Record> records) { }
//                    public void updatePicture(boolean status) { }
//                    public void getPicture(Bitmap encoded_image) { }
//                    public void getStudentStatus(boolean status) {  }
//                    public void onCheckIn(Boolean success) { }
//                    public void onCheckOut(Boolean success) { }
//                });
//
//            }
//        });
    }

    public void createAcc(View view) {
        String firstStr = first.getText().toString();
        String lastStr = last.getText().toString();
        String emailStr = email.getText().toString();
        String passStr = password.getText().toString();

        if (firstStr.isEmpty() || lastStr.isEmpty() || emailStr.isEmpty() || passStr.isEmpty()) {
            alert("Please fill out all the fields");
        } else if (!emailStr.substring(emailStr.length() - 8).equals("@usc.edu")) {
            alert("You must register with your USC email");
        } else if (passStr.length() < 4) {
            alert("Password too short. Your password must be at least 4 characters");
        } else {

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

            // if we get here, we have valid inputs - try to make the account
            db.checkEmail(emailStr, new checkEmailInterface() {
                public void onCheckEmail(boolean exists) {
                    if (exists) {
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
                        // new email - make the account
                        db.addUser(emailStr, firstStr, lastStr, passStr, "manager", "none", 0, new addUserInterface() {
                            @Override
                            public void addUser(boolean status) {
                                if (status) {
                                    Log.d("Testing", "valid acc details, creating acc");
                                    Intent intent = new Intent(getApplicationContext(), ManagerProfileActivity.class);
                                    intent.putExtra("user", emailStr);
                                    startActivity(intent);
                                } else {
                                    alert("Failed to add user. Try again.");
                                }
                            }
                        });
                    }
                }
            });
        }


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
        Intent i = new Intent(this, ManagerProfileActivity.class);
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
}