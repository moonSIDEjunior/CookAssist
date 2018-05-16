package com.example.moonside.cookassist;

import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

public class AsyncTasks {
    private ProductDao productDao;

    public AsyncTasks(ProductDao productDao) {
        this.productDao = productDao;
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
            Log.w("", "");
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


}
