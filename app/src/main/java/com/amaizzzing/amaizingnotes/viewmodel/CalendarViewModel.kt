package com.amaizzzing.amaizingnotes.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amaizzzing.amaizingnotes.NotesApplication
import com.amaizzzing.amaizingnotes.model.entities.Note
import com.amaizzzing.amaizingnotes.model.mappers.NoteMapper
import com.amaizzzing.amaizingnotes.utils.getEndDay
import com.amaizzzing.amaizingnotes.utils.getStartDay
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.*

class CalendarViewModel : ViewModel() {
    var someDaysLiveData: MutableLiveData<MutableList<Note>> = MutableLiveData()
    private val interactor = NotesApplication.instance.getMyTodayNoteInteractor()
    private var compositeDisposable = CompositeDisposable()
    private var dis: Disposable? = null

    fun getSomeDays(range: Long = Date().getEndDay(Calendar.getInstance().time.time)) {
        if (dis != null) compositeDisposable.remove(dis!!)
        val defaultTime = Calendar.getInstance().time.time
        dis = interactor.getTodayNotes(
            Date().getStartDay(defaultTime),
            Date().getEndDay(defaultTime + range)
        )
            ?.map { it -> NoteMapper.listApiNoteToListNote(it) }
            ?.subscribeOn(io.reactivex.schedulers.Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeBy { list -> someDaysLiveData.value = list }!!
        compositeDisposable.add(dis!!)
    }

    fun updateNote(note: Note) {
        interactor.updateNote(NoteMapper.noteToApiNote(note))
            ?.subscribeOn(Schedulers.io())
            ?.subscribe()
    }
}

