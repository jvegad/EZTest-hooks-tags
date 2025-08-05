Feature: Login de usuario

  @Positive
  Scenario: Login exitoso
    Given el usuario esta en la pagina de login
    When ingresa credenciales validas
    Then deberia acceder al sistema

  