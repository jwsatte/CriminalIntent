package android.bignerdranch.criminalintent

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_crime.view.*
import java.util.*

private const val ARG_CRIME_ID = "crime_id"
private const val DIALOG_DATE = "DialogDate"
private const val REQUEST_DATE = 0

class CrimeFragment(private val onCrimeDeleted: (()->Unit)? = null) : Fragment() {

    private lateinit var crime: Crime
    private lateinit var titleField: EditText
    private lateinit var dateButton: Button
    private lateinit var solvedCheckBox: CheckBox
    private lateinit var deleteButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val crimeId = arguments?.getSerializable(ARG_CRIME_ID) as UUID
        crime = CrimeLab.get().getCrime(crimeId) ?: Crime()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        //Get the inflated view with the fragment
        val view = inflater.inflate(R.layout.fragment_crime, container, false)

        //Get the controls in the fragment
        titleField = view.findViewById(R.id.crime_title) as EditText
        dateButton = view.findViewById(R.id.crime_date) as Button
        solvedCheckBox = view.findViewById(R.id.crime_solved) as CheckBox
        deleteButton = view.findViewById(R.id.crime_delete) as Button

        //Anonymous mehthod to observe text changes
        val titleWatcher = object: TextWatcher {
            override fun beforeTextChanged(sequence: CharSequence?,
                                           start: Int,
                                           count: Int,
                                           after: Int) {
                // This space intentionally left blank
            }
            override fun onTextChanged(sequence: CharSequence?,
                                       start: Int,
                                       before: Int,
                                       count: Int) {
                crime.title = sequence.toString()
            }
            override fun afterTextChanged(sequence: Editable?) {
                // This one too
            }
        }
        //Wire up the text lisntener to the crime title text field
        titleField.apply {
            setText(crime.title)
            addTextChangedListener(titleWatcher)
        }

        //Update the button text and OnClickListener
        updateDate()
        dateButton.setOnClickListener {
            DatePickerFragment.newInstance(crime.date).apply {
                setTargetFragment(this@CrimeFragment, REQUEST_DATE)
                val fragmentManager = this@CrimeFragment.fragmentManager
                show(fragmentManager, DIALOG_DATE)
            }
        }

        //Wire up the checkbox via lambda
        solvedCheckBox.apply {
            isChecked = crime.isSolved
            setOnCheckedChangeListener { _, isChecked ->
                crime.isSolved = isChecked
            }
        }

        deleteButton.setOnClickListener {
            CrimeLab.get().deleteCrime(crime)
            onCrimeDeleted?.invoke() //Calling the parent .finish() method
        }
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when {
            resultCode != Activity.RESULT_OK -> return
            requestCode == REQUEST_DATE && data != null -> {
                val date = data.getSerializableExtra(DatePickerFragment.EXTRA_DATE) as Date
                crime.date = date
                updateDate()
            }
        }
    }

    private fun updateDate() {
        dateButton.text = crime.date.toString()
    }

    companion object {
        fun newInstance(crimeId: UUID, onCrimeDeleted: () -> Unit): CrimeFragment {
            val args = Bundle().apply {
                putSerializable(ARG_CRIME_ID, crimeId)
            }
            return CrimeFragment(onCrimeDeleted).apply {
                arguments = args
            }
        }
    }
}