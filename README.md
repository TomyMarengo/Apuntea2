# Apuntea

For the final delivery of the Web Applications Project (PAW) course, the Apuntea application has been fully implemented. Apuntea is a platform designed to share and organize study notes for university students.

This version of Apuntea has been migrated from its previous design, which followed the MVC pattern, to a modern architecture based on a REST API and a Single Page Application (SPA) frontend. The previous version, which used different technologies, can be found at [this repository](https://github.com/TomyMarengo/Apuntea).

The backend is developed using **Java Spring**, providing a robust and scalable REST API, while the frontend is built with **React**, enabling a dynamic and responsive user experience.

## Setup and Installation Instructions

1. Install [Tomcat 7.0.76](https://archive.apache.org/dist/tomcat/tomcat-7/v7.0.76/).
2. Create the `application.properties` file in the `/webapp/src/main/resources` folder based on the [`application.properties.template`](./webapp/src/main/resources/application.properties.template).
3. Create the `.env` file in the `/frontend` folder based on the [.env.template](./frontend/.env.template).
4. Create the database in PostgreSQL and add at least one institution, one degree program, and one user with the `ROLE_ADMIN` role to enable course management within the application.
5. Run `mvn clean install` and move the `webapp/target/webapp.war` file to the `webapps` folder of Tomcat.

## Testing Instructions

1. To run all tests, execute:
   ```sh
   mvn test
   ```
2. To run only frontend tests, navigate to the `/frontend` folder and execute:
   ```sh
   npx vitest
   ```

## Tested Software Versions for Development and Production

- Java 8
- Tomcat 7.0.76
- Maven 3.6.3 or 3.9.4
- PostgreSQL 14.3
- Node.js 22.12.0
- NPM 10.9.0
- React 18.3.1

## Authors

- **Liu**, Jonathan Daniel
- **Marengo**, Tomás Santiago
- **Vilamowski**, Abril
- **Wischñevsky**, David
