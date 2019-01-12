package com.example.moonside.cookassist;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.example.moonside.cookassist.Recipe;

@Database(entities = {Recipe.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class RecipeDatabase extends RoomDatabase {
    public abstract RecipeDao getProductDao();

    private static RecipeDatabase INSTANCE;


    public static RecipeDatabase getDatabase(final Context context) {
        if (INSTANCE == null)  {
            synchronized (ProductDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), RecipeDatabase.class, "recipe_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}

