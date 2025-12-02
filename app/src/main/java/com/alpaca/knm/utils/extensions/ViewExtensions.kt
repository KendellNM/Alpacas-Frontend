package com.alpaca.knm.utils.extensions

import android.view.View

/**
 * Extensiones para Views
 */

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}
