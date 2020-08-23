package com.amaizzzing.amaizingnotes.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.amaizzzing.amaizingnotes.NotesApplication
import com.amaizzzing.amaizingnotes.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        configNavController()

        /*GlobalScope.launch { insertions() }*/



        /*Completable.fromAction {
            noteDao?.insertNote(ApiNote(1,333333L,333333L,333333L,"my"))
            noteDao?.insertNote(ApiNote(2,333333L,333333L,333333L,"my"))
            noteDao?.insertNote(ApiNote(3,333333L,333333L,333333L,"my"))
            noteDao?.insertNote(ApiNote(4,333333L,333333L,333333L,"my"))
            noteDao?.insertNote(ApiNote(5,333333L,333333L,333333L,"my"))
            noteDao?.insertNote(ApiNote(6,333333L,333333L,333333L,"my"))
            noteDao?.insertNote(ApiNote(7,333333L,333333L,333333L,"my"))}
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()*/
    }

    /*suspend fun insertions(){
        val noteDao : NoteDao? = NotesApplication.instance.getAppDataBase(this)?.noteDao()
        with(CoroutineScope(SupervisorJob() + Dispatchers.IO)){
            launch{
                noteDao?.insertNote(ApiNote(11,333333L,333333L,333333L,"11"))
                noteDao?.insertNote(ApiNote(12,333333L,333333L,333333L,"12"))
                noteDao?.insertNote(ApiNote(13,333333L,333333L,333333L,"13"))
                noteDao?.insertNote(ApiNote(14,333333L,333333L,333333L,"14"))
                noteDao?.insertNote(ApiNote(15,333333L,333333L,333333L,"15"))
                noteDao?.insertNote(ApiNote(16,333333L,333333L,333333L,"16"))
                noteDao?.insertNote(ApiNote(17,333333L,333333L,333333L,"17"))
            }.join()
        }
    }*/

    fun configNavController() {
        val navView = nav_view
        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_dashboard,
                R.id.navigation_notifications
            )
        )
        //setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onDestroy() {
        NotesApplication.instance.getAppDataBase(this)?.close()
        NotesApplication.instance.destroyDataBase()

        super.onDestroy()
    }
}

