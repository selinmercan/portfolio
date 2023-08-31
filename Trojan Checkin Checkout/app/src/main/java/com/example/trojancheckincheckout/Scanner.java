package com.example.trojancheckincheckout;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;

public class Scanner extends AppCompatActivity {
    String email;
    CodeScanner codeScanner;
    CodeScannerView scannView;
    TextView resultData;
    Database db;
    String type; //either 'check-in' or 'check-out'; needed bc same scanner used for both interactions
    String buildingName = ""; // if they are in a building give its name
    boolean pausedByAleart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        scannView = findViewById(R.id.scannerView);
        codeScanner = new CodeScanner(this,scannView);
        resultData = findViewById(R.id.resultsOfQr);
        email = getIntent().getStringExtra("user");
        type = getIntent().getStringExtra("type");
        buildingName = getIntent().getStringExtra("building");

        db = new Database();


        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("DB", result.getText());
                        resultData.setText(result.getText());

                        if (type.equals("check-in")) {

                            AlertDialog.Builder alert = new AlertDialog.Builder(Scanner.this);
                            alert.setMessage("Are you sure you want to check in to " + result.getText() + "?");

                            alert.setPositiveButton("Yes",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            checkIn(result.getText());
                                        }
                                    });
                            alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //TODO: Insert Check Out without Scanning

                                    Intent intent = new Intent(getApplicationContext(), StudentProfileActivity.class);
                                    intent.putExtra("user", email);
                                    startActivity(intent);
                                }
                            });

                            AlertDialog popUp = alert.create();
                            popUp.show();
//                            Log.d("Checkin", "performing a check in");

                        } else if (type.equals("check-out")) {

                            if (buildingName.equals(result.getText())) {
                                Log.d("Checkout", "cehcking out");
                                AlertDialog.Builder alert = new AlertDialog.Builder(Scanner.this);
                                alert.setMessage("Are you sure you want to check out?");

                                alert.setPositiveButton("Yes",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                checkOut(result.getText());
                                            }
                                        });
                                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //TODO: Insert Check Out without Scanning

                                        Intent intent = new Intent(getApplicationContext(), StudentProfileActivity.class);
                                        intent.putExtra("user", email);
                                        startActivity(intent);
                                    }
                                });

                                AlertDialog popUp = alert.create();
                                popUp.show();
                            }
                            else {
                                Intent intent = new Intent(getApplicationContext(), StudentProfileActivity.class);
                                intent.putExtra("user", email);
                                intent.putExtra("building", buildingName);
                                intent.putExtra("alert", "checkout-scan");
                                startActivity(intent);
                            }
                        }
                    }
                });
            }
        });

        scannView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeScanner.startPreview();
            }
        });
    }



    public void checkIn(String building) {

        db.getBuildingList(new getBuildingsInterface() {
            @Override
            public void onGetBuildings(ArrayList<Building> buildings) {
                boolean validBuilding = false;
                for (Building b : buildings) {
                    if (b.getName().equals(building)) {
                        validBuilding = true;
                    }
                }

                if (validBuilding) {
                    db.getBuildingCurrentCapacity(building, new getCurrentCapacityInterface() {
                        @Override
                        public void getCurrCapacity(int capacity) {
                            Log.d("capacity", "made it to get cur capacity");
                            int cur_cap = capacity;
                            db.getBuildingMaxCapacity(building, new getMaxCapacityInterface() {
                                @Override
                                public void getMaxCapacity(int capacity) {
                                    Log.d("capacity", "made it to get max capacity");
                                    Log.d("capacity", "made it to the check");
                                    if (capacity <= cur_cap) {
                                        Log.d("capacity", "building was full");
                                        Intent intent = new Intent(getApplicationContext(), StudentProfileActivity.class);
                                        intent.putExtra("user", email);
                                        intent.putExtra("building", building);
                                        intent.putExtra("alert", "buildingFull");
                                        startActivity(intent);
                                    } else {
                                        Log.d("capacity", "building has room");
                                        db.checkIn(email, building, new checkInInterface() {
                                            @Override
                                            public void onCheckIn(boolean success) {
                                                if (success) {
                                                    db.updateLocation(email, building, new updateLocationInterface() {
                                                        @Override
                                                        public void onUpdateLocation(boolean status) {
                                                            if (status) {
                                                                db.getBuildingCurrentCapacity(building, new getCurrentCapacityInterface() {
                                                                    @Override
                                                                    public void getCurrCapacity(int capacity) {
                                                                        int new_capacity = capacity + 1;
                                                                        db.updateCurrentCapacity(building, new_capacity, new updateCurrentCapacityInterface() {
                                                                            @Override
                                                                            public void updateCurrCapacity(boolean status) {
                                                                                if (status) {
                                                                                    Intent intent = new Intent(getApplicationContext(), StudentProfileActivity.class);
                                                                                    intent.putExtra("user", email);
                                                                                    startActivity(intent);
                                                                                } else {
                                                                                    Toast.makeText(Scanner.this, "", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            }
                                                                        });
                                                                    }
                                                                });
                                                            } else {
                                                                Toast.makeText(getApplicationContext(), "Could not check in", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                } else {
                                                    Log.d("checkin", "checkin failed");
                                                    // TODO: toast???
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    });
                } else {
                    Intent intent = new Intent(getApplicationContext(), StudentProfileActivity.class);
                    intent.putExtra("user", email);
                    intent.putExtra("alert", "badQR");
                    startActivity(intent);
                }
            }
        });
    }

    public void checkOut(String building) {

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
                                        Log.d("Checkin", "updating current capacity");
                                        db.updateLocation(email, "none", new updateLocationInterface() {
                                            @Override
                                            public void onUpdateLocation(boolean status) {
                                                if (status) {
                                                    Log.d("Checkin", "everything passed with checking out the user");
                                                    Intent intent = new Intent(getApplicationContext(), StudentProfileActivity.class);
                                                    intent.putExtra("user", email);
                                                    startActivity(intent);
                                                } else {
                                                    Log.d("Checkin", "failed to update location");
                                                }
                                            }
                                        });
                                    } else {
                                        Log.d("Checkin", "failed to update location");
                                    }
                                }
                            });
                        }
                    });
                } else {
                    //TODO: toast???
                    Log.d("checkout", "checkout failed");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        requestForCamera();

    }

    public void requestForCamera() {
        Dexter.withActivity(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {

                codeScanner.startPreview();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                Toast.makeText(Scanner.this, "Camera Permission is Required.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                token.continuePermissionRequest();

            }
        }).check();
    }


}