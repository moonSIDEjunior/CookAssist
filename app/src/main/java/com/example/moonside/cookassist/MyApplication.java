package com.example.moonside.cookassist;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.List;

/**
 * Created by moonSIDE on 21.03.2018.
 */

public class MyApplication extends Application {
    public static final String TAG = MyApplication.class.getSimpleName();

    private Product product;

    private List<Product> myArray;

    private int mIntVar;

    private RequestQueue mRequestqueue;
    
    private static MyApplication mInstance;
    
    @Override
    public void onCreate(){
        super.onCreate();
        mInstance = this;
    }
    
    public static synchronized MyApplication getInstance(){
        return mInstance;
    }
    
    public RequestQueue getRequestqueue(){
        if (mRequestqueue == null){
            mRequestqueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestqueue;
    }

    public List<Product> getList(){
        return myArray;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag){
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestqueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req){
        req.setTag(TAG);
        getRequestqueue().add(req);
    }

    public void cancelPeddingRequests(Object tag){
        if (mRequestqueue != null){
            mRequestqueue.cancelAll(tag);
        }
    }
    public void setIntVar(int var){
        mIntVar = var;
    }

    public int getIntVar(){
        return mIntVar;
    }


    public void setProductArray(Product product){
        myArray.add(product);
    }
}
