package br.pucpr.authserver.reports.requests

import jakarta.validation.constraints.NotNull
import java.time.LocalDate

class UpdateReportRequest (
    @field:NotNull
    val possibleIllness: String,

    @field:NotNull
    val recommendations: String
)