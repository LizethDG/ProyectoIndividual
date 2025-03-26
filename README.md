# Sistema de Autenticación con Spring Boot

Este proyecto es un sistema de autenticación básico implementado con Spring Boot. Incluye funcionalidades de registro, login y manejo de roles, además de integración con Google OAuth2 para autenticación externa.

## Tabla de Contenidos

1. [Descripción del Proyecto](#descripción-del-proyecto)
2. [Tecnologías Utilizadas](#tecnologías-utilizadas)
3. [Requisitos](#requisitos)
4. [Configuración](#configuración)
   1. [Clonar el Repositorio](#clonar-el-repositorio)
   2. [Configurar la Base de Datos](#configurar-la-base-de-datos)
   3. [Configurar Google OAuth2](#configurar-google-oauth2)
   4. [Ejecutar el Proyecto](#ejecutar-el-proyecto)
5. [Endpoints](#endpoints)
   1. [Registro](#registro)
   2. [Login](#login)
6. [Capturas de Pantalla](#capturas-de-pantalla)

## Descripción del Proyecto

Este proyecto es una implementación de un sistema de autenticación que permite:

- **Registro de usuarios**: Los usuarios pueden registrarse proporcionando un nombre de usuario, correo electrónico y contraseña.
- **Login de usuarios**: Los usuarios pueden iniciar sesión con sus credenciales o mediante Google OAuth2.
- **Manejo de roles**: Los usuarios pueden tener roles de `USER` o `ADMIN`, lo que permite controlar el acceso a ciertas funcionalidades.
- **Protección de rutas**: Las rutas están protegidas para garantizar que solo los usuarios autenticados puedan acceder.

## Tecnologías Utilizadas

- **Spring Boot**: Framework principal para la construcción de la aplicación.
- **Spring Security**: Para la autenticación y autorización.
- **Spring Data JPA**: Para la gestión de la base de datos.
- **MySQL**: Base de datos relacional para almacenar la información de los usuarios.
- **Thymeleaf**: Motor de plantillas para las vistas HTML.
- **OAuth2**: Para la integración con Google.
- **Bootstrap**: Para el diseño de las interfaces de usuario.

## Requisitos

- **Java 17**: JDK 17 o superior.
- **Maven**: Para la gestión de dependencias.
- **MySQL**: Servidor de base de datos.
- **Google Cloud Console**: Para obtener las credenciales de OAuth2.

## Configuración

### 1. Clonar el Repositorio

```bash
git clone https://github.com/tu-usuario/tu-repositorio.git
cd tu-repositorio
```
### 2. Configurar la Base de Datos

- Crea una base de datos en MySQL llamada `sistema_login`.
  La estrucutra es la siguiente:
  ```usuario
  CREATE TABLE usuario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    rol VARCHAR(50) NOT NULL);```
  
- Asegúrate de tener las credenciales de usuario y contraseña correctas para acceder a la base de datos.
- Actualiza el archivo `application.properties` con la URL de conexión a la base de datos, el usuario y la contraseña:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/sistema_login
spring.datasource.username=root
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
```
### 3. Configurar Google OAuth2

Para integrar la autenticación de Google en tu aplicación, debes crear un proyecto en la **Google Cloud Console**, habilitar la API de Google OAuth2, y generar las credenciales necesarias. 

#### **Acceder a la Google Cloud Console**
   - Ve a la página de **Google Cloud Console**: [https://console.cloud.google.com/](https://console.cloud.google.com/)
   - Si no tienes una cuenta de Google Cloud, tendrás que crear una. Si ya tienes una cuenta, inicia sesión.

#### **Crear un Proyecto Nuevo**
   - En la parte superior de la pantalla, haz clic en **Seleccionar un proyecto** o **Nuevo Proyecto**.
     ![Captura de Creación de proyecto](https://drive.google.com/uc?export=view&id=1O7KjdmlcN--yarvMPOyUoZ5L8EdYwDCe)
   - Asigna un nombre a tu proyecto (por ejemplo, "Sistema de Autenticación").
   - Haz clic en **Crear**.

#### **Habilitar la API de Google OAuth2**
   - Una vez que tu proyecto está creado, en el menú lateral izquierdo, ve a **API y servicios** > **Biblioteca**.
   - Busca **Google+ API** o **Google Identity Platform** y habilítala.
     ![Agregando Google Api](https://drive.google.com/uc?export=view&id=1l8k3wjS-eqbwyt2v-IDr4Ij5vwxO1_HR)
   - Esto habilita la autenticación de Google a través de OAuth2 en tu proyecto.

#### **Crear las Credenciales OAuth2**
   - Ve a **API y servicios** > **Credenciales**.
   - Haz clic en el botón **Crear credenciales** y selecciona **ID de cliente de OAuth**.
     ![Creando ID de cliente](https://drive.google.com/uc?export=view&id=1egSfbShw74V43nq2muf5ZTfBpNaoX1nC)
   - Te pedirá que configures una **pantalla de consentimiento**. Esto es lo que los usuarios verán cuando inicien sesión con su cuenta de Google en tu aplicación.
     - Ingresa el nombre del producto, la dirección de correo electrónico del soporte, y una URL de privacidad (puedes usar un enlace ficticio si no tienes una).
     - Haz clic en **Guardar**.
   - Ahora, en **Tipo de aplicación**, selecciona **Aplicación web**.
   - ![Crear ID](https://drive.google.com/uc?export=view&id=1umB0v_Y2oZBQ3hzAU6oQuXU8qJf0cE7g)
   - En **URIs de redirección autorizados**, agrega las URLs donde los usuarios serán redirigidos después de iniciar sesión:
     - Por ejemplo: `http://localhost:8080/login/oauth2/code/google` (para pruebas locales).
   - Haz clic en **Crear**.

#### **Obtener el Client ID y Client Secret**
   - Una vez que creas las credenciales, verás dos valores importantes:
     - **Client ID**: Este es el identificador de tu aplicación.
     - **Client Secret**: Es la clave secreta para autenticar tu aplicación.
   - Copia estos dos valores, ya que los necesitarás para configurarlos en tu aplicación Spring Boot.
     ![Secreto de cliente](https://drive.google.com/uc?export=view&id=1r-sVxsGTmxrYuVQgOizVA0QOs-4lOSBe)

#### **Configurar el archivo `application.properties`**
   Ahora que tienes las credenciales, debes agregarlas a tu archivo `application.properties` (o `application.yml` si usas YAML) para configurar la autenticación con Google.

   Abre el archivo `application.properties` de tu proyecto y agrega lo siguiente:

```properties
# Configuración de Google OAuth2
spring.security.oauth2.client.registration.google.client-id=tu-client-id
spring.security.oauth2.client.registration.google.client-secret=tu-client-secret
spring.security.oauth2.client.registration.google.scope=openid, profile, email
spring.security.oauth2.client.registration.google.redirect-uri={baseUrl}/login/oauth2/code/google
spring.security.oauth2.client.registration.google.client-name=google
```
```properties
spring.security.oauth2.client.registration.google.client-id=tu-client-id
spring.security.oauth2.client.registration.google.client-secret=tu-client-secret
```
### Endpoints
#### 1. Registro
- URL: /register
- Método: GET y POST
- Descripción: Permite registrar un nuevo usuario. Si el código de administrador es correcto, el usuario recibirá el rol de ADMIN. De lo contrario, recibirá el rol de USER.
  ![register](https://drive.google.com/uc?export=view&id=1rwy9SBauqwZv1jsvYbMvhQPG0xWVlKgg)

#### 2. Login
- URL: /login
- Método: GET
- Descripción: Permite al usuario iniciar sesión con su nombre de usuario y contraseña.
  ![Login](https://drive.google.com/uc?export=view&id=1SI6va2Kd1SVuqiyS3RqjTOT6jltgoAQh)

#### 3. Home
- URL: /home
- Descripción: Permite al usuario visualizar un panel principal y acceder a su peril. Además, si cuenta con privilegios de admnistrador tendrá un menú especial.
- ![image](https://github.com/user-attachments/assets/dc0d2417-9060-47b1-8b56-1ab341b804e7)

#### 4. Perfil
- URL: /perfil
- Descripción: Se muestra la información del usuario y permite actualizar sus datos.
- ![image](https://github.com/user-attachments/assets/9b176b31-1282-444e-b19f-dd3588b7f289)

## Aplicación dockerizada
- En la url se muestra el nuevo puerto que utiliza docker (9090)
![image](https://github.com/user-attachments/assets/008ef449-a62e-48f1-976b-078460628013)
- Imagen el docker desktop
![image](https://github.com/user-attachments/assets/068aebec-a35f-4405-ab3a-b43a9fcc0b97)

