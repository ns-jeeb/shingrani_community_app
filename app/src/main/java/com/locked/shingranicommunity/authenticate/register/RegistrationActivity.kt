package com.locked.shingranicommunity.authenticate.register

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer

import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.authenticate.data.AuthenticationRepository
import com.locked.shingranicommunity.authenticate.ui.login.*
import com.locked.shingranicommunity.dashboard.DashBoardViewPagerActivity
import com.locked.shingranicommunity.databinding.RegisterationActivityBinding

class RegistrationActivity : AppCompatActivity(),View.OnClickListener , AuthenticationRepository.OnRegisterSuccess {
    override fun onSuccess() {
       getSharedPreferences("token", Context.MODE_PRIVATE).edit().clear().apply()

        var intent: Intent = Intent(this, LoginActivity::class.java)
//        intent.putExtra()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        this.finish()
    }

    override fun onFailed() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onClick(v: View?) {
        registerViewModel.register(mBinding.registerName.text.toString(),mBinding.registerUsername.text.toString(), mBinding.registerPassword.text.toString())
        registerViewModel.onRegisterSuccess(this)
    }

    companion object {
        fun newInstance() = RegistrationActivity()
    }

    private lateinit var registerViewModel: RegisterationViewModel

    private lateinit var  mBinding: RegisterationActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this,R.layout.registeration_activity)
        mBinding.btnJoiningPermission.setOnClickListener(this)

        registerViewModel = ViewModelProviders.of(this, RegisterViewModelFactory()).get(RegisterationViewModel::class.java)

        registerViewModel.authenticFormState.observe(this@RegistrationActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            mBinding.btnJoiningPermission.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                mBinding.btnRegister.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                mBinding.registerPassword.error = getString(loginState.passwordError)
            }
        })
        mBinding.btnRegister.setOnClickListener(this)


        registerViewModel.registerResult.observe(this@RegistrationActivity, Observer {
            val loginResult = it ?: return@Observer


            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
//            if (loginResult.success != null) {
//                updateUiWithUser(loginResult.success)
//            }
            setResult(Activity.RESULT_OK)

        })


    }
    private fun updateUiWithUser() {
        mBinding.loading.visibility = View.GONE
        var intent: Intent = Intent(this, DashBoardViewPagerActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        this.finish()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
    override fun onStart() {
        super.onStart()
        registerViewModel = ViewModelProviders.of(this).get(RegisterationViewModel::class.java)
    }

}
