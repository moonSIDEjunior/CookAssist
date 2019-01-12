package com.example.moonside.cookassist;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.util.Log;

import java.util.List;

@Entity(tableName = "Recipes")
public class Recipe implements Parent<Product>,
        com.bignerdranch.expandablerecyclerview.model.Parent<Product> {

    @PrimaryKey(autoGenerate = true)
    private int id = 0;

    @ColumnInfo(name = "Recipe_Name")
    private String RecipeName;

//    @Ignore
    @ColumnInfo(name = "Recipe_products")
    @TypeConverters(Converters.class)
    private List<Product> ProductList;

    public List<Product> getProductList() {
        return ProductList;
    }

    public Recipe(){

    }

    public Recipe(String RecipeName, List<Product> ProductList){
        this.RecipeName = RecipeName;
        this.ProductList = ProductList;

    }

    @Override
    public List<Product> getChildList() {
        return ProductList;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }

    public Product getProduct(int position){
        return ProductList.get(position);
    }

    public int getId() {
        return id;
    }

    public String getRecipeName() {
        return RecipeName;
    }

    public void setRecipeName(String recipeName) {
        RecipeName = recipeName;
    }

    public void setProductList(List<Product> productList) {
        ProductList = productList;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int countCalories() {
            int temp = 0;
            for (Product item : ProductList) {
                try {
                    temp += Integer.parseInt(String.valueOf(item.getCalories() *
                            item.getCount() / 100));
                    Log.w("Info1", item.getName() + " " +
                            String.valueOf(item.getCalories()) + " " +
                            String.valueOf(item.getCount()) + " " +
                    String.valueOf(temp));
                }
                catch (NullPointerException e) {

                }
            }
        return temp;
    }

    public String findInNames(String currentName, int currentCount) {
        for(int i = 0; i < ProductList.size(); i++) {
            if (ProductList.get(i).getName().equals(currentName))
                if (ProductList.get(i).getCount() > currentCount) {
                    String lackProductName = ProductList.get(i).getName();
                    int lackProductCount = ProductList.get(i).getCount() - currentCount;
                    return "Вам нехватает " + String.valueOf(lackProductCount) +
                            " грамм продукта \"" + lackProductName + "\".";
                }
                else {
                    return currentName;
                }
        }
        return "Нет совпадения";
    }
}

