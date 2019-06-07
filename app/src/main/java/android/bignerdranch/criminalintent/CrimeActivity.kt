package android.bignerdranch.criminalintent

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import java.util.*

private const val EXTRA_CRIME_ID = "com.bignerdranch.android.criminalintent.crime_id"

class CrimeActivity : SingleFragmenteActivity() {

    override fun createFragment(): Fragment {
        val crimeId = intent.getSerializableExtra(EXTRA_CRIME_ID) as UUID
        return CrimeFragment.newInstance(crimeId)
    }

    companion object {
        fun newIntent(context: Context, crimeId: UUID): Intent {
            return Intent(context, CrimeActivity::class.java).apply {
                putExtra(EXTRA_CRIME_ID, crimeId)
            }
        }
    }
}
