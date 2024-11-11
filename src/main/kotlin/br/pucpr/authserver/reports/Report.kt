package br.pucpr.authserver.reports

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import java.time.LocalDate

@Entity
@Table(name="tbReports")
class Report(
    @Id @GeneratedValue
    var id: Long? = null,

    @NotNull
    var doctor: String,

    @NotNull
    var pacient: String,

    @NotNull
    var date: LocalDate,

    @NotNull
    var symptoms: String,

    @NotNull
    var possibleIllness: String,

    @NotNull
    var recommendations: String,
    )
