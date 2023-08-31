package com.example.trojancheckincheckout;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//import com.google.zxing.BarcodeFormat;
//import com.google.zxing.EncodeHintType;
//import com.google.zxing.MultiFormatWriter;
//import com.google.zxing.WriterException;
//import com.google.zxing.common.BitMatrix;
//import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class Building {
    public String buildingName;
    public int max_capacity;
    public int curr_capacity;
    public Bitmap qrCode;

    // getters and setters and generators for this

    // need empty constructor to serialize for firebase
    public Building() {}

    public Building(String name, int max_capacity, int curr_capacity) {
        this.buildingName = name;
        this.max_capacity = max_capacity;
        this.curr_capacity = curr_capacity;


        Database db = new Database();

        db.getBuildingList(new getBuildingsInterface() {

            @Override
            public void onGetBuildings(ArrayList<Building> buildings) {
                boolean validBuilding = false;
                for (Building b : buildings) {
                    if (b.getName().equals(buildingName)) {
                        validBuilding = true;
                    }
                }

                if (!validBuilding) {
                    Log.d("CSV", "Adding building " + buildingName);
                    db.addBuilding(buildingName, max_capacity, new addbuildingInterface() {
                        @Override
                        public void addBuilding(boolean status) {
                            if (status) {
                                Log.d("CSV", "Added successfully, making qr code");
                                buildingQRSetup();
                            }
                            else {
                                Log.d("QR Gen", "failed to add building being generated");
                            }
                        }
                    });
                }
                else {
                    Log.d("CSV", buildingName + " building exists, lets make a qr code");
                    buildingQRSetup();
                }
            }

        });
    }

    public String getName() {
        return buildingName;
    }

    public void setName(String name) {
        this.buildingName = name;
    }

    public int getMax_capacity() {
        return max_capacity;
    }

    public void setMax_capacity(int max_capacity) {
        this.max_capacity = max_capacity;
    }

    public int getCurr_capacity() {
        return curr_capacity;
    }

    public void setCurr_capacity(int curr_capacity) {
        this.curr_capacity = curr_capacity;
    }

    //TODO: implement qr stuff

//    public QRCode getQRCode() {
//        return code;
//    }
//
//    public void setQRCode(QRCode qr) {
//        this.code = qr;
//    }

    public Bitmap createQR(String data, String charset, Map hashMap, int height, int width) throws WriterException, IOException {
        BitMatrix bitMatrix = new MultiFormatWriter().encode(
                new String(data.getBytes(charset), charset),
                BarcodeFormat.QR_CODE, width, height);

        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                bmp.setPixel(x, y, bitMatrix.get(x,y) ? Color.BLACK : Color.WHITE);
            }
        }

        return bmp;
    }

    public void buildingQRSetup() {

        Database db = new Database();

        Log.d("CSV", "about to get " + buildingName + " qr code");

        db.getBuildingQRCode(buildingName, new getQRCodeInterface() {
            @Override
            public void getBuildingQRCode(Bitmap bitmap) {
                if (bitmap == null) {
                    Log.d("CSV", buildingName + " qr code is null");
                    String charset = "UTF-8";

                    Map<EncodeHintType, ErrorCorrectionLevel> hashMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();

                    hashMap.put(EncodeHintType.ERROR_CORRECTION,
                            ErrorCorrectionLevel.L);

                    try {
                        bitmap = createQR(buildingName, charset, hashMap, 200, 200);
                    }
                    catch (Exception e) {
                        //TODO: Toast here instead?
                        System.out.println("error with making the qr code");
                    }

                    if (bitmap != null) {

                        Log.d("CSV", "add a qr code for " + buildingName);

                        db.updateBuildingQRCode(buildingName, bitmap, new updateQRCodeInterface() {
                            @Override
                            public void updateBuildingQRCode(boolean success) {
                                if (!success) {
                                    //TODO: toast here?
                                    Log.d("CSV", "failed update qr code for " + buildingName);
                                }
                                else {
                                    Log.d("CSV", "success update code for " + buildingName);
                                }
                            }
                        });
                    }
                    else {
                        Log.d("CSV", "failed to make a qr code for " + buildingName);
                    }
                }
            }
        });
    }

//
//    public static boolean updateBuildingCSV(String filename){
//        //TODO: get the file somehow
//
//        ArrayList<String[]> newBuildings = new ArrayList<>();
//
//        BufferedReader reader;
//        try {
//            reader = new BufferedReader(new FileReader(filename));
//            String line = reader.readLine();
//            while (line != null) {
//                newBuildings.add(line.split(","));
//                line = reader.readLine();
//            }
//            reader.close();
//        } catch (IOException e) {
//            return false;
//        }
//
//        Hashtable<String, Building> buildingList = new Hashtable<>();
//
//
//        try {
//            for (int i = 0; i < newBuildings.size(); ++i) {
//                String name = newBuildings.get(i)[0];
//                int cur_capacity = Integer.parseInt(newBuildings.get(i)[1]);
//                int max_capacity = Integer.parseInt(newBuildings.get(i)[2]);
//                Building temp = new Building(name, cur_capacity, max_capacity);
//                buildingList.put(name, temp);
//            }
//        }
//        catch (Exception e) {
//            return false;
//        }
//
//        //TODO: call database function to send building list to database
//        Database db = new Database();
//        db.updateTrackedBuildings(buildingList, new FirebaseListener(){
//
//            @Override
//            public void updateBuildingList(boolean buildingStatus) {
//                if (!buildingStatus) {
//                    //TODO: learn to toast
//
//                }
//            }
//            public void updateBuildingQRCode(boolean success) { }
//            public void getBuildingQRCode(Bitmap encoded_image) { }
//            public void getPicture(Bitmap encoded_image) { }
//            public void onGetRecordsByStudent(ArrayList<Record> records) { }
//            public void getStudent(User student) { }
//            public void getUserRole(String role) { }
//            public void onCheckEmail(boolean exists) { }
//            public void deleteUser(boolean deleteStatus) {  }
//            public void verifyPassword(boolean passwordStatus) { }
//
//            @Override
//            public void updatePasswordStatus(boolean status) {
//
//            }
//
//            public void addUser(boolean addStatus) { }
//            public void addBuilding(boolean addStatus) { }
//            public void removeBuilding(boolean removeStatus) { }
//            public void getCurrCapacity(int capacity) { }
//            public void getMaxCapacity(int capacity) { }
//            public void updateCurrCapacity(boolean updateStatus) { }
//
//            @Override
//            public void updateMaxCapacity(boolean updateStatus) {
//
//            }
//
//            public void onGetBuildings(ArrayList<Building> buildings) { }
//            public void onGetRecordsByBuilding(ArrayList<Record> records) { }
//            public void updatePicture(boolean status) { }
//            public void getStudentStatus(boolean status) {  }
//
//            @Override
//            public void onUpdateLocation(boolean status) {
//
//            }
//
//            public void onCheckIn(Boolean success) { }
//            public void onCheckOut(Boolean success) { }
//        });
//
//
//        return true;
//    }
}
