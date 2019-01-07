package com.example.moonside.cookassist;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.example.moonside.cookassist.Recipe;

@Database(entities = {Recipe.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class RecipeDatabase extends RoomDatabase
{
    public abstract RecipeDao myDao();
}

