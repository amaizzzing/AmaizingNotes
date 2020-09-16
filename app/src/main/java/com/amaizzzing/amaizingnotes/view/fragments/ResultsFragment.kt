package com.amaizzzing.amaizingnotes.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.amaizzzing.amaizingnotes.R
import com.amaizzzing.amaizingnotes.model.entities.AllResults
import com.amaizzzing.amaizingnotes.view.base.BaseFragment
import com.amaizzzing.amaizingnotes.view.view_states.ResultsViewState
import com.amaizzzing.amaizingnotes.viewmodel.ResultsViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_results.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class ResultsFragment : BaseFragment<AllResults, ResultsViewState<AllResults>>() {
    override val viewModel: ResultsViewModel by viewModel()
    override val layoutRes: Int = R.layout.fragment_results
    override val rootView: View by lazy {
        this.layoutInflater.inflate(R.layout.fragment_results, container, false)
    }

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
        super.onCreateView(inflater, container, savedInstanceState)

        return rootView
    }

    override fun renderUI(data: ResultsViewState<AllResults>) {
        renderProgress(data.isLoading)
        renderError(data.error)
        renderResults(data.data)
    }

    override fun initViews(v: View) {
        pbResultsFragment = v.pb_results_fragment
        llRatingResultsFragment = v.ll_rating_results_fragment
    }

    override fun initListeners() {

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
