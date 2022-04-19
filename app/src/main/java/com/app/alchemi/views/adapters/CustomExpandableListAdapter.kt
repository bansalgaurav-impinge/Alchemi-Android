package com.app.alchemi.views.adapters

import android.content.Context

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.app.alchemi.R
import kotlinx.android.synthetic.main.list_item.view.*
import java.util.HashMap
class CustomExpandableListAdapter internal constructor(
        private val context: Context,
        private val titleList: List<String>,
        private val dataList: HashMap<String, List<String>>
) : BaseExpandableListAdapter() {
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
            convertView = layoutInflater.inflate(R.layout.list_item_child, null)
        }
        val expandedListTextView = convertView!!.findViewById<TextView>(R.id.tvCoin)
        expandedListTextView.text = expandedListText
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
            convertView = layoutInflater.inflate(R.layout.list_item, null)
        }
        val listTitleTextView = convertView!!.findViewById<TextView>(R.id.tvCoin)
        val ivArrow = convertView!!.findViewById<ImageView>(R.id.ivArrow)

        listTitleTextView.text = listTitle
        if (listPosition==1){
            ivArrow.visibility=View.GONE
            convertView.tvCoinAmountVariation.background=ContextCompat.getDrawable(context,(R.drawable.account_bottom_bg))
        }else{
            ivArrow.visibility=View.VISIBLE
            convertView.tvCoinAmountVariation.background=ContextCompat.getDrawable(context,(R.drawable.account_top_bg))
        }
        return convertView
    }
    override fun hasStableIds(): Boolean {
        return false
    }
    override fun isChildSelectable(listPosition: Int, expandedListPosition: Int): Boolean {
        return true
    }


}