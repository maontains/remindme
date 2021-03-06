package com.example.alexandermao.remindme001;

import android.app.Application;

import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;

public class GlobalVars extends Application {

    private static GlobalVars singleInstance = null;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    private String user;
    private String patient;
    private JSONObject task;
    public static GlobalVars getSingleInstance()
    {
        return singleInstance;
    }

    public void setTask(JSONObject task) {
        this.task = task;
    }

    public JSONObject getTask() {
        return task;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleInstance = this;
        user = "";
        patient = "";
    }


    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }
}
