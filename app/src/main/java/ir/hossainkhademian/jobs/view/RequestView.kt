package ir.hossainkhademian.jobs.view

import android.content.Context
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.View
import com.squareup.picasso.Picasso
import ir.hossainkhademian.jobs.R
import ir.hossainkhademian.jobs.data.model.EmptyRequest
import ir.hossainkhademian.jobs.data.model.Request
import ir.hossainkhademian.jobs.data.model.avatarUrl
import ir.hossainkhademian.jobs.data.model.title
import kotlinx.android.synthetic.main.view_request.view.*

class RequestView : CardView {
  val view: View = inflate(context, R.layout.view_request, this)
  val avatarView = rootView.avatarView
  val titleView = rootView.titleView
  val subtitleView = rootView.subtitleView

  constructor(context: Context) : super(context)
  constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

  var request: Request = EmptyRequest
    set(item) {
      field = item

      titleView.text = item.title
      subtitleView.text = "ID: ${item.id}"
      Picasso.get().load(item.avatarUrl).placeholder(R.drawable.ic_avatar).into(avatarView)
    }
}
