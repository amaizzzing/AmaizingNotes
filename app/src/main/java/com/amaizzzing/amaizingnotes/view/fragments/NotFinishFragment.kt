package com.amaizzzing.amaizingnotes.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amaizzzing.amaizingnotes.R
import com.amaizzzing.amaizingnotes.adapters.TodayNotesAdapter
import com.amaizzzing.amaizingnotes.model.di.components.DaggerComponent2
import com.amaizzzing.amaizingnotes.model.di.modules.ClearModule
import com.amaizzzing.amaizingnotes.model.entities.Note
import com.amaizzzing.amaizingnotes.utils.SPAN_COUNT_RV
import com.amaizzzing.amaizingnotes.view.base.BaseFragment
import com.amaizzzing.amaizingnotes.view.view_states.NotFinishViewState
import com.amaizzzing.amaizingnotes.viewmodel.NotFinishViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_not_finish.view.*
import javax.inject.Inject

class NotFinishFragment :
    BaseFragment<MutableList<Note>, NotFinishViewState<MutableList<Note>>>() {
    @Inject
    lateinit var factory: ViewModelProvider.Factory
    override val viewModel: NotFinishViewModel by lazy {
        ViewModelProvider(this, factory).get(NotFinishViewModel::class.java)
    }
    override val layoutRes: Int = R.layout.fragment_not_finish
    override val rootView: View by lazy {
        this.layoutInflater.inflate(R.layout.fragment_not_finish, container, false)
    }

    private lateinit var rvFragmentNotFinish: RecyclerView
    private lateinit var todayNotesAdapter: TodayNotesAdapter
    private lateinit var navController: NavController
    private lateinit var pbNotFinishFragment: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val comp2 = DaggerComponent2.builder()
            .clearModule(ClearModule())
            .build()
        comp2.injectToNotFinishFragment(this)

        super.onCreateView(inflater, container, savedInstanceState)

        return rootView
    }

    override fun renderUI(data: NotFinishViewState<MutableList<Note>>) {
        renderProgress(data.isLoading)
        renderError(data.error)
        renderNoteList(data.data)
    }

    private fun renderError(error: Throwable?) {
        error?.let {
            Toast.makeText(context, "ERROR!", Toast.LENGTH_LONG).show()
        }
    }

    private fun renderNoteList(notes: List<Note>?) {
        notes?.let {
            initRecyclerView(notes)
        } ?: initRecyclerView(listOf())
    }

    private fun renderProgress(isLoad: Boolean) {
        if (isLoad) {
            pbNotFinishFragment.visibility = View.VISIBLE
            rvFragmentNotFinish.visibility = View.GONE
        } else {
            pbNotFinishFragment.visibility = View.GONE
            rvFragmentNotFinish.visibility = View.VISIBLE
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        navController = findNavController()
    }

    override fun initViews(v: View) {
        rvFragmentNotFinish = v.rv_fragment_not_finish
        pbNotFinishFragment = v.pb_not_finish_fragment
    }

    private fun initRecyclerView(notesList: List<Note>) {
        todayNotesAdapter = TodayNotesAdapter(notesList, object : TodayNotesAdapter.Callback {
            override fun onItemClicked(item: Note) {
                val bundle = Bundle()
                bundle.putLong(getString(R.string.current_note), item.id)
                navController.navigate(
                    R.id.action_navigation_not_finish_to_navigation_add_notes,
                    bundle
                )
            }

            override fun onChcbxChecked(item: Note, isChecked: Boolean) {
                item.isDone = isChecked
                viewModel.updateNote(item)
            }
        })
        rvFragmentNotFinish.layoutManager = GridLayoutManager(context, SPAN_COUNT_RV)
        rvFragmentNotFinish.adapter = todayNotesAdapter
    }

    override fun initListeners() {

    }
}
