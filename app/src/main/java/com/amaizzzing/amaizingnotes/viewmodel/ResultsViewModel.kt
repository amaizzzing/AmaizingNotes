package com.amaizzzing.amaizingnotes.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amaizzzing.amaizingnotes.model.interactors.TodayNotesInteractor
import com.amaizzzing.amaizingnotes.utils.DAYS_30_IN_MILLIS
import com.amaizzzing.amaizingnotes.utils.DAYS_7_IN_MILLIS
import com.amaizzzing.amaizingnotes.utils.getEndDay
import com.amaizzzing.amaizingnotes.utils.getStartDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class ResultsViewModel @Inject constructor(var interactor: TodayNotesInteractor) : ViewModel() {
    var results: MutableLiveData<Pair<Triple<Float, Float, Float>, Triple<Int, Int, Int>>> =
        MutableLiveData()

    init {
        viewModelScope.launch(Dispatchers.Main) {
            results?.value = getRating() to getResultsFromDB()
        }
    }

    suspend fun getRating() =
        viewModelScope.async(Dispatchers.IO) {
            val day = interactor.getCoefRatingForDays()
            val week = 2f
            val months = 1f
            Triple(day, week, months)
        }.await()

    suspend fun getResultsFromDB() =
        viewModelScope.async(Dispatchers.IO) {
            val day3 = async {
                interactor.getCountFinishTasks(
                    Date().getStartDay(Calendar.getInstance().time.time),
                    Date().getEndDay(Calendar.getInstance().time.time + DAYS_7_IN_MILLIS)
                )
            }
            val week = async {
                interactor.getCountFinishTasks(
                    Date().getStartDay(Calendar.getInstance().time.time),
                    Date().getEndDay(Calendar.getInstance().time.time + DAYS_7_IN_MILLIS)
                )
            }
            val month = async {
                interactor.getCountFinishTasks(
                    Date().getStartDay(Calendar.getInstance().time.time),
                    Date().getEndDay(Calendar.getInstance().time.time + DAYS_30_IN_MILLIS)
                )
            }
            Triple(day3.await(), week.await(), month.await())
        }.await()
}