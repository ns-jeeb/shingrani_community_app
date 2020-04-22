package com.locked.shingranicommunity.registration_login.registration

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.databinding.SettingsActivityBinding
import com.locked.shingranicommunity.registration_login.registration.login.LoginActivity

private const val TITLE_TAG = "settingsActivityTitle"

class SettingsActivity : AppCompatActivity(),PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

    lateinit var mBinding: SettingsActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this ,R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingFragment())
                .commit()
        }
        else {
            title = savedInstanceState.getCharSequence(TITLE_TAG)
        }
        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount == 0) {
                setTitle(R.string.title_activity_settings)
            }
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mBinding.backPress.setOnClickListener {
            onBackPressed()
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save current activity title so we can set it again after a configuration change
        outState.putCharSequence(TITLE_TAG, title)
    }

    override fun onSupportNavigateUp(): Boolean {
        if (supportFragmentManager.popBackStackImmediate()) {
            return true
        }
        return super.onSupportNavigateUp()
    }

    override fun onPreferenceStartFragment(
        caller: PreferenceFragmentCompat,
        pref: Preference
    ): Boolean {
        // Instantiate the new Fragment
        val args = pref.extras
        val fragment = supportFragmentManager.fragmentFactory.instantiate(
            classLoader,
            pref.fragment
        ).apply {
            arguments = args
            setTargetFragment(caller, 0)
        }
        // Replace the existing Fragment with the new Fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.settings, fragment)
            .addToBackStack(null)
            .commit()
        title = pref.title
        return true
    }

    class SettingFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.setting_preferences, rootKey)
        }

        override fun onPreferenceTreeClick(preference: Preference?): Boolean {
            return when (preference?.key) {
                getString(R.string.key_logout_)-> {
                    activity?.getSharedPreferences("token", Context.MODE_PRIVATE)?.edit()?.putString("token", "")?.apply()
                    var intent = Intent(activity, LoginActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                    true
                }
                getString(R.string.key_hide_phone)->{
                    Log.d("Preference_Setting","hide phone here")
                    return true
                }
                getString(R.string.key_hide_email)->{
                    Log.d("Preference_Setting","hide email  here")
                    return true
                }
                else -> {
                    super.onPreferenceTreeClick(preference)
                }
            }
        }
    }
}
