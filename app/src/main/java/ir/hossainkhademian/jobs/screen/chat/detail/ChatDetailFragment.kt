package ir.hossainkhademian.jobs.screen.chat.detail

import android.app.Activity
import android.arch.lifecycle.MutableLiveData
import android.content.ClipboardManager
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import ir.hossainkhademian.jobs.R
import ir.hossainkhademian.jobs.screen.BaseFragment
import ir.hossainkhademian.util.LiveDatas.observe
import ir.hossainkhademian.util.TextWatchers.TextWatcher
import ir.hossainkhademian.util.ViewModels.getViewModel
import android.content.ClipData
import android.content.Context
import android.view.animation.AnimationUtils
import ir.hossainkhademian.jobs.data.model.*
import ir.hossainkhademian.util.Texts
import ir.hossainkhademian.util.Texts.getRelativeTime
import ir.hossainkhademian.util.Texts.isEmoji
import ir.hossainkhademian.util.Texts.hideKeyboard
import kotlinx.android.synthetic.main.fragment_chat_detail.view.*
import kotlinx.android.synthetic.main.item_chat_detail.view.*


class ChatDetailFragment : BaseFragment() {
  companion object {
    const val ARG_CONTACT_ID = ARG_ID
  }

  private lateinit var viewModel: ChatDetailViewModel
  private var rootView: View? = null
  private val adapter = ItemAdapter()

  override fun onAttach(context: Context?) {
    super.onAttach(context)
    viewModel = getViewModel { ChatDetailViewModel() }
    viewModel.listener = context as? ChatDetailListener
    viewModel.init(arguments?.getString(ARG_ID) ?: emptyID)
  }

  fun setContactId(contactId: ID) {
    viewModel.contactId = contactId
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val rootView = inflater.inflate(R.layout.fragment_chat_detail, container, false)
    this.rootView = rootView
    val recyclerView = rootView.recyclerView
    val messageField = rootView.message_field
    val sendAction = rootView.sendAction
    val emojiAction = rootView.emojiAction
    val mediaAction = rootView.mediaAction

    recyclerView.adapter = adapter

    messageField.addTextChangedListener(TextWatcher {
      val message = messageField.text.toString()
      if (viewModel.messageField.value != message)
        viewModel.onMessageChanged(message)
    })

    sendAction.setOnClickListener {
      activity?.hideKeyboard()
      viewModel.send()
    }

    emojiAction.setOnClickListener {
      viewModel.onMessageChanged((viewModel.messageField.value ?: "") + Texts.randomEmoji)
    }

    mediaAction.setOnClickListener {
      Snackbar.make(rootView, "Not Implemented yet!", Snackbar.LENGTH_SHORT).show()
    }

    viewModel.error.observe(this) {
      val ex = it.getContentIfNotHandled() ?: return@observe
      Snackbar.make(rootView, "Error :\n${ex.message ?: ex.toString()}\n\nif it happens many times, contact support", Snackbar.LENGTH_LONG).show()
    }
    viewModel.activity.observe(this) {
      val task = it.getContentIfNotHandled() ?: return@observe
      task.invoke(context as Activity)

    }

    viewModel.chats.observe(this) { chats ->
      adapter.items = chats
      adapter.notifyDataSetChanged()
      recyclerView.scrollToPosition(adapter.items.size - 1)
    }

    viewModel.sendEnabled.observe(this, true) { isEnabled ->
      sendAction.isEnabled = isEnabled == true
      // emojiAction.isEnabled = isEnabled != true
      mediaAction.isEnabled = isEnabled != true
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

  private inner class ItemAdapter(items: List<LocalChat> = emptyList()) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
    var items = items
      set(items) {
        field = items
        notifyDataSetChanged()
      }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
      ViewHolder(LayoutInflater.from(parent.context)
        .inflate(R.layout.item_chat_detail, parent, false))

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
      private val animation = AnimationUtils.loadAnimation(context, R.anim.emoji)

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

        emojiView.text = item.message
        emojiView.visibility = if (!isEmoji) View.GONE else View.VISIBLE
        if (isEmoji) emojiView.startAnimation(animation)
        else emojiView.clearAnimation()

        messageView.text = item.message

        timeView.text = item.time.getRelativeTime(context) // DateUtils.getRelativeTimeSpanString(context, DateTime(item.time)) // Date(item.time).toString()
      }
    }
  }
}
