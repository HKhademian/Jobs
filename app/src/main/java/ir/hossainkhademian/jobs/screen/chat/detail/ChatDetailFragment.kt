package ir.hossainkhademian.jobs.screen.chat.detail

import android.arch.lifecycle.ViewModelProviders
import android.content.ClipboardManager
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import ir.hossainkhademian.jobs.App
import ir.hossainkhademian.jobs.R
import ir.hossainkhademian.jobs.data.model.Chat
import ir.hossainkhademian.jobs.data.model.isSended
import ir.hossainkhademian.jobs.screen.BaseFragment
import ir.hossainkhademian.util.LiveDatas.inlineObserve
import ir.hossainkhademian.util.TextWatchers.TextWatcher
import kotlinx.android.synthetic.main.activity_chat_detail.*
import kotlinx.android.synthetic.main.fragment_chat_detail.view.*
import kotlinx.android.synthetic.main.item_chat_detail.view.*
import android.content.ClipData
import android.content.Context
import ir.hossainkhademian.jobs.data.model.EmptyChat
import ir.hossainkhademian.util.Texts.getRelativeTime
import ir.hossainkhademian.util.Texts.isEmoji
import net.danlew.android.joda.DateUtils
import org.joda.time.DateTime
import java.util.*


class ChatDetailFragment : BaseFragment() {
  companion object {
    const val ARG_USER_ID = "user.id"
    const val ARG_USER_TITLE = "user.title"
  }

  private lateinit var viewModel: ChatDetailViewModel
  private var rootView: View? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    activity?.toolbar?.title = arguments?.getString(ARG_USER_TITLE) ?: ""
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val userId = arguments?.getString(ARG_USER_ID) ?: ""
    viewModel = ViewModelProviders.of(this,
      ChatDetailViewModel.Factory(activity!!.application as App, userId))
      .get(ChatDetailViewModel::class.java)

    val rootView = inflater.inflate(R.layout.fragment_chat_detail, container, false)
    this.rootView = rootView
    val recyclerView = rootView.recyclerView
    val messageField = rootView.message_field
    val sendButton = rootView.sendAction
    val emojiButton = rootView.emojiAction
    val mediaButton = rootView.mediaAction

    recyclerView.layoutManager = LinearLayoutManager(activity).apply {
      orientation = LinearLayoutManager.VERTICAL
      reverseLayout = false
      stackFromEnd = false
    }
    recyclerView.itemAnimator = DefaultItemAnimator()
    // recyclerView.addItemDecoration(DividerItemDecoration(activity!!, DividerItemDecoration.VERTICAL))

    val adapter = ItemAdapter(inflater, viewModel.chats.value ?: emptyList())
    recyclerView.adapter = adapter

    messageField.addTextChangedListener(TextWatcher {
      val message = messageField.text.toString()
      if (viewModel.messageField.value != message)
        viewModel.messageField.postValue(message)
    })

    sendButton.setOnClickListener {
      viewModel.send { ex ->
        Snackbar.make(rootView, "Cannot send Message! :\n${ex.message}", Snackbar.LENGTH_LONG).show()
      }
    }

    viewModel.chats.inlineObserve(this) { chats ->
      adapter.setData(chats ?: emptyList())
      adapter.notifyDataSetChanged()
      recyclerView.scrollToPosition(adapter.items.size - 1)
    }

    viewModel.sendEnabled.inlineObserve(this) { isEnabled ->
      sendButton.isEnabled = isEnabled == true
      emojiButton.isEnabled = isEnabled != true
      mediaButton.isEnabled = isEnabled != true
    }

    viewModel.messageField.inlineObserve(this) { message ->
      if (messageField.text.toString() != message) {
        messageField.setText(message ?: "")
        messageField.requestFocus()
      }
    }

    viewModel.isSending.inlineObserve(this) { isSending ->
      rootView.chat_form.visibility = if (isSending == true) View.INVISIBLE else View.VISIBLE
      rootView.wait_form.visibility = if (isSending == true) View.VISIBLE else View.INVISIBLE
    }

    return rootView
  }

  private inner class ItemAdapter(val inflater: LayoutInflater, data: List<Chat> = emptyList()) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
    val items = ArrayList<Chat>()

    init {
      setData(data)
    }

    fun setData(data: List<Chat>) {
      items.clear()
      items += data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
      ViewHolder(inflater.inflate(R.layout.item_chat_detail, parent, false))

    override fun getItemCount() =
      items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      holder.item = items[position]
    }


    private inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
      private val context get() = view.context
      private val chatCard = view.chat_card!!
      private val messageView = view.messageView!!
      private val emojiView = view.emojiView!!
      private val timeView = view.timeView!!

      var item: Chat = EmptyChat
        set(value) {
          field = value
          showItem(item)
          viewModel.markAsSeen(item)
        }

      init {
        view.setOnClickListener {
          val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
          val clip = ClipData.newPlainText("message", item.message)
          clipboard.primaryClip = clip

          Toast.makeText(view.context, "Message Copied!", Toast.LENGTH_SHORT).show()
        }

        view.setOnLongClickListener {
          viewModel.messageField.postValue(item.message)
          true
        }
      }

      private fun showItem(item: Chat) {
        val message = item.message
        val isEmoji = message.isEmoji
        val color = when {
          item.isSended -> R.color.color_message_card_send
          else -> R.color.color_message_card_receive
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
          view.layoutDirection = if (item.isSended) View.LAYOUT_DIRECTION_LTR else View.LAYOUT_DIRECTION_RTL

        chatCard.setCardBackgroundColor(context.resources.getColor(color))
        chatCard.visibility = if (isEmoji) View.GONE else View.VISIBLE

        emojiView.visibility = if (!isEmoji) View.GONE else View.VISIBLE
        emojiView.text = item.message

        messageView.text = item.message

        timeView.text = item.time.getRelativeTime(context) // DateUtils.getRelativeTimeSpanString(context, DateTime(item.time)) // Date(item.time).toString()
      }
    }
  }
}
