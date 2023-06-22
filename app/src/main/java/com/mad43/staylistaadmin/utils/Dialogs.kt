package com.mad43.staylistaadmin.utils

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog

class Dialogs {

    companion object {
        fun showInformationDialog(
            context: Context?,
            message: String?,
        ) {
            val builder = AlertDialog.Builder(
                context!!
            )
            builder.setMessage(message)
            val dialog = builder.create()
            dialog.show()
        }

        fun showConfirmationDialog(
            context: Context?,
            message: String?,
            onYes: Runnable,
            onNo: Runnable
        ) {
            val builder = AlertDialog.Builder(
                context!!
            )
            builder.setMessage(message)
                .setPositiveButton("Yes") { _: DialogInterface?, _: Int -> onYes.run() }
                .setNegativeButton("No") { _: DialogInterface?, _: Int -> onNo.run() }
            val dialog = builder.create()
            dialog.show()
        }
    }
}