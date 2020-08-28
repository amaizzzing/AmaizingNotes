package com.amaizzzing.amaizingnotes

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import androidx.core.app.NotificationCompat
import androidx.room.Room
import com.amaizzzing.amaizingnotes.models.data.TodayNoteDatasource
import com.amaizzzing.amaizingnotes.models.data.TodayNoteDatasourceImpl
import com.amaizzzing.amaizingnotes.models.db.AppDatabase
import com.amaizzzing.amaizingnotes.models.db.NoteDao
import com.amaizzzing.amaizingnotes.models.interactors.TodayNotesInteractor
import com.amaizzzing.amaizingnotes.models.interactors.TodayNotesInteractorImpl
import com.amaizzzing.amaizingnotes.models.repositories.TodayNoteRepository
import com.amaizzzing.amaizingnotes.models.repositories.TodayNoteRepositoryImpl

class NotesApplication : Application() {
    private var notificationManager: NotificationManager? = null

    val todayNoteDataSource:TodayNoteDatasource = TodayNoteDatasourceImpl()
    val todayNoteRepository:TodayNoteRepository = TodayNoteRepositoryImpl(todayNoteDataSource)
    val todayNoteInteractor:TodayNotesInteractor = TodayNotesInteractorImpl(todayNoteRepository)

    var noteType=NOTE_TYPE.NOTE

    var appDataBase: AppDatabase? = null
    var noteDao : NoteDao? = null

    override fun onCreate() {
        super.onCreate()
        instance = this

        val notificationManager =
            applicationContext.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager

        createNotificationChannel(
            resources.getString(R.string.id_notifications_channel),
            resources.getString(R.string.name_notifications_channel),
            resources.getString(R.string.description_notifications_channel)
        )
    }

    fun getNotificationManager() = notificationManager

    fun getMyTodayNoteInteractor() = todayNoteInteractor

    fun getAppDataBase(context:Context = applicationContext): AppDatabase? {
        if (appDataBase == null){
            synchronized(AppDatabase::class){
                appDataBase = Room.databaseBuilder(context, AppDatabase::class.java, "AmaizingNotesDB").build()
                noteDao = appDataBase?.noteDao()
            }
        }
        return appDataBase
    }

    fun getAppNoteDao() = noteDao

    fun destroyDataBase(){
        appDataBase = null
    }

    enum class NOTE_TYPE{NOTE,EXERS}

    companion object {
        lateinit var instance : NotesApplication private set

    }

    private fun createNotificationChannel(
        id: String, name: String,
        description: String
    ) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(id, name, importance)
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            channel.vibrationPattern =
                longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            notificationManager?.createNotificationChannel(channel)
            channel.description = description
        }else{
            val builder = NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Title")
                .setContentText("Notification text")

            val notification: Notification = builder.build()

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(1, notification)
        }

    }
}