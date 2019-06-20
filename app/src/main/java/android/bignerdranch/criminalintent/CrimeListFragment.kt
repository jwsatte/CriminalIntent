package android.bignerdranch.criminalintent

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class CrimeListFragment : Fragment() {

    private lateinit var crimeRecyclerView: RecyclerView
    private var adapter: CrimeAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)

        crimeRecyclerView = view.findViewById(R.id.crime_recycler_view) as RecyclerView
        crimeRecyclerView.layoutManager = LinearLayoutManager(context)

        updateUI()

        return view
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.fragment_crime_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.new_crime -> {
                val crime = Crime()
                CrimeLab.get().addCrime(crime)
                val intent = CrimePagerActivity.newIntent(requireContext(), crime.id)
                startActivity(intent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private inner class CrimeHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        private lateinit var crime: Crime

        private val titleTextView: TextView
        private val dateTextView: TextView
        private val solvedImageView: ImageView

        init{
            itemView.setOnClickListener(this)
            titleTextView = itemView.findViewById(R.id.crime_title)
            dateTextView = itemView.findViewById(R.id.crime_date)
            solvedImageView = itemView.findViewById(R.id.crime_solved)
        }

        fun bind(crime: Crime){
            this.crime = crime
            titleTextView.text = this.crime.title

            val outputPattern = "E, MMM, d, yyyy"
            val outputFormat = SimpleDateFormat(outputPattern, Locale.US)

            dateTextView.text = outputFormat.format(this.crime.date)
            solvedImageView.visibility = if (crime.isSolved) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        override fun onClick(v: View?) {

            val intent = CrimePagerActivity.newIntent(requireContext(), crime.id)
            startActivity(intent)
        }
    }

    private inner class CrimeAdapter(var crimes: List<Crime>) : RecyclerView.Adapter<CrimeHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {
            val layoutInflater = LayoutInflater.from(context)
            val view = layoutInflater.inflate(R.layout.list_item_crime, parent, false)
            return CrimeHolder(view)
        }

        override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
            val crime = crimes[position]
            holder.bind(crime)
        }

        override fun getItemCount() = crimes.size
    }

    private fun updateUI() {
        val crimeLab = CrimeLab.get()
        val crimes = crimeLab.getCrimes()

        adapter?.let {
            it.crimes = crimes
            it.notifyDataSetChanged()
        } ?: run {
            adapter = CrimeAdapter(crimes)
            crimeRecyclerView.adapter = adapter
        }
    }
}