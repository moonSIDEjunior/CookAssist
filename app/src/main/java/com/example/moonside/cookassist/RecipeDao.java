package com.example.moonside.cookassist;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;


import com.example.moonside.cookassist.Recipe;

import java.util.List;

/**
 * Created by 1 on 05.05.2018.
 */
@Dao
public interface RecipeDao {

    @Query("SELECT * FROM Recipes")
    List<Recipe> getAll();

    @Insert
    public void addRecipe(Recipe recipe);

    @Query("DELETE FROM Recipes WHERE Recipe_Name = :name")
    void deleteByName(String... name);

    @Delete
    void delete(Recipe... recipe);

    @Insert
    void insert(Recipe... recipes);

    @Query("SELECT * FROM Recipes")
    List<Recipe> getRecipes();

    @Query("UPDATE Recipes SET Recipe_Name = :nameUpdate WHERE Recipe_Name LIKE :name")
    void updateName(String name, String... nameUpdate);

    @Query("UPDATE Recipes SET Product_Name = :count WHERE Product_Name LIKE :name ")
    void updateCount(String name, String... count);

    @Update
    void updateRecipe(Recipe recipe);
}

