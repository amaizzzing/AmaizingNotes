package com.amaizzzing.amaizingnotes.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.amaizzzing.amaizingnotes.R
import com.amaizzzing.amaizingnotes.adapters.TodayNotesAdapter
import com.amaizzzing.amaizingnotes.model.entities.Note
import com.amaizzzing.amaizingnotes.utils.SPAN_COUNT_RV
import com.amaizzzing.amaizingnotes.view.base.BaseFragment
import com.amaizzzing.amaizingnotes.view.view_states.NotFinishViewState
import com.amaizzzing.amaizingnotes.viewmodel.NotFinishViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_not_finish.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class NotFinishFragment :
    BaseFragment<NotFinishViewState<MutableList<Note>>>() {
    override val viewModel: NotFinishViewModel by viewModel()
    override val layoutRes: Int = R.layout.fragment_not_finish
    override val rootView: View by lazy {
        this.layoutInflater.inflate(R.layout.fragment_not_finish, container, false)
    }

    private lateinit var todayNotesAdapter: TodayNotesAdapter
    val navController by lazy {
        findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
            rootView.pb_not_finish_fragment.visibility = View.VISIBLE
            rootView.rv_fragment_not_finish.visibility = View.GONE
        } else {
            rootView.pb_not_finish_fragment.visibility = View.GONE
            rootView.rv_fragment_not_finish.visibility = View.VISIBLE
        }
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
        rootView.rv_fragment_not_finish.layoutManager = GridLayoutManager(context, SPAN_COUNT_RV)
        rootView.rv_fragment_not_finish.adapter = todayNotesAdapter
    }

    override fun initListeners() {

    }
}
