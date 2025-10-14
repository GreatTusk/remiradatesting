package com.rediego0.remirada.test

import com.f776.remirada.test.MiradaGarcia
import com.f776.remirada.test.PlaywrightTest
import com.microsoft.playwright.APIRequest
import com.microsoft.playwright.APIRequestContext
import com.microsoft.playwright.Page
import com.microsoft.playwright.Playwright
import java.nio.file.Paths
import kotlin.test.Test

class RegisterTest : PlaywrightTest() {

    val SCREENSHOT_DIR = "src/test/resources/registertest"

    private val TEST_PASSWORD = "Contrasena.123xD"
    private val TEST_SENDR_BASE_URL = "https://api.testsendr.link"

    private lateinit var currentTestEmail: String

    private fun generateUniqueEmail(): String {
        val timestamp = System.currentTimeMillis()
        return "remirada.$timestamp@testsendr.link"
    }

    private fun navigateToLoginScreen() {
        page.navigate(MiradaGarcia.BASE_URL + "/sign-up")
    }

    private fun fillEmailAndPassword(email: String, password: String) {
        val emailInput = page.locator("xpath=//*[@id=\"emailAddress-field\"]")
        emailInput.fill(email)

        val passwordField = page.locator("xpath=//*[@id=\"password-field\"]")
        passwordField.fill(password)
    }

    private fun getApiContext(playwright: Playwright): APIRequestContext {
        return playwright.request().newContext(
            APIRequest.NewContextOptions()
                .setBaseURL(TEST_SENDR_BASE_URL)
        )
    }

    private fun waitForVerificationCode(apiContext: APIRequestContext, maxWaitMillis: Long = 60000): String {
        val startTime = System.currentTimeMillis()
        val apiUrl = "/?email=$currentTestEmail"
        println("Searching for email: $currentTestEmail at $TEST_SENDR_BASE_URL$apiUrl")

        while (System.currentTimeMillis() - startTime < maxWaitMillis) {
            val response = apiContext.get(apiUrl)

            if (response.ok()) {
                val responseJson = response.text()

                if (responseJson.length > 2 && responseJson.trim() != "[]") {
                    println("API Response received.")
                    val textBody = responseJson.substringAfter("\"text\":").substringBefore("\n\n").trim().removeSuffix("\\")

                    val codeRegex = Regex("(\\d{6})")
                    return codeRegex.find(textBody)?.value
                        ?: throw Exception("Could not find the 6-digit verification code in the email text body.")
                }
            }
            Thread.sleep(3000)
        }
        throw Exception("Verification email did not arrive after ${maxWaitMillis / 1000} seconds. Searched for: $currentTestEmail")
    }

    @Test
    fun `create new account and verify email automatically`() {
        currentTestEmail = generateUniqueEmail()
        val apiContext = getApiContext(playwright)

        navigateToLoginScreen()
        fillEmailAndPassword(currentTestEmail, TEST_PASSWORD)

        val continueButton =
            page.locator("xpath=/html/body/div[1]/div[1]/div[2]/div/div/div[1]/div[2]/form/div[2]/div[2]/button")

        continueButton.click()

        val verificationCode = waitForVerificationCode(apiContext)
        println("Code obtained: $verificationCode")

        val codeInputField = page.locator("xpath=/html/body/div[1]/div[1]/div[2]/div/div/div[1]/div[2]/div[1]/div/div[1]/div/div[2]/input")
        codeInputField.fill(verificationCode)
        page.screenshot(Page.ScreenshotOptions().setPath(Paths.get("$SCREENSHOT_DIR/create_new_account_and_verify_email_automatically.png")))

        page.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE)


        apiContext.dispose()

        page.waitForURL(MiradaGarcia.BASE_URL)

        page.waitForTimeout(5000.0)
    }
}
