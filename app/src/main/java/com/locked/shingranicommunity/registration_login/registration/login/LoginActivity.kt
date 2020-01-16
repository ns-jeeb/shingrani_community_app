package com.locked.shingranicommunity.registration_login.registration.login
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.locked.shingranicommunity.MainActivity
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.dashboard.DashBoardViewPagerActivity
import com.locked.shingranicommunity.databinding.ActivityLoginBinding
import com.locked.shingranicommunity.di.Storage
import com.locked.shingranicommunity.registration_login.registration.MyApplication
import com.locked.shingranicommunity.registration_login.registration.RegistrationActivity
import javax.inject.Inject

class LoginActivity : AppCompatActivity() {

    lateinit var mBinding: ActivityLoginBinding
    @Inject
    lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        (application as MyApplication).appComponent.loginComponent().create().inject(this)
        super.onCreate(savedInstanceState)

        var mesage = ""
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_login)
        if (!intent.getStringExtra("message").isNullOrEmpty()) {
            mesage = intent.getStringExtra("message")
            mBinding.txtError.text = mesage
            mBinding.txtError.visibility = View.VISIBLE
        }
        loginViewModel.loginState.observe(this, Observer<LoginViewState> { state ->
            when (state) {
                is LoginSuccess -> {
                    startActivity(Intent(this, DashBoardViewPagerActivity::class.java))
                    finish()
                }
                is LoginError ->{
                    mBinding.txtError?.visibility = View.VISIBLE
                }
            }
        })

        setupViews()
    }

    private fun setupViews() {
        var username: String
        if (loginViewModel.getUsername().isBlank()) {
            mBinding.loginEmail.isEnabled = true
            username = mBinding.loginEmail.text.toString()
        }else{
            mBinding.loginEmail.isEnabled = false
            mBinding.txtUserName.text = loginViewModel.getUsername()
            username = loginViewModel.getUsername()
        }

        mBinding.loginPassword.doOnTextChanged { _, _, _, _ -> mBinding.txtError?.visibility = View.INVISIBLE }

        mBinding.btnLogin.setOnClickListener {
            if (username.isBlank() || mBinding.loginPassword.text.toString().isBlank()){
                username = mBinding.loginEmail.text.toString()
            }
            loginViewModel.login(username, mBinding.loginPassword.text.toString())
        }
        mBinding.btnJoiningPermission.setOnClickListener {
            loginViewModel.unregister()
            val intent = Intent(this, RegistrationActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or
                    Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }
}

sealed class LoginViewState
object LoginSuccess : LoginViewState()
object LoginError : LoginViewState()
