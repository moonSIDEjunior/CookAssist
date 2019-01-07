package com.example.moonside.cookassist;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;

/**
 * Created by moonSIDE on 26.03.2018.
 */
@Entity (tableName = "product_table")
public class Product {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "product_id")
    private Integer id = null;

    @ColumnInfo(name = "product_name")
    private String name;

    @ColumnInfo(name = "product_count")
    private Integer count;

    @ColumnInfo(name = "product_calories")
    private Integer calories;

    Product(){

    }

    public Product ProductAllWithId(Integer id, String name, Integer count, Integer calories) {
        this.id = id;
        this.name = name;
        this.count = count;
        this.calories = calories;
        return this;
    };

    public Product ProductAll(String name, Integer count, Integer calories) {
        this.name = name;
        this.count = count;
        this.calories = calories;
        return this;
    };

    public Product ProductIntoReceipt(String name, Integer count) {
        this.name = name;
        this.count = count;
        return this;
    };

    public Integer getId(){
        return id;
    }

    public void setId(int   id){
        this.id = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public Integer getCount(){
        return count;
    }

    public void setCount(Integer count){
        this.count = count;
    }

    public Integer getCalories(){
        return calories;
    }

    public void setCalories(Integer calories){
        this.calories = calories;
    }

}


