package com.amaizzzing.amaizingnotes.view.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amaizzzing.amaizingnotes.NotesApplication
import com.amaizzzing.amaizingnotes.R
import com.amaizzzing.amaizingnotes.adapters.TodayNotesAdapter
import com.amaizzzing.amaizingnotes.models.entities.Note
import com.amaizzzing.amaizingnotes.models.mappers.NoteMapper
import com.amaizzzing.amaizingnotes.viewmodel.TodayNotesViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    lateinit var todayNotesViewModel: TodayNotesViewModel
    lateinit var rvFragmentHome: RecyclerView
    lateinit var todayNotesAdapter: TodayNotesAdapter
    lateinit var fabButtonFragmentHome: FloatingActionButton
    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        initViews(root)
        initListeners()

        todayNotesViewModel = ViewModelProvider(this).get(TodayNotesViewModel::class.java)

        todayNotesViewModel.fetchAllData()?.observe(viewLifecycleOwner, Observer {
            initRecyclerView(it)
        })

        lifecycle.addObserver(todayNotesViewModel)

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        navController = findNavController()
    }

    /*override fun onStart() {
        super.onStart()
        todayNotesViewModel.onStart()
    }*/

    /*override fun onStop() {
        super.onStop()
        todayNotesViewModel.onStop()
    }*/

    fun initViews(v:View) {
        rvFragmentHome = v.findViewById(R.id.rv_fragment_home)
        fabButtonFragmentHome = v.findViewById(R.id.fab_button_fragment_home)
    }

    fun initListeners(){
        fabButtonFragmentHome.setOnClickListener {
            navController.navigate(R.id.action_navigation_home_to_navigation_add_notes)
        }
    }

    fun initRecyclerView(notesList: List<Note>) {
        todayNotesAdapter = TodayNotesAdapter(notesList, object : TodayNotesAdapter.Callback {
            override fun onItemClicked(item: Note) {
                val bundle = Bundle()
                bundle.putLong(getString(R.string.current_note), item.id)
                navController.navigate(R.id.action_navigation_home_to_navigation_add_notes, bundle)
            }

            override fun onDeleteClicked(item:Note) {
                val noteDao = NotesApplication.instance.getAppNoteDao()
                with(CoroutineScope(SupervisorJob() + Dispatchers.IO)) {
                    launch {
                        noteDao?.deleteNode(NoteMapper.noteToApiNote(item))
                    }
                }
            }
        })
        rvFragmentHome.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        rvFragmentHome.adapter = todayNotesAdapter
    }
}
