package com.example.moonside.cookassist;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "Recipes")
public class Recipe {

    @PrimaryKey(autoGenerate = true)
    private int id = 0;

    @ColumnInfo(name = "Recipe_Name")
    private String RecipeName;

    @ColumnInfo(name = "Product_Name")
    private String ProductName;


    public Recipe(String RecipeName, String ProductName){
        this.RecipeName = RecipeName;
        this.ProductName = ProductName;

    }

    public String getRecipeName() {
        return RecipeName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRecipeName(String recipeName) {
        this.RecipeName = recipeName;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        this.ProductName = productName;
    }

}

