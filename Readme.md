## Situación inicial 📍

El equipo de pruebas de la empresa EZTest quiere automatizar el login de su aplicación web. En cada escenario se abre y cierra el navegador de forma repetitiva, lo que genera mucho código duplicado. Para solucionar esto, han decidido usar **Hooks de Cucumber** y así concentrar esa lógica en un solo lugar.

## Descripción del Caso 🔎

Asumes el rol de QA Automation y tu objetivo es:
1. Crear uno o dos escenarios de login.
2. Definir una clase con Hooks (@Before y @After) para manejar la apertura/cierre del navegador.
3. Mantener tus Steps limpios y sin código repetido de configuración.

Con ello, se busca mejorar la productividad y el orden de la suite de pruebas.

## Instrucciones de Configuración y Ejecución
A continuación se detallan los pasos para clonar, compilar y probar el proyecto.

### Prerrequisitos
*   JDK 17 (o compatible) instalado.
*   Apache Maven instalado y configurado en el PATH del sistema.
*   Git instalado.

### Pasos para la Ejecución
1.  **Clonar el repositorio:**
    ```bash
    git clone https://github.com/jvegad/EZTest-hooks-tags
    cd EZTest-hooks-tags
    ```

2.  **Ejecutar las pruebas:**
    ```bash
    mvn clean test
    ```
    Al finalizar, deberías ver un `BUILD SUCCESS` en la consola.

---

## Estructura del Proyecto 📂

El proyecto sigue la estructura estándar de Maven, separando el código de la aplicación (que en este caso no hay) del código de pruebas. Los componentes clave se organizan de la siguiente manera:

```
EZTest-hooks-tags/
|-- pom.xml
`-- src/
    `-- test/
        |-- java/
        |   |-- hooks/
        |   |   |-- Hooks.java              # Configuración @Before y @After
        |   |   `-- WebDriverContainer.java # Comparte la instancia del driver
        |   |-- runner/
        |   |   `-- TestRunner.java       # Ejecutor de pruebas Cucumber
        |   `-- steps/
        |       `-- StepsLogin.java       # Implementación de los pasos Gherkin
        `-- resources/
            `-- features/
                `-- Login.feature         # Escenarios de prueba en Gherkin
```

## Análisis del Código y Solución Aplicada 🧬

### 1. `pom.xml` - Gestión de Dependencias

El `pom.xml` es el corazón del proyecto Maven. Se han incluido las siguientes dependencias clave:
*   **`cucumber-java` y `cucumber-junit`**: Para poder escribir y ejecutar pruebas con Cucumber y JUnit.
*   **`selenium-java`**: La biblioteca fundamental para la automatización de navegadores con WebDriver. **Esta dependencia era crítica y faltaba en la configuración inicial**.
*   **`webdrivermanager`**: Una utilidad que descarga y configura automáticamente el driver del navegador (en este caso, ChromeDriver), evitando tener que gestionarlo manualmente.

### 2. `Login.feature` - Escenario de Prueba

Se define el comportamiento esperado del usuario en un lenguaje natural (Gherkin). La etiqueta `@Positive` permite seleccionar y ejecutar este escenario de forma específica.

### 3. `Hooks.java` - Centralización de la Lógica de Configuración

Esta es la pieza central de la solución.
*   **`@Before`**: Este método se ejecuta **antes** de cada escenario. Se encarga de configurar y abrir el navegador.
*   **`@After`**: Este método se ejecuta **después** de cada escenario, incluso si falla. Su responsabilidad es cerrar el navegador y liberar los recursos.

Para compartir la instancia del `WebDriver` entre el Hook y los Steps, se utiliza una clase simple `WebDriverContainer` que contiene una variable estática.

**`WebDriverContainer.java`**
Esta clase contiene el driver.

```java
package hooks;

import org.openqa.selenium.WebDriver;

public class WebDriverContainer {
    public static WebDriver driver;
}
```

### 4. `StepsLogin.java` - Lógica de los Pasos

Esta clase contiene el "pegamento" (código Java) que implementa cada paso del archivo `.feature`. Gracias a los Hooks, los métodos aquí se centran únicamente en las acciones del usuario dentro de la página, sin preocuparse por abrir o cerrar el navegador.

### 5. `TestRunner.java` - El Ejecutor

Esta clase vacía actúa como un punto de entrada para que JUnit ejecute las pruebas de Cucumber. Las `@CucumberOptions` son clave:
*   `features`: Indica la ruta a los archivos `.feature`.
*   `glue`: Especifica los paquetes donde se encuentran las definiciones de pasos (`steps`) y los hooks (`hooks`). **Fue crucial añadir `hooks` aquí**.
*   `tags`: Filtra los escenarios a ejecutar. En este caso, solo los marcados con `@Positive`.

## Resultados de la Ejecución ✅

Al ejecutar `mvn clean test`, la salida en la consola muestra claramente el orden de ejecución: el Hook `@Before`, los pasos del escenario, y finalmente el Hook `@After`.

```console
[INFO] --- maven-surefire-plugin:3.0.0-M9:test (default-test) @ EZTest-hooks-tags ---
[INFO]
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running runner.TestRunner
[HOOK] Abriendo navegador...
[INFO] ... (Logs de WebDriverManager y ChromeDriver) ...

@Positive
Scenario: Login exitoso                     # features/Login.feature:4
  Given el usuario esta en la pagina de login # steps.StepsLogin.usuarioEnLogin()
Navegando a la página de login ...
  When ingresa credenciales validas          # steps.StepsLogin.ingresarCredenciales()
Ingresando credenciales válidas...
  Then deberia acceder al sistema             # steps.StepsLogin.verificarPantallaPrincipal()
Validando pantalla principal ...

[HOOK] Cerrando navegador...
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 7.852 s - in runner.TestRunner
[INFO]
[INFO] Results:
[INFO]
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```

---

## Conclusiones y Mejoras Futuras 🎯

**Conclusiones:**
*   **Centralización Exitosa**: El uso de Hooks (`@Before` y `@After`) ha permitido eliminar completamente el código de configuración y limpieza del navegador de las clases de Steps.
*   **Código Limpio y Reutilizable**: Los Steps ahora son más legibles y se centran exclusivamente en la lógica de negocio de la prueba.
*   **Mantenibilidad Mejorada**: Si en el futuro se necesita cambiar la forma en que se inicia el navegador (por ejemplo, añadir opciones, cambiar a Firefox), el cambio solo debe realizarse en un único lugar: la clase `Hooks`.

**Posibles Mejoras:**
*   **Gestión de Propiedades**: Las URLs, usuarios y contraseñas no deberían estar "hardcodeadas" en el código. Se podrían mover a un archivo `.properties` para una mejor gestión.
*   **Scenarios Negativos**: Añadir escenarios para pruebas de login fallido, utilizando credenciales incorrectas.
*   **Reportes Avanzados**: Integrar un plugin de reportes de Cucumber (como `cucumber-html-reporter`) para generar informes visuales de las ejecuciones.