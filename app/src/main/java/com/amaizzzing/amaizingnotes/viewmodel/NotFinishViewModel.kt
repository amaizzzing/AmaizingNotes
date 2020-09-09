package com.amaizzzing.amaizingnotes.viewmodel

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
    //var listNotFinishNotes: MutableLiveData<BaseViewState<MutableList<Note>>> = MutableLiveData()
    private var compositeDisposable = CompositeDisposable()

    init {
        getNotFinishNotesFromDB()
    }

    private fun getNotFinishNotesFromDB() {
        compositeDisposable.add(interactor.getNotFinishNotes()
            ?.map { NotFinishViewState(false, null, it) }
            ?.startWith(NotFinishViewState<MutableList<Note>>(true, null, null))
            ?.onErrorReturn { NotFinishViewState(false, it, null) }
            ?.subscribeBy { notFinishViewState ->
                viewStateLiveData.value = notFinishViewState
            }!!
        )
    }

    fun updateNote(note: Note) {
        interactor.updateNote(NoteMapper.noteToApiNote(note))
            ?.subscribeOn(Schedulers.io())
            ?.subscribe()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}