package com.rediego0.remirada.test

import kotlin.test.Test

class StoreTest : AuthPlaywrightTest(){

    @Test
    fun `navigate to store screen`() {
        CartTest().navigateToProductScreen()
        // Su timeout cochino
        page.waitForTimeout(6000.0)
    }

    @Test
    fun `add product to cart` () {
        CartTest().navigateToProductScreen()
        CartTest().addItem()
    }
}