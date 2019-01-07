package com.example.moonside.cookassist;

import java.util.ArrayList;
import java.util.List;

public class ListSingleton  {

    private static ListSingleton mInstance;
    private List<Product> list = null;

    public static ListSingleton getInstance() {
        if(mInstance == null)
            mInstance = new ListSingleton();

        return mInstance;
    }

    private ListSingleton()
    {
        list = new ArrayList<>();
    }
    // retrieve array from anywhere
    public List<Product> getArray()
    {
        return this.list;
    }
    public void setList(List<Product> mList)
    {
        this.list = mList;
    }

}