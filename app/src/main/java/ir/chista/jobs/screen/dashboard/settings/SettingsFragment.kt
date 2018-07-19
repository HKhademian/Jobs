package ir.chista.jobs.screen.dashboard.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.llollox.androidtoggleswitch.widgets.ToggleSwitch
import ir.chista.jobs.R
import ir.chista.jobs.util.BaseFragment
import ir.chista.util.LocaleManager.savedLocale
import kotlinx.android.synthetic.main.fragment_settings.view.*

class SettingsFragment : BaseFragment() {
  companion object {
    val LANGS = arrayOf("en-US", "fa-IR")
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val rootView = inflater.inflate(R.layout.fragment_settings, container, false)

    val savedLocale = activity?.savedLocale ?: LANGS[0]
    rootView.languageToggleView.setCheckedPosition(LANGS.indexOf(savedLocale))
    rootView.languageToggleView.onChangeListener = object : ToggleSwitch.OnChangeListener {
      override fun onToggleSwitchChanged(position: Int) {
        activity?.savedLocale = LANGS[position]
      }
    }
    return rootView
  }
}
