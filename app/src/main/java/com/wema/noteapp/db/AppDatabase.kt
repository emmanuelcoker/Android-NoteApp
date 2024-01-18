package com.wema.noteapp.db

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {


abstract fun noteDao(): NoteDao

    companion object {
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase?{
            Log.d("Database", "database instance")
            if (instance == null) {
                synchronized(AppDatabase::class) {
                    instance = Room.databaseBuilder(context, AppDatabase::class.java, "notes_db")
                        .allowMainThreadQueries()
                        .build()
                }
            }
            return instance
        }
    }
}