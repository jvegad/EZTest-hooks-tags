package hooks;

import org.openqa.selenium.chrome.ChromeDriver;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.github.bonigarcia.wdm.WebDriverManager;

public class Hooks {

    @Before
    public void setUp() {
        System.out.println("[HOOK] Abriendo navegador...");
        WebDriverManager.chromedriver().setup();
        WebDriverContainer.driver = new ChromeDriver();
    }

    @After
    public void tearDown() {
        System.out.println("[HOOK] Cerrando navegador...");
        if (WebDriverContainer.driver != null) {
            WebDriverContainer.driver.quit();
        }
    }
}
