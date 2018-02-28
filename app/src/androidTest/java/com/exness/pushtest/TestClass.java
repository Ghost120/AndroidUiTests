package com.exness.pushtest;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;


import com.exness.pushtest.activities.MainActivity;
import com.exness.pushtest.db.DbProvider;
import com.exness.pushtest.models.Quote;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

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

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;



/**
 * Created by konstantin on 28.02.2018.
 */
@RunWith(AndroidJUnit4.class)
public class TestClass {
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);


    @Test
    public void testStartApplication(){

        onView(withId(R.id.text))
                .perform(click())
                .check(matches(isDisplayed()));
        onView(withText("PushTest")).check(matches(isDisplayed()));
    }

    @Test
    public void testAddNewQuote() {
        clearPreferences();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//        DbProvider.getInstance(InstrumentationRegistry.getInstrumentation().getTargetContext()).insertAll(new Quote(timestamp.getTime(), "USD","eur", 1.1203F, timestamp.getTime()));
        List <Quote> listQuote = DbProvider.getInstance(InstrumentationRegistry.getInstrumentation().getTargetContext()).getAll();
        System.out.println();


    }



    private void sendPushNotification(){
        Retrofit restAdapter = new Retrofit.Builder()
                .baseUrl("https://us-central1-pushtestproject-7f507.cloudfunctions.net")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        Notifications listingService = restAdapter.create(Notifications.class);
//        listingService.getReplies();

        Call<Void> dataCall = listingService.getReplies();
        dataCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                System.out.println("cool");
//                Response{protocol=h2, code=200, message=, url=https://us-central1-pushtestproject-7f507.cloudfunctions.net/sendPush}
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println("not cool");
            }
        });

    }



    @Before
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
