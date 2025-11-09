package com.f776.remirada.test

import com.f776.remirada.test.data.LoginTestData
import com.f776.remirada.test.utils.TestDataLoader
import com.microsoft.playwright.Page
import com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat
import com.microsoft.playwright.options.AriaRole
import com.microsoft.playwright.options.LoadState
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.nio.file.Paths
import java.util.regex.Pattern
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

        const val SCREENSHOT_DIR = "src/test/resources/logintest"

        val testData = TestDataLoader.loadTestData<LoginTestData>("login-test-data.json")
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

    private suspend fun findAndClickSignInButton() {
        val signInButton = page.getByRole(
            AriaRole.BUTTON,
            Page.GetByRoleOptions().setName(
                Pattern.compile("Continuar", Pattern.CASE_INSENSITIVE)
            )
        )

        signInButton.click()
        delay(1000)
    }

    private suspend fun fillEmailAndContinue(email: String) {
        val emailInput = page.locator("#identifier-field")
        emailInput.fill(email)

        val continueButton = page.getByRole(
            AriaRole.BUTTON,
            Page.GetByRoleOptions().setName(
                Pattern.compile("Continuar", Pattern.CASE_INSENSITIVE)
            )
        )

        continueButton.click()
        delay(1000)
    }

    @Test
    fun `login with external provider`() = runBlocking {
        navigateToLoginScreen()

        val googleButton = page.locator("button[class*='button__google']")
        googleButton.click()

        page.waitForLoadState(LoadState.NETWORKIDLE)

        delay(1000)

        assertTrue("Page did not navigate to Google OAuth") {
            page.url().contains("accounts.google.com") &&
                    page.url().contains("oauth")
        }

        // Take screenshot after navigation to Google OAuth
        page.screenshot(
            Page.ScreenshotOptions().setPath(
                Paths.get("$SCREENSHOT_DIR/login_with_external_provider.png")
            )
        )

        Unit
    }

    @Test
    fun `login with email and password`() = runBlocking {
        navigateToLoginScreen()

        fillEmailAndContinue(TEST_EMAIL)

        val passwordField = page.locator("#password-field")
        passwordField.fill(TEST_PASSWORD)

        findAndClickSignInButton()

        delay(1000)

        page.waitForLoadState(LoadState.NETWORKIDLE)

        // Take screenshot after login attempt
        page.screenshot(
            Page.ScreenshotOptions().setPath(
                Paths.get("$SCREENSHOT_DIR/login_with_email_and_password.png")
            )
        )

        assertTrue("User is not logged in") {
            page.url() == MiradaGarcia.BASE_URL + "/carrito-compras"
        }
    }

    @Test
    fun `login with invalid credentials shows error`() = runBlocking {
        navigateToLoginScreen()

        fillEmailAndContinue(testData.invalidEmail)

        val errorMessage = page.locator("#error-identifier")

        // Take screenshot after error appears
        page.screenshot(
            Page.ScreenshotOptions().setPath(
                Paths.get("$SCREENSHOT_DIR/login_with_invalid_credentials_shows_error.png")
            )
        )

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
        passwordField.fill(testData.incorrectPassword)

        findAndClickSignInButton()

        val errorMessage = page.locator("#error-password")

        // Take screenshot after error appears
        page.screenshot(
            Page.ScreenshotOptions().setPath(
                Paths.get("$SCREENSHOT_DIR/login_with_incorrect_password.png")
            )
        )

        assertThat(errorMessage).hasText("Password is incorrect. Try again, or use another method.")
    }

}