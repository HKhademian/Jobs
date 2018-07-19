package ir.chista.jobs.screen.dashboard.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import ir.chista.jobs.R
import ir.chista.jobs.util.BaseFragment
import kotlinx.android.synthetic.main.fragment_about.view.*

class AboutFragment : BaseFragment() {
  private companion object {
    const val SOCIAL_INSTAGRAM_URL = "https://instagram.org/HossainKhademian"
    const val SOCIAL_TWITTER_URL = "https://twitter.com/HKhademian"
    const val SOCIAL_TELEGRAM_URL = "https://t.me/HossainKhademian"
    const val SOCIAL_STACKOVERFLOW_URL = "https://stackoverflow.com/users/1803735/hossain-khademian"
    const val SOCIAL_GITHUB_URL = "https://github.com/HKhademian"
    const val SOCIAL_GITHUB2_URL = "https://github.com/SKhademian"
    const val SOCIAL_KARBOOM_URL = "https://karboom.io/cv/wnjzeo"
    const val SOCIAL_JOBINJA = "https://jobinja.ir/user/HossainKhademian"
    const val SOCIAL_LINKEDIN = "https://www.linkedin.com/in/hossein-khademian-7b39b0bb"
    const val PROFILE_URL = "$SOCIAL_GITHUB_URL.png?size=460"
    const val PROFILE2_URL = "$SOCIAL_GITHUB2_URL.png?size=460"
    // const val PROFILE_URL = "https://avatars1.githubusercontent.com/u/2716791?s=460&v=4"
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val rootView = inflater.inflate(R.layout.fragment_about, container, false)
    Picasso.get().load(PROFILE_URL).placeholder(R.drawable.ic_avatar).into(rootView.developer1_avatar_view)
    Picasso.get().load(PROFILE2_URL).placeholder(R.drawable.ic_avatar).into(rootView.developer2_avatar_view)
    rootView.developer_desc.text = Html.fromHtml(activity!!.assets.open("resume.txt").bufferedReader().readText())

    rootView.developer1_avatar_view.setOnClickListener {
      startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(SOCIAL_GITHUB_URL)))
    }
    rootView.developer2_avatar_view.setOnClickListener {
      startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(SOCIAL_GITHUB2_URL)))
    }

    rootView.social_instagram_action.setOnClickListener {
      startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(SOCIAL_INSTAGRAM_URL)))
    }
    rootView.social_twitter_action.setOnClickListener {
      startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(SOCIAL_TWITTER_URL)))
    }
    rootView.social_telegram_action.setOnClickListener {
      startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(SOCIAL_TELEGRAM_URL)))
    }

    rootView.social_github_action.setOnClickListener {
      startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(SOCIAL_GITHUB_URL)))
    }
    rootView.social_stackoverflow_action.setOnClickListener {
      startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(SOCIAL_STACKOVERFLOW_URL)))
    }
    rootView.social_karboom_action.setOnClickListener {
      startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(SOCIAL_KARBOOM_URL)))
    }
    rootView.social_jobinja_action.setOnClickListener {
      startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(SOCIAL_JOBINJA)))
    }
    rootView.social_linkedin_action.setOnClickListener {
      startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(SOCIAL_LINKEDIN)))
    }

    return rootView
  }
}
