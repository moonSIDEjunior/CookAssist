package com.example.moonside.cookassist;

import java.util.ArrayList;

import static java.sql.Types.NULL;

public class ListForJSON {
    private String name;
    private ArrayList<Product> products;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public String findInNames(String currentName, int currentCount) {
        for(int i = 0; i < products.size(); i++) {
            if (products.get(i).getName().equals(currentName))
                if (products.get(i).getCount() > currentCount) {
                    String lackProductName = products.get(i).getName();
                    int lackProductCount = products.get(i).getCount() - currentCount;
                    return "Вам нехватает " + String.valueOf(lackProductCount) + " грамм продукта \"" + lackProductName + "\".";
                }
                else {
                    return currentName;
                }
        }
        return "Нет совпадения";
    }
}
