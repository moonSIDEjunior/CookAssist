package com.example.moonside.cookassist;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

//
///**
// * Created by moonSIDE on 21.03.2018.
// */
//
public class FavoriteFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = FavoriteFragment.class.getSimpleName();
    private static final String URL = "https://raw.githubusercontent.com/moonSIDEjunior/CookAssistant/master/productJson.json";
    private static final String FILE_NAME = "C:\\Users\\moonSIDE\\AndroidStudioProjects\\CookAssist\\app\\src\\main\\assets\\productJson.json";
    private RecyclerView recyclerView;
    private List<Product> productList;
    private ProductListAdapter mAdapter;
    private CoordinatorLayout coordinatorLayout;
    private String direction = "";
    private ConstraintLayout constraintLayout;
    private AlertDialog productEnterinLayout;
    private ArrayAdapter<Product> adapter;
    private EditText nameText, priceText;
    InputStream targetStream;
    private int navBar;
    private EditText et_country;
    private int edit_position;
    private View view;
    private boolean add = false;
    private Paint p = new Paint();
    private ProductDatabase db;
    private ProductDao productDao;
    AsyncTasks asyncTask = new AsyncTasks(productDao);



    //
//
    public FavoriteFragment() {

    }



    public static FavoriteFragment newInstance() throws FileNotFoundException {
        FavoriteFragment fragment = new FavoriteFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
//
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }

    @Override
    public void onClick(View v) {

    }
//
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
                LayoutInflater inflater = getLayoutInflater();
                View titleView = inflater.inflate(R.layout.alert_title, null);
                final View promptsView = li.inflate(R.layout.product_entering, null);
                final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setView(promptsView)
                        .setPositiveButton(android.R.string.ok, null)
                        .setNegativeButton(android.R.string.cancel, null)
                        .setCustomTitle(titleView)
                        .create();

                final EditText productNameInput = (EditText) promptsView.findViewById(R.id.product_name);
                final EditText productCountInput = (EditText) promptsView.findViewById(R.id.product_count);
                final EditText productCaloriesInput = (EditText) promptsView.findViewById(R.id.product_calories);
                final TextView errorTextShow = (TextView) promptsView.findViewById(R.id.error_show);

                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(final DialogInterface dialog) {
                        Button buttonPos = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                        Button buttonNeg = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
                        buttonPos.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final ProductDatabase db = Room.databaseBuilder(getContext(), ProductDatabase.class, "product_database_v0.3.2")
                                        .build();
                                if (productCountInput.getText().toString().equals("") || productNameInput.getText().toString().equals("") || productCaloriesInput.getText().toString().equals("")) {
                                    if (productNameInput.getText().toString().equals("")) productNameInput.getBackground().setTint(getResources().getColor(R.color.md_red_700));
                                    else productNameInput.getBackground().setTint(getResources().getColor(R.color.app_bar_red));
                                    if (productCountInput.getText().toString().equals("")) productCountInput.getBackground().setTint(getResources().getColor(R.color.md_red_700));
                                    else productCountInput.getBackground().setTint(getResources().getColor(R.color.app_bar_red));
                                    if (productCaloriesInput.getText().toString().equals("")) productCaloriesInput.getBackground().setTint(getResources().getColor(R.color.md_red_700));
                                    else productCaloriesInput.getBackground().setTint(getResources().getColor(R.color.app_bar_red));
                                    errorTextShow.setText("Заполните подсвеченные поля!");

                                } else {
                                    String newProductName = productNameInput.getText().toString();
                                    String newProductCount = productCountInput.getText().toString();
                                    String newProductCalories = productCaloriesInput.getText().toString();


                                    productDao = db.getProductDao();
                                    asyncTask.AAT = new AsyncTasks.addAsyncTask(productDao);
                                    final Product tempProduct = new Product().ProductAll(newProductName,
                                            Integer.valueOf(newProductCount),
                                            Integer.valueOf(newProductCalories));
                                    asyncTask.AAT.execute(tempProduct);

                                    Log.w("","");
                                    mAdapter.addItem(mAdapter.getItemCount(), new Product().ProductAll(newProductName,
                                            Integer.valueOf(newProductCount),
                                            Integer.valueOf(newProductCalories)));
                                    recyclerView.setAdapter(mAdapter);
                                    Log.w("mAdapter_count_add", String.valueOf(mAdapter.getItemCount()));
                                    Log.w("recyclerView_count_add", String.valueOf(recyclerView.getAdapter().getItemCount()));
                                    dialog.dismiss();
                                }
                            }
                        });
                        buttonNeg.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.cancel();
                            }
                        });
                    }
                });

                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.dialog_btn));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setText("ОТМЕНА");
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.dialog_btn));
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setText("ДОБАВИТЬ");
                return true;

            default:
                break;
        }

        return false;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favorite_fragment, container, false);
        TextView TEST_TEXT = view.findViewById(R.id.TEST_TEXT);
        TEST_TEXT.setText("Нет рецептов");
        List<Product> tempList = ListSingleton.getInstance().getArray();
        List<Recipe> tempRecipes = RecipesSingleton.getInstance().getArray();

        /*
        * Поиск рецептов, которые есть возможность приготовить
        */

        for(int i = 0; i < tempRecipes.size(); i++) {
            int recipeSize = tempRecipes.get(i).getChildList().size();
            int tempSize = 0;
            int flag = 0;
            ArrayList temp = new ArrayList();
            for(int k = 0; k < tempRecipes.get(i).getChildList().size(); k++) {
                temp.add(tempRecipes.get(i).getChildList().get(k).getName());
            }
            String lackMessage = "";
            for(int j = 0; j < tempList.size(); j++) {
                String message = tempRecipes.get(i).findInNames(tempList.get(j).getName(),
                        tempList.get(j).getCount());
                if (temp.contains(message)) {
                    tempSize++;
                    temp.remove(message);
                }
                else
                    if (message == "Нет совпадения") {
                    }
                    else {
                        lackMessage = message;
                        if (flag == 0)
                            flag = 1;
                        else
                            flag = 2;
                    }
            }
            if (temp.isEmpty())
                TEST_TEXT.setText("Вы можете реализовать рецепт \"" +
                        tempRecipes.get(i).getRecipeName()
                        + "\".");
            else
                if (tempSize >= (recipeSize - 1))
                    if (lackMessage.equals("")) {
                        for(int l = 0; l < tempSize; l++) {
                            if (temp.contains(tempRecipes.get(i).getChildList().get(l).getName()))
                                TEST_TEXT.setText("Вы можете реализовать рецепт \"" + tempRecipes
                                        .get(i).getRecipeName() + "\", но " + "Вам нехватает " +
                                        String.valueOf(tempRecipes.get(i).getChildList()
                                                .get(l).getCount()) + " грамм продукта \"" +
                                                    temp.get(0));
                            break;
                        }
                    }
                    else
                        TEST_TEXT.setText("Вы можете реализовать рецепт \"" + tempRecipes.get(i)
                            .getRecipeName() + "\", но " + lackMessage);
                if (tempSize < (recipeSize - 1))
                    TEST_TEXT.setText("Ничего вы не можете, Джон Сноу!");
        }
        return view;
    }
}
//
