package com.example.quincy.a00994454asn1;


import android.content.Intent;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.DatePicker;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsNot.not;


@RunWith(AndroidJUnit4.class)
public class SearchTest {
    @Rule
    public ActivityTestRule<SearchActivity> mActivityRule =
            new ActivityTestRule<>(SearchActivity.class);

    //Check Keyword
    @Test
    public void searchKeyword() {
        mActivityRule.launchActivity(new Intent());
        // Type text and then press the button.
        onView(withId(R.id.searchBox))
                .perform(typeText("duck"), closeSoftKeyboard());
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

        onView(withId(R.id.caption)).check(matches(withText("duck")));
    }

    //Search by date
    @Test
    public void searchByDate() {
        mActivityRule.launchActivity(new Intent());
        // Type text and then press the button.
        onView(withId(R.id.dateRadio))
                .perform(click());

        setDate(R.id.startDate, 2018, 10, 9);
        setDate(R.id.endDate, 2018, 10, 13);

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

        onView(withId(R.id.timeStamp)).check(matches(not(withText(""))));

    }

    //SearchByCoordinates
    @Test
    public void SearchByCoordinates() {
        mActivityRule.launchActivity(new Intent());
        onView(withId(R.id.topLeftLat)).perform(typeText("0"), closeSoftKeyboard());
        onView(withId(R.id.topLeftLong)).perform(typeText("0"), closeSoftKeyboard());
        onView(withId(R.id.BotRightLat)).perform(typeText("300"), closeSoftKeyboard());
        onView(withId(R.id.BotRightLong)).perform(typeText("300"), closeSoftKeyboard());

        onView(withId(R.id.searchArea))
                .perform(click());

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
        onView(withId(R.id.Location)).check(matches(not(withText(""))));
    }

    public static void setDate(int datePickerLaunchViewId, int year, int monthOfYear, int dayOfMonth) {
        onView(allOf(withId(datePickerLaunchViewId), isDescendantOfA(withId(R.id.dateContainer)))).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(year, monthOfYear, dayOfMonth));
        onView(withId(android.R.id.button1)).perform(click());
    }
}