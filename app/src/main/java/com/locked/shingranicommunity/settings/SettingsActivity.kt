package com.locked.shingranicommunity.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.databinding.SettingsActivityBinding
import com.locked.shingranicommunity.registration_login.registration.login.LoginActivity
import javax.inject.Inject

class SettingsActivity : AppCompatActivity() {

    @Inject
    lateinit var settingsViewModel: SettingsViewModel

    lateinit var binding: SettingsActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.settings_activity)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment,
                SettingFragment()
            )
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initViews()
    }

    private fun initViews() {
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    class SettingFragment : PreferenceFragmentCompat() {

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.setting_preferences, rootKey)
        }

        override fun onPreferenceTreeClick(preference: Preference?): Boolean {
            return when (preference?.key) {
                getString(R.string.key_logout_)-> {
                    activity?.getSharedPreferences("token", Context.MODE_PRIVATE)?.edit()?.putString("token", "")?.apply()
                    val intent = Intent(activity, LoginActivity::class.java)
                    activity?.finish()
                    startActivity(intent)
                    true
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
