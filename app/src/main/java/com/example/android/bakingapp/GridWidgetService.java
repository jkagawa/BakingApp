package com.example.android.bakingapp;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Joshua on 7/22/2018.
 */

public class GridWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RecipeWidgetGridRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}
