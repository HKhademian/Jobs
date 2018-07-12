package ir.hossainco.jobs.screen.chat

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
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
import ir.hossainco.jobs.R
import ir.hossainco.jobs.data.DataManager
import ir.hossainco.jobs.data.model.UserWithChats
import ir.hossainco.jobs.data.model.avatarUrl
import ir.hossainco.jobs.util.context
import ir.hossainco.jobs.util.launchActivity
import kotlinx.android.synthetic.main.activity_chat_list.*
import kotlinx.android.synthetic.main.fragment_chat_list.*
import kotlinx.android.synthetic.main.item_chat_list.view.*
import org.jetbrains.anko.bundleOf
import org.jetbrains.anko.image

/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [ChatDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class ChatListActivity : AppCompatActivity() {

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private var twoPane: Boolean = false

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_chat_list)

		setSupportActionBar(toolbar)
		supportActionBar?.setDisplayHomeAsUpEnabled(true)
		toolbar.title = title

		fab.setOnClickListener { view ->
			Snackbar.make(view, "Not Implemented yet!", Snackbar.LENGTH_LONG)
				.setAction("Action", null).show()
		}

		if (chat_detail_container != null) {
			twoPane = true
		}

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

		val data = DataManager.getUserChats()
		val adapter = ItemAdapter(LayoutInflater.from(context), data.value ?: emptyList())
		recyclerView.adapter = adapter
		data.observe(this, Observer {
			adapter.data = it ?: emptyList()
			adapter.notifyDataSetChanged()
		})
	}

	private fun onItemSelected(item: UserWithChats) {
		if (twoPane) {
			val fragment = ChatDetailFragment().apply {
				arguments = bundleOf(ChatDetailFragment.ARG_USER_ID to item.user.id,
					ChatDetailFragment.ARG_USER_TITLE to item.user.title)
			}
			supportFragmentManager
				.beginTransaction()
				.replace(R.id.chat_detail_container, fragment)
				.commit()
		} else {
			launchActivity<ChatDetailActivity>(extras = *arrayOf(ChatDetailFragment.ARG_USER_ID to item.user.id,
				ChatDetailFragment.ARG_USER_TITLE to item.user.title))
		}
	}

	private inner class ItemAdapter(val inflater: LayoutInflater, var data: List<UserWithChats>) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
		private val picasso = Picasso.get()!!

		private val onItemClickListener = View.OnClickListener { v ->
			val item = v?.tag as? UserWithChats ?: return@OnClickListener
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
			val avatarView = view.avatar_view!!
			val titleView = view.title_view!!
			val subtitleView = view.subtitle_view!!
			val badgeView = view.badge_view!!

			init {
				view.setOnClickListener(onItemClickListener)
			}

			fun bindItem(item: UserWithChats) {
				view.tag = item
				picasso.load(item.user.avatarUrl).placeholder(R.drawable.ic_avatar).into(avatarView)
				titleView.text = item.user.title
				subtitleView.text = "user id: ${item.user.id}"
				// avatarView.
				badgeView.image = when (item.chats.size) {
					0 -> null
					else -> BadgeDrawable.Builder()
						.type(BadgeDrawable.TYPE_NUMBER).number(item.chats.size)
						.badgeColor(badgeView.context.resources.getColor(R.color.colorAccent)).build()
				}
			}
		}
	}
}
