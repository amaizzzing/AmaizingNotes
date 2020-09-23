package com.amaizzzing.amaizingnotes.model.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.amaizzzing.amaizingnotes.model.NoAuthException
import com.amaizzzing.amaizingnotes.model.api_model.ApiNote
import com.amaizzzing.amaizingnotes.model.data.TodayNoteDatasource
import com.amaizzzing.amaizingnotes.model.entities.User
import com.amaizzzing.amaizingnotes.utils.TASK_TYPE_VALUE
import com.amaizzzing.amaizingnotes.view.view_states.CalendarNoteViewState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Maybe
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel

class FirebaseDaoImpl(val store: FirebaseFirestore, val auth: FirebaseAuth) : TodayNoteDatasource {
    companion object {
        private const val NOTES_COLLECTION = "amaizing_notes"
        private const val USER_COLLECTION = "users"
        const val DATE_FIELD_TO_SEARCH = "date"
        const val TYPE_NOTE_TO_SEARCH = "typeNote"
        const val TEXT_KEY_TO_SEARCH = "text"
        const val ID_KEY_TO_SEARCH = "id"
        const val DONE_KEY = "done"
    }

    private val currentUser
        get() = auth.currentUser

    val getUserNotesCollection
        get() = currentUser?.let {
            store.collection(USER_COLLECTION).document(it.uid).collection(NOTES_COLLECTION)
        } ?: throw NoAuthException()

    override fun getCurrentUser(): LiveData<User?> = MutableLiveData<User?>().apply {
        value = currentUser?.let { User(it.displayName ?: "", it.email ?: "") }
    }

    override fun getAllNotes(
        start: Long,
        end: Long
    ): ReceiveChannel<CalendarNoteViewState<MutableList<ApiNote>>> =
        Channel<CalendarNoteViewState<MutableList<ApiNote>>>(Channel.CONFLATED).apply {
            var registration: ListenerRegistration? = null
            try {
                registration = getUserNotesCollection
                    .whereGreaterThan(DATE_FIELD_TO_SEARCH, start)
                    .whereLessThan(DATE_FIELD_TO_SEARCH, end)
                    .orderBy(DATE_FIELD_TO_SEARCH, Query.Direction.DESCENDING)
                    .addSnapshotListener { value, error ->
                        if (error != null) {
                            CalendarNoteViewState(false, error, null)
                            return@addSnapshotListener
                        } else if (value != null) {
                            CalendarNoteViewState(
                                false,
                                null,
                                value.toObjects(ApiNote::class.java)
                            )
                        }
                        value?.let {
                            offer(
                                CalendarNoteViewState(
                                    false,
                                    null,
                                    value.toObjects(ApiNote::class.java)
                                )
                            )
                        }
                    }
            } catch (e: Throwable) {
                offer(CalendarNoteViewState(false, e, null))
            }

            invokeOnClose {
                registration?.remove()
            }
        }

    override fun getTodayNote(
        startDay: Long,
        endDay: Long,
        typeRecord: String
    ): ReceiveChannel<CalendarNoteViewState<MutableList<ApiNote>>> =
        Channel<CalendarNoteViewState<MutableList<ApiNote>>>(Channel.CONFLATED).apply {
            var registration: ListenerRegistration? = null

            try {
                registration = getUserNotesCollection
                    .whereGreaterThan(DATE_FIELD_TO_SEARCH, startDay)
                    .whereLessThan(DATE_FIELD_TO_SEARCH, endDay)
                    .whereEqualTo(TYPE_NOTE_TO_SEARCH, typeRecord)
                    .orderBy(DATE_FIELD_TO_SEARCH, Query.Direction.DESCENDING)
                    .addSnapshotListener { value, error ->
                        if (error != null) {
                            CalendarNoteViewState(false, error, null)
                            return@addSnapshotListener
                        } else if (value != null) {
                            CalendarNoteViewState(
                                false,
                                null,
                                value.toObjects(ApiNote::class.java)
                            )
                        }
                        value?.let {
                            offer(
                                CalendarNoteViewState(
                                    false,
                                    null,
                                    value.toObjects(ApiNote::class.java)
                                )
                            )
                        }
                    }
            } catch (e: Throwable) {
                offer(CalendarNoteViewState(false, e, null))
            }

            invokeOnClose {
                registration?.remove()
            }
        }

    override fun searchNotes(searchText: String): ReceiveChannel<CalendarNoteViewState<MutableList<ApiNote>>> =
        Channel<CalendarNoteViewState<MutableList<ApiNote>>>(Channel.CONFLATED).apply {
            var registration: ListenerRegistration? = null
            val str = searchText.substring(1, searchText.length - 1)
            try {
                registration = getUserNotesCollection
                    .whereEqualTo(TEXT_KEY_TO_SEARCH, str)
                    .orderBy(DATE_FIELD_TO_SEARCH)
                    .addSnapshotListener { value, error ->
                        if (error != null) {
                            CalendarNoteViewState(false, error, null)
                            return@addSnapshotListener
                        } else if (value != null) {
                            CalendarNoteViewState(
                                false,
                                null,
                                value.toObjects(ApiNote::class.java)
                            )
                        }
                        value?.let {
                            offer(
                                CalendarNoteViewState(
                                    false,
                                    null,
                                    value.toObjects(ApiNote::class.java)
                                )
                            )
                        }
                    }
            } catch (e: Throwable) {
                offer(CalendarNoteViewState(false, e, null))
            }

            invokeOnClose {
                registration?.remove()
            }
        }

    override fun getCoefRatingForDays(): Double {
        TODO("Not yet implemented")
    }

    override fun getNoteById(id1: Long): Maybe<ApiNote> =
        Maybe.create { emitter ->
            run {
                getUserNotesCollection
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
            val listenerRegistration = getUserNotesCollection
                .whereEqualTo(DONE_KEY, false)
                .whereEqualTo(TYPE_NOTE_TO_SEARCH, TASK_TYPE_VALUE)
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


    override suspend fun insertNote(note: ApiNote): Unit =
        run {
            getUserNotesCollection
                .document(note.id.toString())
                .set(note)
                .addOnSuccessListener {
                    note.id
                    return@addOnSuccessListener
                }
                .addOnFailureListener {
                    -1
                }
        }


    override suspend fun updateNote(note: ApiNote) = insertNote(note)

    override suspend fun deleteNote(apiNote: ApiNote) = deleteNoteById(apiNote.id)

    override suspend fun deleteNoteById(id1: Long) {
        run {
            getUserNotesCollection
                .document(id1.toString())
                .delete()
                .addOnSuccessListener {
                    1
                    return@addOnSuccessListener
                }
                .addOnFailureListener {
                    -1
                }
        }

    }

    //-----------------------For test LiveData---------------------------------------
    fun subscribeToAllNotes(): LiveData<CalendarNoteViewState<List<ApiNote?>>> =
        MutableLiveData<CalendarNoteViewState<List<ApiNote?>>>().apply {
            try {
                getUserNotesCollection.addSnapshotListener { snapshot, e ->
                    e?.let {
                        value = CalendarNoteViewState(false, e, null)
                    } ?: let {
                        snapshot?.let {
                            val notes = snapshot.documents.map { doc ->
                                doc.toObject(ApiNote::class.java)
                            }
                            value = CalendarNoteViewState(false, null, notes)
                        }
                    }
                }
            } catch (e: Throwable) {
                value = CalendarNoteViewState(false, e, null)
            }
        }

    fun saveNote(note: ApiNote) = MutableLiveData<CalendarNoteViewState<ApiNote>>().apply {
        try {
            getUserNotesCollection.document(note.id.toString()).set(note)
                .addOnSuccessListener {
                    value = CalendarNoteViewState<ApiNote>(false, null, note)
                }.addOnFailureListener {
                    value = CalendarNoteViewState(false, it, null)
                }
        } catch (e: Throwable) {
            value = CalendarNoteViewState(false, e, null)
        }
    }

    override fun getCountFinishTasks(startDay: Long, endDay: Long): Int {
        TODO("Not yet implemented")
    }
}