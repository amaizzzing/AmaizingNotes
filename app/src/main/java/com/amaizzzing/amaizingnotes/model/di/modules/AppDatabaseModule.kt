package com.amaizzzing.amaizingnotes.model.di.modules

import androidx.room.Room
import com.amaizzzing.amaizingnotes.NotesApplication
import com.amaizzzing.amaizingnotes.utils.DB_NAME
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppDatabaseModule {
    //@Singleton
    //@Provides
    //fun getAppDatabaseModule() = Room.databaseBuilder(NotesApplication.instance, AppDatabase::class.java, DB_NAME).build()
}