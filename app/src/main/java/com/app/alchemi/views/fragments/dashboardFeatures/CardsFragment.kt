package com.app.alchemi.views.fragments.dashboardFeatures

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.app.alchemi.R
import com.app.alchemi.utils.ViewUtils
import com.app.alchemi.views.adapters.CardPagerAdapter
import com.app.alchemi.views.adapters.getTabIndicator
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.card_fragment_layout.*


class CardsFragment: Fragment() {

    companion object {
        fun newInstance() = CardsFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.card_fragment_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val words = arrayListOf(
                "Ruby Metal",
                "Royal Indigo",
                "Visa Card",
                "Ruby Steel",
                "Mountain Royal"
        )

        pager.adapter = CardPagerAdapter(requireContext(), words)
        TabLayoutMediator(tabLayout, pager) {tab, position ->
           tab.customView=getTabIndicator(
                            tabLayout.context,
                            R.drawable.order_card,words[position]
                    )

        }.attach()

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }



    fun getTabIndicator(context: Context, icon: Int, title: String): View {
        val view = LayoutInflater.from(context).inflate(R.layout.card_view, null)
        val iv = view.findViewById(R.id.ivIcon) as ImageView
        val tvTitle = view.findViewById(R.id.tvTabText) as TextView
        iv.setImageResource(icon)
        tvTitle.text =  title.split(" ")[0]+"\n"+title.split(" ")[1]
        return view

    }
}




