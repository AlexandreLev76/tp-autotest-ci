package pages;

import config.Config;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class BookListPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public BookListPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void open() {
        driver.get(Config.BASE_URL + "/");
    }

    public void clickAddBook() {
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Ajouter un livre"))).click();
    }

    public boolean isOnListPage() {
        return wait.until(ExpectedConditions.urlToBe(Config.BASE_URL + "/"))
                && driver.findElement(By.tagName("h1")).getText().contains("Liste des livres");
    }

    public boolean isBookInList(String titre) {
        List<WebElement> rows = driver.findElements(By.cssSelector("table.table tbody tr"));
        for (WebElement row : rows) {
            String cellTitre = row.findElement(By.xpath("./td[2]")).getText();
            if (titre.equals(cellTitre)) return true;
        }
        return false;
    }

    public void clickEditBook(String titre) {
        WebElement row = findRowByTitre(titre);
        if (row != null) {
            row.findElement(By.xpath(".//button[contains(text(),'Modifier')]")).click();
        } else {
            throw new AssertionError("Livre non trouvé dans la liste: " + titre);
        }
    }

    public void clickDeleteBook(String titre) {
        WebElement row = findRowByTitre(titre);
        if (row != null) {
            row.findElement(By.xpath(".//button[contains(text(),'Supprimer')]")).click();
        } else {
            throw new AssertionError("Livre non trouvé dans la liste: " + titre);
        }
    }

    public void acceptDeleteConfirm() {
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();
    }

    private WebElement findRowByTitre(String titre) {
        List<WebElement> rows = driver.findElements(By.cssSelector("table.table tbody tr"));
        for (WebElement row : rows) {
            if (titre.equals(row.findElement(By.xpath("./td[2]")).getText())) {
                return row;
            }
        }
        return null;
    }

    public void waitForListLoaded() {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("table.table, p")));
    }
}
