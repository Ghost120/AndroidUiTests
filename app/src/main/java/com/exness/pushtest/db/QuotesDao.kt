package com.exness.pushtest.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

import com.exness.pushtest.models.Quote
import io.reactivex.Flowable

@Dao
interface QuotesDao {
    @get:Query("SELECT * FROM quote")
    val all: List<Quote>

    @Query("SELECT * FROM quote WHERE quote LIKE :quote AND " + "base LIKE :base LIMIT 1")
    fun find(base: String, quote: String): Flowable<Quote>

    @Insert
    fun insertAll(vararg quotes: Quote)

    @Delete
    fun delete(quote: Quote)
}