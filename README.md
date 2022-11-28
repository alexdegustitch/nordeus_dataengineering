The database is designed in the following way.  There are 7 tables.  I tried to design a database with these tables assuming that these tables represent only a part of the tables of the real database.
For this reason, there is one parent table Event in which all common data for the events are located.  There is no user_id because some events do not depend on the user.  In our case, they all depend, but since I made the tables assuming that they represent a part of the real database, I kept  the event_id in the tables registration_event, login_event, transaction_event.

There is a user table that contains all the values ​​that characterize the user.  A separate Country= table could have also been created.
There is a user table that contains all the values ​​related to the user (their id, name, country code and device_os).
Since we are doing the analysis, INDEXes were used for faster data search purposes.  Of course, in case that insert, update or delete operations are often performed in given database tables, you should use index-es carefully because they slow down these operations.
However, in our case, they are desirable.  The foreign key user_id in the login, transaction and registration tables does not refer to the primary key in the user table because when loading the data, some transactions and logins are located before the registration data of a user.  For this reason, foreign keys user_id do not refer to the primary key user_id from the user table.

When the application starts, two methods are called which are annotated with @Bean annotation found in Config class with @Configuration annotation.
These two methods read the contents of the events.jsonl and exchange_rates.jsonl files and insert the read data in the corresponding tables of our database.
When processing the above mentioned files, all invalid events - those with missing data and those whose values are not valid (in the database there is a check constraint that checks whether the currency_iso_code is from the set 'USD' or 'EUR', whether the transaction value is valid, etc.)
Also, the size of the country is set to be two characters, thus all invalid values for the country are discarded).
Then the methods that clean the data are started.  They clean the data in the following way:
1. Any event that happened before May 8, 2010 or after May 22, 2010 (since we assume that today is May 22, 2010) is deleted. Of course, the upper limit for the date can be changed if the data are analyzed on another day.
2. Any transaction event that has the same event_id and user_id in the login_event and transaction_event tables is deleted.  - I assumed that the user can be logged in on maximum one device at a time and that it is not possible to log in at the same time or perform a transaction.
3. There must be no transaction or login of a user who is not registered
4. There must be no transaction or user login before or at the time of his registration
5. I also wanted to delete all transactions that occurred before the first login of the user.  However, almost a third of the transactions happened before the first login, by which I assumed that the user was logged in after registration, so I didn't delete such transactions.

Application usage:

The application uses Java 19. If you need to use another version of Java, in the pom.xml file change the java.version property to the desired version.
The application is started on localhost on port 8080. (If port 8080 is already in use, change the server.port value to a free port in the application.properties file).
When starting the application one should wait for all the data to be processed, entered into the database and cleaned.

Level stats can be obtained by sending a POST request to http://localhost:8080/user/userStats whose body has the following format { "user_id": userId, "date": date}, where userId and date are parameters (date - optional).
Game stats can be obtained by sending a POST request to http://localhost:8080/game/gameStats (when country is not specified) and http://localhost:8080/game/gameStatsByCountry (when country is specified) whose bodies have the following format {"date":date} where date is a parameter that can be set.

Also - the results of the request can be seen on the client application which represents the React application and whose git repository is located on the following link https://github.com/alexdegustitch/nordeus_dataengineering_reactapp.git.



