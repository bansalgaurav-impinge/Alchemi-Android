package com.app.alchemi.utils

import android.content.Context
import android.util.AttributeSet

import com.google.android.material.tabs.TabLayout


import java.lang.reflect.Field

class CustomTabLayout : TabLayout {

    constructor(context: Context) : super(context) {
        initTabMinWidth()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initTabMinWidth()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr) {
        initTabMinWidth()
    }

    private fun initTabMinWidth() {
        val wh = ViewUtils.getScreenSize(context)
        val tabMinWidth = wh[WIDTH_INDEX] / DIVIDER_FACTOR

        val field: Field
        try {
            field = TabLayout::class.java.getDeclaredField(SCROLLABLE_TAB_MIN_WIDTH)
            field.isAccessible = false
            field.set(this, tabMinWidth)
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    companion object {

        private val WIDTH_INDEX = 0
        private val DIVIDER_FACTOR = 3
       // mTabStrip
      private const val SCROLLABLE_TAB_MIN_WIDTH = "mTabMinWidth"
    }
}
