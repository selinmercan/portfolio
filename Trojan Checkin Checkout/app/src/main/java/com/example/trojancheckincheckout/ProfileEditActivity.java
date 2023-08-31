package com.example.trojancheckincheckout;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import androidx.core.content.res.ResourcesCompat;
import android.graphics.Typeface;

import java.util.ArrayList;

public class ProfileEditActivity extends AppCompatActivity {

    String email = "";
    String userRole = "";
    Database db;
    int SELECT_PICTURE = 200;
    ImageView profilePic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        TextView textView = findViewById(R.id.edit);

        Typeface typeface = ResourcesCompat.getFont(
                this,
                R.font.poppins);
        textView.setTypeface(typeface);

        Intent intent = getIntent();
        email = intent.getStringExtra("user");
        profilePic = new ImageView(this);


        db = new Database();

        db.getUserRole(email, new userRoleInterface() {
            @Override
            public void getUserRole(String role) {
                userRole = role;
            }
        });
    }

    private final Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            profilePic.setImageBitmap(bitmap);
            updatePic();
        }

        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
            e.printStackTrace();
        }
        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
        }
    };


    public void getPhotofromLink(){
        final EditText imageLink = new EditText(ProfileEditActivity.this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(imageLink);
        imageLink.setHint("Paste the link to the image here");
        builder.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO: handle image link
                String link = imageLink.getText().toString();
                Picasso.get().load(link).resize(1280, 720).centerCrop()
                        .into(target);
            }
        });
        builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();

    }

    public void updatePic() {
        Database db = new Database();
        db.updateStudentPicture(email, profilePic, new updatePictureInterface() {
            public void updatePicture(boolean status) {
                if(status){
                    alert("Profile Picture Updated");
                }
                else {
                    alert("There was an error updating your profile picture");
                }
            }
        });

    }


    public void getPhoto(View view) {

        // create an instance of the
        // intent of the type image
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("How would you like to upload your image?");
        builder.setPositiveButton("Upload From Gallery", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);

                // pass the constant to compare it
                // with the returned requestCode
                startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);

            }
        });
        builder.setNeutralButton("Upload by Link", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                getPhotofromLink();
            }
        });
        builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    // this function is triggered when user
    // selects the image from the imageChooser
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant

            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    profilePic
                            .setImageURI(selectedImageUri);
                    Database db = new Database();
                    db.updateStudentPicture(email, profilePic, new updatePictureInterface() {
                        public void updatePicture(boolean status) {
                            if(status){
                                alert("Profile Picture Updated");
                            }
                            else {
                                alert("There was an error updating your profile picture");
                            }
                        }
                    });
                }
            }

        }

    }

    public void alert(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }


    public void save(View view) {
        TextView pass = (TextView) findViewById(R.id.password);
        String password = pass.getText().toString();
        Log.d("Testing", password);

        if (!password.equals("")) {

            if (password.length() < 4) {
                alert("New password must be at least 4 characters");
                return;
            }

            db.updatePassword(email, password, new updatePasswordInterface() {
                @Override
                public void updatePasswordStatus(boolean status) {
                    if (status) {
                        Log.d("Testing", "successful pw update");

                        db.getUserRole(email, new userRoleInterface() {
                            @Override
                            public void getUserRole(String role) {
                                alert("Password saved successfully");
                                if (role.equals("student")) {
                                    Intent intent = new Intent(getApplicationContext(), StudentProfileActivity.class);
                                    intent.putExtra("user", email);
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(getApplicationContext(), ManagerProfileActivity.class);
                                    intent.putExtra("user", email);
                                    startActivity(intent);
                                }
                            }
                        });
                    } else {
                        AlertDialog.Builder alert = new AlertDialog.Builder(getApplicationContext());
                        alert.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        alert.setMessage("Failed to update password.");

                        AlertDialog alertDialog = alert.create();
                        alertDialog.show();
                    }
                }
            });
        }
        else {
            db.getUserRole(email, new userRoleInterface() {
                @Override
                public void getUserRole(String role) {
                    alert("No password entered, returning to profile page");
                    if (role.equals("student")) {
                        Intent intent = new Intent(getApplicationContext(), StudentProfileActivity.class);
                        intent.putExtra("user", email);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getApplicationContext(), ManagerProfileActivity.class);
                        intent.putExtra("user", email);
                        startActivity(intent);
                    }
                }
            });
        }
    }

    public void goBack(View view){
        if (userRole.equals("student")) {
            Intent i = new Intent(this, StudentProfileActivity.class);
            i.putExtra("user", email);
            startActivity(i);
        }
        else {
            Intent i = new Intent(this, ManagerProfileActivity.class);
            i.putExtra("user", email);
            startActivity(i);
        }

    }

}