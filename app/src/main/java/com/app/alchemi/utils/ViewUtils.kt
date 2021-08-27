package com.app.alchemi.utils

import Constants
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.res.Resources
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.Settings.Secure
import android.text.Spannable
import android.text.SpannableString
import android.text.format.DateFormat
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.app.alchemi.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.android.material.snackbar.Snackbar
import org.json.JSONObject
import java.math.RoundingMode
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.math.ln
import kotlin.math.pow


class ViewUtils {

    companion object {
        private val WIDTH_INDEX = 0
        private val HEIGHT_INDEX = 1
        var alert: AlertDialog?=null

        /***
         *  Get Screen Size of Device
         */
        @Suppress("DEPRECATION")
        fun getScreenSize(context: Context): IntArray {
            val widthHeight = IntArray(2)
            widthHeight[WIDTH_INDEX] = 0
            widthHeight[HEIGHT_INDEX] = 0

            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = windowManager.defaultDisplay

            val size = Point()
            display.getSize(size)
            widthHeight[WIDTH_INDEX] = size.x
            widthHeight[HEIGHT_INDEX] = size.y
            if (!isScreenSizeRetrieved(widthHeight)) {
                val metrics = DisplayMetrics()
                display.getMetrics(metrics)
                widthHeight[0] = metrics.widthPixels
                widthHeight[1] = metrics.heightPixels
            }
            // Last defense. Use deprecated API that was introduced in lower than API 13
            if (!isScreenSizeRetrieved(widthHeight)) {
                widthHeight[0] = display.width // deprecated
                widthHeight[1] = display.height // deprecated
            }

            return widthHeight
        }

        /***
         *  get Hardware devices
         */
        @SuppressLint("HardwareIds")
        fun getHardwareDeviceId(context: Context):String{
            return Secure.getString(context.contentResolver, Secure.ANDROID_ID)
        }

        /***
         *  Show KeyBoard
         */

        fun showSoftKeyboard(activity: Activity, editText: EditText) {
            (activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                    .showSoftInput(editText, InputMethodManager.SHOW_FORCED)
        }

        /***
         *
         *  method  to round od value
         */
         fun roundOffValue(int: Int, value: Double) : Double{
           return value.toBigDecimal().setScale(int, RoundingMode.UP).toDouble()
         }

        /**
         *
         *  Check to received screen Size
         */
        private fun isScreenSizeRetrieved(widthHeight: IntArray): Boolean {
            return false
        }
        var mDialog: Dialog?=null

        /***
         *  show Snackbar
         */
        fun showSnackBar(rootView: View?, message: String?) {
            val snackbar = Snackbar.make(rootView!!, message!!, Snackbar.LENGTH_LONG)
            snackbar.view.setBackgroundColor(ContextCompat.getColor(rootView.context, R.color.colorYellow))
            snackbar.show()
        }

        /**
         * method to convert dp to pix
         */
        //opposite

        fun pxToDp(context: Context,dp: Int): Int {
            val density: Float = context.resources.displayMetrics.density
            return Math.round(dp * density)
          //  return (px / (Resources.getSystem().displayMetrics.density)/DisplayMetrics.DENSITY_DEFAULT)
        }

        /***
         *
         * methiod to convert dp to pix
         */
         fun dpToPx(dp: Int): Int {
            return (Resources.getSystem().displayMetrics.density * dp).toInt()
        }

        /***
         *  animations
         */
        fun showAnimation(context: Context?, view: View, slideInLeft: Int){
            val aninSlideRight = AnimationUtils.loadAnimation(context,
                    slideInLeft)
            view.startAnimation(aninSlideRight)
        }
        fun verifyAvailableNetwork1(context: Context): Boolean {
            val connectivityManager =
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }
        /**
       check internet is available or not
       */
        @Suppress("DEPRECATION")
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        fun verifyAvailableNetwork(context: Context): Boolean {
            val connectivityManager =
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }

        private fun isInternetAvailable(context: Context): Boolean {
            var result = false
            val connectivityManager =
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val networkCapabilities = connectivityManager.activeNetwork ?: return false
                val actNw =
                        connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
                result = when {
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            } else {
                @Suppress("DEPRECATION")
                connectivityManager.run {
                    connectivityManager.activeNetworkInfo?.run {
                        result = when (type) {
                            ConnectivityManager.TYPE_WIFI -> true
                            ConnectivityManager.TYPE_MOBILE -> true
                            ConnectivityManager.TYPE_ETHERNET -> true
                            else -> false
                        }

                    }
                }
            }

            return result
        }

        /***
         * Method to format numbers With K,M,B etc
         */

        fun getFormatedNumber(count: Long): String {
            if (count < 1000) return "" + count
            val exp = (ln(count.toDouble()) / ln(1000.0)).toInt()
            return String.format("%.2f%c", count / 1000.0.pow(exp.toDouble()), "KMBTPE"[exp - 1])
        }

        /***
         *  Method to get Currency format
         */

        fun getFormattedCurrency(number: Double):String{
            val format = NumberFormat.getCurrencyInstance()
        format.maximumFractionDigits = 2
        format.currency = Currency.getInstance("USD")

       return format.format(number)
        }

        /***
         * show  Progress bar
         */
        fun showProgress(context: Context) {

            if (mDialog != null && mDialog!!.isShowing) {
                dismissProgress()
            }
            mDialog = Dialog(context)
            mDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            mDialog!!.setCancelable(false)
            mDialog!!.setContentView(R.layout.custom_progress_bar)
            mDialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            mDialog!!.show()
        }

        /***
         * Hide progress bar
         */
        fun dismissProgress() {
            if (mDialog != null && mDialog!!.isShowing) {
                mDialog!!.dismiss()
            }
        }

        /**
         *  Load Image
         */
        fun loadImage(path_url: String, imageView: ImageView, context: Context) {
            Glide.with(context)
                .load(path_url)
                .apply(
                        RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .skipMemoryCache(false)
                                .placeholder(R.drawable.placeholder)
                                .circleCrop()
                )

                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                            e: GlideException?,
                            model: Any,
                            target: Target<Drawable>,
                            isFirstResource: Boolean
                    ): Boolean {
                        imageView.visibility = View.VISIBLE
                        return false
                    }

                    override fun onResourceReady(
                            resource: Drawable,
                            model: Any,
                            target: Target<Drawable>,
                            dataSource: DataSource,
                            isFirstResource: Boolean
                    ): Boolean {
                        imageView.visibility = View.VISIBLE

                        return false
                    }
                })

                .into(imageView)
        }

        /***
         * Down;load Image for cards
         */
        fun downloadImage(path_url: String, imageView: ImageView, context: Context) {
            Glide.with(context)
                .load(path_url)
                .apply(
                    RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .skipMemoryCache(false)
                        .placeholder(R.drawable.order_card)

                )

                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        imageView.visibility = View.VISIBLE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        imageView.visibility = View.VISIBLE

                        return false
                    }
                })

                .into(imageView)
        }

        /***
         *  Change text color
         */
        fun changeTextColor(textView: TextView, text: String, color: Int, startLength: Int, endLength: Int, oneWord: Boolean){
            val spannable = SpannableString(text)
            spannable.setSpan(RelativeSizeSpan(1.3f), startLength, endLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannable.setSpan(ForegroundColorSpan(color), startLength, endLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            if (oneWord){
                spannable.setSpan(RelativeSizeSpan(1.3f), endLength + 4, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                spannable.setSpan(ForegroundColorSpan(color), endLength + 4, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            textView.text = spannable
        }
        /***
         *  Change text color
         */
        fun changeTextColorSize(textView: TextView, text: String, color: Int, startLength: Int, endLength: Int, oneWord: Boolean){
            val spannable = SpannableString(text)
            spannable.setSpan(ForegroundColorSpan(color), startLength, endLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            textView.text = spannable
        }


        /***
         *  hide keyboard from activity
         */
        fun hideKeyBoard(activity: Activity) {
            try {
                val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                if (null != activity.currentFocus)
                    imm.hideSoftInputFromWindow(
                            activity.currentFocus!!
                                    .applicationWindowToken, 0
                    )
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        /***
         *  Get Date from utc Time stamp
         */
        fun getDateFromUTCTimestamp(mTimestamp: Long, mDateFormate: String?): String? {
            var date: String? = null
            try {
                val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                cal.timeInMillis = mTimestamp * 1000L
                date = DateFormat.format(mDateFormate, cal.timeInMillis).toString()
                val formatter = SimpleDateFormat(mDateFormate, Locale.ENGLISH)
                val value = formatter.parse(date)
                val dateFormatter = SimpleDateFormat(mDateFormate, Locale.ENGLISH)
                dateFormatter.timeZone = TimeZone.getDefault()
                date = dateFormatter.format(value)

                return date
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return date
        }

        /**
         * Show alert dialog
         */
        fun showAlertDialog(context: Context,text: String,view: View) {
            if (alert != null && alert!!.isShowing) {
                alert!!.dismiss()
            }
            val dialogBuilder = AlertDialog.Builder(context)
            dialogBuilder.setMessage(text)
                    .setCancelable(false)
                    .setPositiveButton(context.resources.getString(R.string.yes)) { dialog, id ->
                        dialog.cancel()

                    }
                    .setNegativeButton(context.resources.getString(R.string.no)) { dialog, id ->
                        dialog.cancel()

                    }
            alert = dialogBuilder.create()
            alert!!.show()
        }

    }


    /**
       method to convert string into HMAC
    */
    fun getHMAC(data: String, key: String, algorithm: String): String {
        var mac: Mac? = null
        var value = ""
        try {
            val secretKeySpec = SecretKeySpec(key.toByteArray(), algorithm)
            mac = Mac.getInstance(algorithm)
            mac!!.init(secretKeySpec)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: InvalidKeyException) {
            e.printStackTrace()
        }
        if (mac != null) {
            value = toHexString(mac.doFinal(data.toByteArray()))
        }
        return value
    }

    /**
    Part of getHMAC method
    */
    private fun toHexString(bytes: ByteArray): String {
        val formatter = Formatter()
        for (b in bytes) {
            formatter.format("%02x", b)
        }
        return formatter.toString()
    }

    /****
     *  method to create final Hmac
     */
    fun getFinalHmac(value: HashMap<String, String>, context: Context): String {
        var user_id = ""
        if (AlchemiApplication.alchemiApplication?.getUUID() != "") {
            user_id = AlchemiApplication.alchemiApplication!!.getUUID()
        }
        var hmac = ""
        val key = Constants.getDeviceID(context) + user_id
        try {
            val encryptedValue = CryptoHelper.encrypt(JSONObject(value as Map<*, *>).toString())
            hmac = getHMAC(encryptedValue, key, Constants.ALGORITHM)
        } catch (e: Exception) {
        }
        Log.e(TAG, "\nKey:\n$key")
        Log.e(TAG, "\nHMAC:\n$hmac")
        return hmac
    }

    /**
        method to return the displaying activity name
     */
    fun getCurrentActivityName(): String {
        return this.javaClass.simpleName
    }



}