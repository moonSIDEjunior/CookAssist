package com.example.moonside.cookassist;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by moonSIDE on 21.03.2018.
 */

public class MyApplication  {

    private static MyApplication mInstance;
    private ArrayList<String[]> list = null;

    public static MyApplication getInstance() {
        if(mInstance == null)
            mInstance = new MyApplication();

        return mInstance;
    }

    private MyApplication() {
        list = new ArrayList<>();
    }
    // retrieve array from anywhere
    public ArrayList<String[]> getArray() {
        return this.list;
    }
    public void setList(ArrayList<String[]> mList) {
        this.list = mList;
    }
    //Add element to array
    public void addToArray(String[] value) {
        list.add(value);
    }
}