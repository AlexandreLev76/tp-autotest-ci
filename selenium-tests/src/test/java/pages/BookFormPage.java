package pages;

import config.Config;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BookFormPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public BookFormPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void openCreate() {
        driver.get(Config.BASE_URL + "/create");
    }

    public void openEdit(long id) {
        driver.get(Config.BASE_URL + "/edit/" + id);
    }

    public void setTitre(String titre) {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("titre")));
        driver.findElement(By.name("titre")).clear();
        driver.findElement(By.name("titre")).sendKeys(titre);
    }

    public void setAuteur(String auteur) {
        driver.findElement(By.name("auteur")).clear();
        driver.findElement(By.name("auteur")).sendKeys(auteur);
    }

    public void setAnnee(int annee) {
        driver.findElement(By.name("annee")).clear();
        driver.findElement(By.name("annee")).sendKeys(String.valueOf(annee));
    }

    public void fillForm(String titre, String auteur, int annee) {
        setTitre(titre);
        setAuteur(auteur);
        setAnnee(annee);
    }

    public void submit() {
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']"))).click();
    }

    public boolean isCreatePage() {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("h1")));
        return driver.findElement(By.tagName("h1")).getText().contains("Ajouter un livre");
    }

    public boolean isEditPage() {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("h1")));
        return driver.findElement(By.tagName("h1")).getText().contains("Modifier le livre");
    }
}
