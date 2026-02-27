package tests;

import config.Config;
import config.DriverFactory;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.BookFormPage;
import pages.BookListPage;
import pages.LoginPage;

import java.time.Duration;

/**
 * Test UI du parcours complet CRUD livre : création, consultation, modification, suppression.
 * Prérequis : Backend et Frontend démarrés (ex. Backend sur 8080, Frontend sur 4200).
 */
@DisplayName("Parcours CRUD livre (Create, Read, Update, Delete)")
class BookCrudTest {

    private WebDriver driver;
    private LoginPage loginPage;
    private BookListPage bookListPage;
    private BookFormPage bookFormPage;
    private WebDriverWait wait;

    private static final String USER = "test";
    private static final String PASSWORD = "password";
    private static final String TITRE_CREATE = "Le Parcours CRUD Test";
    private static final String AUTEUR_CREATE = "Auteur E2E";
    private static final int ANNEE_CREATE = 2024;
    private static final String TITRE_UPDATE = "Le Parcours CRUD Test (modifié)";
    private static final String AUTEUR_UPDATE = "Auteur E2E Mis à jour";
    private static final int ANNEE_UPDATE = 2025;

    @BeforeEach
    void setup() {
        driver = DriverFactory.createDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        loginPage = new LoginPage(driver);
        bookListPage = new BookListPage(driver);
        bookFormPage = new BookFormPage(driver);
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("Utilisateur crée, consulte, modifie puis supprime un livre")
    void full_crud_cycle_create_read_update_delete() {
        // ——— Connexion ———
        loginPage.open();
        loginPage.login(USER, PASSWORD);
        Assertions.assertTrue(loginPage.isRedirectedToHome(),
                "Après login, redirection vers la liste des livres");

        bookListPage.waitForListLoaded();

        // ——— CREATE : ajouter un livre ———
        bookListPage.clickAddBook();
        wait.until(ExpectedConditions.urlContains("/create"));
        Assertions.assertTrue(bookFormPage.isCreatePage(), "Page « Ajouter un livre » affichée");

        bookFormPage.fillForm(TITRE_CREATE, AUTEUR_CREATE, ANNEE_CREATE);
        bookFormPage.submit();

        wait.until(ExpectedConditions.urlToBe(Config.BASE_URL + "/"));

        // ——— READ (consultation) : vérifier que le livre apparaît dans la liste ———
        Assertions.assertTrue(bookListPage.isBookInList(TITRE_CREATE),
                "Le livre créé doit apparaître dans la liste (consultation)");

        // ——— UPDATE : modifier le livre ———
        bookListPage.clickEditBook(TITRE_CREATE);
        wait.until(ExpectedConditions.urlContains("/edit/"));

        Assertions.assertTrue(bookFormPage.isEditPage(), "Page « Modifier le livre » affichée");
        bookFormPage.fillForm(TITRE_UPDATE, AUTEUR_UPDATE, ANNEE_UPDATE);
        bookFormPage.submit();

        wait.until(ExpectedConditions.urlToBe(Config.BASE_URL + "/"));

        // ——— READ après modification ———
        Assertions.assertTrue(bookListPage.isBookInList(TITRE_UPDATE),
                "Le livre modifié doit apparaître avec le nouveau titre");
        Assertions.assertFalse(bookListPage.isBookInList(TITRE_CREATE),
                "L'ancien titre ne doit plus apparaître");

        // ——— DELETE : supprimer le livre ———
        bookListPage.clickDeleteBook(TITRE_UPDATE);
        bookListPage.acceptDeleteConfirm();

        // Attendre que la liste soit mise à jour (livre disparu)
        wait.until(d -> !bookListPage.isBookInList(TITRE_UPDATE));

        // ——— Vérifier que le livre n'est plus dans la liste ———
        Assertions.assertFalse(bookListPage.isBookInList(TITRE_UPDATE),
                "Le livre supprimé ne doit plus apparaître dans la liste");
    }
}
