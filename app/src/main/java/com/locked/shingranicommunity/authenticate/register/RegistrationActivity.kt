package com.locked.shingranicommunity.authenticate.register

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer

import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.authenticate.data.AuthenticationRepository
import com.locked.shingranicommunity.authenticate.ui.login.*
import com.locked.shingranicommunity.databinding.RegisterationActivityBinding

class RegistrationActivity : AppCompatActivity(),View.OnClickListener , AuthenticationRepository.OnAuthenticatedSuccess {
    override fun onSuccess() {
       getSharedPreferences("token", Context.MODE_PRIVATE).edit().clear().apply()

        var intent: Intent = Intent(this, LoginActivity::class.java)
//        intent.putExtra()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        this.finish()
    }

    override fun onFailed() {
    }

    override fun onClick(v: View?) {
        var intent: Intent
        if (v?.id == R.id.btn_joining_permission){
            intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

        }else{
            mBinding.loading.visibility = View.VISIBLE
            registerViewModel.register(mBinding.registerName.text.toString(),mBinding.registerUsername.text.toString(), mBinding.registerPassword.text.toString(),mBinding.registerPasswordConform.text.toString())
//            registerViewModel.onRegisterSuccess(this)
        }


    }

    companion object {
        fun newInstance() = RegistrationActivity()
    }

    private lateinit var registerViewModel: RegisterationViewModel

    private lateinit var  mBinding: RegisterationActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.registeration_activity)

        registerViewModel = ViewModelProviders.of(this, RegisterViewModelFactory(this)).get(RegisterationViewModel::class.java)

        mBinding.btnRegister.isEnabled = false
        mBinding.btnJoiningPermission.setOnClickListener(this)

        registerViewModel.authenticFormState.observe(this@RegistrationActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            mBinding.btnRegister.isEnabled = loginState.isDataValid

            if (loginState.nameError != null) {
                mBinding.registerName.error = getString(loginState.nameError)

            }
            if (loginState.usernameError != null) {
                mBinding.registerUsername.error = getString(loginState.usernameError)

            }
            if (loginState.passwordError != null) {
                mBinding.registerPassword.error = getString(loginState.passwordError)
            }
            if (loginState.passwordConformError != null) {
                mBinding.registerPasswordConform.error = getString(loginState.passwordConformError)

            }
            mBinding.btnRegister.isEnabled = loginState.isDataValid



        })
        registerViewModel.onRegisterSuccess(this)
        mBinding.btnRegister.setOnClickListener(this)


        registerViewModel.registerResult.observe(this@RegistrationActivity, Observer {
            val loginResult = it ?: return@Observer


            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                mBinding.btnRegister.isEnabled = true
                Log.d("filed_error","loginResult.success ${loginResult.success}")
            }
//            setResult(Activity.RESULT_OK)

        })

        mBinding.registerUsername.afterTextChanged {
            registerViewModel.registerDataChanged(
                mBinding.registerUsername.text.toString(),
                mBinding.registerPassword.text.toString(),
                mBinding.registerPasswordConform.text.toString()
            )
        }
        mBinding.registerPasswordConform.afterTextChanged {
            registerViewModel.registerDataChanged(
                mBinding.registerUsername.text.toString(),
                mBinding.registerPassword.text.toString(),
                mBinding.registerPasswordConform.text.toString()
            )
        }

        mBinding.registerName.afterTextChanged {
            registerViewModel.registerDataChanged(
                mBinding.registerUsername.text.toString(),
                mBinding.registerPassword.text.toString(),
                mBinding.registerPasswordConform.text.toString()
            )
        }

        mBinding.registerPassword.apply {
            afterTextChanged {
                registerViewModel.registerDataChanged(
                    mBinding.registerUsername.text.toString(),
                    mBinding.registerPassword.text.toString(),
                    mBinding.registerPasswordConform.text.toString()
                )
            }
            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {

                    EditorInfo.IME_ACTION_DONE ->
                        registerViewModel.register(
                            mBinding.registerName.text.toString(),
                            mBinding.registerUsername.text.toString(),
                            mBinding.registerPassword.text.toString(),
                            mBinding.registerPasswordConform.text.toString()
                        )

                }
                false
            }
        }

    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
        mBinding.btnRegister.isEnabled = false
    }

    override fun onStart() {
        super.onStart()
        registerViewModel = ViewModelProviders.of(this).get(RegisterationViewModel::class.java)
    }


}
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}