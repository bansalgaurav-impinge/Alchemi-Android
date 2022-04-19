package com.app.alchemi.views.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.alchemi.R
import com.app.alchemi.models.Message
import com.app.alchemi.utils.AlchemiApplication
import com.app.alchemi.utils.ViewUtils
import com.app.alchemi.viewModel.CheckUserPinViewModel
import com.app.alchemi.viewModel.ContactSupportMessageListViewModel
import com.app.alchemi.views.activities.HomeActivity
import com.app.alchemi.views.adapters.ContactSupportAdapter
import kotlinx.android.synthetic.main.contact_support_layout.*
import kotlinx.android.synthetic.main.recycler_view.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import java.util.*

class ContactSupportFragment: Fragment() {
    private lateinit var contactSupportAdapter: ContactSupportAdapter
    private var optionList= ArrayList<String>()
    private lateinit var checkUserPinViewModel: CheckUserPinViewModel
    private lateinit var contactSupportMessageListViewModel: ContactSupportMessageListViewModel
    companion object {
        fun newInstance() = ContactSupportFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View = inflater.inflate(R.layout.contact_support_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkUserPinViewModel = ViewModelProvider(this).get(CheckUserPinViewModel::class.java)
        contactSupportMessageListViewModel=ViewModelProvider(this).get(ContactSupportMessageListViewModel::class.java)
        /***
         * Set Adapter
         */

        optionList.add("Hi, Welcome to Alchemi support \nHow can we help you?")
//        val llm = LinearLayoutManager(context)
//        llm.orientation = LinearLayoutManager.VERTICAL
//        llm.stackFromEnd = true
//        llm.reverseLayout = true
//        rvList?.layoutManager = llm
//       /// contactSupportAdapter = ContactSupportAdapter(optionList)
//        rvList?.adapter = contactSupportAdapter
//        rvList.smoothScrollToPosition(1)
//        llm.scrollToPositionWithOffset(contactSupportAdapter.itemCount - 1, 0)
//       // rvList?.layoutManager = llm
    }

    override fun onResume() {
        super.onResume()
        (activity as HomeActivity).tab_layout?.visibility= View.GONE
        requireActivity().clToolbar.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.colorYellow))
        requireActivity().toolbar.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.colorYellow))
        requireActivity().ivChat.visibility= View.GONE
        requireActivity().rlAcx.visibility= View.GONE
        requireActivity().ivSettings.visibility= View.GONE
        requireActivity().ivBack.visibility= View.VISIBLE
        requireActivity().toolbar_title.text =""
        requireActivity().ivBack.setColorFilter(ContextCompat.getColor(requireContext(),R.color.colorLightBlack)) // Add tint color
        ivSend.setOnClickListener {
            if (etMessage.text.toString().trim().isNotEmpty()) {
                ViewUtils.hideKeyBoard(requireActivity())
                saveContactSupportMessage(etMessage.text.toString())
                etMessage.text.clear()
            }
        }
        getContactSupportMessageList()

    }

    override fun onDetach() {
        requireActivity().clToolbar.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.black))
        requireActivity().toolbar.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.black))
        requireActivity().ivBack.setColorFilter(ContextCompat.getColor(requireContext(),R.color.colorGrey))

        if (requireActivity().supportFragmentManager.fragments.last().toString().contains("Settings")){
        requireActivity().ivChat.visibility= View.GONE
        requireActivity().rlAcx.visibility= View.GONE
        requireActivity().toolbar_title.text  = getString(R.string.settings)
        }else{
            requireActivity().ivChat.visibility= View.VISIBLE
            requireActivity().rlAcx.visibility= View.VISIBLE
            requireActivity().ivSettings.visibility= View.VISIBLE
            requireActivity().ivBack.visibility= View.GONE
            requireActivity().toolbar_title.text  = getString(R.string.home)
        }
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

    /***
     * save contract Support Message
     */
    fun saveContactSupportMessage(message: String){
        val hashMap= HashMap<String,String>()
      //  hashMap[Constants.KEY_USER]=""+AlchemiApplication.alchemiApplication?.getUUID()
        hashMap[Constants.KEY_MESSAGE]=message
        if (!ViewUtils.verifyAvailableNetwork(requireContext())) {
            ViewUtils.showSnackBar(view, getString(R.string.you_re_not_connected_to_the_internet_n_please_connect_and_retry))
        }
        else {
            checkUserPinViewModel.saveContactSupportMessage(hashMap,view,Constants.Token + AlchemiApplication.alchemiApplication?.getToken())!!.observe(viewLifecycleOwner, Observer { liveData ->
                ViewUtils.dismissProgress()
                if (liveData.code == Constants.CODE_200) {
                    getContactSupportMessageList()
                } else {
                    val msg = liveData.message
                    ViewUtils.showSnackBar(view, "" + msg)
                }
            })
        }
    }
    /***
     * get contract Support Message List
     */
    fun getContactSupportMessageList(){
        if (!ViewUtils.verifyAvailableNetwork(requireContext())) {
            ViewUtils.showSnackBar(view, getString(R.string.you_re_not_connected_to_the_internet_n_please_connect_and_retry))
        }
        else {
            contactSupportMessageListViewModel.getContactSupportMessageList(view,Constants.Token + AlchemiApplication.alchemiApplication?.getToken())!!.observe(viewLifecycleOwner, Observer { liveData ->
                ViewUtils.dismissProgress()
                if (liveData.code == Constants.CODE_200) {
                    val llm = LinearLayoutManager(context)
                    llm.orientation = LinearLayoutManager.VERTICAL
                    llm.stackFromEnd = true
                  //  llm.reverseLayout = true
                    rvList?.layoutManager = llm
                 //  Collections.reverse(liveData.data.messages)
                    val messageModel = Message("","","Hi, Welcome to Alchemi support \n" +
                            "How can we help you?","")
                    var list: List<Message>?=null
                    list= listOf(messageModel)
                    list=list+liveData.data.messages
                    contactSupportAdapter = ContactSupportAdapter(list)
                    rvList?.adapter = contactSupportAdapter
                    rvList.smoothScrollToPosition(liveData.data.messages.size)
                    contactSupportAdapter.notifyDataSetChanged()
                    llm.scrollToPosition(contactSupportAdapter.itemCount - 1)
                    rvList.scrollToPosition(contactSupportAdapter.itemCount)
                } else {
                    val llm = LinearLayoutManager(context)
                    llm.orientation = LinearLayoutManager.VERTICAL
                    llm.stackFromEnd = true
                    llm.reverseLayout = true
                    rvList?.layoutManager = llm
                    Collections.reverse(liveData.data.messages)
                    val messageModel = Message("","","Hi, Welcome to Alchemi support \n" +
                            "How can we help you?","")
                    var list: List<Message>?=null
                    list= listOf(messageModel)
                    list=liveData.data.messages
                    contactSupportAdapter = ContactSupportAdapter(list)
                    rvList?.adapter = contactSupportAdapter
                    rvList.smoothScrollToPosition(liveData.data.messages.size)
                    llm.scrollToPosition(contactSupportAdapter.itemCount - 1)
                 //   llm.scrollToPositionWithOffset(contactSupportAdapter.itemCount - 1, 0)
                }
            })
        }
    }
}