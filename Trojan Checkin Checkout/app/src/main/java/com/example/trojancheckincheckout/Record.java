package com.example.trojancheckincheckout;

import java.util.Date;

public class Record {
    public String user_email;
    public String building_name;
    public Date check_in;
    public Date check_out;

    //need empty constructor to serialize in firebase
    public Record() {}

    public Record(String user_email, String building_name, Date check_in, Date check_out) {
        this.user_email = user_email;
        this.building_name = building_name;
        this.check_in = check_in;
        this.check_out = check_out;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getBuilding_name() {
        return building_name;
    }

    public void setBuilding_name(String building_name) {
        this.building_name = building_name;
    }

    public Date getCheck_in() {
        return check_in;
    }

    public void setCheck_in(Date check_in) {
        this.check_in = check_in;
    }

    public Date getCheck_out() {
        return check_out;
    }

    public void setCheck_out(Date check_out) {
        this.check_out = check_out;
    }

}
