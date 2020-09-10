package com.amaizzzing.amaizingnotes

import android.app.Application
import android.content.Context
import com.amaizzzing.amaizingnotes.model.db.AppDatabase
import com.amaizzzing.amaizingnotes.model.db.FirebaseDaoImpl
import com.amaizzzing.amaizingnotes.model.di.components.DaggerDiNotesComponent
import com.amaizzzing.amaizingnotes.model.di.components.DiNotesComponent
import com.amaizzzing.amaizingnotes.model.di.modules.AppDatabaseModule
import com.amaizzzing.amaizingnotes.model.di.modules.NoteNotificationModule
import com.amaizzzing.amaizingnotes.utils.DB_NAME
import com.amaizzzing.amaizingnotes.utils.MyNotificationChannel
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class NotesApplication : Application() {
    private lateinit var noteComponent: DiNotesComponent

    @Inject
    lateinit var notificationChannel: MyNotificationChannel

    @Inject
    lateinit var appDataBase: AppDatabase

    lateinit var db: FirebaseFirestore
    lateinit var notesReference: CollectionReference
    val fireBaseDao: FirebaseDaoImpl = FirebaseDaoImpl()

    override fun onCreate() {
        super.onCreate()
        instance = this

        noteComponent = DaggerDiNotesComponent
            .builder()
            .appDatabaseModule(AppDatabaseModule())
            .noteNotificationModule(NoteNotificationModule())
            .build()
        noteComponent.injectDiApplication(this)

        notificationChannel.createNotificationChannel(
            resources.getString(R.string.id_notifications_channel),
            resources.getString(R.string.name_notifications_channel),
            resources.getString(R.string.description_notifications_channel)
        )

        db = FirebaseFirestore.getInstance()
        notesReference = db.collection(DB_NAME)
    }


    fun getAppDataBase(context: Context = applicationContext) = appDataBase

    fun getNotificationManager() = notificationChannel.notificationManager

    fun getFirebaseNotesReference() = notesReference

    companion object {
        lateinit var instance: NotesApplication private set
    }
}

