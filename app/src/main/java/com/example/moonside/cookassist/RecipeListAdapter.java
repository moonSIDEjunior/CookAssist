//package com.example.moonside.cookassist;
//
//import android.animation.ObjectAnimator;
//import android.arch.persistence.room.Room;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.support.design.widget.Snackbar;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.widget.RecyclerView;
//import android.util.DisplayMetrics;
//import android.util.SparseBooleanArray;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
//import com.github.aakira.expandablelayout.ExpandableLinearLayout;
//import com.github.aakira.expandablelayout.Utils;
//
//import java.util.List;
//
//class MyViewHolderWithoutChild extends RecyclerView.ViewHolder implements View.OnClickListener{
//
//    public TextView textView;
//
//    ItemClickListener itemClickListener;
//
//    public MyViewHolderWithoutChild(View itemView){
//        super(itemView);
//        textView = itemView.findViewById(R.id.textView);
//
//        itemView.setOnClickListener(this);
//    }
//
//    public void setItemClickListener(ItemClickListener itemClickListener){
//        this.itemClickListener = itemClickListener;
//    }
//
//    @Override
//    public void onClick(View view) {
//        itemClickListener.onClick(view, getAdapterPosition(),false);
//    }
//}
//
// class MyViewHolderWithChild extends RecyclerView.ViewHolder implements View.OnClickListener{
//
//    public TextView textView,textViewChild;
//    public RelativeLayout button;
//    public Button childDelete,childUpgrade, addRecipe;
//    public ExpandableLinearLayout expandableLayout;
//
//
//    ItemClickListener itemClickListener;
//
//    public MyViewHolderWithChild(View itemView){
//        super(itemView);
//        textView = itemView.findViewById(R.id.textView);
//        textViewChild = itemView.findViewById(R.id.textViewChild);
//        childDelete = itemView.findViewById(R.id.ChildDelete);
//        childUpgrade = itemView.findViewById(R.id.ChildUpgrade);
//        //addRecipe = itemView.findViewById(R.id.Add);
//        button = (RelativeLayout) itemView.findViewById(R.id.button);
//        expandableLayout = (ExpandableLinearLayout) itemView.findViewById(R.id.expandableLayout);
//
//        itemView.setOnClickListener(this);
//    }
//
//    public void setItemClickListener(ItemClickListener itemClickListener){
//        this.itemClickListener = itemClickListener;
//    }
//
//    @Override
//    public void onClick(View view) {
//        itemClickListener.onClick(view, getAdapterPosition(),false);
//    }
//}
//
//
//class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//
//    List<Recipe> recipes;
//    Context context;
//    SparseBooleanArray expandState = new SparseBooleanArray();
//    private RecipeDao mydao;
//    private AsyncTasksForRecipes asyncTasks = new AsyncTasksForRecipes(mydao);
////    private List<Recipe> recipeList;
////    private List<Recipe> recipeListFiltered;
////    SearchView searchView;
//
//    public MyAdapter(List<Recipe> recipes){
//        this.recipes = recipes;
//        for (int i=0;i<recipes.size();i++)
//            expandState.append(i,false);
//
//        notifyItemInserted(recipes.size());
//        notifyDataSetChanged();
//        notifyItemRangeChanged(recipes.size(), recipes.size());
//
//    }
//
//
//
//    //press ctrl+o
//
//
//    @Override
//    public int getItemViewType(int position) {
//        return 1;
//    }
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType){
//        this.context = parent.getContext();
//        if (viewType == 0) //Without items
//        {
//            LayoutInflater inflater = LayoutInflater.from(context);
//            View view = inflater.inflate(R.layout.layout_without_child,parent,false);
//            return new MyViewHolderWithoutChild(view);
//        }
//        else
//        {
//            LayoutInflater inflater = LayoutInflater.from(context);
//            View view = inflater.inflate(R.layout.layout_with_child,parent,false);
//            return new MyViewHolderWithChild(view);
//        }
//    }
//
//    @Override
//    public void onBindViewHolder (RecyclerView.ViewHolder holder, final int position){
//
//
//        switch (holder.getItemViewType())
//        {
//            case 0:
//            {
//                MyViewHolderWithoutChild viewHolder = (MyViewHolderWithoutChild) holder;
//                final Recipe recipe = recipes.get(position);
//                viewHolder.setIsRecyclable(false);
//                viewHolder.textView.setText(recipes.get(position).getRecipeName());
//
//                //Set Event
//                viewHolder.setItemClickListener(new ItemClickListener() {
//                    @Override
//                    public void onClick(View view, int position, boolean isLongClick) {
//                        Toast.makeText(context,  recipes.get(position).getRecipeName(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//            break;
//            case 1:
//            {
//                final MyViewHolderWithChild viewHolder = (MyViewHolderWithChild) holder;
//                final Recipe recipe = recipes.get(position);
//
//                final RecipeDatabase db = Room.databaseBuilder(context, RecipeDatabase.class,"RecipeDB_V0.1").build();
//                viewHolder.setIsRecyclable(false);
//                viewHolder.textView.setText(recipe.getRecipeName());
//                //Because we using Recycler View so we need use 'ExpandableLinearLayout'
//                viewHolder.expandableLayout.setInRecyclerView(true);
//                viewHolder.expandableLayout.setExpanded(expandState.get(position));
//                viewHolder.expandableLayout.setListener(new ExpandableLayoutListenerAdapter() {
//
//                    @Override
//                    public void onPreOpen() {
//                        changeRotate(viewHolder.button,0f,180f).start();
//                        expandState.put(position,true);
//                    }
//
//                    @Override
//                    public void onPreClose() {
//                        changeRotate(viewHolder.button,180f,0f).start();
//                        expandState.put(position,false);
//                    }
//
//                });
//
//                viewHolder.button.setRotation(expandState.get(position)?180f:0f);
//                viewHolder.button.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        //Expandable child item
//                        viewHolder.expandableLayout.toggle();
//                    }
//                });
//
//                viewHolder.textViewChild.setText(recipes.get(position).getProductName());
//
//                viewHolder.textViewChild.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Toast.makeText(context, "" + recipes.get(position).getProductName(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//                //Set Event
//                viewHolder.setItemClickListener(new ItemClickListener() {
//                    @Override
//                    public void onClick(View view, int position, boolean isLongClick) {
//                        Toast.makeText(context, ""+recipes.get(position).getRecipeName(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//                viewHolder.childDelete.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        final Recipe tempRecipe = recipe;
//                        final int pos = viewHolder.getAdapterPosition();
//
//                        mydao = db.myDao();
//                        asyncTasks.DAT = new AsyncTasksForRecipes.deleteAsyncTask(mydao);
//                        asyncTasks.DAT.execute(tempRecipe);
//
//                        removeRecipe(pos);
//
//                        Snackbar snackbar = Snackbar.make(view,"" + tempRecipe.getRecipeName() + " successfully deleted", Snackbar.LENGTH_SHORT);
//                        snackbar.setAction("ОТМЕНИТЬ", new View.OnClickListener(){
//                            @Override
//                            public void onClick(View view){
//                                //MainActivity.myAppDatabase.myDao().addRecipe(tempRecipe);
//                                mydao = db.myDao();
//                                asyncTasks.AAT = new AsyncTasksForRecipes.addAsyncTask(mydao);
//                                asyncTasks.AAT.execute(tempRecipe);
//
//                                recipes.add(pos,tempRecipe);
//                                notifyItemInserted(pos);
//                                notifyDataSetChanged();
//                                notifyItemRangeChanged(pos, recipes.size());
//                            }
//                        });
//
////                        View snackBarView = snackbar.getView();
////                        Snackbar.SnackbarLayout.LayoutParams params = (Snackbar.SnackbarLayout.LayoutParams) snackBarView.getLayoutParams();
////                        params.setMargins(0,0,0, (int) convertDpToPx(56,view));
////                        snackBarView.setLayoutParams(params);
////                        snackBarView.setElevation(0f);
////                        snackbar.setActionTextColor(Color.YELLOW);
////                        snackbar.show();
//                    }
//                });
//
//                viewHolder.childUpgrade.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        LayoutInflater li = LayoutInflater.from(context);
//                        View promptsView = li.inflate(R.layout.upgrade_layout, null);
//
//                        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);
//
//                        mDialogBuilder.setView(promptsView);
//                        final EditText updateRecipe = (EditText) promptsView.findViewById(R.id.input_new_recipe_name);
//                        final EditText updateProducts = (EditText) promptsView.findViewById(R.id.update_products);
//                        updateRecipe.setText(recipes.get(position).getRecipeName());
//                        updateProducts.setText(recipes.get(position).getProductName());
//                        final String oldrecipename = getField("name", viewHolder.getAdapterPosition());
//                        final String oldproducts = getField("count", viewHolder.getAdapterPosition());
//
//                        mDialogBuilder
//                                .setCancelable(false)
//                                .setPositiveButton("OK",
//                                        new DialogInterface.OnClickListener() {
//                                            public void onClick(DialogInterface dialog, int id) {
//                                                String newrecipename = updateRecipe.getText().toString();
//                                                String newproducts = updateProducts.getText().toString();
//                                                Recipe recipe1 = new Recipe(newrecipename,newproducts);
//
//                                                mydao = db.myDao();
//                                                asyncTasks.UAT = new AsyncTasksForRecipes.updateAsyncTask(mydao);
//                                                asyncTasks.UAT.execute(oldrecipename, newrecipename, newproducts);
////                                                RecipeDatabase.myDao().updateCount(oldproducts,newproducts);
////                                                RecipeDatabase.myDao().updateName(oldrecipename,newrecipename);
//                                                replaceItem(viewHolder.getAdapterPosition(),recipe1);
//                                            }
//                                        })
//                                .setNegativeButton("Cancel",
//                                        new DialogInterface.OnClickListener() {
//                                            public void onClick(DialogInterface dialog, int id) {
//                                                dialog.cancel();
//                                            }
//                                        });
//
//                        AlertDialog alertDialog = mDialogBuilder.create();
//                        alertDialog.show();
//
//                    }
//                });
//            }
//            break;
//            default:
//                break;
//        }
//    }
//
////
////    public Filter getFilter() {
////        return new Filter() {
////            @Override
////            protected FilterResults performFiltering(CharSequence charSequence) {
////                String charString = charSequence.toString();
////                if (charString.isEmpty()) {
////                    recipeListFiltered = recipeList;
////                } else {
////                    List<Recipe> filteredList = new ArrayList<>();
////                    for (Recipe row : recipeList) {
////                        if (row.getRecipeName().toLowerCase().contains(charString.toLowerCase())) {
////                            filteredList.add(row);
////                        }
////                    }
////
////                    recipeListFiltered = filteredList;
////                }
////
////                FilterResults filterResults = new FilterResults();
////                filterResults.values = recipeListFiltered;
////                return filterResults;
////            }
////
////            @Override
////            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
////                recipeListFiltered= (ArrayList<Recipe>) filterResults.values;
////                notifyDataSetChanged();
////            }
////        };
////    }
//
//
//    public void removeRecipe(int position) {
//        recipes.remove(position);
//        expandState.put(position,false);
//        notifyItemRemoved(position);
//        notifyDataSetChanged();
//        notifyItemRangeChanged(position, recipes.size());
//    }
//
//    public void addRecipe(int position, Recipe recipe) {
//        recipes.add(recipe);
//        notifyItemInserted(position);
//        notifyDataSetChanged();
//        notifyItemRangeChanged(position, recipes.size());
//    }
//
//    public void replaceItem(int position, Recipe recipe){
//        recipes.remove(position);
//        recipes.add(position,recipe);
//        notifyItemInserted(position);
//        notifyDataSetChanged();
//        notifyItemRangeChanged(position, recipes.size());
//    }
//
//
//    public String getField(String str, int position){
//        String temp = "";
//        switch (str) {
//            case "name":
//                temp = recipes.get(position).getRecipeName();
//                break;
//            case "count":
//                temp = String.valueOf(recipes.get(position).getProductName());
//                break;
//        }
//        return temp;
//    }
//
//    private ObjectAnimator changeRotate(RelativeLayout button, float from, float to) {
//        ObjectAnimator animator = ObjectAnimator.ofFloat(button, "rotation",from,to);
//        animator.setDuration(200);
//        animator.setInterpolator(Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR));
//        return animator;
//    }
//
//    @Override
//    public int getItemCount()
//    {
//        return recipes.size();
//    }
//
//    private int convertDpToPx(int dp, View view){
//        return Math.round(dp*(view.getResources().getDisplayMetrics().xdpi/ DisplayMetrics.DENSITY_DEFAULT));
//
//    }
//
//}
