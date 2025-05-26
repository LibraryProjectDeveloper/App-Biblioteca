# API REST para Gestión de Biblioteca

Una API REST desarrollada con Spring Boot para gestionar las operaciones de una biblioteca, incluyendo el manejo de libros, usuarios, préstamos y con seguridad implementada mediante Spring Security.

## Tabla de Contenidos

- [Descripción](#descripción)
- [Características Principales](#características-principales)
- [Tecnologías Utilizadas](#tecnologías-utilizadas)
- [Requisitos Previos](#requisitos-previos)
- [Configuración del Entorno](#configuración-del-entorno)
- [Instalación](#instalación)
- [Ejecución de la Aplicación](#ejecución-de-la-aplicación)
- [Documentación de la API (Endpoints)](#documentación-de-la-api-endpoints)
- [Seguridad](#seguridad)
- [Variables de Entorno](#variables-de-entorno-opcional)
- [Cómo Contribuir](#cómo-contribuir-opcional)
- [Licencia](#licencia)

## Descripción

Este proyecto es una API RESTful que provee servicios para la administración de una biblioteca. Permite a los usuarios (bibliotecarios y clientes) interactuar con el sistema para buscar libros, solicitar préstamos y manejar usuarios. La seguridad de la API está gestionada con Spring Security, utilizando JwT (JSON Web Token), passwordEncoder para las contraseñas y configuración con para proteger los endpoints en roles y permisos.

## Características Principales

- Gestión completa de Libros (CRUD: Crear, Leer, Actualizar, Eliminar).
- Gestión de Usuarios (registro, roles, etc.).
- Sistema de Préstamos.
- Búsqueda y filtrado de libros (por título, autor, ISBN, etc.).
- Autenticación y Autorización basada en roles (ej. `USER`, `ADMIN`,`    LIBRARIAN`).
- Endpoints seguros protegidos con Spring Security.
- Conexión a base de datos mysql.

## Tecnologías Utilizadas

- **Java:** 21
- **Spring Boot:** 3.5.0
- **Spring Security:** Última versión
- **Spring Data JPA:** Para la persistencia de datos.
- **Base de Datos:** Mysql
- **Maven / Gradle:** Maven 3.9.7
- **(Opcional) Lombok:** Para reducir código boilerplate.

## Requisitos Previos

Antes de comenzar, asegúrate de tener instalado lo siguiente:

- JDK 21
- Maven [Tu versión, ej: 3.6 o superior] / Gradle [Tu versión]
- Crea una base de datos en mysql bd_biblioteca.
- Un cliente API como Postman, Insomnia o curl para probar los endpoints.
- (Opcional) Un IDE como IntelliJ IDEA, Eclipse o VS Code.

## Configuración del Entorno

1.  **Base de Datos:**
    * Crea una base de datos llamada `nombre_de_tu_bd`.
    * Configura las credenciales de la base de datos en el archivo `src/main/resources/application.properties`:

    ```properties
    	spring.application.name=AppBiblioteca
		spring.datasource.url=jdbc:mysql://localhost:3306/db_biblioteca
		spring.datasource.username=root
		spring.datasource.password=tu_cotraseña
		spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
		spring.jpa.hibernate.ddl-auto=update #importante para que las tablas se creen
		spring.jpa.show-sql=true
		logging.level.org.springframework.security=DEBUG
    ```

2.  **(Opcional) Configuración de Spring Security:**
    * Si utilizas JWT, puede que necesites configurar un secreto para firmar los tokens en `application.properties`:
        ```properties
        # Ejemplo para JWT
        jwt.secret=TU_SECRETO_SUPER_SEGURO_AQUI_DEBE_SER_LARGO
        jwt.expiration.ms=86400000 # 24 horas
        ```

## Instalación

1.  **Clona el repositorio:**
    ```bash
    git clone https://github.com/m1chaelxde19/App-Biblioteca.git
    cd App-Biblioteca
    ```

2.  **Construye el proyecto:**
    * Si usas Maven:
        ```bash
        mvn clean install
        ```
    * Si usas Gradle:
        ```bash
        gradle clean build
        ```

## Ejecución de la Aplicación

Una vez configurado e instalado, puedes ejecutar la aplicación:

* Si usas Maven:
    ```bash
    mvn spring-boot:run
    ```
* Si usas Gradle:
    ```bash
    gradle bootRun
    ```

La aplicación estará disponible por defecto en `http://localhost:8080`.

## Documentación de la API (Endpoints)

**Autenticación**

* `POST /api/auth/login`
    * Descripción: Autentica a un usuario y retorna un token [JWT/otro].
    * Request Body: `{ "username": "usuario", "password": "password" }`
    * Response: `{ "token": "tu_jwt_token" }`
* `POST /api/user/add` Requiere autentificación de ADMIN
    * Descripción: Registra un nuevo usuario.
    * Request Body: `{ "name": "nuevo_usuario", "lastname":"newApellido","email": "correo@ejemplo.com", "password": "password","phone":"newPhone","address":"newAddress" ,"dni":"newDni","idRol":1}`

**Libros (Requieren `USER` o `ADMIN` o `LIBRARIAN` según corresponda, algunos)**

* `GET /api/LIBRARIAN/books/actives`
    * Descripción: Obtiene una lista de todos los libros activos.
* `GET /api/LIBRARIAN/books/inactives`
    * Descripción: Obtiene una lista de todos los libros inactivos.
* `GET /api/LIBRARIAN/books/{id}`
    * Descripción: Obtiene un libro específico por su ID.
* `POST /api/LIBRARIAN/books/add` (Requiere rol `ADMIN`  o `LIBRARIAN`)
    * Descripción: Crea un nuevo libro.
    * Request Body: Ejemplo `{ "titulo": "Cien Años de Soledad", "isbn": "978-0307350438", "stockTotal": 10 }`
* `PUT/api/LIBRARIAN/books/{id}` (Requiere `ADMIN` o `LIBRARIAN`)
    * Descripción: Actualiza un libro existente.

## Seguridad

La seguridad de esta API se gestiona con Spring Security.

* **Autenticación:** Se utiliza el mecanismo de autenticación basada en JWT. Para acceder a los endpoints protegidos, primero debes obtener un token de autenticación a través del endpoint `/api/auth/login`. Este token debe ser incluido en la cabecera `Authorization` de las solicitudes subsiguientes como un `Bearer token`.
    ```
    Authorization: Bearer <TU_TOKEN_JWT>
    ```
* **Autorización:** Los endpoints están protegidos por roles. Los roles principales son:
    * `USER`: Permite realizar operaciones básicas como buscar libros, solicitar préstamos.
    * `ADMIN`: Permite realizar todas las operaciones, incluyendo la gestión de libros, usuarios y préstamos.
	* `LIBRARIAN`: Permite realizar operaciones de guardar un libro y registrar un prestamo.
