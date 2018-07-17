/*
 * https://gist.github.com/JoseAlcerreca/e0bba240d9b3cffa258777f12e5c0ae9
 * https://medium.com/google-developers/livedata-with-snackbar-navigation-and-other-events-the-singleliveevent-case-ac2622673150
 */
package ir.hossainkhademian.util

import android.arch.lifecycle.Observer

/**
 * An [Observer] for [Event]s, simplifying the pattern of checking if the [Event]'s content has
 * already been handled.
 *
 * [onEventUnhandledContent] is *only* called if the [Event]'s contents has not been handled.
 */
class EventObserver<T>(private val onEventUnhandledContent: (T) -> Unit) : Observer<Event<T>> {
  override fun onChanged(event: Event<T>?) {
    event?.getContentIfNotHandled()?.let { value ->
      onEventUnhandledContent(value)
    }
  }
}
