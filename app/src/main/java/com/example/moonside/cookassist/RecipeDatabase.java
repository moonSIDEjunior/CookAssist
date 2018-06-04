package com.example.moonside.cookassist;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.moonside.cookassist.Recipe;

@Database(entities = {Recipe.class}, version = 1)
public abstract class RecipeDatabase extends RoomDatabase
{
    public abstract RecipeDao myDao();
}

