package com.example.android.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Joshua on 7/21/2018.
 */

public class RecipeWidgetGridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    Context mContext;
    //public int mPosition;

    private String mRecipeName;
    private List<String> mIngredientQuantityList;
    private List<String> mIngredientMeasureList;
    private List<String> mIngredientNameList;
    private int mAppWidgetId;

    private static Toast mToast;

    public RecipeWidgetGridRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        //mPosition = position;
    }

    @Override
    public void onCreate() {
        mToast.makeText(mContext, String.valueOf(mRecipeName), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if(mIngredientNameList==null) {
            return 0;
        }
        else {
            return mIngredientNameList.size();
        }
    }

    @Override
    public RemoteViews getViewAt(int position) {
        //Context context = holder.ingredientNameView.getContext();

        // We construct a remote views item based on our widget item xml file, and set the
        // text based on the position.
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.ingredients_list_item_widget);

        rv.setTextViewText(R.id.detail_ingredient_name, mIngredientNameList.get(position));
        rv.setTextViewText(R.id.detail_ingredient_amount, mContext.getResources().getString(R.string.ingredient_amount, mIngredientQuantityList.get(position), mIngredientMeasureList.get(position)));

        // Return the remote views object.
        return rv;

//        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.ingredients_list_item);
//
//        views.setTextViewText(R.id.detail_ingredient_name, mIngredientNameList.get(position));
//
//        views.setTextViewText(R.id.detail_ingredient_amount, mContext.getResources().getString(R.string.ingredient_amount, mIngredientQuantityList.get(position), mIngredientMeasureList.get(position)));
//
//        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(mContext);
//        appWidgetManager.notifyAppWidgetViewDataChanged(mAppWidgetId, R.id.appwidget_recipe_ingredients_grid_view);
//
//        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    public void setData(String recipeName, List<String> ingredientQuantityList, List<String> ingredientMeasureList, List<String> ingredientNameList, int appWidgetId) {
        mRecipeName = recipeName;
        mIngredientQuantityList = ingredientQuantityList;
        mIngredientMeasureList = ingredientMeasureList;
        mIngredientNameList = ingredientNameList;
        mAppWidgetId = appWidgetId;
    }
}
