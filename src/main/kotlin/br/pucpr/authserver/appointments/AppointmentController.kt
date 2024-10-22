package br.pucpr.authserver.appointments

import br.pucpr.authserver.appointments.requests.CreateAppointmentRequest
import br.pucpr.authserver.appointments.responses.AppointmentResponse
import br.pucpr.authserver.users.SortDir
import br.pucpr.authserver.users.requests.UpdateUserRequest
import br.pucpr.authserver.users.responses.UserResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import br.pucpr.authserver.appointments.requests.UpdateAppointmentRequest


@RestController
@RequestMapping("/appointments")
class AppointmentController(
    val service: AppointmentService,
) {
    @PostMapping
    fun insert(@RequestBody @Valid appointment: CreateAppointmentRequest) =
        service.insert(appointment.toAppointment())
            .let { AppointmentResponse(it) }
            .let { ResponseEntity.status(HttpStatus.CREATED).body(it) }

    @GetMapping
    fun list(
        @RequestParam(required = false) sortDir: String?
    ) = SortDir.getByName(sortDir)
        ?.let { service.list(it) }
        ?.map { AppointmentResponse(it) }
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
        @RequestBody @Valid updateAppointmentRequest: UpdateAppointmentRequest
    ): ResponseEntity<AppointmentResponse> =
        service.update(id, updateAppointmentRequest.date!!)
            ?.let { AppointmentResponse(it) }
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.noContent().build()
}
