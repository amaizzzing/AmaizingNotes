package com.amaizzzing.amaizingnotes.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.amaizzzing.amaizingnotes.R
import com.amaizzzing.amaizingnotes.model.di.components.DaggerComponent2
import com.amaizzzing.amaizingnotes.model.di.modules.ClearModule
import com.amaizzzing.amaizingnotes.model.entities.AllResults
import com.amaizzzing.amaizingnotes.view.base.BaseViewState
import com.amaizzzing.amaizingnotes.view.view_states.ResultsViewState
import com.amaizzzing.amaizingnotes.viewmodel.ResultsViewModel
import kotlinx.android.synthetic.main.fragment_results.view.*
import javax.inject.Inject

class ResultsFragment : Fragment() {
    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private lateinit var resultViewModel: ResultsViewModel

    private lateinit var countNotesDayFragmentResults: TextView
    private lateinit var countNotesWeekFragmentResults: TextView
    private lateinit var countNotesMonthFragmentResults: TextView
    private lateinit var ratBarDaysFragmentResults: RatingBar
    private lateinit var ratBarWeeksFragmentResults: RatingBar
    private lateinit var ratBarMonthsFragmentResults: RatingBar
    private lateinit var pbResultsFragment: ProgressBar
    private lateinit var llRatingResultsFragment: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_results, container, false)

        val comp2 = DaggerComponent2.builder()
            .clearModule(ClearModule())
            .build()
        comp2.injectToResultsFragment(this)

        pbResultsFragment = root.pb_results_fragment
        llRatingResultsFragment = root.ll_rating_results_fragment

        resultViewModel =
            ViewModelProvider(this, factory).get(ResultsViewModel::class.java)
        resultViewModel.results.observe(viewLifecycleOwner, Observer {
            renderUI(it)
        })

        return root
    }

    private fun renderUI(resultsViewState: BaseViewState<AllResults>) {
        renderProgress(resultsViewState.isLoading)
        renderError(resultsViewState.error)
        renderResults(resultsViewState.data)
    }

    private fun renderResults(allResults: AllResults?) {
        if (allResults != null) {
            countNotesDayFragmentResults.text = allResults.dayResults.countTasks.toString()
            countNotesWeekFragmentResults.text = allResults.day7Results.countTasks.toString()
            countNotesMonthFragmentResults.text = allResults.day30Results.countTasks.toString()
            ratBarDaysFragmentResults.rating = allResults.dayResults.rating
            ratBarWeeksFragmentResults.rating = allResults.day7Results.rating
            ratBarMonthsFragmentResults.rating = allResults.day30Results.rating
        }

    }

    private fun renderError(error: Throwable?) {
        if (error != null) {
            Toast.makeText(context, "ERROR!", Toast.LENGTH_LONG).show()
        }
    }

    private fun renderProgress(loading: Boolean) {
        if (loading) {
            pbResultsFragment.visibility = View.VISIBLE
            llRatingResultsFragment.visibility = View.GONE
        } else {
            pbResultsFragment.visibility = View.GONE
            llRatingResultsFragment.visibility = View.VISIBLE
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        countNotesDayFragmentResults = view.count_notes_day_fragment_results
        countNotesWeekFragmentResults = view.count_notes_week_fragment_results
        countNotesMonthFragmentResults = view.count_notes_month_fragment_results
        ratBarDaysFragmentResults = view.rat_bar_days_fragment_results
        ratBarWeeksFragmentResults = view.rat_bar_weeks_fragment_results
        ratBarMonthsFragmentResults = view.rat_bar_months_fragment_results
    }
}
