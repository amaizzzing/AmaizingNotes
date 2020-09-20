package com.amaizzzing.amaizingnotes.viewmodel

import androidx.lifecycle.viewModelScope
import com.amaizzzing.amaizingnotes.model.entities.AllResults
import com.amaizzzing.amaizingnotes.model.interactors.TodayNotesInteractor
import com.amaizzzing.amaizingnotes.utils.DAYS_30_IN_MILLIS
import com.amaizzzing.amaizingnotes.utils.DAYS_7_IN_MILLIS
import com.amaizzzing.amaizingnotes.utils.getEndDay
import com.amaizzzing.amaizingnotes.utils.getStartDay
import com.amaizzzing.amaizingnotes.view.base.BaseViewModel
import com.amaizzzing.amaizingnotes.view.view_states.ResultsViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*

class ResultsViewModel(val interactor: TodayNotesInteractor) :
    BaseViewModel<AllResults, ResultsViewState<AllResults>>() {
    private var resultsViewState: ResultsViewState<AllResults> = ResultsViewState(true, null, null)
    private var allResultsNotes = AllResults()

    init {
        viewModelScope.launch(Dispatchers.Main) {
            getRating()
            getResultsFromDB()
            viewStateLiveData.value = resultsViewState
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

        resultsViewState = try {
            scope.await()
            ResultsViewState(false, null, allResultsNotes)
        } catch (t: Throwable) {
            ResultsViewState(false, t, null)
        }
    }

    private fun getTasks(start: Long, end: Long): Int = interactor.getCountFinishTasks(start, end)
}