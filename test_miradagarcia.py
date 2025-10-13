import json
from playwright.sync_api import sync_playwright, expect

with open("cookies.json", "r", encoding="utf-8") as f:
    cookies = json.load(f)

miradagarcia_cookies = []
for cookie in cookies:
    if 'sameSite' in cookie:
        if cookie['sameSite'] not in ['Strict', 'Lax', 'None']:
            cookie['sameSite'] = 'Lax'
    else:
        cookie['sameSite'] = 'Lax'
    miradagarcia_cookies.append(cookie)

# CP22 - Resumen del pedido: Confirmar la compra llenando datos de facturación como persona natural
def test_compra_persona():
    with sync_playwright() as p:
        browser = p.chromium.launch(headless=False)
        context = browser.new_context()
        page = context.new_page()
        
        # Carga cookies de login
        context.add_cookies(miradagarcia_cookies)
        
        page.goto("http://localhost:3000/")
        page.wait_for_load_state("load")

        # Hacer click en Carrito
        page.get_by_role("link", name="Carrito").click()
        expect(page).to_have_url("http://localhost:3000/carrito-compras")
        page.wait_for_load_state("load")

        # Seleccionar Plan Xpress y continuar con la compra
        page.get_by_role("button", name="Añadir al carro").first.click()
        page.get_by_role("link", name="Continuar a la compra").click()
        page.wait_for_load_state("load")
        expect(page).to_have_url("http://localhost:3000/carrito-compras/checkout")

        # Rellenar información de facturación y aceptar
        page.get_by_role("button", name="Ingresar datos de facturación").click()
        page.get_by_role("textbox", name="Los poetas #652, Santiago").fill("Avenida Testeo 123")
        page.get_by_role("textbox", name="Ingrese su nombre").fill("Cosme")
        page.get_by_role("textbox", name="Ingrese su apellido").fill("Fulanito")
        page.get_by_role("textbox", name="912345678").fill("912345678")
        page.get_by_role("textbox", name="Describa su evento aquí").fill("Hullabalooza 2025")
        page.get_by_role("button", name="Confirmar").click()

        # Marcar los términos y condiciones, esperar 1 segundo para que el botón se habilite y confirmar la compra
        page.get_by_role("checkbox", name="Estoy de acuerdo con los Té").check()
        page.wait_for_timeout(1000)
        page.get_by_role("link", name="Confirmar pedido").click()
        page.wait_for_load_state("load")
        expect(page).to_have_url("http://localhost:3000/carrito-compras/pedido-confirmado")

        browser.close()

# CP23 - Resumen del pedido: Confirmar la compra llenando datos de facturación como empresa
def test_compra_empresa():
    with sync_playwright() as p:
        browser = p.chromium.launch(headless=False)
        context = browser.new_context()
        page = context.new_page()
        
        # Carga cookies de login
        context.add_cookies(miradagarcia_cookies)
        
        page.goto("http://localhost:3000/")
        page.wait_for_load_state("load")

        # Hacer click en Carrito
        page.get_by_role("link", name="Carrito").click()
        page.wait_for_load_state("load")
        expect(page).to_have_url("http://localhost:3000/carrito-compras")

        # Seleccionar Plan Xpress y continuar con la compra
        page.get_by_role("button", name="Añadir al carro").first.click()
        page.get_by_role("link", name="Continuar a la compra").click()
        page.wait_for_load_state("load")
        expect(page).to_have_url("http://localhost:3000/carrito-compras/checkout")

        # Rellenar información de facturación, contratar como empresa y aceptar
        # Ya que se seleccionó "Contratar como empresa", algunos campos cambian
        page.get_by_role("button", name="Ingresar datos de facturación").click()
        page.get_by_role("checkbox").check()
        page.get_by_role("textbox", name="Flowbite LLC").fill("Planta Nuclear de Springfield")
        page.get_by_role("textbox", name="09.999.999-9").fill("11.111.111-1")
        page.get_by_role("textbox", name="Los poetas #652, Santiago").fill("Avenida Siempreviva 742")
        page.get_by_role("textbox", name="Ingrese su nombre").fill("Cosme")
        page.get_by_role("textbox", name="Ingrese su apellido").fill("Fulanito")
        page.get_by_role("textbox", name="912345678").fill("912345678")
        page.get_by_role("textbox", name="Describa su evento aquí").fill("Hullabalooza 2025")
        page.get_by_role("button", name="Confirmar").click()

        # Marcar los términos y condiciones, esperar 1 segundo para que el botón se habilite y confirmar la compra
        page.get_by_role("checkbox", name="Estoy de acuerdo con los Té").check()
        page.wait_for_timeout(1000)
        page.get_by_role("link", name="Confirmar pedido").click()
        page.wait_for_load_state("load")
        expect(page).to_have_url("http://localhost:3000/carrito-compras/pedido-confirmado")

        browser.close()

# CP24 - Resumen del pedido: Confirmar la compra sin aceptar términos y condiciones
def test_compra_sin_tyc():
    with sync_playwright() as p:
        browser = p.chromium.launch(headless=False)
        context = browser.new_context()
        page = context.new_page()
        
        # Carga cookies de login
        context.add_cookies(miradagarcia_cookies)
        
        page.goto("http://localhost:3000/")
        page.wait_for_load_state("load")

        # Hacer click en Carrito
        page.get_by_role("link", name="Carrito").click()
        expect(page).to_have_url("http://localhost:3000/carrito-compras")
        page.wait_for_load_state("load")

        # Seleccionar Plan Xpress y continuar con la compra
        page.get_by_role("button", name="Añadir al carro").first.click()
        page.get_by_role("link", name="Continuar a la compra").click()
        page.wait_for_load_state("load")
        expect(page).to_have_url("http://localhost:3000/carrito-compras/checkout")

        # Rellenar información de facturación y aceptar
        page.get_by_role("button", name="Ingresar datos de facturación").click()
        page.get_by_role("textbox", name="Los poetas #652, Santiago").fill("Avenida Siempreviva 742")
        page.get_by_role("textbox", name="Ingrese su nombre").fill("Cosme")
        page.get_by_role("textbox", name="Ingrese su apellido").fill("Fulanito")
        page.get_by_role("textbox", name="912345678").fill("912345678")
        page.get_by_role("textbox", name="Describa su evento aquí").fill("Hullabalooza 2025")
        page.get_by_role("button", name="Confirmar").click()

        # Verificar que el botón "Confirmar pedido" este deshabilitado si no se aceptan los TyC
        page.wait_for_timeout(1000)
        expect(page.get_by_role("button", name="Confirmar pedido")).to_be_disabled()

        browser.close()

# CP29 - Footer: Ver los términos y condiciones
def test_tyc():
    with sync_playwright() as p:
        browser = p.chromium.launch(headless=False)
        page = browser.new_page()

        # Ir a la página principal y scrollea hasta el final de la página
        page.goto("http://localhost:3000/")
        home = page.url
        page.wait_for_load_state("load")
        page.keyboard.press('End')
        page.wait_for_selector('footer')

        # Localiza el link de Términos y Condiciones en el footer y hace clic
        page.get_by_role("link", name="Términos & Condiciones").click()
        page.wait_for_load_state("load")
        expect(page).to_have_url("http://localhost:3000/terminos-condiciones", timeout=1000)
  
        browser.close()

# CP30 - Footer: Ver el repositorio Git de la página
def test_github():
    with sync_playwright() as p:
        browser = p.chromium.launch(headless=False)
        context = browser.new_context()
        page = context.new_page()
        
        # Ir a la página principal y scrollea hasta el final de la página
        page.goto("http://localhost:3000/")
        page.wait_for_load_state("load")
        page.keyboard.press('End')
        page.wait_for_selector('footer')
        
        # Localiza el link del GitHub en el footer:
        github_link = page.query_selector('footer a[href*="github.com"]')

        # Verifica que el link es funcional y hace clic en él
        if github_link is not None:
            # Cambia el contexto a la nueva pestaña de GitHub
            with context.expect_page() as new_page_info:
                github_link.click()
            new_page = new_page_info.value
            new_page.wait_for_load_state("load")
            assert new_page.url == "https://github.com/GreatTusk/re-mirada-garcia"
        else:
            raise AssertionError()

        browser.close()

# CP31 - Footer: Ver los conciertos
def test_conciertos():
    with sync_playwright() as p:
        browser = p.chromium.launch(headless=False)
        page = browser.new_page()

        # Ir a la página principal y scrollea hasta el final de la página
        page.goto("http://localhost:3000/")
        home = page.url
        page.wait_for_load_state("load")
        page.keyboard.press('End')
        page.wait_for_selector('footer')
        
        # Localiza el link de Conciertos en el footer:
        page.get_by_role("link", name="Conciertos").click()
        page.wait_for_load_state("load")
        expect(page).to_have_url("http://localhost:3000/portafolio#fotos-concierto", timeout=1000)
        
        browser.close()

# CP32 - Footer: Ver los retratos
def test_retratos():
    with sync_playwright() as p:
        browser = p.chromium.launch(headless=False)
        page = browser.new_page()

        # Ir a la página principal y scrollea hasta el final de la página
        page.goto("http://localhost:3000/")
        home = page.url
        page.wait_for_load_state("load")
        page.keyboard.press('End')
        page.wait_for_selector('footer')
        
        # Localiza el link de Retratos en el footer y hace clic
        page.get_by_role("link", name="Retratos").click()
        page.wait_for_load_state("load")
        expect(page).to_have_url("http://localhost:3000/portafolio#fotos-retratos", timeout=1000)

        browser.close()