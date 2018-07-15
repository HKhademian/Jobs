package ir.hossainkhademian.jobs.screen.chat.list

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
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
import ir.hossainkhademian.jobs.data.model.User
import ir.hossainkhademian.jobs.data.model.avatarUrl
import ir.hossainkhademian.jobs.data.model.idStr
import ir.hossainkhademian.jobs.screen.chat.detail.ChatDetailActivity
import ir.hossainkhademian.jobs.screen.chat.detail.ChatDetailFragment
import ir.hossainkhademian.util.context
import ir.hossainkhademian.util.launchActivity
import kotlinx.android.synthetic.main.activity_chat_list.*
import kotlinx.android.synthetic.main.fragment_chat_list.*
import kotlinx.android.synthetic.main.item_chat_list.view.*
import org.jetbrains.anko.bundleOf
import org.jetbrains.anko.image

class ChatListActivity : AppCompatActivity() {
  private val picasso = Picasso.get()!!
  private var twoPane: Boolean = false
  private lateinit var viewModel: ChatListViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_chat_list)

    twoPane = chat_detail_container != null

    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    toolbar.title = title

    fab.setOnClickListener { view ->
      Snackbar.make(view, "Not Implemented yet!", Snackbar.LENGTH_LONG)
        .setAction("Action", null).show()
    }


    viewModel = ViewModelProviders.of(this).get(ChatListViewModel::class.java)

    setupRecyclerView(chat_list)
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
    // recyclerView.adapter = SimpleItemRecyclerViewAdapter(this, DummyContent.ITEMS, twoPane)
    recyclerView.layoutManager = LinearLayoutManager(this).apply { orientation = LinearLayoutManager.VERTICAL }
    recyclerView.itemAnimator = DefaultItemAnimator()
    recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

    val adapter = ItemAdapter(LayoutInflater.from(context),
      viewModel.userChats.value ?: emptyList())
    recyclerView.adapter = adapter
    viewModel.userChats.observe(this, Observer {
      adapter.data = it ?: emptyList()
      adapter.notifyDataSetChanged()
    })
  }

  private fun onItemSelected(item: Pair<User, Int>) {
    val user = item.first
    if (twoPane) {
      val fragment = ChatDetailFragment().apply {
        arguments = bundleOf(
          ChatDetailFragment.ARG_USER_TITLE to user.title,
          ChatDetailFragment.ARG_USER_ID to user.idStr
        )
      }
      supportFragmentManager
        .beginTransaction()
        .replace(R.id.chat_detail_container, fragment)
        .commit()
    } else {
      launchActivity<ChatDetailActivity>(extras = *arrayOf(
        ChatDetailFragment.ARG_USER_TITLE to user.title,
        ChatDetailFragment.ARG_USER_ID to user.idStr
      ))
    }
  }

  private inner class ItemAdapter(val inflater: LayoutInflater, var data: List<Pair<User, Int>> = emptyList()) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    private val onItemClickListener = View.OnClickListener { v ->
      val item = v?.tag as? Pair<User, Int> ?: return@OnClickListener
      onItemSelected(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
      ViewHolder(inflater.inflate(R.layout.item_chat_list, parent, false))

    override fun getItemCount() =
      data.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      holder.bindItem(data[position])
    }

    private inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
      val avatarView = view.avatarView!!
      val titleView = view.titleView!!
      val subtitleView = view.subtitleView!!
      val badgeView = view.badge_view!!

      init {
        view.setOnClickListener(onItemClickListener)
      }

      fun bindItem(item: Pair<User, Int>) {
        val user = item.first
        val chatCount = item.second
        val imageUrl = user.avatarUrl

        view.tag = item
        picasso.load(imageUrl).placeholder(R.drawable.ic_avatar).into(avatarView)
        titleView.text = user.title
        subtitleView.text = "user id: ${user.id}"
        // avatarView.
        badgeView.image = when (chatCount) {
          0 -> null
          else -> BadgeDrawable.Builder()
            .type(BadgeDrawable.TYPE_NUMBER).number(chatCount)
            .badgeColor(badgeView.context.resources.getColor(R.color.colorAccent)).build()
        }
      }
    }
  }
}
