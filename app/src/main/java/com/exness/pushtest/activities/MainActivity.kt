package com.exness.pushtest.activities

import android.arch.persistence.room.Room
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.exness.pushtest.R
import com.exness.pushtest.db.AppDatabase
import com.exness.pushtest.db.DbProvider
import com.exness.pushtest.services.QuotesService
import com.exness.pushtest.utils.DeviceUtils
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private val disposable: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val firebaseDatabase = FirebaseDatabase.getInstance().reference


        disposable.add(DbProvider.getInstance(this).find("EUR", "USD")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    it?.let {
                        text.text = "Instrument: " + it.base+"/"+it.quote + " = " +
                                it.value + "\n" +
                                "Updated at: " + getDate(it.timestamp)

                    }
                }, {
                    Log.e(MainActivity::class.java.simpleName, "", it)
                }))

        val refreshedToken = FirebaseInstanceId.getInstance().token
        if (refreshedToken != null)
            firebaseDatabase.child("tokens").child(DeviceUtils.getDeviceId(this)).setValue(refreshedToken)
    }

    private fun getDate(timestamp: Long): String {
        return SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(Date(timestamp))
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }
}
