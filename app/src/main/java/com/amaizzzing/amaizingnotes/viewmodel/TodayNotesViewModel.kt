package com.amaizzzing.amaizingnotes.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amaizzzing.amaizingnotes.NotesApplication
import com.amaizzzing.amaizingnotes.models.entities.Note
import com.amaizzzing.amaizingnotes.models.interactors.TodayNotesInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class TodayNotesViewModel() : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    val listTodayNotes: MutableLiveData<List<Note>> = MutableLiveData()

    fun onStart() {
        compositeDisposable.add(NotesApplication.instance.getMyTodayNoteInteractor().getTodayNotes()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { listNote -> listTodayNotes.value = listNote }
        )
    }

    fun onStop() {
        compositeDisposable.dispose()
    }
}