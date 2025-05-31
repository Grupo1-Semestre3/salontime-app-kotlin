package sptech.salonTime.dto

import java.time.LocalTime

interface HorariosOcupadosDto {
    fun getInicio(): LocalTime
    fun getFim(): LocalTime
    fun getCapacidade(): Int
}