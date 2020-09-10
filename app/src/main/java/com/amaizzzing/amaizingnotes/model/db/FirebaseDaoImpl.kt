package com.amaizzzing.amaizingnotes.model.db

import com.amaizzzing.amaizingnotes.NotesApplication
import com.amaizzzing.amaizingnotes.model.api_model.ApiNote
import com.amaizzzing.amaizingnotes.model.data.TodayNoteDatasource
import com.google.firebase.firestore.Query
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Maybe

const val DATE_FIELD_TO_SEARCH = "date"
const val TYPE_NOTE_TO_SEARCH = "typeNote"
const val TEXT_KEY_TO_SEARCH = "text"
const val ID_KEY_TO_SEARCH = "id"
const val DONE_KEY = "done"

class FirebaseDaoImpl : TodayNoteDatasource {
    override fun getAllNotes(start: Long, end: Long): Flowable<List<ApiNote>> =
        Flowable.create({
            val listenerRegistration = NotesApplication.instance.getFirebaseNotesReference()
                .whereGreaterThan(DATE_FIELD_TO_SEARCH, start)
                .whereLessThan(DATE_FIELD_TO_SEARCH, end)
                .orderBy(DATE_FIELD_TO_SEARCH, Query.Direction.DESCENDING)
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        it.onError(error)
                        return@addSnapshotListener
                    } else if (value != null) {
                        it.onNext(value.toObjects(ApiNote::class.java))
                        it.onComplete()
                    }
                }
            it.setCancellable { listenerRegistration.remove() }
        }, BackpressureStrategy.LATEST)

    override fun getCountFinishTasks(startDay: Long, endDay: Long): Int {
        TODO("Not yet implemented")
    }

    override fun getTodayNote(
        startDay: Long,
        endDay: Long,
        typeRecord: String
    ): Flowable<List<ApiNote>> =
        Flowable.create({
            val listenerRegistration = NotesApplication.instance.getFirebaseNotesReference()
                .whereGreaterThan(DATE_FIELD_TO_SEARCH, startDay)
                .whereLessThan(DATE_FIELD_TO_SEARCH, endDay)
                .whereEqualTo(TYPE_NOTE_TO_SEARCH, typeRecord)
                .orderBy(DATE_FIELD_TO_SEARCH, Query.Direction.DESCENDING)
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        it.onError(error)
                        return@addSnapshotListener
                    } else if (value != null) {
                        it.onNext(value.toObjects(ApiNote::class.java))
                        it.onComplete()
                    }
                }
            it.setCancellable { listenerRegistration.remove() }
        }, BackpressureStrategy.LATEST)


    override fun searchNotes(searchText: String): Flowable<List<ApiNote>> {
        val str = searchText.substring(1, searchText.length - 1)
        return Flowable.create({
            val listenerRegistration = NotesApplication.instance.getFirebaseNotesReference()
                .whereEqualTo(TEXT_KEY_TO_SEARCH, str)
                .orderBy(DATE_FIELD_TO_SEARCH)
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        it.onError(error)
                        return@addSnapshotListener
                    } else if (value != null) {
                        it.onNext(value.toObjects(ApiNote::class.java))
                        it.onComplete()
                    }
                }
            it.setCancellable { listenerRegistration.remove() }
        }, BackpressureStrategy.LATEST)
    }

    override fun getCoefRatingForDays(): Double {
        TODO("Not yet implemented")
    }

    override fun getNoteById(id1: Long): Maybe<ApiNote> =
        Maybe.create { emitter ->
            run {
                NotesApplication.instance.getFirebaseNotesReference()
                    .whereEqualTo(ID_KEY_TO_SEARCH, id1)
                    .addSnapshotListener { value, error ->
                        if (error != null) {
                            emitter.onError(error)
                            return@addSnapshotListener
                        } else if (value != null) {
                            val list = value.toObjects(ApiNote::class.java)
                            if (list.size != 0)
                                emitter.onSuccess(value.toObjects(ApiNote::class.java)[0])
                            emitter.onComplete()
                        }
                    }
            }
        }

    override fun getNotFinishNotes(): Flowable<List<ApiNote>>? =
        Flowable.create({
            val listenerRegistration = NotesApplication.instance.getFirebaseNotesReference()
                .whereEqualTo(DONE_KEY, false)
                .orderBy(DATE_FIELD_TO_SEARCH)
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        it.onError(error)
                        return@addSnapshotListener
                    } else if (value != null) {
                        it.onNext(value.toObjects(ApiNote::class.java))
                        it.onComplete()
                    }
                }
            it.setCancellable { listenerRegistration.remove() }
        }, BackpressureStrategy.LATEST)


    override fun insertNote(note: ApiNote): Maybe<Long> =
        Maybe.create { emitter ->
            run {
                NotesApplication.instance.getFirebaseNotesReference()
                    .document(note.id.toString())
                    .set(note)
                    .addOnSuccessListener {
                        emitter.onSuccess(note.id)
                        emitter.onComplete()
                        return@addOnSuccessListener
                    }
                    .addOnFailureListener {
                        emitter.onError(it)
                    }
            }
        }

    override fun updateNote(note: ApiNote): Maybe<Int> = insertNote(note).map { it.toInt() }

    override fun deleteNote(apiNote: ApiNote): Maybe<Int>? = deleteNoteById(apiNote.id)

    override fun deleteNoteById(id1: Long): Maybe<Int> {
        return Maybe.create { emitter ->
            run {
                NotesApplication.instance.getFirebaseNotesReference()
                    .document(id1.toString())
                    .delete()
                    .addOnSuccessListener {
                        emitter.onSuccess(1)
                        emitter.onComplete()
                        return@addOnSuccessListener
                    }
                    .addOnFailureListener {
                        emitter.onError(it)
                    }
            }
        }
    }
}