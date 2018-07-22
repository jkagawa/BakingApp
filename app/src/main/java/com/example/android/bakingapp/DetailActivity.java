package com.example.android.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.FragmentManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.example.android.bakingapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Joshua on 7/6/2018.
 */

public class DetailActivity extends AppCompatActivity implements StepsAdapter.StepsAdapterOnClickHandler {

    public static String mRecipeID;
    public static String mRecipeName;
    public static String mIngredients;
    public static String mSteps;
    public static String mServings;
    public static String mImage;

    private FrameLayout mFragment;

    public static final String INGREDIENT_QUANTITY_KEY = "quantity";
    public static final String INGREDIENT_MEASURE_KEY = "measure";
    public static final String INGREDIENT_NAME_KEY = "ingredient";
    public static final String STEPS_ID_KEY = "id";
    public static final String STEPS_SHORT_DESCRIPTION_KEY = "shortDescription";
    public static final String STEPS_DESCRIPTION_KEY = "description";
    public static final String STEPS_VIDEO_URL_KEY = "videoURL";
    public static final String STEPS_THUMBNAIL_URL_KEY = "thumbnailURL";


    public static List<String> mIngredientQuantityList = new ArrayList<>();
    public static List<String> mIngredientMeasureList = new ArrayList<>();
    public static List<String> mIngredientNameList = new ArrayList<>();
    public static List<String> mStepsIDList = new ArrayList<>();
    public static List<String> mStepsShortDescriptionList = new ArrayList<>();
    public static List<String> mStepsDescriptionList = new ArrayList<>();
    public static List<String> mStepsVideoURLList = new ArrayList<>();
    public static List<String> mStepsThumbnailURLList = new ArrayList<>();

    public static String mStepsID;
    public static String mStepsShortDescription;
    public static String mStepsDescription;
    public static String mStepsVideoURL;
    public static String mStepsThumbnailURL;

    private IngredientsAdapter mIngredientsAdapter;
    private StepsAdapter mStepsAdapter;

    @BindView(R2.id.detail_servings) TextView mServingsView;
    @BindView(R2.id.recyclerview_ingredients) RecyclerView mIngredientsRecyclerView;
    @BindView(R2.id.recyclerview_steps) RecyclerView mStepsRecyclerView;

    public static final String EXTRA_STEPS_ID_KEY = "EXTRA_STEPS_ID";
    public static final String EXTRA_STEPS_SHORT_DESCRIPTION_KEY = "EXTRA_STEPS_SHORT_DESCRIPTION";
    public static final String EXTRA_STEPS_DESCRIPTION_KEY = "EXTRA_STEPS_DESCRIPTION";
    public static final String EXTRA_STEPS_VIDEO_URL_KEY = "EXTRA_STEPS_VIDEO_URL";
    public static final String EXTRA_STEPS_THUMBNAIL_URL_KEY = "EXTRA_STEPS_THUMBNAIL_URL";

    private Toast mToast;

    public static boolean mTwoPane;
    StepsFragment stepsFragment;
    public static int mPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        //mServingsView = findViewById(R.id.detail_servings);
        //mIngredientsRecyclerView = findViewById(R.id.recyclerview_ingredients);
        //mStepsRecyclerView = findViewById(R.id.recyclerview_steps);

        Intent intentFromMainActivity = getIntent();

        Bundle extras = getIntent().getExtras();

        if(extras != null || savedInstanceState != null) {

            mIngredientQuantityList = new ArrayList<>();
            mIngredientMeasureList = new ArrayList<>();
            mIngredientNameList = new ArrayList<>();
            mStepsIDList = new ArrayList<>();
            mStepsShortDescriptionList = new ArrayList<>();
            mStepsDescriptionList = new ArrayList<>();
            mStepsVideoURLList = new ArrayList<>();
            mStepsThumbnailURLList = new ArrayList<>();

            if(extras != null) {
                mRecipeID = intentFromMainActivity.getStringExtra(MainActivity.EXTRA_RECIPE_ID_KEY);
                mRecipeName = intentFromMainActivity.getStringExtra(MainActivity.EXTRA_RECIPE_NAME_KEY);
                mIngredients = intentFromMainActivity.getStringExtra(MainActivity.EXTRA_INGREDIENT_KEY);
                mSteps = intentFromMainActivity.getStringExtra(MainActivity.EXTRA_STEPS_KEY);
                mServings = intentFromMainActivity.getStringExtra(MainActivity.EXTRA_SERVINGS_KEY);
                mImage = intentFromMainActivity.getStringExtra(MainActivity.EXTRA_IMAGE_KEY);

                mToast.makeText(this, "Intents worked!", Toast.LENGTH_SHORT).show();
            }
            else if(savedInstanceState != null) {

                mRecipeID = savedInstanceState.getString(MainActivity.EXTRA_RECIPE_ID_KEY);
                mRecipeName = savedInstanceState.getString(MainActivity.EXTRA_RECIPE_NAME_KEY);
                mIngredients = savedInstanceState.getString(MainActivity.EXTRA_INGREDIENT_KEY);
                mSteps = savedInstanceState.getString(MainActivity.EXTRA_STEPS_KEY);
                mServings = savedInstanceState.getString(MainActivity.EXTRA_SERVINGS_KEY);
                mImage = savedInstanceState.getString(MainActivity.EXTRA_IMAGE_KEY);

                mToast.makeText(this, "SaveInstance worked!", Toast.LENGTH_SHORT).show();
            }

            if (mIngredients != null && !mIngredients.equals("")) {

                GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
                mIngredientsRecyclerView.setLayoutManager(layoutManager);

                mIngredientsAdapter = new IngredientsAdapter(MainActivity.NUMBER_OF_ITEMS);
                mIngredientsRecyclerView.setAdapter(mIngredientsAdapter);

                try {
                    //JSONObject json = new JSONObject(jsonFromUrl);
                    JSONArray json = new JSONArray(mIngredients);

                    for(int i = 0; i<json.length(); i++) {
                        JSONObject focus = json.getJSONObject(i);

                        mIngredientQuantityList.add(focus.optString(INGREDIENT_QUANTITY_KEY));
                        mIngredientMeasureList.add(focus.optString(INGREDIENT_MEASURE_KEY));
                        mIngredientNameList.add(focus.optString(INGREDIENT_NAME_KEY));

                    }

                    mIngredientsAdapter.setData(mIngredientQuantityList, mIngredientMeasureList, mIngredientNameList);


                    //mToast.makeText(this, String.valueOf(mIngredientNameList.size()), Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            if (mSteps != null && !mSteps.equals("")) {

                GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
                mStepsRecyclerView.setLayoutManager(layoutManager);

                mStepsAdapter = new StepsAdapter(MainActivity.NUMBER_OF_ITEMS, this);
                mStepsRecyclerView.setAdapter(mStepsAdapter);

                try {
                    JSONArray json = new JSONArray(mSteps);

                    for(int i = 0; i<json.length(); i++) {
                        JSONObject focus = json.getJSONObject(i);

                        mStepsIDList.add(focus.optString(STEPS_ID_KEY));
                        mStepsShortDescriptionList.add(focus.optString(STEPS_SHORT_DESCRIPTION_KEY));
                        mStepsDescriptionList.add(focus.optString(STEPS_DESCRIPTION_KEY));
                        mStepsVideoURLList.add(focus.optString(STEPS_VIDEO_URL_KEY));
                        mStepsThumbnailURLList.add(focus.optString(STEPS_THUMBNAIL_URL_KEY));

                    }

                    mStepsAdapter.setData(mStepsIDList, mStepsShortDescriptionList, mStepsDescriptionList, mStepsVideoURLList, mStepsThumbnailURLList);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


            setTitle(mRecipeName);

            mServingsView.setText(getResources().getString(R.string.servings, mServings));

            if(findViewById(R.id.steps_linear_layout) != null) {
                mTwoPane = true;

                /*if(stepsFragment != null) {
                    FragmentManager fragmentManager = getSupportFragmentManager();

                    fragmentManager.beginTransaction()
                            .remove(stepsFragment)
                            .commit();
                }

                stepsFragment = new StepsFragment();

                FragmentManager fragmentManager = getSupportFragmentManager();

                fragmentManager.beginTransaction()
                        .add(R.id.steps_fragment, stepsFragment)
                        .commit();*/
            }
            else {
                mTwoPane = false;
            }
            mToast.makeText(this, "Two Pane: " + mTwoPane, Toast.LENGTH_SHORT).show();

        }


    }

    @Override
    public void onClick(int position) {

        mPosition = position;

        /*mStepsID = mStepsIDList.get(position);
        mStepsShortDescription = mStepsShortDescriptionList.get(position);
        mStepsDescription = mStepsDescriptionList.get(position);
        mStepsVideoURL = mStepsVideoURLList.get(position);
        mStepsThumbnailURL = mStepsThumbnailURLList.get(position);

        if(stepsFragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .remove(stepsFragment)
                    .commit();
        }

        stepsFragment = new StepsActivity();

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .add(R.id.steps_fragment, stepsFragment)
                .commit();
*/
        if(mTwoPane) {
            if(stepsFragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();

                fragmentManager.beginTransaction()
                        .remove(stepsFragment)
                        .commit();
            }

            stepsFragment = new StepsFragment();

            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .add(R.id.steps_fragment, stepsFragment)
                    .commit();
        }
        else {
            Intent movieDetailIntent = new Intent(DetailActivity.this, StepsActivity.class);
            movieDetailIntent.putExtra(MainActivity.EXTRA_RECIPE_NAME_KEY, mRecipeName);
            movieDetailIntent.putExtra(EXTRA_STEPS_ID_KEY, mStepsIDList.get(position));
            movieDetailIntent.putExtra(EXTRA_STEPS_SHORT_DESCRIPTION_KEY, mStepsShortDescriptionList.get(position));
            movieDetailIntent.putExtra(EXTRA_STEPS_DESCRIPTION_KEY, mStepsDescriptionList.get(position));
            movieDetailIntent.putExtra(EXTRA_STEPS_VIDEO_URL_KEY, mStepsVideoURLList.get(position));
            movieDetailIntent.putExtra(EXTRA_STEPS_THUMBNAIL_URL_KEY, mStepsThumbnailURLList.get(position));
            startActivity(movieDetailIntent);
        }
    }

    /*
    public void goBack(View view) {
        //mFragment = findViewById(R.id.steps_fragment);

        //mFragment.setVisibility(View.INVISIBLE);

        //StepsActivity stepsFragment = new StepsActivity();

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .remove(stepsFragment)
                .commit();
    }
    */

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putString(MainActivity.EXTRA_RECIPE_ID_KEY, mRecipeID);
        outState.putString(MainActivity.EXTRA_RECIPE_NAME_KEY, mRecipeName);
        outState.putString(MainActivity.EXTRA_INGREDIENT_KEY, mIngredients);
        outState.putString(MainActivity.EXTRA_STEPS_KEY, mSteps);
        outState.putString(MainActivity.EXTRA_SERVINGS_KEY, mServings);
        outState.putString(MainActivity.EXTRA_IMAGE_KEY, mImage);

        super.onSaveInstanceState(outState);

    }

}
