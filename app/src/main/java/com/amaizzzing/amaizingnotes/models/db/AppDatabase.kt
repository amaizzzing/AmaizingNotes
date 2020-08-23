package com.amaizzzing.amaizingnotes.models.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.amaizzzing.amaizingnotes.models.api_model.ApiNote

@Database(entities = [ApiNote::class],version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract fun noteDao() : NoteDao
}