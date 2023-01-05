package com.unbiasedshelf.agepredictor.ui.common

import android.content.Context
import android.widget.Toast
import com.unbiasedshelf.agepredictor.data.repository.Status

fun <T> Status<T>.showToast(context: Context) {
    messageId?.let {
        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
    }
}