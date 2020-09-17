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

    override fun initListeners() {

    }

    private fun renderResults(allResults: AllResults?) {
        allResults?.let {
            rootView.count_notes_day_fragment_results.text = allResults.dayResults.countTasks.toString()
            rootView.count_notes_week_fragment_results.text = allResults.day7Results.countTasks.toString()
            rootView.count_notes_month_fragment_results.text = allResults.day30Results.countTasks.toString()
            rootView.rat_bar_days_fragment_results.rating = allResults.dayResults.rating
            rootView.rat_bar_weeks_fragment_results.rating = allResults.day7Results.rating
            rootView.rat_bar_months_fragment_results.rating = allResults.day30Results.rating
        }

    }

    private fun renderError(error: Throwable?) {
        error?.let {
            Toast.makeText(context, "ERROR!", Toast.LENGTH_LONG).show()
        }
    }

    private fun renderProgress(loading: Boolean) {
        if (loading) {
            rootView.pb_results_fragment.visibility = View.VISIBLE
            rootView.ll_rating_results_fragment.visibility = View.GONE
        } else {
            rootView.pb_results_fragment.visibility = View.GONE
            rootView.ll_rating_results_fragment.visibility = View.VISIBLE
        }
    }
}
