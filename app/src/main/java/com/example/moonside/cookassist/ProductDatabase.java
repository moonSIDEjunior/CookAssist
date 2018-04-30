package com.example.moonside.cookassist;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Product.class}, version = 1)
public abstract class ProductDatabase extends RoomDatabase{
    public abstract ProductDao getProductDao();

    private static ProductDatabase INSTANCE;


    public static ProductDatabase getDatabase(final Context context) {
        if (INSTANCE == null)  {
            synchronized (ProductDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), ProductDatabase.class, "product_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
