package br.pucpr.authserver.reports

import br.pucpr.authserver.appointments.Appointment
import org.springframework.data.jpa.repository.JpaRepository

interface ReportRepository: JpaRepository<Report, Long> {

}