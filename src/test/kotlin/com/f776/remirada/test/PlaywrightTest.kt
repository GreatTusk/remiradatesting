package com.f776.remirada.test

import com.microsoft.playwright.Browser
import com.microsoft.playwright.Page
import com.microsoft.playwright.Playwright
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

abstract class PlaywrightTest {
    private lateinit var playwright: Playwright
    private lateinit var browser: Browser
    protected lateinit var page: Page

    @BeforeTest
    fun setup() {
        playwright = Playwright.create()
        browser = playwright.chromium().launch()
        page = browser.newPage()
    }

    @AfterTest
    fun tearDown() {
        page.close()
        browser.close()
        playwright.close()
    }
}