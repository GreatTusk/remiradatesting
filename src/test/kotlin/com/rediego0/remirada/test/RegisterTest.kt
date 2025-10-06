package com.rediego0.remirada.test

import com.f776.remirada.test.MiradaGarcia
import com.f776.remirada.test.PlaywrightTest
import kotlin.test.Test

class RegisterTest : PlaywrightTest(){
    private fun navigateToRegisterScreen() {
        page.navigate(MiradaGarcia.BASE_URL)

        val loginButton = page.locator("xpath=/html/body/div[1]/nav/div/div[1]/div/button")

        page.waitForCondition {
            loginButton.isEnabled
        }

        loginButton.click()

        val registerButton = page.locator("xpath=/html/body/div[1]/div[1]/div[2]/div/div/div[2]/div[1]/a")

        page.waitForCondition { registerButton.isEnabled }

        registerButton.click()
    }

    private fun fillEmailAndPassword(email: String, password: String) {
        val emailInput = page.locator("#email")
        emailInput.fill(email)

        val passwordInput = page.locator("#password")
        passwordInput.fill(password)
    }

    @Test
    fun `email registration` () {
        navigateToRegisterScreen()

        fillEmailAndPassword("refriappnoreply@gmail.com", "Contrasena.123LOL")

        val continueButton = page.locator("xpath=/html/body/div[1]/div[1]/div[2]/div/div/div[1]/div[2]/form/div[2]/div[2]/button")

        page.waitForCondition { continueButton.isEnabled }

        continueButton.click()

        // F Autenticación con código al correo XD
    }
}