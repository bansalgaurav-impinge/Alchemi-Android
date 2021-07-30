package com.app.alchemi.views.fragments.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.app.alchemi.R
import com.app.alchemi.utils.FragmentNavigation
import kotlinx.android.synthetic.main.document_submission_layout.*
import kotlinx.android.synthetic.main.toolbar_layout.*

class UploadDocumentFragment : Fragment() {

    companion object {
        fun newInstance() = UploadDocumentFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.document_submission_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val aninSlideRight = AnimationUtils.loadAnimation(context,
            R.anim.slide_in_left)
        // assigning that animation to
        // the image and start animation
        ll_layout.startAnimation(aninSlideRight)
        tvTapAnywhere.startAnimation(aninSlideRight)

            cl_parent.setOnClickListener {
                FragmentNavigation
                        .Builder(activity?.supportFragmentManager, R.id.fragmentContainer, UploadDocumentVerifiedFragment.newInstance())
                        .addToBackstack()
                        .execute()
            }


    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requireActivity().toolbar_title.text = ""

    }
}