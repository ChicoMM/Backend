package br.pucpr.authserver.reports.requests

import br.pucpr.authserver.appointments.Appointment
import br.pucpr.authserver.reports.Report
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDate

data class CreateReportRequest(
    @field:NotBlank
    val doctor: String?,

    @field:NotNull
    val date: LocalDate,

    @field:NotNull
    val pacient: String,

    @field:NotNull
    val symptoms: String,

    @field:NotNull
    val possibleIllness: String,

    @field:NotNull
    val recommendations: String,

    ) {
    fun toReport() = Report(
        doctor = doctor!!,
        date = date!!,
        pacient = pacient!!,
        symptoms = symptoms!!,
        possibleIllness = possibleIllness!!,
        recommendations = recommendations!!
    )
}