package com.f776.remirada.test

import com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertTrue

class LoginTest : PlaywrightTest() {

    private companion object {
        val TEST_EMAIL by lazy {
            System.getenv("TEST_EMAIL") ?: error("No TEST_EMAIL env var set")
        }

        val TEST_PASSWORD by lazy {
            System.getenv("TEST_PASSWORD") ?: error("No TEST_PASSWORD env var set")
        }
    }

    private fun navigateToLoginScreen() {
        page.navigate(MiradaGarcia.BASE_URL)

        page.waitForSelector("xpath=//*[@id='auth-container']//button[1]")

        val loginButton = page.locator("xpath=//*[@id='auth-container']//button[1]")

        page.waitForCondition {
            loginButton.isEnabled
        }

        assertTrue("Login button is not visible") {
            loginButton.isVisible
        }

        loginButton.click()

        page.waitForURL("**/sign-in**")

        assertTrue("Page is not on login screen") {
            println(page.url())
            page.url().contains("/sign-in")
        }
    }

    private suspend fun fillEmailAndContinue(email: String) {
        val emailInput = page.locator("#identifier-field")
        emailInput.fill(email)

        val continueButton =
            page.locator("xpath=/html/body/div[1]/div[1]/div[2]/div/div/div[1]/div[2]/form/div[2]/button")
        continueButton.click()
        delay(800)
    }

    @Test
    fun `login with external provider`() = runBlocking {
        navigateToLoginScreen()

        val googleButton = page.locator("button[class*='button__google']")
        googleButton.click()

        delay(1000)

        page.waitForURL("**accounts.google.com**")

        assertTrue("Page did not navigate to Google OAuth") {
            page.url().contains("accounts.google.com/o/oauth2/auth")
        }
    }

    @Test
    fun `login with email and password`() = runBlocking {
        navigateToLoginScreen()

        fillEmailAndContinue(TEST_EMAIL)

        val passwordField = page.locator("#password-field")
        passwordField.fill(TEST_PASSWORD)

        page.waitForSelector("xpath=/html/body/div[1]/div[1]/div[2]/div/div/div[1]/div[2]/form/button[2]")

        val signInButton = page.locator("xpath=/html/body/div[1]/div[1]/div[2]/div/div/div[1]/div[2]/form/button[2]")
        signInButton.click()

        delay(1000)

        assertTrue("User is not logged in") {
            page.url() == MiradaGarcia.BASE_URL + "/"
        }
    }

    @Test
    fun `login with invalid credentials shows error`() = runBlocking {
        navigateToLoginScreen()

        fillEmailAndContinue("mymail@mail.com")

        val errorMessage = page.locator("#error-identifier")

        assertTrue("Error message is not displayed") {
            errorMessage.isVisible
        }

        assertThat(errorMessage).hasText("Couldn't find your account.")
    }

    @Test
    fun `login with incorrect password`() = runBlocking {
        navigateToLoginScreen()

        fillEmailAndContinue(TEST_EMAIL)

        val passwordField = page.locator("#password-field")
        passwordField.fill("Contrasena.123")

        page.waitForSelector("xpath=/html/body/div[1]/div[1]/div[2]/div/div/div[1]/div[2]/form/button[2]")

        val signInButton = page.locator("xpath=/html/body/div[1]/div[1]/div[2]/div/div/div[1]/div[2]/form/button[2]")
        signInButton.click()

        delay(500)

        val errorMessage = page.locator("#error-password")

        assertThat(errorMessage).hasText("Password is incorrect. Try again, or use another method.")
    }

}