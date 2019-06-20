package android.bignerdranch.criminalintent

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import java.util.*

private const val EXTRA_CRIME_ID = "com.bignerdranch.android.criminalintent.crime_id"

class CrimePagerActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var crimes: List<Crime>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crime_pager)

        viewPager = findViewById(R.id.crime_view_pager)

        val crimeId = intent.getSerializableExtra(EXTRA_CRIME_ID)

        viewPager.apply {
            crimes = CrimeLab.get().getCrimes()
            adapter = object : FragmentStatePagerAdapter(supportFragmentManager) {
                override fun getItem(position: Int): Fragment {
                    val crime = crimes[position]
                    return CrimeFragment.newInstance(crime.id) {
                        finish()
                    }
                }

                override fun getCount() = crimes.size
            }

            for (i in crimes.indices){
                if (crimes[i].id == crimeId) {
                    currentItem = i
                    break
                }
            }
        }

    }

    companion object {
        fun newIntent(context: Context, crimeId: UUID): Intent {
            return Intent(context, CrimePagerActivity::class.java).apply {
                putExtra(EXTRA_CRIME_ID, crimeId)
            }
        }
    }
}