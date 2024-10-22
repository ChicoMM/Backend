package br.pucpr.authserver.appointments.requests

import br.pucpr.authserver.appointments.Appointment

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.sql.Date
import java.time.LocalDate


data class CreateAppointmentRequest(
    @field:NotBlank
    val doctor: String?,

    @field:NotNull
    val date: LocalDate,

) {
    fun toAppointment() = Appointment(
        doctor = doctor!!,
        date = date!!
    )
}