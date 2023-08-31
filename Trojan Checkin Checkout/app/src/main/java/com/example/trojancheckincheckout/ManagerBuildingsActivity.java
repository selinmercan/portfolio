package com.example.trojancheckincheckout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;

import androidx.core.content.res.ResourcesCompat;
import android.graphics.Typeface;

public class ManagerBuildingsActivity extends AppCompatActivity {
    TableLayout tl;
    ArrayList<Building> buildingList;
    String user = "";
    Database db;
    boolean error = false;
    String errorMessage = "";

    DatabaseReference databaseReference;
        // DO NOT TOUCH THIS IF YOU ARE NOT IN THE DB TEAM
        // NOT FOR REGULAR DB QUERIES THIS IS ONLY FOR THE PERSISTENT LISTENER

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_buildings);

        TextView textView = findViewById(R.id.title);

        Typeface typeface = ResourcesCompat.getFont(
                this,
                R.font.poppins);
        textView.setTypeface(typeface);

        user = getIntent().getStringExtra("user");
        db = new Database();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        db.getBuildingList(new getBuildingsInterface() {
            @Override
            public void onGetBuildings(ArrayList<Building> buildings) {
                if (buildings == null) {
                    //TODO: toast error here
                }
                else {
                    buildingList = buildings;
                    displayData();
                }
            }
        });

        databaseReference.child("buildings").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("herehere", "change observed");
                ArrayList<Building> updated_list = new ArrayList<Building>();

                for (DataSnapshot building : snapshot.getChildren()) {
                    Building b = new Building();
                    Log.d("Testing", building.toString());
                    Log.d("Testing", building.child("curr_capacity").getValue().toString());
                    b.setCurr_capacity(Integer.parseInt(building.child("curr_capacity").getValue().toString()));
                    b.setMax_capacity(Integer.parseInt(building.child("max_capacity").getValue().toString()));
                    b.setName(building.child("name").getValue().toString());
                    updated_list.add(b);
                }

                buildingList = updated_list;
                displayData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Testing", error.toString());
            }
        });
    }

    public void displayData() {
        tl = (TableLayout) findViewById(R.id.managerBuildingTable);
        tl.setPadding(10, 0, 0, 0);


        if (tl.getChildCount() != 0) {
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
        col2.setText("Current");
        col2.setTextColor(Color.BLACK);
        tr_head.addView(col2);

        TextView col3 = new TextView(this);
        col3.setText("Max");
        col3.setTextColor(Color.BLACK);
        tr_head.addView(col3);

        TextView col4 = new TextView(this);
        col4.setText("More");
        col4.setTextColor(Color.BLACK);
        tr_head.addView(col4);

        tl.addView(tr_head, new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,                    //part4
                TableLayout.LayoutParams.MATCH_PARENT));

        tl.setColumnStretchable(0, true);
        tl.setColumnStretchable(1, true);
        tl.setColumnStretchable(2, true);
        tl.setColumnShrinkable(3, true);

        TableRow tr;
        Building b = new Building();
        for (int i = 0; i < buildingList.size(); ++i) {
            tr = new TableRow(this);
            b = buildingList.get(i);
            TextView cell;
            for (int j = 0; j < 3; ++j) {
                cell = new TextView(this);
                cell.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                cell.setMaxLines(10);

                if (j == 0) {
                    String concat = b.getName() + " ";
                    cell.setText(concat);
                }
                else if (j == 1) {
                    String concat = "" + b.getCurr_capacity() + " ";
                    cell.setText(concat);
                }
                else  {
                    String concat = "" + b.getMax_capacity() + " ";
                    cell.setText(concat);
                }
                tr.addView(cell);
            }
            ImageButton info = new ImageButton(this);
            info.setContentDescription("@string/info_button");
            info.setImageResource(R.drawable.info);
            Building finalB = b;
            info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    realOnClick(finalB.getName(), finalB.getCurr_capacity(), finalB.getMax_capacity());
                }
            });
            tr.addView(info);
            tl.addView(tr, new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,                    //part4
                    TableLayout.LayoutParams.MATCH_PARENT));
        }
    }

    public void realOnClick(String buildingName, int capacity, int max_capacity){
        Intent i = new Intent(this, BuildingInfoActivity.class);
        i.putExtra("building", buildingName);
        i.putExtra("user", user);
        i.putExtra("capacity", String.valueOf(capacity));
        i.putExtra("max_capacity", String.valueOf(max_capacity));
        startActivity(i);
    }

    public void goBack(View view){
        Intent i = new Intent(this, ManagerProfileActivity.class);
        i.putExtra("user", user);
        startActivity(i);
    }

    public void startImport(View view) {
        Intent i = new Intent();
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("*/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(i, "Select CSV File"), 100);
    }

    public void readCSV(Uri path) {

        // if its not a csv and not a txt file let the user know and return
        if (!path.getPath().endsWith(".csv") && !path.getPath().endsWith(".txt")) {
            AlertDialog.Builder alert = new AlertDialog.Builder(ManagerBuildingsActivity.this);
            alert.setMessage("Please select a .csv or .txt file");

            AlertDialog popUp = alert.create();
            popUp.show();
            return;
        }
        // else we read the file

        ArrayList<String[]> newBuildings = new ArrayList<>();

        try {
            //InputStream is = am.open(path);
            InputStream in = getContentResolver().openInputStream(path);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;

            while ((line = reader.readLine()) != null) {
                if (!(line.trim().equals(""))) {
                    String[] temp = line.split(",");
                    newBuildings.add(temp);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

            AlertDialog.Builder alert = new AlertDialog.Builder(ManagerBuildingsActivity.this);
            alert.setMessage("Error reading the file");
            AlertDialog popUp = alert.create();
            popUp.show();
            return;
        }

        if (newBuildings.isEmpty()) {
                AlertDialog.Builder alert = new AlertDialog.Builder(ManagerBuildingsActivity.this);
                alert.setMessage("The CSV file was empty");
                AlertDialog popUp = alert.create();
                popUp.show();
            return;
        }

        HashMap<String, Integer> buildingMap = new HashMap<>();
        for (Building b : buildingList) {
            buildingMap.put(b.getName(), b.getCurr_capacity());
        }

        // i collect everything that will be changed
        // validating as i collect
        // that way if there is an error
        // i dont make some changes in the file then fail
        HashSet<String> toDelete = new HashSet<>();
        HashMap<String, Integer> toAdd = new HashMap<String, Integer>();
        HashMap<String, Integer> toUpdate = new HashMap<String, Integer>();

        for (String[] temp : newBuildings) {

            String command = temp[0].trim();
            String bname = temp[1].trim();

            if (bname.equalsIgnoreCase("")) {
                System.out.println("I should do something there");
            }

            // if the command is to delete
            if (command.equalsIgnoreCase("del")) {

                // if we have too many/few arguments
                if (temp.length != 2) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(ManagerBuildingsActivity.this);
                    alert.setMessage("CSV format error: 'del' requires only a building name");
                    AlertDialog popUp = alert.create();
                    popUp.show();
                    return;
                }

                if (buildingMap.containsKey(bname)) {
                    if (buildingMap.get(bname) != 0) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(ManagerBuildingsActivity.this);
                        alert.setMessage("Cannot delete building with capacity > 0");
                        AlertDialog popUp = alert.create();
                        popUp.show();
                        return;
                    }
                    else {
                        toDelete.add(bname);
                    }
                }
                else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(ManagerBuildingsActivity.this);
                    alert.setMessage("Cannot delete building that does not exist");
                    AlertDialog popUp = alert.create();
                    popUp.show();
                    return;
                }
            }
            else if (command.equalsIgnoreCase("add")) {
                // check number of arguments
                if (temp.length != 3) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(ManagerBuildingsActivity.this);
                    alert.setMessage("CSV format error, 'add' requires a building name and capacity");
                    AlertDialog popUp = alert.create();
                    popUp.show();
                    return;
                }

                // if we dont have the building
                if (!buildingMap.containsKey(bname)) {
                    int capacity = 0;
                    try {
                        capacity = Integer.parseInt(temp[2].trim());
                    }
                    catch (Exception e) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(ManagerBuildingsActivity.this);
                        alert.setMessage("Capacity must be a number ");
                        AlertDialog popUp = alert.create();
                        popUp.show();
                        return;
                    }

                    if (capacity >= 0) {
                        toAdd.put(bname, capacity);
                    }
                    else {
                        AlertDialog.Builder alert = new AlertDialog.Builder(ManagerBuildingsActivity.this);
                        alert.setMessage("Capacity must be >= 0");
                        AlertDialog popUp = alert.create();
                        popUp.show();
                        return;
                    }
                }
                else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(ManagerBuildingsActivity.this);
                    alert.setMessage("Cannot add building that already exists, use 'update' to change capacity");
                    AlertDialog popUp = alert.create();
                    popUp.show();
                    return;
                }
            }
            else if (command.equalsIgnoreCase("update")) {
                // check number of arguments
                if (temp.length != 3) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(ManagerBuildingsActivity.this);
                    alert.setMessage("CSV format error, 'update' requires a building name and capacity");
                    AlertDialog popUp = alert.create();
                    popUp.show();
                    return;
                }

                // if we have the building
                if (buildingMap.containsKey(bname)) {

                    int capacity = 0;
                    try {
                        capacity = Integer.parseInt(temp[2].trim());
                    }
                    catch (Exception e) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(ManagerBuildingsActivity.this);
                        alert.setMessage("Capacity must be a number ");
                        AlertDialog popUp = alert.create();
                        popUp.show();
                        return;
                    }

                    if (capacity >= buildingMap.get(bname)) {
                        toUpdate.put(bname, capacity);
                    }
                    else {
                        AlertDialog.Builder alert = new AlertDialog.Builder(ManagerBuildingsActivity.this);
                        alert.setMessage("Capacity must be >= current building capacity");
                        AlertDialog popUp = alert.create();
                        popUp.show();
                        return;
                    }
                }
                else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(ManagerBuildingsActivity.this);
                    alert.setMessage("Cannot update building that does not exists, try 'add'");
                    AlertDialog popUp = alert.create();
                    popUp.show();
                    return;
                }
            }
        }


        for (String name : toDelete) {
            db.deleteBuilding(name, new deleteBuildingInterface() {
                @Override
                public void removeBuilding(boolean status) {

                }
            });
        }

        for (String name : toAdd.keySet()) {
            db.addBuilding(name, toAdd.get(name), new addbuildingInterface() {
                @Override
                public void addBuilding(boolean status) {

                }
            });
        }

        for (String name : toUpdate.keySet()) {

            db.updateMaxCapacity(name, toUpdate.get(name), new updateMaxCapacityInterface() {
                @Override
                public void updateMaxCapacity(boolean status) {

                }
            });
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                // Get the url of the image from data
                Uri fileURI = data.getData();

                if (fileURI != null) {
                    readCSV(fileURI);
                }
            }

        }

    }

    public ArrayList<String[]> fixBuildings(ArrayList<String[]> newBuildings) {
        ArrayList<String[]> fixed = new ArrayList<>();

        for (int i = 0; i < newBuildings.size(); ++i) {

            if (Integer.parseInt(newBuildings.get(i)[1]) < 0) {
                error = true;
                errorMessage = "Cannot have negative maximum capacity";
                return null;
            }
            // {name, max cap, cur cap, already exists}
            String[] temp = {newBuildings.get(i)[0].trim(), newBuildings.get(i)[1].trim(), "0", "0"};
            fixed.add(temp);

            String curName = newBuildings.get(i)[0];
            for (int j = 0; j < buildingList.size(); ++j) {

                if (buildingList.get(j).buildingName.equalsIgnoreCase(curName)) {
                    fixed.get(i)[2] = "" + buildingList.get(j).getCurr_capacity();
                    fixed.get(i)[3] = "1";
                    //current capacity of this building is less than the new max capacity
                    if (buildingList.get(j).getCurr_capacity() > Integer.parseInt(newBuildings.get(i)[1].trim())){
                        error = true;
                        errorMessage = "Cannot change the maximum capacity to less than the current capacity of a building";
                        return null;
                    }
                }
            }
        }

        return fixed;
    }


    public void addBuilding(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Building");

        TableLayout tb = new TableLayout(this);

        TableRow tr1 = new TableRow(this);
        TableRow tr2 = new TableRow(this);

        final EditText bName = new EditText(this);

        bName.setInputType(InputType.TYPE_CLASS_TEXT);
        bName.setHint("Building Name");


        final EditText bCap = new EditText(this);

        bCap.setInputType(InputType.TYPE_CLASS_TEXT);
        bCap.setHint("Capacity");

        tr1.addView(bName);
        tb.addView(tr1);

        tr2.addView(bCap);
        tb.addView(tr2);

        tb.setStretchAllColumns(true);

        builder.setView(tb);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                handleAddAlert(bName.getText().toString(), bCap.getText().toString());
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

    public void handleAddAlert(String name, String capacity) {
        int cap = 0;
        try {
            cap = Integer.parseInt(capacity);
        }
        catch (Exception e){
           e.printStackTrace();
            AlertDialog.Builder alert = new AlertDialog.Builder(ManagerBuildingsActivity.this);
            alert.setMessage("Please enter a valid capacity as a number >= 0");
            AlertDialog popUp = alert.create();
            popUp.show();
            return;
        }

        if (cap < 0) {
            AlertDialog.Builder alert = new AlertDialog.Builder(ManagerBuildingsActivity.this);
            alert.setMessage("Please enter a valid capacity as a number >= 0");
            AlertDialog popUp = alert.create();
            popUp.show();
            return;
        }

        Database db = new Database();

        int finalCap = cap;
        db.getBuildingList(new getBuildingsInterface() {
            @Override
            public void onGetBuildings(ArrayList<Building> buildings) {
                boolean exists = false;
                for (int i = 0; i < buildings.size(); i++) {
                    if (buildings.get(i).buildingName.equals(name)) {
                        exists = true;
                        break;
                    }
                }

                if (exists) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(ManagerBuildingsActivity.this);
                    alert.setMessage("Building with that name already exists");
                    AlertDialog popUp = alert.create();
                    popUp.show();
                }
                else {
                    db.addBuilding(name, finalCap, new addbuildingInterface() {
                        @Override
                        public void addBuilding(boolean status) {
                            //TODO: should probaby do something if this fails
                        }
                    });
                }
            }
        });

    }

    public void deleteBuilding(View view) {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Building");

        Spinner spinner = new Spinner(this);

        Database db = new Database();
        db.getBuildingList(new getBuildingsInterface() {
            @Override
            public void onGetBuildings(ArrayList<Building> buildings) {
                String[] options = new String[buildings.size()];
                for (int i = 0; i < buildings.size(); i++) {
                    options[i] = buildings.get(i).buildingName;
                }
                ArrayAdapter<String> building_adapter = new ArrayAdapter<>(ManagerBuildingsActivity.this, android.R.layout.simple_spinner_dropdown_item, options);
                spinner.setAdapter(building_adapter);
            }
        });

        builder.setView(spinner);

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TextView textView = (TextView)spinner.getSelectedView();
                String result = textView.getText().toString();

                db.getBuildingCurrentCapacity(result, new getCurrentCapacityInterface() {
                    @Override
                    public void getCurrCapacity(int capacity) {
                        if (capacity <= 0) {
                            db.deleteBuilding(result, new deleteBuildingInterface() {
                                @Override
                                public void removeBuilding(boolean status) {

                                }
                            });
                        }
                        else {
                            AlertDialog.Builder alert = new AlertDialog.Builder(ManagerBuildingsActivity.this);
                            alert.setMessage("Cannot delete building with capacity > 0");
                            AlertDialog popUp = alert.create();
                            popUp.show();
                        }
                    }
                });

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
}