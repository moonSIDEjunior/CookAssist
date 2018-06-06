package com.example.moonside.cookassist;

import java.util.ArrayList;

public class MySingleton {

    private ArrayList savedAutoComplete;

    private static final MySingleton ourInstance = new MySingleton();

    public static MySingleton getInstance() {
        return ourInstance;
    }

    private MySingleton() {
    }

    public void saveAutoComplete(ArrayList instance) {
        this.savedAutoComplete = instance;
    }

    public ArrayList getSavedAutoComplete() {
        return savedAutoComplete;
    }
}
