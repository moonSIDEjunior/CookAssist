package com.example.moonside.cookassist;

import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

public class AsyncTasksForRecipes {
    private RecipeDao recipeDao;

    public createAsyncTask CAT;
    public addAsyncTask AAT;
    public deleteAsyncTask DAT;
    public updateAsyncTask UAT;


    public AsyncTasksForRecipes(RecipeDao recipeDao) {
        this.recipeDao = recipeDao;
    }

    static class insertAsyncTask extends AsyncTask <Recipe,Void,Void> {

        private RecipeDao mAsyncTaskDao;

        insertAsyncTask(RecipeDao recipeDao) {
            mAsyncTaskDao = recipeDao;
        }

        @Override
        protected Void doInBackground(Recipe... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }

    }

    public void insert (Recipe recipe) {
        new insertAsyncTask(recipeDao).execute(recipe);
    }

    public static class createAsyncTask extends AsyncTask<Void, Void, List<Recipe>> {

        private RecipeDao mAsyncTaskDao;

        public createAsyncTask(RecipeDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Recipe> doInBackground(Void... voids) {
            List<Recipe> recipeList;
            recipeList = mAsyncTaskDao.getAll();
            return recipeList;
        }

        @Override
        protected void onPostExecute(List<Recipe> result) {
            super.onPostExecute(result);
        }

    }

    public static class addAsyncTask extends AsyncTask<Recipe, Void, Void> {

        private RecipeDao mAsyncTaskDao;

        public addAsyncTask(RecipeDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Recipe... recipes) {
            mAsyncTaskDao.insert(recipes);
            Log.w("", "");
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }

    }

    public static class deleteAsyncTask extends AsyncTask<Recipe , Void, Void> {

        private RecipeDao mAsyncTaskDao;

        public deleteAsyncTask(RecipeDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Recipe... recipe) {
            mAsyncTaskDao.delete(recipe);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }

    }

    public static class updateAsyncTask extends AsyncTask <String,Void,Void> {

        private RecipeDao mAsyncTaskDao;

        public updateAsyncTask(RecipeDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... strings) {
            mAsyncTaskDao.updateCount(strings[0], strings[2]);
            mAsyncTaskDao.updateName(strings[0], strings[1]);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }

    }


}
