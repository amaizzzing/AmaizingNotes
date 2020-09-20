package com.amaizzzing.amaizingnotes.view.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.amaizzzing.amaizingnotes.R
import com.amaizzzing.amaizingnotes.model.NoAuthException
import com.amaizzzing.amaizingnotes.viewmodel.SplashViewModel
import com.firebase.ui.auth.AuthUI
import org.koin.android.viewmodel.ext.android.viewModel

const val REQUEST_CODE = 4242

class SplashActivity : AppCompatActivity() {
    private val viewModel1: SplashViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

    private fun startLogin() {
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

    private fun renderError(error: Throwable?) {
        when (error) {
            is NoAuthException -> startLogin()
            else -> error?.message?.let { message ->
                showError(message)
            }
        }
    }

    private fun renderData(data: Boolean?) {
        data?.takeIf { it }?.let {
            startMainActivity()
        }
    }

    private fun startMainActivity() {
        MainActivity.start(this)
        finish()
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
