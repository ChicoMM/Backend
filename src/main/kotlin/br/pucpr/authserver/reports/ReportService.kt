package br.pucpr.authserver.reports

import br.pucpr.authserver.reports.Report
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ReportService (
    val repository: ReportRepository
    ) {
        fun insert(report: Report): Report {
            if (report.id != null){
                log.warn("ID já existente, não foi salvo o processo. id={}", report.id)
                throw IllegalArgumentException("Consulta já inserido!")}

            val savedReport = repository.save(report)
            log.info("Report realizado. id={} doctor={} patient={}", savedReport.id, savedReport.doctor, savedReport.pacient)
            return repository.save(report)
        }

    companion object {
        private val log = LoggerFactory.getLogger(ReportService::class.java)
    }
}