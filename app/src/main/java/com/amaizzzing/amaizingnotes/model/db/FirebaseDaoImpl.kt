package com.amaizzzing.amaizingnotes.model.db

import android.util.Log
import com.amaizzzing.amaizingnotes.NotesApplication
import com.amaizzzing.amaizingnotes.model.api_model.ApiNote
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import io.reactivex.Flowable
import io.reactivex.Maybe

class FirebaseDaoImpl : FirebaseDao {
    override fun getAllNotes(): Flowable<List<ApiNote>> {
        var result : Flowable<List<ApiNote>> = Flowable.just(listOf())
        Flowable.just(NotesApplication.instance.getFirebaseNotesReference().addSnapshotListener { value, error ->
            if(error!=null){
                result = Flowable.just(listOf())
            }else if (value != null) {
                val notes = mutableListOf<ApiNote>()

                for (doc: QueryDocumentSnapshot in value) {
                    notes.add(doc.toObject(ApiNote::class.java))
                }
                result = Flowable.fromArray(notes)
            }

        })
        NotesApplication.instance.getFirebaseNotesReference().addSnapshotListener { value, error ->
            if(error!=null){
                result = Flowable.just(listOf())
            }else if (value != null) {
                val notes = mutableListOf<ApiNote>()

                for (doc: QueryDocumentSnapshot in value) {
                    notes.add(doc.toObject(ApiNote::class.java))
                }
                result = Flowable.fromArray(notes)
            }

        }
        return result
    }

    override fun insertNote(note: ApiNote): Maybe<Long> {
        var result:Maybe<Long> = Maybe.just(-1)
        NotesApplication.instance.getFirebaseNotesReference().document(note.id.toString()).set(note)
            .addOnSuccessListener {
                    result = Maybe.just(note.id)
            }
            .addOnFailureListener {
                result = Maybe.just(-1)
            }
        return result
    }
}