package co.tiagoaguiar.fitnesstracker.model

import androidx.room.TypeConverter
import java.util.*

object DateConverter{

    //usada pra quando buscar um objeto
    @TypeConverter
    fun toDate(dateLong: Long?) : Date?{

        return if(dateLong != null) Date(dateLong) else null
    }

    //usada quando gravar no banco
    @TypeConverter
    fun fromDate(date: Date?): Long? {
    return date?.time
    }
}