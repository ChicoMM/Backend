package br.pucpr.authserver.appointments

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import java.sql.Date
import java.time.LocalDate

@Entity
@Table(name="tbAppointments")
class Appointment(
    @Id @GeneratedValue
    var id: Long? = null,

    @NotNull
    var doctor: String,

    @NotNull
    var date: LocalDate,
)