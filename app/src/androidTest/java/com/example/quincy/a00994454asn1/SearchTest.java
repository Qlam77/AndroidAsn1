package com.example.quincy.a00994454asn1;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
public class SearchTest {
    @Rule
    public ActivityTestRule<SearchActivity> mActivityRule =
            new ActivityTestRule<>(SearchActivity.class);
    @Test
    public void searchFoundNothing() {
        // Type text and then press the button.
        onView(withId(R.id.searchBox))
                .perform(typeText("HELLO"), closeSoftKeyboard());
        onView(withId(R.id.searchConfirm)).perform(click());
        // Check that the text was changed.
        onView(withId(R.id.resultText)).check(matches(withText("None found")));
        //onView(withContentDescription("Navigate up")).perform(click());
    }

    @Test
    public void searchFoundSomething() {
        // Type text and then press the button.
        onView(withId(R.id.searchBox))
                .perform(typeText("Disneyland"), closeSoftKeyboard());
        onView(withId(R.id.searchConfirm)).perform(click());
        // Check that the text was changed.
        onView(withId(R.id.resultText)).check(matches(withText("Found something")));
        //onView(withContentDescription("Navigate up")).perform(click());
    }
}