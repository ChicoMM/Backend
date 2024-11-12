package br.pucpr.authserver.reports

import br.pucpr.authserver.appointments.AppointmentService
import br.pucpr.authserver.appointments.requests.CreateAppointmentRequest
import br.pucpr.authserver.appointments.requests.UpdateAppointmentRequest
import br.pucpr.authserver.appointments.responses.AppointmentResponse
import br.pucpr.authserver.reports.requests.CreateReportRequest
import br.pucpr.authserver.reports.requests.UpdateReportRequest
import br.pucpr.authserver.reports.responses.ReportResponse
import br.pucpr.authserver.users.SortDir
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

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

        @GetMapping
        fun list(
            @RequestParam(required = false) sortDir: String?
        ) = SortDir.getByName(sortDir)
            ?.let { service.list(it) }
            ?.map { ReportResponse(it) }
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.status(HttpStatus.BAD_REQUEST).build()

        @DeleteMapping("/{id}")
        fun delete(@PathVariable id: Long): ResponseEntity<Void> =
            service.delete(id)
                ?.let { ResponseEntity.ok().build() }
                ?: ResponseEntity.notFound().build()

        @PatchMapping("/{id}")
        fun update(
            @PathVariable id: Long,
            @RequestBody @Valid updateReportRequest: UpdateReportRequest
        ): ResponseEntity<ReportResponse> =
            service.update(id, updateReportRequest.possibleIllness!!, updateReportRequest.recommendations!!)
                ?.let { ReportResponse(it) }
                ?.let { ResponseEntity.ok(it) }
                ?: ResponseEntity.noContent().build()
}