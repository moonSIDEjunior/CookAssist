package com.example.moonside.cookassist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.ChildViewHolder;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends ExpandableRecyclerAdapter<Recipe, Product, RecipeViewHolder, ProductViewHolder> implements Filterable {

    private static final int PARENT_VEGETARIAN = 0;
    private static final int PARENT_NORMAL = 1;
    private static final int CHILD_NORMAL = 3;

    private LayoutInflater mInflater;
    private List<Recipe> mRecipeList;
    private List<Recipe> mRecipeListFiltered;
    private List<Recipe> tempList;

    public RecipeAdapter(Context context, @NonNull List<Recipe> recipeList) {
        super(recipeList);
        mRecipeList = recipeList;
        mInflater = LayoutInflater.from(context);
        mRecipeListFiltered = recipeList;
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
        recipeViewHolder.bind(mRecipeList.get(parentPosition));
        if (recipeViewHolder.isExpanded()) {
            recipeViewHolder.setExpanded(false);
            recipeViewHolder.rotateDown();
        } else {
            recipeViewHolder.setExpanded(true);
            recipeViewHolder.rotateUP();
        }

    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mRecipeListFiltered = mRecipeList;
                } else {
                    List<Recipe> filteredList = new ArrayList<>();
                    for (Recipe row : mRecipeList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getRecipeName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    mRecipeListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mRecipeListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mRecipeListFiltered = (ArrayList<Recipe>) filterResults.values;
                int j = 0;
                //TODO: Complete or fucking delete crappy filtering
//                for (int i = 0; i + j < mRecipeList.size(); i++){
//                    if (!mRecipeListFiltered.contains(mRecipeList.get(i + j))) {
//                        tempList.add(mRecipeList.get(i + j));
//                        notifyParentRemoved(i);
//                        notifyItemRangeChanged(i, mRecipeList.size());
//                        i -= 1;
//                        j += 1;
//                    }
//                }
//                for (int i = 0; i < tempList.size(); i++){
//                    if (mRecipeListFiltered.contains(tempList.get(i))) {
//                        notifyParentInserted(i);
//                        notifyItemRangeChanged(i, mRecipeListFiltered.size());
//                    }
//                }
            }
        };
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
//        Product product = mRecipeList.get(parentPosition).getProduct(childPosition);
        return CHILD_NORMAL;

    }

    @Override
    public boolean isParentViewType(int viewType) {
        return viewType == PARENT_VEGETARIAN || viewType == PARENT_NORMAL;
    }

    public String getName(int position){
        return mRecipeList.get(position).getRecipeName();
    }

    public List<Product> getList(int position) {
        return mRecipeList.get(position).getProductList();
    }

    public void addItem(int position, Recipe recipe) {
        mRecipeList.add(recipe);
        notifyParentInserted(position);
        notifyItemRangeChanged(position, mRecipeList.size());
    }

    public void removeItem(int position) {
        mRecipeList.remove(position);
        notifyItemRemoved(position);
        notifyParentRemoved(position);
        notifyItemRangeChanged(position, mRecipeList.size());
    }

    final public void restoreItem(int position, Recipe recipe) {
        mRecipeList.add(position, recipe);
        notifyParentInserted(position );
        notifyItemRangeChanged(position + 1, mRecipeList.size());
    }

}

