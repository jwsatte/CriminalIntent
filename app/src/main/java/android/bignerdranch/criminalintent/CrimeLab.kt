package android.bignerdranch.criminalintent

import android.bignerdranch.criminalintent.database.CrimeBaseHelper
import android.bignerdranch.criminalintent.database.CrimeCursorWrapper
import android.bignerdranch.criminalintent.database.CrimeTable
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import java.util.*

class CrimeLab private constructor(context: Context) {

    private val database = CrimeBaseHelper(context).writableDatabase

    fun addCrime(crime: Crime) {
        val values = getContentValues(crime)
        database.insert(CrimeTable.NAME, null, values)
    }

    fun updateCrime(crime: Crime) {
        val uuidString = crime.id.toString()
        val values = getContentValues(crime)

        database.update(CrimeTable.NAME,
            values,
            "${CrimeTable.Cols.UUID} = ?",
            arrayOf(uuidString))
    }

    private fun queryCrimes(whereClause: String?, whereArgs: Array<String>?): CrimeCursorWrapper {
        val cursor = database.query(
            CrimeTable.NAME,
            null, // null selects all columns
            whereClause,
            whereArgs,
            null,
            null,
            null)

        return CrimeCursorWrapper(cursor)
    }

    fun deleteCrime(crime: Crime){
        val uuidString = crime.id.toString()
        database.delete(CrimeTable.NAME, "${CrimeTable.Cols.UUID} = ?", arrayOf(uuidString))
    }

    fun getCrimes(): List<Crime> {

        val crimes = mutableListOf<Crime>()
        val cursor = queryCrimes(null, null)
        cursor.use {
            it.moveToFirst()
            while (!it.isAfterLast) {
                crimes.add(it.getCrime())
                it.moveToNext()
            }
        }

        return crimes
    }

    fun getCrime(id: UUID): Crime? {
        val cursor = queryCrimes("${CrimeTable.Cols.UUID} = ?", arrayOf(id.toString()))

        cursor.use {
            if (it.count == 0) {
                return null
            }

            cursor.moveToFirst()
            return cursor.getCrime()
        }
    }

    private fun getContentValues(crime: Crime): ContentValues {
        val solved = if (crime.isSolved) {
            1
        } else {
            0
        }

        return ContentValues().apply {
            put(CrimeTable.Cols.UUID, crime.id.toString())
            put(CrimeTable.Cols.TITLE, crime.title)
            put(CrimeTable.Cols.DATE, crime.date.time)
            put(CrimeTable.Cols.SOLVED, solved)
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