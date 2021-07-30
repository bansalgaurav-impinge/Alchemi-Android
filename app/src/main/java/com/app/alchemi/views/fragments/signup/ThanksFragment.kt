package com.app.alchemi.views.fragments.signup

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.TextView.BufferType
import androidx.fragment.app.Fragment
import com.app.alchemi.R
import com.app.alchemi.utils.AlchemiApplication
import com.app.alchemi.utils.FragmentNavigation
import com.app.alchemi.utils.ViewUtils

import kotlinx.android.synthetic.main.thanks_for_joining_layout.*
import kotlinx.android.synthetic.main.thanks_for_joining_layout.checkboxTermsConditions
import kotlinx.android.synthetic.main.thanks_for_joining_layout.cl_parent
import kotlinx.android.synthetic.main.thanks_for_joining_layout.llBottoms
import kotlinx.android.synthetic.main.thanks_for_joining_layout.tvContinue
import kotlinx.android.synthetic.main.toolbar_layout.*


class ThanksFragment: Fragment() {
    companion object {
        fun newInstance() = ThanksFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.thanks_for_joining_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val aninSlideRight = AnimationUtils.loadAnimation(context,
            R.anim.slide_in_left)
        llBottoms.startAnimation(aninSlideRight)

        AlchemiApplication.alchemiApplication?.saveScreenIndex("6")
        singleTextView(tvThanksBottomText,getString(R.string.thanks_bottom_text),getString(R.string.privacy_notice),getString(R.string.further_information))

        /***
         *  Click to open Email confirmation
         */
        tvContinue.setOnClickListener {
            if (!checkboxTermsConditions.isChecked) {
                ViewUtils.showSnackBar(cl_parent, getString(R.string.please_select_checkbox_to_receive_offers))
            } else if (!ViewUtils.verifyAvailableNetwork(requireContext())) {
                ViewUtils.showSnackBar(cl_parent, getString(R.string.you_re_not_connected_to_the_internet_n_please_connect_and_retry)
                )
            } else {
                showSSNScreen()

            }
        }

    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
               requireActivity().toolbar_title.text  = ""

    }

    fun showSSNScreen(){
        FragmentNavigation
                .Builder(activity?.supportFragmentManager, R.id.fragmentContainer, SSNFragment.newInstance())
                .addToBackstack()
                .execute()
    }

    /***
     *  Method to append text and color of text
     */
    private fun singleTextView(textView: TextView, text: String, privacyNotice: String, furtherInfo: String,) {
        val spanText = SpannableStringBuilder()
        spanText.append(text)
        spanText.append(" $privacyNotice")
        spanText.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {

                // On Click Action
                FragmentNavigation
                    .Builder(activity?.supportFragmentManager, R.id.fragmentContainer, PrivacyNoticeFragment.newInstance())
                    .addToBackstack()
                    .execute()
            }

            override fun updateDrawState(textPaint: TextPaint) {
                textPaint.color = textPaint.linkColor // you can use custom color
                textPaint.isUnderlineText = false // this remove the underline
            }
        }, spanText.length - privacyNotice.length, spanText.length, 0)
        spanText.append(" $furtherInfo")
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.setText(spanText, BufferType.SPANNABLE)
    }




}