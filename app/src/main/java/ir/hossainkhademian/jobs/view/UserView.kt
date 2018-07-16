package ir.hossainkhademian.jobs.view

import android.content.Context
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.View
import com.squareup.picasso.Picasso
import ir.hossainkhademian.jobs.R
import ir.hossainkhademian.jobs.data.model.EmptyUser
import ir.hossainkhademian.jobs.data.model.User
import ir.hossainkhademian.jobs.data.model.avatarUrl
import ir.hossainkhademian.util.Texts.getRelativeTime
import kotlinx.android.synthetic.main.view_user.view.*

class UserView : CardView {
  private val view: View = inflate(context, R.layout.view_user, this)
  private val avatarView get() = view.avatarView
  private val titleView get() = view.titleView
  private val subtitleView get() = view.subtitleView
  private val badgeView get() = view.badgeView
  private val lastSeenView get() = view.lastSeenView

  constructor(context: Context) : super(context)
  constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

  var user: User = EmptyUser
    set(item) {
      field = item

      titleView.text = item.title
      subtitleView.text = "ID: ${item.id}"

      badgeView.text = item.role.key
      lastSeenView.text = item.lastSeen.getRelativeTime(context)
      Picasso.get().load(item.avatarUrl).placeholder(R.drawable.ic_avatar).into(avatarView)
    }
}
