package com.example.moonside.cookassist;

import android.app.AlertDialog;
import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


//
///**
// * Created by moonSIDE on 21.03.2018.
// */
//
public class ProductFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = ProductFragment.class.getSimpleName();
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
    private LiveData<List<Product>> mAllProducts;
    public ProductFragment() {

    }


    public static ProductFragment newInstance() throws FileNotFoundException {
        ProductFragment fragment = new ProductFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    //
    public void onCreate(Bundle savedInstanceState, Application application) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }

    @Override
    public void onClick(View v) {

    }
    //
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
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

                                    Product product = new Product();
                                    product.setName(newProductName);
                                    product.setCount(Integer.valueOf(newProductCount));
                                    product.setCalories(Integer.valueOf(newProductCalories));


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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_fragment, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_product);
        List<Product> productList = new ArrayList<>();

        try {
            InputStreamReader is = new InputStreamReader(getActivity().getAssets().open("product_db.csv"));
            BufferedReader reader = new BufferedReader(is);
            reader.readLine();
            String line;
            String[] st;
            while ((line = reader.readLine()) != null) {
                st = line.split(";");
                Product product = new Product();
                product.setId((Integer.valueOf(st[0])));
                product.setName(st[1]);
                product.setCount(Integer.valueOf(st[2]));
                product.setCalories(Integer.valueOf(st[3]));
                productList.add(product);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

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
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT){
//                    adapter.removeItem(position);
                    Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), "Вы должны заполнить поля!", Snackbar.LENGTH_SHORT);
                    snackbar.setAction("ОТМЕНИТЬ", new View.OnClickListener(){
                        @Override
                        public void onClick(View view){
//                    mAdapter.restoreProduct(deletedProduct, deletedIndex);
                        }
                    });
                    View snackBarView = snackbar.getView();
                    Snackbar.SnackbarLayout.LayoutParams params = (Snackbar.SnackbarLayout.LayoutParams)snackBarView.getLayoutParams();
                    params.setMargins(0,0, 0, (int) convertDpToPx(56));
                    snackBarView.setLayoutParams(params);
                    snackBarView.setElevation(0f);
                    snackbar.setActionTextColor(Color.YELLOW);
                    snackbar.show();
                } else {
//                    removeView();
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

                    final EditText productNameInput = (EditText) promptsView.findViewById(R.id.product_name);
                    final EditText productCountInput = (EditText) promptsView.findViewById(R.id.product_count);
                    final EditText productCaloriesInput = (EditText) promptsView.findViewById(R.id.product_calories);
                    final TextView errorTextShow = (TextView) promptsView.findViewById(R.id.error_show);

                    productNameInput.setText(mAdapter.getField("name", position));
                    productCountInput.setText(mAdapter.getField("count", position));
                    productCaloriesInput.setText(mAdapter.getField("calories", position));

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
//
//
////    @Override
////    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
////        ImageButton delete_bg = (ImageButton) getActivity().findViewById(R.id.delete_button_bg);
////        ImageButton settings_bg = (ImageButton) getActivity().findViewById(R.id.settings_button_bg);
////        delete_bg.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                Toast.makeText(getActivity(),
////                        "ImageButton is clicked!", Toast.LENGTH_SHORT).show();
////            }
////        });
////        if (viewHolder instanceof ProductListAdapter.MyViewHolder){
////            Log.w("fds", "dfsd");
//////            Log.w("ProdLIst: ", productList.toString());
////            Log.w("fds", "dfsd");
//////            HashMap deletedProduct = productList.get(viewHolder.getAdapterPosition());
//////            final int deletedIndex = viewHolder.getAdapterPosition();
////
////            mAdapter.removeProduct(viewHolder.getAdapterPosition());
////
////            Snackbar snackbar = Snackbar
////                    .make(coordinatorLayout, "Продукт удален!", Snackbar.LENGTH_LONG);
////            snackbar.setAction("ОТМЕНИТЬ", new View.OnClickListener(){
////                @Override
////                public void onClick(View view){
//////                    mAdapter.restoreProduct(deletedProduct, deletedIndex);
////                }
////            });
////
////            View snackBarView = snackbar.getView();
////            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams)snackbar.getView().getLayoutParams();
////            params.setMargins(0,0, 0, (int) convertDpToPx(56));
////            snackBarView.setLayoutParams(params);
////            snackbar.setActionTextColor(Color.YELLOW);
////            snackbar.show();
////        }
////    }
//
//    private int convertDpToPx(int dp) {
//        return Math.round(dp * (getResources().getDisplayMetrics().xdpi / DisplayMetrics.DENSITY_DEFAULT));
//
//    }


}
//
