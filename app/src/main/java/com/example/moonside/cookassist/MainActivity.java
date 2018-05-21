package com.example.moonside.cookassist;
//Застрял на импорте фрагментов

import android.app.ActionBar;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;


public class MainActivity extends AppCompatActivity {

    private ActionBar toolbar;
    private MenuItem navigateBar;
    android.support.v4.app.Fragment fragment;
    private String textVar = "";
    public int height = 0;
    MyApplication ms;

    public static class GlobalVars
    {
        public static int value;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//Настройка статус бара
        Window window = MainActivity.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.red_black_Statusbar));
//Инициализация App Bar
        Toolbar appbar = (Toolbar) findViewById(R.id.appbar);

//        SpannableString text = new SpannableString(textVar);
//        text.setSpan(new TypefaceSpan("assets/fonts/kiril.otf"), 0, text.length(),
//                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        setSupportActionBar(appbar);
//        appbar.setTitle(text);
//        toolbar = getSupportActionBar();



        BottomNavigationView navigation = findViewById(R.id.navBar);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();
//        layoutParams.setBehavior(new BottomNavigationBehavior());


        textVar = "Рецепты";
        SpannableString text = new SpannableString(textVar);
        text.setSpan(new TypefaceSpan("assets/fonts/kiril.otf"), 0, text.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        appbar.setTitle(text);
        fragment = new ProductFragment();
        loadFragment(fragment);

    }
//Создания пункта мемю в App Bar


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected( MenuItem item) {
            Toolbar appbar = (Toolbar) findViewById(R.id.appbar);
            setSupportActionBar(appbar);
            android.support.v4.app.Fragment fragment;
            switch (item.getItemId()){
                case R.id.navigation_recipes:
                    textVar = "Рецепты";
                    SpannableString text = new SpannableString(textVar);
                    text.setSpan(new TypefaceSpan("assets/fonts/kiril.otf"), 0, text.length(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    appbar.setTitle(text);
                    fragment = new RecipeFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_product:
                    textVar = "Продукты";
                    text = new SpannableString(textVar);
                    text.setSpan(new TypefaceSpan("assets/fonts/kiril.otf"), 0, text.length(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    appbar.setTitle(text);
                    fragment = new ProductFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_favorite:
                    textVar = "Любимые рецепты";
                    text = new SpannableString(textVar);
                    text.setSpan(new TypefaceSpan("assets/fonts/kiril.otf"), 0, text.length(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    appbar.setTitle(text);
                    fragment = new FavoriteFragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(android.support.v4.app.Fragment fragment) {
        // Загрузка фрагмента
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
