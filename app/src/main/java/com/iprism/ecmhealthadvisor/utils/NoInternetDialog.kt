package com.iprism.ecmhealthadvisor.utils


import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import com.iprism.ecmhealthadvisor.R
import kotlinx.coroutines.*

object NoInternetDialog {

    private var dialog: Dialog? = null

    fun show(context: Context) {
        if (dialog?.isShowing == true) return
        if (context !is Activity) return // ✅ Important: only show when we have an Activity context

        dialog = Dialog(context).apply {
            setContentView(LayoutInflater.from(context).inflate(R.layout.no_internet_dialog, null))
            setCancelable(false)
            window?.setBackgroundDrawableResource(R.color.white)

            val retryButton: Button = findViewById(R.id.btn_retry)
            retryButton.setOnClickListener {
                GlobalScope.launch(Dispatchers.Main) {
                    if (NetworkUtil.isNetworkAvailable(context) && NetworkUtil.hasInternetAccess()) {
                        dismiss()
                    } else {
                        Toast.makeText(context, "Still no internet", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            show()
        }
    }

    fun dismiss() {
        dialog?.dismiss()
        dialog = null
    }

}
