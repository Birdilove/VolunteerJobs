package com.csis4280.volunteerjobs.ui.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [user::class,job::class,participants::class], version = 3, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun userDao(): userDao?
    abstract fun jobDao(): jobDao?
    abstract fun paeticipantDao(): participantDao?

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "database.db"
                    ).build()
                }
            }
            return INSTANCE
        }
    }
}