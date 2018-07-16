package ir.hossainkhademian.jobs.screen.chat.list

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import cn.nekocode.badge.BadgeDrawable
import com.squareup.picasso.Picasso
import ir.hossainkhademian.jobs.R
import ir.hossainkhademian.jobs.data.model.*
import ir.hossainkhademian.jobs.screen.chat.detail.ChatDetailActivity
import ir.hossainkhademian.jobs.screen.chat.detail.ChatDetailFragment
import ir.hossainkhademian.jobs.screen.request.detail.UserChat
import ir.hossainkhademian.jobs.screen.request.detail.unreadCount
import ir.hossainkhademian.jobs.screen.request.detail.user
import ir.hossainkhademian.util.ViewModels.getViewModel
import ir.hossainkhademian.util.LiveDatas.observe
import ir.hossainkhademian.util.context
import ir.hossainkhademian.util.launchActivity
import kotlinx.android.synthetic.main.activity_chat_list.*
import kotlinx.android.synthetic.main.fragment_chat_list.*
import kotlinx.android.synthetic.main.item_chat_list.view.*
import org.jetbrains.anko.bundleOf
import org.jetbrains.anko.image


class ChatListActivity : AppCompatActivity() {
  private var twoPane: Boolean = false
  private lateinit var viewModel: ChatListViewModel

  private val adapter = UserChatAdapter()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_chat_list)

    twoPane = chat_detail_container != null

    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    toolbar.title = title

//    fab.setOnClickListener { view ->
//      Snackbar.make(view, "Not Implemented yet!", Snackbar.LENGTH_LONG)
//        .setAction("Action", null).show()
//    }

    setupRecyclerView(chat_list)


    val userId = intent.getStringExtra(ChatDetailFragment.ARG_USER_ID) ?: emptyID
    val userTitle = intent.getStringExtra(ChatDetailFragment.ARG_USER_TITLE) ?: emptyID
    if (userId != emptyID) {
      sendMessageTo(userId, userTitle)
      if (!twoPane) {
        finish()
        return
      }
    }

    viewModel = getViewModel { ChatListViewModel(userId) }

    viewModel.userChats.observe(this) { userChats ->
      adapter.items = userChats ?: emptyList()
    }

    viewModel.selectedUserId.observe(this) { selectedUserId ->
      adapter.selectedUserId = selectedUserId
    }
  }

  override fun onOptionsItemSelected(item: MenuItem) =
    when (item.itemId) {
      android.R.id.home -> {
        NavUtils.navigateUpFromSameTask(this)
        true
      }
      else -> super.onOptionsItemSelected(item)
    }

  private fun setupRecyclerView(recyclerView: RecyclerView) {
    recyclerView.adapter = adapter
    recyclerView.itemAnimator = DefaultItemAnimator()
    recyclerView.layoutManager = LinearLayoutManager(this).apply { orientation = LinearLayoutManager.VERTICAL }
    recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
  }

  private fun onItemSelected(item: Pair<User, Int>) {
    val user = item.user
    sendMessageTo(user.idStr, user.title)
  }

  private fun sendMessageTo(userId: ID, userTitle: String) {
    if (twoPane) {
      val fragment = ChatDetailFragment().apply {
        arguments = bundleOf(
          ChatDetailFragment.ARG_USER_TITLE to userTitle,
          ChatDetailFragment.ARG_USER_ID to userId
        )
      }
      supportFragmentManager
        .beginTransaction()
        .replace(R.id.chat_detail_container, fragment)
        .commit()
    } else {
      launchActivity<ChatDetailActivity>(extras = *arrayOf(
        ChatDetailFragment.ARG_USER_TITLE to userTitle,
        ChatDetailFragment.ARG_USER_ID to userId
      ))
    }
  }


  private inner class UserChatAdapter(items: List<UserChat> = emptyList(), selectedUserId: ID = emptyID) : RecyclerView.Adapter<UserChatViewHolder>() {
    var items: List<Pair<User, Int>> = items
      set(items) {
        field = items
        notifyDataSetChanged()
      }

    var selectedUserId: ID = selectedUserId
      set(selectedUserId) {
        field = selectedUserId
        notifyDataSetChanged()
      }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
      UserChatViewHolder(LayoutInflater.from(context).inflate(R.layout.item_chat_list, parent, false))

    override fun getItemCount() =
      items.size

    override fun onBindViewHolder(holder: UserChatViewHolder, position: Int) {
      val item = items[position]
      holder.item = item
      holder.selected = item.user.id == selectedUserId
    }
  }


  private inner class UserChatViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    private val avatarView = view.avatarView
    private val titleView = view.titleView
    private val subtitleView = view.subtitleView
    private val badgeView = view.badge_view

    var item: UserChat = Pair(EmptyUser, 0)
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
      val unreadCount = item.unreadCount
      val imageUrl = user.avatarUrl

      view.setBackgroundColor(if (twoPane && selected) Color.parseColor("#3666") else 0)

      Picasso.get().load(imageUrl).placeholder(R.drawable.ic_avatar).into(avatarView)
      titleView.text = user.title
      subtitleView.text = "user id: ${user.id}"

      badgeView.image = when (unreadCount) {
        0 -> null
        else -> BadgeDrawable.Builder()
          .type(BadgeDrawable.TYPE_NUMBER).number(unreadCount)
          .badgeColor(badgeView.context.resources.getColor(R.color.colorAccent)).build()
      }
    }
  }

}
