package ir.chista.jobs.screen.chat.detail

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
import ir.chista.jobs.R
import ir.chista.jobs.data.model.*
import ir.chista.jobs.screen.chat.list.ChatListActivity
import ir.chista.util.ViewModels.getViewModel
import ir.chista.util.LiveDatas.observe
import ir.chista.util.PicassoCircleTransform
import ir.chista.util.Texts.getRelativeTime
import ir.chista.util.activity
import ir.chista.util.bundle
import ir.chista.util.context
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
      handleIntent(intent)
    }
  }

  override fun onNewIntent(intent: Intent?) {
    super.onNewIntent(intent)
    intent?.let { handleIntent(it) }
  }

  override fun onOptionsItemSelected(item: MenuItem) =
    when (item.itemId) {
      android.R.id.home -> {
        navigateUpTo(Intent(this, ChatListActivity::class.java))
        true
      }
      else -> super.onOptionsItemSelected(item)
    }

  private fun handleIntent(intent: Intent) {
    val contactId = intent.getStringExtra(ChatDetailFragment.ARG_CONTACT_ID) ?: emptyID
    viewModel.init(contactId)

    val tag = ChatDetailFragment::class.java.simpleName
    val fragment =
      (supportFragmentManager.findFragmentByTag(tag) as? ChatDetailFragment)?.also { it.setContactId(contactId) }
        ?: ChatDetailFragment().also { it.arguments = bundle(ChatDetailFragment.ARG_CONTACT_ID to contactId) }

    supportFragmentManager.beginTransaction()
      .replace(R.id.detailContainer, fragment, tag)
      .commit()
  }
}
