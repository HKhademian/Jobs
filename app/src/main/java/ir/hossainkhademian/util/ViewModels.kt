package ir.hossainkhademian.util

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity

object ViewModels {
  inline fun <reified T : ViewModel> FragmentActivity.withViewModel(
    crossinline factory: () -> T,
    body: T.() -> Unit = {}
  ) =
    getViewModel(factory).apply(body)

  inline fun <reified T : ViewModel> Fragment.withViewModel(
    crossinline factory: () -> T,
    body: T.() -> Unit = {}
  ) =
    getViewModel(factory).apply(body)

  inline fun <reified T : ViewModel> FragmentActivity.getViewModel(crossinline factory: () -> T) =
    ViewModelProviders.of(this, getViewModelFactory(factory))[T::class.java]

  inline fun <reified T : ViewModel> Fragment.getViewModel(crossinline factory: () -> T) =
    ViewModelProviders.of(this, getViewModelFactory(factory))[T::class.java]

  inline fun <reified T : ViewModel> getViewModelFactory(crossinline factory: () -> T) =
    object : ViewModelProvider.Factory {
      override fun <U : ViewModel> create(modelClass: Class<U>): U = factory() as U
    }
}
