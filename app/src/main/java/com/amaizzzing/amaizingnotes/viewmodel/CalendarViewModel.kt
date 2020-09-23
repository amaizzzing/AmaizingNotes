package com.amaizzzing.amaizingnotes.viewmodel

import androidx.annotation.VisibleForTesting
import com.amaizzzing.amaizingnotes.model.api_model.ApiNote
import com.amaizzzing.amaizingnotes.model.entities.Note
import com.amaizzzing.amaizingnotes.model.entities.NoteType
import com.amaizzzing.amaizingnotes.model.interactors.TodayNotesInteractor
import com.amaizzzing.amaizingnotes.model.mappers.NoteMapper
import com.amaizzzing.amaizingnotes.utils.getEndDay
import com.amaizzzing.amaizingnotes.utils.getStartDay
import com.amaizzzing.amaizingnotes.view.base.BaseViewModel
import com.amaizzzing.amaizingnotes.view.view_states.CalendarNoteViewState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import java.util.*

const val LENGTH_STR_TO_SEARCH = 3

class CalendarViewModel(val interactor: TodayNotesInteractor) :
    BaseViewModel<CalendarNoteViewState<MutableList<Note>>>() {
    //private val notesChannel = notesRepository.getNotes()

    private var compositeDisposable = CompositeDisposable()
    private var dis: Disposable? = null

    fun getSomeDays(
        range: Long = Date().getEndDay(Calendar.getInstance().time.time),
        noteType: NoteType
    ) {
        launch {
            val defaultTime = Calendar.getInstance().time.time
            if (noteType == NoteType.ALL) {
                interactor.getAllNotes(
                    Date().getStartDay(defaultTime),
                    Date().getEndDay(defaultTime + range)
                ).consumeEach {
                    val newState = CalendarNoteViewState(
                        it._isLoading,
                        it._error,
                        NoteMapper.listApiNoteToListNote(it.data!!.toList())
                    )
                    setData(newState)
                }
            }else{
                interactor.getTodayNotes(
                    Date().getStartDay(defaultTime),
                    Date().getEndDay(defaultTime + range),
                    noteType.type
                ).consumeEach {
                    val newState = CalendarNoteViewState(
                        it._isLoading,
                        it._error,
                        NoteMapper.listApiNoteToListNote(it.data!!.toList())
                    )
                    setData(newState)
                }
            }
        }
    }

    fun searchNotes(searchText: String) {
        launch {
            val defaultTime = Calendar.getInstance().time.time
            interactor.searchNotes("%$searchText%").consumeEach {
                val newState = CalendarNoteViewState(
                    it._isLoading,
                    it._error,
                    NoteMapper.listApiNoteToListNote(it.data!!.toList())
                )
                setData(newState)
            }

        }
    }

    suspend fun updateNote(note: Note) {
        interactor.updateNote(NoteMapper.noteToApiNote(note))
    }

    @VisibleForTesting
    public override fun onCleared() {
        //notesChannel.cancel()
        super.onCleared()
    }
}

