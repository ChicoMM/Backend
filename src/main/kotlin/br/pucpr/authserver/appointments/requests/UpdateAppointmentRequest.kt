package br.pucpr.authserver.appointments.requests

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDate

data class UpdateAppointmentRequest(
    @field:NotNull
    val date: LocalDate?
)
