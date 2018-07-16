package ir.hossainkhademian.jobs.view

import android.content.Context
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import ir.hossainkhademian.jobs.R
import kotlinx.android.synthetic.main.view_field_box.view.*

class FieldBox : CardView {
  private var view: View? = null
  private val titleView get() = view!!.titleView
  private val actionView get() = view!!.actionView
  private val childView get() = view!!.childView

  constructor(context: Context) : super(context) {
    view = inflate(context, R.layout.view_field_box, this)
  }

  constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    view = inflate(context, R.layout.view_field_box, this)

    val a = context.obtainStyledAttributes(attrs, R.styleable.FieldBox, defStyleAttr, defStyleAttr)
    title = a.getString(R.styleable.FieldBox_android_text) ?: ""
    action = a.getString(R.styleable.FieldBox_action) ?: ""
    a.recycle()
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
    get() = actionView.text.toString()
    set(value) {
      actionView.text = value
    }

  var onActionClickListener: View.OnClickListener = View.OnClickListener { }
    set(listener) {
      field = listener
      actionView.setOnClickListener(listener)
    }
}
