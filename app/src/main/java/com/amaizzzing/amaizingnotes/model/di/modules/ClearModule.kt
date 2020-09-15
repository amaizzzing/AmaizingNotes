package com.amaizzzing.amaizingnotes.model.di.modules

import com.amaizzzing.amaizingnotes.model.data.TodayNoteDatasource
import com.amaizzzing.amaizingnotes.model.data.TodayNoteDatasourceImpl
import com.amaizzzing.amaizingnotes.model.db.FirebaseDaoImpl
import com.amaizzzing.amaizingnotes.model.interactors.TodayNotesInteractor
import com.amaizzzing.amaizingnotes.model.interactors.TodayNotesInteractorImpl
import com.amaizzzing.amaizingnotes.model.repositories.TodayNoteRepository
import com.amaizzzing.amaizingnotes.model.repositories.TodayNoteRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ClearModule {
    @Provides
    @Singleton
    fun getNoteInteractorModule(noteRepository:TodayNoteRepository) : TodayNotesInteractor = TodayNotesInteractorImpl(noteRepository)
    @Provides
    @Singleton
    fun getNoteDatasourceModule(firebaseDaoImpl: FirebaseDaoImpl) : TodayNoteDatasource = TodayNoteDatasourceImpl(firebaseDaoImpl)
    @Provides
    @Singleton
    fun getNoteRepositoryModule(noteDataSource:TodayNoteDatasource):TodayNoteRepository = TodayNoteRepositoryImpl(noteDataSource)
    @Provides
    @Singleton
    fun getFirebaseDaoImpl(store: FirebaseFirestore, auth: FirebaseAuth) : FirebaseDaoImpl = FirebaseDaoImpl(store,auth)
    @Provides
    @Singleton
    fun getFirebaseFirestore() : FirebaseFirestore = FirebaseFirestore.getInstance()
    @Provides
    @Singleton
    fun getFirebaseAuth() : FirebaseAuth = FirebaseAuth.getInstance()
}