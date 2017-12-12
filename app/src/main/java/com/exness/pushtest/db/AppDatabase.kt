package com.exness.pushtest.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.exness.pushtest.models.Quote

/**
 * Created by alexander.shtanko on 12/12/17.
 */

@Database(entities = arrayOf(Quote::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun quotesDao(): QuotesDao
}