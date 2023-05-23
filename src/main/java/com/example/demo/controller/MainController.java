package com.example.demo.controller;

import com.example.demo.entity.AuthRequest;
import com.example.demo.entity.TaskDto;
import com.example.demo.entity.User;
import com.example.demo.service.CustomUserDetailsService;
import com.example.demo.service.TaskService;
import com.example.demo.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/") //TODO acá deberia de ir /api/v1/ pero por un problema con la seguridad no me lo toma
@Api(value = "Main controller")
public class MainController {

    private final CustomUserDetailsService customUserDetailsService;
    private final UserService userService;
    private final TaskService taskService;

    public MainController( CustomUserDetailsService customUserDetailsService, UserService userService, TaskService taskService) {
        this.customUserDetailsService = customUserDetailsService;
        this.userService = userService;
        this.taskService = taskService;
    }


    @GetMapping("/free")
    public String welcomefree() {
        return "Welcome to no free zone!";
    }


    @PostMapping("/register")
    @ApiOperation(value = "Metodo para registar un nuevo usario. (No se necesita estar logeado)")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Success"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Error en el servidor.")
    })
    public ResponseEntity<Object> addUser(@RequestBody User user){
        userService.saveUser(user);
        return new ResponseEntity("usuario registrado.", HttpStatus.CREATED);
    }

    @PostMapping("/authenticate")
    @ApiOperation(value = "Metodo para loguearse en el sistema. (No se necesita estar logeado)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Error en el servidor.")
    })
    public ResponseEntity<Object> generateToken(@RequestBody AuthRequest authRequest) {
        return new ResponseEntity<>(userService.authenticate(authRequest), HttpStatus.OK);
    }


    @GetMapping("/task")
    @ApiOperation(value = "Obtener todas las tareas ordenadas por usuario. (No se necesita estar logeado)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 500, message = "Error en el servidor.")
    })
    public ResponseEntity<Object> getAllTask(){
        return new ResponseEntity<>( taskService.getAllTask(), HttpStatus.OK);
    }


    @ApiOperation(value = "Obtener todas las tareas de un usuario.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 400, message = "Bad Request."),
            @ApiResponse(code = 404, message = "Data no found."),
            @ApiResponse(code = 500, message = "Error en el servidor.")
    })
    @GetMapping("task/{email}")
    public ResponseEntity<Object> getAllTaskByUser(@PathVariable String email){
        return new ResponseEntity<>( taskService.getAllTaskByUser(email), HttpStatus.OK);
    }


    @PostMapping("/task")
    @ApiOperation(value = "Metodo para registar una tarea.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Success"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Error en el servidor.")
    })
    public ResponseEntity<Object> addTask(@RequestBody TaskDto taskDto){
        taskService.createTask(taskDto);
        return new ResponseEntity("Tarea Creada.", HttpStatus.CREATED);
    }


    /*TODO implement patch*/
    @PutMapping("/finishTask/{taskId}")
    @ApiOperation(value = "Metodo para finalizar una tarea.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success."),
            @ApiResponse(code = 400, message = "Bad Request."),
            @ApiResponse(code = 404, message = "Data no found."),
            @ApiResponse(code = 500, message = "Error en el servidor.")
    })
    public ResponseEntity<Object> finishTask(@PathVariable Long taskId){
        taskService.finishTask(taskId);
        return new ResponseEntity("Tarea Finalizada.", HttpStatus.OK);
    }


    @ApiOperation(value = "Metodo para finalizar todas las tareas de un usuario.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success."),
            @ApiResponse(code = 400, message = "Bad Request."),
            @ApiResponse(code = 404, message = "Invalid User."),
            @ApiResponse(code = 500, message = "Error en el servidor.")
    })
    @PutMapping("/finishAllTask")
    public ResponseEntity<Object> finishAllTask(@RequestHeader("Authorization") String bearerToken){
        String user = customUserDetailsService.getUserFromToken(bearerToken);
        taskService.finishAllTask(user);
        return new ResponseEntity("Tareas del usuario "+user+" Finalizadas.", HttpStatus.OK);
    }


    @ApiOperation(value = "Metodo para eliminar una tarea.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success."),
            @ApiResponse(code = 400, message = "Bad Request."),
            @ApiResponse(code = 403, message = "Data Forbidden."),
            @ApiResponse(code = 404, message = "Data no found."),
            @ApiResponse(code = 500, message = "Error en el servidor.")
    })
    @DeleteMapping("/task/{taskId}")
    public ResponseEntity<Object> deleteTask(@PathVariable Long taskId, @RequestHeader("Authorization") String bearerToken){
        taskService.deleteTask(taskId, customUserDetailsService.getUserFromToken(bearerToken));
        return new ResponseEntity("Tarea Eliminada.", HttpStatus.OK);
    }


    @DeleteMapping("/user/{userMail}")
    @ApiOperation(value = "Metodo para eliminar un usuario.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success."),
            @ApiResponse(code = 400, message = "Bad Request."),
            @ApiResponse(code = 403, message = "Data Forbidden."),
            @ApiResponse(code = 404, message = "Data no found."),
            @ApiResponse(code = 500, message = "Error en el servidor.")
    })
    public ResponseEntity<Object> deleteUser(@PathVariable String userMail){
        userService.deleteUser(userMail);
        return new ResponseEntity("Usuario Eliminado.", HttpStatus.OK);
    }


    @GetMapping("/user")
    @ApiOperation(value = "Metodo para obtener todos los usuarios")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 500, message = "Error en el servidor.")
    })
    public ResponseEntity<Object>getAllUsers(){
        return new ResponseEntity<>(userService.getAllUser(),HttpStatus.OK);
    }



}
