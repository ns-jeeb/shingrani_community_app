package com.locked.shingranicommunity.authenticate.ui.login

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.authenticate.LoginEvent
import com.locked.shingranicommunity.authenticate.register.RegistrationActivity
import com.locked.shingranicommunity.dashboard.DashBoardViewPagerActivity
import com.locked.shingranicommunity.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity(), LoginEvent {
    override fun onLoginFailed(error: kotlin.Result<Any>) {
    }

    private lateinit var mBinding : ActivityLoginBinding
    override fun onLoginSuccess(): Boolean {
        return true
    }
    private lateinit var loginViewModel: LoginViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_login)

        loginViewModel = ViewModelProviders.of(this, LoginViewModelFactory(this)).get(LoginViewModel::class.java)

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            mBinding.btnLogin.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                mBinding.loginEmail.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                mBinding.loginPassword.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)
            }
            setResult(Activity.RESULT_OK)

        })

        mBinding.loginEmail.afterTextChanged {
            loginViewModel.loginDataChanged(
                mBinding.loginEmail.text.toString(),
                mBinding.loginPassword.text.toString()
            )
        }

        mBinding.loginPassword.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    mBinding.loginEmail.text.toString(),
                    mBinding.loginPassword.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                            mBinding.loginEmail.text.toString(),
                            mBinding.loginPassword.text.toString()
                        )
                }
                false
            }
//            loginViewModel.auth()
            mBinding.btnLogin.setOnClickListener {
                mBinding.loading.visibility = View.VISIBLE
                loginViewModel.login(mBinding.loginEmail.text.toString(), mBinding.loginPassword.text.toString())
            }
        }
        mBinding.btnJoiningPermission.setOnClickListener{
            var intent: Intent = Intent(this, RegistrationActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            this.finish()
        }
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        if (onLoginSuccess())
        mBinding.loginEmail.visibility = View.GONE

        var intent: Intent = Intent(this, DashBoardViewPagerActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        this.finish()


    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })

}
