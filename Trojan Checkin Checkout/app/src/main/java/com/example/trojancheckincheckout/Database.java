package com.example.trojancheckincheckout;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseReference.CompletionListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;

public class Database {

    private final DatabaseReference database;

    public Database() {
        this.database = FirebaseDatabase.getInstance().getReference();
    }

    /**
     * Adds a user to database, assuming user does not already exist (check should have already been done)
     * @param email      email address of new user (must be unique)
     * @param first_name first name of new user
     * @param last_name  last name of new user
     * @param passsword  plain-text password of new user
     * @param role the assigned role of new user - "manager" or "student"
     * @param major student entered major
     * @param usc_ID student entered usc_ID
     * @return add status - True = successful add, False = unsuccessful add
     */
    public void addUser(String email, String first_name, String last_name, String passsword, String role, String major, long usc_ID, addUserInterface listener) {
        User u = new User(email, first_name, last_name, passsword, role, major, usc_ID, "none"); //all users start with no location
        String userID = User.getUserId(email);

        database.child("users").child(userID).setValue(u, new CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                listener.addUser(error == null);
            }
        });
    }

    /**
     * sets "status" to "inactivate" for user in DB - we want to retain their info so we can show them in building histories
     * @param email we delete users by their trimmed email (w/out @usc.edu)
     * @return delete status - True = successful delete, False = unsuccessful delete
     */
    public void deleteUser(String email, deleteUserInterface listener) {
        String userID = User.getUserId(email);

        database.child("users").child(userID).child("status").setValue("inactive", new CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                listener.deleteUser(error == null);
            }
        });
    }


    /**
     * Removes child "status" for user in DB - we want to reset as if it was never deleted
     * @param email we delete users by their trimmed email (w/out @usc.edu)
     * @return delete status - True = successful reactivate, False = unsuccessful reactivate
     */
    public void reactivateUser(String email, reactivateUserInterface listener) {
        String userID = User.getUserId(email);

        database.child("users").child(userID).child("status").removeValue(new CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                listener.reactivateUser(error == null);
            }
        });

    }

    /**
     * Update user with specified fields
     * @param email the user we're looking for
     * @param password plain-text password we're updating the value to
     * @return update status - True = successful password change, False = failed to change password
     */
    public void updatePassword(String email, String password, updatePasswordInterface listener) {
        String userID = User.getUserId(email);
        String hashed_pw = User.hash(password);
        Log.d("Testing", "in updated password in db");
        database.child("users").child(userID).child("password").setValue(hashed_pw).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                listener.updatePasswordStatus(task.isSuccessful());
            }
        });
    }

    /**
     * Returns whether or not given email exists in database
     * @param email the email to search for in the db
     * @return email existence - True = exists, False = dne
     **/
    public void checkEmail(String email, final checkEmailInterface listener) {

        String userID = User.getUserId(email);

        // this is the correct way to check if a key exists in the DB (emails are keys)
        database.child("users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listener.onCheckEmail(snapshot.exists());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("DB", error.toString());
            }
        });

    }

    public void checkID(String id, final checkIdInterface listener) {
        long l_id = Long.parseLong(id);
        Query query = database.child("users").orderByChild("usc_ID").equalTo(l_id);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listener.onCheckId(snapshot.exists());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // USAGE: (delete this)
//        String id = "5090748694";
//        db.checkID(id, new checkIdInterface() {
//            @Override
//            public void onCheckId(boolean exists) {
//                if (exists) System.out.println("found user");
//                else System.out.println("could not find");
//            }
//        });
    }

    public void checkAccountStatus(String email, final checkAccountStatusInterface listener) {
        String userID = User.getUserId(email);

        database.child("users").child(userID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                listener.checkAccountStatus(task.getResult().child("status").exists());
            }
        });
    }

    /**
     * Gets role of user specified by email
     * @param email user to search for
     * @return user role - "manager" or "student"
     */

    public void getUserRole(String email, userRoleInterface listener) {
        String userID = User.getUserId(email);

        database.child("users").child(userID).child("role").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) listener.getUserRole(task.getResult().getValue().toString());
                else listener.getUserRole(null);
            }
        });
    }

    /**
     * Hashes password and checks if match exists for the specified user - this is only called after checkEmail()
     * @param email denotes the user we're looking for
     * @param password the plain-text password before being hashed
     * @return if password matches user account - True = match found, False = no match
     */
    public void verifyPassword(String email, String password, verifyPasswordInterface listener) {
        //hashing the plain-text password
        String hashed_pw;
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(password.getBytes());
            byte[] messageDigest = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) hexString.append(Integer.toHexString(0xFF & b));
            hashed_pw = hexString.toString();

            Query query = database.child("users").child(User.getUserId(email));
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        if (child.getKey().equals("password")) {
                            listener.verifyPassword(child.getValue().toString().equals(hashed_pw));
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("DB", error.toString());
                }
            });

        } catch (Exception e) {
            Log.d("DB", "error hashing password");
        }

    }

    /**
     * Returns details (name, email, major, ID, etc) of specified student
     *      note: this function assumes user does exist in db
     * @param email email of user we're looking for
     * @return Student object with parameters filled in
     */
    public void getStudent(String email, getUserInterface listener) {
        String userID = User.getUserId(email);

        database.child("users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User student = new User();
                student.setFirst_name(snapshot.child("first_name").getValue().toString());
                student.setLast_name(snapshot.child("last_name").getValue().toString());
                student.setUsc_ID(Long.parseLong(snapshot.child("usc_ID").getValue().toString()));
                student.setMajor(snapshot.child("major").getValue().toString());
                student.setEmail(email);
                student.setCurr_loc(snapshot.child("curr_loc").getValue().toString());
                student.setPassword(null); // we don't want to ever return the true password

                if (snapshot.child("status").exists()) {
                    // this user was deleted
                    // TODO - should we still return their details or send back null?
                        // TODO - i'm thinking we can still send their data but also send back a flag and then display a message of like "this user has been deleted"
                    listener.getUser(null);
                }

                listener.getUser(student);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("DB", error.toString());
            }
        });
    }

    /**
     * Returns bitmap to set ImageView to
     * @param email the user we're looking for
     * @return bitmap object - null if no picture stored (use default picture)
     */
    public void getStudentPicture(String email, getPictureInterface listener) {
        String userID = User.getUserId(email);
        database.child("users").child(userID).child("image").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String encoded_rep = snapshot.getValue().toString();
                    byte[] decoded_string = Base64.decode(encoded_rep, Base64.DEFAULT);
                    Bitmap decoded_bitmap = BitmapFactory.decodeByteArray(decoded_string, 0, decoded_string.length);
                    listener.getPicture(decoded_bitmap);
                } else {
                    listener.getPicture(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { Log.d("DB", error.toString()); }
        });
    }

    /**
     * Updates student picture
     * @param email indicates which user we're looking for
     * @param img the ImageView which contains the image we're updating for the user
     * @return update status - True = success, False = failure
     */
    public void updateStudentPicture(String email, ImageView img, updatePictureInterface listener) {
        // getting the encoding from the ImageView and saving as bitmap
        Drawable drawable = img.getDrawable();
        Bitmap bitmap;
        if (drawable instanceof BitmapDrawable) {
            bitmap = ((BitmapDrawable) drawable).getBitmap();
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        }

        //getting base64 encoding from bitmap
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();
        byte[] encoded_image = Base64.encode(bytes, Base64.DEFAULT);
        String string_rep = new String(encoded_image, StandardCharsets.UTF_8); // can ony save as string rep in firebase

        String userID = User.getUserId(email);

        database.child("users").child(userID).child("image").setValue(string_rep).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                listener.updatePicture(task.isSuccessful());
            }
        });
    }

    // BUILDING QUERIES //

    /**
     * Adding a building to the db from the UI
     * @param name         name of the new building
     * @param max_capacity max capacity of the new building
     * @return add status - True = successful add, False = failed to add
     */
    public void addBuilding(String name, int max_capacity, addbuildingInterface listener) {
        Building building = new Building(name, max_capacity, 0);

        database.child("buildings").child(name).setValue(building, new CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                listener.addBuilding(error == null);
            }
        });

    }

    /**
     * Deleting a building from the UI
     * @param name name of the building being deleted
     * @return delete status - True = successful delete, False = failed to delete
     */
    public void deleteBuilding(String name, deleteBuildingInterface listener) {
        // TODO - what about user records that have this specific building?
        database.child("buildings").child(name).removeValue(new CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                listener.removeBuilding(error == null);
            }
        });
    }

    /**
     * Update stored building list from .csv file of buildings
     * @param buildings map of buildings object read in from csv (name --> building obj)
     * @return update status - True = successful update, False = failed to update
     */
    public void updateTrackedBuildings(Hashtable<String, Building> buildings, updateBuildingListInterface listener) {
        // TODO - what about user records that have a building being removed?
        database.child("buildings").removeValue(new CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error == null) {
                    database.child("buildings").setValue(buildings).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) listener.updateBuildingList(true);
                            else listener.updateBuildingList(false);
                        }
                    });
                } else { //if we couldn't delete all the buildings currently in db
                    listener.updateBuildingList(false);
                }
            }
        });
    }

    /**
     * Gets building QR code from DB
     * @param building_name the name of the building we're gettng
     * @return the Bitmap obj (or null)
     */
    public void getBuildingQRCode(String building_name, getQRCodeInterface listener) {
        database.child("buildings").child(building_name).child("qr_code").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().getValue() == null) {
                        listener.getBuildingQRCode(null);
                    }
                    else {
                        String encoded_rep = task.getResult().getValue().toString();
                        byte[] decoded_string = Base64.decode(encoded_rep, Base64.DEFAULT);
                        Bitmap decoded_bitmap = BitmapFactory.decodeByteArray(decoded_string, 0, decoded_string.length);
                        listener.getBuildingQRCode(decoded_bitmap);
                    }
                } else {
                    listener.getBuildingQRCode(null);
                }
            }
        });
    }

    /**
     * Updates student picture
     * @param building_name indicates which user we're looking for
     * @param bitmap the ImageView which contains the image we're updating for the user
     * @return update status - True = success, False = failure
     */
    public void updateBuildingQRCode(String building_name, Bitmap bitmap, updateQRCodeInterface listener) {
        //getting base64 encoding from bitmap
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();
        byte[] encoded_image = Base64.encode(bytes, Base64.DEFAULT);
        String string_rep = new String(encoded_image, StandardCharsets.UTF_8); // can ony save as string rep in firebase

        database.child("buildings").child(building_name).child("qr_code").setValue(string_rep).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                listener.updateBuildingQRCode(task.isSuccessful());
            }
        });
    }

    /**
     * Get current building capacity
     * @param building_name name of the building to retrieve
     * @return the building capacity
     */
    public void getBuildingCurrentCapacity(String building_name, getCurrentCapacityInterface listener) {
        database.child("buildings").child(building_name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // TODO - this can be written much neater
                for (DataSnapshot child : snapshot.getChildren()) {
                    if (child.getKey().equals("curr_capacity"))
                        listener.getCurrCapacity(Integer.parseInt(child.getValue().toString()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("DB", error.toString());
            }
        });
    }

    /**
     * Get maximum building capacity
     * @param building_name name of the building to retrieve
     * @return the max capacity of the requested building
     */
    public void getBuildingMaxCapacity(String building_name, getMaxCapacityInterface listener) {
        database.child("buildings").child(building_name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    if (child.getKey().equals("max_capacity"))
                        listener.getMaxCapacity(Integer.parseInt(child.getValue().toString()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("DB", error.toString());
            }
        });
    }

    /**
     * Update the current capacity of a given building
     * @param building_name name of the building to update
     * @param update        the new value for the building
     * @return update status - True = successful update, False = failed to update
     */
    public void updateCurrentCapacity(String building_name, int update, updateCurrentCapacityInterface listener) {
        Log.d("Testing", "updating current capacity");
        Log.d("Testing", building_name);
        database.child("buildings").child(building_name).child("curr_capacity").setValue(update).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                listener.updateCurrCapacity(task.isSuccessful());
            }
        });
    }

    public void updateMaxCapacity(String building_name, int update, updateMaxCapacityInterface listener) {
        Log.d("Testing", "updating current capacity");
        Log.d("Testing", building_name);
        database.child("buildings").child(building_name).child("max_capacity").setValue(update).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                listener.updateMaxCapacity(task.isSuccessful());
            }
        });
    }

    /**
     * Gets list of all buildings in the system
     * @return a Array list of building objects
     */
    public void getBuildingList(getBuildingsInterface listener) {
        database.child("buildings").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot buildings = task.getResult();
                ArrayList<Building> buildingList = new ArrayList<Building>();

                for (DataSnapshot building : buildings.getChildren()) {
                    Building b = new Building();
                    Log.d("Testing", building.toString());
                    Log.d("Testing", building.child("curr_capacity").getValue().toString());
                    b.setCurr_capacity(Integer.parseInt(building.child("curr_capacity").getValue().toString()));
                    b.setMax_capacity(Integer.parseInt(building.child("max_capacity").getValue().toString()));
                    b.setName(building.child("name").getValue().toString());
                    buildingList.add(b);
                }

                listener.onGetBuildings(buildingList);
//                Log.d("Testing", task.getResult().getValue().toString());
            }
        });
    }

    // RECORD QUERIES //

    /**
     * Queries history of specific student
     * @param userEmail student identifier
     * @return ArrayList of record objects
     */
    public void getStudentHistory(String userEmail, getRecordsByStudentInterface listener) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        Query query = database.child("records").orderByChild("user_email").equalTo(userEmail);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange (@NonNull DataSnapshot snapshot){
                ArrayList<Record> records = new ArrayList<Record>();
                DateFormat input = new SimpleDateFormat("SSS");
                DateFormat output = new SimpleDateFormat("dd MMM HH:mm");
                for (DataSnapshot r : snapshot.getChildren()){
                    String building_name = r.child("building_name").getValue().toString();
                    String check_in_string = r.child("check_in").child("time").getValue().toString();
                    Date check_in_date = new Date(Long.parseLong(check_in_string));

                    String check_out_string = "None";
                    Date check_out_date = null;
                    if (r.child("check_out").exists()) {
                        check_out_string = r.child("check_out").child("time").getValue().toString();
                        check_out_date = new Date(Long.parseLong(check_out_string));
                    }
                    
                    records.add(new Record(userEmail, building_name, check_in_date, check_out_date));
                }
                listener.onGetRecordsByStudent(records);
            }

            @Override
            public void onCancelled (@NonNull DatabaseError error){
                Log.d("DB", error.toString());
            }
        });
    }

    /**
     * Returns list of records associated with the building
     * @param building_name name of specified building
     * @return the list of Record objects
     */
    public void getBuildingHistory(String building_name, getRecordsByBuildingInterface listener) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        Query query = database.child("records").orderByChild("building_name").equalTo(building_name);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange (@NonNull DataSnapshot snapshot){

                ArrayList<Record> records = new ArrayList<Record>();
                for (DataSnapshot r : snapshot.getChildren()){
                    String check_in_string = r.child("check_in").child("time").getValue().toString();
                    Log.d("Testing", check_in_string);
                    Date check_in_date = null;
                    try {
//                        check_in_date = formatter.parse(check_in_string);
                        check_in_date = new Date(Long.parseLong(check_in_string));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Date check_out_date = null;
                    if(r.child("check_out").exists()) {
                        try {
//                            check_out_date = formatter.parse(check_out_string.toString());
                            check_out_date = new Date(Long.parseLong(r.child("check_out").child("time").getValue().toString()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        check_out_date = null;
                    }

                    String email = r.child("user_email").getValue().toString();
                    records.add(new Record(email, building_name, check_in_date, check_out_date));
                }
                listener.onGetRecordsByBuilding(records);
            }

            @Override
            public void onCancelled (@NonNull DatabaseError error){
                Log.d("DB", error.toString());
            }
        });

    }

    /**
     * Gets a user's status (in or out of a building)
     * @param userEmail specified user we're looking for
     * @return user status - True = user is in a building, False = user is not in a building
     */
    public void getStatus(String userEmail, getStudentStatusInterface listener) {
       final boolean[] finished = {false};
        Query query = database.child("records").orderByChild("user_email").equalTo(userEmail);
        query.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot r : snapshot.getChildren()){
                    if(r.child("check_out").getValue() == null) {
                        listener.getStudentStatus(true);
                        finished[0] = true;
                    }
                }
                if(!finished[0]) {
                    //if we make it out here, student is in a building
                    listener.getStudentStatus(false);
                }
            }
            @Override
            public void onCancelled (@NonNull DatabaseError error) {
                Log.d("DB", error.toString());
            }
        });
    }

    /**
     * Creates a new record object for the given user in specified building
     *      note: this does not check error handling
     * @param userEmail specified user
     * @param building_name specified building
     * @return check in status - True = successful check in, False = failed to check in
     */
    public void checkIn(String userEmail, String building_name, checkInInterface listener) {
        Log.d("aaa", "database - check in ");
        Date check_in = new Date();
        Record r = new Record(userEmail, building_name, check_in, null);
        Log.d("Testing", "creating check in record");
        String key = database.child("records").push().getKey();
        database.child("records").child(key).setValue(r).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                listener.onCheckIn(task.isSuccessful());
            }
        });
    }

    /**
     * Checks user out of building (updates record object)
     *      note: this does not check error handling
     * @param userEmail specifed user
     * @param building_name specified building
     * @return check out status - True = successful check out, False = failed to check out user
     */
    public void checkOut(String userEmail, String building_name, checkOutInterface listener) {
        Log.d("aaa", "database - check out");
        // TODO - update this to make sure this is the record from specified building name
        final boolean[] finished = {false};
        Date check_out = new Date();
        Query query = database.child("records").orderByChild("user_email").equalTo(userEmail);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot r : snapshot.getChildren()){
                    if(r.child("check_out").getValue() == null) {
                        r.child("check_out").getRef().setValue(check_out);
                        listener.onCheckOut(true);
                        finished[0] = true;
                    }
                }
                if(!finished[0]) {
                    //if we get here, no record found without a checkout time
                    listener.onCheckOut(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    /**
     * Updates user location to specified building
     *      note: always called in conjunction with check-in and check-out
     * @param userEmail user email
     * @param building_name building name
     * @return status indicating success of update - True = successful update, False = failure to update
     */
    public void updateLocation(String userEmail, String building_name, updateLocationInterface listener) {
        String userID = User.getUserId(userEmail);
        Log.d("Testing", "updating user location");
        database.child("users").child(userID).child("curr_loc").setValue(building_name).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                listener.onUpdateLocation(task.isSuccessful());
            }
        });
    }



    // new functions for A5

    /**
     * gets a list of all records where a user is currently checked in
     * @param listener gets the list of records
     */
    public void getAllCurrentlyCheckedInRecords(getAllCurrentlyCheckedInRecordsInterface listener){ //this one is so the manager can see the list
        database.child("records").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot records = task.getResult();
                ArrayList<Record> recordList = new ArrayList<Record>();
                for (DataSnapshot r : records.getChildren()) {
                    if (!r.child("check_out").exists()) {
                        String userEmail = r.child("user_email").getValue().toString();
                        String building_name = r.child("building_name").getValue().toString();
                        String check_in_string = r.child("check_in").child("time").getValue().toString();
                        Date check_in_date = new Date(Long.parseLong(check_in_string));
                        recordList.add(new Record(userEmail, building_name, check_in_date, null));
                    }
                }
                listener.getAllCurrentlyCheckedInRecords(recordList);
            }
        });
    }

    /**
     * checks out all users that are currently checked in
     * @param listener gets a boolean to see if action was successful
     */
    public void automaticCheckOut(automaticCheckoutInterface listener){
        Date currDate = new Date();
        database.child("records").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot records = task.getResult();
                ArrayList<Record> recordList = new ArrayList<Record>();
                for (DataSnapshot r : records.getChildren()) {
                    if (!r.child("check_out").exists()) {
                        r.child("check_out").getRef().setValue(currDate);
                    }
                }
                listener.automaticCheckOut(task.isSuccessful());
            }
        });
    }

    /**
     * gets all students that are currently checked into a building
     * @param building_name the building being queried
     * @param listener an array list of records
     */
    public void getCurrentStudentsInBuilding(String building_name, getCurrentStudentsInBuildingInterface listener){
        Query query = database.child("records").orderByChild("building_name").equalTo(building_name);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange (@NonNull DataSnapshot snapshot){
                ArrayList<Record> records = new ArrayList<Record>();
                for (DataSnapshot r : snapshot.getChildren()) {
                    if (!r.child("check_out").exists()) {
                        String check_in_string = r.child("check_in").child("time").getValue().toString();
                        Date check_in_date = null;
                        try {
                            check_in_date = new Date(Long.parseLong(check_in_string));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Date check_out_date = null;

                        String email = r.child("user_email").getValue().toString();
                        records.add(new Record(email, building_name, check_in_date, null));
                    }
                }
                listener.getCurrentStudentsInBuilding(records);
            }

            @Override
            public void onCancelled (@NonNull DatabaseError error){
                Log.d("DB", error.toString());
            }
        });
    }

    /**
     * gets a list of all students active and inactive
     * @param listener gets an ArrayList of User objects (only students)
     */
    public void getAllStudents(getAllStudentsInterface listener){ //used for searching feature
        database.child("users").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
        @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot students = task.getResult();
                ArrayList<User> studentList = new ArrayList<User>();

                for (DataSnapshot student : students.getChildren()) {
                    if (student.child("role").getValue().toString().equals("student")) {
                        User s = new User();
                        s.setEmail(student.child("email").getValue().toString());
                        s.setCurr_loc(student.child("curr_loc").getValue().toString());
                        s.setFirst_name(student.child("first_name").getValue().toString());
                        s.setLast_name(student.child("last_name").getValue().toString());
                        s.setMajor(student.child("major").getValue().toString());
                        s.setPassword(null);
                        //s.setProfilePic();
                        s.setRole("student");
                        s.setUsc_ID(Long.parseLong(student.child("usc_ID").getValue().toString()));
                        studentList.add(s);
                    }
                }
                listener.getAllStudents(studentList);
            }
        });
    }

    /**
     * when a user "deletes" their account, their account becomes inactive but remains in the database
     * @param email the user's email
     * @param listener gets a boolean to see if action was successful
     */
    public void setUserAccountToInactive(String email, setUserAccountToInactiveInterface listener){
        database.child("users").child(email).child("active_status").setValue("inactive").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                listener.setStudentAccountToInactive(task.isSuccessful());
            }
        });
    }


    /**
     *  return everything that matches given fields - note that for now this is static
     *      later you will have to modify it to handle realtime changes
     * @param building_name
     * @param major
     * @param date
     * @param time
     */
    public void search(String building_name, String major, Date date, String time) {
        // if anything is not set ("none", null, "") don't filter by that input (i.e return all if none are set)

        database.child("records").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<DataSnapshot> record_snapshots = new ArrayList<>(); // valid snapshots after filtering from records
                if (!building_name.equals("none")) { // we are filtering by building
                    for (DataSnapshot record : snapshot.getChildren()) {
                        if (record.child("building_name").getValue().toString().equals(building_name)) {
                            record_snapshots.add(record);
                        }
                    }
                } else { // we are not filtering by building
                    for (DataSnapshot record : snapshot.getChildren()) {
                        record_snapshots.add(record);
                    }
                }
            }

            public void onCancelled(@NonNull DatabaseError error) { }
        });

        database.child("records").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<DataSnapshot> record_snapshots = new ArrayList<>(); // valid snapshots after filtering from records
                if (!building_name.equals("none")) { // we are filtering by building
                    for (DataSnapshot record : snapshot.getChildren()) {
                        if (record.child("building_name").getValue().toString().equals(building_name)) {
                            record_snapshots.add(record);
                        }
                    }
                } else { // we are not filtering by building
                    for (DataSnapshot record : snapshot.getChildren()) {
                        record_snapshots.add(record);
                    }
                }

                // want to make sure we grab all records
                System.out.println("Grabbed all records: " + record_snapshots.size());

                ArrayList<DataSnapshot> user_snapshots = new ArrayList<>(); // valid snapshot afte filtering from users
                ArrayList<DataSnapshot> valid_records = new ArrayList<>(); // we have all matching records to non user filters, this is the actual full matching record list
                for (DataSnapshot record : record_snapshots) {
                     String userEmail = record.child("user_email").getValue().toString();
                     String userID = User.getUserId(userEmail);
                     System.out.println("accessing student with id: " + userID);
                     database.child("users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                         @Override
                         public void onDataChange(@NonNull DataSnapshot snapshot) {
                             if (!snapshot.child("status").exists()) { // we only want active users (only deleted users have "status" flag set to inactive
                                 if (snapshot.child("major").getValue().toString().equals(major)) {
                                     System.out.println("found matching records");
                                     user_snapshots.add(snapshot);
                                     valid_records.add(record);
                                 } else {
                                     System.out.println("no matching record found");
                                 }
                             }
                         }

                         @Override
                         public void onCancelled(@NonNull DatabaseError error) {

                         }
                     });
                }

                // sleeping to make sure we can correctly filter all the valid user snapshots
                    // remember to use loading animation for search results page
                try {
                    Thread.sleep(10000);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // by here we have all the valid snapshots, now just build the results
                ArrayList<Record> records = new ArrayList<>();
                HashMap<String, User> users = new HashMap<>();

                for (DataSnapshot record : valid_records) {
                    String check_in_string = record.child("check_in").child("time").getValue().toString();
                    Date check_in = new Date(Long.parseLong(check_in_string));

                    Date check_out = null;
                    if (record.child("check_out").exists()) {
                        String check_out_string = record.child("check_out").child("time").getValue().toString();
                        check_out = new Date(Long.parseLong(check_out_string));
                    }

                    Record r = new Record();
                    r.building_name = record.child("building_name").getValue().toString();
                    r.user_email = record.child("user_email").getValue().toString();
                    r.check_in = check_in;
                    r.check_out = check_out;

                    records.add(r);
                }

                for (DataSnapshot user : user_snapshots) {
                    User u = new User();
                    u.email = user.child("email").getValue().toString();
                    u.first_name = user.child("first_name").getValue().toString();
                    u.last_name = user.child("last_name").getValue().toString();
                    u.password = null; // never want to actually return password
                    u.role = user.child("role").getValue().toString(); // this will always be student
                    u.major = user.child("major").getValue().toString();
                    u.usc_ID = Integer.parseInt(user.child("usc_ID").getValue().toString());
                    u.curr_loc = user.child("curr_loc").getValue().toString();
                    users.put(user.child("email").getValue().toString(), u);
                }

                // testing with print statements / access semantics
                System.out.println("everything should be done by now");
                for (Record r : records) {
                    System.out.println(users.get(r.user_email).first_name + " was at " + r.building_name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void aaa(String building_name, String major) {
        database.child("records").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<DataSnapshot> record_snapshots = new ArrayList<>(); // valid snapshots after filtering from records
                if (!building_name.equals("none")) { // we are filtering by building
                    for (DataSnapshot record : snapshot.getChildren()) {
                        if (record.child("building_name").getValue().toString().equals(building_name)) {
                            System.out.println("Adding a record");
                            record_snapshots.add(record);
                        }
                    }
                } else { // we are not filtering by building
                    for (DataSnapshot record : snapshot.getChildren()) {
                        record_snapshots.add(record);
                    }
                }

                //we now have record objects filtered by building (time and date to come soon)
                System.out.println("Record objects gathered: " + record_snapshots.size());

                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // next we want to grab the users that match user-specific inputs (major)
                ArrayList<DataSnapshot> user_snapshots = new ArrayList<>();
                database.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot user : snapshot.getChildren()) {
                            if (user.child("major").getValue().toString().equals(major)) {
                                System.out.println("Adding a user");
                                user_snapshots.add(user);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                System.out.println("User objects gathered: " + user_snapshots.size());

                // we have records that match record filters and users that match user-filters, now join
                ArrayList<DataSnapshot> results = mimicJoin(record_snapshots, user_snapshots);

                //now we can just build our results: list of records and map from email--> user
                ArrayList<Record> final_records = getRecords(results);
                HashMap<String, User> mapped_users = getUserMap(user_snapshots);

                System.out.println("Built final results: " + final_records.size());
                for (Record r : final_records) {
                    System.out.println(mapped_users.get(r.user_email).first_name + " was at " + r.building_name);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public ArrayList<DataSnapshot> mimicJoin(ArrayList<DataSnapshot> records, ArrayList<DataSnapshot> users) {
        ArrayList<String> userEmails = new ArrayList<>();
        for (DataSnapshot user : users) {
            userEmails.add(user.child("email").getValue().toString());
        }

        ArrayList<DataSnapshot> results = new ArrayList<>();
        for (DataSnapshot record : records) {
            if (userEmails.contains(record.child("user_email").getValue().toString())) {
                // this is a valid data snapshot that matches ALL search criteria
                results.add(record);
            }
        }

        return results;
    }

    public ArrayList<Record> getRecords(ArrayList<DataSnapshot> results) {
        ArrayList<Record> records = new ArrayList<>();
        for (DataSnapshot record : results) {
            String check_in_string = record.child("check_in").child("time").getValue().toString();
            Date check_in = new Date(Long.parseLong(check_in_string));

            Date check_out = null;
            if (record.child("check_out").exists()) {
                String check_out_string = record.child("check_in").child("time").getValue().toString();
                check_out = new Date(Long.parseLong(check_out_string));
            }

            Record r = new Record();
            r.building_name = record.child("building_name").getValue().toString();
            r.user_email = record.child("user_email").getValue().toString();
            r.check_in = check_in;
            r.check_out = check_out;

            records.add(r);
        }

        return records;
    }

    public HashMap<String, User> getUserMap(ArrayList<DataSnapshot> users) {
        HashMap<String, User> results = new HashMap<>();
        for (DataSnapshot user : users) {
            User u = new User();
            u.email = user.child("email").getValue().toString();
            u.first_name = user.child("first_name").getValue().toString();
            u.last_name = user.child("last_name").getValue().toString();
            u.password = null; // never want to actually return password
            u.role = user.child("role").getValue().toString(); // this will always be student
            u.major = user.child("major").getValue().toString();
            u.usc_ID = Integer.parseInt(user.child("usc_ID").getValue().toString());
            u.curr_loc = user.child("curr_loc").getValue().toString();
            results.put(user.child("email").getValue().toString(), u);
        }

        return results;
    }

    // SEARCH FUNCTIONS -- have to individually query and then combine results in java code :(
        // DON'T SHOW DELETED ACCOUNTS
    public void queryRecords(String building_name, long start_date, long end_date, queryRecordsInterface listener) {
        ArrayList<Record> results = new ArrayList<>();
        if (building_name.equals("none")) {
            database.child("records").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot record : snapshot.getChildren()) {
                        boolean time_valid = true; // checks if our record is within the time range
                        if (start_date != 0 && end_date != 0) { // if one is set the other MUST be set; neither set is both as 0
                            time_valid = false;
                            long record_start = Long.parseLong(record.child("check_in").child("time").getValue().toString());
                            long record_end = end_date;
                            if (record.child("check_out").exists()) {
                                record_end = Long.parseLong(record.child("check_out").child("time").getValue().toString());
                            }
                            if (record_start <= end_date && record_end >= start_date) {
                                time_valid = true;
                            }
                        }

                        if (time_valid && !record.child("status").exists()) { // status only exists for deleted accounts
                            String check_in_string = record.child("check_in").child("time").getValue().toString();

                            Date check_in = new Date(Long.parseLong(check_in_string));

                            Date check_out = null;
                            if (record.child("check_out").exists()) {
                                String check_out_string = record.child("check_out").child("time").getValue().toString();
                                check_out = new Date(Long.parseLong(check_out_string));
                            }

                            Record r = new Record();
                            r.building_name = record.child("building_name").getValue().toString();
                            r.user_email = record.child("user_email").getValue().toString();
                            r.check_in = check_in;
                            r.check_out = check_out;

                            results.add(r);
                        }
                    }
                    listener.queryRecordsResult(results);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) { }
            });
        } else {
            database.child("records").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot record : snapshot.getChildren()) {
                        if (record.child("building_name").getValue().toString().equals(building_name)) {
                            // do the unix timestamp comparison stuff here
                            boolean time_valid = true; // checks if our record is within the time range
                            if (start_date != 0 && end_date != 0) { // if one is set the other MUST be set; neither set is both as 0
                                time_valid = false;
                                long record_start = Long.parseLong(record.child("check_in").child("time").getValue().toString());
                                long record_end = end_date;
                                if (record.child("check_out").exists()) {
                                    record_end = Long.parseLong(record.child("check_out").child("time").getValue().toString());
                                }
                                if (record_start <= end_date && record_end >= start_date) {
                                    time_valid = true;
                                }
                            }

                            if (time_valid && !record.child("status").exists()) { // status only exists for deleted accounts
                                String check_in_string = record.child("check_in").child("time").getValue().toString();
                                Date check_in = new Date(Long.parseLong(check_in_string));

                                Date check_out = null;
                                if (record.child("check_out").exists()) {
                                    String check_out_string = record.child("check_out").child("time").getValue().toString();
                                    check_out = new Date(Long.parseLong(check_out_string));
                                }

                                Record r = new Record();
                                r.building_name = record.child("building_name").getValue().toString();
                                r.user_email = record.child("user_email").getValue().toString();
                                r.check_in = check_in;
                                r.check_out = check_out;

                                results.add(r);
                            }
                        }
                    }
                    listener.queryRecordsResult(results);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) { }
            });
        }
    }

    public void queryUsers(String major, queryUsersInterface listener) {
        if (major.equals("none")) {
            HashMap<String, User> results = new HashMap<>();
            database.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot user : snapshot.getChildren()) {
                        if (!user.child("status").exists()) { //only want to show non deleted accounts
                            User u = new User();
                            u.email = user.child("email").getValue().toString();
                            u.first_name = user.child("first_name").getValue().toString();
                            u.last_name = user.child("last_name").getValue().toString();
                            u.password = null; // never want to actually return password
                            u.role = user.child("role").getValue().toString(); // this will always be student
                            u.major = user.child("major").getValue().toString();
                            u.usc_ID = Integer.parseInt(user.child("usc_ID").getValue().toString());
                            u.curr_loc = user.child("curr_loc").getValue().toString();
                            results.put(u.email, u);
                        }

                    }

                    listener.queryUsersResults(results);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) { }
            });
        } else {
            HashMap<String, User> results = new HashMap<>();
            database.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot user : snapshot.getChildren()) {
                        if (user.child("major").getValue().toString().equals(major) && !user.child("status").exists()) {
                            User u = new User();
                            u.email = user.child("email").getValue().toString();
                            u.first_name = user.child("first_name").getValue().toString();
                            u.last_name = user.child("last_name").getValue().toString();
                            u.password = null; // never want to actually return password
                            u.role = user.child("role").getValue().toString(); // this will always be student
                            u.major = user.child("major").getValue().toString();
                            u.usc_ID = Integer.parseInt(user.child("usc_ID").getValue().toString());
                            u.curr_loc = user.child("curr_loc").getValue().toString();
                            results.put(u.email, u);
                        }

                    }

                    listener.queryUsersResults(results);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) { }
            });
        }
    }

    // ACTUAL WORKING SEARCH QUERIES - DONT USE ABOVE ONES

    public void userQuery(String major, long usc_ID, userQueryInterface listener) {
        if (major.equals("none")) {
            ArrayList<User> users = new ArrayList<>();
            database.child("users").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    for (DataSnapshot user : task.getResult().getChildren()) {
                        // I removed the && !user.child("status").exists() from the if statement - Lucas
                        // so that deleted accounts will now show up - Also Lucas
                        if (user.child("role").getValue().toString().equals("student")) {
                            boolean id_match = false;
                            if (usc_ID != 0 && usc_ID == Long.valueOf(user.child("usc_ID").getValue().toString())) {
                                id_match = true;
                            } else if (usc_ID == 0) {
                                id_match = true;
                            }

                            if (id_match) {
                                User u = new User();
                                u.email = user.child("email").getValue().toString();
                                u.first_name = user.child("first_name").getValue().toString();
                                u.last_name = user.child("last_name").getValue().toString();
                                u.password = null; // never want to actually return password
                                u.role = user.child("role").getValue().toString(); // this will always be student
                                u.major = user.child("major").getValue().toString();
                                u.usc_ID = Long.parseLong(user.child("usc_ID").getValue().toString());
                                u.curr_loc = user.child("curr_loc").getValue().toString();
                                users.add(u);
                            }
                        }
                    }
                    listener.userQuery(users);
                }
            });
        } else {
            ArrayList<User> users = new ArrayList<>();
            database.child("users").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    for (DataSnapshot user : task.getResult().getChildren()) {
                        if (user.child("major").getValue().toString().equals(major)) {
                            // I removed the && !user.child("status").exists() from the if statement - Lucas
                            // so that deleted accounts will now show up - also Lucas
                            if (user.child("role").getValue().toString().equals("student")) {
                                boolean id_match = false;
                                if (usc_ID != 0 && usc_ID == Long.valueOf(user.child("usc_ID").getValue().toString())) {
                                    id_match = true;
                                } else if (usc_ID == 0) {
                                    id_match = true;
                                }

                                if (id_match) {
                                    User u = new User();
                                    u.email = user.child("email").getValue().toString();
                                    u.first_name = user.child("first_name").getValue().toString();
                                    u.last_name = user.child("last_name").getValue().toString();
                                    u.password = null; // never want to actually return password
                                    u.role = user.child("role").getValue().toString(); // this will always be student
                                    u.major = user.child("major").getValue().toString();
                                    u.usc_ID = Long.parseLong(user.child("usc_ID").getValue().toString());
                                    u.curr_loc = user.child("curr_loc").getValue().toString();
                                    users.add(u);
                                }
                            }
                        }
                    }

                    listener.userQuery(users);
                }
            });
        }
    }

    public void recordQuery(String building, long start_date, long end_date, resultsQueryInterface listener) {
        if(building.equals("none")) {
            HashMap<String, Record> mapped_records = new HashMap<>();
            database.child("records").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    for (DataSnapshot record : task.getResult().getChildren()) {
                        String user = record.child("user_email").getValue().toString();
                        boolean time_valid = true; // checks if our record is within the time range
                        if (start_date != 0 && end_date != 0) { // if one is set the other MUST be set; neither set is both as 0
                            time_valid = false;
                            long record_start = Long.parseLong(record.child("check_in").child("time").getValue().toString());
                            long record_end = end_date;
                            if (record.child("check_out").exists()) {
                                record_end = Long.parseLong(record.child("check_out").child("time").getValue().toString());
                            }
                            if (record_start <= end_date && record_end >= start_date) {
                                time_valid = true;
                            }
                        }

                        if (time_valid && !record.child("status").exists()) { // status only exists for deleted accounts
                            String check_in_string = record.child("check_in").child("time").getValue().toString();

                            Date check_in = new Date(Long.parseLong(check_in_string));

                            Date check_out = null;
                            if (record.child("check_out").exists()) {
                                String check_out_string = record.child("check_out").child("time").getValue().toString();
                                check_out = new Date(Long.parseLong(check_out_string));
                            }

                            Record r = new Record();
                            r.building_name = record.child("building_name").getValue().toString();
                            r.user_email = record.child("user_email").getValue().toString();
                            r.check_in = check_in;
                            r.check_out = check_out;

                            mapped_records.put(user, r);
                        }
                    }

                    listener.resultsQuery(mapped_records);
                }
            });
        } else {
            HashMap<String, Record> mapped_records = new HashMap<>();
            database.child("records").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    for (DataSnapshot record : task.getResult().getChildren()) {
                        if (record.child("building_name").getValue().toString().equals(building)) {
                            String user = record.child("user_email").getValue().toString();
                            boolean time_valid = true; // checks if our record is within the time range
                            if (start_date != 0 && end_date != 0) { // if one is set the other MUST be set; neither set is both as 0
                                time_valid = false;
                                long record_start = Long.parseLong(record.child("check_in").child("time").getValue().toString());
                                long record_end = end_date;
                                if (record.child("check_out").exists()) {
                                    record_end = Long.parseLong(record.child("check_out").child("time").getValue().toString());
                                }
                                if (record_start <= end_date && record_end >= start_date) {
                                    time_valid = true;
                                }
                            }

                            if (time_valid && !record.child("status").exists()) { // status only exists for deleted accounts
                                String check_in_string = record.child("check_in").child("time").getValue().toString();

                                Date check_in = new Date(Long.parseLong(check_in_string));

                                Date check_out = null;
                                if (record.child("check_out").exists()) {
                                    String check_out_string = record.child("check_out").child("time").getValue().toString();
                                    check_out = new Date(Long.parseLong(check_out_string));
                                }

                                Record r = new Record();
                                r.building_name = record.child("building_name").getValue().toString();
                                r.user_email = record.child("user_email").getValue().toString();
                                r.check_in = check_in;
                                r.check_out = check_out;

                                mapped_records.put(user, r);
                            }
                        }
                    }
                    listener.resultsQuery(mapped_records);
                }
            });
        }
    }

}
