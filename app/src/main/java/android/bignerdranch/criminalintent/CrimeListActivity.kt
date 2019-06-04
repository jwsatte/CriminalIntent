package android.bignerdranch.criminalintent

import androidx.fragment.app.Fragment

class CrimeListActivity : SingleFragmenteActivity() {
    override fun createFragment(): Fragment {
        return CrimeListFragment()
    }
}