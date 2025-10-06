package com.rediego0.remirada.test

import com.f776.remirada.test.MiradaGarcia
import kotlin.test.Test

class CartTest : AuthPlaywrightTest(){
    // Incompleto y sin sentido, página de carrito no está disponible
    private fun navigateToCartScreen() {
        page.navigate(MiradaGarcia.BASE_URL)

        val cartButton = page.locator("xpath=/html/body/div[1]/nav/div/div[2]/ul/li[4]/a")

        page.waitForCondition { cartButton.isEnabled }

        cartButton.click()
    }

    private fun continueButton() {
        val continueButton = page.locator("xpath=ni idea bro")
        page.waitForCondition { continueButton.isEnabled }
        continueButton.click()
    }

    fun navigateToProductScreen() {
        page.navigate(MiradaGarcia.BASE_URL)

        val storeButton = page.locator("xpath=/html/body/div[1]/nav/div/div[2]/ul/li[3]/a")

        page.waitForCondition { storeButton.isEnabled }

        storeButton.click()
    }

    fun addItem() {
        val buyPlanPlus = page.locator("xpath=//*[@id=\"3\"]/div/div/a/button")
        page.waitForCondition { buyPlanPlus.isEnabled }
        buyPlanPlus.click()
    }

    @Test
    fun `checkout with empty cart`() {
        navigateToCartScreen()
        continueButton()
        // Ni idea si pide inputs antes de darle a continuar o después
    }

    @Test
    fun `total amount reflects product quantity changes` () {
        navigateToCartScreen()
        // Se muestra el carrito vacío

        navigateToProductScreen()
        addItem()
        // Agregar un item redirige automáticamente al carrito
        // Habría que repetir el proceso varias veces para probar con diferentes cantidades

    }

    @Test
    fun `checkout with products` () {
        navigateToProductScreen()
        addItem()
        continueButton()
    }
}