package ir.hossainkhademian.jobs.view

import android.content.Context
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.View
import com.squareup.picasso.Picasso
import ir.hossainkhademian.jobs.R
import ir.hossainkhademian.jobs.data.model.*
import kotlinx.android.synthetic.main.view_skill.view.*

class SkillView : CardView {
  private val view: View
  private val avatarView get() = view.avatarView
  private val titleView get() = view.titleView

  constructor(context: Context) : super(context) {
    view = inflate(context, R.layout.view_skill, this)
  }

  constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    view = inflate(context, R.layout.view_skill, this)

    val a = context.obtainStyledAttributes(attrs, R.styleable.SkillView, defStyleAttr, defStyleAttr)
    // jobId = a.getString(R.styleable.SkillView_skillId) ?: emptyID
    // small = a.getBoolean(R.styleable.SkillView_small, false)
    a.recycle()
  }

  var small = false
    set(small) {
      field = small
      val size = ((if (small) 48 else 64) * context.resources.displayMetrics.density).toInt()
      avatarView.layoutParams = avatarView.layoutParams.apply {
        width = size
        height = size
      }
    }

  var skill: Skill = EmptySkill
    set(item) {
      field = item

      titleView.text = item.title
      Picasso.get().load(item.avatarUrl).placeholder(R.drawable.ic_avatar).into(avatarView)
    }

  var skillId: ID
    get() = skill.id
    set(skillId) {
      // query
    }
}
