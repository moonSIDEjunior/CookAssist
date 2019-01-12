package com.example.moonside.cookassist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.ChildViewHolder;

import java.util.List;

public class RecipeAdapter extends ExpandableRecyclerAdapter<Recipe, Product, RecipeViewHolder, ProductViewHolder> {

    private static final int PARENT_VEGETARIAN = 0;
    private static final int PARENT_NORMAL = 1;
    private static final int CHILD_NORMAL = 3;

    private LayoutInflater mInflater;
    private List<Recipe> mRecipeList;


    public RecipeAdapter(Context context, @NonNull List<Recipe> recipeList) {
        super(recipeList);
        mRecipeList = recipeList;
        mInflater = LayoutInflater.from(context);
    }

    @UiThread
    @NonNull
    @Override
    public RecipeViewHolder onCreateParentViewHolder(ViewGroup parentViewGroup, int viewType) {
        View recipeView;
        switch (viewType) {
            default:
            case PARENT_NORMAL:
                recipeView = mInflater.inflate(R.layout.recipe_view, parentViewGroup, false);
                break;
        }
        return new RecipeViewHolder(recipeView);
    }

    @UiThread
    @NonNull
    @Override
    public ProductViewHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
        View ingredientView;
        switch (viewType) {
            default:
            case CHILD_NORMAL:
                ingredientView = mInflater.inflate(R.layout.ingredient_view, childViewGroup, false);
                break;
        }
        return new ProductViewHolder(ingredientView);
    }

    @UiThread
    @Override
    public void onBindParentViewHolder(@NonNull RecipeViewHolder recipeViewHolder, int parentPosition, @NonNull Recipe recipe) {
        recipeViewHolder.bind(recipe);
        if (recipeViewHolder.isExpanded()) {
            recipeViewHolder.setExpanded(false);
            recipeViewHolder.rotateDown();
        }
        else {
            recipeViewHolder.setExpanded(true);
            recipeViewHolder.rotateUP();
        }

    }



    @UiThread
    @Override
    public void onBindChildViewHolder(@NonNull ProductViewHolder ingredientViewHolder, int parentPosition, int childPosition, @NonNull Product product) {
        ingredientViewHolder.bind(product);
    }

    @Override
    public int getParentViewType(int parentPosition) {
        return PARENT_NORMAL;
    }

    @Override
    public int getChildViewType(int parentPosition, int childPosition) {
        Product product = mRecipeList.get(parentPosition).getProduct(childPosition);
        return CHILD_NORMAL;

    }

    @Override
    public boolean isParentViewType(int viewType) {
        return viewType == PARENT_VEGETARIAN || viewType == PARENT_NORMAL;
    }

    public void addItem(int position, Recipe recipe) {
        mRecipeList.add(recipe);
        notifyItemInserted(position);
        notifyParentInserted(position);
        notifyItemRangeChanged(position, mRecipeList.size());
    }

}

