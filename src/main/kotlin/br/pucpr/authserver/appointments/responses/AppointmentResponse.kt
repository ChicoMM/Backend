package br.pucpr.authserver.appointments.responses

import br.pucpr.authserver.appointments.Appointment
import java.sql.Date
import java.time.LocalDate


data class AppointmentResponse(
    val id: Long,
    val doctor: String,
    var date: LocalDate,
) {
    constructor(appointment: Appointment): this(
        id = appointment.id!!,
        doctor = appointment.doctor!!,
        date = appointment.date!!
    )
}
