# Arkkosoft Test
## _Evaluación técnica para Carlos Escalona por parte de Arkkosoft_
-------------------------------------------------------------------
### Tecnologías usadas:
- Java 8
- Spring Boot 2.7.12
- Base de Datos en memoria H2
- Lombok
- Spring Security
- Swagger 2

-------------------------------------------------------------------

## Instalación/Ejecución
- Descargar y ejecutar el proyecto, puerto por defecto 8080.
- La base de datos está en memoria, por lo tanto, no es necesario configurar una conexión.
- Al momento de correr el proyecto se crea la Base de Datos.
- En caso que se necesite acceder a la BD se puede hacer por la url (http://localhost:8080/h2-console/) con los siguientes parámetros:
    * Driver Class:  org.h2.Driver
    * JDBC URL: jdbc:h2:mem:devdb
    * User Name: sa
    * Password:  password
- Existe un script que carga las tablas necesarias (User, Task) para probar el sistema.
- El acceso a la documentación de los servicios rest del proyecto está en (http://localhost:8080/swagger-ui.html#/)
-------------------------------------------------------------------

## Funcionamiento
Para usar el sistema debemos registrar usuarios y luego hacer login, ya existen un usuario registrado de forma 
predeterminada en la Base de Datos (root@gmail.com, admin123). Al momento de hacer login este nos generara un 
Token que usaremos para acceder en los demas endpoint del sistemas que listan a continuación. 


- Crear usuario:
  * http://localhost:8080/register
  *  {
      "userName":"user",
      "email":"user@gmail.com",
      "password":"user123"
    }
-------------------------------------------------------------------

- Autenticar usuario:
   * http://localhost:8080/authenticate
   *  {
       "email":"user@gmail.com",
       "password":"user123"
     }
 -------------------------------------------------------------------
 
- Obtener las tareas de un usuario
  * http://localhost:8080/task/{UserEmail}
-------------------------------------------------------------------

- Obtener todas las tareas de todos los usarios (No se necesita estar autenticado)
  * http://localhost:8080/task
-------------------------------------------------------------------

- Crear Tarea 
  * http://localhost:8080/task/
  * {
      "titulo":"La Tarea",
      "descripcion":"Decripcion",
      "usuario":"user@gmail.com"
    }
-------------------------------------------------------------------

- Finalizar tarea 
  * http://localhost:8080/finishTask/{IdTarea}
-------------------------------------------------------------------

- Finalizar todas las tareas (Solamente las tareas del usuario legueado)
  * http://localhost:8080/finishAllTask
 -------------------------------------------------------------------

- Eliminar tareas (Solo podra eliminar tareas propias)
  * http://localhost:8080/task/{IdTarea}
-------------------------------------------------------------------

- Eliminar Usuario
 * http://localhost:8080/user/{UserEmail}