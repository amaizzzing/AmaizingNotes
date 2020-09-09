package com.amaizzzing.amaizingnotes.model.db

import androidx.lifecycle.LiveData
import com.amaizzzing.amaizingnotes.model.api_model.ApiNote
import io.reactivex.Flowable
import io.reactivex.Maybe

interface FirebaseDao {
    fun getAllNotes(): Flowable<List<ApiNote>>
    //fun getNoteById(id: String): LiveData<NoteResult>
    fun insertNote(note: ApiNote) : Maybe<Long>

}