package config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

public class DriverFactory {

//    public static WebDriver createDriver() {
//        return new ChromeDriver();
//    }


    public static WebDriver createDriver() {
        try {
            ChromeOptions options = new ChromeOptions();
            options.addArguments(
                    "--headless=new",
                    "--no-sandbox",
                    "--disable-dev-shm-usage",
                    "--disable-gpu",
                    "--window-size=1920,1080"
            );

            return new ChromeDriver(options);

        } catch (Exception e) {
            throw new RuntimeException("Impossible de créer le driver Selenium", e);
        }
    }

}