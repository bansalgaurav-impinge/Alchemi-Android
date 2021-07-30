package com.app.alchemi.views.fragments.signup

import android.content.res.Configuration
import android.os.AsyncTask
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.alchemi.R
import com.app.alchemi.utils.*
import com.app.alchemi.views.activities.SignUpActivity
import com.geetest.sdk.*
import com.geetest.sdk.utils.GT3ServiceNode
import kotlinx.android.synthetic.main.toolbar_layout.*
import kotlinx.android.synthetic.main.verify_catcha_layout.*
import org.json.JSONObject


class CaptchaVerificationFragment: Fragment() {
    private var gt3GeetestUtils: GT3GeetestUtils? = null
    private var gt3ConfigBean: GT3ConfigBean? = null
    val TAG = "Captcha>>"
    companion object {
        fun newInstance() = CaptchaVerificationFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.verify_catcha_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().toolbar_title.text  = ""

         if (!ViewUtils.verifyAvailableNetwork(requireContext())) {
            ViewUtils.showSnackBar(scrollable, getString(R.string.you_re_not_connected_to_the_internet_n_please_connect_and_retry))
             (activity as SignUpActivity).onBackPressed()
        }
        else {
             geetestInit()
            gt3GeetestUtils?.startCustomFlow()
        }

    }

    private fun geetestInit() {
        gt3GeetestUtils = GT3GeetestUtils(activity)
        gt3ConfigBean = GT3ConfigBean()
        // Set verification mode, 1: bind, 2: unbind
        gt3ConfigBean!!.pattern = 1
        gt3ConfigBean!!.isUnCanceledOnTouchKeyCodeBack =false
        gt3ConfigBean!!.isCanceledOnTouchOutside=false



        gt3ConfigBean!!.lang ="en"
        gt3ConfigBean!!.timeout =100000
        // Set WebView internal H5 page request timeout, unit ms, default 10000
        gt3ConfigBean!!.webviewTimeout =100000
        gt3ConfigBean!!.gt3ServiceNode = GT3ServiceNode.NODE_IPV6
        val gt3LoadImageView= GT3LoadImageView(activity)
        gt3LoadImageView.loadViewWidth=50
        gt3LoadImageView.loadViewHeight=50
        gt3ConfigBean?.loadImageView=gt3LoadImageView
        gt3ConfigBean!!.listener = object : GT3Listener() {
            /**
             *
            The verification code is loaded
             * @param duration Information such as loading time and version, in json format
             */
            override fun onDialogReady(duration: String) {
                Log.e(TAG, "GT3BaseListener-->onDialogReady-->$duration")
            }

            /**
             * Graphic verification result callback
             * @param code 1
             */
            override fun onReceiveCaptchaCode(code: Int) {
                Log.e(TAG, "GT3BaseListener-->onReceiveCaptchaCode-->$code")
            }

            /**
             * Custom api2 callback
             * @param result，api2
             */
            override fun onDialogResult(result: String) {
                Log.e(TAG, "GT3BaseListener-->onDialogResult-->$result")

                RequestAPI2().execute(result)
            }

            /**
             * Statistics, refer to the access document
             * @param result
             */
            override fun onStatistics(result: String) {
                Log.e(TAG, "GT3BaseListener-->onStatistics-->$result")
            }

            /**
             *
             * @param num 1 Click the close button of the verification code to close the verification code
             */
            override fun onClosed(num: Int) {
                Log.e(TAG, "GT3BaseListener-->onClosed-->$num")
                val currentFragment = activity!!.supportFragmentManager.findFragmentById(R.id.fragmentContainer)
                val fragmentTransaction = activity!!.supportFragmentManager!!.beginTransaction()
                fragmentTransaction.detach(currentFragment!!)
                fragmentTransaction.attach(currentFragment!!)
                fragmentTransaction.commit()

            }

            /**
             *
             * @param result
             */
            override fun onSuccess(result: String) {
                Log.e(TAG, "GT3BaseListener-->onSuccess-->$result")
                AlchemiApplication.alchemiApplication?.saveScreenIndex("4")
                FragmentNavigation
                        .Builder(activity?.supportFragmentManager, R.id.fragmentContainer, VerifyYourIdentity.newInstance())
                        .execute()
               // (activity as SignUpActivity).onBackPressed()
            }

            /**
             *
             * @param errorBean
             */
            override fun onFailed(errorBean: GT3ErrorBean) {
                Log.e(TAG, "GT3BaseListener-->onFailed-->$errorBean")
                //(activity as SignUpActivity).onBackPressed()
            }

            /**
             * Hit Captcha API
             */
            override fun onButtonClick() {
                RequestAPI1().execute()
            }
        }
        gt3GeetestUtils!!.init(gt3ConfigBean)
    }

    /**
     * Captcha API
     */
    internal inner class RequestAPI1 : AsyncTask<Void?, Void?, JSONObject?>() {

        override fun onPostExecute(params: JSONObject?) {
            // SDK可识别格式为
            // {"success":1,"challenge":"06fbb267def3c3c9530d62aa2d56d018","gt":"019924a82c70bb123aae90d483087f94","new_captcha":true}
            // TODO 设置返回api1数据，即使为 null 也要设置，SDK内部已处理
            gt3ConfigBean!!.api1Json = params
            // 继续验证
            gt3GeetestUtils!!.getGeetest()
        }

        override fun doInBackground(vararg params: Void?): JSONObject? {
            var jsonObject: JSONObject? = null
            try {
                val result = NetRequestUtils.requestGet(AddressUtils.getRegister(context!!, "slide"))
                jsonObject = JSONObject(result)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return jsonObject        }
    }

    /**
     * 请求api2
     */
    internal inner class RequestAPI2 : AsyncTask<String?, Void?, String>() {


        override fun onPostExecute(result: String) {
            try {
                val jsonObject = JSONObject(result)
                var status = jsonObject.optString("result")
                if (TextUtils.isEmpty(status)) {
                    status = jsonObject.optString("status")
                }
                getActivity()?.runOnUiThread(Runnable {
                    if ("success" == status) {
                        gt3GeetestUtils?.dismissGeetestDialog()
                        gt3GeetestUtils!!.showSuccessDialog()

                    } else {
                        gt3GeetestUtils?.dismissGeetestDialog()
                        gt3GeetestUtils!!.showFailedDialog()
                    }
                })

            } catch (e: Exception) {
                e.printStackTrace()
                gt3GeetestUtils!!.showFailedDialog()
            }
        }

        override fun doInBackground(vararg params: String?): String {
            return NetRequestUtils.requestPostByForm(
                    AddressUtils.getValidate(context!!, "slide"),
                    params[0]
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        if (gt3GeetestUtils != null) {
            gt3GeetestUtils!!.destory()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        gt3GeetestUtils!!.changeDialogLayout()
    }

}