package com.example.moonside.cookassist;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ParentViewHolder;

public class RecipeViewHolder extends ParentViewHolder {

    private TextView mRecipeTextView;
    private TextView getmRecipeCalories;
    private ImageView mRecipeArrow;
    private int expanded;
    private View view;

    public RecipeViewHolder(View itemView) {
        super(itemView);
        this.expanded = expanded;
        this.view = itemView;
        mRecipeTextView = itemView.findViewById(R.id.recipe_textview);
        getmRecipeCalories = itemView.findViewById(R.id.recipe_calories);
        mRecipeArrow = itemView.findViewById(R.id.arrow_expand_imageview);
    }

    public void bind(Recipe recipe) {
        mRecipeTextView.setText(recipe.getRecipeName());
        getmRecipeCalories.setText("Калорийность: " + String.valueOf(recipe.countCalories()));
    }

    public void rotateDown() {
        mRecipeArrow.setImageDrawable(view.getResources().getDrawable(R.drawable.arrow_down));
    }
    public void rotateUP() {
        mRecipeArrow.setImageDrawable(view.getResources().getDrawable(R.drawable.arrow_right));
    }

}
