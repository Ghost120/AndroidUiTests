package com.exness.pushtest;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by konstantin on 28.02.2018.
 */

public interface Notifications {
    @GET("/sendPush")
    Call<Void> getReplies();
}