package com.f776.remirada.test

import com.microsoft.playwright.Browser
import com.microsoft.playwright.BrowserType
import com.microsoft.playwright.Page
import com.microsoft.playwright.Playwright
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

abstract class PlaywrightTest {
    private lateinit var playwright: Playwright
    private lateinit var browser: Browser
    protected lateinit var page: Page

    private companion object {
        private val OPEN_BROWSER by lazy {
            System.getenv("OPEN_BROWSER")?.toBoolean() ?: true
        }
        private val BROWSER_WIDTH by lazy {
            System.getenv("BROWSER_WIDTH")?.toIntOrNull() ?: 1280
        }
        private val BROWSER_HEIGHT by lazy {
            System.getenv("BROWSER_HEIGHT")?.toIntOrNull() ?: 1000
        }
    }

    @BeforeTest
    fun setup() {
        playwright = Playwright.create()
        browser = playwright.chromium().launch(BrowserType.LaunchOptions().setHeadless(!OPEN_BROWSER))
        page = browser.newPage()
        page.setViewportSize(BROWSER_WIDTH, BROWSER_HEIGHT)
    }

    @AfterTest
    fun tearDown() {
        page.close()
        browser.close()
        playwright.close()
    }
}