package com.example.moonside.cookassist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by moonSIDE on 26.03.2018.
 */

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.MyViewHolder> {
    private Context context;
    private List<Product> productList;

    public interface ClickListener {

        void onPositionClicked(int position);

        void onLongClicked(int position);
    }

    private final ClickListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        public TextView name, count, calories;
        public ImageView delete_image, settings_image;
        public RelativeLayout viewBackground, viewForeground;

        public MyViewHolder(View view){
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
            switch (v.getId()){
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

    public ProductListAdapter(Context context, List<Product> productList, ClickListener listener){
        this.context = context;
        this.productList = productList;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View productView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_product, parent, false);

        return new MyViewHolder(productView);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position){
        MyApplication.getInstance();
        MyApplication ms = null;
        Product product = productList.get(position);
//        final Product product = productList.get(position);
        holder.name.setText(product.getName());
        holder.count.setText("Количество: " + product.getCount().toString());
        holder.calories.setText("Калорийность: " + product.getCalories().toString());
    }

    public void removeItem(int position) {
        productList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, productList.size());
    }

    public void replaceItem(int position, Product product){
        productList.remove(position);
        productList.add(product);
        notifyItemInserted(position);
        notifyDataSetChanged();
        notifyItemRangeChanged(position, productList.size());
    }

    public void addItem(int position, Product product) {
        productList.add(product);
        notifyItemInserted(position);
        notifyDataSetChanged();
        notifyItemRangeChanged(position, productList.size());
    }

    final public void addItemWithoutList(int position, Product product) {
        productList.add(product);
        notifyItemInserted(position);
        notifyDataSetChanged();
        notifyItemRangeChanged(position, position);
    }

    public void restoreItem(Product product, int position){
        productList.add(product);

    }

    public String getField(String str, int position){
        String temp = "";
        switch (str) {
            case "name":
                temp = productList.get(position).getName();
                break;
            case "count":
                temp = String.valueOf(productList.get(position).getCount());
                break;
            case "calories":
                temp =  String.valueOf(productList.get(position).getCalories());
                break;
        }
        return temp;
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}



