package com.example.trojancheckincheckout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ManagerImportActivity extends AppCompatActivity {

    Uri csvUri; //global variable
    View root;
    Button uploadFileButton;
    String email = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_import);

        Log.d("Testing", "in the on create of the import activity");

        Intent intent = getIntent();
        email = intent.getStringExtra("user");

        uploadFileButton = (Button) findViewById(R.id.upload);

        TextView textView = findViewById(R.id.question);

        Typeface typeface = ResourcesCompat.getFont(
                this,
                R.font.poppins);
        textView.setTypeface(typeface);

        uploadFileButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    selectCSV();
                }
                else{ //ask for permission
                    ActivityCompat.requestPermissions( ManagerImportActivity.this,
                            new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
                }
            }
        });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        //acknowledgment by user
        if(requestCode==9 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            selectCSV();
        }
        else{
            Toast.makeText(getApplicationContext(), "Please give permission to access files", Toast.LENGTH_SHORT).show();
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        //check whether user has selected file or not
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 86 && resultCode == RESULT_OK && data != null){
            csvUri=data.getData();
            //parse, pass into another function, etc (I know you already wrote code for this)
            Log.d("Testing", csvUri.getPath());
            List<Building> buildings = new ArrayList<>();

            System.out.println(getApplicationContext().getDataDir().getAbsolutePath());
            File extStore = Environment.getExternalStorageDirectory();
            System.out.println(extStore.getAbsolutePath());
//            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
//                String line;
//                while ((line = br.readLine()) != null) {
//                    System.out.println("in here");
//                    String[] values = line.split(",");
//                    Building b = new Building(values[0], Integer.parseInt(values[1]), Integer.parseInt(values[2]));
//                    buildings.add(b);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

            System.out.println(buildings);

        }
        else{
            Toast.makeText(getApplicationContext(), "Please select a file", Toast.LENGTH_SHORT).show();
        }
    }
    private void selectCSV(){
        //offer user to select a file using file manager
        Intent intent = new Intent();
        intent.setType("text/*");
        intent.setAction(Intent.ACTION_GET_CONTENT); //to fetch files
        startActivityForResult(intent, /*request code*/ 86);
    }

    public void goBack(View view){
        Intent i = new Intent(this, ManagerProfileActivity.class);
        i.putExtra("user", email);
        startActivity(i);

    }


}