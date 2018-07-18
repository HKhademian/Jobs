package ir.hossainkhademian.util

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.provider.Telephony
import android.util.Log

object SmsUtil {
  fun sentSms(context: Context, address: String, body: String, read: Boolean = false) {
    try {
      val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) Telephony.Sms.Inbox.CONTENT_URI else return
      val values = ContentValues()
      values.put(Telephony.Sms.Inbox.ADDRESS, address)
      values.put(Telephony.Sms.Inbox.BODY, body)
      values.put(Telephony.Sms.Inbox.READ, read)
      context.contentResolver.insert(uri, values)
    } catch (e: Throwable) {
      Log.e("SMS-FAKE", e.message, e)
      e.printStackTrace()
    }
  }

  fun inboxSms(context: Context, address: String, body: String, read: Boolean = false) {
    try {
      val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) Telephony.Sms.Inbox.CONTENT_URI else return

      val values = ContentValues()
      values.put(Telephony.Sms.Inbox.ADDRESS, address)
      values.put(Telephony.Sms.Inbox.BODY, body)
      values.put(Telephony.Sms.Inbox.READ, read)
      context.contentResolver.insert(uri, values)

    } catch (e: Throwable) {
      Log.e("SMS-FAKE", e.message, e)
      e.printStackTrace()
    }
  }
}
