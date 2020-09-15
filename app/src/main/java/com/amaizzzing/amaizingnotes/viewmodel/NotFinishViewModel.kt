package com.amaizzzing.amaizingnotes.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amaizzzing.amaizingnotes.model.entities.Note
import com.amaizzzing.amaizingnotes.model.interactors.TodayNotesInteractor
import com.amaizzzing.amaizingnotes.model.mappers.NoteMapper
import com.amaizzzing.amaizingnotes.view.base.BaseViewModel
import com.amaizzzing.amaizingnotes.view.base.BaseViewState
import com.amaizzzing.amaizingnotes.view.view_states.NotFinishViewState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class NotFinishViewModel @Inject constructor(private var interactor: TodayNotesInteractor) :
    BaseViewModel<MutableList<Note>,NotFinishViewState<MutableList<Note>>>() {
    private var compositeDisposable = CompositeDisposable()

    init {
        getNotFinishNotesFromDB()
    }

    private fun getNotFinishNotesFromDB() {
        val list = interactor.getNotFinishNotes()
            ?.map { NotFinishViewState(false, null, it) }
            ?.startWith(NotFinishViewState<MutableList<Note>>(true, null, null))
            ?.onErrorReturn { NotFinishViewState(false, it, null) }
            ?.subscribeBy { notFinishViewState ->
                viewStateLiveData.value = notFinishViewState
            }
        list?.let {
            compositeDisposable.add(list)
        }

    }

    fun updateNote(note: Note) {
        val upd = interactor.updateNote(NoteMapper.noteToApiNote(note))
            ?.flatMapPublisher { interactor.getNotFinishNotes() }
            ?.map { NotFinishViewState(false, null, it) }
            ?.onErrorReturn { NotFinishViewState(false, it, null) }
            ?.subscribeOn(Schedulers.io())
            ?.subscribe(
                {viewStateLiveData.value = it}
            )
            upd?.let {
                compositeDisposable.add(upd)
            }!!
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}