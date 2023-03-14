### ContactManager
- Application Used to Manage user contacts.
###Technologies Used
-Spring boot
-Hibernate
-Spring security
-Thymeleaf
-JS, Jquery, HTML, CSS
-MySql
 ### Description
 - Using this application user can login, signup, perform CRUD operations on user contacts, search functionlity.
 - Role Based Authentication and authorization is implemented by spring security.
 - User details and contacts are stored in MySQL database using hiberate ORM.
 - Each User contains his own contacts implemented using one to many ORM mapping.
 - User cannot access other user contacts.
  ### project components explained
-CustomUserDetailsService -> to get user by name(email).
 - Myconfig -> Configuration for security which contains authentication provider, password encoder, security rules.
 - Home controller -> contains handlers for home page, user signup and login.
 - User controller -> contains handlers for performing crud operations on contacts.
 - Search Controller -> for searching contacts. implemented using javascript.
 - contact and user repository interfaces -> to communicate with database.
 - Entities -> UserEntity and ContactEntity
 - Messsage -> for displaying messages on performing operations.

