package com.example.trojancheckincheckout;

import android.graphics.Bitmap;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public interface FirebaseListener {
    void addUser(boolean addStatus);
    void deleteUser(boolean deleteStatus);
    void reactivateUser(boolean reactivateStatus);
    void getUserRole(String role);
    void verifyPassword(boolean passwordStatus);
    void updatePasswordStatus(boolean status);
    void getStudent(User student);
    void addBuilding(boolean addStatus);
    void removeBuilding(boolean removeStatus);
    void updateBuildingList(boolean buildingStatus);
    void getCurrCapacity(int capacity);
    void updateCurrCapacity(boolean updateStatus);
    void updateMaxCapacity(boolean updateStatus);
    void getMaxCapacity(int capacity);
    void onCheckEmail(boolean exists);
    void updateBuildingQRCode(boolean status);
    void getBuildingQRCode(Bitmap bitmap);
    void onGetBuildings(ArrayList<Building> buildings);
    void onGetRecordsByBuilding(ArrayList<Record> records);
    void onGetRecordsByStudent(ArrayList<Record> records);
    void updatePicture(boolean status);
    void getPicture(Bitmap bitmap);
    void getStudentStatus(boolean status);
    void onUpdateLocation(boolean status);
    void onCheckIn(Boolean success);
    void onCheckOut(Boolean success);
}

interface addUserInterface { void addUser(boolean status);}
interface deleteUserInterface { void deleteUser(boolean status);}
interface
reactivateUserInterface { void reactivateUser(boolean status);}
interface updatePasswordInterface { void updatePasswordStatus(boolean status); }
interface userRoleInterface {void getUserRole(String role);}
interface verifyPasswordInterface { void verifyPassword(boolean status);}
interface getUserInterface { void getUser(User user);}
interface addbuildingInterface { void addBuilding(boolean status); }
interface deleteBuildingInterface{ void removeBuilding(boolean status);}
interface updateBuildingListInterface {void updateBuildingList(boolean status); }
interface getCurrentCapacityInterface{ void getCurrCapacity(int capacity); }
interface updateCurrentCapacityInterface {void updateCurrCapacity(boolean status); }
interface updateMaxCapacityInterface{ void updateMaxCapacity(boolean status);}
interface getMaxCapacityInterface {void getMaxCapacity(int capacity);}
interface checkEmailInterface {void onCheckEmail(boolean exists); }
interface checkIdInterface {void onCheckId(boolean exists); }
interface checkAccountStatusInterface { void checkAccountStatus(boolean deleted); }
interface updateQRCodeInterface {void updateBuildingQRCode(boolean status);}
interface getQRCodeInterface {void getBuildingQRCode(Bitmap qr_code);}
interface getBuildingsInterface{ void onGetBuildings(ArrayList<Building> buildings);}
interface getRecordsByBuildingInterface {void onGetRecordsByBuilding(ArrayList<Record> records);}
interface getRecordsByStudentInterface {void onGetRecordsByStudent(ArrayList<Record> records);}
interface updatePictureInterface {void updatePicture(boolean status);}
interface getPictureInterface {void getPicture(Bitmap picture);}
interface getStudentStatusInterface {void getStudentStatus(boolean status);}
interface updateLocationInterface{void onUpdateLocation(boolean status);}
interface checkInInterface{void onCheckIn(boolean success);}
interface checkOutInterface{void onCheckOut(boolean success);}
interface getAllCurrentlyCheckedInRecordsInterface{void getAllCurrentlyCheckedInRecords(ArrayList<Record> records);}
interface automaticCheckoutInterface{void automaticCheckOut(boolean success);}
interface getCurrentStudentsInBuildingInterface{void getCurrentStudentsInBuilding(ArrayList<Record> records);}
interface getAllStudentsInterface{void getAllStudents(ArrayList<User> students);}
interface setUserAccountToInactiveInterface{void setStudentAccountToInactive(boolean success);}
interface queryRecordsInterface{ void queryRecordsResult(ArrayList<Record> results); }
interface queryUsersInterface{ void queryUsersResults(HashMap<String, User> results); }
interface userQueryInterface { void userQuery(ArrayList<User> results); }
interface resultsQueryInterface { void resultsQuery(HashMap<String, Record> results); }
