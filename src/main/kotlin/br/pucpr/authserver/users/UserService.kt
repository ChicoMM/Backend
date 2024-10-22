package br.pucpr.authserver.users

import br.pucpr.authserver.errors.BadRequestException
import br.pucpr.authserver.errors.NotFoundException
import br.pucpr.authserver.roles.RoleRepository
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class UserService(
    val repository: UserRepository,
    val roleRepository: RoleRepository
) {
    fun insert(user: User): User {
        if (user.id != null)
            throw IllegalArgumentException("Usuário já inserido!")
        
        if (repository.findByEmail(user.email) != null)
            throw BadRequestException("Usuário com email ${user.email} já existe!")
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

    fun delete(id: Long) = repository.deleteById(id)

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
}
