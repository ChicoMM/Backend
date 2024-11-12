package br.pucpr.authserver.reports

import br.pucpr.authserver.appointments.Appointment
import br.pucpr.authserver.errors.NotFoundException
import br.pucpr.authserver.reports.Report
import br.pucpr.authserver.users.SortDir
import br.pucpr.authserver.users.User
import br.pucpr.authserver.users.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class ReportService(
    val repository: ReportRepository,
    val userRepository: UserRepository // Repositório para buscar o usuário
) {
    private val log = LoggerFactory.getLogger(ReportService::class.java)

        fun insert(report: Report): Report {
            if (report.id != null) {
                log.warn("ID já existente, não foi salvo o processo. id={}", report.id)
                throw IllegalArgumentException("Consulta já inserido!")
            }

            val savedReport = repository.save(report)
            log.info("Report realizado. id={} doctor={} patient={}", savedReport.id, savedReport.doctor, savedReport.pacient)
            return savedReport
        }



    fun list(sortDir: SortDir) =
        if (sortDir == SortDir.ASC)
            repository.findAll()
        else
            repository.findAll(Sort.by("id").reverse())

    fun delete(id: Long) = repository.deleteById(id)

    fun update(id: Long, possibleIllness: String, recommendations: String): Report? {
        val report = repository.findByIdOrNull(id)
            ?: throw NotFoundException("Consulta ${id} não encontrada!")
        if (report.possibleIllness != possibleIllness)
            report.possibleIllness = possibleIllness
        if (report.recommendations != recommendations)
            report.recommendations = recommendations

        return repository.save(report)
    }

    companion object {
        private val log = LoggerFactory.getLogger(ReportService::class.java)
    }
}
