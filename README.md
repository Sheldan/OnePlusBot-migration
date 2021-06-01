# OnePlusBot migration application
Contains the application responsible for migrating the data from the original database into the new database

The legacy database was managed and created in [OnePlusBot](https://github.com/Rithari/OnePlusBot) and was a [mysql](https://www.mysql.com/) database with only a single server in mind. The database structure in the [new version](https://github.com/Sheldan/OnePlusBot) is a [PostgreSQL](https://www.postgresql.org/) database, with multiple servers in mind and a generally overhauled database structure. Therefore the database structure needs to be migrated in order to maintain the history.

The migrations are done incrementally, as the features are ported and each of them ave individual jobs controlled by command line parameters.
The application uses a lot of services from the new version in order to create the objects appropriately, leading to a lot of dependencies pulled in (scheduling etc), which are not necessary, but as the application is a one off migration for each feature, this is not a big issue.
In its essence, this application is a standalone [Spring Boot](https://spring.io/projects/spring-boot) application which executes the migration when starting up.

Supported migrations: 
* Starboard
* Reminders
* Profanities
* Tracked emotes 
* Experience
* Mutes