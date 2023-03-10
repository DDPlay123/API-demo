package com.tutorial.demo.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.tutorial.demo.R
import com.tutorial.demo.utils.Constants.location_permission

/**
 * @author 麥光廷
 * @date 2023/03/10
 * 存放一些常用到的擴充函數。
 */
fun Activity.requestLocationPermission(): Boolean {
    if (!Method.requestPermission(this, *location_permission)) {
        displayShortToast(getString(R.string.toast_ask_location_permission))
        return false
    }
    return true
}

fun Activity.hideKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)
}

fun Activity.hideSoftKeyboard() {
    currentFocus?.let {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
    }
}

fun Context.displayShortToast(message: String) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()