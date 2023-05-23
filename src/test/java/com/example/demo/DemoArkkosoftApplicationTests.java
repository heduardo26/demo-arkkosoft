package com.example.demo;

import com.example.demo.entity.Task;
import com.example.demo.entity.User;
import com.example.demo.repository.TaskRepository;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

//@SpringBootTest
@DataJpaTest
class DemoArkkosoftApplicationTests {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TaskRepository taskRepository;

	@Test
	@Order(1)
	void should_create_user() {
		// Given
		User newUser = buildUser();
		// When
		User persistedUser = this.userRepository.save(newUser);
		// Then
		assertNotNull(persistedUser);
		assertEquals(4, persistedUser.getId());
	}

	@Test
	@Order(2)
	void should_create_task(){
		//Given
		Task newTask = new Task();
		newTask.setTitle("Titulo");
		newTask.setDescription("Descripcion");
		newTask.setComplete(false);
		User newUser = buildUser();
		newTask.setUser(newUser);

		//When
		Task persistedTask = taskRepository.save(newTask);

		//Then
		assertNotNull(persistedTask);
		assertEquals(5, persistedTask.getId());
	}

	@Test
	@Order(3)
	void should_find_all_task(){
		//When
		List<Task> taskList = taskRepository.findAll();
		//Then
		assertEquals(4, taskList.size());
	}

	@Test
	@Order(4)
	void should_find_task_by_user(){
		//Given
		User newUser = buildUser();
		this.userRepository.save(newUser);
		//When
		List<Task> taskList = taskRepository.findAllByUser(newUser);
		//Then
		assertEquals(1, taskList.size());

	}


	private User buildUser(){
		User newUser = new User();
		newUser.setUserName("User");
		newUser.setEmail("user@mail.com");
		newUser.setPassword("password");
		return newUser;
	}
}