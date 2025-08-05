package runner;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"steps", "hooks"},
        plugin = {"pretty"},
        tags = "@Positive"
)
public class TestRunner {
    // Clase vacía: solo sirve para ejecutar Cucumber con JUnit
}
