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
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.authenticate.LoginEvent
import com.locked.shingranicommunity.dashboard.DashBoardViewPagerActivity
import com.locked.shingranicommunity.databinding.ActivityLoginBinding
import com.locked.shingranicommunity.databinding.ActivityMainBinding
import com.locked.shingranicommunity.tutorials.User
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity(), LoginEvent {
    override fun onLoginFailed(error: kotlin.Result<Any>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private lateinit var mBinding : ActivityLoginBinding
    override fun onLoginSuccess(user: User, isLogin: Boolean) {
        //here launching the new activity
        if (isLogin){
            Log.d("LoginLog","user** ${user.token} isLogin** $isLogin")
            this.finish()
            var intent: Intent = Intent(this, DashBoardViewPagerActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
    private lateinit var loginViewModel: LoginViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_login)

        val username = mBinding.username
        val password = mBinding.password
        val login = mBinding.login
        val loading = mBinding.loading


        loginViewModel = ViewModelProviders.of(this, LoginViewModelFactory(this)).get(LoginViewModel::class.java)

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)
            }
            setResult(Activity.RESULT_OK)

            //Complete and destroy login activity once successful
//            finish()

        })

        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                            username.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }
//            loginViewModel.auth()
            login.setOnClickListener {
                loading.visibility = View.VISIBLE
                loginViewModel.login(username.text.toString(), password.text.toString())
            }
        }
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName
        // TODO : initiate successful logged in experience
//        Toast.makeText(
//            applicationContext,
//            "$welcome $displayName",
//            Toast.LENGTH_LONG
//        ).show()

    }

    private fun showLoginFailed(@StringRes errorString: Int) {
//        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
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
