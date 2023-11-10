package com.locked.shingranicommunity.settings

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.locked.shingranicommunity.UrCommunityApplication
import com.locked.shingranicommunity.R
import com.locked.shingranicommunity.announcement.AnnouncementComponentProvider
import com.locked.shingranicommunity.announcement.AnnouncementCreateViewModel
import com.locked.shingranicommunity.auth.AuthActivity
import com.locked.shingranicommunity.auth.LoginFragment
import com.locked.shingranicommunity.common.FragmentActivity
import com.locked.shingranicommunity.common.NavigationHandler
import com.locked.shingranicommunity.databinding.ActivitySettingsBinding
import com.locked.shingranicommunity.di.settings.SettingsComponent
import com.locked.shingranicommunity.session.SessionManager
import javax.inject.Inject

class SettingsActivity : FragmentActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var settingsComponent: SettingsComponent
    private lateinit var viewModel: SettingsViewModel
    private lateinit var settingsBinding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        settingsComponent = UrCommunityApplication.instance.appComponent.settingsComponentFactory.create(this)
        settingsComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(SettingsViewModel::class.java)
        intent?.putExtra(NavigationHandler.EXTRA_FRAGMENT_CLASS, SettingFragment::class.java)
        super.onCreate(savedInstanceState)
        setupViews()
    }

    override fun loadLayoutBinding() {
        settingsBinding = DataBindingUtil.setContentView(this, R.layout.activity_settings)
        toolbar = settingsBinding.toolbar
    }

    private fun setupViews() {
        viewModel.title.observe(this, Observer {
            title = it
        })
        viewModel.fullName.observe(this, Observer {
            settingsBinding.name.text = it
        })
        viewModel.email.observe(this, Observer {
            settingsBinding.email.text = it
        })
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    class SettingFragment : PreferenceFragmentCompat() {

        @Inject
        lateinit var sessionManager: SessionManager
        @Inject
        lateinit var viewModelFactory: ViewModelProvider.Factory
        private lateinit var viewModel: SettingsViewModel

        override fun onAttach(context: Context) {
            super.onAttach(context)
            (activity as SettingsActivity).settingsComponent.inject(this)
            viewModel = ViewModelProvider(this, viewModelFactory).get(SettingsViewModel::class.java)
        }

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.setting_preferences, rootKey)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            viewModel.appVersion.observe(viewLifecycleOwner, Observer {
                preferenceScreen.findPreference<Preference>(getString(R.string.key_version))?.summary = it
            })
        }

        override fun onPreferenceTreeClick(preference: Preference): Boolean {
            return when (preference.key) {
                getString(R.string.key_logout)-> {
                    viewModel.logout()
                    true
                }
                getString(R.string.key_hide_phone)->{
                    return true
                }
                else -> {
                    super.onPreferenceTreeClick(preference)
                }
            }
        }
    }
}
