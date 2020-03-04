package com.locked.shingranicommunity.registration_login.registration.login
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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
    lateinit var viewModelProvider : ViewModelProvider.Factory

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
        loginViewModel = ViewModelProviders.of(this, viewModelProvider).get(LoginViewModel::class.java)
        mBinding.loading.visibility = View.VISIBLE

        var token = getSharedPreferences("token", Context.MODE_PRIVATE).getString("token","")

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

        if (!token!!.isBlank()) {
            hideOrShowProgress(false)
            startActivity(Intent(this, DashBoardViewPagerActivity::class.java))
            finish()
        }else{
            hideOrShowProgress(false)
        }
        setupViews()
    }

    private fun setupViews() {

        mBinding.loginPassword.doOnTextChanged { _, _, _, _ -> mBinding.txtError?.visibility = View.INVISIBLE }
        if (!loginViewModel.getUsername().isBlank()){
            mBinding.loginEmail.setText(loginViewModel.getUsername())
            mBinding.loginEmail.isEnabled = false
        }else{
            mBinding.loginEmail.isEnabled = true
        }
        mBinding.btnLogin.setOnClickListener {
            if (loginViewModel.validateInput(mBinding.txtUserName.text.toString(),mBinding.loginPassword.text.toString())){
                loginViewModel.login(mBinding.loginEmail.text.toString(), mBinding.loginPassword.text.toString()).observe(this, Observer {
                    startActivity(Intent(this, DashBoardViewPagerActivity::class.java))
                    finish()
                })
            }
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
    fun hideOrShowProgress(showProgress: Boolean) {
        if (showProgress) {
            mBinding.loading.visibility = View.VISIBLE
            mBinding.loading.visibility = View.VISIBLE
        }else{
            mBinding.loading.visibility = View.GONE
            mBinding.loading.visibility = View.GONE
        }

    }
}

sealed class LoginViewState
object LoginSuccess : LoginViewState()
data class LoginError(val error: String) : LoginViewState()
