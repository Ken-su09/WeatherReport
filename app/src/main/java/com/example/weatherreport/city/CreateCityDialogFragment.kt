package com.example.weatherreport.city

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.DialogFragment
import com.example.weatherreport.R

class CreateCityDialogFragment : DialogFragment() {

    interface CreateCityDialogListener {
        fun onDialogPositiveClick(cityName: String)
        fun onDialogNegativeClick()
    }

    var listener: CreateCityDialogListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context)

        val input = AppCompatEditText(requireContext())
        with(input) {
            inputType = InputType.TYPE_CLASS_TEXT
            hint = context.getString(R.string.alert_dialog_edit_text_hint)
        }

        builder.setTitle(getString(R.string.alert_dialog_create_city_title))
            .setView(input)
            .setPositiveButton(getString(R.string.alert_dialog_create_city_positive_button_text)) { _, _ ->
                listener!!.onDialogPositiveClick(input.text.toString())
            }
            .setNegativeButton(getString(R.string.alert_dialog_negative_button_text)) { dialog, _ ->
                dialog.cancel()
                listener!!.onDialogNegativeClick()
            }

        return builder.create()
    }
}