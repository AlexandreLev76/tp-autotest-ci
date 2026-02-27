package pages;

import config.Config;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

public class LoginPage {

    private WebDriver driver;
    private WebDriverWait wait;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void open() {
        driver.get(Config.BASE_URL + "/login");
    }

    public void login(String user, String password) {
        driver.findElement(By.id("username")).clear();
        driver.findElement(By.id("username")).sendKeys(user);

        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys(password);

        driver.findElement(By.id("login-btn")).click();
    }

    public boolean isErrorDisplayed() {
        try {
            WebElement error = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.id("errorMsg")
                    )
            );
            return error.isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isLoginSuccessful() {
        return wait.until(
                ExpectedConditions.urlToBe(Config.BASE_URL + "/")
        );
    }

}
