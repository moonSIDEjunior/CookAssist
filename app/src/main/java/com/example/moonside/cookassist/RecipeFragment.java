package com.example.moonside.cookassist;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.MonthDisplayHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
//import com.example.moonside.cookassist.MyAdapter;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.concurrent.ExecutionException;

public class RecipeFragment extends Fragment implements View.OnClickListener {

    private Button AddBn, Delete, Upgrade, AddButton;
    private TextView mTextMessage, error;
    private EditText recipeName, productName, productCount;
    public static RecipeDatabase myAppDatabase;
    public static android.support.v4.app.FragmentManager fragmentManager;
    private RecipeDao myDao;
    private AsyncTasksForRecipes asyncTasks = new AsyncTasksForRecipes(myDao);
    private boolean z = false;
    private boolean b = false;
    private String textVar = "";
    public String products = "";
    private List<Recipe> recipes;
    private RecipeAdapter adapter;
    private RecyclerView mRecyclerView;
    private String tempCalorie;
    private SearchView searchView;
    private List<Recipe> singletonArray;


    private int tempParentPosition;
    private boolean tempCollapsed = false;
    //    private MyAdapter adapter;
    MyApplication ms;
    ArrayList<String[]> productJson;
    ArrayList<String> autoCompleteList;

    RecyclerView list;
    RecyclerView.LayoutManager layoutManager;

    public RecipeFragment() {

    }

    @Override
    public void onClick(View v) {

    }

    public static RecipeFragment newInstance() throws FileNotFoundException {
        RecipeFragment fragment = new RecipeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private void initSwipe(){

        final ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return true;
            }
            //TODO: Complete action on swipes!
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getItemViewType();
                adapter.collapseAllParents();
                tempParentPosition = viewHolder.getAdapterPosition();
                if (position == 1) {
                    String toastMsg = "Lest Parent";

                    final RecipeDatabase db = Room.databaseBuilder(getContext(),
                            RecipeDatabase.class, "RecipeDB_V0.1")
                            .build();
                    asyncTasks.DAT = new AsyncTasksForRecipes.deleteAsyncTask(myDao);
                    asyncTasks.DAT.execute(adapter.getName(tempParentPosition));

                    final Recipe tempRecipe = new Recipe(adapter.getName(tempParentPosition),
                            adapter.getList(tempParentPosition));
                    adapter.removeItem(tempParentPosition);

                    Snackbar snackbar = Snackbar.make(getActivity()
                            .findViewById(android.R.id.content), "Рецепт \"" +
                            tempRecipe.getRecipeName() + "\" удален!", Snackbar.LENGTH_SHORT);
                    snackbar.setAction("ОТМЕНИТЬ", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            adapter.restoreItem(
                                    tempParentPosition,
                                    tempRecipe);
                            if (!singletonArray.contains(tempRecipe)) {
                                asyncTasks.AAT = new AsyncTasksForRecipes.addAsyncTask(myDao);
                                asyncTasks.AAT.execute(tempRecipe);
                            }
                        }
                    });
                    View snackBarView = snackbar.getView();
                    Snackbar.SnackbarLayout.LayoutParams params = (Snackbar.SnackbarLayout.LayoutParams) snackBarView.getLayoutParams();
                    params.setMargins(0, 0, 0, (int) convertDpToPx(56));
                    snackBarView.setLayoutParams(params);
                    snackBarView.setElevation(0f);
                    snackbar.setActionTextColor(Color.YELLOW);
                    snackbar.show();
                }
                else {
                    Snackbar snackbar = Snackbar.make(getActivity()
                                    .findViewById(android.R.id.content),
                            "Сожалею, но пока это не работает", Snackbar.LENGTH_SHORT);
                    View snackBarView = snackbar.getView();
                    Snackbar.SnackbarLayout.LayoutParams params = (Snackbar.SnackbarLayout.LayoutParams) snackBarView.getLayoutParams();
                    params.setMargins(0, 0, 0, (int) convertDpToPx(56));
                    snackBarView.setLayoutParams(params);
                    snackBarView.setElevation(0f);
                    snackbar.setActionTextColor(Color.YELLOW);
                    snackbar.show();
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                Bitmap icon;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;
                    Paint p = new Paint();
                    if (viewHolder.getItemViewType() == 1) {
                        if (dX > 0) {
                            p.setColor(Color.parseColor("#EF5350"));
                            RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                            c.drawRect(background, p);
                            icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete);
                            RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                            c.drawBitmap(icon, null, icon_dest, p);
                        } else {
                            p.setColor(Color.parseColor("#EF5350"));
                            RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                            c.drawRect(background, p);
                            icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete);
                            RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                            c.drawBitmap(icon, null, icon_dest, p);
                        }
                    }
                    else {
                        if (dX > 0) {
                            p.setColor(Color.parseColor("#039BE5"));
                            RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                            c.drawRect(background,p);
                            icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_settings);
                            RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                            c.drawBitmap(icon,null,icon_dest,p);
                        } else {
                            p.setColor(Color.parseColor("#EF5350"));
                            RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                            c.drawRect(background, p);
                            icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete);
                            RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                            c.drawBitmap(icon, null, icon_dest, p);
                        }
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.recipe_fragment, container, false);

        final RecipeDatabase db = Room.databaseBuilder(getContext(), RecipeDatabase.class, "RecipeDB_V0.1")
                .build();

        singletonArray = RecipesSingleton.getInstance().getArray();
        LinearLayout ll = (LinearLayout) view.findViewById(R.id.linear_recipe_layout);
        //Set recycles layout height
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager()
                .getDefaultDisplay()
                .getRealMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        Log.w("Statusbar ", String.valueOf(getStatusBarHeight()));
        int tempWidth = displayMetrics.widthPixels;
        final int tempHeight = height - 2 * (int) convertDpToPx(56) - getStatusBarHeight();
//        ll.getLayoutParams().width = temp + convertDpToPx(900);
        ll.setLayoutParams(new FrameLayout.LayoutParams(tempWidth,tempHeight));


        myDao = db.getProductDao();
        asyncTasks.CAT = new AsyncTasksForRecipes.createAsyncTask(myDao);
        asyncTasks.CAT.execute();
        try {
            recipes = asyncTasks.CAT.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        for (Recipe item : singletonArray) {
            if (!recipes.contains(item)) {
                recipes.add(item);
            }
        }

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerviewAdapter);
        adapter = new RecipeAdapter(getActivity(), recipes);
        final RecyclerView.ViewHolder viewHolder;
        adapter.setExpandCollapseListener(new ExpandableRecyclerAdapter.ExpandCollapseListener() {
            @UiThread
            @Override
            public void onParentExpanded(int parentPosition) {
                Recipe expandedRecipe = recipes.get(parentPosition);
                tempParentPosition = parentPosition;
                tempCollapsed = true;

            }

            @UiThread
            @Override
            public void onParentCollapsed(int parentPosition) {
                Recipe collapsedRecipe = recipes.get(parentPosition);
                tempCollapsed = false;
            }

        });



        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);


        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
        initSwipe();
        return view;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        productJson = ms.getInstance().getArray();

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
//        searchView.setSubmitButtonEnabled(true);
        EditText txtSearch = ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text));
        txtSearch.setHint("Поиск..");
        txtSearch.setHintTextColor(getResources().getColor(R.color.red_black_Statusbar));
        txtSearch.setTextColor(getResources().getColor(R.color.red_black));


        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                adapter.getFilter().filter(query);
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                LayoutInflater li = LayoutInflater.from(getActivity());
                View promptsView = li.inflate(R.layout.addrecipe, null);
                final List<Product> products = new ArrayList<Product>();

                final AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(getActivity());

                mDialogBuilder.setView(promptsView);
                final EditText recipeNameInput = (EditText) promptsView.findViewById(R.id.input_recipe_name);
                final AutoCompleteTextView productNameInput = promptsView.findViewById(R.id.input_product_name);
                final EditText productCountInput = promptsView.findViewById(R.id.input_product_count);
                final TextView error = promptsView.findViewById(R.id.error_show);

                mDialogBuilder
                        .setCancelable(false)
                        .setNeutralButton("ОТМЕНА", null)
                        .setPositiveButton("ДОБАВИТЬ ПРОДУКТ", null)
                        .setNegativeButton("ГОТОВО", null);


                autoCompleteList = new ArrayList<String>();

                for (String[] temp : productJson) {
                    autoCompleteList.add(temp[0]);
                }

                productNameInput.setAdapter(new ArrayAdapter(getActivity(),
                        android.R.layout.simple_dropdown_item_1line, autoCompleteList));

                productNameInput.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        String tempName = productNameInput.getText().toString();
                        String tempCalories = null;
                        for (String[] temp : productJson) {
                            if (temp[0].equals(tempName)) {
                                tempCalories = temp[1];
                                break;
                            }
                        }
                        tempCalorie = tempCalories;
                    }
                });



                final AlertDialog alertDialog = mDialogBuilder.create();
                alertDialog.show();
                recipeNameInput.requestFocus();
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.dialog_btn));
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.dialog_btn));
                alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.dialog_btn));

                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String productname = productNameInput.getText().toString();
                        String productcount = productCountInput.getText().toString();
                        if (productname.equals("")) {
                            productNameInput.getBackground().setTint(getResources().getColor(R.color.md_red_700));
                            error.setText(R.string.enter_product_name);
                        } else {
                            {
                                productNameInput.getBackground().setTint(getResources().getColor(R.color.app_bar_red));
                                if (productcount.equals("")) {
                                    productCountInput.getBackground().setTint(getResources().getColor(R.color.md_red_700));
                                    error.setText(R.string.enter_product_count);
                                } else {
                                    productCountInput.getBackground().setTint(getResources().getColor(R.color.app_bar_red));
                                    try {
                                        products.add(new Product().ProductAll(
                                                productNameInput.getText().toString(),
                                                Integer.parseInt(productCountInput.getText().toString()),
                                                Integer.parseInt(tempCalorie)));
                                    }
                                    catch (NumberFormatException e) {
                                        products.add(new Product().ProductIntoReceipt(
                                                productNameInput.getText().toString(),
                                                Integer.parseInt(productCountInput.getText()
                                                        .toString())));
                                        Log.w("Adding", "True");
                                    }
                                    tempCalorie = null;

                                    productNameInput.setText("");
                                    productCountInput.setText("");
                                    recipeNameInput.setEnabled(false);
                                    Toast.makeText(getActivity(),  "\"" + productname + "\"" + " успешно добавлен!", Toast.LENGTH_SHORT).show();
                                    productNameInput.requestFocus();
                                }
                            }
                        }
                    }
                });

                alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final RecipeDatabase db = Room.databaseBuilder(getContext(), RecipeDatabase.class, "RecipeDB_V0.1")
                                .build();
                        String recipename = recipeNameInput.getText().toString();
                        if (recipename.equals("")) {
                            recipeNameInput.getBackground().setTint(getResources().getColor(R.color.md_red_700));
                            error.setText(R.string.enter_recipe_name);
                        } else {
                            recipeNameInput.getBackground().setTint(getResources().getColor(R.color.app_bar_red));
                            if (products.isEmpty()) {
                                if (productNameInput.getText().toString().equals("") & productCountInput.getText().toString().equals("")) {
                                    productNameInput.getBackground().setTint(getResources().getColor(R.color.md_red_700));
                                    productCountInput.getBackground().setTint(getResources().getColor(R.color.md_red_700));
                                    error.setText(R.string.enter_products);
                                } else if (productNameInput.getText().toString().equals("") & !productCountInput.getText().toString().equals("")) {
                                    productNameInput.getBackground().setTint(getResources().getColor(R.color.md_red_700));
                                    productCountInput.getBackground().setTint(getResources().getColor(R.color.app_bar_red));
                                    error.setText(R.string.enter_product_name);
                                } else if (!productNameInput.getText().toString().equals("") & productCountInput.getText().toString().equals("")) {
                                    productNameInput.getBackground().setTint(getResources().getColor(R.color.app_bar_red));
                                    productCountInput.getBackground().setTint(getResources().getColor(R.color.md_red_700));
                                    error.setText(R.string.enter_product_count);
                                } else if (!productNameInput.getText().toString().equals("") & !productCountInput.getText().toString().equals("")) {
                                    try {
                                        products.add(new Product().ProductAll(
                                                productNameInput.getText().toString(),
                                                Integer.parseInt(productCountInput.getText().toString()),
                                                Integer.parseInt(tempCalorie)));
                                    }
                                    catch (NumberFormatException e) {
                                        products.add(new Product().ProductIntoReceipt(
                                                productNameInput.getText().toString(),
                                                Integer.parseInt(productCountInput.getText()
                                                        .toString())));
                                        Log.w("Adding", "True");
                                    }
                                    tempCalorie = null;

                                    recipeNameInput.setEnabled(true);
                                    List<Product> localList = new ArrayList<>(products);
                                    final Recipe recipe = new Recipe(recipename, localList);

                                    myDao = db.getProductDao();
                                    asyncTasks.AAT = new AsyncTasksForRecipes.addAsyncTask(myDao);
                                    asyncTasks.AAT.execute(recipe);
                                    adapter.addItem(adapter.getItemCount(), recipe);

                                    mRecyclerView.setAdapter(adapter);

                                    Toast.makeText(getActivity(), "\"" + recipename + "\"" + " успешно добавлен!", Toast.LENGTH_SHORT).show();
                                    error.setText("");
                                    products.clear();
                                    alertDialog.dismiss();
                                } else {
                                    productNameInput.getBackground().setTint(getResources().getColor(R.color.md_red_700));
                                    productCountInput.getBackground().setTint(getResources().getColor(R.color.md_red_700));
                                    error.setText(R.string.enter_products);
                                }
                            } else {
                                if (!productNameInput.getText().toString().equals("") & productCountInput.getText().toString().equals("")) {
                                    productNameInput.getBackground().setTint(getResources().getColor(R.color.app_bar_red));
                                    productCountInput.getBackground().setTint(getResources().getColor(R.color.md_red_700));
                                    error.setText(R.string.enter_product_count);
                                } else if (productNameInput.getText().toString().equals("") & !productCountInput.getText().toString().equals("")) {
                                    productNameInput.getBackground().setTint(getResources().getColor(R.color.md_red_700));
                                    productCountInput.getBackground().setTint(getResources().getColor(R.color.app_bar_red));
                                    error.setText(R.string.enter_product_name);
                                } else if (!productNameInput.getText().toString().equals("") & !productCountInput.getText().toString().equals("")) {
                                    try {
                                        products.add(new Product().ProductAll(
                                                productNameInput.getText().toString(),
                                                Integer.parseInt(productCountInput.getText().toString()),
                                                Integer.parseInt(tempCalorie)));
                                    }
                                    catch (NumberFormatException e) {
                                        products.add(new Product().ProductIntoReceipt(
                                                productNameInput.getText().toString(),
                                                Integer.parseInt(productCountInput.getText()
                                                        .toString())));
                                        Log.w("Adding", "True");
                                    }
                                    tempCalorie = null;

                                    recipeNameInput.setEnabled(true);
                                    List<Product> localList = new ArrayList<>(products);
                                    final Recipe recipe = new Recipe(recipename, localList);

                                    myDao = db.getProductDao();
                                    asyncTasks.AAT = new AsyncTasksForRecipes.addAsyncTask(myDao);
                                    asyncTasks.AAT.execute(recipe);

                                    adapter.addItem(adapter.getItemCount(), recipe);
                                    mRecyclerView.setAdapter(adapter);

                                    Toast.makeText(getActivity(), "\"" + recipename + "\"" + " успешно добавлен!", Toast.LENGTH_SHORT).show();
                                    error.setText("");

                                    products.clear();
                                    alertDialog.dismiss();
                                } else {
                                    List<Product> localList = new ArrayList<>(products);
                                    final Recipe recipe = new Recipe(recipename, localList);

                                    myDao = db.getProductDao();
                                    asyncTasks.AAT = new AsyncTasksForRecipes.addAsyncTask(myDao);
                                    asyncTasks.AAT.execute(recipe);

                                    adapter.addItem(adapter.getItemCount(), recipe);
                                    mRecyclerView.setAdapter(adapter);

                                    recipeNameInput.setEnabled(true);

                                    Toast.makeText(getActivity(), "\"" + recipename + "\"" + " успешно добавлен!", Toast.LENGTH_SHORT).show();
                                    error.setText("");
                                    products.clear();
                                    alertDialog.dismiss();
                                }
                            }
                        }
                    }
                });
//mda
                alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.cancel();
                    }
                });
                return true;

            default:
                break;
        }

        return false;
    }


    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    private int convertDpToPx(int dp){
        return Math.round(dp*(getResources().getDisplayMetrics().xdpi/ DisplayMetrics.DENSITY_DEFAULT));

    }


//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.add:
//                LayoutInflater li = LayoutInflater.from(getActivity());
//                View promptsView = li.inflate(R.layout.addrecipe, null);
//                products = "";
//
//                final AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(getActivity());
//
//                mDialogBuilder.setView(promptsView);
//                final EditText recipeNameInput = (EditText) promptsView.findViewById(R.id.input_recipe_name);
//                final AutoCompleteTextView productNameInput = promptsView.findViewById(R.id.input_product_name);
//                final EditText productCountInput = promptsView.findViewById(R.id.input_product_count);
//                final TextView error = promptsView.findViewById(R.id.error_show);
//
//                mDialogBuilder
//                        .setCancelable(false)
//                        .setNeutralButton("Cancel", null)
//                        .setPositiveButton("+", null)
//                        .setNegativeButton("Add", null);
//
//                autoCompleteList = new ArrayList<String>();
//
//                for (String[] temp : productJson) {
//                    autoCompleteList.add(temp[0]);
//                }
//
//                productNameInput.setAdapter(new ArrayAdapter(getActivity(),
//                        android.R.layout.simple_dropdown_item_1line, autoCompleteList));
//
//
//                final AlertDialog alertDialog = mDialogBuilder.create();
//                alertDialog.show();
//                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        String productname = productNameInput.getText().toString();
//                        String productcount = productCountInput.getText().toString();
//                        if (productname.equals("")) {
//                            productNameInput.getBackground().setTint(getResources().getColor(R.color.md_red_700));
//                            error.setText(R.string.enter_product_name);
//                        } else {
//                            {
//                                productNameInput.getBackground().setTint(getResources().getColor(R.color.app_bar_red));
//                                if (productcount.equals("")) {
//                                    productCountInput.getBackground().setTint(getResources().getColor(R.color.md_red_700));
//                                    error.setText(R.string.enter_product_count);
//                                } else {
//                                    productCountInput.getBackground().setTint(getResources().getColor(R.color.app_bar_red));
//                                    products += productname + ": " + productcount + "\n";
//
//                                    productNameInput.setText("");
//                                    productCountInput.setText("");
//
//                                    Toast.makeText(getActivity(), "Product " + productname + " added successfully", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        }
//                    }
//                });
//
//                alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        final RecipeDatabase db = Room.databaseBuilder(getContext(), RecipeDatabase.class, "RecipeDB_V0.1")
//                                .build();
//                        String recipename = recipeNameInput.getText().toString();
//                        if (recipename.equals("")) {
//                            recipeNameInput.getBackground().setTint(getResources().getColor(R.color.md_red_700));
//                            error.setText(R.string.enter_recipe_name);
//                        } else {
//                            recipeNameInput.getBackground().setTint(getResources().getColor(R.color.app_bar_red));
//                            if (products.equals("")) {
//                                if (!productNameInput.getText().toString().equals("") & productCountInput.getText().toString().equals("")) {
//                                    productNameInput.getBackground().setTint(getResources().getColor(R.color.app_bar_red));
//                                    productCountInput.getBackground().setTint(getResources().getColor(R.color.md_red_700));
//                                    error.setText(R.string.enter_product_count);
//                                } else if (productNameInput.getText().toString().equals("") & !productCountInput.getText().toString().equals("")) {
//                                    productNameInput.getBackground().setTint(getResources().getColor(R.color.md_red_700));
//                                    productCountInput.getBackground().setTint(getResources().getColor(R.color.app_bar_red));
//                                    error.setText(R.string.enter_product_name);
//                                } else if (!productNameInput.getText().toString().equals("") & !productCountInput.getText().toString().equals("")) {
//                                    products += productNameInput.getText().toString() + ": " + productCountInput.getText().toString();
//                                    Recipe recipe = new Recipe(recipename, products);
//
//                                    myDao = db.myDao();
//                                    asyncTasks.AAT = new AsyncTasksForRecipes.addAsyncTask(myDao);
//                                    asyncTasks.AAT.execute(recipe);
//                                    adapter.addRecipe(adapter.getItemCount(), recipe);
//                                    list.setAdapter(adapter);
//
//                                    Toast.makeText(getActivity(), "" + recipename + " added successfully", Toast.LENGTH_SHORT).show();
//                                    error.setText("");
//
//                                    recipeNameInput.setText("");
//                                    productNameInput.setText("");
//                                    productCountInput.setText("");
//                                    recipeNameInput.getBackground().setTint(getResources().getColor(R.color.app_bar_red));
//                                    productNameInput.getBackground().setTint(getResources().getColor(R.color.app_bar_red));
//                                    productCountInput.getBackground().setTint(getResources().getColor(R.color.app_bar_red));
//                                    products = "";
//                                } else {
//                                    productNameInput.getBackground().setTint(getResources().getColor(R.color.md_red_700));
//                                    productCountInput.getBackground().setTint(getResources().getColor(R.color.md_red_700));
//                                    error.setText(R.string.enter_products);
//                                }
//                            } else {
//                                if (!productNameInput.getText().toString().equals("") & productCountInput.getText().toString().equals("")) {
//                                    productNameInput.getBackground().setTint(getResources().getColor(R.color.app_bar_red));
//                                    productCountInput.getBackground().setTint(getResources().getColor(R.color.md_red_700));
//                                    error.setText(R.string.enter_product_count);
//                                } else if (productNameInput.getText().toString().equals("") & !productCountInput.getText().toString().equals("")) {
//                                    productNameInput.getBackground().setTint(getResources().getColor(R.color.md_red_700));
//                                    productCountInput.getBackground().setTint(getResources().getColor(R.color.app_bar_red));
//                                    error.setText(R.string.enter_product_name);
//                                } else if (!productNameInput.getText().toString().equals("") & !productCountInput.getText().toString().equals("")) {
//                                    products += productNameInput.getText().toString() + ": " + productCountInput.getText().toString();
//                                    Recipe recipe = new Recipe(recipename, products);
//
//                                    myDao = db.myDao();
//                                    asyncTasks.AAT = new AsyncTasksForRecipes.addAsyncTask(myDao);
//                                    asyncTasks.AAT.execute(recipe);
//                                    adapter.addRecipe(adapter.getItemCount(), recipe);
//                                    list.setAdapter(adapter);
//
//                                    Toast.makeText(getActivity(), "" + recipename + " added successfully", Toast.LENGTH_SHORT).show();
//                                    error.setText("");
//
//                                    recipeNameInput.setText("");
//                                    productNameInput.setText("");
//                                    productCountInput.setText("");
//                                    recipeNameInput.getBackground().setTint(getResources().getColor(R.color.app_bar_red));
//                                    productNameInput.getBackground().setTint(getResources().getColor(R.color.app_bar_red));
//                                    productCountInput.getBackground().setTint(getResources().getColor(R.color.app_bar_red));
//                                    products = "";
//                                } else {
//                                    Recipe recipe = new Recipe(recipename, products);
//
//                                    myDao = db.myDao();
//                                    asyncTasks.AAT = new AsyncTasksForRecipes.addAsyncTask(myDao);
//                                    asyncTasks.AAT.execute(recipe);
//                                    adapter.addRecipe(adapter.getItemCount(), recipe);
//                                    list.setAdapter(adapter);
//
//                                    Toast.makeText(getActivity(), "" + recipename + " added successfully", Toast.LENGTH_SHORT).show();
//                                    error.setText("");
//
//                                    recipeNameInput.setText("");
//                                    productNameInput.setText("");
//                                    productCountInput.setText("");
//                                    recipeNameInput.getBackground().setTint(getResources().getColor(R.color.app_bar_red));
//                                    productNameInput.getBackground().setTint(getResources().getColor(R.color.app_bar_red));
//                                    productCountInput.getBackground().setTint(getResources().getColor(R.color.app_bar_red));
//                                    products = "";
//                                }
//                            }
//                        }
//                    }
//                });
////mda
//                alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        alertDialog.cancel();
//                    }
//                });
//                return true;
//
//            default:
//                break;
//        }
//
//        return false;
//    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        productJson = new ArrayList<>();
//        productJson = ms.getInstance().getArray();
//
//        View view = inflater.inflate(R.layout.recipe_fragment, container, false);
//
//        mTextMessage = (TextView) view.findViewById(R.id.message);
//        BottomNavigationView navigation = (BottomNavigationView) view.findViewById(R.id.navigation);
//
//        list = (RecyclerView) view.findViewById(R.id.recycler);
//
//        list.setHasFixedSize(true);
//        layoutManager = new LinearLayoutManager(getActivity());
//        list.setLayoutManager(layoutManager);
//
//        myAppDatabase = Room.databaseBuilder(getContext(), RecipeDatabase.class, "RecipeDB_V0.1")
//                .allowMainThreadQueries()
//                .build();
//
//        recipes = RecipeFragment.myAppDatabase.myDao().getRecipes();
//
//        final RecipeDatabase db = Room.databaseBuilder(getContext(), RecipeDatabase.class, "RecipeDB_V0.1")
//                .build();
//        List<Recipe> recipes = new ArrayList<>();
//        myDao = db.myDao();
//        asyncTasks.CAT = new AsyncTasksForRecipes.createAsyncTask(myDao);
//        asyncTasks.CAT.execute();
//        try {
//            recipes = asyncTasks.CAT.get();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//        adapter = new MyAdapter(recipes);
//
//        list.setAdapter(adapter);
//
//        return view;
//    }




//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        adapter.onRestoreInstanceState(savedInstanceState);
//    }
//
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        adapter.onSaveInstanceState(outState);
//    }

}
