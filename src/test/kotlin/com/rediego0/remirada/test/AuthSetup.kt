package com.rediego0.remirada.test

import com.f776.remirada.test.MiradaGarcia
import com.microsoft.playwright.BrowserContext
import kotlin.test.Test
import java.nio.file.Path

class AuthSetup : AuthPlaywrightTest(){
    // Lógica de login copiada a lo cochino nomás y que pasa
    private fun navigateToLoginScreen() {

        page.navigate(MiradaGarcia.BASE_URL + "/sign-in")

        val emailInput = page.locator("#identifier-field")
        emailInput.waitFor(com.microsoft.playwright.Locator.WaitForOptions().setState(com.microsoft.playwright.options.WaitForSelectorState.VISIBLE))

    }

    private fun fillEmailAndContinue(email: String) {
        val emailInput = page.locator("#identifier-field")
        emailInput.fill(email)

        val continueButton =
            page.locator("xpath=/html/body/div[1]/div[1]/div[2]/div/div/div[1]/div[2]/form/div[2]/button")
        continueButton.click()
    }

    @Test
    fun `save authenticated state`(){
        navigateToLoginScreen()

        fillEmailAndContinue("refriappnoreply@gmail.com")

        val passwordField = page.locator("#password-field")
        passwordField.fill("Contrasena.123LOL")

        val signInButton = page.locator("xpath=/html/body/div[1]/div[1]/div[2]/div/div/div[1]/div[2]/form/button[2]")
        signInButton.click()

        page.navigate(MiradaGarcia.BASE_URL)

        page.waitForURL(MiradaGarcia.BASE_URL)

        val filename = "auth-state.json"
        val pathObject = Path.of(filename)
        val context: BrowserContext = page.context()
        context.storageState(BrowserContext.StorageStateOptions().setPath(pathObject))

        println("Guardado en: ${pathObject.toAbsolutePath()}")

    }

}