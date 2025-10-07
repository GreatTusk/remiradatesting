package com.f776.remirada.test

import com.microsoft.playwright.Page
import com.microsoft.playwright.options.AriaRole
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import java.util.regex.Pattern
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue


class ContactFormTest : PlaywrightTest() {

    private val errorMessages = arrayOf(
        "¡Uy! El nombre no es válido.",
        "¡Uy! Por favor, ingrese un correo válido.",
        "¡Uy! El número de teléfono debe tener 9 dígitos.",
        "¡Uy! La consulta debe tener al menos 10 caracteres."
    )

    private fun navigateToShopAndOpenForm() {
        page.navigate("${MiradaGarcia.BASE_URL}/tienda")

        val contactButton = page.getByRole(
            AriaRole.BUTTON,
            Page.GetByRoleOptions().setName(
                Pattern.compile("Contacto ventas", Pattern.CASE_INSENSITIVE)
            )
        ).all().first()

        contactButton.click()
    }

    private suspend fun submitForm() {
        val sendButton = page.locator("//*[@id=\":Re9kvfajta:\"]/div/div[2]/div/form/div[7]/button")
        sendButton.click()
        delay(500)
    }

    @Test
    fun `send form without filling fields`() = runBlocking {
        navigateToShopAndOpenForm()
        submitForm()

        errorMessages.forEach {
            assertTrue("Error message '$it' was not visible") {
                page.locator("text=$it").isVisible
            }
        }
    }

    @Test
    fun `send form filling name only`() = runBlocking {
        navigateToShopAndOpenForm()

        val nameInput = page.locator("//*[@id=\"nombre\"]")
        nameInput.fill("Test User")

        submitForm()

        errorMessages.sliceArray(1 until errorMessages.size).forEach {
            assertTrue("Error message '$it' was not visible") {
                page.locator("text=$it").isVisible
            }
        }
    }

    @Test
    fun `send form filling all fields correctly`() = runBlocking {
        navigateToShopAndOpenForm()

        val nameInput = page.getByLabel("Ingrese su nombre:")
        nameInput.fill("Test User")

        val emailInput = page.locator("//*[@id=\"email\"]")
        emailInput.fill("mymail@mail.com")

        val phoneInput = page.getByLabel("Ingrese su teléfono:")
        phoneInput.fill("912345678")

        val messageInput = page.getByLabel("Ingrese su consulta:")
        messageInput.fill("This is a test message for the contact form.")

        submitForm()

        val errorMessages = listOf(
            "¡Uy! El nombre no es válido.",
            "¡Uy! Por favor, ingrese un correo válido.",
            "¡Uy! El número de teléfono debe tener 9 dígitos.",
            "¡Uy! La consulta debe tener al menos 10 caracteres."
        )

        errorMessages.forEach {
            assertFalse("Error message '$it' was not visible") {
                page.locator("text=$it").isVisible
            }
        }

    }
}