package com.app.alchemi.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.app.alchemi.R
import com.app.alchemi.utils.ViewUtils


internal class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {
    override fun getCount(): Int {
        return mFragmentList.size
    }

    private val mFragmentTitleList = ArrayList<String>()
    private var mFragmentList = ArrayList<Fragment>()

    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    fun addFrag(fragment: Fragment, title: String) {
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }

    override fun getPageTitle(position: Int): CharSequence {
        return mFragmentTitleList[position]
    }
}


fun getTabIndicator(context: Context, icon: Int, title: String): View {
    val view = LayoutInflater.from(context).inflate(R.layout.custom_tab, null)
    val iv = view.findViewById(R.id.ivIcon) as ImageView
     val tv_title = view.findViewById(R.id.tvText) as TextView
    iv.setImageResource(icon)
    tv_title.text = title
    if (title.equals("Home")) {
        tv_title.setTextColor(ContextCompat.getColorStateList(context!!, R.color.colorYellow))
    }
        if (title.isEmpty()) {
        iv.layoutParams.height = ViewUtils.dpToPx(20)
        iv.layoutParams.width = ViewUtils.dpToPx(50)
        iv.scaleType=ImageView.ScaleType.FIT_XY
        iv.adjustViewBounds=true
        }

    return view

}

