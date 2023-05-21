package com.example.demo.service;

import com.example.demo.entity.Task;
import com.example.demo.entity.TaskDto;
import com.example.demo.entity.User;
import com.example.demo.exception.DataNotFoundException;
import com.example.demo.exception.InvalidUserException;
import com.example.demo.repository.TaskRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private static final String COMPLETADA = "Completada";
    private static final String PENDIENTE = "Pendiente";
    private final TaskRepository taskRepository;
    private final UserService userService;

    public TaskService(TaskRepository taskRepository, UserService userService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
    }

    public List<TaskDto> getAllTask(){
        List<Task> taskList = taskRepository.findAll();
        return taskList
                .stream()
                .map(t -> new TaskDto(t.getId(), t.getTitle(), t.getDescription(), Boolean.TRUE.equals(t.getComplete())?COMPLETADA:PENDIENTE, t.getUser().getEmail() ))
                .sorted(Comparator.comparing(TaskDto::getUsuario))
                .collect(Collectors.toList());
    }

    public List<TaskDto> getAllTaskByUser(String email){

        User user = userService.getUserByEmail(email);
        if(Objects.isNull(user)) {
            throw new InvalidUserException("Usuario "+email+" no existe.");
        }

        List<Task> taskList = taskRepository.findAllByUser(user);
        return taskList
                .stream()
                .map(t -> new TaskDto(t.getId(), t.getTitle(), t.getDescription(), Boolean.TRUE.equals(t.getComplete())?COMPLETADA:PENDIENTE, t.getUser().getEmail() ))
                .sorted(Comparator.comparing(TaskDto::getUsuario))
                .collect(Collectors.toList());
    }

    public void createTask(TaskDto taskDto) {
        User user = userService.getUserByEmail(taskDto.getUsuario());
        if(Objects.isNull(user)) {
            throw new InvalidUserException("No se creo la tarea, el usuario "+taskDto.getUsuario()+" no existe.");
        }

        Task newTask = new Task(taskDto.getTitulo(), taskDto.getDescripcion(), false, user);
        taskRepository.save(newTask);
    }

    public void finishTask(Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(()-> new DataNotFoundException("Tarea "+taskId+" no existe."));
        task.setComplete(true);
        taskRepository.save(task);
    }

    public void finishAllTask(String userEmail) {
        User user = userService.getUserByEmail(userEmail);
        if(Objects.isNull(user)) {
            throw new InvalidUserException("Usuario "+userEmail+" no existe.");
        }

        //Finish all tasks of the user
        List <Task> taskList = taskRepository.findAllByUser(user)
                                .stream().map(t -> { t.setComplete(true);
                                                     return t;
                                                    })
                                .collect(Collectors.toList());

        taskRepository.saveAll(taskList);
    }

    public void deleteTask(Long taskId, String user) {

        //Buscamos la tarea antes de eliminarla y validamos que no sea de un usuario diferente
        Task task = taskRepository.findById(taskId).orElseThrow(()-> new DataNotFoundException("Tarea "+taskId+" no existe."));
        if(!task.getUser().getEmail().equals(user)){
            throw new DataIntegrityViolationException("No puede eliminar actividades de otros usuarios.");
        }

        taskRepository.deleteById(taskId);
    }
}
