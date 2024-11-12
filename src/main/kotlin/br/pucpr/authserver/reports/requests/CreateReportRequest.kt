package br.pucpr.authserver.reports.requests

import br.pucpr.authserver.appointments.Appointment
import br.pucpr.authserver.reports.Report
import br.pucpr.authserver.users.User
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDate

data class CreateReportRequest(
    @field:NotBlank
    val doctor: String?,

    @field:NotNull
    val date: LocalDate,

    @field:NotNull
    val symptoms: String,

    @field:NotNull
    val possibleIllness: String,

    @field:NotNull
    val recommendations: String,

    @field:NotNull
    val appointmentId: Long
) {
    fun toReport(user: User, appointment: Appointment): Report {
        return Report(
            doctor = doctor!!,
            date = date!!,
            pacient = user.name,
            symptoms = symptoms!!,
            possibleIllness = possibleIllness!!,
            recommendations = recommendations!!,
            user = user,
            appointment = appointment
        )
    }
}
