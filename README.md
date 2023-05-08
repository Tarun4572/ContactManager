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

 ### Project components explained
 - CustomUserDetailsService -> to get user by name(email).
 - Myconfig -> Configuration for security which contains authentication provider, password encoder, security rules.
 - Home controller -> contains handlers for home page, user signup and login.
 - User controller -> contains handlers for performing crud operations on contacts.
 - Search Controller -> for searching contacts. implemented using javascript.
 - contact and user repository interfaces -> to communicate with database.
 - Entities -> UserEntity and ContactEntity
 - Messsage -> for displaying messages on performing operations.
 
   ### Thymeleaf
   - Thymeleaf is template engine
   - In this project Thymeleaf is used especially th:replace, th:fragment
   - one base html file is used to provide navbar and default structure , other html pages use base.html to get embedded into page using th:replace
   - base html file contains links to boostrap, jquery and icon CDNs. so that when other html pages gets embedded these links get applied.
   - other thymeleaf syntax like th:text, th:if, th:foreach are used to different operations.
   
![s1](https://user-images.githubusercontent.com/81795770/236774403-989e59d2-8838-458b-bd39-372784edfc83.png)
![s2](https://user-images.githubusercontent.com/81795770/236774433-37191b71-b941-4864-86d0-5f775fe70900.png)
![s3](https://user-images.githubusercontent.com/81795770/236774452-74be15b7-7c49-430a-904b-beec31bc2def.png)
![s4](https://user-images.githubusercontent.com/81795770/236774362-8653287c-420c-4d82-aac7-fffe662f2d2a.png)
![s5](https://user-images.githubusercontent.com/81795770/236774481-e9409b15-e120-4ca7-8856-f56c21d5120b.png)




