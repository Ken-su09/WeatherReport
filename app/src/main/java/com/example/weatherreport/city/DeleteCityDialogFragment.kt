package com.example.weatherreport.city

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.DialogFragment
import com.example.weatherreport.R

class DeleteCityDialogFragment : DialogFragment() {

    interface DeleteCityDialogListener {
        fun onDialogPositiveClick()
        fun onDialogNegativeClick()
    }

    companion object {
        private const val EXTRA_CITY_NAME = "com.example.weatherreport.extras.EXTRA_CITY_NAME"

        fun newInstance(cityName: String): DeleteCityDialogFragment {
            val fragment = DeleteCityDialogFragment()
            fragment.arguments = Bundle().apply {
                putString(
                    EXTRA_CITY_NAME, cityName
                )
            }

            return fragment
        }
    }

    var listener: DeleteCityDialogListener? = null
    private lateinit var cityName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cityName = arguments!!.getString(EXTRA_CITY_NAME)!!
    }

    @SuppressLint("StringFormatInvalid")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context)

        builder.setTitle(getString(R.string.alert_dialog_delete_city_title, cityName))
            .setPositiveButton(getString(R.string.alert_dialog_delete_city_positive_button)) { _, _ ->
                listener!!.onDialogPositiveClick()
            }
            .setNegativeButton(getString(R.string.alert_dialog_negative_button_text)) { dialog, _ ->
                dialog.cancel()
                listener!!.onDialogNegativeClick()
            }

        return builder.create()
    }
}