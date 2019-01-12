package com.example.moonside.cookassist;


import android.arch.persistence.room.OnConflictStrategy;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;


public class ProductViewHolder extends ChildViewHolder {

    private TextView mProductTextView;
    private TextView mProductCount;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        mProductTextView = (TextView) itemView.findViewById(R.id.ingredient_textview);
        mProductCount = itemView.findViewById(R.id.ingredient_count);
    }

    public void bind(@NonNull Product product) {
        mProductTextView.setText(product.getName());
        mProductCount.setText("Количество: " + String.valueOf(product.getCount()));
    }
}