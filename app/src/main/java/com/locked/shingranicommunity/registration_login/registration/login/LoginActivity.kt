package com.locked.shingranicommunity.registration_login.registration.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.*
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.dashboard.DashBoardViewPagerActivity
import com.locked.shingranicommunity.databinding.ActivityLoginBinding
import com.locked.shingranicommunity.registration_login.registration.MyApplication
import com.locked.shingranicommunity.registration_login.registration.RegistrationActivity
import javax.inject.Inject

class LoginActivity : AppCompatActivity() {

    lateinit var mBinding: ActivityLoginBinding
    @Inject
    lateinit var loginViewModel: LoginViewModel
    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {

        (application as MyApplication).appComponent.loginComponent().create().inject(this)
        super.onCreate(savedInstanceState)
        var mesage = ""

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        if (!intent.getStringExtra("message").isNullOrEmpty()) {
            mesage = intent.getStringExtra("message")
            mBinding.txtError.text = mesage
            mBinding.txtError.visibility = View.VISIBLE
        }
        loginViewModel =
            ViewModelProviders.of(this, viewModelProvider).get(LoginViewModel::class.java)
        //mBinding.loading.visibility = View.VISIBLE

        if (!loginViewModel.getToken().isBlank() && loginViewModel.getCurrentUser() != null) {
            //hideOrShowProgress(false)
            loginViewModel.fetchedSingleApi()
            startActivity(Intent(this, DashBoardViewPagerActivity::class.java))
            finish()
        } else {
            //hideOrShowProgress(false)
        }

        loginViewModel.loginState.observe(this, Observer {
            var loginState = it ?: return@Observer
            if (loginState.usernameError != null) {
                mBinding.loginEmail.error = getString(R.string.invalid_username)
            }
            if (loginState.passwordError != null) {
                mBinding.loginPassword.error = getString(R.string.error_incorrect_password)
            }
        })
        loginViewModel.loginResult.observe(this, Observer {
            var loginResult = it ?: return@Observer
            //mBinding.loading.visibility = View.GONE
            if (loginResult.error != null) {
                Toast.makeText(this, "login is Field", Toast.LENGTH_LONG).show()
            }
            when {
                loginResult.error != null -> {
                    //mBinding.loading.visibility = View.GONE
                    Toast.makeText(this, "login is Failed", Toast.LENGTH_LONG).show()
                }
                loginResult.success -> {
                    startActivity(Intent(this, DashBoardViewPagerActivity::class.java))
                    finish()
                }
                else -> {
                    Toast.makeText(this, "login is Field", Toast.LENGTH_LONG).show()
                }
            }
        })
        mBinding.loginEmail.doAfterTextChanged {
            loginViewModel.loginDataChanged(
                mBinding.loginEmail.text.toString(),
                mBinding.loginPassword.text.toString()
            )
        }
        mBinding.loginPassword.apply {
            doAfterTextChanged {
                loginViewModel.loginDataChanged(
                    mBinding.loginEmail.text.toString(),
                    mBinding.loginPassword.text.toString()
                )
            }
            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.loginDataChanged(
                            mBinding.loginEmail.text.toString(),
                            mBinding.loginPassword.text.toString()
                        )
                }
                false
            }
        }

        setupViews()
    }

    private fun setupViews() {

        mBinding.loginPassword.doOnTextChanged { _, _, _, _ ->
            mBinding.txtError?.visibility = View.INVISIBLE
        }
//        if (!loginViewModel.getUsername().isBlank()) {
//            mBinding.loginEmail.setText(loginViewModel.getUsername())
//            mBinding.loginEmail.isEnabled = false
//        } else {
//            mBinding.loginEmail.isEnabled = true
//        }
        mBinding.btnLogin.setOnClickListener {
            loginViewModel.login(
                mBinding.loginEmail.text.toString(),
                mBinding.loginPassword.text.toString()
            ).observe(this, Observer {
                if (it.isDataValid) {
                    loginViewModel.fetchedSingleApi()
                    val intent = Intent(this, DashBoardViewPagerActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                } else {
                    mBinding.txtError.visibility = View.VISIBLE
                    mBinding.txtError.text = it.message
                }
            })
        }
        mBinding.btnInvited.setOnClickListener {
            loginViewModel.unregister()
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }
    }
}

data class LoginFormState(
    val usernameError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false,
    val message: String ? = null
)

class LoginResult(
    val success: Boolean = false,
    val error: Int? = null
)