package ir.chista.jobs.view

import android.content.Context
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import ir.chista.jobs.R
import kotlinx.android.synthetic.main.view_field_box.view.*

class FieldBox : CardView {
  private var view: View? = null
  private val titleView get() = view!!.titleView
  private val action1View get() = view!!.action1View
  private val action2View get() = view!!.action2View
  private val action3View get() = view!!.action3View
  private val childView get() = view!!.childView

  constructor(context: Context) : super(context) {
    init()
  }

  constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    init()

    val a = context.obtainStyledAttributes(attrs, R.styleable.FieldBox, defStyleAttr, defStyleAttr)
    title = a.getString(R.styleable.FieldBox_android_text) ?: ""
    action1 = a.getString(R.styleable.FieldBox_action1) ?: a.getString(R.styleable.FieldBox_action) ?: ""
    action2 = a.getString(R.styleable.FieldBox_action2) ?: ""
    action3 = a.getString(R.styleable.FieldBox_action3) ?: ""
    a.recycle()
  }

  private fun init() {
    view = inflate(context, R.layout.view_field_box, this)
    title = ""
    action = ""
    action2 = ""
    action3 = ""
    onAction1ClickListener = null
    onAction2ClickListener = null
    onAction3ClickListener = null

    radius = 8 * context.resources.displayMetrics.scaledDensity
  }

  override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
    if (view == null) super.addView(child, index, params)
    else childView.addView(child, index, params)
  }

  var title: String
    get() = titleView.text.toString()
    set(value) {
      titleView.text = value
    }

  var action: String
    get() = action1
    set(value) = let { action1 = value }


  var action1: String
    get() = action1View.text.toString()
    set(value) {
      action1View.text = value
      action1View.visibility = if (value.isEmpty()) View.GONE else View.VISIBLE // use space to not gone
    }

  var action2: String
    get() = action2View.text.toString()
    set(value) {
      action2View.text = value
      action2View.visibility = if (value.isEmpty()) View.GONE else View.VISIBLE // use space to not gone
    }

  var action3: String
    get() = action3View.text.toString()
    set(value) {
      action3View.text = value
      action3View.visibility = if (value.isEmpty()) View.GONE else View.VISIBLE // use space to not gone
    }

  var onActionClickListener: View.OnClickListener?
    get() = onAction1ClickListener
    set(listener) = let { onAction1ClickListener = listener }

  var onAction1ClickListener: View.OnClickListener? = null // View.OnClickListener { }
    set(listener) {
      field = listener
      action1View.setOnClickListener(listener)
      action1View.isEnabled = listener != null // use empty listener if you want
    }

  var onAction2ClickListener: View.OnClickListener? = null // View.OnClickListener { }
    set(listener) {
      field = listener
      action2View.setOnClickListener(listener)
      action2View.isEnabled = listener != null // use empty listener if you want
    }

  var onAction3ClickListener: View.OnClickListener? = null // View.OnClickListener { }
    set(listener) {
      field = listener
      action3View.setOnClickListener(listener)
      action3View.isEnabled = listener != null // use empty listener if you want
    }
}
