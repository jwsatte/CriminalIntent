package android.bignerdranch.criminalintent

import android.content.Context
import java.util.*

class CrimeLab private constructor(context: Context) {

    private val crimes = mutableListOf<Crime>()

    fun addCrime(crime: Crime) {
        crimes.add(crime)
    }

    fun deleteCrime(crime: Crime){
        val index = crimes.indexOf(crime)
        if (index >= 0) crimes.removeAt(index)
    }

    fun getCrimes(): List<Crime> {
        return crimes
    }

    fun getCrime(id: UUID): Crime? {
        return crimes.find {
            it.id == id
        }
    }

    companion object {

        private var INSTANCE: CrimeLab? = null

        fun initialize(context: Context){
            INSTANCE = CrimeLab(context)
        }

        fun get(): CrimeLab {
            return INSTANCE ?:
                    throw IllegalStateException("CrimeLab mus be initialized")
        }
    }
}