package com.example.moonside.cookassist;

import java.util.ArrayList;
import java.util.List;

public class RecipesSingleton {
    private static RecipesSingleton mInstance;
    private List<Recipe> list = null;

    public static RecipesSingleton getInstance() {
        if(mInstance == null)
            mInstance = new RecipesSingleton();

        return mInstance;
    }

    private RecipesSingleton()
    {
        list = new ArrayList<Recipe>();
    }
    // retrieve array from anywhere
    public List<Recipe> getArray()
    {
        return this.list;
    }
    public void setList(List<Recipe> mList)
    {
        this.list = mList;
    }

}