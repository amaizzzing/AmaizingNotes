package com.amaizzzing.amaizingnotes.model.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.amaizzzing.amaizingnotes.model.NoAuthException
import com.amaizzzing.amaizingnotes.model.api_model.ApiNote
import com.amaizzzing.amaizingnotes.model.db.FirebaseDaoImpl.Companion.DATE_FIELD_TO_SEARCH
import com.amaizzzing.amaizingnotes.view.view_states.CalendarNoteViewState
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import io.mockk.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FirebaseDaoImplTest {
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockDb = mockk<FirebaseFirestore>()
    private val mockAuth = mockk<FirebaseAuth>()
    private val mockResultCollection = mockk<CollectionReference>()
    private val mockUser = mockk<FirebaseUser>()

    private val mockDocument1 = mockk<DocumentSnapshot>()
    private val mockDocument2 = mockk<DocumentSnapshot>()
    private val mockDocument3 = mockk<DocumentSnapshot>()

    private val testNotes = listOf(ApiNote(), ApiNote(), ApiNote())

    private val provider = FirebaseDaoImpl(mockDb, mockAuth)

    @Before
    fun setup() {
        clearAllMocks()
        every { mockAuth.currentUser } returns mockUser
        every { mockUser.uid } returns ""
        every {
            mockDb.collection(any()).document(any()).collection(any())
        } returns mockResultCollection
        every { mockDocument1.toObject(ApiNote::class.java) } returns testNotes[0]
        every { mockDocument2.toObject(ApiNote::class.java) } returns testNotes[1]
        every { mockDocument3.toObject(ApiNote::class.java) } returns testNotes[2]
    }

    @Test
    fun `should throw NoAuthException if no auth`() {
        var result: Any? = null
        every { mockAuth.currentUser } returns null

        provider.getAllNotes(0L, 1900502642777L)
            .subscribe({},
                { t -> result = t as NoAuthException })

        assertTrue(result is NoAuthException)
    }

    @Test
    fun `save note calls set`() {
        val mockDocumentReference = mockk<DocumentReference>(relaxed = true)
        every { mockResultCollection.document(testNotes[1].id.toString()) } returns mockDocumentReference

        provider.insertNote(testNotes[1])
            .subscribe({}, { t -> t.printStackTrace() })

        verify(exactly = 1) { mockDocumentReference.set(testNotes[1]) }
    }

    @Test
    fun `subscribeToAllNotes returns notes`() {
        var result: List<ApiNote>? = null
        val mockSnapshot = mockk<QuerySnapshot>(relaxed = true)
        val slot = slot<EventListener<QuerySnapshot>>()
        every { mockSnapshot.documents } returns listOf(mockDocument1, mockDocument2, mockDocument3)
        every {
            mockResultCollection
                .whereGreaterThan(DATE_FIELD_TO_SEARCH, 0L)
                .whereLessThan(DATE_FIELD_TO_SEARCH, 1900502642777L)
                .orderBy(DATE_FIELD_TO_SEARCH, Query.Direction.DESCENDING)
                .addSnapshotListener(capture(slot))
        } returns mockk()

        provider.getAllNotes(0L, 1900502642777L)
            .doOnNext {
                slot.captured.onEvent(mockSnapshot, null);
                result = it }
            .subscribe(
                {},
                { t -> t.printStackTrace() }
            )

        assertEquals(result, testNotes)
    }

    @Test
    fun `saveNote returns Note`() {
        var result: Long = -1
        val mockDocumentReference = mockk<DocumentReference>(relaxed = true)
        val slot = slot<OnSuccessListener<in Void>>()

        every { mockResultCollection.document(testNotes[0].id.toString()) } returns mockDocumentReference
        every { mockDocumentReference.set(testNotes[0]).addOnSuccessListener(capture(slot)) } returns mockk()

        provider.insertNote(testNotes[1])
            .doOnSuccess {
                slot.captured.onSuccess(null)
                result = it
            }
            .subscribe({result = it}, { t -> t.printStackTrace() })
        assertEquals(result, testNotes[0].id)
    }

//--------------------------------Tests for LiveData------------------------------------------------
    @Test
    fun `subscribeToAllNotes returns notes for LiveData`() {
        var result: List<ApiNote?>? = null
        val mockShapshot = mockk<QuerySnapshot>()
        val slot = slot<EventListener<QuerySnapshot>>()

        every { mockShapshot.documents } returns listOf(mockDocument1, mockDocument2, mockDocument3)
        every { mockResultCollection.addSnapshotListener(capture(slot)) } returns mockk()

        provider.subscribeToAllNotes().observeForever {
            result = it.notes
        }
        slot.captured.onEvent(mockShapshot, null)
        assertEquals(result, testNotes)
    }

    @Test
    fun `subscribeToAllNotes returns error for LiveData`() {
        var result: Throwable? = null
        val mockException = mockk<FirebaseFirestoreException>()
        val slot = slot<EventListener<QuerySnapshot>>()
        every { mockResultCollection.addSnapshotListener(capture(slot)) } returns mockk()

        provider.subscribeToAllNotes().observeForever {
            result = it.error
        }
        slot.captured.onEvent(null, mockException)
        assertEquals(result, mockException)
    }

    @Test
    fun `saveNote returns Note for LiveData`() {
        var result: ApiNote? = null
        val mockDocumentReference = mockk<DocumentReference>()
        val slot = slot<OnSuccessListener<in Void>>()

        every { mockResultCollection.document(testNotes[0].id.toString()) } returns mockDocumentReference
        every { mockDocumentReference.set(testNotes[0]).addOnSuccessListener(capture(slot)) } returns mockk()

        provider.saveNote(testNotes[0]).observeForever {
            result = it.notes
        }

        slot.captured.onSuccess(null)
        assertEquals(result, testNotes[0])
    }
}