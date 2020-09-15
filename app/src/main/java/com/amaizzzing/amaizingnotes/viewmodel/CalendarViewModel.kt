package com.amaizzzing.amaizingnotes.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amaizzzing.amaizingnotes.model.api_model.ApiNote
import com.amaizzzing.amaizingnotes.model.entities.Note
import com.amaizzzing.amaizingnotes.model.entities.NoteType
import com.amaizzzing.amaizingnotes.model.interactors.TodayNotesInteractor
import com.amaizzzing.amaizingnotes.model.mappers.NoteMapper
import com.amaizzzing.amaizingnotes.utils.getEndDay
import com.amaizzzing.amaizingnotes.utils.getStartDay
import com.amaizzzing.amaizingnotes.view.base.BaseViewModel
import com.amaizzzing.amaizingnotes.view.base.BaseViewState
import com.amaizzzing.amaizingnotes.view.view_states.CalendarNoteViewState
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

const val LENGTH_STR_TO_SEARCH = 3

class CalendarViewModel /*@Inject constructor*/(var interactor: TodayNotesInteractor) :
    BaseViewModel<MutableList<Note>,CalendarNoteViewState<MutableList<Note>>>() {
    private var compositeDisposable = CompositeDisposable()
    private var dis: Disposable? = null

    fun getSomeDays(
        range: Long = Date().getEndDay(Calendar.getInstance().time.time),
        noteType: NoteType
    ) {
        if (dis != null) {compositeDisposable.remove(dis!!);compositeDisposable.remove(dis!!)}
        val defaultTime = Calendar.getInstance().time.time
        val flowNotes = if (noteType == NoteType.ALL) {
            interactor.getAllNotes(
                Date().getStartDay(defaultTime),
                Date().getEndDay(defaultTime + range)
            )
        } else {
            interactor.getTodayNotes(
                Date().getStartDay(defaultTime),
                Date().getEndDay(defaultTime + range),
                noteType.type
            )
        }
        dis = flowNotes
            ?.map { it -> CalendarNoteViewState(false, null, it) }
            ?.startWith(CalendarNoteViewState<MutableList<Note>>(true, null, null))
            ?.onErrorReturn { CalendarNoteViewState(false, it, null) }
            ?.subscribeBy { noteViewState ->
                viewStateLiveData.value = noteViewState
            }!!
        compositeDisposable.add(dis!!)
    }

    fun searchNotes(searchText: String) {
        if(searchText.length>=LENGTH_STR_TO_SEARCH) {
            compositeDisposable.add(interactor.searchNotes("%$searchText%")
                .map { CalendarNoteViewState(false, null, it) }
                .startWith(CalendarNoteViewState<MutableList<Note>>(true, null, null))
                .onErrorReturn { CalendarNoteViewState(false, it, null) }
                .subscribeBy { noteViewState ->
                    viewStateLiveData.value = noteViewState
                }
            )
        }
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

