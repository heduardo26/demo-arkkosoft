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
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
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

    @GetMapping("/task")
    @ApiOperation(value = "Obtener todas las tareas ordenadas por usuario.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 500, message = "Error en el servidor.")
    })
    public ResponseEntity<Object> getAllTask(){
        return new ResponseEntity<>( taskService.getAllTask(), HttpStatus.OK);
    }

    @GetMapping("task/{email}")
    public ResponseEntity<Object> getAllTaskByUser(@PathVariable String email){
        return new ResponseEntity<>( taskService.getAllTaskByUser(email), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Object> addUser(@RequestBody User user){
        userService.saveUser(user);
        return new ResponseEntity("usuario registrado.", HttpStatus.CREATED);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<Object> generateToken(@RequestBody AuthRequest authRequest) {
        return new ResponseEntity<>(userService.authenticate(authRequest), HttpStatus.OK);
    }


    @PostMapping("/task")
    public ResponseEntity<Object> addTask(@RequestBody TaskDto taskDto){
        taskService.createTask(taskDto);
        return new ResponseEntity("Tarea Creada.", HttpStatus.CREATED);
    }

    /*TODO implement patch*/
    @PutMapping("/finishTask/{taskId}")
    public ResponseEntity<Object> finishTask(@PathVariable Long taskId){
        taskService.finishTask(taskId);
        return new ResponseEntity("Tarea Finalizada.", HttpStatus.OK);
    }


    @PutMapping("/finishAllTask")
    public ResponseEntity<Object> finishAllTask(@RequestHeader("Authorization") String bearerToken){
        taskService.finishAllTask(customUserDetailsService.getUserFromToken(bearerToken));
        return new ResponseEntity("Tareas Finalizadas.", HttpStatus.OK);
    }

    @DeleteMapping("/task/{taskId}")
    public ResponseEntity<Object> deleteTask(@PathVariable Long taskId, @RequestHeader("Authorization") String bearerToken){
        taskService.deleteTask(taskId, customUserDetailsService.getUserFromToken(bearerToken));
        return new ResponseEntity("Tarea Eliminada.", HttpStatus.OK);
    }

    @DeleteMapping("/user/{userMail}")
    public ResponseEntity<Object> deleteUser(@PathVariable String userMail){
        userService.deleteUser(userMail);
        return new ResponseEntity("Usuario Eliminado.", HttpStatus.OK);
    }



}
