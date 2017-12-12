package com.exness.pushtest.services

import com.exness.pushtest.models.QuotesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by alexander.shtanko on 12/12/17.
 */

interface RestQuotesService {
    @GET("/latest")
    fun quotes(@Query("base") base:String,@Query("symbols") quote:String): Call<QuotesResponse>
}
