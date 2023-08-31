package com.example.trojancheckincheckout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.content.res.ResourcesCompat;
import android.graphics.Typeface;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BuildingInfoActivity extends AppCompatActivity {
    String buildingName = "";
    String email = "";
    String curr_capacity = "";
    String max_capacity = "";
    Database db;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_info);

        email = getIntent().getStringExtra("user");
        buildingName = getIntent().getStringExtra("building");
        curr_capacity = getIntent().getStringExtra("capacity");
        max_capacity = getIntent().getStringExtra("max_capacity");

        TextView textView = findViewById(R.id.building_name);
        TextView textView2 = findViewById(R.id.capacity);
//        TextView textView3 = findViewById(R.id.contact);
        TextView textView4 = findViewById(R.id.contactactual);

        Typeface typeface = ResourcesCompat.getFont(
                this,
                R.font.poppins);
//        textView.setTypeface(typeface);
//        textView2.setTypeface(typeface);
////        textView3.setTypeface(typeface);
//        textView4.setTypeface(typeface);



        TextView capacity = findViewById(R.id.label_for_name);
        TextView build_name = findViewById(R.id.building_name);
        EditText max_input = findViewById(R.id.capacityinput);
        db = new Database();

        capacity.setText(curr_capacity);
        build_name.setText(buildingName);
        CharSequence temp = max_capacity;
        max_input.setText(temp);

        Database db = new Database();

        ImageView pic = (ImageView) findViewById(R.id.qrcode);

        db.getBuildingQRCode(buildingName, new getQRCodeInterface() {
            @Override
            public void getBuildingQRCode(Bitmap bitmap) {

                if (bitmap != null) {
                    pic.setImageBitmap(bitmap);
                }
                else {
                    // gen the QR code here but this will never happen
                    pic.setImageDrawable(getResources().getDrawable(R.drawable.tommytrojan));
                }
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("buildings").child(buildingName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                capacity.setText(snapshot.child("curr_capacity").getValue().toString());
                max_input.setText(snapshot.child("max_capacity").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void goBack(View view){
        Intent i = new Intent(this, ManagerBuildingsActivity.class);
        i.putExtra("user", email);
        startActivity(i);
    }

    public void alert(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void viewStudentsInBuilding(View view) {
        Intent intent = new Intent(getApplicationContext(), BuildingCurrentStudentsActivity.class);
        Log.d("Forward", "To student list, " + curr_capacity + " : " + max_capacity);
        intent.putExtra("building", buildingName);
        intent.putExtra("user", email);
        intent.putExtra("capacity", curr_capacity);
        intent.putExtra("max_capacity", max_capacity);
        startActivity(intent);
    }

    public void updateMaxCapacity(View view) {
        EditText new_max = findViewById(R.id.capacityinput);

        int val = 0;
        try {
            val = Integer.parseInt(new_max.getText().toString());
        }
        catch (NumberFormatException e){
            alert("Invalid Capacity");
            return;
        }
        if(val < 0){
            alert("Invalid Capacity");
            return;
        }

        if (Integer.parseInt(curr_capacity) > val) {
            alert("Cannot set capacity lower to current occupancy");
            return;
        }

        db.updateMaxCapacity(buildingName, val, new updateMaxCapacityInterface() {
            @Override
            public void updateMaxCapacity(boolean status) {
                if (status) {
                    Intent i = new Intent(getApplicationContext(), ManagerBuildingsActivity.class);
                    i.putExtra("user", email);
                    startActivity(i);
                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(getApplicationContext());
                    alert.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    alert.setMessage("Unable to update max capacity");

                    AlertDialog alertDialog = alert.create();
                    alertDialog.show();
                }
            }
        });
    }
}