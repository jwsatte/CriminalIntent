package android.bignerdranch.criminalintent

import androidx.fragment.app.Fragment

class CrimeActivity : SingleFragmenteActivity() {

    override fun createFragment(): Fragment {
        return CrimeFragment()
    }
}
