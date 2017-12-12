package com.exness.pushtest.services

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.PowerManager
import com.exness.pushtest.models.QuotesResponse
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import com.exness.pushtest.db.AppDatabase
import android.arch.persistence.room.Room
import com.exness.pushtest.models.Quote
import java.util.*


/**
 * Created by alexander.shtanko on 12/12/17.
 */

class QuotesService : IntentService(QuotesService::class.java.simpleName) {
    private lateinit var restService: RestQuotesService
    private lateinit var wifilock: WifiManager.WifiLock
    private lateinit var wl: PowerManager.WakeLock

    private lateinit var db: AppDatabase

    private val quote = "USD"
    private val base = "EUR"


    override fun onCreate() {
        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        wifilock = wifiManager.createWifiLock(WifiManager.WIFI_MODE_FULL, "LockTag")
        wifilock.acquire()

        val pm = applicationContext.getSystemService(Context.POWER_SERVICE) as PowerManager
        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                QuotesService::class.java.simpleName)

        wl.acquire((60 * 1000).toLong())

        val retrofit = Retrofit.Builder()
                .baseUrl("https://api.fixer.io")
                .addConverterFactory(JacksonConverterFactory.create())
                .build()

        restService = retrofit.create(RestQuotesService::class.java)

        db = Room.databaseBuilder(applicationContext,
                AppDatabase::class.java, "db").build()
    }

    override fun onDestroy() {
        super.onDestroy()
        wl.release()
        wifilock.release()
    }


    override fun onHandleIntent(intent: Intent?) {
        val response = restService.quotes(base,quote).execute()
        if(response.isSuccessful)
        {
            val quotesResponse:QuotesResponse? = response.body()
            if(quotesResponse?.rates != null && quotesResponse.rates.containsKey(quote))
            {
                val value:Float= quotesResponse.rates[quote]!!
                val timestamp = Date().time
                val entity = Quote(timestamp,base,quote,value, timestamp)
                db.quotesDao().insertAll(entity)
            }
        }
    }
}
