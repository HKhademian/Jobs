package ir.hossainkhademian.util

import android.text.Editable

object TextWatchers {
  val EmptyTextWatcher = DefaultTextWatcher()

  class TextWatcher(private val afterTextChangedListener: (Editable?) -> Unit = {}) : DefaultTextWatcher() {
    override fun afterTextChanged(p0: Editable?) = afterTextChangedListener(p0)
  }

  open class DefaultTextWatcher : android.text.TextWatcher {
    override fun afterTextChanged(p0: Editable?) = Unit
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
  }
}
