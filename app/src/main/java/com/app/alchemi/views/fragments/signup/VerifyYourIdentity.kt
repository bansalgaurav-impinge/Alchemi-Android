package com.app.alchemi.views.fragments.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.alchemi.R
import com.app.alchemi.interfaceintercation.OnItemClickListener
import com.app.alchemi.utils.FragmentNavigation
import com.app.alchemi.views.adapters.UserVerifyAdapter
import kotlinx.android.synthetic.main.recycler_view.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import kotlinx.android.synthetic.main.verify_identity_layout.*

class VerifyYourIdentity: Fragment(), OnItemClickListener {
    private lateinit var userVerifyAdapter: UserVerifyAdapter

    companion object {
        fun newInstance() = VerifyYourIdentity()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.verify_identity_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().toolbar_title.text = getString(R.string.verify_your_identtity)
        val imageIdList = arrayOf<Int>(
                R.drawable.ic_user,
                R.drawable.ic_id,
                R.drawable.ic_camera)
        val optionList = arrayOf<String>(
                getString(R.string.full_legal_name),
                getString(R.string.id_doument),
                getString(R.string.selfie))
        val llm = LinearLayoutManager(context)
        llm.orientation = LinearLayoutManager.VERTICAL
        rvList?.layoutManager = llm
        userVerifyAdapter = UserVerifyAdapter(optionList, imageIdList, this)
        rvList?.setHasFixedSize(true)
        rvList?.adapter = userVerifyAdapter
        tvContinue.setOnClickListener {
            showFullLegalNameScreen()
        }
    }


    override fun getItemClicked(itemId: String?) {
        when (itemId){

        }
    }
    fun showFullLegalNameScreen() {
        FragmentNavigation
                .Builder(activity?.supportFragmentManager, R.id.fragmentContainer,FullLegalNameFragment.newInstance())
                .addToBackstack()
                .execute()
    }


}