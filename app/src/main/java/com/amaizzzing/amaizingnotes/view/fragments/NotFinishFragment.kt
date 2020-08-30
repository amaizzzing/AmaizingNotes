package com.amaizzzing.amaizingnotes.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amaizzzing.amaizingnotes.R
import com.amaizzzing.amaizingnotes.adapters.TodayNotesAdapter
import com.amaizzzing.amaizingnotes.model.entities.Note
import com.amaizzzing.amaizingnotes.utils.SPAN_COUNT_RV
import com.amaizzzing.amaizingnotes.viewmodel.NotFinishViewModel
import kotlinx.android.synthetic.main.fragment_not_finish.view.*

class NotFinishFragment : Fragment() {
    private lateinit var notFinishViewModel: NotFinishViewModel
    private lateinit var rvFragmentNotFinish: RecyclerView
    private lateinit var todayNotesAdapter: TodayNotesAdapter
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_not_finish, container, false)

        initViews(root)

        notFinishViewModel =
            ViewModelProvider(this).get(NotFinishViewModel::class.java)

        notFinishViewModel.fetchAllNotFinishNotes()?.observe(viewLifecycleOwner, Observer {
            initRecyclerView(it)
        })


        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        navController = findNavController()
    }

    private fun initViews(v: View) {
        rvFragmentNotFinish = v.rv_fragment_not_finish
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
