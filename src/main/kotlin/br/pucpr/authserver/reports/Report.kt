package br.pucpr.authserver.reports

import br.pucpr.authserver.appointments.Appointment
import br.pucpr.authserver.users.User
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import org.springframework.boot.autoconfigure.security.SecurityProperties
import java.time.LocalDate

@Entity
@Table(name = "tbReports")
class Report(
    @Id @GeneratedValue
    var id: Long? = null,

    @NotNull
    var doctor: String,

    @NotNull
    var date: LocalDate,

    @NotNull
    var pacient: String, // Nome do paciente será o nome do usuário

    @NotNull
    var symptoms: String,

    @NotNull
    var possibleIllness: String,

    @NotNull
    var recommendations: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    var user: User? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointmentId", nullable = false)
    var appointment: Appointment? = null // Relacionamento com a consulta
)

