package br.pucpr.authserver.reports.responses

import br.pucpr.authserver.appointments.Appointment
import br.pucpr.authserver.reports.Report
import br.pucpr.authserver.users.User
import jakarta.validation.constraints.NotNull
import java.time.LocalDate

data class ReportResponse(
    val id: Long,
    val doctor: String,
    var pacient: String,
    val date: LocalDate,
    val symptoms: String,
    var possibleIllness: String,
    var recommendations: String,
) {
    constructor(report: Report): this(
        id = report.id!!,
        doctor = report.doctor!!,
        pacient = report.pacient!!,
        date = report.date!!,
        symptoms = report.symptoms!!,
        possibleIllness = report.possibleIllness!!,
        recommendations = report.recommendations!!,

    )
}
