package ir.hossainkhademian.jobs.screen.chat.detail

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import ir.hossainkhademian.jobs.R
import ir.hossainkhademian.jobs.data.model.*
import ir.hossainkhademian.jobs.screen.chat.list.ChatListActivity
import ir.hossainkhademian.util.ViewModels.getViewModel
import ir.hossainkhademian.util.LiveDatas.observe
import ir.hossainkhademian.util.PicassoCircleTransform
import ir.hossainkhademian.util.Texts.getRelativeTime
import ir.hossainkhademian.util.activity
import ir.hossainkhademian.util.bundle
import ir.hossainkhademian.util.context
import kotlinx.android.synthetic.main.activity_chat_detail.*
import java.lang.Exception

class ChatDetailActivity : AppCompatActivity(), ChatDetailListener {
  private lateinit var viewModel: ChatDetailViewModel

  private val avatarImageListener = object : Target {
    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
      supportActionBar?.setLogo(placeHolderDrawable)
    }

    override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
      supportActionBar?.setLogo(errorDrawable)
    }

    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
      supportActionBar?.setLogo(BitmapDrawable(resources, bitmap))
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel = getViewModel { ChatDetailViewModel() }
    setContentView(R.layout.activity_chat_detail)

    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)

    viewModel.contact.observe(this, EmptyUser) {
      userView.user = it
//      supportActionBar?.title = if (it.isEmpty) activity.title else it.title
//      supportActionBar?.subtitle = if (it.isEmpty) "" else it.lastSeen.getRelativeTime(context)
//      Picasso.get().load(it.avatarUrl)
//        .placeholder(R.drawable.ic_avatar)
//        .error(R.drawable.ic_avatar)
//        .transform(PicassoCircleTransform())
//        .into(avatarImageListener)
    }

    if (savedInstanceState == null) {
      val contactId = intent.getStringExtra(ChatDetailFragment.ARG_CONTACT_ID) ?: emptyID
      viewModel.init(contactId)

      val fragment = ChatDetailFragment().apply {
        arguments = bundle(ChatDetailFragment.ARG_CONTACT_ID to contactId)
      }

      supportFragmentManager.beginTransaction()
        .replace(R.id.detailContainer, fragment)
        .commit()
    }
  }

  override fun onOptionsItemSelected(item: MenuItem) =
    when (item.itemId) {
      android.R.id.home -> {
        navigateUpTo(Intent(this, ChatListActivity::class.java))
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
}
