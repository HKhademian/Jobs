package ir.chista.jobs.screen.chat.list

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import ir.chista.jobs.R
import ir.chista.jobs.data.model.*
import ir.chista.jobs.screen.chat.detail.ChatDetailActivity
import ir.chista.jobs.screen.chat.detail.ChatDetailFragment
import ir.chista.util.Collections.consume
import ir.chista.util.ViewModels.getViewModel
import ir.chista.util.LiveDatas.observe
import ir.chista.util.context
import ir.chista.util.Texts.getRelativeTime
import ir.chista.util.bundle
import ir.chista.util.launchActivity
import kotlinx.android.synthetic.main.activity_chat_list_holder.*
import kotlinx.android.synthetic.main.activity_chat_list.*
import kotlinx.android.synthetic.main.fragment_chat_list.*
import kotlinx.android.synthetic.main.item_chat_list.view.*
import org.jetbrains.anko.bundleOf


class ChatListActivity : AppCompatActivity() {
  private var twoPane: Boolean = false
  private lateinit var viewModel: ChatListViewModel

  private val adapter = UserChatAdapter()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel = getViewModel { ChatListViewModel() }

    setContentView(R.layout.activity_chat_list_holder)

    twoPane = detailContainer != null
    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    toolbar.title = title

    recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    recyclerView.adapter = adapter

    fab.setOnClickListener { view ->
      Snackbar.make(view, "Not Implemented yet!", Snackbar.LENGTH_LONG)
        .setAction("Action", null).show()
    }

    if (savedInstanceState == null) {
      viewModel.init()
      val selectedId = intent.getStringExtra(ChatDetailFragment.ARG_CONTACT_ID) ?: emptyID
      if (twoPane) sendMessageTo(selectedId)
    }

    viewModel.userChats.observe(this) { userChats ->
      adapter.items = userChats
    }

    viewModel.selectedId.observe(this) { selectedId ->
      adapter.selectedId = selectedId
    }
  }

  private fun sendMessageTo(contactId: ID) {
    if (!twoPane) {
      launchActivity<ChatDetailActivity>(extras = *arrayOf(
        ChatDetailFragment.ARG_CONTACT_ID to contactId
      ))
    } else {
      val tag = ChatDetailFragment::class.java.simpleName
      val fragment =
        (supportFragmentManager.findFragmentByTag(tag) as? ChatDetailFragment)?.also { it.setContactId(contactId) }
          ?: ChatDetailFragment().also { it.arguments = bundle(ChatDetailFragment.ARG_CONTACT_ID to contactId) }

      supportFragmentManager
        .beginTransaction()
        .replace(R.id.detailContainer, fragment, tag)
        .commit()
    }
  }

  override fun onOptionsItemSelected(item: MenuItem) =
    when (item.itemId) {
      android.R.id.home -> consume {
        NavUtils.navigateUpFromSameTask(this)
      }
      else -> super.onOptionsItemSelected(item)
    }

  private fun onItemSelected(item: UserChat) {
    val user = item.user
    if (user.isEmpty) return
    viewModel.postSelectedId(item.user.id)
    sendMessageTo(user.idStr)
  }

  private inner class UserChatAdapter(items: List<UserChat> = emptyList(), selectedId: ID = emptyID) : RecyclerView.Adapter<UserChatViewHolder>() {
    var items = items
      set(items) {
        field = items
        notifyDataSetChanged()
      }

    var selectedId = selectedId
      set(selectedId) {
        field = selectedId
        notifyDataSetChanged()
      }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
      UserChatViewHolder(LayoutInflater.from(context).inflate(R.layout.item_chat_list, parent, false))

    override fun getItemCount() =
      items.size

    override fun onBindViewHolder(holder: UserChatViewHolder, position: Int) {
      val item = items[position]
      holder.item = item
      holder.selected = item.user.id == selectedId
    }
  }


  private inner class UserChatViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    private val avatarView = view.avatarView
    private val titleView = view.titleView
    private val subtitleView = view.subtitleView
    private val lastSeenView = view.lastSeenView
    private val badgeView = view.badgeView

    var item: UserChat = EmptyUserChat
      set(item) {
        field = item
        showItem(item, selected)
      }
    var selected: Boolean = false
      set(selected) {
        field = selected
        showItem(item, selected)
      }

    init {
      view.setOnClickListener {
        onItemSelected(item)
      }
    }

    private fun showItem(item: UserChat, selected: Boolean) {
      val user = item.user
      val badge = item.badge
      val imageUrl = user.avatarUrl

      view.setBackgroundColor(if (twoPane && selected) context.resources.getColor(R.color.colorSelector) else 0)

      Picasso.get().load(imageUrl).placeholder(R.drawable.ic_avatar).into(avatarView)

      titleView.text = user.title

      val you = "you: "
      subtitleView.text = when {
        item.lastChat.isEmpty -> "Click to start chat with him/her" // rare occurrence
        else -> "${if (item.lastChat.isSender) you else ""}${item.lastChat.message.trim()}"
      }

      badgeView.text = badge
      badgeView.visibility = if (badge.isNotEmpty) View.VISIBLE else View.GONE

      lastSeenView.text = user.lastSeen.getRelativeTime(context)
    }
  }

}
