package com.app.alchemi.views.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.alchemi.R
import com.app.alchemi.utils.ViewUtils
import com.app.alchemi.views.activities.HomeActivity
import com.app.alchemi.views.adapters.ContactSupportAdapter
import kotlinx.android.synthetic.main.contact_support_layout.*
import kotlinx.android.synthetic.main.recycler_view.*
import kotlinx.android.synthetic.main.toolbar_layout.*

class ContactSupportFragment: Fragment() {
    private lateinit var contactSupportAdapter: ContactSupportAdapter
    private var optionList= ArrayList<String>()
    companion object {
        fun newInstance() = ContactSupportFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View = inflater.inflate(R.layout.contact_support_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /***
         * Set Adapter
         */

       // val optionList = arrayOf<String>()
        optionList.add("Hi, Welome to Alchemi support \nHow can we help you?")
        val llm = LinearLayoutManager(context)
        llm.orientation = LinearLayoutManager.VERTICAL
        llm.stackFromEnd = true
        llm.reverseLayout = true
        rvList?.layoutManager = llm
         contactSupportAdapter = ContactSupportAdapter(optionList)
        rvList?.adapter = contactSupportAdapter
        rvList.smoothScrollToPosition(1)
        llm.scrollToPositionWithOffset(contactSupportAdapter.itemCount - 1, 0)
        rvList?.layoutManager = llm

        try {
            if (activity!=null) {
                (activity as HomeActivity).ivBack.setOnClickListener {
                    //  SettingsFragment
                    (activity as HomeActivity).onBackPressed()
                }
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as HomeActivity).tab_layout?.visibility= View.GONE
        requireActivity().ivBack.setImageResource(R.drawable.ic_back)
        requireActivity().clToolbar.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.colorYellow))
        requireActivity().toolbar.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.colorYellow))
        requireActivity().ivChat.visibility= View.GONE
        requireActivity().rlAcx.visibility= View.GONE
        requireActivity().toolbar_title.text  =""
        requireActivity().ivBack.setColorFilter(ContextCompat.getColor(requireContext(),R.color.colorLightBlack)) // Add tint color
        ivSend.setOnClickListener {
            if (etMessage.text.toString().trim().isNotEmpty()) {
                ViewUtils.hideKeyBoard(requireActivity())

                etMessage.text.clear()
            }
        }


    }

    override fun onDetach() {
        requireActivity().clToolbar.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.black))
        requireActivity().toolbar.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.black))
        requireActivity().ivChat.visibility= View.GONE
        requireActivity().rlAcx.visibility= View.GONE
        requireActivity().toolbar_title.text  = getString(R.string.settings)
        requireActivity().ivBack.setColorFilter(ContextCompat.getColor(requireContext(),R.color.colorGrey))
        super.onDetach()
    }

    override fun onDestroy() {
        requireActivity().clToolbar.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.black))
        requireActivity().toolbar.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.black))
        requireActivity().ivChat.visibility= View.GONE
        requireActivity().rlAcx.visibility= View.GONE
        requireActivity().toolbar_title.text  = getString(R.string.settings)
        requireActivity().ivBack.setColorFilter(ContextCompat.getColor(requireContext(),R.color.colorGrey))
        (activity as HomeActivity).tab_layout?.visibility= View.VISIBLE
        try {
            ViewUtils.hideKeyBoard(requireActivity())
        }catch (e:Exception){
            e.printStackTrace()
        }
        super.onDestroy()
    }

}