
# Trabajo Práctico PAW - Apuntea - TPE Final
Para la entrega final del trabajo práctico de la materia Proyecto de Aplicaciones Web, se terminará de implementar la aplicación Apuntea, un organizador de apuntes y carpetas para alumnos universitarios.
Se migrará de su diseño anterior siguiendo el patrón MVC a una arquitectura basada en una API REST y un frontend SPA.
 

## Instrucciones de configuración e instalación
1. Instalar [Tomcat 7.0.76](https://archive.apache.org/dist/tomcat/tomcat-7/v7.0.76/)
2. Configurar el archivo `application.properties` que se encuentra en `/webapp/src/main/java/resources`. Para esto debe utilizarse el template `application.properties.template` que se encuentra en el mismo directorio.

## Instrucciones de testing
1. Para correr todos los tests, ejecutar el comando `mvn test`.
2. Para correr solo los tests de frontend, ejecutar el comando `npx vitest` en la carpeta `/frontend`.

## Versiones de software probadas para desarrollo y producción
* Java 8
* Tomcat 7.0.76
* Maven 3.6.3 / 3.9.4
* Postgres 14.3
* Node v22.12.0
* NPM 10.9.0
* React 18.3.1

## Usuarios
### Administrador
* Usuario: admin@apuntea.com
* Contraseña: Admin1

### Alumno ITBA Ingeniería Informática
* Usuario: aluinfo@itba.edu.ar
* Contraseña: Itba1

### Alumno ITBA Ingeniería Mecánica
* Usuario: alumeca@itba.edu.ar
* Contraseña: Itba1

### Alumno UTN
* Usuario: alu@utn.edu.ar
* Contraseña: Utn1

## Autores
* Liu, Jonathan Daniel 62533
* Marengo, Tomás Santiago 61587
* Vilamowski, Abril 62495
* Wischñevsky, David 62494

## Acerca de
Este trabajo fue realizado como entrega final del Trabajo Práctico de la materia Proyecto de Aplicaciones Web, en el tercer año de la carrera de Ingeniería Informática en el ITBA.


### Profesores
* Sotuyo Dodero, Juan Martín
* D'onofrio, Nicolás
* Arce, Julián
* Quesada, Francisco