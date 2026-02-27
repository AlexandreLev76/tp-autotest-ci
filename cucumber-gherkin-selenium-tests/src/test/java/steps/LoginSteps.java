package steps;

import config.DriverFactory;
import io.cucumber.java.*;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import pages.LoginPage;

import static org.junit.jupiter.api.Assertions.*;

public class LoginSteps {

    private WebDriver driver;
    private LoginPage loginPage;

    @Before
    public void setup() {
        driver = DriverFactory.createDriver();
        loginPage = new LoginPage(driver);
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    @Given("je suis sur la page de login")
    public void open_login_page() {
        loginPage.open();
    }

    @When("je me connecte avec {string} et {string}")
    public void login(String user, String password) {
        loginPage.login(user, password);
    }

    @Then("un message d'erreur est affiché")
    public void error_message_displayed() {
        assertTrue(loginPage.isErrorDisplayed());
    }

    @Then("je suis redirigé vers le dashboard")
    public void redirected_to_dashboard() {
        assertTrue(loginPage.isLoginSuccessful());
    }
}
