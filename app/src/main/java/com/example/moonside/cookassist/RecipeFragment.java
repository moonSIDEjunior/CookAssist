package com.example.moonside.cookassist;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by moonSIDE on 21.03.2018.
 */



public class RecipeFragment extends Fragment {
    private static final String TAG = RecipeFragment.class.getSimpleName();
    private static final String URL = "https://api.androidhive.info/json/movies_2017.json";

    private RecyclerView recyclerView;
    private List<Recipes> recipesList;
    private StoreAdapter mAdapter;

    public RecipeFragment(){

    }

    public static RecipeFragment newInstance(){
        RecipeFragment fragment = new RecipeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.recipe_fragment, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recipesList = new ArrayList<>();
        mAdapter = new StoreAdapter(getActivity(), recipesList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(8), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setNestedScrollingEnabled(false);

        fetchRecipesItems();

        return view;
    }
//
    private void fetchRecipesItems(){
        JsonArrayRequest request = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response == null) {
                    Toast.makeText(getActivity(), "No!", Toast.LENGTH_LONG).show();
                    return;
                }

                List<Recipes> items = new Gson().fromJson(response.toString(), new TypeToken<List<Recipes>>() {
                }.getType());

                recipesList.clear();
                recipesList.addAll(items);

                mAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getActivity(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        MyApplication.getInstance().addToRequestQueue(request);
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        private int spanPrice;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanPrice, int spacing, boolean includeEdge) {
            this.spanPrice = spanPrice;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            int column = position % spanPrice;

            if (includeEdge){
                outRect.left = spacing - column * spacing / spanPrice;
                outRect.right = (column) * spacing / spanPrice;

                if (position < spanPrice){
                    outRect.top = spacing;
                }
                outRect.bottom = spacing;
            } else{
                outRect.left = column * spacing / spanPrice;
                outRect.right = spacing - (column + 1) * spacing / spanPrice;
                if (position >= spanPrice){
                    outRect.top = spacing;
                }
            }
        }
    }
//
    private int dpToPx(int dp){
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
//
    class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.MyViewHolder>{
        private Context context;
        private List<Recipes> recipesList;

        public class MyViewHolder extends RecyclerView.ViewHolder{
            public TextView title;
            public TextView price;
            public ImageView thumbnail;

            public MyViewHolder(View view){
                super(view);
                title = view.findViewById(R.id.nameProduct);
                price = view.findViewById(R.id.countProduct);
                thumbnail = view.findViewById(R.id.thumbnail);
            }
        }
//
        public StoreAdapter(Context context, List<Recipes> recipesList){
            this.context = context;
            this.recipesList = recipesList;
        }
//
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item_row, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position){
            final Recipes recipe = recipesList.get(position);
            holder.title.setText(recipe.getName());
            holder.price.setText(recipe.getCount());

            Glide.with(context)
                    .load(recipe.getImage())
                    .into(holder.thumbnail);
        }

        @Override
        public int getItemCount(){
            return recipesList.size();
        }
    }
}