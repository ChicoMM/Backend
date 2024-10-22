package br.pucpr.authserver.appointments

import br.pucpr.authserver.errors.NotFoundException
import br.pucpr.authserver.users.SortDir
import br.pucpr.authserver.users.User
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class AppointmentService(
    val repository: AppointmentRepository
) {
    fun insert(appointment: Appointment): Appointment {
        if (appointment.id != null)
            throw IllegalArgumentException("Consulta já inserido!")

        return repository.save(appointment)
    }
    fun list(sortDir: SortDir) =
        if (sortDir == SortDir.ASC)
            repository.findAll()
        else
            repository.findAll(Sort.by("id").reverse())

    fun delete(id: Long) = repository.deleteById(id)

    fun update(id: Long, date: LocalDate): Appointment? {
        val appointment = repository.findByIdOrNull(id)
            ?: throw NotFoundException("Consulta ${id} não encontrada!")
        if (appointment.date == date)
            return null
        appointment.date = date
        return repository.save(appointment)
    }
}