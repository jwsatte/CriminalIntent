package android.bignerdranch.criminalintent

import java.util.*

class Crime (val id: UUID = UUID.randomUUID(),
             var title: String = "",
             var date: Date = Date(),
             var isSolved: Boolean = false,
             var requiresPolice: Boolean = false)