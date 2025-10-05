package com.f776.remirada.test

import com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat
import kotlin.test.Test
import kotlin.test.assertTrue

class LoginTest : PlaywrightTest() {

    private fun navigateToLoginScreen() {
        page.navigate(MiradaGarcia.BASE_URL)

        val loginButton = page.locator("xpath=/html/body/div[1]/nav/div/div[1]/button[1]")

        page.waitForCondition {
            loginButton.isEnabled
        }

        assertTrue("Login button is not visible") {
            loginButton.isVisible
        }

        assertTrue("Login button is not enabled") {
            loginButton.isEnabled && loginButton.evaluate("el => el.tagName.toLowerCase() === 'button'") == true
        }

        loginButton.click()

        assertTrue("Page is not on login screen") {
            println(page.url())
            page.url().contains("/sign-in")
        }
    }

    private fun fillEmailAndContinue(email: String) {
        val emailInput = page.locator("#identifier-field")
        emailInput.fill(email)

        val continueButton =
            page.locator("xpath=/html/body/div[1]/div[1]/div[2]/div/div/div[1]/div[2]/form/div[2]/button")
        continueButton.click()
    }

    @Test
    fun `login with external provider`() {
        navigateToLoginScreen()

        val googleButton = page.locator("button[class*='button__google']")
        googleButton.click()

        assertTrue("Page did not navigate to Google OAuth") {
            page.url().contains("accounts.google.com/o/oauth2/auth")
        }
    }

    @Test
    fun `login with email and password`() {
        navigateToLoginScreen()

        fillEmailAndContinue("refriappnoreply@gmail.com")

        val passwordField = page.locator("#password-field")
        passwordField.fill("Contrasena.123LOL")

        val signInButton = page.locator("xpath=/html/body/div[1]/div[1]/div[2]/div/div/div[1]/div[2]/form/button[2]")
        signInButton.click()

        assertTrue("User is not logged in") {
            page.url() == MiradaGarcia.BASE_URL + "/"
        }
    }

    @Test
    fun `login with invalid credentials shows error`() {
        navigateToLoginScreen()

        fillEmailAndContinue("mymail@mail.com")

        val errorMessage = page.locator("#error-identifier")

        assertTrue("Error message is not displayed") {
            errorMessage.isVisible
        }

        assertThat(errorMessage).hasText("Couldn't find your account.")
    }

    @Test
    fun `login with incorrect password`() {
        navigateToLoginScreen()

        fillEmailAndContinue("refriappnoreply@gmail.com")

        val passwordField = page.locator("#password-field")
        passwordField.fill("Contrasena.123")

        val errorMessage = page.locator("#error-password")

        assertThat(errorMessage).hasText("Password is incorrect. Try again, or use another method.")
    }

}