package com.rediego0.remirada.test

import com.f776.remirada.test.MiradaGarcia
import com.microsoft.playwright.Page
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertTrue

class PaymentTest : AuthPlaywrightTest(){

    val SCREENSHOT_DIR = "src/test/resources/paymenttest"

    private fun navigateToCartScreen() {
        page.navigate(MiradaGarcia.BASE_URL)
        page.waitForTimeout(3000.0)

        val cartButton = page.locator("xpath=/html/body/div[1]/nav/div/div[2]/ul/li[4]/a")
        page.waitForCondition { cartButton.isEnabled }
        cartButton.click()
    }

    fun addItem(repetitions: Int = 1) {
        val plans = mapOf(
            "petite" to "xpath=/html/body/section/div/div/div[1]/div[2]/div/div[3]/div[3]/button[2]",
            "xpress" to "xpath=/html/body/section/div/div/div[1]/div[2]/div/div[1]/div[3]/button[2]",
            "plus" to "xpath=/html/body/section/div/div/div[1]/div[2]/div/div[2]/div[3]/button[2]"
        )

        repeat(repetitions) {
            val randomPlan = plans.entries.random()
            val selectedPlan = page.locator(randomPlan.value)

            page.waitForCondition { selectedPlan.isEnabled }
            selectedPlan.click()
        }
    }

    @Test
    fun `continue to payment without billing details`() {
        navigateToCartScreen()
        addItem(1)

        page.locator("xpath=/html/body/section/div/div/div[2]/div[1]/a").click()

        page.waitForURL(MiradaGarcia.BASE_URL + "/carrito-compras/checkout")

        val paymentButton = page.locator("xpath=/html/body/section/form/div/div[2]/div[2]/div[3]/button")

        assertTrue(paymentButton.isDisabled, "Payment button should be disabled.")

        page.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE)

        page.waitForTimeout(2000.0)

        page.screenshot(Page.ScreenshotOptions().setPath(Paths.get("$SCREENSHOT_DIR/continue_to_payment_without_billing_details.png")))

        page.waitForTimeout(3000.0)

    }
}