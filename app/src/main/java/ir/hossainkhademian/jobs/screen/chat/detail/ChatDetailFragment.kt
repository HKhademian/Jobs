package ir.hossainkhademian.jobs.screen.chat.detail

import android.arch.lifecycle.MutableLiveData
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
import ir.hossainkhademian.jobs.R
import ir.hossainkhademian.jobs.data.model.isSender
import ir.hossainkhademian.jobs.screen.BaseFragment
import ir.hossainkhademian.util.LiveDatas.observe
import ir.hossainkhademian.util.TextWatchers.TextWatcher
import ir.hossainkhademian.util.ViewModels.getViewModel
import kotlinx.android.synthetic.main.activity_chat_detail.*
import kotlinx.android.synthetic.main.fragment_chat_detail.view.*
import kotlinx.android.synthetic.main.item_chat_detail.view.*
import android.content.ClipData
import android.content.Context
import ir.hossainkhademian.jobs.data.model.EmptyChat
import ir.hossainkhademian.jobs.data.model.LocalChat
import ir.hossainkhademian.util.Texts.getRelativeTime
import ir.hossainkhademian.util.Texts.isEmoji
import ir.hossainkhademian.util.Texts.hideKeyboard


class ChatDetailFragment : BaseFragment() {
  companion object {
    const val ARG_USER_ID = "user.id"
    const val ARG_USER_TITLE = "user.title"
    const val ARG_SINGLE_PANEL = "singlePanel"
  }

  private lateinit var viewModel: ChatDetailViewModel
  private var rootView: View? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    activity?.toolbar?.title = arguments?.getString(ARG_USER_TITLE) ?: ""
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val userId = arguments?.getString(ARG_USER_ID) ?: ""
    viewModel = getViewModel { ChatDetailViewModel(app, userId) }

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
        (viewModel.messageField as MutableLiveData).postValue(message)
    })

    sendButton.setOnClickListener {
      activity?.hideKeyboard()
      viewModel.send()
    }

    viewModel.error.observe(this) { ex ->
      Snackbar.make(rootView, "Error :\n${ex.message ?: ex.toString()}\n\nif it happens many times, contact support", Snackbar.LENGTH_LONG).show()
    }

    viewModel.chats.observe(this, emptyList()) { chats ->
      adapter.items = chats
      adapter.notifyDataSetChanged()
      recyclerView.scrollToPosition(adapter.items.size - 1)
    }

    viewModel.sendEnabled.observe(this, true) { isEnabled ->
      sendButton.isEnabled = isEnabled == true
      emojiButton.isEnabled = isEnabled != true
      mediaButton.isEnabled = isEnabled != true
    }

    viewModel.messageField.observe(this, "") { message ->
      if (messageField.text.toString() != message) {
        messageField.setText(message)
        // messageField.requestFocus()
        activity?.hideKeyboard()
      }
    }

    viewModel.isSending.observe(this, false) { isSending ->
      rootView.chat_form.visibility = if (isSending) View.INVISIBLE else View.VISIBLE
      rootView.wait_form.visibility = if (isSending) View.VISIBLE else View.INVISIBLE
    }

    return rootView
  }

  private inner class ItemAdapter(val inflater: LayoutInflater, items: List<LocalChat> = emptyList()) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
    var items = items
      set(items) {
        field = items
        notifyDataSetChanged()
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

      var item: LocalChat = EmptyChat
        set(value) {
          field = value
          showItem(item)
        }

      init {
        view.setOnClickListener {
          val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
          val clip = ClipData.newPlainText("message", item.message)
          clipboard.primaryClip = clip

          Toast.makeText(view.context, "Message Copied!", Toast.LENGTH_SHORT).show()
        }

        view.setOnLongClickListener {
          (viewModel.messageField as MutableLiveData).postValue(item.message)
          true
        }
      }

      private fun showItem(item: LocalChat) {
        val message = item.message
        val isEmoji = message.isEmoji
        val color = when {
          item.isSender -> R.color.color_message_card_send
          else -> R.color.color_message_card_receive
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
          view.layoutDirection = if (item.isSender) View.LAYOUT_DIRECTION_LTR else View.LAYOUT_DIRECTION_RTL

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
