package com.app.alchemi.views.fragments.signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.app.alchemi.R
import com.app.alchemi.utils.AlchemiApplication
import com.app.alchemi.utils.FragmentNavigation
import com.app.alchemi.views.activities.HomeActivity
import com.app.alchemi.views.activities.MainActivity
import kotlinx.android.synthetic.main.email_verification_layout.*
import kotlinx.android.synthetic.main.email_verification_layout.tvEmail
import kotlinx.android.synthetic.main.toolbar_layout.*

class EmailVerificationFragment : Fragment() {

    companion object {
        fun newInstance() = EmailVerificationFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.email_verification_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val aninSlideRight = AnimationUtils.loadAnimation(context,
            R.anim.slide_in_left)
        // assigning that animation to
        // the image and start animation
        ll_layout.startAnimation(aninSlideRight)
        tvTapAnywhere.startAnimation(aninSlideRight)


        cl_parent.setOnClickListener {
            if (!Constants.isSignIn && !Constants.isEditProfile) {
                AlchemiApplication.alchemiApplication?.saveScreenIndex("3")
                FragmentNavigation
                        .Builder(activity?.supportFragmentManager, R.id.fragmentContainer, PhoneNumberFragment.newInstance())
                        .addToBackstack()
                        .execute()
            }else if(Constants.isEditProfile){

                (activity as HomeActivity).clearStack(2)
            }
            else
                startActivity(Intent(context, HomeActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
                activity?.finish()
        }


    }
    override fun onResume() {
        super.onResume()
        Constants.EMAIL= AlchemiApplication.alchemiApplication?.getEmail().toString()
        if(Constants.isEditProfile) {
            requireActivity().toolbar_title.text = ""
            requireActivity().ivBack.setImageResource(R.drawable.ic_back)
            requireActivity().ivChat.visibility=View.GONE
            requireActivity().rlAcx.visibility=View.GONE

            (activity as HomeActivity).tab_layout?.visibility=View.GONE
        }
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        try {
            if ( AlchemiApplication.alchemiApplication?.getEmail()?.trim()!="" && AlchemiApplication.alchemiApplication?.getEmail()?.trim()!!.isNotEmpty() && AlchemiApplication.alchemiApplication?.getEmail()?.trim()!=null){
                Constants.EMAIL= AlchemiApplication.alchemiApplication?.getEmail().toString()

            }
        }catch (e: Exception){
            e.printStackTrace()
        }
        tvEmail.text = Constants.EMAIL
        requireActivity().toolbar_title.text = ""

    }

    override fun onDestroyView() {
        if (!Constants.isSignIn){
            AlchemiApplication.alchemiApplication?.saveScreenIndex("3")
         }else{
            AlchemiApplication.alchemiApplication?.saveScreenIndex("0")
        }
        super.onDestroyView()
    }

}