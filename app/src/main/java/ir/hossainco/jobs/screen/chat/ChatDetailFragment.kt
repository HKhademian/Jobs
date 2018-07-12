package ir.hossainco.jobs.screen.chat

import android.arch.lifecycle.Observer
import android.os.Build
import android.os.Bundle
import android.support.v7.widget.CardView
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import ir.hossainco.jobs.R
import ir.hossainco.jobs.data.DataManager
import ir.hossainco.jobs.data.database.ChatEntity
import ir.hossainco.jobs.data.model.Chat
import ir.hossainco.jobs.screen.BaseFragment
import kotlinx.android.synthetic.main.activity_chat_detail.*
import kotlinx.android.synthetic.main.fragment_chat_detail.view.*
import kotlinx.android.synthetic.main.item_chat.view.*

class ChatDetailFragment : BaseFragment() {
	companion object {
		const val ARG_USER_ID = "userId"
		const val ARG_USER_TITLE = "userTitle"
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		activity?.toolbar?.title = arguments?.getString(ARG_USER_TITLE) ?: ""
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val userId = arguments?.getLong(ARG_USER_ID) ?: 0

		val view = inflater.inflate(R.layout.fragment_chat_detail, container, false)
		val recyclerView = view.recyclerView
		val messageField = view.message_field
		val sendButton = view.send_button

		recyclerView.layoutManager = LinearLayoutManager(activity).apply {
			orientation = LinearLayoutManager.VERTICAL
			reverseLayout = false
			stackFromEnd = false
		}
		recyclerView.itemAnimator = DefaultItemAnimator()
		// recyclerView.addItemDecoration(DividerItemDecoration(activity!!, DividerItemDecoration.VERTICAL))

		val data = DataManager.getChatsByContact(userId)
		val adapter = ItemAdapter(inflater, data.value)
		recyclerView.adapter = adapter
		data.observe(this, Observer {
			adapter.setData(it)
			adapter.notifyDataSetChanged()
			recyclerView.scrollToPosition(adapter.items.size - 1)
		})

		sendButton.isEnabled = messageField.text.isNotEmpty()
		messageField.addTextChangedListener(object : TextWatcher {
			override fun afterTextChanged(s: Editable?) {
				sendButton.isEnabled = messageField.text.isNotBlank()
			}

			override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
			override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
		})
		sendButton.setOnClickListener {
			val message = messageField.text.toString()
			if (message.isEmpty())
				return@setOnClickListener
			adapter.items.add(ChatEntity(0, userId, false, message, System.currentTimeMillis()))
			adapter.notifyDataSetChanged()
			recyclerView.scrollToPosition(adapter.items.size - 1)
			messageField.text.clear()
		}

		return view
	}

	private inner class ItemAdapter(val inflater: LayoutInflater, data: List<Chat>?) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
		val items = ArrayList<Chat>()

		init {
			setData(data)
		}

		fun setData(data: List<Chat>?) {
			items.clear()
			if (data != null)
				items.addAll(data)
		}

		override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
			ViewHolder(inflater.inflate(R.layout.item_chat, parent, false))

		override fun getItemCount() =
			items.size

		override fun onBindViewHolder(holder: ViewHolder, position: Int) {
			holder.bindItem(items[position])
		}


		private inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
			private val chatCard = view.chat_card!!
			private val messageView = view.message!!
			var item: Chat? = null; private set

			init {
				view.setOnClickListener(this)
			}

			fun bindItem(item: Chat) {
				this.item = item
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
					view.layoutDirection = if (item.direction) View.LAYOUT_DIRECTION_LTR else View.LAYOUT_DIRECTION_RTL
				}
				chatCard.setCardBackgroundColor(view.context.resources.getColor(
					if (item.direction) R.color.color_message_card_send
					else R.color.color_message_card_receive))
				messageView.text = item.text
			}

			override fun onClick(v: View?) {
				Toast.makeText(view.context, "NOT IMPLEMENTED YET!", Toast.LENGTH_SHORT).show()
			}
		}
	}
}
