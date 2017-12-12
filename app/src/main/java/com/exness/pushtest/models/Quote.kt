package com.exness.pushtest.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by alexander.shtanko on 12/12/17.
 */
@Entity(tableName = "quote")
 class Quote(
        @PrimaryKey  var id: Long,
        @ColumnInfo(name = "base")  var base: String,
        @ColumnInfo(name = "quote")  var quote: String,
        @ColumnInfo(name = "value")  var value: Float,
        @ColumnInfo(name = "timestamp")  var timestamp: Long)

