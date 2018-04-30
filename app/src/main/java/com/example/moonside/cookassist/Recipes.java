package com.example.moonside.cookassist;

/**
 * Created by moonSIDE on 21.03.2018.
 */

public class Recipes {
    String title;
    String image;
    String price;

    public String getName(){
        return title;
    }

    public void setName(String name){
        this.title = name;
    }

    public String getImage(){
        return image;
    }

    public void setImage(String image){
        this.image = image;
    }

    public String getCount(){
        return price;
    }

    public void setCount(String count){
        this.price = count;
    }
}
