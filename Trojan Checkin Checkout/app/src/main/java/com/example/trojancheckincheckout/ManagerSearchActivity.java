package com.example.trojancheckincheckout;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.core.content.res.ResourcesCompat;
import android.graphics.Typeface;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.datepicker.DateValidatorPointBackward;

import java.util.ArrayList;
import java.util.Calendar;

import static java.lang.String.format;

public class ManagerSearchActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String email = "";

    Button start_date;
    Button start_time;
    Button end_date;
    Button end_time;

    Calendar startDateTime = Calendar.getInstance();
    Calendar endDateTime = Calendar.getInstance();

    boolean startDateSet = false;
    boolean startTimeSet = false;
    boolean endDateSet = false;
    boolean endTimeSet = false;

    // corresponds 1-4 to the edit text vars above
    DatePickerDialog datePickerDialog1;
    TimePickerDialog timePickerDialog1;
    DatePickerDialog datePickerDialog2;
    TimePickerDialog timePickerDialog2;

    int majorIndex = 1;
    int buildingIndex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_search); // this line is causing errors

        TextView textView = findViewById(R.id.title);

        Typeface typeface = ResourcesCompat.getFont(
                this,
                R.font.poppins);
        textView.setTypeface(typeface);

        Intent intent = getIntent();
        email = intent.getStringExtra("user");

        startDateSet = false;
        startTimeSet = false;
        endDateSet = false;
        endTimeSet = false;
        majorIndex = 1;
        buildingIndex = 1;

        createDatePickers();

        Spinner building = (Spinner)findViewById(R.id.building);

        building.setOnItemSelectedListener(this);

        Database db = new Database();
        db.getBuildingList(new getBuildingsInterface() {
            @Override
            public void onGetBuildings(ArrayList<Building> buildings) {
                String[] options = new String[buildings.size()+1];
                options[0] = "none";
                for (int i = 0; i < buildings.size(); i++) {
                    options[i+1] = buildings.get(i).buildingName;
                }
                ArrayAdapter<String> building_adapter = new ArrayAdapter<>(ManagerSearchActivity.this, android.R.layout.simple_spinner_dropdown_item, options);
                building.setAdapter(building_adapter);
            }
        });

        Spinner major = (Spinner)findViewById(R.id.major);

        major.setOnItemSelectedListener(this);

        String[] items = new String[]{"none", "Anthropology", "Business Administration", "Chemistry", "Computer Science", "Economics", "History"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        major.setAdapter(adapter);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (parent.getId() == R.id.major) {
            majorIndex = position;
        }
        else if (parent.getId() == R.id.building) {
            buildingIndex = position;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void createDatePickers() {

        // initiate the date picker and a button
        start_date = (Button) findViewById(R.id.start_date);
        // perform click event on edit text

        start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog1 = new DatePickerDialog(ManagerSearchActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                String temp = "Start Date: " + String.format("%02d/%02d/%04d",(monthOfYear + 1),dayOfMonth, year);
//                                String temp = (monthOfYear + 1) + "/" + dayOfMonth + "/" + year;
//                                String temp = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                start_date.setText(temp);

                                startDateTime.set(year, monthOfYear, dayOfMonth);
                                startDateSet = true;
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog1.show();
            }
        });

        // initiate the date picker and a button
        start_time = (Button) findViewById(R.id.start_time);
        // perform click event on edit text
        start_time.setOnClickListener(new View.OnClickListener() {

            final int hourOfDay = 0;
            final int minute = 0;

            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender

                // date picker dialog
                timePickerDialog1 = new TimePickerDialog(ManagerSearchActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String temp = "Start Time: " + String.format("%02d:%02d", hourOfDay, minute);
//                        String temp = hourOfDay + ":" + minute;
                        start_time.setText(temp);

                        startDateTime.set(Calendar.HOUR, hourOfDay);
                        startDateTime.set(Calendar.MINUTE, minute);
                        startTimeSet = true;
                    }
                }, hourOfDay, minute, true);

                timePickerDialog1.show();
            }
        });


        // end time date stuff

        // initiate the date picker and a button
        end_date = (Button) findViewById(R.id.end_date);
        // perform click event on edit text
        end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog2 = new DatePickerDialog(ManagerSearchActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                String temp = "End Date: " + String.format("%02d/%02d/%04d",(monthOfYear + 1),dayOfMonth, year);
                                end_date.setText(temp);

                                endDateTime.set(year, monthOfYear, dayOfMonth);
                                endDateSet = true;
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog2.show();
            }
        });

        // initiate the date picker and a button
        end_time = (Button) findViewById(R.id.end_time);
        // perform click event on edit text
        end_time.setOnClickListener(new View.OnClickListener() {

            final int hourOfDay = 0;
            final int minute = 0;

            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender

                // date picker dialog
                timePickerDialog2 = new TimePickerDialog(ManagerSearchActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String temp = "End Time: " + String.format("%02d:%02d", hourOfDay, minute);
                        end_time.setText(temp);

                        endDateTime.set(Calendar.HOUR, hourOfDay);
                        endDateTime.set(Calendar.MINUTE, minute);
                        endTimeSet = true;
                    }
                }, hourOfDay, minute, true);

                timePickerDialog2.show();
            }
        });
    }


    public void onBack(View view) {
        Intent i = new Intent(this, ManagerProfileActivity.class);
        i.putExtra("user", email);
        startActivity(i);
    }

    public void search(View view) {

        // validate name
        String name = ((EditText)findViewById(R.id.name)).getText().toString();
        if (name.trim().equals("")) {
            name = null;
        }
        else {
            boolean passed = true;
            for (int i = 0; i < name.length(); ++i) {
                if (!Character.isAlphabetic(name.charAt(i)) && !Character.isWhitespace(name.charAt(i))) {
                    passed = false;
                }
            }

            if (!passed) {
                alert("Name field can only contain letters");
                return;
            }
        }

        String building = null;
        if (buildingIndex != -1) {
            building = (String) ((Spinner)findViewById(R.id.building)).getItemAtPosition(buildingIndex);
        }

        String major = null;
        if (majorIndex != -1) {
            major = (String) ((Spinner)findViewById(R.id.major)).getItemAtPosition(majorIndex);
        }

        String idString = ((EditText) findViewById(R.id.uscID)).getText().toString();
        Long id = null;
        if (!idString.trim().equals("")) {

            try {
                id = Long.parseLong(idString);
            }
            catch(Exception e) {
                e.printStackTrace();
                alert("ID field can only contain numbers");
                return;
            }
        }
        // ten 9's, the maximum theoretical USC ID
        // have to do it this way bc android studio is stupid
        long big = 1111111111L;
        big *= 9;

        if (id != null && (id < 1000000000 || id > big)) {
            alert("Please enter a valid usc ID");
            return;
        }

        // some times were passed
        if (startDateSet || startTimeSet || endDateSet || endTimeSet) {

            // all times were passed
            if (startDateSet && startTimeSet && endDateSet && endTimeSet) {
                //call search with all the vars
                Intent i = new Intent(this, SearchResultsActivity.class);
                i.putExtra("user", email);
                i.putExtra("name", name);
                i.putExtra("building", building);
                i.putExtra("major", major);
                i.putExtra("id", id);
                i.putExtra("startCal", startDateTime.getTimeInMillis());
                i.putExtra("endCal", endDateTime.getTimeInMillis());
                startActivity(i);
            }
            // not all of the times were passed
            else {
                startDateSet = false;
                startTimeSet = false;
                endDateSet = false;
                endTimeSet = false;
                alert("If you enter one date/time you must fill out all of them");
            }
        }
        // no times were passed
        else {
            //call search with all the vars
            Intent i = new Intent(this, SearchResultsActivity.class);
            i.putExtra("user", email);
            i.putExtra("name", name);
            i.putExtra("building", building);
            i.putExtra("major", major);
            i.putExtra("id", id);

            System.out.println(email + building + major);
            startActivity(i);
        }

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
