package com.example.trojancheckincheckout;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class StudentDetailsActivity extends AppCompatActivity {

    public String email = "";
    public String studentEmail = "";
    String buildingName = "";
    String curr_capacity = "";
    String max_capacity = "";
    public User stud = new User();
    TableLayout tl;
    public ArrayList<Record> studentHistory = new ArrayList<>();
    String name = "";
    String from = "";
    String major = "";
    long startCal = 0;
    long endCal = 0;
    public Database db;
    Button Send;
    Uri URI = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);

        db = new Database();

        TextView textView = findViewById(R.id.managerprofile);
        TextView textView2 = findViewById(R.id.name);
        TextView textView3 = findViewById(R.id.info);
        TextView textView4 = findViewById(R.id.label_for_name);
        TextView textView5 = findViewById(R.id.info1);
        TextView textView6 = findViewById(R.id.label_for_usc_id);
        TextView textView7 = findViewById(R.id.info2);
        TextView textView8 = findViewById(R.id.label_for_major);
        TextView textView9 = findViewById(R.id.curr_loc);
        TextView textView10 = findViewById(R.id.label_for_cur_building);

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

        // all of the intent information is just used for determining where
        // to go and what information is needed when pressing the back
        // button, do not use it for features on this page
        // because the values refer to different things than
        // what might seem relevant for this page
        Intent intent = getIntent();
        // these fields exist if we come from the building info page
        email = intent.getStringExtra("user");
        studentEmail = intent.getStringExtra("student");
        buildingName = getIntent().getStringExtra("building");
        curr_capacity = getIntent().getStringExtra("capacity");
        max_capacity = getIntent().getStringExtra("max_capacity");

        // these fields exist when coming from student search page
        from = getIntent().getStringExtra("from");
        name = intent.getStringExtra("name");
        major = intent.getStringExtra("major");
        startCal = intent.getLongExtra("startCal", 0);
        endCal = intent.getLongExtra("endCal", 0);

        Send = findViewById(R.id.btSend);
        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });

        Database db = new Database();

        db.getStudent(studentEmail, new getUserInterface() {
            @Override
            public void getUser(User user) {
                if (user == null) {
                    Log.d("DB", "there was an error getting the student object");
                } else {
                    stud = user;

                    TextView major = (TextView) findViewById(R.id.label_for_major);
                    TextView name = (TextView) findViewById(R.id.label_for_name);
                    TextView uscID = (TextView) findViewById(R.id.label_for_usc_id);
                    TextView location = (TextView) findViewById(R.id.label_for_cur_building);

                    String placeholder = user.getFirst_name() + " " + user.getLast_name();
                    major.setText(user.getMajor());
                    name.setText(placeholder);

                    uscID.setText(String.valueOf(user.getUsc_ID()));

                    if (user.getCurr_loc().equals("")) {
                        location.setText("You are not currently in a building");
                    }
                    else {
                        location.setText(user.getCurr_loc());
                    }

                    ImageView pic = (ImageView) findViewById(R.id.tommy);

                    db.getStudentPicture(studentEmail, new getPictureInterface() {
                        @Override
                        public void getPicture(Bitmap encoded_image) {
                            if (encoded_image != null) {
                                pic.setImageBitmap(encoded_image);
                            } else {
                                pic.setImageDrawable(getResources().getDrawable(R.drawable.tommytrojan));
                            }

                            db.getStudentHistory(studentEmail, new getRecordsByStudentInterface() {
                                @Override
                                public void onGetRecordsByStudent(ArrayList<Record> records) {
                                    if (records == null) {
                                        //TODO: toast?
                                    }
                                    else {
                                        studentHistory = records;
                                        System.out.println(records.toString());
                                        displayData();
                                    }
                                }

                            });

                        }
                    });
                }
            }
        });

    }

    public void displayData() {
        tl = (TableLayout) findViewById(R.id.historyTable);

        if (tl != null && tl.getChildCount() > 0) {
            tl.removeViews(0, tl.getChildCount());
        }

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
                        try {
                            DateFormat formatter = new SimpleDateFormat("dd MMM HH:mm ");
                            String output = formatter.format(r.getCheck_in());
                            cell.setText(output);
                        } catch (Exception e) {
                            cell.setText("error");
                            e.printStackTrace();
                        }
                    }
                }
                else  {
                    if (r.getCheck_out() == null) {
                        cell.setText("None");
                    }
                    else {
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
            tl.addView(tr, new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,                    //part4
                    TableLayout.LayoutParams.MATCH_PARENT));
        }
    }

    public void goBack(View view){

        if (from != null && from.equals("search")) {
            Intent i = new Intent(this, SearchResultsActivity.class);
            i.putExtra("user", email);
            i.putExtra("name", name);
            i.putExtra("building", buildingName);
            i.putExtra("major", major);
            i.putExtra("startCal", startCal);
            i.putExtra("endCal", endCal);
            startActivity(i);
        }
        else {
            Intent i = new Intent(this, BuildingCurrentStudentsActivity.class);
            i.putExtra("user", email);
            i.putExtra("role", "manager");
            i.putExtra("building", buildingName);
            i.putExtra("capacity", curr_capacity);
            i.putExtra("max_capacity", max_capacity);
            startActivity(i);
        }
    }

    public void kickOutPressed(View view) {

        AlertDialog.Builder alert = new AlertDialog.Builder(StudentDetailsActivity.this);

        Database db = new Database();

        db.getStudent(studentEmail, new getUserInterface() {
            @Override
            public void getUser(User user) {
                if (user != null) {
                    String curLoc = user.getCurr_loc();
                    if (curLoc.equals("none") || curLoc.equals("")) {
                        alert.setMessage("You are not currently in a building");
                    } else {

                        alert.setMessage("Are you sure you want to kick out this student?");
                        alert.setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dbCheckout();
                                    }
                                });
                        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                    }

                    AlertDialog popUpInner = alert.create();
                    popUpInner.show();

                }
                else {
                    //TODO: toast or something?
                }
            }
        });
    }

    public String emailFormat() {
        String line = stud.getFirst_name() + " " + stud.getLast_name() + "'s Report\n";
        line += "Email: " + stud.getEmail() + "\n";
        line += "USC ID: " + stud.getUsc_ID() + "\n";
        line += "Major: " + stud.getMajor() + "\n";
        for (int i = 0; i < studentHistory.size(); ++i) {
            Record r = studentHistory.get(i);
            for (int j = 0; j < 3; ++j) {
                if (j == 0) {
                    line += "Building: " + r.getBuilding_name() + " | ";
                }
                else if (j == 1) {
                    if (r.getCheck_in() == null) {
                        line += "Check in: None | ";
                    }
                    else {
                        try {
                            DateFormat formatter = new SimpleDateFormat("dd MMM HH:mm ");
                            line += "Check in: " + formatter.format(r.getCheck_in()) + " | ";
                        } catch (Exception e) {
                            line += "Check in: error | ";
                            e.printStackTrace();
                        }
                    }
                }
                else  {
                    if (r.getCheck_out() == null) {
                        line += "Check out: None";
                    }
                    else {
                        try {
                            DateFormat formatter = new SimpleDateFormat("dd MMM HH:mm ");
                            line += "Check out: " + formatter.format(r.getCheck_in());
                        } catch (Exception e) {
                            line += "Check in: error";
                            e.printStackTrace();
                        }
                    }
                }
            }
            line += "\n";
        }
        return line;
    }

    /*** Sample email format:
     *
     * kevin imami's Report
     * Email: mix1@usc.edu
     * USC ID: 1111111111
     * Major: Computer Science
     * Building: Seeley G Mudd | Check in: 22 Apr 14:10  | Check out: 22 Apr 14:10
     * Building: Grace Ford Salvatori Hall | Check in: 22 Apr 14:24  | Check out: None
     *
     */

    public void sendEmail() {
        try {
            final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            emailIntent.setType("plain/text");
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{studentEmail});
            String subject = stud.getFirst_name() + " " + stud.getLast_name() + "'s Report";
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
            if (URI != null) {
                emailIntent.putExtra(Intent.EXTRA_STREAM, URI);
            }
            String emailContent = emailFormat();
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, emailContent);
            this.startActivity(Intent.createChooser(emailIntent, "Sending email..."));
        } catch (Throwable t) {
            Toast.makeText(this, "Request failed try again: "+ t.toString(), Toast.LENGTH_LONG).show();
        }
    }

//    public void reportPressed(View view) {
//        Log.d("Testing", "clicked report pressed from student details activity");
//        Intent intent = new Intent(this, EmailReportActivity.class);
//        intent.putExtra("studentemail", studentEmail);
//        intent.putExtra("studentname", name);
//        String emailContent = emailFormat();
//        intent.putExtra("emailcontent", emailContent);
//        startActivity(intent);
//    }

    public void dbCheckout() {
        db.checkOut(studentEmail, buildingName, new checkOutInterface() {
            @Override
            public void onCheckOut(boolean success) {
                if (success) {
                    db.getBuildingCurrentCapacity(buildingName, new getCurrentCapacityInterface() {
                        @Override
                        public void getCurrCapacity(int capacity) {
                            int new_capacity = capacity - 1;
                            curr_capacity = "" + (Integer.parseInt(curr_capacity) - 1);
                            db.updateCurrentCapacity(buildingName, new_capacity, new updateCurrentCapacityInterface() {
                                @Override
                                public void updateCurrCapacity(boolean status) {
                                    if (status) {
                                        Log.d("Look here", "update building is called");
                                        db.updateLocation(studentEmail, "kickout", new updateLocationInterface() {
                                            @Override
                                            public void onUpdateLocation(boolean status) {
                                                if (status) {
                                                    // TODO - lucas look into this
                                                    Log.d("Look here", "ok building is updated to kickout");
                                                    ((TextView) findViewById(R.id.label_for_cur_building)).setText("Student not in a building");
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

}
