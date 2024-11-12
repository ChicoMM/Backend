package br.pucpr.authserver.appointments

import br.pucpr.authserver.errors.NotFoundException
import br.pucpr.authserver.roles.RoleRepository
import br.pucpr.authserver.users.SortDir
import br.pucpr.authserver.users.User
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDate
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Service
class AppointmentService(
    val repository: AppointmentRepository,

) {
    val role = "ADMIN"
    companion object {
        private val log = LoggerFactory.getLogger(AppointmentService::class.java)
    }

    fun insert(appointment: Appointment): Appointment {
        if (appointment.id != null) {
            log.warn("Tentativa de inserção de consulta já existente. id={}", appointment.id)
            throw IllegalArgumentException("Consulta já inserida!")
        }

        val savedAppointment = repository.save(appointment)
        log.info("Consulta inserida com sucesso. id={} data={}", savedAppointment.id, savedAppointment.date)
        return savedAppointment
    }

    fun list(sortDir: SortDir): List<Appointment> {
        log.info("Listando consultas na ordem: {}", sortDir)
        return if (sortDir == SortDir.ASC) {
            repository.findAll()
        } else {
            repository.findAll(Sort.by("id").reverse())
        }
    }

    fun delete(id: Long) {

        // Verifica se o usuário tem a função ADMIN
        if (role != "ADMIN" ) {
            log.warn("Usuário sem permissão tentou deletar uma consulta.")
            throw IllegalArgumentException("Apenas usuários com a função ADMIN podem deletar consultas")
        }

        // Realiza a exclusão caso o usuário tenha a função ADMIN
        if (repository.existsById(id)) {
            repository.deleteById(id)
            log.info("Consulta deletada com sucesso. id={}", id)
        } else {
            log.warn("Tentativa de exclusão falhou, consulta não encontrada. id={}", id)
        }
    }

    fun update(id: Long, date: LocalDate): Appointment? {
        val appointment = repository.findByIdOrNull(id)
            ?: run {
                log.warn("Consulta não encontrada para atualização. id={}", id)
                throw NotFoundException("Consulta ${id} não encontrada!")
            }

        if (appointment.date == date) {
            log.info("Data fornecida é a mesma da consulta existente. id={} data={}", id, date)
            return null
        }

        appointment.date = date
        val updatedAppointment = repository.save(appointment)
        log.info("Consulta atualizada com sucesso. id={} novaData={}", updatedAppointment.id, updatedAppointment.date)
        return updatedAppointment
    }
}