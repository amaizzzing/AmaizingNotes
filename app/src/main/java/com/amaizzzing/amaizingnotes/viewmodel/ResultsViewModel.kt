package com.amaizzzing.amaizingnotes.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amaizzzing.amaizingnotes.model.entities.AllResults
import com.amaizzzing.amaizingnotes.model.interactors.TodayNotesInteractor
import com.amaizzzing.amaizingnotes.utils.DAYS_30_IN_MILLIS
import com.amaizzzing.amaizingnotes.utils.DAYS_7_IN_MILLIS
import com.amaizzzing.amaizingnotes.utils.getEndDay
import com.amaizzzing.amaizingnotes.utils.getStartDay
import com.amaizzzing.amaizingnotes.view.view_states.ResultsViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class ResultsViewModel @Inject constructor(var interactor: TodayNotesInteractor) : ViewModel() {
    private var resultsViewState: ResultsViewState = ResultsViewState(true, null, null)
    var results: MutableLiveData<ResultsViewState> = MutableLiveData(resultsViewState)
    private var allResultsNotes = AllResults()

    init {
        viewModelScope.launch(Dispatchers.Main) {
            getRating()
            getResultsFromDB()
            results.value = resultsViewState
        }
    }

    private suspend fun getRating() {
        val rat = viewModelScope.async(Dispatchers.IO) {
            allResultsNotes.dayResults.rating = interactor.getCoefRatingForDays()
            allResultsNotes.day7Results.rating = 2f
            allResultsNotes.day30Results.rating = 1f
        }
        try {
            rat.await()
        } catch (t: Throwable) {
            resultsViewState = ResultsViewState(false, t, null)
        }
    }

    private suspend fun getResultsFromDB() {
        val scope = viewModelScope.async(Dispatchers.IO) {
            allResultsNotes.dayResults.countTasks = async {
                getTasks(
                    Date().getStartDay(Calendar.getInstance().time.time),
                    Date().getEndDay(Calendar.getInstance().time.time)
                )
            }.await()
            allResultsNotes.day7Results.countTasks = async {
                getTasks(
                    Date().getStartDay(Calendar.getInstance().time.time),
                    Date().getEndDay(Calendar.getInstance().time.time + DAYS_7_IN_MILLIS)
                )
            }.await()
            allResultsNotes.day30Results.countTasks = async {
                getTasks(
                    Date().getStartDay(Calendar.getInstance().time.time),
                    Date().getEndDay(Calendar.getInstance().time.time + DAYS_30_IN_MILLIS)
                )
            }.await()
        }

        try {
            scope.await()
            resultsViewState = ResultsViewState(false, null, allResultsNotes)
        } catch (t: Throwable) {
            resultsViewState = ResultsViewState(false, t, null)
        }
    }

    private fun getTasks(start: Long, end: Long): Int = interactor.getCountFinishTasks(start, end)
}