package com.rediego0.remirada.test

import com.microsoft.playwright.Browser
import com.microsoft.playwright.BrowserType
import com.microsoft.playwright.Page
import com.microsoft.playwright.Playwright
import java.nio.file.Path
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

abstract class AuthPlaywrightTest {
    private lateinit var authPlaywright: Playwright
    private lateinit var authBrowser: Browser
    protected lateinit var page: Page

    @BeforeTest
    fun setup() {
        authPlaywright = Playwright.create()

        val filename = "auth-state.json"
        val authFilePath = Path.of(System.getProperty("user.dir"), filename)


        val contextOptions = Browser.NewContextOptions().setStorageStatePath(authFilePath)

        val launchOptions = BrowserType.LaunchOptions().setHeadless(false)
        authBrowser = authPlaywright.chromium().launch(launchOptions)

        val context = authBrowser.newContext(contextOptions)
        page = context.newPage()
        page.setViewportSize(1280, 1000)

    }

    @AfterTest
    fun tearDownAuth() {
        if (::page.isInitialized) {
            page.close()
        }
        if (::authBrowser.isInitialized) {
            authBrowser.close()
        }
        if (::authPlaywright.isInitialized) {
            authPlaywright.close()
        }
    }
}