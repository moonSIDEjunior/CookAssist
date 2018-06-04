package com.example.moonside.cookassist;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

//
///**
// * Created by moonSIDE on 21.03.2018.
// */
//
public class ProductFragment extends Fragment implements View.OnClickListener, SearchView.OnQueryTextListener {
    private static final String TAG = ProductFragment.class.getSimpleName();
    private static final String URL = "https://raw.githubusercontent.com/moonSIDEjunior/CookAssistant/master/productJson.json";
    private static final String FILE_NAME = "C:\\Users\\moonSIDE\\AndroidStudioProjects\\CookAssist\\app\\src\\main\\assets\\productJson.json";
    private RecyclerView recyclerView;
    List<Product> productList;
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
    private SearchView searchView;
    ArrayList<String> autoCompleteList;
    private SearchView addSearchView;
    ArrayList<String[]> productJson;


    //
//
    public ProductFragment() {

    }


    public static ProductFragment newInstance() throws FileNotFoundException {
        ProductFragment fragment = new ProductFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    //
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        asyncTask.PJS = new AsyncTasks.parseJSON();
        asyncTask.PJS.execute(loadJSONFromAsset());
        try {
            productJson = asyncTask.PJS.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Log.w("sdfdsf", productJson.get(3)[0]);
//        try {
//            JSONObject obj = new JSONObject(loadJSONFromAsset());
//            JSONArray m_jArry = obj.getJSONArray("products");
//            productJson = new ArrayList<HashMap<String, String >>();
//            HashMap<String, String > m_li;
//
//            for (int i = 0; i < m_jArry.length(); i++) {
//                JSONObject jo_inside = m_jArry.getJSONObject(i);
//                String product_name = jo_inside.getString("name");
//                String product_calories = jo_inside.getString("calories");
//
//                //Add your values in your `ArrayList` as below:
//                m_li = new HashMap<String, String >();
//                m_li.put("name", product_name);
//                m_li.put("calories", product_calories);
//
//                productJson.add(m_li);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }


    }

    @Override
    public void onClick(View v) {

    }
    //
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
//        getActivity().getMenuInflater().inflate(R.menu.menu_main, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mAdapter.getFilter().filter(query);
                return false;
            }
        });

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

                final AutoCompleteTextView productNameInput = (AutoCompleteTextView) promptsView.findViewById(R.id.product_name);
                final EditText productCountInput = (EditText) promptsView.findViewById(R.id.product_count);
                final EditText productCaloriesInput = (EditText) promptsView.findViewById(R.id.product_calories);
                autoCompleteList = new ArrayList<String>();

                for (String[] temp : productJson) {
                    autoCompleteList.add(temp[0]);
                }

                productNameInput.setAdapter(new ArrayAdapter(getActivity(),
                        android.R.layout.simple_dropdown_item_1line, autoCompleteList));

                productNameInput.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        String tempName = productNameInput.getText().toString();
                        String tempCalorie = null;
                        for (String[] temp : productJson) {
                            if (temp[0].equals(tempName)) {
                                tempCalorie = temp[1];
                                break;
                            }
                        }
                        productCaloriesInput.setText(tempCalorie);
                    }
                });

                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(final DialogInterface dialog) {
                        Button buttonPos = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                        Button buttonNeg = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
                        final TextView errorTextShow = (TextView) promptsView.findViewById(R.id.error_show);
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

            case R.id.search:
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_fragment, container, false);
        ProductDatabase db = Room.databaseBuilder(getContext(), ProductDatabase.class, "product_database_v0.3.2")
                .build();
//        List<Product> productList = new ArrayList<>();
        productDao = db.getProductDao();
        asyncTask.CAT = new AsyncTasks.createAsyncTask(productDao);
        asyncTask.CAT.execute();
        try {
            productList = asyncTask.CAT.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        recyclerView = view.findViewById(R.id.recycler_view_product);
        mAdapter = new ProductListAdapter(getActivity(), productList, new ProductListAdapter.ClickListener() {
            @Override
            public void onPositionClicked(int position) {
                return;
            }

            @Override
            public void onLongClicked(int position) {
                return;
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        mAdapter.notifyDataSetChanged();
        initSwipe();

        return view;
    }
    private void initSwipe(){

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT){
                    ProductDatabase db = Room.databaseBuilder(getContext(), ProductDatabase.class, "product_database_v0.3.2")
                            .build();
                    asyncTask.DAT = new AsyncTasks.deleteAsyncTask(productDao);
                    asyncTask.DAT.execute(mAdapter.getField("name", position));

                    final Product tempProduct = new Product().ProductAll(mAdapter.getField("name", position),
                            Integer.valueOf(mAdapter.getField("count", position)),
                            Integer.valueOf(mAdapter.getField("calories", position)));
                    mAdapter.removeItem(position);

                    Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), "Продукт \"" + tempProduct.getName() + "\" удален!", Snackbar.LENGTH_SHORT);
                    snackbar.setAction("ОТМЕНИТЬ", new View.OnClickListener(){
                        @Override
                        public void onClick(View view){
                            mAdapter.addItemWithoutList(
                                    mAdapter.getItemCount(),
                                    tempProduct);
                            asyncTask.AAT = new AsyncTasks.addAsyncTask(productDao);
                            asyncTask.AAT.execute(tempProduct);
                        }
                    });
                    View snackBarView = snackbar.getView();
                    Snackbar.SnackbarLayout.LayoutParams params = (Snackbar.SnackbarLayout.LayoutParams) snackBarView.getLayoutParams();
                    params.setMargins(0,0, 0, (int) convertDpToPx(56));
                    snackBarView.setLayoutParams(params);
                    snackBarView.setElevation(0f);
                    snackbar.setActionTextColor(Color.YELLOW);
                    snackbar.show();
                } else {
                    mAdapter.notifyDataSetChanged();
                    LayoutInflater li = LayoutInflater.from(getActivity());
                    LayoutInflater inflater = getLayoutInflater();
                    View titleView = inflater.inflate(R.layout.alert_title, null);
                    TextView title = titleView.findViewById(R.id.alert_title);
                    title.setText("Редактирование продутка");
                    final View promptsView = li.inflate(R.layout.product_entering, null);
                    final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                            .setView(promptsView)
                            .setPositiveButton(android.R.string.ok, null)
                            .setNegativeButton(android.R.string.cancel, null)
                            .setCustomTitle(titleView)
                            .create();

                    final AutoCompleteTextView productNameInput = (AutoCompleteTextView) promptsView.findViewById(R.id.product_name);
                    final EditText productCountInput = (EditText) promptsView.findViewById(R.id.product_count);
                    final EditText productCaloriesInput = (EditText) promptsView.findViewById(R.id.product_calories);
                    final TextView errorTextShow = (TextView) promptsView.findViewById(R.id.error_show);

                    productNameInput.setText(mAdapter.getField("name", position));
                    final String tempName = productNameInput.getText().toString();
                    productCountInput.setText(mAdapter.getField("count", position));
                    productCaloriesInput.setText(mAdapter.getField("calories", position));

                    productNameInput.setAdapter(new ArrayAdapter(getActivity(),
                            android.R.layout.simple_dropdown_item_1line, autoCompleteList));

                    productNameInput.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                            String tempName = productNameInput.getText().toString();
                            String tempCalorie = null;
                            for (String[] temp : productJson) {
                                if (temp[0].equals(tempName)) {
                                    tempCalorie = temp[1];
                                    break;
                                }
                            }
                            productCaloriesInput.setText(tempCalorie);
                        }
                    });



                    dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(final DialogInterface dialog) {
                            Button buttonPos = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                            Button buttonNeg = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
                            buttonPos.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
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

//                                        String newList[];
//                                        newList = new String[2];
//                                        newList[0] = newProductName;
//                                        newList[1] = newProductCount;
//                                        newList[2] = newProductCalories;

                                        asyncTask.UAT = new AsyncTasks.updateAsyncTask(productDao);
                                        asyncTask.UAT.execute(tempName, newProductName, newProductCount, newProductCalories);
                                        mAdapter.replaceItem(position, new Product().ProductAll(newProductName,
                                                Integer.valueOf(newProductCount),
                                                Integer.valueOf(newProductCalories)));
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
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setText("СОХРАНИТЬ ИЗМЕНЕНИЯ");
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if(dX > 0){
                        p.setColor(Color.parseColor("#039BE5"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_settings);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    } else {
                        p.setColor(Color.parseColor("#EF5350"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private int convertDpToPx(int dp){
        return Math.round(dp*(getResources().getDisplayMetrics().xdpi/ DisplayMetrics.DENSITY_DEFAULT));

    }


    private void removeView(){
        if(view.getParent()!=null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    protected boolean isAlwaysExpanded() {
        return true;
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("productCaloriesList");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
//
