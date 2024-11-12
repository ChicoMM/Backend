package br.pucpr.authserver.users

import br.pucpr.authserver.errors.BadRequestException
import io.kotest.matchers.shouldBe
import io.mockk.checkUnnecessaryStub
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class UserServiceTest {
    private val repositoryMock = mockk<UserRepository>()
    private val service = UserService(
        repository = repositoryMock,
        roleRepository = mockk()
    )

    @AfterEach
    fun cleanUp() {
        checkUnnecessaryStub(repositoryMock)
    }

    @Test
    fun `insert throws BadRequestException if the email exists`() {
        val user = user(id = null, email = "user@email.com")
        every { repositoryMock.findByEmail(user.email) } returns user

        assertThrows<BadRequestException> {
            service.insert(user)
        }
    }

    @Test
    fun `insert saves the data if the email does not exist`() {
        val user = user(id = null, email = "user@email.com")
        val newUser = User(
            id = 1,
            name = user.name,
            email = user.email,
            password = user.password,
            roles = user.roles
        )

        every { repositoryMock.findByEmail(user.email) } returns null
        every { repositoryMock.save(user) } returns newUser

        service.insert(user) shouldBe newUser
    }

    @Test
    fun `insert throws IllegalArgumentException if the user has an id`() {
        val userWithId = user(id = 1)

        assertThrows<IllegalArgumentException> {
            service.insert(userWithId)
        }
    }
}

