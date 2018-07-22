package com.example.android.bakingapp;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


/**
 * Created by Joshua on 7/16/2018.
 */

@RunWith(AndroidJUnit4.class)
public class RecipeClickShowDetailPageTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void clickOnRecipeStep() {

        //onData(anything()).inAdapterView(withId(R.id.recyclerview_recipes))
                //.atPosition(1)
                //.perform(click());

        onView(withId(R.id.recyclerview_recipes))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        onView(withId(R.id.recyclerview_ingredients)).check(matches(isDisplayed()));

        //matches(withText("Ingredients:"))


    }


}
