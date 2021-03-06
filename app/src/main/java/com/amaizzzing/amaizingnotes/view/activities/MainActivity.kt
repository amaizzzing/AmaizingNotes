package com.amaizzzing.amaizingnotes.view.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.amaizzzing.amaizingnotes.R
import com.amaizzzing.amaizingnotes.utils.MY_WORKER_TYPE
import com.amaizzzing.amaizingnotes.utils.MyWorkerFactory
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    companion object {
        fun start(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        configNavController()
        startWork()
    }

    private fun startWork() {
        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            "mywork",
            ExistingPeriodicWorkPolicy.KEEP,
            MyWorkerFactory.getWorker(MY_WORKER_TYPE.PERIODIC) as PeriodicWorkRequest
        ).state.observeForever {}
    }

    private fun configNavController() {
        val navView = nav_view
        val navController = findNavController(R.id.nav_host_fragment)
        AppBarConfiguration(
            setOf(
                R.id.navigation_calendar,
                R.id.navigation_not_finish,
                R.id.navigation_results
            )
        )
        navView.setupWithNavController(navController)
    }
}

