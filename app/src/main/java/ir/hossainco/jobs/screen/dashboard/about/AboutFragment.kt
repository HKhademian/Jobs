package ir.hossainco.jobs.screen.dashboard.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import ir.hossainco.jobs.R
import ir.hossainco.jobs.screen.BaseFragment
import kotlinx.android.synthetic.main.fragment_about.view.*

class AboutFragment : BaseFragment() {
	companion object {
		const val PROFILE_URL = "https://scontent-vie1-1.cdninstagram.com/vp/b6b404cf41534e0b3b7709fbf7a69583/5B7A5EDB/t51.2885-19/s150x150/29095059_163688904339431_3339787455795560448_n.jpg"
		const val SOCIAL_INSTAGRAM_URL = "https://instagram.org/HossainKhademian"
		const val SOCIAL_TWITTER_URL = "https://twitter.com/HKhademian"
		const val SOCIAL_TELEGRAM_URL = "https://t.me/HossainKhademian"
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val rootView = inflater.inflate(R.layout.fragment_about, container, false)
		Picasso.get().load(PROFILE_URL).placeholder(R.drawable.ic_avatar).into(rootView.developer_avatar_view)
		rootView.developer_desc.text = Html.fromHtml(activity!!.assets.open("resume.txt").bufferedReader().readText())

		rootView.social_instagram_action.setOnClickListener {
			startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(SOCIAL_INSTAGRAM_URL)))
		}
		rootView.social_twitter_action.setOnClickListener {
			startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(SOCIAL_TWITTER_URL)))
		}
		rootView.social_telegram_action.setOnClickListener {
			startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(SOCIAL_TELEGRAM_URL)))
		}

		return rootView
	}
}
