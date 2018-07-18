package ir.chista.jobs.view

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.View
import com.squareup.picasso.Picasso
import ir.chista.jobs.R
import ir.chista.jobs.data.model.*
import kotlinx.android.synthetic.main.view_job.view.*

class JobView : CardView {
  private val view: View
  private val avatarView get() = view.avatarView
  private val titleView get() = view.titleView
  private val subtitleView get() = view.subtitleView

  constructor(context: Context) : super(context) {
    view = inflate(context, R.layout.view_job, this)
  }

  constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    view = inflate(context, R.layout.view_job, this)

    val a = context.obtainStyledAttributes(attrs, R.styleable.JobView, defStyleAttr, defStyleAttr)
    // jobId = a.getString(R.styleable.JobView_jobId) ?: emptyID
    small = a.getBoolean(R.styleable.JobView_small, false)
    a.recycle()
  }

  var small = false
    set(small) {
      field = small
      val size = ((if (small) 64 else 96) * context.resources.displayMetrics.density).toInt()
      avatarView.layoutParams = avatarView.layoutParams.apply {
        width = size
        height = size
      }
    }

  var job: Job = EmptyJob
    set(item) {
      field = item

      titleView.text = item.title
      subtitleView.text = "ID: ${item.id}"
      Picasso.get().load(item.avatarUrl).placeholder(R.drawable.ic_avatar).into(avatarView)
    }

  var jobId: ID
    get() = job.id
    set(jobId) {
      // query
    }
}
