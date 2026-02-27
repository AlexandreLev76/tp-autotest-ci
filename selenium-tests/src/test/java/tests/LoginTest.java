package tests;

import config.DriverFactory;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import pages.LoginPage;

public class LoginTest {

    private WebDriver driver;
    private LoginPage loginPage;

    @BeforeEach
    void setup() {
        driver = DriverFactory.createDriver();
        loginPage = new LoginPage(driver);
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }


    @Test
    void login_failure() {
        loginPage.open();
        loginPage.login("admin", "wrong");

        Assertions.assertTrue(
                loginPage.isErrorDisplayed()
        );
    }

    @Test
    void login_success_redirects_to_home() {
        loginPage.open();
        loginPage.login("test", "password"); // identifiants valides

        Assertions.assertTrue(
                loginPage.isRedirectedToHome(),
                "L'utilisateur devrait être redirigé vers la page d'accueil après une connexion réussie"
        );
    }

}
