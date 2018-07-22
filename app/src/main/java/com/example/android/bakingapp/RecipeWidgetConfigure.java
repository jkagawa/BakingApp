package com.example.android.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.widget.RemoteViews;
import android.widget.Toast;

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

public class RecipeWidgetConfigure extends AppCompatActivity implements RecipeAdapter.RecipeAdapterOnClickHandler {

    private RecipeWidgetGridRemoteViewsFactory mRecipeWidgetGridRemoteViewsFactory;

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

    public static String mRecipeName;
    public static List<String> mIngredientQuantityList = new ArrayList<>();
    public static List<String> mIngredientMeasureList = new ArrayList<>();
    public static List<String> mIngredientNameList = new ArrayList<>();

    private int mAppWidgetId;

    private static final String PREFS_RECIPE_NAME_KEY = "recipe_name";

    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setResult(RESULT_CANCELED);

        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        ButterKnife.bind(this);
        //mRecyclerView = findViewById(R.id.recyclerview_recipes);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int gridSpan = 1;
        if(width > 1500) {
            gridSpan = 3;
        }

        GridLayoutManager layoutManager = new GridLayoutManager(this, gridSpan);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecipeAdapter = new RecipeAdapter(NUMBER_OF_ITEMS, this);

        mRecyclerView.setAdapter(mRecipeAdapter);

        new UrlQueryTask().execute(JSON_RESULT);
    }

    @Override
    public void onClick(int position) {

        final Context context = RecipeWidgetConfigure.this;

        mIngredientQuantityList = new ArrayList<>();
        mIngredientMeasureList = new ArrayList<>();
        mIngredientNameList = new ArrayList<>();

        mRecipeName = mRecipeNameList.get(position);

        Intent intent2 = new Intent(context, GridWidgetService.class);
        mRecipeWidgetGridRemoteViewsFactory = new RecipeWidgetGridRemoteViewsFactory(context, intent2);
        //mIngredientsRecyclerView.setAdapter(mRecipeWidgetGridRemoteViewsFactory);

        try {
            //JSONObject json = new JSONObject(jsonFromUrl);
            JSONArray json = new JSONArray(mIngredientsList.get(position));

            for(int i = 0; i<json.length(); i++) {
                JSONObject focus = json.getJSONObject(i);

                mIngredientQuantityList.add(focus.optString(DetailActivity.INGREDIENT_QUANTITY_KEY));
                mIngredientMeasureList.add(focus.optString(DetailActivity.INGREDIENT_MEASURE_KEY));
                mIngredientNameList.add(focus.optString(DetailActivity.INGREDIENT_NAME_KEY));

            }

            mRecipeWidgetGridRemoteViewsFactory.setData(mRecipeName, mIngredientQuantityList, mIngredientMeasureList, mIngredientNameList, mAppWidgetId);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider);

        for (int i=0; i<mIngredientNameList.size(); i++) {
            views.setRemoteAdapter(R.id.appwidget_recipe_ingredients_grid_view, intent2);
        }
        appWidgetManager.notifyAppWidgetViewDataChanged(mAppWidgetId, R.id.appwidget_recipe_ingredients_grid_view);

        saveRecipeNamePref(context, mAppWidgetId, mRecipeName);

        views.setTextViewText(R.id.appwidget_text, mRecipeName);
//
//        // Here we setup the intent which points to the AppLauncherWidgetViewService which will
//// provide the views for this collection.
//        Intent intent = new Intent(context, RecipeWidgetGridRemoteViewsFactory.class);
//        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
//// When intents are compared, the extras are ignored, so we need to embed the extras
//// into the data so that the extras will not be ignored.
//        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
//        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider);
//        rv.setRemoteAdapter(R.id.appwidget_recipe_ingredients_grid_view, intent);
//
//        appWidgetManager.notifyAppWidgetViewDataChanged(mAppWidgetId, R.id.appwidget_recipe_ingredients_grid_view);

        appWidgetManager.updateAppWidget(mAppWidgetId, views);

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();

    }

    static void saveRecipeNamePref(Context context, int appWidgetId, String recipeName) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor  = preferences.edit();
        editor.putString(PREFS_RECIPE_NAME_KEY, recipeName);
        editor.apply();
    }

    static String loadRecipeName(Context context, int appWidgetId) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String recipeName = preferences.getString(PREFS_RECIPE_NAME_KEY, "");
        if(!recipeName.equalsIgnoreCase(""))
        {
            return recipeName;
        } else {
            return context.getString(R.string.appwidget_text);
        }

//        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, 0);
//        String recipeName = preferences.getString(PREFS_RECIPE_NAME_KEY, null);
//        if (recipeName != null) {
//            return recipeName;
//        } else {
//            return context.getString(R.string.appwidget_text);
//        }
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
                    //JSONObject json = new JSONObject(jsonFromUrl);
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
