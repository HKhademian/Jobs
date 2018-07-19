package ir.chista.util

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
    viewModel(factory).apply(body)

  inline fun <reified T : ViewModel> Fragment.withViewModel(
    crossinline factory: () -> T,
    body: T.() -> Unit = {}
  ) =
    viewModel(factory).apply(body)


  inline fun <reified T : ViewModel> FragmentActivity.viewModel() =
    ViewModelProviders.of(this, viewModelFactory<T>())[T::class.java]

  inline fun <reified T : ViewModel> FragmentActivity.viewModel(crossinline factory: () -> T) =
    ViewModelProviders.of(this, viewModelFactory(factory))[T::class.java]

  inline fun <reified T : ViewModel> Fragment.viewModel(crossinline factory: () -> T) =
    ViewModelProviders.of(this, viewModelFactory(factory))[T::class.java]


  inline fun <reified T : ViewModel> viewModelFactory(crossinline factory: () -> T) =
    object : ViewModelProvider.Factory {
      override fun <U : ViewModel> create(modelClass: Class<U>): U = factory() as U
    }


  inline fun <reified T : ViewModel> viewModelFactory() =
    object : ViewModelProvider.Factory {
      override fun <U : ViewModel> create(modelClass: Class<U>): U = T ::class.java.newInstance() as U
    }
}
