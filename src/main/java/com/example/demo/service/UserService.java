package com.example.demo.service;

import com.example.demo.entity.AuthRequest;
import com.example.demo.entity.Task;
import com.example.demo.entity.User;
import com.example.demo.exception.DataDuplicadeException;
import com.example.demo.exception.DataNotFoundException;
import com.example.demo.exception.InvalidUserException;
import com.example.demo.repository.TaskRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtil;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public UserService(UserRepository userRepository, TaskRepository taskRepository, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public User saveUser(User user){
        if(Boolean.TRUE.equals(validateEmail(user.getEmail()))){
             return userRepository.save(user);
        }else{
            throw new DataDuplicadeException("Usuario "+user.getEmail()+" ya existe.");
        }
    }

    //Funcion para validar la exitencia de un usuario
    private Boolean validateEmail(String email){
        User user = getUserByEmail(email);
        return Objects.isNull(user);
    }

    public String authenticate(AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        }catch (Exception e){
            throw new InvalidUserException("user/password Invalido.");
        }

        return jwtUtil.generateToken(authRequest.getEmail());
    }


    public void deleteUser(String userMail) {

        User user = userRepository.findByEmail(userMail);
        if(Objects.isNull(user)){
            throw new DataNotFoundException("El usuario "+userMail+" no existe.");
        }

        List<Task> taskList = taskRepository.findAllByUser(user);
        if(!taskList.isEmpty()){
            throw new DataIntegrityViolationException("El usuario "+userMail+ "no puede ser eliminado ya que tiene tareas registradas.");
        }

        userRepository.deleteById(user.getId());
    }
}
