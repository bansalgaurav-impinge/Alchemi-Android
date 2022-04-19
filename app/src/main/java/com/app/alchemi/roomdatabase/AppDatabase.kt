package com.app.alchemi.roomdatabase

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CardDetail::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cardDao(): CardDao
}