package com.app.alchemi.views.adapters

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.app.alchemi.R
import java.util.*


internal class AddPaymentMethodAdapter(
    private val context: Context,
    private val titleList: List<String>,
    private val dataList: HashMap<String, List<String>>,
    private val imageIdList: Array<Int>,
    private val cardIcons: Array<Int>
) : BaseExpandableListAdapter() {
    var selectedPosition=0
    override fun getChild(listPosition: Int, expandedListPosition: Int): Any {
        return this.dataList[this.titleList[listPosition]]!![expandedListPosition]
    }
    override fun getChildId(listPosition: Int, expandedListPosition: Int): Long {
        return expandedListPosition.toLong()
    }
    override fun getChildView(listPosition: Int, expandedListPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val expandedListText = getChild(listPosition, expandedListPosition) as String
        if (convertView == null) {
            val layoutInflater =
                this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.add_payment_method_card_view, null)
        }
        val expandedListTextView = convertView!!.findViewById<TextView>(R.id.tvCardName)
        val expandedListIcon = convertView.findViewById<ImageView>(R.id.ivIcon)

        expandedListIcon.setImageResource(cardIcons[expandedListPosition])
        expandedListTextView.text = expandedListText
        if (expandedListPosition==selectedPosition){
            expandedListIcon.setImageDrawable(context.getDrawable(R.drawable.card_yellow))
            expandedListTextView.setTextColor(context.resources.getColor(R.color.colorYellow))
        }else{
            expandedListIcon.setImageDrawable(context.getDrawable(R.drawable.card))
            expandedListTextView.setTextColor(context.resources.getColor(R.color.black))
        }
//        convertView.tag = expandedListPosition
//        convertView.setOnClickListener {
//            selectedPosition=expandedListPosition
//            notifyDataSetChanged()
//        }
        return convertView
    }
    override fun getChildrenCount(listPosition: Int): Int {
        return this.dataList[this.titleList[listPosition]]!!.size
    }
    override fun getGroup(listPosition: Int): Any {
        return this.titleList[listPosition]
    }
    override fun getGroupCount(): Int {
        return this.titleList.size
    }
    override fun getGroupId(listPosition: Int): Long {
        return listPosition.toLong()
    }
    override fun getGroupView(listPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView

        val listTitle = getGroup(listPosition) as String
        if (convertView == null) {
            val layoutInflater =
                this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.add_payment_method_adapter, null)
        }
        val listTitleTextView = convertView!!.findViewById<TextView>(R.id.tvOption)
        val ivArrow = convertView.findViewById<ImageView>(R.id.ivIcon)
        ivArrow.setImageResource(imageIdList[listPosition])
        listTitleTextView.text = listTitle



        return convertView
    }
    override fun hasStableIds(): Boolean {
        return false
    }
    override fun isChildSelectable(listPosition: Int, expandedListPosition: Int): Boolean {
        return true
    }
}
