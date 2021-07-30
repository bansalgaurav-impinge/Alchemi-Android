package com.app.alchemi.models

import java.util.*
import kotlin.collections.HashMap

internal object ExpandableListData {
    val data: HashMap<String, List<String>>
        get() {
            val expandableListDetail = HashMap<String, List<String>>()
            val alchemiWallet: MutableList<String> =
                    ArrayList()
            alchemiWallet.add("Alchemi.com")
            alchemiWallet.add("Bitcoin")
            alchemiWallet.add("USD Coin")
            alchemiWallet.add("Yearn Finance")
            val alchemiEarn: MutableList<String> = ArrayList()
            expandableListDetail["Alchemi Wallet"] = alchemiWallet
            expandableListDetail["Alchemi Earn"] = alchemiEarn
            return expandableListDetail
        }
}