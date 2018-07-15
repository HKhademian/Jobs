package ir.hossainkhademian.util

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.os.Parcelable
import android.util.Size
import android.util.SizeF
import java.io.Serializable

val <T : Context> T.context get() = this
val <T : Activity> T.activity get() = this

inline fun <reified T : Any> Activity.launchActivity(
  requestCode: Int = -1,
  options: Bundle? = null,
  vararg extras: Pair<String, Any>,
  noinline init: Intent.() -> Unit = {}) {

  val intent = newIntent<T>(this)
  intent.putExtras(*extras)
  intent.init()

//	overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
    startActivityForResult(intent, requestCode, options)
  else
    startActivityForResult(intent, requestCode)
}

inline fun <reified T : Any> Context.launchActivity(
  options: Bundle? = null,
  vararg extras: Pair<String, Any>,
  noinline init: Intent.() -> Unit = {}) {

  val intent = newIntent<T>(this)
  intent.putExtras(*extras)
  intent.init()

//	overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
    startActivity(intent, options)
  else
    startActivity(intent)
}

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
fun bundle(vararg pairs: Pair<String, Any>): Bundle {
  val bundle = Bundle()
  bundle.put(*pairs)
  return bundle
}

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
fun Bundle.put(vararg pairs: Pair<String, Any>) =
  pairs.forEach {
    when (it.second) {
      is String -> putString(it.first, it.second as String)
      is CharSequence -> putCharSequence(it.first, it.second as CharSequence)
      is Double -> putDouble(it.first, it.second as Double)
      is Float -> putFloat(it.first, it.second as Float)
      is Long -> putLong(it.first, it.second as Long)
      is Int -> putInt(it.first, it.second as Int)
      is Short -> putShort(it.first, it.second as Short)
      is Byte -> putByte(it.first, it.second as Byte)
      is Char -> putChar(it.first, it.second as Char)
      is DoubleArray -> putDoubleArray(it.first, it.second as DoubleArray)
      is FloatArray -> putFloatArray(it.first, it.second as FloatArray)
      is LongArray -> putLongArray(it.first, it.second as LongArray)
      is IntArray -> putIntArray(it.first, it.second as IntArray)
      is ShortArray -> putShortArray(it.first, it.second as ShortArray)
      is ByteArray -> putByteArray(it.first, it.second as ByteArray)
      is CharArray -> putCharArray(it.first, it.second as CharArray)
      is Bundle -> putBundle(it.first, it.second as Bundle)
      is Parcelable -> putParcelable(it.first, it.second as Parcelable)
      is Serializable -> putSerializable(it.first, it.second as java.io.Serializable)
      is IBinder -> putBinder(it.first, it.second as IBinder)
      is Size -> putSize(it.first, it.second as Size)
      is SizeF -> putSizeF(it.first, it.second as SizeF)
      else -> throw UnsupportedOperationException()
    }
  }

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
fun Intent.putExtras(vararg pairs: Pair<String, Any>) =
  pairs.forEach {
    when (it.second) {
      is String -> putExtra(it.first, it.second as String)
      is CharSequence -> putExtra(it.first, it.second as CharSequence)
      is Double -> putExtra(it.first, it.second as Double)
      is Float -> putExtra(it.first, it.second as Float)
      is Long -> putExtra(it.first, it.second as Long)
      is Int -> putExtra(it.first, it.second as Int)
      is Short -> putExtra(it.first, it.second as Short)
      is Byte -> putExtra(it.first, it.second as Byte)
      is Char -> putExtra(it.first, it.second as Char)
      is DoubleArray -> putExtra(it.first, it.second as DoubleArray)
      is FloatArray -> putExtra(it.first, it.second as FloatArray)
      is LongArray -> putExtra(it.first, it.second as LongArray)
      is IntArray -> putExtra(it.first, it.second as IntArray)
      is ShortArray -> putExtra(it.first, it.second as ShortArray)
      is ByteArray -> putExtra(it.first, it.second as ByteArray)
      is CharArray -> putExtra(it.first, it.second as CharArray)
      is Bundle -> putExtra(it.first, it.second as Bundle)
      is Parcelable -> putExtra(it.first, it.second as Parcelable)
      is Serializable -> putExtra(it.first, it.second as java.io.Serializable)
      else -> throw UnsupportedOperationException()
    }
  }

inline fun <reified T : Any> newIntent(context: Context): Intent =
  Intent(context, T::class.java)
