package com.example.moonside.cookassist;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ProductDao {
    @Query("SELECT * FROM product_table")
    List<Product> getAll();

    @Query("SELECT * FROM product_table WHERE product_name LIKE :name LIMIT 1")
    Product findByName(String name);

    @Insert
    void insertOne(Product product);

    @Insert
    void insert(Product... products);

    @Update
    void update(Product product);

    @Query("DELETE FROM product_table WHERE product_name = :name")
    void deleteByName(String... name);

    @Query("UPDATE product_table SET product_name = :nameUpdate WHERE product_name LIKE :name")
    void updateName(String name, String... nameUpdate);

    @Query("UPDATE product_table SET product_count = :count WHERE product_name LIKE :name")
    void updateCount(String name, String... count);

    @Query("UPDATE product_table SET product_calories = :calories WHERE product_name LIKE :name")
    void updateCalories(String name, String... calories);

    @Query("UPDATE product_table SET product_id = :id")
    void updateIds(Integer... id);

    @Delete
    void delete(Product... products);
}
