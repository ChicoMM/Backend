package br.pucpr.authserver.reports

import br.pucpr.authserver.reports.Report
import org.springframework.stereotype.Service

@Service
class ReportService (
    val repository: ReportRepository
    ) {
        fun insert(report: Report): Report {
            if (report.id != null)
                throw IllegalArgumentException("Consulta já inserido!")

            return repository.save(report)
        }
}