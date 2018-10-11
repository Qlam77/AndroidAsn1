package com.example.quincy.a00994454asn1;

import android.content.Intent;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;


@RunWith(AndroidJUnit4.class)
public class SearchTest {
    @Rule
    public ActivityTestRule<SearchActivity> mActivityRule =
            new ActivityTestRule<>(SearchActivity.class);
    @Test
    public void searchFoundNothing() {
        mActivityRule.launchActivity(new Intent());
        // Type text and then press the button.
        onView(withId(R.id.searchBox))
                .perform(typeText("HELLO"), closeSoftKeyboard());
        onView(withId(R.id.searchConfirm)).check(matches(allOf( isEnabled(), isClickable()))).perform(
                new ViewAction() {
                    @Override
                    public Matcher<View> getConstraints() {
                        return ViewMatchers.isEnabled(); // no constraints, they are checked above
                    }

                    @Override
                    public String getDescription() {
                        return "click plus button";
                    }

                    @Override
                    public void perform(UiController uiController, View view) {
                        view.performClick();
                    }
                }
        );
        // Check that the text was changed.
        onView(withId(R.id.resultText)).check(matches(withText("None found")));
        //onView(withContentDescription("Navigate up")).perform(click());
    }

    @Test
    public void searchFoundSomething() {
        mActivityRule.launchActivity(new Intent());
        // Type text and then press the button.
        onView(withId(R.id.searchBox))
                .perform(typeText("Disneyland"), closeSoftKeyboard());
        onView(withId(R.id.searchConfirm)).check(matches(allOf( isEnabled(), isClickable()))).perform(
                new ViewAction() {
                    @Override
                    public Matcher<View> getConstraints() {
                        return ViewMatchers.isEnabled(); // no constraints, they are checked above
                    }

                    @Override
                    public String getDescription() {
                        return "click plus button";
                    }

                    @Override
                    public void perform(UiController uiController, View view) {
                        view.performClick();
                    }
                }
        );
        // Check that the text was changed.
        onView(withId(R.id.resultText)).check(matches(withText("Found something")));
        //onView(withContentDescription("Navigate up")).perform(click());
    }
}