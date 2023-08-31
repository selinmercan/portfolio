package com.example.trojancheckincheckout;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

public class SearchResultsActivity extends AppCompatActivity {
    String email = "";
    String name = "";
    String building = "";
    String major = "";
    Long id = null;

    long startCal;
    long endCal;

    TableLayout tl;
    ArrayList<String[]> studentInfo;
    HashSet<String> seen;

    Calendar startDateTime = Calendar.getInstance();
    Calendar endDateTime = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results); // this line is causing errors

        seen = seen = new HashSet<>();
        studentInfo = new ArrayList<>();

        Intent intent = getIntent();
        email = intent.getStringExtra("user");

        name = intent.getStringExtra("name");
        building = intent.getStringExtra("building");
        major = intent.getStringExtra("major");
        startCal = intent.getLongExtra("startCal", 0);
        endCal = intent.getLongExtra("endCal", 0);
        id = intent.getLongExtra("id", 0);

        // TODO: Abbas do you want an empty id to be null or zero?
        // Is 0000000000 a valid id?
        if (id == 0) {
            id = Long.valueOf(0);
        }

        if (name == null) name = "";
        if (building == null) building = "none";

        Database db = new Database();

        //TODO: integrate new id var into the querry
        db.userQuery(major, id, new userQueryInterface() {
            @Override
            public void userQuery(ArrayList<User> results) {
                ArrayList<User> users = results;
                db.recordQuery(building, startCal, endCal, new resultsQueryInterface() {
                    @Override
                    public void resultsQuery(HashMap<String, Record> results) {

                        for (User u : users) {
                            System.out.println(u);
                            if (building.equals("none") && startCal == 0 && endCal == 0) {
                                // TODO - anything inside of this bracket is a valid result for the search query
                                String first_name = u.first_name;
                                String last_name = u.last_name;
                                String fullName = first_name + " " + last_name;
                                String id = "" + u.usc_ID;

                                if (fullName.toLowerCase().contains(name.toLowerCase())) {
                                    String[] temp = new String[3];
                                    temp[0] = fullName;
                                    temp[1] = id;
                                    temp[2] = u.email;
                                    studentInfo.add(temp);
                                }
                            } else if (results.get(u.email) != null) {
                                String first_name = u.first_name;
                                String last_name = u.last_name;
                                String fullName = first_name + " " + last_name;
                                String id = "" + u.usc_ID;

                                if (fullName.toLowerCase().contains(name.toLowerCase())) {
                                    String[] temp = new String[3];
                                    temp[0] = fullName;
                                    temp[1] = id;
                                    temp[2] = u.email;
                                    studentInfo.add(temp);
                                }
                            }
                        }

                        sortData();
                        displayData();
                    }
                });
            }
        });

//        db.queryRecords(building, startCal, endCal, new queryRecordsInterface() {
//            @Override
//            public void queryRecordsResult(ArrayList<Record> results) {
//                ArrayList<Record> records = results;
//                db.queryUsers(major, new queryUsersInterface() {
//                    @Override
//                    public void queryUsersResults(HashMap<String, User> results) {
//                        System.out.println("We have " + results.size() + " users with that major.");
//                        HashMap<String, User> mapped_users = results;
//
//                        for (Record r : records) {
//                            if (mapped_users.get(r.user_email) != null) {
//                                String first_name = mapped_users.get(r.user_email).first_name;
//                                String last_name = mapped_users.get(r.user_email).last_name;
//                                String major = mapped_users.get(r.user_email).major;
//                                long id =  mapped_users.get(r.user_email).getUsc_ID();
//                                Date check_in = r.check_in;
//                                Date check_out = r.check_out;
//                                String email = mapped_users.get(r.user_email).getEmail();
//                                String fullName = first_name + " " + last_name;
//
//                                if (fullName.toLowerCase().contains(name.toLowerCase())) {
//
//                                    if (!seen.contains(email.toLowerCase())) {
//                                        seen.add(email.toLowerCase());
//                                        String[] temp = new String[3];
//                                        temp[0] = email;
//                                        temp[1] = fullName;
//                                        temp[2] = "" + id;
//                                        studentInfo.add(temp);
//                                    }
//                                    else {
//                                        Log.d("search", "already seen " + email);
//                                    }
//                                    System.out.println(first_name + " " + last_name + " was in " + r.building_name + " studying " + major + " from " + check_in + " to " + check_out);
//                                }
//                            }
//                        }
//
//                        sortData();
//                        displayData();
//                    }
//                });
//            }
//        });

    }

    private void sortData(){
        // comparator with a lambda expression
        Comparator<String[]> comparator = (o1, o2) -> {

            // the array contains [email, name, id] so we are sorting by name
            String name1 = o1[0].toUpperCase();
            String name2 = o2[0].toUpperCase();

            //ascending order
            return name1.compareTo(name2);

            // descending order would be
            // return name2.compareTo(name1);
        };

        studentInfo.sort(comparator);

        Log.d("search", "" + studentInfo.size());
    }


    public void displayData() {

        tl = (TableLayout) findViewById(R.id.resultsTable);

        if (tl != null && tl.getChildCount() > 0) {
            tl.removeViews(1, tl.getChildCount() - 1);
        }

        TableRow tr_head = new TableRow(this);

        tr_head.setLayoutParams(new TableRow.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        TextView col1 = new TextView(this);
        col1.setText("Name");
        col1.setTextColor(Color.BLACK);
        tr_head.addView(col1);

        TextView col2 = new TextView(this);
        col2.setText("USCID");
        col2.setTextColor(Color.BLACK);
        tr_head.addView(col2);

        TextView col3 = new TextView(this);
        col3.setText("Details");
        col3.setTextColor(Color.BLACK);
        tr_head.addView(col3);

        tl.addView(tr_head, new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.MATCH_PARENT));

//        tl.setColumnShrinkable(0, true);
//        tl.setColumnShrinkable(1, true);
        tl.setColumnShrinkable(2, true);


        TableRow tr;
        String[] temp;
        for (int i = 0; i < studentInfo.size(); ++i) {
            tr = new TableRow(this);
            temp = studentInfo.get(i);
            TextView cell;
            for (int j = 0; j < 2; ++j) {
                cell = new TextView(this);
                cell.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                cell.setMaxLines(10);

                if (j == 0) {
                    String concat = temp[0] + " ";
                    cell.setText(concat);
                }
                else if (j == 1) {
                    String concat = "" + temp[1] + " ";
                    cell.setText(concat);
                }

                tr.addView(cell);
            }
            ImageButton info = new ImageButton(this);
            info.setContentDescription("@string/info_button");
            info.setImageResource(R.drawable.info);
            String[] finalArr = temp;
            info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    realOnClick(finalArr[2]);
                }
            });
            tr.addView(info);
            tl.addView(tr, new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,                    //part4
                    TableLayout.LayoutParams.MATCH_PARENT));
        }
    }

    private void realOnClick(String studentEmail) {
        Intent i = new Intent(this, StudentDetailsActivity.class);
        i.putExtra("user", email);
        i.putExtra("student", studentEmail);
        i.putExtra("from", "search");
        i.putExtra("user", email);
        i.putExtra("name", name);
        i.putExtra("building", building);
        i.putExtra("major", major);
        i.putExtra("startCal", startCal);
        i.putExtra("endCal", endCal);
        startActivity(i);
    }

    public void onBack(View view) {
        Intent i = new Intent(this, ManagerProfileActivity.class);
        i.putExtra("user", email);
        startActivity(i);
    }

    public void search(View view) {
        Intent i = new Intent(this, ManagerSearchActivity.class);
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
