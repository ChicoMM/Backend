package br.pucpr.authserver.reports

import br.pucpr.authserver.appointments.AppointmentRepository
import br.pucpr.authserver.appointments.AppointmentService
import br.pucpr.authserver.appointments.requests.CreateAppointmentRequest
import br.pucpr.authserver.appointments.requests.UpdateAppointmentRequest
import br.pucpr.authserver.appointments.responses.AppointmentResponse
import br.pucpr.authserver.reports.requests.CreateReportRequest
import br.pucpr.authserver.reports.requests.UpdateReportRequest
import br.pucpr.authserver.reports.responses.ReportResponse
import br.pucpr.authserver.reports.ReportService
import br.pucpr.authserver.users.SortDir
import br.pucpr.authserver.users.UserRepository
import br.pucpr.authserver.users.UserService
import jakarta.validation.Valid
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

class NotFoundException(message: String) : RuntimeException(message)

@RestController
@RequestMapping("/reports")
class ReportController(
    val service: ReportService,
    val userRepository: UserRepository,
    val appointmentRepository: AppointmentRepository
) {

    @PostMapping
    fun insert(@RequestBody reportRequest: CreateReportRequest, @RequestParam userId: Long): ResponseEntity<Any> {
        // Buscar o usuário pelo ID
        val user = userRepository.findByIdOrNull(userId)
            ?: throw NotFoundException("Usuário com ID $userId não encontrado.")

        // Buscar o agendamento pelo ID
        val appointment = appointmentRepository.findByIdOrNull(reportRequest.appointmentId)
            ?: throw NotFoundException("Agendamento com ID ${reportRequest.appointmentId} não encontrado.")

        // Converter o CreateReportRequest para um objeto Report e associar o usuário e o agendamento
        val report = reportRequest.toReport(user, appointment)

        // Chamar o serviço para salvar o relatório
        val savedReport = service.insert(report)

        // Retornar a resposta com o relatório salvo e a data do agendamento
        val reportResponse = mapOf(
            "id" to savedReport.id,
            "doctor" to savedReport.doctor,
            "date" to savedReport.date,
            "pacient" to savedReport.pacient,
            "symptoms" to savedReport.symptoms,
            "possibleIllness" to savedReport.possibleIllness,
            "recommendations" to savedReport.recommendations,
            "appointmentDate" to savedReport.appointment?.date
        )

        return ResponseEntity.status(HttpStatus.CREATED).body(reportResponse)
    }

    @GetMapping
    fun list(@RequestParam(required = false) sortDir: String?) = SortDir.getByName(sortDir)
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
