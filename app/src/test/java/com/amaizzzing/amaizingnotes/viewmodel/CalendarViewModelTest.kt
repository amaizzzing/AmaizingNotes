package com.amaizzzing.amaizingnotes.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.amaizzzing.amaizingnotes.model.api_model.ApiNote
import com.amaizzzing.amaizingnotes.model.entities.Note
import com.amaizzzing.amaizingnotes.model.interactors.TodayNotesInteractor
import com.amaizzzing.amaizingnotes.model.mappers.NoteMapper
import com.amaizzzing.amaizingnotes.view.view_states.CalendarNoteViewState
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableOnSubscribe
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CalendarViewModelTest {
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockInteractor = mockk<TodayNotesInteractor>()
    private val rxdata = Flowable.create<MutableList<Note>>({},BackpressureStrategy.LATEST)

    private lateinit var viewModel: CalendarViewModel

    @Before
    fun setup() {
        clearAllMocks()
        every { mockInteractor.getAllNotes(0,19111111111L) } returns rxdata
        viewModel = CalendarViewModel(mockInteractor)
    }

    @Test
    fun `should call getNotes once`() {
        verify(exactly = 1) { mockInteractor.getAllNotes(0,19111111111L).subscribe({},{t->t.printStackTrace()}) }
    }

    @Test
    fun `should return notes`(){
        var result: List<Note>? = null
        val testData = NoteMapper.listApiNoteToListNote(listOf(ApiNote(), ApiNote()))
        viewModel.viewStateLiveData.observeForever{
            result = it?.notes
        }
        viewModel.viewStateLiveData.value = CalendarNoteViewState(false,null,testData)
        assertEquals(testData, result)
    }

    @Test
    fun `should return error`(){
        var result: Throwable? = null
        val testData = Throwable("error")
        viewModel.viewStateLiveData.observeForever{
            result = it?.error
        }
        viewModel.viewStateLiveData.value = CalendarNoteViewState(false,testData,null)
        assertEquals(testData, result)
    }

    @Test
    fun `should remove observer`(){
        viewModel.onCleared()
        assertFalse(viewModel.viewStateLiveData.hasObservers())
    }
}