package com.example.moonside.cookassist;

import android.app.AlertDialog;
import android.arch.persistence.room.Room;
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
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
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
import java.util.concurrent.ExecutionException;

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
    createAsyncTask CAT;
    addAsyncTask AAT;
    deleteAsyncTask DAT;
    updateAsyncTask UAT;


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
                                final ProductDatabase db = Room.databaseBuilder(getContext(), ProductDatabase.class, "product_database")
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
                                    AAT = new addAsyncTask(productDao);
                                    final Product tempProduct = new Product().ProductAll(recyclerView.getAdapter().getItemCount(), newProductName,
                                            Integer.valueOf(newProductCount),
                                            Integer.valueOf(newProductCalories));
                                    AAT.execute(tempProduct);
                                    Log.w("Product_add_id", tempProduct.getId().toString());


                                    mAdapter.addItem(mAdapter.getItemCount(), new Product().ProductAll(recyclerView.getAdapter().getItemCount() - 1, newProductName,
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favorite_fragment, container, false);
                ProductDatabase db = Room.databaseBuilder(getContext(), ProductDatabase.class, "product_database")
                .build();
        List<Product> productList = new ArrayList<>();
        productDao = db.getProductDao();
        CAT = new createAsyncTask(productDao);
        CAT.execute();
        try {
            productList = CAT.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        recyclerView = view.findViewById(R.id.recycler_view_product);

//        ProductDatabase db = Room.databaseBuilder(getContext(), ProductDatabase.class, "product_database")
//                .allowMainThreadQueries()
//                .build();
//        productList = db.getProductDao().getAll();

//        try {
//            InputStreamReader is = new InputStreamReader(getActivity().getAssets().open("product_db.csv"));
//            BufferedReader reader = new BufferedReader(is);
//            reader.readLine();
//            String line;
//            String[] st;
//            while ((line = reader.readLine()) != null) {
//                st = line.split(";");
//                Product product = new Product();
//                product.setId((Integer.valueOf(st[0])));
//                product.setName(st[1]);
//                product.setCount(Integer.valueOf(st[2]));
//                product.setCalories(Integer.valueOf(st[3]));
//                productList.add(product);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

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
                    ProductDatabase db = Room.databaseBuilder(getContext(), ProductDatabase.class, "product_database")
                            .build();
                    DAT = new deleteAsyncTask(productDao);
                    DAT.execute(mAdapter.getField("name", position));
                    final Product tempProduct = new Product().ProductAll(position, mAdapter.getField("name", position),
                            Integer.valueOf(mAdapter.getField("count", position)),
                            Integer.valueOf(mAdapter.getField("calories", position)));
                    mAdapter.removeItem(position);

                    Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), "Вы должны заполнить поля!", Snackbar.LENGTH_SHORT);
                    snackbar.setAction("ОТМЕНИТЬ", new View.OnClickListener(){
                        @Override
                        public void onClick(View view){
                            mAdapter.addItem(position - 1, tempProduct);
                            Log.w("Product_restore_id", tempProduct.getId().toString());
                            AAT = new addAsyncTask(productDao);
                            AAT.execute(tempProduct);
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

                    final EditText productNameInput = (EditText) promptsView.findViewById(R.id.product_name);
                    final EditText productCountInput = (EditText) promptsView.findViewById(R.id.product_count);
                    final EditText productCaloriesInput = (EditText) promptsView.findViewById(R.id.product_calories);
                    final TextView errorTextShow = (TextView) promptsView.findViewById(R.id.error_show);

                    productNameInput.setText(mAdapter.getField("name", position));
                    final String tempName = productNameInput.getText().toString();
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

//                                        String newList[];
//                                        newList = new String[2];
//                                        newList[0] = newProductName;
//                                        newList[1] = newProductCount;
//                                        newList[2] = newProductCalories;

                                        UAT = new updateAsyncTask(productDao);
                                        UAT.execute(tempName, newProductName, newProductCount, newProductCalories);
                                        mAdapter.replaceItem(position, new Product().ProductAll(recyclerView.getAdapter().getItemCount(), newProductName,
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

    private static class insertAsyncTask extends AsyncTask<Product, Void, Void> {

        private ProductDao mAsyncTaskDao;

        insertAsyncTask(ProductDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Product... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }

    }

    public void insert (Product product) {
        new insertAsyncTask(productDao).execute(product);
    }

    private static class createAsyncTask extends AsyncTask<Void, Void, List<Product>> {

        private ProductDao mAsyncTaskDao;

        createAsyncTask(ProductDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Product> doInBackground(Void... voids) {
            List<Product> productList;
            productList = mAsyncTaskDao.getAll();
            return productList;
        }

        @Override
        protected void onPostExecute(List<Product> result) {
            super.onPostExecute(result);
        }

    }

    private static class addAsyncTask extends AsyncTask<Product, Void, Void> {

        private ProductDao mAsyncTaskDao;

        addAsyncTask(ProductDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Product... products) {
            mAsyncTaskDao.insert(products);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }

    }

    private static class deleteAsyncTask extends AsyncTask<String , Void, Void> {

        private ProductDao mAsyncTaskDao;

        deleteAsyncTask(ProductDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... name) {
            mAsyncTaskDao.deleteByName(name);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }

    }

    private static class updateIdsAsyncTask extends AsyncTask< , Void, Void> {

        private ProductDao mAsyncTaskDao;

        updateIdsAsyncTask(ProductDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(List<Product>... ids) {
            mAsyncTaskDao.updateIds(ids.);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }

    }

    private static class updateAsyncTask extends AsyncTask<String, Void, Void> {

        private ProductDao mAsyncTaskDao;

        updateAsyncTask(ProductDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... strings) {
            mAsyncTaskDao.updateCount(strings[0], strings[2]);
            mAsyncTaskDao.updateCalories(strings[0], strings[3]);
            mAsyncTaskDao.updateName(strings[0], strings[1]);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }

    }


    private void removeView(){
        if(view.getParent()!=null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }



            }
//
