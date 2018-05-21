package com.example.moonside.cookassist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by moonSIDE on 26.03.2018.
 */

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private List<Product> productList;
    private List<Product> productListFiltered;

    public interface ClickListener {

        void onPositionClicked(int position);

        void onLongClicked(int position);
    }

    private final ClickListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public TextView name, count, calories;
        public ImageView delete_image, settings_image;
        public RelativeLayout viewBackground, viewForeground;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            count = view.findViewById(R.id.count);
            calories = view.findViewById(R.id.calories);
            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);
            delete_image = view.findViewById(R.id.delete_button_bg);
            settings_image = view.findViewById(R.id.settings_button_bg);

            delete_image.setOnClickListener(this);
            settings_image.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.delete_button_bg:
                    Log.w("fdfsdfsdfds", "sdfsdfsdfds");
                    break;
                case R.id.settings_button_bg:
                    break;
            }

        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }

    public ProductListAdapter(Context context, List<Product> productList, ClickListener listener) {
        this.context = context;
        this.productList = productList;
        this.productListFiltered = productList;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View productView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_product, parent, false);

        return new MyViewHolder(productView);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Product product = productListFiltered.get(position);
//        final Product product = productList.get(position);
        holder.name.setText(product.getName());
        holder.count.setText("Количество: " + product.getCount().toString());
        holder.calories.setText("Калорийность: " + product.getCalories().toString());
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    productListFiltered = productList;
                } else {
                    List<Product> filteredList = new ArrayList<>();
                    for (Product row : productList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    productListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = productListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                productListFiltered = (ArrayList<Product>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public void removeItem(int position) {
        productListFiltered.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, productList.size());
    }

    public void replaceItem(int position, Product product){
        productListFiltered.remove(position);
        productListFiltered.add(product);
        notifyItemInserted(position);
        notifyDataSetChanged();
        notifyItemRangeChanged(position, productListFiltered.size());
    }

    public void addItem(int position, Product product) {
        productListFiltered.add(product);
        notifyItemInserted(position);
        notifyDataSetChanged();
        notifyItemRangeChanged(position, productList.size());
    }

    final public void addItemWithoutList(int position, Product product) {
        productListFiltered.add(product);
        notifyItemInserted(position);
        notifyDataSetChanged();
        notifyItemRangeChanged(position, position + 1);
    }

    public void restoreItem(Product product, int position){
        productListFiltered.add(product);

    }

    public String getField(String str, int position){
        String temp = "";
        switch (str) {
            case "name":
                temp = productListFiltered.get(position).getName();
                break;
            case "count":
                temp = String.valueOf(productListFiltered.get(position).getCount());
                break;
            case "calories":
                temp =  String.valueOf(productListFiltered.get(position).getCalories());
                break;
        }
        return temp;
    }

    @Override
    public int getItemCount() {
        return productListFiltered.size();
    }


}



