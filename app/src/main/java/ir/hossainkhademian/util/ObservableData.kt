package ir.hossainkhademian.util

import io.reactivex.ObservableSource
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

class ObservableData<T>(
  private val subject: Subject<T> = BehaviorSubject.create<T>(),
  init: T
) : ObservableSource<T> by subject {
  val observable = subject.hide()

  var data = init
    set(value) {
      field = value
      subject.onNext(value)
    }

  constructor(
    subject: PublishSubject<T> = PublishSubject.create<T>(),
    init: () -> T
  ) : this(subject, init.invoke())
}
