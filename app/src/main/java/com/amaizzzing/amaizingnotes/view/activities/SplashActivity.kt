package com.amaizzzing.amaizingnotes.view.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.amaizzzing.amaizingnotes.R
import com.amaizzzing.amaizingnotes.model.NoAuthException
import com.amaizzzing.amaizingnotes.model.di.components.DaggerComponent2
import com.amaizzzing.amaizingnotes.model.di.modules.ClearModule
import com.amaizzzing.amaizingnotes.viewmodel.SplashViewModel
import com.firebase.ui.auth.AuthUI
import javax.inject.Inject

const val REQUEST_CODE = 4242

class SplashActivity : AppCompatActivity() {
    @Inject
    lateinit var factory: ViewModelProvider.Factory

    val viewModel1 : SplashViewModel by lazy { ViewModelProvider(this, factory).get(SplashViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val comp2 = DaggerComponent2.builder()
            .clearModule(ClearModule())
            .build()
        comp2.injectToSplashActivity(this)

        viewModel1.viewStateLiveData.observe(this, Observer { state ->
            state ?: return@Observer
            state.error?.let { e ->
                renderError(e)
                return@Observer
            }
            renderData(state.data != null)
        })
    }


    override fun onResume() {
        super.onResume()
        viewModel1.requestUser()
    }

    private fun startLogin(){
        val providers = listOf(
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        val intent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setLogo(R.drawable.ic_calendar_24dp)
            .setTheme(R.style.LoginStyle)
            .setAvailableProviders(providers)
            .build()

        startActivityForResult(intent, REQUEST_CODE)

    }
    protected fun renderError(error: Throwable?) {
        when(error){
            is NoAuthException -> startLogin()
            else ->  error?.message?.let { message ->
                showError(message)
            }
        }
    }

    fun renderData(data: Boolean?) {
        data?.takeIf { it }?.let {
            startMainActivity()
        }
    }

    private fun startMainActivity() {
        MainActivity.start(this)
        finish()
    }

    protected fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
