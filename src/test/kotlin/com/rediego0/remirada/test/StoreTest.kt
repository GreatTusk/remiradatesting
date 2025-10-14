package com.rediego0.remirada.test

import com.f776.remirada.test.MiradaGarcia
import com.microsoft.playwright.Page
import java.nio.file.Paths
import kotlin.test.Test

class StoreTest : AuthPlaywrightTest(){

    val SCREENSHOT_DIR = "src/test/resources/storetest"

    private fun navigateToStoreScreen() {
        page.navigate(MiradaGarcia.BASE_URL)
        page.waitForTimeout(3000.0)

        val storeButton = page.locator("xpath=/html/body/div[1]/nav/div/div[2]/ul/li[3]/a")
        page.waitForCondition { storeButton.isEnabled }
        storeButton.click()
    }

    private fun selectRandomPlan(): com.microsoft.playwright.Locator {
        val plans = mapOf(
            "petite" to "xpath=//*[@id=\"2\"]/div/div/a/button/span",
            "xpress" to "xpath=//*[@id=\"1\"]/div/div/a/button/span",
            "plus" to "xpath=//*[@id=\"3\"]/div/div/a/button/span"
        )

        val randomPlanEntry = plans.entries.random()
        return page.locator(randomPlanEntry.value)
    }

    @Test
    fun `navigate to store screen`() {
        navigateToStoreScreen()

        page.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE)

        page.waitForTimeout(2000.0)

        page.screenshot(Page.ScreenshotOptions().setPath(Paths.get("$SCREENSHOT_DIR/navigate_to_store_screen.png")))

        page.waitForTimeout(2000.0)
    }

    @Test
    fun `add product to cart in store screen`() {
        // Se le da click al plan pero la página redirige al carrito vacío
        navigateToStoreScreen()
        val planButton = selectRandomPlan()

        page.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE)

        planButton.click()

        page.screenshot(Page.ScreenshotOptions().setPath(Paths.get("$SCREENSHOT_DIR/add_product_to_cart_in_store_screen.png")))

        page.waitForTimeout(4000.0)
    }
}