package ir.hossainkhademian.util

import android.content.Context
import android.net.ConnectivityManager


object Networks {
  val Context.isOnline
    get() =
      (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
        .activeNetworkInfo?.isConnected ?: false

}
