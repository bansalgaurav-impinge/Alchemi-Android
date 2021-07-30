package com.app.alchemi.utils

import android.R
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN
import com.app.alchemi.utils.FragmentNavigation.Builder

/**
 * Use the [Builder] from this class to construct a valid FragmentTransaction and call
 * execute afterwards.
 */
class FragmentNavigation {

    class Builder(
        private val fragmentManager: FragmentManager?, private var container: Int,
        private val fragment: Fragment
    ) {

        private var addFragment = false
        private var animate = true
        private var addToBackstack = false
        private var allowStateLoss = false
        private var addSharedElement = false
        private var sharedElement: View? = null
        private var sharedElementName: String? = null


        /**
         * Call this method when the transaction should not call onSaveInstanceState
         * because this can cause issues on API > 11
         * Solution suggested on
         * https://stackoverflow.com/questions/7575921/
         * illegalstateexception-can-not-perform-this-action-after-onsaveinstancestate-wit
         */
        fun allowStateLoss(): Builder {
            allowStateLoss = true
            return this
        }

        /**
         * Call this method when the fragment should not replace the current fragment but should be
         * added on top of the current fragment.
         */
        fun addFragment(): Builder {
            addFragment = true
            return this
        }

        /**
         * Call this method when the fragment transaction should be added to the back stack.
         */
        fun addToBackstack(): Builder {
            addToBackstack = true
            return this
        }

        /**
         * Executing the FragmentTransaction shows a default fade animation.
         * Call this method to prevent the animation.
         */
        fun withoutAnimation(): Builder {
            animate = false
            return this
        }

        /**
         * adding the Shared Element with opening a new fragment
         * @param sharedElement the view that should move
         * @param sharedElementName the view Name
         */
        fun addSharedElement(sharedElement: View?, sharedElementName: String?): Builder {
            this.sharedElementName = sharedElementName
            this.sharedElement = sharedElement
            addSharedElement = true
            return this
        }

        /**
         * Execute the previously build FragmentTransaction.
         */
        fun execute() {
            val transaction = fragmentManager?.beginTransaction()

            if (animate) {
              //  transaction?.setTransition(TRANSIT_FRAGMENT_OPEN)
                transaction?.setCustomAnimations(
                    R.anim.fade_in,
                    R.anim.fade_out
                )
            }

            if (addFragment) {
                transaction?.add(container, fragment)
            } else {
                transaction?.replace(container, fragment)
            }

            if (addToBackstack) {
                transaction?.addToBackStack(null)
            }
            if (addSharedElement) {
                sharedElement?.let { sharedElement ->
                    sharedElementName?.let { name ->
                        transaction?.addSharedElement(sharedElement, name)
                    }
                }
            }

            if (allowStateLoss) {
                transaction?.commitAllowingStateLoss()
            } else {
                transaction?.commit()
            }
        }


    }

}