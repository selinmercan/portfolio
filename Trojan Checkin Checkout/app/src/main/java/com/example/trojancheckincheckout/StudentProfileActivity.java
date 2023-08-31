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
import android.widget.PopupMenu;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import androidx.core.content.res.ResourcesCompat;
import android.graphics.Typeface;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class StudentProfileActivity extends AppCompatActivity {
    String email = "";
    String role = "student";
    String building = "";
    Database db;
    int SELECT_PICTURE = 200;
    ImageView profilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        profilePic = new ImageView(this);

        TextView textView = findViewById(R.id.studentprofile);
        TextView textView2 = findViewById(R.id.welcomeback);
        TextView textView3 = findViewById(R.id.info);
        TextView textView4 = findViewById(R.id.label_for_name);
        TextView textView5 = findViewById(R.id.info1);
        TextView textView6 = findViewById(R.id.label_for_usc_id);
        TextView textView7 = findViewById(R.id.info2);
        TextView textView8 = findViewById(R.id.label_for_major);
        TextView textView9 = findViewById(R.id.curr_loc);
        TextView textView10 = findViewById(R.id.curr_loc_text_view);
        TextView textView11 = findViewById(R.id.checkintext);
        TextView textView12 = findViewById(R.id.checkouttext);
        TextView textView13 = findViewById(R.id.history_);

        Typeface typeface = ResourcesCompat.getFont(
                this,
                R.font.poppins);
        textView.setTypeface(typeface);
        textView2.setTypeface(typeface);
        textView3.setTypeface(typeface);
        textView4.setTypeface(typeface);
        textView5.setTypeface(typeface);
        textView6.setTypeface(typeface);
        textView7.setTypeface(typeface);
        textView8.setTypeface(typeface);
        textView9.setTypeface(typeface);
        textView10.setTypeface(typeface);
        textView11.setTypeface(typeface);
        textView12.setTypeface(typeface);
        textView13.setTypeface(typeface);

        db = new Database();
        Intent intent = getIntent();
        email = intent.getStringExtra("user");
        String error = intent.getStringExtra("alert");

        if (error != null && error.equals("checkout-scan")) {
            String buildingName = intent.getStringExtra("building");
            AlertDialog.Builder alert = new AlertDialog.Builder(StudentProfileActivity.this);
            alert.setMessage("You must scan the qr code for " + buildingName + " to check out." +
                    "\nOr check out without scanning");

            AlertDialog popUp = alert.create();
            popUp.show();
        }
        else if (error != null && error.equals("buildingFull")) {
            String buildingName = intent.getStringExtra("building");
            AlertDialog.Builder alert = new AlertDialog.Builder(StudentProfileActivity.this);
            alert.setMessage("The building " + buildingName + " is at capacity");

            AlertDialog popUp = alert.create();
            popUp.show();
        }
        else if (error != null && error.equals("badQR")) {

            AlertDialog.Builder alert = new AlertDialog.Builder(StudentProfileActivity.this);
            alert.setMessage("Please scan a valid qr code");

            AlertDialog popUp = alert.create();
            popUp.show();
        }

        db.getStudent(email, new getUserInterface() {
            @Override
            public void getUser(User user) {
                if (user == null) {
                    Log.d("DB", "there was an error getting the student object");
                    // note: this will never occur if the student does exist in the db
                } else {

                    TextView major = (TextView) findViewById(R.id.label_for_major);
                    TextView name = (TextView) findViewById(R.id.label_for_name);
                    TextView uscID = (TextView) findViewById(R.id.label_for_usc_id);

                    TextView location = (TextView) findViewById(R.id.curr_loc_text_view);

                    major.setText(user.major);
                    String full_name = user.first_name + " " + user.last_name;
                    name.setText(full_name);

                    uscID.setText(String.valueOf(user.usc_ID));

                    building = user.getCurr_loc();

                    Log.d("database", "In Building: " + building);

                    if (building.equals("none")) {
                        location.setText("You are not currently in a building");
                    }
                    else if(building.equals("kickout")){
                        location.setText("You are not currently in a building");
                        alertNice("A manager has kicked you out of the building.", true);
                        building = "none";
                    }
                    else {
                        location.setText(building);
                    }

                    ImageView pic = (ImageView) findViewById(R.id.tommy);

                    db.getStudentPicture(email, new getPictureInterface(){
                        @Override
                        public void getPicture(Bitmap encoded_image) {
                            if (encoded_image != null) {
                                pic.setImageBitmap(encoded_image);
                            }
                            else {
                                pic.setImageDrawable(getResources().getDrawable(R.drawable.tommytrojan));
                            }
                        }
                    });
                }
            }
        });
    }

    public void signOut(View view){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
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
                AlertDialog.Builder alert = new AlertDialog.Builder(StudentProfileActivity.this);
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
                        AlertDialog.Builder alert = new AlertDialog.Builder(StudentProfileActivity.this);
                        alert.setMessage("Password updated!");
                        AlertDialog popUp = alert.create();
                        popUp.show();
                    } else {
                        AlertDialog.Builder alert = new AlertDialog.Builder(StudentProfileActivity.this);
                        alert.setMessage("Database error, failed to update");
                        AlertDialog popUp = alert.create();
                        popUp.show();
                    }
                }
            });
        }
        else {
            AlertDialog.Builder alert = new AlertDialog.Builder(StudentProfileActivity.this);
            alert.setMessage("Password cannot be whitespace");
            AlertDialog popUp = alert.create();
            popUp.show();
        }
    }

    public void deleteAlert(View view) {
        AlertDialog.Builder alert = new AlertDialog.Builder(StudentProfileActivity.this);
        alert.setMessage("Are you sure you want to delete your account?");

        alert.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAccount();
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


    public void deleteAccount() {
        // Database db = new Database();
        // TODO - test this


        Log.d("lucas", "building name is: " + building);

        if (!building.equals("none")) {
            Log.d("lucas", "called name != none function");
            db.checkOut(email, building, new checkOutInterface() {
                @Override
                public void onCheckOut(boolean success) {
                    if (success) {
                        db.getBuildingCurrentCapacity(building, new getCurrentCapacityInterface() {
                            @Override
                            public void getCurrCapacity(int capacity) {
                                int new_capacity = capacity - 1;
                                db.updateCurrentCapacity(building, new_capacity, new updateCurrentCapacityInterface() {
                                    @Override
                                    public void updateCurrCapacity(boolean status) {
                                        if (status) {
                                            db.updateLocation(email, "none", new updateLocationInterface() {
                                                @Override
                                                public void onUpdateLocation(boolean status) {
                                                    if (status) {
                                                        building = "none";
                                                        deleteAccount();
                                                    } else {
                                                    }
                                                }
                                            });
                                        } else {
                                        }
                                    }
                                });
                            }
                        });
                    } else {
                    }
                }
            });
        }
        else {
            Log.d("lucas", "called checked out function");
            db.deleteUser(email, new deleteUserInterface() {
                @Override
                public void deleteUser(boolean status) {
                    if (status) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    } else {
                        AlertDialog.Builder alert = new AlertDialog.Builder(getApplicationContext());
                        alert.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        alert.setMessage("Failed to delete account.");

                        AlertDialog alertDialog = alert.create();
                        alertDialog.show();
                    }
                }
            });
        }
    }

    public void loadStudentHistory(View view) {
        Intent intent = new Intent(getApplicationContext(), UserHistoryActivity.class);
        intent.putExtra("user", email);
        intent.putExtra("role", "student");
        startActivity(intent);
    }

    public void checkOutPressed(View view){

        AlertDialog.Builder alert = new AlertDialog.Builder(StudentProfileActivity.this);

        if (this.building.equals("none") || this.building.equals("")) {
            alert.setMessage("You are not currently in a building");
        }
        else {
            alert.setPositiveButton("Scan Building Code",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            checkOut();
                        }
                    });
            alert.setNegativeButton("Check Out Now", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //TODO: Insert Check Out without Scanning

                    AlertDialog.Builder inner = new AlertDialog.Builder(StudentProfileActivity.this);
                    inner.setMessage("Are you sure you want to check out?");
                    inner.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dbCheckout();
                                }
                            });
                    inner.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //TODO: Insert Check Out without Scanning
                            dialog.dismiss();
                        }
                    });

                    AlertDialog popUpInner = inner.create();
                    popUpInner.show();
                }
            });
            alert.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alert.setMessage("How would you like to check out?");
        }


        AlertDialog popUp = alert.create();
        popUp.show();
    }

    public void checkOut() {
        Intent intent = new Intent(getApplicationContext(), Scanner.class);
        intent.putExtra("user", email);
        intent.putExtra("type", "check-out"); //needed to tell the scanner why it's scanning
        intent.putExtra("building", building);
        Log.d("Testing", "checking out from stud prifle page");
        startActivity(intent);
    }

    public void checkIn(View view) {

        AlertDialog.Builder alert = new AlertDialog.Builder(StudentProfileActivity.this);

        if (!this.building.equals("none") && !this.building.equals("")) {
            alert.setMessage("You must checkout before you can check in to a new building");

            alert.setPositiveButton("Check Out",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dbCheckout();
                        }
                    });
            alert.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //TODO: Insert Check Out without Scanning

                    dialog.dismiss();
                }
            });

            AlertDialog popUp = alert.create();
            popUp.show();
        }
        else {

            //TODO: check if building capacity is full

            Log.d("Testing", "checking in from student page");
            Log.d("aaa", "check in button pressed");
            Intent intent = new Intent(getApplicationContext(), Scanner.class);
            intent.putExtra("user", email);
            intent.putExtra("type", "check-in"); //needed to tell the scanner why it's scanning

            startActivity(intent);
        }
    }

    public void dbCheckout() {
        db.checkOut(email, building, new checkOutInterface() {
            @Override
            public void onCheckOut(boolean success) {
                if (success) {
                    db.getBuildingCurrentCapacity(building, new getCurrentCapacityInterface() {
                        @Override
                        public void getCurrCapacity(int capacity) {
                            int new_capacity = capacity - 1;
                            db.updateCurrentCapacity(building, new_capacity, new updateCurrentCapacityInterface() {
                                @Override
                                public void updateCurrCapacity(boolean status) {
                                    if (status) {
                                        Log.d("Testing", "updating current capacity");
                                        db.updateLocation(email, "none", new updateLocationInterface() {
                                            @Override
                                            public void onUpdateLocation(boolean status) {
                                                if (status) {
                                                    Log.d("Testing", "everything passed with checking out the user");
                                                    Intent intent = new Intent(getApplicationContext(), StudentProfileActivity.class);
                                                    intent.putExtra("user", email);
                                                    startActivity(intent);
                                                } else {
                                                    Log.d("DB", "failed to update location");
                                                }
                                            }

                                        });
                                    } else {
                                        Log.d("Testing", "failed to update location");
                                    }
                                }
                            });
                        }
                    });
                } else {
                    // TODO: toast???
                    Log.d("cehckout", "checkout failed");
                }
            }
        });
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
        final EditText imageLink = new EditText(StudentProfileActivity.this);
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

    public void alert(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void alertNice(String message, boolean kick_out){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (kick_out) {
                    db.updateLocation(email, "none", new updateLocationInterface() {
                        @Override
                        public void onUpdateLocation(boolean status) {

                        }
                    });
                }
                dialog.dismiss(); // cehck this
            }
        });
        alert.setMessage(message);

        AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }

}