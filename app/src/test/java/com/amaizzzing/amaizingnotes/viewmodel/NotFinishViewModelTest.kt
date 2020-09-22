package com.amaizzzing.amaizingnotes.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.amaizzzing.amaizingnotes.model.api_model.ApiNote
import com.amaizzzing.amaizingnotes.model.entities.Note
import com.amaizzzing.amaizingnotes.model.interactors.TodayNotesInteractor
import com.amaizzzing.amaizingnotes.model.mappers.NoteMapper
import com.amaizzzing.amaizingnotes.view.view_states.CalendarNoteViewState
import com.amaizzzing.amaizingnotes.view.view_states.NotFinishViewState
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import junit.framework.Assert
import junit.framework.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NotFinishViewModelTest {
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockInteractor = mockk<TodayNotesInteractor>()
    private val rxdata = Flowable.create<MutableList<Note>>({}, BackpressureStrategy.LATEST)

    private lateinit var viewModel: NotFinishViewModel

    @Before
    fun setup() {
        clearAllMocks()
        every { mockInteractor.getAllNotes(0,19111111111L) } returns rxdata
        viewModel = NotFinishViewModel(mockInteractor)
    }

    @Test
    fun `should call getNotes once`() {
        verify(exactly = 1) { mockInteractor.getNotFinishNotes()?.subscribe({},{t->t.printStackTrace()}) }
    }

    @Test
    fun `should return notes`(){
        var result: List<Note>? = null
        val testData = NoteMapper.listApiNoteToListNote(listOf(ApiNote(), ApiNote()))
        viewModel.viewStateLiveData.observeForever{
            result = it?.notes
        }
        viewModel.viewStateLiveData.value = NotFinishViewState(false,null,testData)
        Assert.assertEquals(testData, result)
    }

    @Test
    fun `should return error`(){
        var result: Throwable? = null
        val testData = Throwable("error")
        viewModel.viewStateLiveData.observeForever{
            result = it?.error
        }
        viewModel.viewStateLiveData.value = NotFinishViewState(false,testData,null)
        Assert.assertEquals(testData, result)
    }

    @Test
    fun `should remove observer`(){
        viewModel.onCleared()
        assertFalse(viewModel.viewStateLiveData.hasObservers())
    }
}