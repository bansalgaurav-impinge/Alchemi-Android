package com.app.alchemi.views.activities

import android.os.Bundle
import android.util.Log
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import com.app.alchemi.R
import com.app.alchemi.interfaceintercation.OnItemClickListener
import com.app.alchemi.utils.Target
import com.app.alchemi.utils.navigateTo
import com.app.alchemi.views.adapters.AddPaymentMethodAdapter
import kotlinx.android.synthetic.main.add_payment_method_layout.*
import kotlinx.android.synthetic.main.toolbar_layout_11.*


class AddPaymentMethodActivity: AppCompatActivity(), OnItemClickListener {
    private lateinit var addPaymentMethodAdapter: AddPaymentMethodAdapter
    private var lastExpandedPosition = 0
    private var childSelectedPosition=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_payment_method_layout)
        toolbar_title.text = getString(R.string.payment_method)
        val imageIdList = arrayOf<Int>(
            R.drawable.master_card_black,
            R.drawable.visa_card_black,
            R.drawable.banking_icon)
        val cardIcons = arrayOf<Int>(
            R.drawable.card_tab_bg,
            R.drawable.card_tab_bg)
        ivBack.setOnClickListener {
            onBackPressed()
        }

        tvContinue.setOnClickListener {
            if (lastExpandedPosition!=2) {
                val bundle = Bundle()
                bundle.putString(Constants.KEY_TITLE, "" + lastExpandedPosition)
                bundle.putString(Constants.KEY_DESCRIPTION, "" + childSelectedPosition)
                navigateTo(this, Target.ADD_CARD_DETAIL, bundle)
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_left)
            }else{
                navigateTo(this, Target.PLAID)
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_left)
            }
        }
        if (expandableListView != null) {
            val expandableListDetail = HashMap<String, List<String>>()
            val cards: MutableList<String> = ArrayList()
            cards.add(getString(R.string.debit_card))
            cards.add(getString(R.string.credit_card))
            val banking: MutableList<String> = ArrayList()
            expandableListDetail[getString(R.string.master_card)]=cards
            expandableListDetail[getString(R.string.visa_card)]=cards
            expandableListDetail[getString(R.string.banking)]=banking

            val titleList = listOf(getString(R.string.master_card),getString(R.string.visa_card),getString(R.string.banking))
            addPaymentMethodAdapter = AddPaymentMethodAdapter(
               this,
                titleList,
                expandableListDetail,imageIdList,cardIcons
            )
            expandableListView!!.setAdapter(addPaymentMethodAdapter)
            expandableListView!!.refreshDrawableState()
            expandableListView.expandGroup(0)

            expandableListView!!.setOnGroupExpandListener { groupPosition ->
                if (lastExpandedPosition != -1 && groupPosition != lastExpandedPosition) {
                    expandableListView.collapseGroup(lastExpandedPosition)
                }
                lastExpandedPosition = groupPosition
                addPaymentMethodAdapter.selectedPosition=0
                        addPaymentMethodAdapter.notifyDataSetChanged()

            }
            expandableListView!!.setOnGroupCollapseListener { groupPosition ->
            }
            expandableListView.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
                addPaymentMethodAdapter.selectedPosition=childPosition
                addPaymentMethodAdapter.notifyDataSetChanged()
                childSelectedPosition = childPosition
//                Toast.makeText(
//                    applicationContext, expandableTitleList.get(groupPosition)
//                        .toString() + " -> "
//                            + expandableDetailList.get(
//                        expandableTitleList.get(groupPosition)
//                    ).get(
//                        childPosition
//                    ), Toast.LENGTH_SHORT
//                ).show()
                false
            }

            val vto: ViewTreeObserver = expandableListView.viewTreeObserver
            vto.addOnGlobalLayoutListener {

                expandableListView.setIndicatorBounds(
                    expandableListView.measuredWidth - 80, expandableListView.measuredWidth)
            }

        }
    }







    override fun getItemClicked(itemId: String?) {
        when (itemId){
            "0"->{
              //  showFullLegalNameScreen()
            }
            "1"->{
             //   showIdDocumentsScreen()
            }
            "2"->{
               /// showFullLegalNameScreen(FullLealNameFragment.newInstance())
            }
        }



   }

    override fun onResume() {
        super.onResume()
        if(Constants.SEKECTED_ACTIVITY==1){
            Constants.SEKECTED_ACTIVITY=0
            finish()
        }

    }


}