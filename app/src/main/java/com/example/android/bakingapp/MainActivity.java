package com.example.android.bakingapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.example.android.bakingapp.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.android.bakingapp.utilities.NetworkUtils.stringToURL;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.RecipeAdapterOnClickHandler {

    public final static String JSON_LINK = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    public final static URL JSON_RESULT = stringToURL(JSON_LINK);

    public static final String RECIPE_ID_KEY = "id";
    public static final String RECIPE_NAME_KEY = "name";
    public static final String INGREDIENTS_KEY = "ingredients";
    public static final String STEPS_KEY = "steps";
    public static final String SERVINGS_KEY = "servings";
    public static final String IMAGE_KEY = "image";

    private RecipeAdapter mRecipeAdapter;
    @BindView(R2.id.recyclerview_recipes) RecyclerView mRecyclerView;

    public static final int NUMBER_OF_ITEMS = 100;

    List<String> mRecipeIDList = new ArrayList<>();
    List<String> mRecipeNameList = new ArrayList<>();
    List<String> mIngredientsList = new ArrayList<>();
    List<String> mStepsList = new ArrayList<>();
    List<String> mServingsList = new ArrayList<>();
    List<String> mImageList = new ArrayList<>();

    public static final String EXTRA_RECIPE_ID_KEY = "EXTRA_RECIPE_ID";
    public static final String EXTRA_RECIPE_NAME_KEY = "EXTRA_RECIPE_NAME";
    public static final String EXTRA_INGREDIENT_KEY = "EXTRA_INGREDIENT";
    public static final String EXTRA_STEPS_KEY = "EXTRA_STEPS";
    public static final String EXTRA_SERVINGS_KEY = "EXTRA_SERVINGS";
    public static final String EXTRA_IMAGE_KEY = "EXTRA_IMAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int gridSpan = 1;
        if(width > 1500) {
            gridSpan = 2;
        }

        GridLayoutManager layoutManager = new GridLayoutManager(this, gridSpan);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecipeAdapter = new RecipeAdapter(NUMBER_OF_ITEMS, this);

        mRecyclerView.setAdapter(mRecipeAdapter);

        new UrlQueryTask().execute(JSON_RESULT);
    }

    @Override
    public void onClick(int position) {
        Intent movieDetailIntent = new Intent(MainActivity.this, DetailActivity.class);
        movieDetailIntent.putExtra(EXTRA_RECIPE_ID_KEY, mRecipeIDList.get(position));
        movieDetailIntent.putExtra(EXTRA_RECIPE_NAME_KEY, mRecipeNameList.get(position));
        movieDetailIntent.putExtra(EXTRA_INGREDIENT_KEY, mIngredientsList.get(position));
        movieDetailIntent.putExtra(EXTRA_STEPS_KEY, mStepsList.get(position));
        movieDetailIntent.putExtra(EXTRA_SERVINGS_KEY, mServingsList.get(position));
        movieDetailIntent.putExtra(EXTRA_IMAGE_KEY, mImageList.get(position));
        startActivity(movieDetailIntent);
    }

    public class UrlQueryTask extends AsyncTask<URL, Void, String> {
        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];

            String urlResults = null;
            try {
                urlResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);


            } catch (IOException e) {
                e.printStackTrace();
            }
            return urlResults;
        }

        @Override
        protected void onPostExecute(String jsonFromUrl) {

            if (jsonFromUrl != null && !jsonFromUrl.equals("")) {
                try {
                    JSONArray json = new JSONArray(jsonFromUrl);

                    for(int i = 0; i<json.length(); i++) {
                        JSONObject focus = json.getJSONObject(i);

                        mRecipeIDList.add(focus.optString(RECIPE_ID_KEY));
                        mRecipeNameList.add(focus.optString(RECIPE_NAME_KEY));
                        mIngredientsList.add(focus.optString(INGREDIENTS_KEY));
                        mStepsList.add(focus.optString(STEPS_KEY));
                        mServingsList.add(focus.optString(SERVINGS_KEY));
                        mImageList.add(focus.optString(IMAGE_KEY));
                    }

                    mRecipeAdapter.setData(mRecipeNameList, mImageList);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
