package com.amaizzzing.amaizingnotes

import android.app.Application
import com.amaizzzing.amaizingnotes.model.di.koin.*
import com.amaizzzing.amaizingnotes.utils.MyNotificationChannel
import org.koin.android.ext.android.inject
import org.koin.android.ext.android.startKoin

class NotesApplication : Application() {
    private val notificationChannel: MyNotificationChannel by inject()

    private val moduleList = listOf(
        appModule, calendarFragmentModule, addNoteFragmentModule,
        notFinishViewModel, resultsViewModel, splashViewModel
    )

    override fun onCreate() {
        super.onCreate()
        instance = this

        startKoin(this, moduleList)

        notificationChannel.createNotificationChannel(
            resources.getString(R.string.id_notifications_channel),
            resources.getString(R.string.name_notifications_channel),
            resources.getString(R.string.description_notifications_channel)
        )
    }

    fun getNotificationManager() = notificationChannel.notificationManager

    companion object {
        lateinit var instance: NotesApplication private set
    }
}

