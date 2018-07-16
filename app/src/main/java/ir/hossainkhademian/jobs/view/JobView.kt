package ir.hossainkhademian.jobs.view

import android.content.Context
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.View
import com.squareup.picasso.Picasso
import ir.hossainkhademian.jobs.R
import ir.hossainkhademian.jobs.data.model.*
import kotlinx.android.synthetic.main.view_job.view.*

class JobView : CardView {
  private val view: View = inflate(context, R.layout.view_job, this)
  private val avatarView get() = view.avatarView
  private val titleView get() = view.titleView
  private val subtitleView get() = view.subtitleView

  constructor(context: Context) : super(context)
  constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

  var request: Job = EmptyJob
    set(item) {
      field = item

      titleView.text = item.title
      subtitleView.text = "ID: ${item.id}"
      Picasso.get().load(item.avatarUrl).placeholder(R.drawable.ic_avatar).into(avatarView)
    }
}
