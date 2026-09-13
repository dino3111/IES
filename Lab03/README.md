3.1 Done

3.2 Done

c) 
1. The userRepository is instantiated by Spring Boot as a bean and injected into UserController using constructor injection (@Autowired).
2. Methods invoked in userRepository by UserController:
    - save()
    - findAll()
    - findById()
    - delete() 
    They are defined in the CrudRepository interface, which userRepository extends.
3. Data is being saved in the database configured in your Spring Boot application's properties.
4. Defined in the User class with the annotation @NotBlank(message = "Email is mandatory") on the email field.

3.3 

e) 
- curl -X GET http://localhost:8080/api/employees
- curl -X GET http://localhost:8080/api/employees/1
- curl -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -d '{"name":"Dino","email":"claudinomartins@ua.pt"}'
- curl -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -d '{"id":1,"name":"Dino","email":"claudinomartins@ua.pt"}'
- curl -X DELETE http://localhost:8080/api/employees/1

