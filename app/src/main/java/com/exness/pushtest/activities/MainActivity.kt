package com.exness.pushtest.activities

import android.arch.persistence.room.Room
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.exness.pushtest.R
import com.exness.pushtest.db.AppDatabase
import com.exness.pushtest.utils.DeviceUtils
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private val disposable: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = Room.databaseBuilder(applicationContext,
                AppDatabase::class.java, "db").build()

        disposable.add(db.quotesDao().find("EUR", "USD")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    it?.let {
                        text.text = "Base:" + it.base + "/n" +
                                "Quote:" + it.quote + "/n" +
                                "Value:" + it.value + "/n" +
                                "Timestamp:" + it.timestamp

                    }
                }, {
                    Log.e(MainActivity::class.java.simpleName, "", it)
                }))

        val refreshedToken = FirebaseInstanceId.getInstance().token
        val mDatabase = FirebaseDatabase.getInstance().reference
        if (refreshedToken != null)
            mDatabase.child("tokens").child(DeviceUtils.getDeviceId(this)).setValue(refreshedToken)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }
}
