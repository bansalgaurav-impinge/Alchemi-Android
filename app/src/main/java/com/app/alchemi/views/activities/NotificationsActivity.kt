package com.app.alchemi.views.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.alchemi.R
import com.app.alchemi.viewModel.GetTopNewsViewModel
import com.app.alchemi.views.adapters.NotificationsAdapter
import kotlinx.android.synthetic.main.recycler_view.*
import kotlinx.android.synthetic.main.toolbar_layout.*


class NotificationsActivity: AppCompatActivity() {
    private lateinit var getTopNewsViewModel: GetTopNewsViewModel
    private lateinit var notificationsAdapter: NotificationsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.notification_layout)
        toolbar_title.text=getString(R.string.notifications)
        ivBack.setOnClickListener {
            onBackPressed()
        }

        getTopNewsViewModel = ViewModelProvider(this).get(GetTopNewsViewModel::class.java)
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        rvList?.layoutManager = llm
        val  hash=HashMap<String, String>()
        hash[Constants.KEY_FIRST_NAME]="Today"
        hash[Constants.KEY_LAST_NAME]="You have received a payment of <b>$600.00</b> from <b>Bitcoin BTC</b>"
        hash[Constants.KEY_STATUS]="09:40AM"
        val  hash1=HashMap<String, String>()
        hash1[Constants.KEY_FIRST_NAME]="Today"
        hash1[Constants.KEY_LAST_NAME]="You have deposited the payment of <b>$800.00</b> to <b>Binance BNB</b>"
        hash1[Constants.KEY_STATUS]="08:00AM"
        val  hash4=HashMap<String, String>()
        hash4[Constants.KEY_FIRST_NAME]="Today"
        hash4[Constants.KEY_LAST_NAME]="You have deposited the payment of <b>$300.00</b> to <b>Binance BNB</b>"
        hash4[Constants.KEY_STATUS]="07:30AM"
        val  hash2=HashMap<String, String>()
        hash2[Constants.KEY_FIRST_NAME]="Yesterday"
        hash2[Constants.KEY_LAST_NAME]="You have deposited the payment of <b>$300.00</b> to <b>LiteCoin LTC</b>"
        hash2[Constants.KEY_STATUS]="05:00PM"
        val  hash3=HashMap<String, String>()
        hash3[Constants.KEY_FIRST_NAME]="Yesterday"
        hash3[Constants.KEY_LAST_NAME]="You have withdrawn the payment of <b>$600.00</b> from <b>Alchemi.com</b>"
        hash3[Constants.KEY_STATUS]="05:00PM"
        val dataList=ArrayList<HashMap<String, String>>()
        dataList.add(hash)
        dataList.add(hash1)
        dataList.add(hash4)
        dataList.add(hash2)
        dataList.add(hash3)
        notificationsAdapter = NotificationsAdapter(dataList)
        rvList?.adapter = notificationsAdapter
//        swipeRefresh.setOnRefreshListener {
//            getTopNews(true)              // refresh your list contents somehow
//        }
    }




}