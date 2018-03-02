package com.exness.pushtest;

import android.content.pm.ActivityInfo;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;


import com.exness.pushtest.activities.MainActivity;
import com.exness.pushtest.db.DbProvider;
import com.exness.pushtest.models.Quote;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.sql.Timestamp;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.exness.pushtest.Helper.checkThatInsertRowInDB;
import static com.exness.pushtest.Helper.getQuote;
import static com.exness.pushtest.Helper.getSizeDB;
import static com.exness.pushtest.Helper.sendPushNotificationAndWaitResponse;


/**
 * Created by konstantin on 28.02.2018.
 */
@RunWith(AndroidJUnit4.class)
public class TestClass {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testFirstStartApplication(){
        onView(withId(R.id.text))
                .perform(click())
                .check(matches(isDisplayed()));
        onView(withText("PushTest")).check(matches(isDisplayed()));
    }

    @Test
    public void testAddNewQuote() {
        int sizeDBbefore = getSizeDB();
        sendPushNotificationAndWaitResponse(9000);
        checkThatInsertRowInDB(9000, sizeDBbefore);
    }

    @Test
    public void testRotateScreen() {
        mActivityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        mActivityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        System.out.println("");
    }

    public void clearPreferences() {
        try {
            // clearing app data
            Runtime runtime = Runtime.getRuntime();
            runtime.exec("pm clear PushTest");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
