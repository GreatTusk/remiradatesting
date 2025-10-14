package com.rediego0.remirada.test

import com.f776.remirada.test.MiradaGarcia
import com.microsoft.playwright.Page
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertTrue

class CartTest : AuthPlaywrightTest(){

    val SCREENSHOT_DIR = "src/test/resources/carttest"

    private fun navigateToCartScreen() {
        page.navigate(MiradaGarcia.BASE_URL)
        page.waitForTimeout(3000.0)

        val cartButton = page.locator("xpath=/html/body/div[1]/nav/div/div[2]/ul/li[4]/a")

        page.waitForCondition { cartButton.isEnabled }

        cartButton.click()
    }

    private fun continueButton() {
        val continueButton = page.locator("xpath=/html/body/section/div/div/div[2]/div[1]/a")
        page.waitForCondition { continueButton.isEnabled }
        continueButton.click()
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
    fun `checkout with empty cart`() {
        navigateToCartScreen()

        val continueButton = page.locator("xpath=/html/body/section/div/div/div[2]/div[1]/button")

        page.waitForTimeout(2000.0)

        page.screenshot(Page.ScreenshotOptions().setPath(Paths.get("$SCREENSHOT_DIR/checkout_with_empty_cart.png")))

        assertTrue(continueButton.isDisabled, "Continue button should be disabled.")
    }

    @Test
    fun `total amount reflects product quantity changes` () {
        navigateToCartScreen()
        addItem(3)

        page.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE)

        page.waitForTimeout(2000.0)
        page.screenshot(Page.ScreenshotOptions().setPath(Paths.get("$SCREENSHOT_DIR/total_amount_reflects_product_quantity_changes.png")))

        page.waitForTimeout(5000.0)

    }

    @Test
    fun `checkout with products` () {
        navigateToCartScreen()
        addItem(2)
        page.waitForTimeout(2000.0)
        continueButton()

        page.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE)

        page.waitForTimeout(2000.0)

        page.screenshot(Page.ScreenshotOptions().setPath(Paths.get("$SCREENSHOT_DIR/checkout_with_products.png")))
        page.waitForTimeout(5000.0)
    }
}