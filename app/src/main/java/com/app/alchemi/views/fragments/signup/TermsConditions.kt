package com.app.alchemi.views.fragments.signup

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.ViewUtils
import androidx.core.view.marginEnd
import androidx.fragment.app.Fragment
import com.app.alchemi.R
import com.app.alchemi.views.activities.SignUpActivity
import kotlinx.android.synthetic.main.terms_conditions_fragment_layout.*
import kotlinx.android.synthetic.main.toolbar_layout.*

class TermsConditions: Fragment() {
    companion object {
        fun newInstance() = TermsConditions()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.terms_conditions_fragment_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().toolbar_title.gravity= Gravity.CENTER
        webView.loadUrl("https://www.google.com/")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requireActivity().toolbar_title.text = getString(R.string.terms_and_conditions)

    }
    override fun onDetach() {
        requireActivity().toolbar_title.gravity=Gravity.NO_GRAVITY
        super.onDetach()
    }

}