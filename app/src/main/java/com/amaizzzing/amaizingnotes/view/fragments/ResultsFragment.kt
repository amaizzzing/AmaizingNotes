package com.amaizzzing.amaizingnotes.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.amaizzzing.amaizingnotes.R
import com.amaizzzing.amaizingnotes.model.di.components.DaggerComponent2
import com.amaizzzing.amaizingnotes.model.di.modules.ClearModule
import com.amaizzzing.amaizingnotes.viewmodel.ResultsViewModel
import kotlinx.android.synthetic.main.fragment_results.view.*
import javax.inject.Inject

class ResultsFragment : Fragment() {
    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private lateinit var resultViewModel: ResultsViewModel

    lateinit var countNotesDayFragmentResults: TextView
    lateinit var countNotesWeekFragmentResults: TextView
    lateinit var countNotesMonthFragmentResults: TextView
    lateinit var ratBarDaysFragmentResults: RatingBar
    lateinit var ratBarWeeksFragmentResults: RatingBar
    lateinit var ratBarMonthsFragmentResults: RatingBar
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

        resultViewModel =
            ViewModelProvider(this, factory).get(ResultsViewModel::class.java)
        resultViewModel.results?.observe(viewLifecycleOwner, Observer {

            countNotesDayFragmentResults.text = it.second.first.toString()
            countNotesWeekFragmentResults.text = it.second.second.toString()
            countNotesMonthFragmentResults.text = it.second.third.toString()
            ratBarDaysFragmentResults.rating = it.first.first
            ratBarWeeksFragmentResults.rating = it.first.second
            ratBarMonthsFragmentResults.rating = it.first.third
        })

        return root
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
