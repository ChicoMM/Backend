package br.pucpr.authserver.reports

import br.pucpr.authserver.appointments.AppointmentService
import br.pucpr.authserver.appointments.requests.CreateAppointmentRequest
import br.pucpr.authserver.appointments.responses.AppointmentResponse
import br.pucpr.authserver.reports.requests.CreateReportRequest
import br.pucpr.authserver.reports.responses.ReportResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/reports")
class ReportController (
    val service: ReportService,
){
        @PostMapping
        fun insert(@RequestBody @Valid report: CreateReportRequest) =
            service.insert(report.toReport())
                .let { ReportResponse(it) }
                .let { ResponseEntity.status(HttpStatus.CREATED).body(it) }
}