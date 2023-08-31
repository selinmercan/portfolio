package com.example.trojancheckincheckout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import androidx.core.content.res.ResourcesCompat;
import android.graphics.Typeface;

public class UserHistoryActivity extends AppCompatActivity {

    ArrayList<Record> studentHistory = new ArrayList<>();
    String email = "";
    String role = "";
    TableLayout tl;

//    Record r = new Record();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_history);

        TextView textView = findViewById(R.id.title);

        Typeface typeface = ResourcesCompat.getFont(
                this,
                R.font.poppins);
        textView.setTypeface(typeface);

        Intent intent = getIntent();
        email = intent.getStringExtra("user");
        role = intent.getStringExtra("role");


        Database db = new Database();

        db.getStudentHistory(email, new getRecordsByStudentInterface() {
            @Override
            public void onGetRecordsByStudent(ArrayList<Record> records) {
                if (records == null) {
                    Log.d("DB", "error retrieving student records");
                } else {
                    Log.d("DB", "successful retrieval of student records");
                    // student records are stored in records ArrayList
                    studentHistory = records;

                    displayData();
                }
            }

        });
    }

    public void displayData() {
        tl = (TableLayout) findViewById(R.id.historyTable);

        TableRow tr_head = new TableRow(this);

        tr_head.setLayoutParams(new TableRow.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        TextView col1 = new TextView(this);
        col1.setText("Building");
        col1.setTextColor(Color.BLACK);
        tr_head.addView(col1);

        TextView col2 = new TextView(this);
        col2.setText("Check In Time");
        col2.setTextColor(Color.BLACK);
        tr_head.addView(col2);

        TextView col3 = new TextView(this);
        col3.setText("Check Out Time");
        col3.setTextColor(Color.BLACK);
        tr_head.addView(col3);

        tl.addView(tr_head, new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.MATCH_PARENT));

        tl.setColumnShrinkable(0, true);
        tl.setColumnShrinkable(1, true);
        tl.setColumnShrinkable(2, true);

        TableRow tr;
        Record r;

        for (int i = 0; i < studentHistory.size(); ++i) {
            tr = new TableRow(this);
            r = studentHistory.get(i);
            TextView cell;

            for (int j = 0; j < 3; ++j) {
                cell = new TextView(this);
                cell.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                cell.setMaxLines(10);

                if (j == 0) {
                    String concat = r.getBuilding_name() + " ";
                    cell.setText(concat);
                }
                else if (j == 1) {
                    if (r.getCheck_in() == null) {
                        cell.setText("None");
                    }
                    else {
                        DateFormat formatter = new SimpleDateFormat("dd MMM HH:mm ");
                        String output = formatter.format(r.getCheck_in());
                        cell.setText(output);
                    }
                }
                else  {
                    if (r.getCheck_out() == null) {
                        cell.setText("None");
                    }
                    else {
                        DateFormat formatter = new SimpleDateFormat("dd MMM HH:mm ");
                        String output = formatter.format(r.getCheck_out());
                        cell.setText(output);
                    }
                }
                tr.addView(cell);
            }
            tl.addView(tr, new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,                    //part4
                    TableLayout.LayoutParams.MATCH_PARENT));
        }
    }

    public void goBack(View view){

        if (role.equals("student")) {
            Intent i = new Intent(this, StudentProfileActivity.class);
            i.putExtra("user", email);
            i.putExtra("role", role);
            startActivity(i);
        }
        else {
            Intent i = new Intent(this, ManagerProfileActivity.class);
            i.putExtra("user", email);
            i.putExtra("role", role);
            startActivity(i);
        }
    }
}