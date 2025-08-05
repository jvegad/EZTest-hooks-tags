package steps;

import hooks.WebDriverContainer;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static org.junit.Assert.assertTrue;

public class StepsLogin {

    private WebDriver driver = WebDriverContainer.driver;

    @Given("el usuario esta en la pagina de login")
    public void usuarioEnLogin() {
        System.out.println("Navegando a la página de login ...");
        driver.get("https://www.saucedemo.com/");
    }

    @When("ingresa credenciales validas")
    public void ingresarCredenciales() {
        System.out.println("Ingresando credenciales válidas...");
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();
    }

    @Then("deberia acceder al sistema")
    public void verificarPantallaPrincipal() {
        System.out.println("Validando pantalla principal ...");
        assertTrue(driver.findElement(By.id("inventory_container")).isDisplayed());
    }
}
