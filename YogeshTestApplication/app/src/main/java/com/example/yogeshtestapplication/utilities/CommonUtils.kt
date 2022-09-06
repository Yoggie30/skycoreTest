package com.example.yogeshtestapplication.utilities

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import com.example.yogeshtestapplication.R
import com.google.android.material.snackbar.Snackbar

object CommonUtils {

    fun toast (context: Context, view: View, msg : String) {
        val snackBar = Snackbar.make(context, view, msg, Snackbar.LENGTH_SHORT)
            .setAction("Action", null)
        snackBar.view.setBackgroundResource(R.drawable.background_red)
        val params = snackBar.view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.CENTER_HORIZONTAL
        params.setMargins(0,100,0,0)
        snackBar.view.layoutParams = params
        snackBar.show()
    }

}