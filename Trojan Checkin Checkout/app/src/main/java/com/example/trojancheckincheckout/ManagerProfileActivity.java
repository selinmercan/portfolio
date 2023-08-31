package com.example.trojancheckincheckout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import androidx.core.content.res.ResourcesCompat;
import android.graphics.Typeface;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class ManagerProfileActivity extends AppCompatActivity {
    String email = "";
    Database db;

    int SELECT_PICTURE = 200;
    ImageView profilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_profile); // this line is causing errors

        db = new Database();
        profilePic = new ImageView(this);

        TextView textView = findViewById(R.id.managerprofile);
        TextView textView2 = findViewById(R.id.welcomeback);
        TextView textView3 = findViewById(R.id.info);
        TextView textView4 = findViewById(R.id.name);
        TextView textView5 = findViewById(R.id.building_label);
        TextView textView6 = findViewById(R.id.searchtext);

        Typeface typeface = ResourcesCompat.getFont(
                this,
                R.font.poppins);
        textView.setTypeface(typeface);
        textView2.setTypeface(typeface);
        textView3.setTypeface(typeface);
        textView4.setTypeface(typeface);
        textView5.setTypeface(typeface);
        textView6.setTypeface(typeface);

        Intent intent = getIntent();
        email = intent.getStringExtra("user");
        Database db = new Database();

        db.getStudent(email, new getUserInterface() {
            @Override
            public void getUser(User user) {
                if (user == null) {
                    Log.d("DB", "there was an error getting the student object");
                    // note: this will never occur if the student does exist in the db
                } else {

                    //TextView major = (TextView) findViewById(R.id.label_for_name);
                    TextView name = (TextView) findViewById(R.id.name);

                    String concat = user.first_name + " " + user.last_name;

                    name.setText(concat);

                    ImageView pic = (ImageView) findViewById(R.id.tommy);

                    db.getStudentPicture(email, new getPictureInterface(){
                        @Override
                        public void getPicture(Bitmap encoded_image) {
                            if (encoded_image != null) {
                                pic.setImageBitmap(encoded_image);
                            }
                            else {
                                if (pic == null) {
                                    Log.d("err", "manager page is broken rn");
                                }
                                pic.setImageDrawable(getResources().getDrawable(R.drawable.tommytrojan));
                            }
                        }
                    });


                }
            }
        });
    }

    public void signOut(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void editProfile(View view){
        Log.d("Testing", "clicked edit profile from manager");
        Intent intent = new Intent(this, ProfileEditActivity.class);
        intent.putExtra("user", email);
        intent.putExtra("role", "manager");
        startActivity(intent);
    }

    public void deleteAlert(View view) {

        AlertDialog.Builder alert = new AlertDialog.Builder(ManagerProfileActivity.this);
        alert.setMessage("Are you sure you want to delete your account?");

        alert.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAccount(view);
                    }
                });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO: Insert Check Out without Scanning

                dialog.dismiss();
            }
        });

        AlertDialog popUp = alert.create();
        popUp.show();

    }

    public void deleteAccount(View view) {
       // Database db = new Database();

        db.deleteUser(email, new deleteUserInterface() {
            @Override
            public void deleteUser(boolean status) {
                if (status) {
                    Log.d("DB", "user delete succeeded");
                    signOut(view);
                } else {
                    Log.d("DB", "user deleted failed");
                }
            }
        });


    }

    public void loadBuildingList(View view) {
        // moves you to the manager buildings page to list all of the buildings the manager can see
        Intent intent = new Intent(getApplicationContext(), ManagerBuildingsActivity.class);
        intent.putExtra("user", email);
        intent.putExtra("role", "manager");
        startActivity(intent);
    }

    public void loadSearch(View view) {
        Intent intent = new Intent(getApplicationContext(), ManagerSearchActivity.class);
        intent.putExtra("user", email);
        startActivity(intent);
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

    public void editPhoto(View view){
        getPhoto();
//        Intent i = new Intent(this, ProfileEditActivity.class);
//        i.putExtra("user", email);
//        i.putExtra("role", "student");
//        startActivity(i);
    }

    public void editPassword(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit password");

        final EditText pass = new EditText(this);

        pass.setInputType(InputType.TYPE_CLASS_TEXT);
        pass.setHint("New Password");

        builder.setView(pass);

        builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                validate(pass.getText().toString());
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    public void validate(String password){
        if (!password.trim().equals("")) {
            Log.d("new pass", "the password isnt empty");
            if (password.length() < 4) {
                AlertDialog.Builder alert = new AlertDialog.Builder(ManagerProfileActivity.this);
                alert.setMessage("Password length must be >= 4");
                AlertDialog popUp = alert.create();
                popUp.show();
                return;
            }
            Log.d("new pass", "password >= 4");

            db.updatePassword(email, password, new updatePasswordInterface() {
                @Override
                public void updatePasswordStatus(boolean status) {
                    if (status) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(ManagerProfileActivity.this);
                        alert.setMessage("Password updated!");
                        AlertDialog popUp = alert.create();
                        popUp.show();
                    } else {
                        AlertDialog.Builder alert = new AlertDialog.Builder(ManagerProfileActivity.this);
                        alert.setMessage("Database error, failed to update");
                        AlertDialog popUp = alert.create();
                        popUp.show();
                    }
                }
            });
        }
        else {
            AlertDialog.Builder alert = new AlertDialog.Builder(ManagerProfileActivity.this);
            alert.setMessage("Password cannot be whitespace");
            AlertDialog popUp = alert.create();
            popUp.show();
        }
    }

    //    Pasted in Sam's photo stuff down here to move it to this page

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
        final EditText imageLink = new EditText(ManagerProfileActivity.this);
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


    public void getPhoto() {

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
                    profilePic.setImageURI(selectedImageUri);

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

}
