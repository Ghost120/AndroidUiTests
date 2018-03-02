package com.exness.pushtest;

import android.support.test.InstrumentationRegistry;

import com.exness.pushtest.db.DbProvider;
import com.exness.pushtest.models.Quote;
import com.exness.pushtest.models.QuotesResponse;
import com.exness.pushtest.services.RestQuotesService;

import org.junit.Assert;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by konstantin on 01.03.2018.
 */

public class Helper {
    private static boolean isPushSuccess;
    private static String quote = "USD";
    private static String base = "EUR";
    private static float rate;

    public static void sendPushNotificationAndWaitResponse(long delay){
        isPushSuccess = false;
        sendPushNotification();
        long time = System.currentTimeMillis();
        while (System.currentTimeMillis() - time < delay) {
            try {
                if (isPushSuccess)
                    break;
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Assert.assertTrue("Push", isPushSuccess);
    }

    private static void sendPushNotification(){
        Retrofit restAdapter = new Retrofit.Builder()
                .baseUrl("https://us-central1-pushtestproject-7f507.cloudfunctions.net")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        Notifications listingService = restAdapter.create(Notifications.class);
        Call<Void> dataCall = listingService.getReplies();
        dataCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                isPushSuccess = true;
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                new Exception("Failure push notification");
            }
        });
    }

    public static void getQuote (){
        Retrofit restAdapter = new Retrofit.Builder()
                .baseUrl("https://api.fixer.io")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        RestQuotesService restQuotesService = restAdapter.create(RestQuotesService.class);
        Call<QuotesResponse> call = restQuotesService.quotes(base, quote);

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                QuotesResponse quotesResponse = (QuotesResponse) response.body();
                rate = quotesResponse.getRates().get("USD").floatValue();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                new Exception("Failure getting quotes");
            }
        });
    }

    public static void checkThatInsertRowInDB(long delay, int sizeBefore) {
        boolean isAddRow = false;
        long time = System.currentTimeMillis();
        while (System.currentTimeMillis() - time < delay) {
            try {
                isAddRow = getSizeDB() > sizeBefore;
                if (isAddRow)
                    break;
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Assert.assertTrue("Row in DB is not added", isAddRow);
    }

    public static int getSizeDB() {
        return getListQuoteFromDB().size();
    }

    public static List<Quote> getListQuoteFromDB(){
        return DbProvider.getInstance(InstrumentationRegistry.getInstrumentation().getTargetContext()).getAll();
    }
}