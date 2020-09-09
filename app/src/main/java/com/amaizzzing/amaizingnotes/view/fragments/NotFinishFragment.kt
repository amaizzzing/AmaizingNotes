package com.amaizzzing.amaizingnotes.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
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
import com.amaizzzing.amaizingnotes.view.base.BaseViewState
import com.amaizzzing.amaizingnotes.view.view_states.NotFinishViewState
import com.amaizzzing.amaizingnotes.viewmodel.NotFinishViewModel
import kotlinx.android.synthetic.main.fragment_not_finish.view.*
import javax.inject.Inject

class NotFinishFragment : Fragment() {
    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private lateinit var notFinishViewModel: NotFinishViewModel
    private lateinit var rvFragmentNotFinish: RecyclerView
    private lateinit var todayNotesAdapter: TodayNotesAdapter
    private lateinit var navController: NavController
    private lateinit var pbNotFinishFragment: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_not_finish, container, false)

        initViews(root)

        val comp2 = DaggerComponent2.builder()
            .clearModule(ClearModule())
            .build()
        comp2.injectToNotFinishFragment(this)

        notFinishViewModel =
            ViewModelProvider(this, factory).get(NotFinishViewModel::class.java)

        notFinishViewModel.listNotFinishNotes.observe(viewLifecycleOwner, Observer {
            renderUI(it)
        })

        return root
    }

    private fun renderUI(noteViewState: BaseViewState<MutableList<Note>>) {
        renderProgress(noteViewState.isLoading)
        renderError(noteViewState.error)
        renderNoteList(noteViewState.data)
    }

    private fun renderError(error: Throwable?) {
        if (error != null) {
            Toast.makeText(context, "ERROR!", Toast.LENGTH_LONG).show()
        }
    }

    private fun renderNoteList(notes: List<Note>?) {
        if (notes != null) {
            initRecyclerView(notes)
        }
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

    private fun initViews(v: View) {
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
                notFinishViewModel.updateNote(item)
            }
        })
        rvFragmentNotFinish.layoutManager = GridLayoutManager(context, SPAN_COUNT_RV)
        rvFragmentNotFinish.adapter = todayNotesAdapter
    }
}
