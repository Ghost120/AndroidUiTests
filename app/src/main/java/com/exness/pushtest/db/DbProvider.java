package com.exness.pushtest.db;

import android.arch.persistence.room.Room;
import android.content.Context;

/**
 * Created by alexander.shtanko on 12/12/17.
 */

public class DbProvider {
    private static QuotesDao dao;

    public static synchronized QuotesDao getInstance(Context context) {
        if (dao == null)
            dao = Room.databaseBuilder(context,
                    AppDatabase.class, "db").build().quotesDao();
        return dao;
    }
}
