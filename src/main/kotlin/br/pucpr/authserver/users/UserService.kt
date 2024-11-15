package br.pucpr.authserver.users

import br.pucpr.authserver.errors.BadRequestException
import br.pucpr.authserver.errors.NotFoundException
import br.pucpr.authserver.roles.RoleRepository
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class UserService(
    val repository: UserRepository,
    val roleRepository: RoleRepository
) {
    val role = "ADMIN"
    fun insert(user: User): User {
        if (user.id != null)
            throw IllegalArgumentException("Usuário já inserido!")
        
        if (repository.findByEmail(user.email) != null)
            throw BadRequestException("Usuário com email ${user.email} já existe!")
        log.info("Usuário cadastrado. id={} name={}", user.id, user.name)
        return repository.save(user)
    }

    fun list(sortDir: SortDir, role: String?): List<User> {
        if (role != null) {
            return repository.findByRole(role)
        } else {
            return when (sortDir) {
                SortDir.ASC -> repository.findAll()
                SortDir.DESC -> repository.findAll(Sort.by("id").reverse())
            }
        }
    }

    fun findByIdOrNull(id: Long) = repository.findByIdOrNull(id)

    fun delete(id: Long) {

        if (role != "ADMIN" ) {
            log.warn("Usuário sem permissão tentou deletar uma consulta.")
            throw IllegalArgumentException("Apenas usuários com a função ADMIN podem deletar consultas")
        }

        if (repository.existsById(id)) {
            repository.deleteById(id)
            log.info("Consulta deletada com sucesso. id={}", id)
        } else {
            log.warn("Tentativa de exclusão falhou, consulta não encontrada. id={}", id)
        }
    }

    fun update(id: Long, name: String): User? {
        val user = repository.findByIdOrNull(id)
            ?: throw NotFoundException("Usuário ${id} não encontrado!")
        if (user.name == name)
            return null
        user.name = name
        return repository.save(user)
    }

    fun addRole(id: Long, roleName: String): Boolean {
        val user = repository.findByIdOrNull(id)
            ?: throw NotFoundException("Usuário não encontrado")

        if (user.roles.any { it.name == roleName }) return false

        val role = roleRepository.findByName(roleName)
            ?: throw BadRequestException("Invalid role name!")
        user.roles.add(role)
        repository.save(user)
        return true
    }

    companion object {
        val log = LoggerFactory.getLogger(UserService::class.java)
    }
}
