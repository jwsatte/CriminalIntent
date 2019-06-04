package android.bignerdranch.criminalintent

import android.content.Context
import java.util.*

class CrimeLab private constructor(context: Context) {

    private val crimes = mutableListOf<Crime>()

    init {
        for (i in 0 until 100) {
            val crime = Crime()
            crime.title = "Crime #$i"
            crime.isSolved = i % 2 == 0
            crimes += crime
        }
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