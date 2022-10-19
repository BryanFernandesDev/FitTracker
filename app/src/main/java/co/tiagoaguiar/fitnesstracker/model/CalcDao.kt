package co.tiagoaguiar.fitnesstracker.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CalcDao {

    @Insert
    fun insert(calc: Calc)

    @Query("select * from Calc where type = :type")
    fun getRegisterByType(type: String): List<Calc>

}