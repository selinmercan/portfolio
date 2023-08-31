package com.example.trojancheckincheckout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.core.content.res.ResourcesCompat;
import android.graphics.Typeface;

public class BuildingCurrentStudentsActivity extends AppCompatActivity {

    ArrayList<Record> buildingHistory = new ArrayList<>();
    String buildingName = "";
    String email = "";
    String curr_capacity = "";
    String max_capacity = "";
    TableLayout tl;

    DatabaseReference databaseReference;
        // DO NOT TOUCH - persistent listener

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_current_students);

        TextView textView = findViewById(R.id.title);

        Typeface typeface = ResourcesCompat.getFont(
                this,
                R.font.poppins);
        textView.setTypeface(typeface);

        buildingName = getIntent().getStringExtra("building");
        email = getIntent().getStringExtra("user");
        curr_capacity = getIntent().getStringExtra("capacity");
        max_capacity = getIntent().getStringExtra("max_capacity");

        tl = (TableLayout) findViewById(R.id.student_list_table);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        Database db = new Database();
        db.getBuildingHistory(buildingName, new getRecordsByBuildingInterface() {

            @Override
            public void onGetRecordsByBuilding(ArrayList<Record> records) {
                if (records == null) {
                    //TODO: toast?

                }
                else {
                    // TODO - this gets the records correctly, but idk how to put them in the table view below
                    buildingHistory = records;


                    displayHistory();
                }
            }
        });

        databaseReference.child("records").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Record> updatedList = new ArrayList<>();
                for (DataSnapshot record : snapshot.getChildren()) {
                    if (record.child("building_name").getValue().toString().equals(buildingName)) {
                        String check_in_string = record.child("check_in").child("time").getValue().toString();
                        Date check_in_date = new Date(Long.parseLong(check_in_string));
                        Date check_out_date = null;
                        if (record.child("check_out").exists()) {
                            String check_out_string = record.child("check_out").child("time").getValue().toString();
                            check_out_date = new Date(Long.parseLong(check_out_string));
                        }

                        Record r = new Record();
                        r.building_name = record.child("building_name").getValue().toString();
                        r.user_email = record.child("user_email").getValue().toString();
                        r.check_in = check_in_date;
                        r.check_out = check_out_date;
                        updatedList.add(r);
                    }
                }

                buildingHistory = updatedList;
                displayHistory();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void displayHistory(){

        // removing what was already there

        if (tl != null && tl.getChildCount() > 0) {
            tl.removeAllViews();
        }

        TableRow tr_head = new TableRow(getApplicationContext());

        tr_head.setBackgroundColor(Color.GRAY);        // part1
        tr_head.setLayoutParams(new TableRow.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        TextView col1 = new TextView(getApplicationContext());
        col1.setText("Email");
        tr_head.addView(col1);

        TextView col2 = new TextView(getApplicationContext());
        col2.setText("Check-In");
        tr_head.addView(col2);

        TextView col3 = new TextView(getApplicationContext());
        col3.setText("Check-Out");
        tr_head.addView(col3);

        TextView col4 = new TextView(getApplicationContext());
        col4.setText("More");
        tr_head.addView(col4);

        tl.setColumnStretchable(0, true);
        tl.setColumnStretchable(1, true);
        tl.setColumnStretchable(2, true);
        tl.setColumnShrinkable(3, true);

        TableRow tr;

        Record r = new Record();

        tl.addView(tr_head, new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,                    //part4
                TableLayout.LayoutParams.MATCH_PARENT));

        for (int i = 0; i < buildingHistory.size(); ++i) {
            tr = new TableRow(getApplicationContext());

            r = buildingHistory.get(i);

            if (r.getCheck_out() == null) {
                TextView cell;

                for (int j = 0; j < 3; ++j) {
                    cell = new TextView(getApplicationContext());
                    cell.setLayoutParams(new TableRow.LayoutParams(
                            TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT));
                    cell.setMaxLines(10);

                    if (j == 0) {
                        String concat = r.getUser_email() + " ";
                        cell.setText(concat);
                    } else if (j == 1) {
                        if (r.getCheck_in() == null) {
                            cell.setText("None");
                        } else {
                            try {
                                DateFormat formatter = new SimpleDateFormat("dd MMM HH:mm ");
                                String output = formatter.format(r.getCheck_in());
                                cell.setText(output);
                            } catch (Exception e) {
                                cell.setText("error");
                                e.printStackTrace();
                            }
                        }
                    } else {
                        if (r.getCheck_out() == null) {
                            cell.setText("None");
                        } else {
                            try {
                                DateFormat formatter = new SimpleDateFormat("dd MMM HH:mm ");
                                String output = formatter.format(r.getCheck_out());
                                cell.setText(output);
                            } catch (Exception e) {
                                cell.setText("error");
                                e.printStackTrace();
                            }
                        }
                    }

                    tr.addView(cell);
                }

                ImageButton info = new ImageButton(getApplicationContext());

                info.setContentDescription("@string/info_button");
                info.setImageResource(R.drawable.info);
                Record finalR = r;
                info.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        realOnClick(finalR.getUser_email());
                    }
                });
                tr.addView(info);
                tl.addView(tr, new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.MATCH_PARENT));
            }
        }
    }

    public void realOnClick(String rowEmail){
        Intent i = new Intent(this, StudentDetailsActivity.class);
        Log.d("Forward", "To student details, " + curr_capacity + " : " + max_capacity);
        i.putExtra("building", buildingName);
        i.putExtra("student", rowEmail);
        i.putExtra("user", email);

        i.putExtra("capacity", curr_capacity);
        i.putExtra("max_capacity", max_capacity);
        startActivity(i);
    }

    public void goBack(View view){
        // should take us back to the building page action once it exists
        Intent i = new Intent(this, BuildingInfoActivity.class);
        Log.d("Back", "To building info, " + curr_capacity + " : " + max_capacity);
        i.putExtra("user", email);
        i.putExtra("building", buildingName);
        i.putExtra("capacity", curr_capacity);
        i.putExtra("max_capacity", max_capacity);
        startActivity(i);
    }

}