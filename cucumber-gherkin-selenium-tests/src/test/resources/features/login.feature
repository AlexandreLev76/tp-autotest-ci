Feature: Authentification utilisateur

  Scenario: Login invalide
    Given je suis sur la page de login
    When je me connecte avec "admin" et "wrong"
    Then un message d'erreur est affiché

  Scenario: Login valide
    Given je suis sur la page de login
    When je me connecte avec "test" et "password"
    Then je suis redirigé vers le dashboard
