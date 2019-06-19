package android.bignerdranch.criminalintent

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.DatePicker
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import java.util.*

private const val ARG_DATE = "date"

class DatePickerFragment : DialogFragment() {

    private lateinit var datePicker: DatePicker

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val date = arguments?.getSerializable(ARG_DATE) as Date
        val calendar = Calendar.getInstance()
        calendar.time = date
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val view = LayoutInflater.from(context)
            .inflate(R.layout.dialog_date, null)

        datePicker = view.findViewById(R.id.dialog_date_picker)
        datePicker.init(year, month, day, null)

        return AlertDialog.Builder(requireContext())
            .setView(view)
            .setTitle(R.string.date_picker_title)
            .setPositiveButton(android.R.string.ok, null)
            .create()
    }

    companion object {
        fun newInstance(date: Date): DatePickerFragment {
            val args = Bundle().apply {
                putSerializable(ARG_DATE, date)
            }

            return DatePickerFragment().apply {
                arguments = args
            }
        }
    }
}