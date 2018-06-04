package com.example.moonside.cookassist;


import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.io.FileNotFoundException;
import java.util.ArrayList;
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
    private MyAdapter adapter;

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

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                LayoutInflater li = LayoutInflater.from(getActivity());
                View promptsView = li.inflate(R.layout.addrecipe, null);
                products = "";

                final AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(getActivity());

                mDialogBuilder.setView(promptsView);
                final EditText recipeNameInput = (EditText) promptsView.findViewById(R.id.input_recipe_name);
                final EditText productNameInput = promptsView.findViewById(R.id.input_product_name);
                final EditText productCountInput = promptsView.findViewById(R.id.input_product_count);
                final TextView error = promptsView.findViewById(R.id.error_show);

                mDialogBuilder
                        .setCancelable(false)
                        .setNeutralButton("Cancel", null)
                        .setPositiveButton("+", null)
                        .setNegativeButton("Add", null);


                final AlertDialog alertDialog = mDialogBuilder.create();
                alertDialog.show();
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
                                    products += productname + ": " + productcount + "\n";

                                    productNameInput.setText("");
                                    productCountInput.setText("");

                                    Toast.makeText(getActivity(), "Product " + productname + " added successfully", Toast.LENGTH_SHORT).show();
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
                            if (products.equals("")) {
                                if (!productNameInput.getText().toString().equals("") & productCountInput.getText().toString().equals("")) {
                                    productNameInput.getBackground().setTint(getResources().getColor(R.color.app_bar_red));
                                    productCountInput.getBackground().setTint(getResources().getColor(R.color.md_red_700));
                                    error.setText(R.string.enter_product_count);
                                } else if (productNameInput.getText().toString().equals("") & !productCountInput.getText().toString().equals("")) {
                                    productNameInput.getBackground().setTint(getResources().getColor(R.color.md_red_700));
                                    productCountInput.getBackground().setTint(getResources().getColor(R.color.app_bar_red));
                                    error.setText(R.string.enter_product_name);
                                } else if (!productNameInput.getText().toString().equals("") & !productCountInput.getText().toString().equals("")) {
                                    products += productNameInput.getText().toString() + ": " + productCountInput.getText().toString();
                                    Recipe recipe = new Recipe(recipename, products);

                                    myDao = db.myDao();
                                    asyncTasks.AAT = new AsyncTasksForRecipes.addAsyncTask(myDao);
                                    asyncTasks.AAT.execute(recipe);
                                    adapter.addRecipe(adapter.getItemCount(), recipe);
                                    list.setAdapter(adapter);

                                    Toast.makeText(getActivity(), "" + recipename + " added successfully", Toast.LENGTH_SHORT).show();
                                    error.setText("");

                                    recipeNameInput.setText("");
                                    productNameInput.setText("");
                                    productCountInput.setText("");
                                    recipeNameInput.getBackground().setTint(getResources().getColor(R.color.app_bar_red));
                                    productNameInput.getBackground().setTint(getResources().getColor(R.color.app_bar_red));
                                    productCountInput.getBackground().setTint(getResources().getColor(R.color.app_bar_red));
                                    products = "";
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
                                    products += productNameInput.getText().toString() + ": " + productCountInput.getText().toString();
                                    Recipe recipe = new Recipe(recipename, products);

                                    myDao = db.myDao();
                                    asyncTasks.AAT = new AsyncTasksForRecipes.addAsyncTask(myDao);
                                    asyncTasks.AAT.execute(recipe);
                                    adapter.addRecipe(adapter.getItemCount(), recipe);
                                    list.setAdapter(adapter);

                                    Toast.makeText(getActivity(), "" + recipename + " added successfully", Toast.LENGTH_SHORT).show();
                                    error.setText("");

                                    recipeNameInput.setText("");
                                    productNameInput.setText("");
                                    productCountInput.setText("");
                                    recipeNameInput.getBackground().setTint(getResources().getColor(R.color.app_bar_red));
                                    productNameInput.getBackground().setTint(getResources().getColor(R.color.app_bar_red));
                                    productCountInput.getBackground().setTint(getResources().getColor(R.color.app_bar_red));
                                    products = "";
                                } else {
                                    Recipe recipe = new Recipe(recipename, products);

                                    myDao = db.myDao();
                                    asyncTasks.AAT = new AsyncTasksForRecipes.addAsyncTask(myDao);
                                    asyncTasks.AAT.execute(recipe);
                                    adapter.addRecipe(adapter.getItemCount(), recipe);
                                    list.setAdapter(adapter);

                                    Toast.makeText(getActivity(), "" + recipename + " added successfully", Toast.LENGTH_SHORT).show();
                                    error.setText("");

                                    recipeNameInput.setText("");
                                    productNameInput.setText("");
                                    productCountInput.setText("");
                                    recipeNameInput.getBackground().setTint(getResources().getColor(R.color.app_bar_red));
                                    productNameInput.getBackground().setTint(getResources().getColor(R.color.app_bar_red));
                                    productCountInput.getBackground().setTint(getResources().getColor(R.color.app_bar_red));
                                    products = "";
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_fragment, container, false);

        mTextMessage = (TextView) view.findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) view.findViewById(R.id.navigation);

        AddBn = (Button) view.findViewById(R.id.Add);
        list = (RecyclerView) view.findViewById(R.id.recycler);

        list.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        list.setLayoutManager(layoutManager);

        myAppDatabase = Room.databaseBuilder(getContext(), RecipeDatabase.class, "RecipeDB_V0.1")
                .allowMainThreadQueries()
                .build();

        recipes = RecipeFragment.myAppDatabase.myDao().getRecipes();

        final RecipeDatabase db = Room.databaseBuilder(getContext(), RecipeDatabase.class, "RecipeDB_V0.1")
                .build();
        List<Recipe> recipes = new ArrayList<>();
        myDao = db.myDao();
        asyncTasks.CAT = new AsyncTasksForRecipes.createAsyncTask(myDao);
        asyncTasks.CAT.execute();
        try {
            recipes = asyncTasks.CAT.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        adapter = new MyAdapter(recipes);

        list.setAdapter(adapter);

        return view;
    }

}